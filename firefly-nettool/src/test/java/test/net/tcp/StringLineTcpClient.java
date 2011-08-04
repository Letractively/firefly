package test.net.tcp;

import com.firefly.net.Client;
import com.firefly.net.Session;
import com.firefly.net.tcp.TcpClient;
import com.firefly.utils.log.LogFactory;

public class StringLineTcpClient {
	public static void main(String[] args) {
		StringLineClientHandler handler = new StringLineClientHandler();
		Client client = new TcpClient(new StringLineDecoder(),
				new StringLineEncoder(), handler);
        Session session = client.connect("localhost", 9900);

		session.encode("hello client");
		String ret = (String)session.getResult(1000);
		System.out.println("receive[" + ret + "]");

		session.encode("test2");
		ret = (String)session.getResult(1000);
		System.out.println("receive[" + ret + "]");

		session.encode("quit");
		ret = (String)session.getResult(1000);
		System.out.println("receive[" + ret + "]");

        session = client.connect("localhost", 9900);

		session.encode("getfile");
        ret = (String)session.getResult(1000);
		System.out.println("receive[" + ret + "]");

        session.close(false);
        client.shutdown();
        LogFactory.getInstance().shutdown();
	}
}