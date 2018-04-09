package com.home.core.web;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.home.common.utils.RequestUtils;
import com.home.core.entity.Log;
import com.home.core.service.LogService;

import io.swagger.annotations.ApiOperation;

/**
 * 日志记录AOP实现
 */
@Aspect
@Component
public class LogAspect {

	private static Logger logger = LoggerFactory.getLogger(LogAspect.class);

	private long startTime = 0L;
	private long endTime = 0L;

	@Autowired
	private LogService logService;

	@Before("@annotation(io.swagger.annotations.ApiOperation)")
	public void doBeforeInServiceLayer(JoinPoint joinPoint) {
		logger.debug("doBeforeInServiceLayer");
		startTime = System.currentTimeMillis();
	}

	@After("@annotation(io.swagger.annotations.ApiOperation)")
	public void doAfterInServiceLayer(JoinPoint joinPoint) {
		logger.debug("doAfterInServiceLayer");
	}

	@Around("@annotation(io.swagger.annotations.ApiOperation)")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		// 获取request
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		Log log = new Log();
		// 从注解中获取操作名称、获取响应结果
		Object result = pjp.proceed();
		Signature signature = pjp.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		endTime = System.currentTimeMillis();
		log.setIp(RequestUtils.getIpAddress(request));
		log.setDescription(method.getAnnotation(ApiOperation.class).value());
		log.setUrl(request.getRequestURL().toString());
		log.setMethod(request.getMethod());
		log.setUserAgent(request.getHeader("User-Agent"));
		log.setParameter(request.getQueryString());
		log.setResult(JSON.toJSONString(result));
		log.setLogTime(new Date(startTime));
		log.setSpendTime((int) (endTime - startTime));
		log.setUsername(CoreThreadContext.getUserName());
		logService.save(log);
		return result;
	}
}
