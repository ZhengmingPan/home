package com.home.common.config;

import com.home.core.web.HandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;

/**
 * websocket消息实时发送
 * 
 * @author Administrator
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	/**
	 * 输入通道参数配置
	 */
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.taskExecutor().corePoolSize(4) // 设置消息输入通道的线程池数
				.maxPoolSize(8) // 最大线程数
				.keepAliveSeconds(60); // 线程存活时间
	}

	/**
	 * 输出通道参数配置
	 */
	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {
		registration.taskExecutor().corePoolSize(4).maxPoolSize(8);
	}

	/**
	 * 配置Broker
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic"); // (群发， 指定用户) 设置可以订阅的地址，也就是服务器可以发送的地址
		/*registry.setApplicationDestinationPrefixes("/app/");
		registry.setUserDestinationPrefix("/user/");*/
	}

	@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		return true;
	}

	/**
	 * 消息传输参数配置
	 */
	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
		registry.setMessageSizeLimit(8192) // 设置消息字节数大小
				.setSendBufferSizeLimit(8192) // 设置消息缓存大小
				.setSendTimeLimit(10000); // 设置消息发送时间限制毫秒
	}

	/**
	 * 服务器要监听的端口， message从这里进来，加上Handler<br>
	 * 这前台可以通过websocket连接上服务
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/homeWs") // 设置断点
				.setAllowedOrigins("*") // 匹配前端域名
				.addInterceptors(new HandshakeInterceptor()).withSockJS();
	}

}
