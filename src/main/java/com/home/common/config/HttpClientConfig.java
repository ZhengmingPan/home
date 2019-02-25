package com.home.common.config;

import com.home.common.config.properties.HttpClientProperties;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * 配置Http客户端连接
 * 
 * @author zhengming
 *
 */
@Configuration
public class HttpClientConfig {

	@Autowired
	private HttpClientProperties httpProp;

	/**
	 * 首先实例化一个连接池管理器,设置最大连接数和并发连接数
	 * 
	 * @return
	 */
	@Bean(name = "httpClientConnectionManager")
	public PoolingHttpClientConnectionManager getHttpClientConnectionManager() {
		PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager(
				httpProp.getTimeToLive(), TimeUnit.SECONDS);
		httpClientConnectionManager.setMaxTotal(httpProp.getMaxTotal());
		httpClientConnectionManager.setDefaultMaxPerRoute(httpProp.getDefaultMaxPerRoute());
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
			return httpProp.getKeepAliveTime() * 1000;
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
		return builder.setConnectTimeout(httpProp.getConnectTimeout())
				.setConnectionRequestTimeout(httpProp.getConnectionRequestTimeout())
				.setSocketTimeout(httpProp.getSocketTimeout())
				.setStaleConnectionCheckEnabled(httpProp.getStaleConnectionCheckEnabled());
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
