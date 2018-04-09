package com.home.common.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * HTTP请求工具类
 * 
 * @author Administrator
 *
 */
public class RequestUtils {

	/**
	 * 判断是否是Ajax请求
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With")) || request.getHeader("Accept") != null && request.getHeader("Accept").contains("application/json");
	} 
	
	/**
	 * 获取用户IP地址
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static final String getIpAddress(final HttpServletRequest request) throws Exception {
		if (request == null) {
			throw new Exception("getIpAddr method HttpServletRequest Object is null");
		}
		String ipString = request.getHeader("x-forwarded-for");
		if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
			ipString = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
			ipString = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
			ipString = request.getRemoteAddr();
		}

		// 多个路由时，取第一个非unknown的ip
		final String[] arr = ipString.split(",");
		for (final String str : arr) {
			if (!"unknown".equalsIgnoreCase(str)) {
				ipString = str;
				break;
			}
		}

		return ipString;
	}
}
