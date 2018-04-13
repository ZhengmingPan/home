package com.home.core.config;

import javax.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;

@EnableJms
@Configuration
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
	@Primary
	public JmsListenerContainerFactory<?> myJmsContainerFactory(ConnectionFactory connectionFactory) {
		SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setPubSubDomain(true);
		factory.setSubscriptionDurable(true);
		return factory;
	}

	@Bean
	@Primary
	public CachingConnectionFactory connectionFactory(ConnectionFactory connectionFactory) {
		CachingConnectionFactory factory = new CachingConnectionFactory();
		factory.setTargetConnectionFactory(connectionFactory);
		factory.setSessionCacheSize(100);
		return factory;

	}

}
