package com.home.core.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alibaba.fastjson.JSON;
import com.home.common.http.ResponseResult;
import com.home.common.utils.RequestUtils;
import com.home.core.entity.Log;
import com.home.core.service.LogService;

/**
 * Controller增强器 Api统一返回json形式的异常信息
 * 
 * @author Administrator
 *
 */
@RestControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

	@Autowired
	private LogService logService;
	
	@ExceptionHandler(value = { Exception.class })
	public final ResponseEntity<ResponseResult<?>> handleGeneralException(Exception ex, HttpServletRequest request) throws Exception {

		ResponseResult<?> result = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		result = ResponseResult.createError(HttpStatus.FORBIDDEN, ex.getMessage()); 
		logError(ex, request, result);
		return new ResponseEntity<>(result, headers, HttpStatus.FORBIDDEN);
	}

	private void logError(Exception ex, HttpServletRequest request, ResponseResult<?> result) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("message", ex.getMessage());
		map.put("from", RequestUtils.getIpAddress(request));
		map.put("path", request.getRequestURL().toString());
		LOGGER.error(JSON.toJSONString(map), ex);
 
		Log log = new Log();
		log.setIp(RequestUtils.getIpAddress(request));
		log.setDescription(request.getMethod());
		log.setUrl(request.getRequestURL().toString());
		log.setMethod(request.getMethod());
		log.setUserAgent(request.getHeader("User-Agent"));
		log.setParameter(request.getQueryString());
		log.setResult(JSON.toJSONString(result));
		log.setLogTime(new Date());
		log.setUsername(CoreThreadContext.getUserName());
		logService.save(log);
	}
}
