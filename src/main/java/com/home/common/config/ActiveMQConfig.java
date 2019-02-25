package com.home.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

//@EnableJms
//@Configuration
public class ActiveMQConfig {

	/***************************************************
	 * 客户端接受消息端(jmsListener) *
	 ***************************************************/

	/**
	 * 设置topice模型的监听ConnectionFactory
	 * 
	 * @param connectionFactory
	 * @return
	 */
	@Bean("myJmsContainerFactory")
	public JmsListenerContainerFactory<?> myJmsContainerFactory(ConnectionFactory connectionFactory) {
		SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setPubSubDomain(true);
		factory.setSubscriptionDurable(true);
		return factory;
	}

}
