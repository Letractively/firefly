package com.firefly.mvc.web.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * 保存请求key对应的对象
 *
 * @author alvinqiu
 *
 */
public class BeanHandle {
	// private static Logger log = LoggerFactory.getLogger(BeanHandle.class);
	private final Object object;
	private final Method method;
	private final Class<?>[] paraTypes;
	private final String[] paraClassNames;
	private final ViewHandle viewHandle;

	public BeanHandle(Object object, Method method, ViewHandle viewHandle) {
		super();
		this.object = object;
		this.method = method;
		this.viewHandle = viewHandle;

		paraTypes = method.getParameterTypes();
		paraClassNames = new String[paraTypes.length];
		for (int i = 0; i < paraTypes.length; i++) {
			paraClassNames[i] = paraTypes[i].getName();
		}
	}

	public ViewHandle getViewHandle() {
		return viewHandle;
	}

	public String[] getParaClassNames() {
		return paraClassNames;
	}

	public Object getObject() {
		return object;
	}

	public Method getMethod() {
		return method;
	}

	public Object invoke(Object[] args) {
		Object ret = null;
		try {
			// log.info("method isAccessible [{}]", method.isAccessible());
			ret = method.invoke(object, args);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return ret;
	}
}