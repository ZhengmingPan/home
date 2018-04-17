package com.home.core.utils;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * HttpClient 远程调用工具类
 * 
 * @author Administrator
 *
 */
@Component
public class HttpClient {

	private static Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

	@Autowired
	private CloseableHttpClient httpClient;
	@Autowired
	private RequestConfig requestConfig;

	public String doGet(String url) {
		HttpGet httpGet = new HttpGet(url); 
		try {   
			httpGet.setConfig(requestConfig);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			String entity = EntityUtils.toString(response.getEntity());
			LOGGER.info("通信成功：" + entity);
			response.close();
			return entity;
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void demo() {
		doGet("http://www.datamm.com/api/variety/page");
	}

}
