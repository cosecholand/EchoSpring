package org.litespring.test.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.service.v1.PetStoreService;

public class BeanFactoryTest {
	@Test
	public void testGetBean() {
		DefaultBeanFactory factory = new DefaultBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		reader.loadBeanDefinitions("petstore-v1.xml");
		BeanDefinition bd = factory.getBeanDefinition("petStore");
		assertEquals("org.litespring.service.v1.PetStoreService",bd.getBeanClassName());
		PetStoreService petStore = (PetStoreService)factory.getBean("petStore");
		assertNotNull(petStore);
	}
	
	
	
	//测试获取bean异常
	@Test
	public void testInvalidBean(){
		DefaultBeanFactory factory = new DefaultBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		reader.loadBeanDefinitions("petstore-v1.xml");
		try{
			factory.getBean("invalidBean");
		}catch(BeanCreationException e){
			return;
		}
		Assert.fail("expect BeanCreationException ");
	}
	
	
	//测试解析xml异常
	@Test
	public void testInvalidXML(){
		
		try{
			DefaultBeanFactory factory = new DefaultBeanFactory();
			XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
			reader.loadBeanDefinitions("xxx.xml");
		}catch(BeanDefinitionStoreException e){
			return;
		}
		Assert.fail("expect BeanDefinitionStoreException ");
	}	
}
