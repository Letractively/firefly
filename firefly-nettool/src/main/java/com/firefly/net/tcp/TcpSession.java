package com.firefly.net.tcp;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import com.firefly.net.Config;
import com.firefly.net.EventType;
import com.firefly.net.Session;
import com.firefly.net.ThreadLocalBoolean;
import com.firefly.net.buffer.SocketSendBufferPool.SendBuffer;

public class TcpSession implements Session {
	private final int sessionId;
	private final SelectionKey selectionKey;
	private long openTime;
	private final TcpWorker worker;
	private final Config config;
	private final Map<String, Object> map = new HashMap<String, Object>();
	private final Runnable writeTask = new WriteTask();
	private final AtomicInteger writeBufferSize = new AtomicInteger();
	private final AtomicInteger highWaterMarkCounter = new AtomicInteger();
	private final AtomicBoolean writeTaskInTaskQueue = new AtomicBoolean();
	private volatile int interestOps = SelectionKey.OP_READ;
	private boolean inWriteNowLoop;
	private boolean writeSuspended;
	private final Object interestOpsLock = new Object();
	private final Object writeLock = new Object();
	private final Queue<ByteBuffer> writeBuffer = new WriteRequestQueue();
	private ByteBuffer currentWrite;
	private SendBuffer currentWriteBuffer;

	public TcpSession(int sessionId, TcpWorker worker, Config config,
			long openTime, SelectionKey selectionKey) {
		super();
		this.sessionId = sessionId;
		this.worker = worker;
		this.config = config;
		this.openTime = openTime;
		this.selectionKey = selectionKey;
	}


	public SendBuffer getCurrentWriteBuffer() {
		return currentWriteBuffer;
	}


	public void setCurrentWriteBuffer(SendBuffer currentWriteBuffer) {
		this.currentWriteBuffer = currentWriteBuffer;
	}


	public void setCurrentWrite(ByteBuffer currentWrite) {
		this.currentWrite = currentWrite;
	}

	public ByteBuffer getCurrentWrite() {
		return currentWrite;
	}

	public Queue<ByteBuffer> getWriteBuffer() {
		return writeBuffer;
	}

	public AtomicBoolean getWriteTaskInTaskQueue() {
		return writeTaskInTaskQueue;
	}

	public Runnable getWriteTask() {
		return writeTask;
	}

	public SelectionKey getSelectionKey() {
		return selectionKey;
	}

	public Object getInterestOpsLock() {
		return interestOpsLock;
	}

	public Object getWriteLock() {
		return writeLock;
	}

	public boolean isInWriteNowLoop() {
		return inWriteNowLoop;
	}

	public void setInWriteNowLoop(boolean inWriteNowLoop) {
		this.inWriteNowLoop = inWriteNowLoop;
	}

	public boolean isWriteSuspended() {
		return writeSuspended;
	}

	public void setWriteSuspended(boolean writeSuspended) {
		this.writeSuspended = writeSuspended;
	}

	public AtomicInteger getWriteBufferSize() {
		return writeBufferSize;
	}

	public AtomicInteger getHighWaterMarkCounter() {
		return highWaterMarkCounter;
	}

	public long getOpenTime() {
		return openTime;
	}

	public int getSessionId() {
		return sessionId;
	}

	@Override
	public void setAttribute(String key, Object value) {
		map.put(key, value);
	}

	@Override
	public Object getAttribute(String key) {
		return map.get(key);
	}

	@Override
	public void removeAttribute(String key) {
		map.remove(key);
	}

	@Override
	public void clearAttributes() {
		map.clear();
	}

	@Override
	public void fireReceiveMessage(Object message) {
		worker.fire(EventType.RECEIVE, this, message, null);
	}

	@Override
	public void encode(Object message) {
		config.getEncoder().encode(message, this);
	}

	@Override
	public void write(ByteBuffer byteBuffer) {
        boolean offered = writeBuffer.offer(byteBuffer);
        assert offered;
        worker.writeFromUserCode(this);
	}

	// @Override
	// public int getInterestOps() {
	// int interestOps = getRawInterestOps();
	// int writeBufferSize = this.writeBufferSize.get();
	// if (writeBufferSize != 0) {
	// if (highWaterMarkCounter.get() > 0) {
	// int lowWaterMark = config.getWriteBufferLowWaterMark();
	// if (writeBufferSize >= lowWaterMark) {
	// interestOps |= SelectionKey.OP_WRITE;
	// } else {
	// interestOps &= ~SelectionKey.OP_WRITE;
	// }
	// } else {
	// int highWaterMark = config.getWriteBufferHighWaterMark();
	// if (writeBufferSize >= highWaterMark) {
	// interestOps |= SelectionKey.OP_WRITE;
	// } else {
	// interestOps &= ~SelectionKey.OP_WRITE;
	// }
	// }
	// } else {
	// interestOps &= ~SelectionKey.OP_WRITE;
	// }
	// return interestOps;
	// }

	@Override
	public int getRawInterestOps() {
		return interestOps;
	}

	@Override
	public void setInterestOpsNow(int interestOps) {
		this.interestOps = interestOps;
	}

	@Override
	public void close() {
		worker.close(selectionKey);
	}

	private final class WriteTask implements Runnable {

		WriteTask() {
			super();
		}

		@Override
		public void run() {
			writeTaskInTaskQueue.set(false);
			worker.writeFromTaskLoop(TcpSession.this);
		}
	}

	private final class WriteRequestQueue extends
			ConcurrentLinkedQueue<ByteBuffer> {
		private static final long serialVersionUID = -2493148252918843163L;
		private final ThreadLocalBoolean notifying = new ThreadLocalBoolean();

		private WriteRequestQueue() {
			super();
		}

		@Override
		public boolean offer(ByteBuffer byteBuffer) {
			boolean success = super.offer(byteBuffer);
			assert success;

			int messageSize = byteBuffer.remaining();
			int newWriteBufferSize = writeBufferSize.addAndGet(messageSize);
			int highWaterMark = config.getWriteBufferHighWaterMark();

			if (newWriteBufferSize >= highWaterMark) {
				if (newWriteBufferSize - messageSize < highWaterMark) {
					highWaterMarkCounter.incrementAndGet();
					if (!notifying.get()) {
						notifying.set(Boolean.TRUE);
						worker.setInterestOps(TcpSession.this,
								SelectionKey.OP_READ);
						notifying.set(Boolean.FALSE);
					}
				}
			}
			return true;
		}

		@Override
		public ByteBuffer poll() {
			ByteBuffer byteBuffer = super.poll();
			if (byteBuffer != null) {
				int messageSize = byteBuffer.remaining();
				int newWriteBufferSize = writeBufferSize
						.addAndGet(-messageSize);
				int lowWaterMark = config.getWriteBufferLowWaterMark();

				if (newWriteBufferSize == 0
						|| newWriteBufferSize < lowWaterMark) {
					if (newWriteBufferSize + messageSize >= lowWaterMark) {
						highWaterMarkCounter.decrementAndGet();
						if (!notifying.get()) {
							notifying.set(Boolean.TRUE);
							worker.setInterestOps(TcpSession.this,
									SelectionKey.OP_READ);
							notifying.set(Boolean.FALSE);
						}
					}
				}
			}
			return byteBuffer;
		}
	}

}