package com.home.core.service;

import java.io.Serializable;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQQueueSender;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.ActiveMQTopicPublisher;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import com.home.core.vo.AmqScheduled;

/**
 * JMS消息发送客户端
 * 
 * @author Administrator
 *
 */
@Component
public class ActiveMQClient {

	private static Logger LOGGER = LoggerFactory.getLogger(ActiveMQClient.class);

	/**
	 * 默认Queue消息的Destinaton
	 */
	private static String DEFAULT_DESTINATION_QUEUE_NAME = "queue.default";
	private static String DEFAULT_DESTINATION_TOPIC_NAME = "topic.default";

	@Autowired
	private JmsMessagingTemplate jmsTemplate;

	/**
	 * P2P（Point to Point）模型 <br>
	 * 生产者向默认队列发送消息
	 * 
	 * @param message
	 *            消息内容
	 */
	public void sendByP2P(Serializable message) {
		sendByP2P(DEFAULT_DESTINATION_QUEUE_NAME, message);
	}

	/**
	 * P2P（Point to Point）模型 <br>
	 * 延时生产者向默认Queue发送消息
	 * 
	 * @param destinationString
	 *            发送的目的队列
	 * @param message
	 *            消息内容
	 * @throws JMSException
	 */
	public void sendByP2P(Serializable message, AmqScheduled scheduled) throws JMSException {
		sendByP2P(DEFAULT_DESTINATION_QUEUE_NAME, message, scheduled);
	}

	/**
	 * P2P（Point to Point）模型 <br>
	 * 生产者向特定Queue发送消息
	 * 
	 * @param destinationString
	 *            发送的目的队列
	 * @param message
	 *            消息内容
	 */
	public void sendByP2P(String destinationName, Serializable message) {
		ActiveMQQueue destination = new ActiveMQQueue(destinationName);
		send(destination, message);
	}

	/**
	 * P2P（Point to Point）模型 <br>
	 * 延时生产者向特定Queue发送消息
	 * 
	 * @param destinationString
	 *            发送的目的队列
	 * @param message
	 *            消息内容
	 * @param scheduled
	 *            延时时间单位：milliSeconds
	 * @throws JMSException
	 */
	public void sendByP2P(String destinationName, Serializable message, AmqScheduled scheduled) throws JMSException {
		ActiveMQQueue destination = new ActiveMQQueue(destinationName);
		send(destination, message, scheduled);
	}

	/**
	 * Publish/Subscribe(Pub/Sub) 模型 <br>
	 * 发布者向默认Topic发布消息
	 * 
	 * @param message
	 *            消息内容
	 */
	public void publishByPubSub(Serializable message) {
		publishByPubSub(DEFAULT_DESTINATION_TOPIC_NAME, message);
	}

	/**
	 * Publish/Subscribe(Pub/Sub) 模型 <br>
	 * 延时发布者向默认Topic发布消息
	 * 
	 * @param destinationString
	 *            发送的目的队列
	 * @param message
	 *            消息内容
	 * @param scheduled
	 *            延时时间单位：milliSeconds
	 * @throws JMSException
	 */
	public void publishByPubSub(Serializable message, AmqScheduled scheduled) throws JMSException {
		publishByPubSub(DEFAULT_DESTINATION_TOPIC_NAME, message, scheduled);
	}

	/**
	 * Publish/Subscribe(Pub/Sub) 模型 <br>
	 * 立即发布者向特定发布类型消息
	 * 
	 * @param destinationString
	 *            发送的目的队列
	 * @param message
	 *            消息内容
	 */
	public void publishByPubSub(String destinationName, Serializable message) {
		ActiveMQTopic destination = new ActiveMQTopic(destinationName);
		send(destination, message);
	}

	/**
	 * Publish/Subscribe(Pub/Sub) 模型 <br>
	 * 延时发布者向特定Topic发布消息
	 * 
	 * @param destinationString
	 *            发送的目的队列
	 * @param message
	 *            消息内容
	 * @param scheduled
	 *            延时时间单位：milliSeconds
	 * @throws JMSException
	 */
	public void publishByPubSub(String destinationName, Serializable message, AmqScheduled scheduled) throws JMSException {
		ActiveMQTopic destination = new ActiveMQTopic(destinationName);
		send(destination, message, scheduled);
	}

	/**
	 * 即时发送消息/发布
	 * 
	 * @param destination
	 *            发送的目的队列
	 * @param message
	 *            消息内容
	 */
	private void send(ActiveMQDestination destination, Serializable message) {
		try {
			jmsTemplate.convertAndSend(destination, message);
			LOGGER.info("JMS消息发送成功！！ 内容：" + message);
		} catch (Exception e) {
			LOGGER.error("JMS即时发送消息/发布异常," + e.getMessage());
		}
	}

