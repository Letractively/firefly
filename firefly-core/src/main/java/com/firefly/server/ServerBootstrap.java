package com.firefly.server;

import com.firefly.mvc.web.AnnotationWebContext;
import com.firefly.mvc.web.servlet.HttpServletDispatcherController;
import com.firefly.net.Server;
import com.firefly.net.tcp.TcpServer;
import com.firefly.server.http.Config;
import com.firefly.server.http.HttpDecoder;
import com.firefly.server.http.HttpEncoder;
import com.firefly.server.http.HttpHandler;
import com.firefly.utils.log.Log;
import com.firefly.utils.log.LogFactory;

public class ServerBootstrap {
	private static Log log = LogFactory.getInstance().getLog("firefly-system");

	public static void start(String serverHome, String host, int port) {
		Config config = new Config(serverHome, host, port);
		start(config);
	}

	public static void start(Config config) {
		long start = System.currentTimeMillis();
		AnnotationWebContext context = new AnnotationWebContext(
				"firefly-server.xml", config.getServerHome());
		HttpServletDispatcherController controller = HttpServletDispatcherController
				.getInstance().init(context);
		
		config.setEncoding(context.getEncoding());
		
		Server server = new TcpServer(new HttpDecoder(config),
				new HttpEncoder(), new HttpHandler(controller, config));
		server.start(config.getHost(), config.getPort());
		long end = System.currentTimeMillis();
		log.info("firefly startup in {} ms", (end - start));
	}
}
