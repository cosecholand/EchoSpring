package org.litespring.beans.factory.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.utils.ClassUtils;

public class DefaultBeanFactory implements BeanFactory {
	private static final String ID_ATTRIBUTE = "id";
	private static final String CLASS_ATTRIBUTE = "class";
	private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(64);
	public DefaultBeanFactory(String confFile) {
		loadBeanDefinition(confFile);
	}
	
	/**
	 * 加载并解析xml文件
	 * @param confFile
	 */
	private void loadBeanDefinition(String confFile) {
		InputStream is = null;
		try {
		ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
		is = classLoader.getResourceAsStream(confFile);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(is);
		
		Element element = doc.getRootElement();
		Iterator iter = element.elementIterator();
		while(iter.hasNext()){
			Element ele = (Element)iter.next();
			String id = ele.attributeValue(ID_ATTRIBUTE);
			String beanClassName = ele.attributeValue(CLASS_ATTRIBUTE);
			BeanDefinition bd = new GenericBeanDefinition(id,beanClassName);
			this.beanDefinitionMap.put(id, bd);
		}
		
		} catch (DocumentException e) {
			throw new BeanDefinitionStoreException("IOException parsing XML document",e);
		}finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public BeanDefinition getBeanDefinition(String beanId) {
		return this.beanDefinitionMap.get(beanId);
	}

	public Object getBean(String beanId) {
		BeanDefinition bd = this.beanDefinitionMap.get(beanId);
		if(bd==null){
			return null;
		}
		ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
		String beanClassName = bd.getBeanClassName();
		try {
			Class<?> clz = classLoader.loadClass(beanClassName);
			return clz.newInstance();
		} catch (Exception e) {
			throw new BeanCreationException("Create Bean for "+beanClassName+" faild,",e);
		}
	}

}
