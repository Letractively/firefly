package com.firefly.server.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.firefly.net.Session;

public class NetBufferedOutputStream extends OutputStream {

	protected byte[] buf;
	protected int count;
	protected Session session;
	protected int bufferSize;
	protected boolean keepAlive;

	public NetBufferedOutputStream(Session session, int bufferSize,
			boolean keepAlive) {
		this.session = session;
		this.bufferSize = bufferSize;
		this.keepAlive = keepAlive;
	}

	@Override
	public void write(int b) throws IOException {
		if (count >= buf.length)
			flush();

		buf[count++] = (byte) b;
	}

	@Override
	public void write(byte b[], int off, int len) throws IOException {
		if (len >= buf.length) {
			flush();
			session.write(ByteBuffer.wrap(b, off, len));
			return;
		}
		if (len > buf.length - count)
			flush();

		System.arraycopy(b, off, buf, count, len);
		count += len;
	}

	@Override
	public void flush() throws IOException {
		if (count > 0) {
			session.write(ByteBuffer.wrap(buf, 0, count));
			count = 0;
		}
	}

	@Override
	public void close() throws IOException {
		flush();
		if (!keepAlive)
			session.close(false);
	}

}
