package com.firefly.template;

import com.firefly.utils.log.Log;
import com.firefly.utils.log.LogFactory;

public class Config {
	public static Log LOG = LogFactory.getInstance().getLog("firefly-system");
	private String viewPath;
	private String compiledPath;
	private String suffix;
	private boolean compress;
	public static final String COMPILED_FOLDER_NAME = "_compiled_view";

	public String getViewPath() {
		return viewPath;
	}

	public void setViewPath(String viewPath) {
		this.viewPath = viewPath;
		if (viewPath.charAt(viewPath.length() - 1) == '/')
			compiledPath = viewPath + COMPILED_FOLDER_NAME;
		else
			compiledPath = viewPath + "/" + COMPILED_FOLDER_NAME;
	}

	public String getCompiledPath() {
		return compiledPath;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public boolean isCompress() {
		return compress;
	}

	public void setCompress(boolean compress) {
		this.compress = compress;
	}
	
	

}
