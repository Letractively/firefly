package com.test.sample.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firefly.annotation.Interceptor;

@Interceptor(uri = "/itest*")
public class HelloInterceptor {
	private static Logger log = LoggerFactory.getLogger(HelloInterceptor.class);

	public void before(HttpServletRequest request, HttpServletResponse response) {
		log.info("before 0 [{}]", request.getRequestURI());
	}

	public String after(HttpServletRequest request, HttpServletResponse response) {
		log.info("after 0 [{}]", request.getRequestURI());
		return null;
	}
}
