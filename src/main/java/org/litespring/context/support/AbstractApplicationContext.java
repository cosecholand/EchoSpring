package org.litespring.context.support;

import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.context.ApplicationContext;
import org.litespring.core.io.Resource;

public abstract class AbstractApplicationContext  implements ApplicationContext {
	private DefaultBeanFactory factory = null;
	public AbstractApplicationContext(String configFile) {
		factory = new DefaultBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);	
		Resource resource = this.getResourceByPath(configFile);
		reader.loadBeanDefinitions(resource);
	}
	public Object getBean(String beanId) {
		return factory.getBean(beanId);
	}
	
	protected abstract Resource getResourceByPath(String path);
}
