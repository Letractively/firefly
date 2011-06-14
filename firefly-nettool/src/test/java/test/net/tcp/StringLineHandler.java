package test.net.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firefly.net.Handler;
import com.firefly.net.Session;

public class StringLineHandler implements Handler {
	private static Logger log = LoggerFactory.getLogger(StringLineHandler.class);
	
	@Override
	public void sessionOpened(Session session) {
		log.info("session open |" + session.getSessionId());
		log.info("local: " + session.getLocalAddress());
		log.info("remote: " + session.getRemoteAddress());
	}

	@Override
	public void sessionClosed(Session session) {
		log.info("session close|" + session.getSessionId());
	}

	@Override
	public void messageRecieved(Session session, Object message) {
		String str = (String) message;
		if (str.equals("quit")) {
			session.encode("bye!");
			session.close(false);
		} else {
			log.debug("recive: " + str);
			session.encode(message);
		}
	}

	@Override
	public void exceptionCaught(Session session, Throwable t) {
		log.error( t.getMessage() + "|" + session.getSessionId());
	}

}