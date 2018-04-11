package com.home.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service("producer")
public class ActiveMQClient {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ActiveMQClient.class);


	public static final String QUEUE_NAME_TEST = "test.queue";
	public static final String QUEUE_NAME_OUT = "out.queue";

	@Autowired
	private JmsMessagingTemplate jmsTemplate;

	/**
	 * 发送消息
	 * 
	 * @param destination
	 *            发送的目的队列
	 * @param message
	 *            消息内容
	 */
	public void sendMessage(String queueName, String message) {
		jmsTemplate.convertAndSend(queueName, message);
	}
	

	@JmsListener(destination = ActiveMQClient.QUEUE_NAME_TEST)
	@SendTo(QUEUE_NAME_OUT)
	public String receiveQueue(String message) { 
		LOGGER.info(ActiveMQClient.QUEUE_NAME_TEST + "收到的报文：" + message);
		return ActiveMQClient.QUEUE_NAME_TEST + "收到的报文：" + message;
	}

}
