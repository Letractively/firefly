package com.firefly.core.support;

import java.util.Set;


/**
 * Bean信息
 * @author 杰然不同
 * @date 2010-11-29
 * @Version 1.0
 */
public interface BeanDefinition {
	// id className 以及该组件所有接口类名作为 map 的key
	String getId();

	String getClassName();

	void setId(String id);

	void setClassName(String className);

	Set<String> getInterfaceNames();

	void setInterfaceNames(Set<String> names);

	// 该组件的对象实例
	Object getObject();

	void setObject(Object object);
}
