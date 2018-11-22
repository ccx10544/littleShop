package com.cai.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * @ClassName:ActiveMqSpringTest
 * @Description:ActiveMQ整合Spring
 * @author Vergil
 * @date 2018年11月10日 下午11:18:41
 * @version V1.0
 * @Copyright:2018 公司名 Inc. All rights reserved
 */
public class ActiveMqSpringTest {
	@SuppressWarnings("resource")
	@Test
	public void sendMessage() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-activemq.xml");
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		ActiveMQQueue mqQueue = applicationContext.getBean(ActiveMQQueue.class);
		jmsTemplate.send(mqQueue, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage("send activemq message");
				return message;
			}
		});
	}

	@Test
	public void getMessage() throws Exception {
		// 创建一个ConnectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
		// 创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 创建一个destination对象,queue
		Queue queue = session.createQueue("spring-queue");
		// 使用Session对象创建一个消费者对象
		MessageConsumer messageConsumer = session.createConsumer(queue);
		// 接收对象
		messageConsumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				// 打印结果
				TextMessage textMessage = (TextMessage) message;
				try {
					String text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		// 等待接收消息
		System.in.read();
		// 关闭资源
		messageConsumer.close();
		session.close();
		connection.close();
	}
}
