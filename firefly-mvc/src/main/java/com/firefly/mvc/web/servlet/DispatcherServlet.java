package com.firefly.mvc.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firefly.mvc.web.DispatcherController;

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
	private DispatcherController dispatcherController;

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
			HttpServletResponse response) {
		dispatcherController.dispatcher(request, response);
	}

	@Override
	public void init() {
		dispatcherController = HttpServletDispatcherController.getInstance();
		String initParam = this.getInitParameter(INIT_PARAM);
		log.info("initParam [{}]", initParam);
		dispatcherController.init(initParam);
	}

}
