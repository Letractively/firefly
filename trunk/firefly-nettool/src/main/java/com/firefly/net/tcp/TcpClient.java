package com.firefly.net.tcp;

import com.firefly.net.*;
import com.firefly.utils.log.Log;
import com.firefly.utils.log.LogFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class TcpClient implements Client {

	private static Log log = LogFactory.getInstance().getLog("firefly-system");
    private Config config;
    private Worker[] workers;
    private AtomicInteger sessionId = new AtomicInteger(0);
    private volatile boolean started = false;
    private Synchronizer<Session> synchronizer = new Synchronizer<Session>();

    public TcpClient() {
    }

    public TcpClient(Decoder decoder, Encoder encoder, Handler handler) {
        this();
        config = new Config();
        config.setDecoder(decoder);
        config.setEncoder(encoder);
        config.setHandler(handler);
        config.setHandleThreads(-1);
        config.setWorkerThreads(Runtime.getRuntime().availableProcessors() / 2);
    }

    private synchronized Client init() {
        if (started)
            return this;

        if (config == null)
            throw new IllegalArgumentException("init error config is null");

        log.info("client init");
        workers = new Worker[config.getWorkerThreads()];
        for (int i = 0; i < config.getWorkerThreads(); i++) {
            workers[i] = new TcpWorker(config, i, synchronizer);
        }
        started = true;
        return this;
    }

    @Override
    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public Session connect(String host, int port) {
        if (!started)
            init();

        int id = sessionId.getAndIncrement();
        synchronizer.reset(id);
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.socket().connect(new InetSocketAddress(host, port), config.getTimeout());
            accept(socketChannel, id);
        } catch (IOException e) {
            log.error("connect error", e);
        }
        return synchronizer.get(id);
    }

    private void accept(SocketChannel socketChannel, int sessionId) {
        try {
            int workerIndex = Math.abs(sessionId) % workers.length;
            log.debug("accept sessionId [{}] and worker index [{}]",
                    sessionId, workerIndex);
            workers[workerIndex].registerSelectableChannel(socketChannel,
                    sessionId);
        } catch (Exception e) {
            log.error("Failed to initialize an accepted socket.", e);
            try {
                socketChannel.close();
            } catch (IOException e1) {
                log.error("Failed to close a partially accepted socket.",
                        e1);
            }
        }
    }

	@Override
	public void shutdown() {
		for(Worker worker : workers) {
			worker.shutdown();
		}
	}

}
