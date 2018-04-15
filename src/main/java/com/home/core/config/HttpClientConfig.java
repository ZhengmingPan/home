package com.home.core.config;

import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置Http客户端连接
 * 
 * @author zhengming
 *
 */
@Configuration
public class HttpClientConfig {

	@Value("${httpclient.pool.maxTotal}")
	private Integer maxTotal;

	@Value("${httpclient.pool.defaultMaxPerRoute}")
	private Integer defaultMaxPerRoute;

	@Value("${httpclient.pool.connectTimeout}")
	private Integer connectTimeout;

	@Value("${httpclient.pool.connectionRequestTimeout}")
	private Integer connectionRequestTimeout;

	@Value("${httpclient.pool.socketTimeout}")
	private Integer socketTimeout;

	@Value("${httpclient.pool.staleConnectionCheckEnabled}")
	private Boolean staleConnectionCheckEnabled;

	@Value("${httpclient.pool.retryTime}")
	private Integer retryTime;

	@Value("${httpclient.pool.timeToLive}")
	private Long timeToLive;

	@Value("${httpclient.pool.keepAliveTime}")
	private Integer keepAliveTime;

	/**
	 * 首先实例化一个连接池管理器,设置最大连接数和并发连接数
	 * 
	 * @return
	 */
	@Bean(name = "httpClientConnectionManager")
	public PoolingHttpClientConnectionManager getHttpClientConnectionManager() {
		PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager(
				timeToLive, TimeUnit.SECONDS);
		httpClientConnectionManager.setMaxTotal(maxTotal);
		httpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
		return httpClientConnectionManager;
	}

	/**
	 * 实例化连接池,设置连接池管理器
	 * 
	 * @param httpClientConnectionManager
	 * @return
	 */
	@Bean(name = "httpClientBuilder")
	public HttpClientBuilder getHttpClientBuilder(
			@Qualifier("httpClientConnectionManager") PoolingHttpClientConnectionManager httpClientConnectionManager) {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		httpClientBuilder.setConnectionManager(httpClientConnectionManager);
		return httpClientBuilder;
	}

	/**
	 * 设置请求重试最大次数
	 * 
	 * @return
	 */
	@Bean(name = "httpRequestRetryHandler")
	public HttpRequestRetryHandler httpRequestRetryHandler() {
		return (exception, executionCount, context) -> {
			if (executionCount >= 5) { // Do not retry if over max retry count
				return false;
			}
			if (exception instanceof InterruptedIOException) { // Timeout
				return false;
			}
			if (exception instanceof UnknownHostException) { // Unknown host
				return false;
			}
			if (exception instanceof ConnectTimeoutException) { // Connection refused
				return false;
			}
			if (exception instanceof SSLException) { // SSL handshake exception
				return false;
			}
			HttpClientContext clientContext = HttpClientContext.adapt(context);
			HttpRequest request = clientContext.getRequest();
			boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
			if (idempotent) { // Retry if the request is considered idempotent
				return true;
			}
			return false;
		};
	}

	/**
	 * 连接保持策略
	 * 
	 * @return
	 */
	@Bean("connectionKeepAliveStrategy")
	public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
		return (response, context) -> {
			// Honor 'keep-alive' header
			HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
			while (it.hasNext()) {
				HeaderElement headerElement = it.nextElement();
				String param = headerElement.getName();
				String value = headerElement.getValue();
				if (value != null && param.equalsIgnoreCase("timeout")) {
					try {
						return Long.parseLong(value) * 1000;
					} catch (NumberFormatException ignore) {
					}
				}
			}
			return keepAliveTime * 1000;
		};
	}

	/**
	 * 注入连接池，用于获取httpClient
	 * 
	 * @param httpClientBuilder
	 * @return
	 */
	@Bean
	public CloseableHttpClient getCloseableHttpClient(
			@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder,
			@Qualifier("httpRequestRetryHandler") HttpRequestRetryHandler httpRequestRetryHandler,
			@Qualifier("connectionKeepAliveStrategy") ConnectionKeepAliveStrategy connectionKeepAliveStrategy) {
		return httpClientBuilder.setRetryHandler(httpRequestRetryHandler)
				.setKeepAliveStrategy(connectionKeepAliveStrategy)
				.build();
	}

	/**
	 * Builder是RequestConfig的一个内部类 通过RequestConfig的custom方法来获取到一个Builder对象
	 * 设置builder的连接信息 这里还可以设置proxy，cookieSpec等属性。有需要的话可以在此设置
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@Bean(name = "builder")
	public RequestConfig.Builder getBuilder() {
		RequestConfig.Builder builder = RequestConfig.custom();
		return builder.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectionRequestTimeout)
				.setSocketTimeout(socketTimeout)
				.setStaleConnectionCheckEnabled(staleConnectionCheckEnabled);
	}

	/**
	 * 使用builder构建一个RequestConfig对象
	 * 
	 * @param builder
	 * @return
	 */
	@Bean
	public RequestConfig getRequestConfig(@Qualifier("builder") RequestConfig.Builder builder) {
		return builder.build();
	}
}
