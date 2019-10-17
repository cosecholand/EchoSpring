package org.litespring.beans.factory.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.core.io.Resource;
import org.litespring.utils.ClassUtils;

public class XmlBeanDefinitionReader {
	
	public static final String ID_ATTRIBUTE = "id";	

	public static final String CLASS_ATTRIBUTE = "class";
	BeanDefinitionRegistry registry;
	
	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry){
		this.registry = registry;
	}
	
	
	
	/**
	 * 加载并解析xml文件
	 * @param confFile
	 */
	public void loadBeanDefinitions(Resource resource) {
		InputStream is = null;
		try {
			is = resource.getInputStream();
			SAXReader reader = new SAXReader();
			Document doc = reader.read(is);
			
			Element element = doc.getRootElement();
			Iterator iter = element.elementIterator();
			while(iter.hasNext()){
				Element ele = (Element)iter.next();
				String id = ele.attributeValue(ID_ATTRIBUTE);
				String beanClassName = ele.attributeValue(CLASS_ATTRIBUTE);
				BeanDefinition bd = new GenericBeanDefinition(id,beanClassName);
				this.registry.registerBeanDefinition(id, bd);
			}
		
		} catch (Exception e) {
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
}

