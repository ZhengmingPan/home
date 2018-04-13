package com.home.core.web;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.AuthorizationException;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.home.common.entity.ResponseResult;
import com.home.common.utils.RequestUtils;
import com.home.core.entity.Log;
import com.home.core.service.LogService;

import io.swagger.annotations.ApiOperation;

/**
 * Controller增强器 Api统一返回json形式的异常信息
 * 
 * @author Administrator
 *
 */
@ControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

	@Autowired
	private LogService logService;

	@ExceptionHandler(value = { Exception.class })
	public final ResponseEntity<ResponseResult<?>> handleGeneralException(Exception ex, HttpServletRequest request) throws Exception {

		ResponseResult<?> result = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
		if (ex instanceof AuthorizationException) {
			result = ResponseResult.createError(HttpStatus.FORBIDDEN, ex.getMessage());
		} else {
			result = ResponseResult.createError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
		logError(ex, request, result);
		return new ResponseEntity<>(result, headers, HttpStatus.OK);
	}

	private void logError(Exception ex, HttpServletRequest request, ResponseResult<?> result) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("message", ex.getMessage());
		map.put("from", RequestUtils.getIpAddress(request));
		map.put("path", request.getRequestURL().toString());
		LOGGER.error(JSON.toJSONString(map), ex);

		// 获取request
		Log log = new Log();
		log.setIp(RequestUtils.getIpAddress(request));
		log.setDescription(request.getMethod());
		log.setUrl(request.getRequestURL().toString());
		log.setMethod(request.getMethod());
		log.setUserAgent(request.getHeader("User-Agent"));
		log.setParameter(request.getQueryString());
		log.setResult(JSON.toJSONString(result));
		log.setUsername(CoreThreadContext.getUserName());
		logService.save(log);
	}
}
