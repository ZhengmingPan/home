package com.home.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.home.common.entity.ShiroUser;
import com.home.common.utils.RequestUtils;
import com.home.core.entity.BaseUser;
import com.home.core.service.BaseUserService;

/**
 * 拦截并存储当前登录的用户信息
 * 
 * @author Administrator
 *
 */
public class ContextInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private BaseUserService baseUserService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		// 解决baseUserService为null无法注入问题
		if (baseUserService == null) {
			BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
			baseUserService = (BaseUserService) factory.getBean("baseUserService");
		}

		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (user != null) {
			BaseUser baseUser = baseUserService.get(user.getId());
			CoreThreadContext.setUser(baseUser);
		}
		CoreThreadContext.setIp(RequestUtils.getIpAddress(request));
		CoreThreadContext.setUrl(request.getRequestURL().toString());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

		// modelAndView传递参数

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		CoreThreadContext.reset();
		if (ex != null) {
			ex.printStackTrace();
		}
	}

	public BaseUserService getBaseUserService() {
		return baseUserService;
	}

	public void setBaseUserService(BaseUserService baseUserService) {
		this.baseUserService = baseUserService;
	}

}
