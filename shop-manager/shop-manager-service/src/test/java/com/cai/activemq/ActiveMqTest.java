package com.cai.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
//import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

/**
 * 
 * @ClassName: ActiveMqTest
 * @Description: activemq测试类
 * @author Vergil
 * @date 2018年11月10日
 *
 */
public class ActiveMqTest {

	/**
	 * 
	 * @Title:testQueueProducer
	 * @Description:点到点
	 * @param:
	 * @return: void
	 * @throws Exception
	 */
	@Test
	public void testQueueProducer() throws Exception {
		// 1、创建一个连接工厂对象,需要指定服务的ip端口
		ConnectionFactory mqConnectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
		// 2、使用工厂对象创建一个connection对象
		Connection connection = mqConnectionFactory.createConnection();
		// 3、开启连接,调用connection对象start方法
		connection.start();
		// 4、创建一个session对象
		// 第一个参数:是否开启事务.如果是true开启事务,第二个参数无意义。一般不开启事务
		// 第二个参数:应答模式。自动应答或者手动应答,一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5、使用session对象创建一个destination对象,两种形式queue,topic,现在应该使用queue
		Queue queue = session.createQueue("test-queue");
		// 6、使用session对象创建一个producer对象
		MessageProducer producer = session.createProducer(queue);
		// 7、创建一个message对象,可以使用textmessage
		/*
		 * TextMessage textMessage = new ActiveMQTextMessage();
		 * textMessage.setText("hello activeMQ");
		 */
		TextMessage message = session.createTextMessage("hello activeMQ");
		// 8、发送信息
		producer.send(message);
		// 9、关闭资源
		producer.close();
		session.close();
		connection.close();
	}

	/**
	 * 
	 * @Title: testQueueConsumer
	 * @Description: 接收消息
	 * @param:
	 * @throws: Exception
	 * @return: void
	 */
	@Test
	public void testQueueConsumer() throws Exception {
		// 创建一个ConnectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
		// 创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 创建一个destination对象,queue
		Queue queue = session.createQueue("test-queue");
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

	/**
	 * @Title: testTopicProducer @Description:topicProducer @param: @return
	 *         void @throws
	 */
	@Test
	public void testTopicProducer() throws Exception {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic("test-topic");
		TextMessage textMessage = session.createTextMessage("hello topicConsumer");
		MessageProducer producer = session.createProducer(topic);
		producer.send(textMessage);
		producer.close();
		session.close();
		connection.close();
	}

	/**
	 * 
	 * @Title: testTopicConsumer @Description: topicConsumer1 @param: @throws
	 *         Exception @return: void @throws
	 */
	@Test
	public void testTopicConsumer() throws Exception {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic("test-topic");
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {
					String text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("topicConsumer1");
		System.in.read();
		consumer.close();
		session.close();
		connection.close();
	}

	/**
	 * 
	 * @Title: testTopicConsumer @Description: topicConsumer2 @param: @throws
	 *         Exception @return: void @throws
	 */
	@Test
	public void testTopicConsumer2() throws Exception {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic("test-topic");
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {
					String text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("topicConsumer2");
		System.in.read();
		consumer.close();
		session.close();
		connection.close();
	}

	/**
	 * 
	 * @Title: testTopicConsumer @Description: topicConsumer3 @param: @throws
	 *         Exception @return: void @throws
	 */
	@Test
	public void testTopicConsumer3() throws Exception {
		System.out.println("topicConsumer3");
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic("test-topic");
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {
					String text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.in.read();
		consumer.close();
		session.close();
		connection.close();
	}
}
