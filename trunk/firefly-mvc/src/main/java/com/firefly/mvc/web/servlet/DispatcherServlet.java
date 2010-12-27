package com.firefly.mvc.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firefly.mvc.web.FontController;
import com.firefly.mvc.web.impl.FrontControllerImpl;

/**
 * mvc前端控制器Servlet
 *
 * @author alvinqiu
 *
 */
public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = -3638120056786910984L;
	private static Logger log = LoggerFactory
			.getLogger(DispatcherServlet.class);
	private static final String INIT_PARAM = "contextConfigLocation";
	private static final String DEFAULT_CONFIG = "firefly_mvc.properties";
	private FontController fontController;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processDispatcher(request, response);

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processDispatcher(request, response);
	}

	@Override
	public void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processDispatcher(request, response);

	}

	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processDispatcher(request, response);
	}

	protected void processDispatcher(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		fontController.dispatcher(request, response);
	}

	@Override
	public void init() {
		fontController = FrontControllerImpl.getInstance();
		String initParam = this.getInitParameter(INIT_PARAM);
		if (initParam == null)
			initParam = DEFAULT_CONFIG;
		log.info("initParam [{}]", initParam);
		fontController.load(initParam);
	}

}
