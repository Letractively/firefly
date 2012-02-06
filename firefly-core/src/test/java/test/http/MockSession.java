package test.http;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.firefly.net.Session;

public class MockSession implements Session {
	
	Map<String, Object> map = new HashMap<String, Object>();

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
		// TODO Auto-generated method stub

	}

	@Override
	public void encode(Object message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getInterestOps() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSessionId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getOpenTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLastReadTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLastWrittenTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLastActiveTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getReadBytes() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getWrittenBytes() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void close(boolean immediately) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getState() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public InetSocketAddress getLocalAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		// TODO Auto-generated method stub
		return null;
	}

}
