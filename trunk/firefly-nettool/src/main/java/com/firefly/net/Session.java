package com.firefly.net;

import java.net.InetSocketAddress;

public interface Session {
	int CLOSE = 0;
	int OPEN = 1;
	String CLOSE_FLAG = "firefly-session-close";

	void setAttribute(String key, Object value);

	Object getAttribute(String key);

	void removeAttribute(String key);

	void clearAttributes();

	void fireReceiveMessage(Object message);

	void encode(Object message);

	void write(Object object);

	int getInterestOps();

	int getSessionId();

	long getOpenTime();

	void close(boolean immediately);

	int getState();

	boolean isOpen();

	InetSocketAddress getLocalAddress();

	InetSocketAddress getRemoteAddress();

    void setResult(Object result, long timeout);

    Object getResult(long timeout);
}
