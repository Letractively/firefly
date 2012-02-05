package com.firefly.server.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HttpServletRequestImpl implements HttpServletRequest {
	int status, contentLength, serverPort, remotePort, localPort;
	String method, requestURI, queryString, characterEncoding, contentType,
			protocol, serverName, remoteAddr, remoteHost, localName, localAddr;
	PipedInputStream pipedInputStream = new PipedInputStream();
	Cookie[] cookies;
	Map<String, String> headMap = new HashMap<String, String>();

	private Map<String, Object> parameterMap = new HashMap<String, Object>(),
			attributeMap = new HashMap<String, Object>();
	private ServletInputStream servletInputStream = new ServletInputStream() {

		@Override
		public int read() throws IOException {
			return pipedInputStream.read();
		}

		@Override
		public int available() throws IOException {
			return pipedInputStream.available();
		}

		@Override
		public void close() throws IOException {
			pipedInputStream.close();
		}

		public int read(byte[] b, int off, int len) throws IOException {
			return pipedInputStream.read(b, off, len);
		}
	};

	@Override
	public Object getAttribute(String name) {
		return attributeMap.get(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return new Enumeration<String>() {
			private Iterator<String> iterator = attributeMap.keySet()
					.iterator();

			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public String nextElement() {
				return iterator.next();
			}
		};
	}

	@Override
	public String getCharacterEncoding() {
		return characterEncoding;
	}

	@Override
	public void setCharacterEncoding(String characterEncoding)
			throws UnsupportedEncodingException {
		this.characterEncoding = characterEncoding;
	}

	@Override
	public int getContentLength() {
		return contentLength;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return servletInputStream;
	}

	@Override
	public String getParameter(String name) {
		return (String) parameterMap.get(name);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return new Enumeration<String>() {
			private Iterator<String> iterator = parameterMap.keySet()
					.iterator();

			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public String nextElement() {
				return iterator.next();
			}
		};
	}

	@Override
	public String[] getParameterValues(String name) {
		return (String[]) parameterMap.get(name);
	}

	@Override
	public Map<String, Object> getParameterMap() {
		return parameterMap;
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public String getScheme() {
		return "http";
	}

	/**
	 *@return 服务器绑定的ip或者域名
	 */
	@Override
	public String getServerName() {
		return serverName;
	}

	/**
	 *@return 服务器监听的端口
	 */
	@Override
	public int getServerPort() {
		return serverPort;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteAddr() {
		return remoteAddr;
	}

	@Override
	public String getRemoteHost() {
		return remoteHost;
	}

	@Override
	public void setAttribute(String name, Object o) {
		attributeMap.put(name, o);
	}

	@Override
	public void removeAttribute(String name) {
		attributeMap.remove(name);
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<?> getLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRealPath(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRemotePort() {
		return remotePort;
	}

	/**
	 * @return 接收请求的ip或域名
	 */
	@Override
	public String getLocalName() {
		return localName;
	}

	/**
	 * @return 接收请求的ip
	 */
	@Override
	public String getLocalAddr() {
		return localAddr;
	}

	/**
	 * @return 接收请求的端口
	 */
	@Override
	public int getLocalPort() {
		return localPort;
	}

	@Override
	public String getAuthType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cookie[] getCookies() {
		return cookies;
	}

	@Override
	public long getDateHeader(String name) {
		return Long.parseLong(headMap.get(name));
	}

	@Override
	public String getHeader(String name) {
		return headMap.get(name);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		return new Enumeration<String>() {
			private Iterator<String> iterator = headMap.keySet()
					.iterator();

			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public String nextElement() {
				return iterator.next();
			}
		};
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIntHeader(String name) {
		return Integer.parseInt(headMap.get(name));
	}

	@Override
	public String getMethod() {
		return method;
	}

	@Override
	public String getPathInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQueryString() {
		return queryString;
	}

	@Override
	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUserInRole(String role) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestURI() {
		return requestURI;
	}

	@Override
	public StringBuffer getRequestURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession(boolean create) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return isRequestedSessionIdFromURL();
	}

}