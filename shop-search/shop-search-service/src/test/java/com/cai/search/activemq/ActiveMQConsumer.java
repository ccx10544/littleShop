package com.cai.search.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ActiveMQConsumer {
	@SuppressWarnings({ "unused", "resource" })
	@Test
	public void consumer() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-activemq.xml");
		System.in.read();
	}
}
