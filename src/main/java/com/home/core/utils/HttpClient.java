package com.home.core.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

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

	public String doGetString(String url) throws ParseException, IOException {
		HttpEntity httpEntity = doGet(url, new HashMap<String, String>(), new HashMap<String, String>(), CharEncoding.UTF_8);
		return EntityUtils.toString(httpEntity, CharEncoding.UTF_8);
	}

	public String doGetString(String url, Map<String, String> bodyParams) throws ParseException, IOException {
		HttpEntity httpEntity = doGet(url, bodyParams, new HashMap<String, String>(), CharEncoding.UTF_8);
		return EntityUtils.toString(httpEntity, CharEncoding.UTF_8);
	}

	public String doGetString(String url, Map<String, String> bodyParams, String charset) throws ParseException, IOException {
		HttpEntity httpEntity = doGet(url, bodyParams, new HashMap<String, String>(), charset);
		return EntityUtils.toString(httpEntity, charset);
	}

	public String doGetString(String url, Map<String, String> bodyParams, Map<String, String> headParams) throws ParseException, IOException {
		HttpEntity httpEntity = doGet(url, bodyParams, new HashMap<String, String>(), CharEncoding.UTF_8);
		return EntityUtils.toString(httpEntity, CharEncoding.UTF_8);
	}

	public String doGetString(String url, Map<String, String> bodyParams, Map<String, String> headParams, String charset) throws ParseException, IOException {
		HttpEntity httpEntity = doGet(url, bodyParams, headParams, charset);
		return EntityUtils.toString(httpEntity, charset);
	}

	/**
	 * 私有化get請求
	 * 
	 * @param url
	 *            请求地址
	 * @param bodyParams
	 *            请求参数
	 * @param headParams
	 *            请求头参数
	 * @param charset
	 *            编码方式 <code>CharEncoding</code>
	 * @return
	 */
	private HttpEntity doGet(String url, Map<String, String> bodyParams, Map<String, String> headParams, String charset) {
		url = buildQuery(url, bodyParams, charset);
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		if (headParams != null && !headParams.isEmpty()) {
			LOGGER.info("api head params:" + JSONObject.toJSONString(headParams));
			for (Entry<String, String> entry : headParams.entrySet()) {
				httpGet.addHeader(entry.getKey(), entry.getValue());
			}
		}
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int status = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (status != 200) {
				LOGGER.error(String.format("api return http code %d, detail: \n%s", status, EntityUtils.toString(entity, charset)));
			} else {
				LOGGER.info(String.format("api return detail: \n%s", EntityUtils.toString(entity, charset)));
			}
			response.close();
			return entity;
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String doPostString(String url) throws ParseException, IOException {
		HttpEntity httpEntity = doPost(url, new HashMap<String, String>(), new HashMap<String, String>(), CharEncoding.UTF_8);
		return EntityUtils.toString(httpEntity, CharEncoding.UTF_8);
	}

	public String doPostString(String url, Map<String, String> bodyParams) throws ParseException, IOException {
		HttpEntity httpEntity = doPost(url, bodyParams, new HashMap<String, String>(), CharEncoding.UTF_8);
		return EntityUtils.toString(httpEntity, CharEncoding.UTF_8);
	}

	public String doPostString(String url, Map<String, String> bodyParams, String charset) throws ParseException, IOException {
		HttpEntity httpEntity = doPost(url, bodyParams, new HashMap<String, String>(), charset);
		return EntityUtils.toString(httpEntity, charset);
	}

	public String doPostString(String url, Map<String, String> bodyParams, Map<String, String> headParams) throws ParseException, IOException {
		HttpEntity httpEntity = doPost(url, bodyParams, new HashMap<String, String>(), CharEncoding.UTF_8);
		return EntityUtils.toString(httpEntity, CharEncoding.UTF_8);
	}

	public String doPostString(String url, Map<String, String> bodyParams, Map<String, String> headParams, String charset) throws ParseException, IOException {
		HttpEntity httpEntity = doPost(url, bodyParams, headParams, charset);
		return EntityUtils.toString(httpEntity, charset);
	}

	/**
	 * 私有化post請求
	 * 
	 * @param url
	 *            请求地址
	 * @param bodyParams
	 *            请求参数
	 * @param headParams
	 *            请求头参数
	 * @param charset
	 *            编码方式 <code>CharEncoding</code>
	 * @return
	 */
	private HttpEntity doPost(String url, Map<String, String> bodyParams, Map<String, String> headParams, String charset) {
		LOGGER.info("api url:" + url);

		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		if (headParams != null && !headParams.isEmpty()) {
			LOGGER.info("api head params:" + JSONObject.toJSONString(headParams));
			for (Entry<String, String> entry : headParams.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
		}
		try {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (bodyParams != null && !bodyParams.isEmpty()) {
				LOGGER.info("api body params:" + JSONObject.toJSONString(bodyParams));
				for (Entry<String, String> entry : bodyParams.entrySet()) {
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, charset));
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (status != 200) {
				LOGGER.error(String.format("api return http code %d, detail: \n%s", status, EntityUtils.toString(entity, charset)));
			} else {
				LOGGER.info(String.format("api return detail: \n%s", EntityUtils.toString(entity, charset)));
			}
			response.close();
			return entity;
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String doPutString(String url) throws ParseException, IOException {
		HttpEntity httpEntity = doPut(url, new HashMap<String, String>(), new HashMap<String, String>(), CharEncoding.UTF_8);
		return EntityUtils.toString(httpEntity, CharEncoding.UTF_8);
	}

	public String doPutString(String url, Map<String, String> bodyParams) throws ParseException, IOException {
		HttpEntity httpEntity = doPut(url, bodyParams, new HashMap<String, String>(), CharEncoding.UTF_8);
		return EntityUtils.toString(httpEntity, CharEncoding.UTF_8);
	}

	public String doPutString(String url, Map<String, String> bodyParams, String charset) throws ParseException, IOException {
		HttpEntity httpEntity = doPut(url, bodyParams, new HashMap<String, String>(), charset);
		return EntityUtils.toString(httpEntity, charset);
	}

	public String doPutString(String url, Map<String, String> bodyParams, Map<String, String> headParams) throws ParseException, IOException {
		HttpEntity httpEntity = doPut(url, bodyParams, new HashMap<String, String>(), CharEncoding.UTF_8);
		return EntityUtils.toString(httpEntity, CharEncoding.UTF_8);
	}

	public String doPutString(String url, Map<String, String> bodyParams, Map<String, String> headParams, String charset) throws ParseException, IOException {
		HttpEntity httpEntity = doPut(url, bodyParams, headParams, charset);
		return EntityUtils.toString(httpEntity, charset);
	}

	/**
	 * 私有化post請求
	 * 
	 * @param url
	 *            请求地址
	 * @param bodyParams
	 *            请求参数
	 * @param headParams
	 *            请求头参数
	 * @param charset
	 *            编码方式 <code>CharEncoding</code>
	 * @return
	 */
	private HttpEntity doPut(String url, Map<String, String> bodyParams, Map<String, String> headParams, String charset) {
		LOGGER.info("api url:" + url);

		HttpPut httpPut = new HttpPut(url);
		httpPut.setConfig(requestConfig);
		if (headParams != null && !headParams.isEmpty()) {
			LOGGER.info("api head params:" + JSONObject.toJSONString(headParams));
			for (Entry<String, String> entry : headParams.entrySet()) {
				httpPut.addHeader(entry.getKey(), entry.getValue());
			}
		}
		try {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (bodyParams != null && !bodyParams.isEmpty()) {
				LOGGER.info("api body params:" + JSONObject.toJSONString(bodyParams));
				for (Entry<String, String> entry : bodyParams.entrySet()) {
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			httpPut.setEntity(new UrlEncodedFormEntity(nvps, charset));
			CloseableHttpResponse response = httpClient.execute(httpPut);
			int status = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (status != 200) {
				LOGGER.error(String.format("api return http code %d, detail: \n%s", status, EntityUtils.toString(entity, charset)));
			} else {
				LOGGER.info(String.format("api return detail: \n%s", EntityUtils.toString(entity, charset)));
			}
			response.close();
			return entity;
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 构建请求地址
	 * 
	 * @param params
	 *            请求参数
	 * @return 构建query
	 */
	private String buildQuery(String url, Map<String, String> params, String charset) {
		LOGGER.info("api url:" + url);
		if (params == null || params.isEmpty()) {
			return url;
		}
		LOGGER.info("api body params:" + JSONObject.toJSONString(params));
		StringBuilder sb = new StringBuilder(url);
		if (StringUtils.contains(url, "?")) {
			sb.append("&");
		} else {
			sb.append("?");
		}

		boolean first = true;
		for (Entry<String, String> entry : params.entrySet()) {
			if (first) {
				first = false;
			} else {
				sb.append("&");
			}
			String key = entry.getKey();
			String value = entry.getValue();
			if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
				try {
					sb.append(key).append("=").append(URLEncoder.encode(value, charset));
				} catch (UnsupportedEncodingException e) {
				}
			}
		}
		return sb.toString();

	}

}