	/**
	 * 延时发送/发布
	 * 
	 * @param destination
	 *            消息生产者的消息发送目标或者说消息消费者的消息来源
	 * @param message
	 *            消息内容
	 * @param scheduled
	 *            消息定时发送的配置信息
	 * @throws JMSException
	 */
	private void send(ActiveMQDestination destination, Serializable message, AmqScheduled scheduled) throws JMSException {
		try {
			// 获取连接工厂
			ActiveMQConnectionFactory connectionFactory = (ActiveMQConnectionFactory) jmsTemplate.getConnectionFactory();
			// 获取连接
			ActiveMQConnection connection = (ActiveMQConnection) connectionFactory.createConnection();
			connection.start();
			// 获取session
			ActiveMQSession session = (ActiveMQSession) connection.createSession(Boolean.TRUE, ActiveMQSession.AUTO_ACKNOWLEDGE);
			commitDetination(session, destination, message, scheduled);
			session.close();
			connection.close();
		} catch (JMSException e) {
			LOGGER.error("JMS_ActiveMQ连接失败" + e.getMessage());
		}
	}

	/**
	 * 根据消息模式判断决定采用哪种延时发布
	 * 
	 * @param session
	 *            操作消息的接口，提供了事务的功能
	 * @param destination
	 *            消息生产者的消息发送目标或者说消息消费者的消息来源
	 * @param message
	 *            消息内容
	 * @param scheduled
	 *            消息定时发送的配置信息
	 * @throws JMSException
	 */
	private void commitDetination(ActiveMQSession session, ActiveMQDestination destination, Serializable message, AmqScheduled scheduled) throws JMSException {
		String destinationType = destination.getDestinationTypeAsString();
		if ("Queue".equals(destinationType)) {
			commitQueue(session, destination, message, scheduled);
		} else if ("Topic".equals(destinationType)) {
			commitTopic(session, destination, message, scheduled);
		}
	}

	/**
	 * P2P模式延时发送
	 * 
	 * @param session
	 *            操作消息的接口，提供了事务的功能
	 * @param destination
	 *            消息生产者的消息发送目标或者说消息消费者的消息来源
	 * @param message
	 *            消息内容
	 * @param scheduled
	 *            消息定时发送的配置信息
	 * @throws JMSException
	 */
	private void commitQueue(ActiveMQSession session, ActiveMQDestination destination, Serializable message, AmqScheduled scheduled) throws JMSException {
		try {
			ActiveMQQueue queue = (ActiveMQQueue) session.createQueue(destination.getPhysicalName());
			ActiveMQQueueSender sender = (ActiveMQQueueSender) session.createSender(queue);
			sender.setDeliveryMode(DeliveryMode.PERSISTENT);
			ActiveMQObjectMessage objMessage = (ActiveMQObjectMessage) session.createObjectMessage(message);
			// 设置延迟时间
			objMessage.setProperties(scheduled.getPropertiesMap());
			// 发送 
			sender.send(objMessage);
			session.commit();
			sender.close();
			LOGGER.info("JMS定時发送成功！！ 内容：" + message);
		} catch (JMSException e) {
			LOGGER.error("Queue延时发送," + e.getMessage());
		}
	}

	/**
	 * Pub/Sub模式延时发布
	 * 
	 * @param session
	 *            操作消息的接口，提供了事务的功能
	 * @param destination
	 *            消息生产者的消息发送目标或者说消息消费者的消息来源
	 * @param message
	 *            消息内容
	 * @param scheduled
	 *            消息定时发送的配置信息
	 * @throws JMSException
	 */
	private void commitTopic(ActiveMQSession session, ActiveMQDestination destination, Serializable message, AmqScheduled scheduled) {
		try {
			ActiveMQTopic topic = (ActiveMQTopic) session.createTopic(destination.getPhysicalName());
			ActiveMQTopicPublisher publisher = (ActiveMQTopicPublisher) session.createPublisher(topic);
			publisher.setDeliveryMode(DeliveryMode.PERSISTENT);
			ActiveMQObjectMessage objMessage = (ActiveMQObjectMessage) session.createObjectMessage(message);
			// 设置延迟时间
			objMessage.setProperties(scheduled.getPropertiesMap());
			// 发送 
			publisher.publish(objMessage);
			session.commit();
			publisher.close();
			LOGGER.info("JMS定時发送成功！！ 内容：" + message);
		} catch (JMSException e) {
			LOGGER.error("Topic延时发布失败," + e.getMessage());
		}
	}

}
