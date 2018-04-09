package com.home.core.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Controller增强器 Api统一返回json形式的异常信息
 * 
 * @author Administrator
 *
 */
@ControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

	@ExceptionHandler(value = { Exception.class })
	public final ResponseEntity<ResponseResult<?>> handleGeneralException(Exception ex, HttpServletRequest request) throws Exception {
		logError(ex, request);
		ResponseResult<?> result = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
		if (ex instanceof AuthorizationException) {
			result = ResponseResult.createError(HttpStatus.FORBIDDEN, ex.getMessage());
		} else {
			result = ResponseResult.createError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
		return new ResponseEntity<>(result, headers, HttpStatus.OK);
	}

	public void logError(Exception ex, HttpServletRequest request) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("message", ex.getMessage());
		map.put("from", RequestUtils.getIpAddress(request));
		map.put("path", request.getRequestURL().toString());
		LOGGER.error(JSON.toJSONString(map), ex);
	}
}
