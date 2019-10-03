package com.home.core.web.filter;

import com.home.common.http.ResponseResult;
import com.home.common.utils.RequestUtils;
import com.home.common.utils.ResponseUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录权限认证过滤器，控制登录页面跳转或接口返回错误信息
 * 适用前提表单域名必须使用<code>FormAuthenticationFilter</code>中的内置常量
 * 
 * @author Administrator
 *
 */
public class PasswordAuthenFilter extends FormAuthenticationFilter {

	/**
	 * 用户验证登录
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		return super.onAccessDenied(request, response);
	}

	/**
	 * 登录成功后，控制页面重定向，如按不同权限进入不同页面
	 */
	@Override
	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
		super.issueSuccessRedirect(request, response);
	}

	/**
	 * 登录成功跳转，分析请求是否是Ajax请求返回json格式
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		if (RequestUtils.isAjaxRequest((HttpServletRequest) request)) {
			ResponseUtils.renderJson((HttpServletResponse) response, ResponseResult.createSuccess(subject));
			return false;
		}
		return super.onLoginSuccess(token, subject, request, response);
	}

	/**
	 * 登录失败跳转，分析请求是否是Ajax请求返回json格式
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		if (RequestUtils.isAjaxRequest((HttpServletRequest) request)) {
			ResponseUtils.renderJson((HttpServletResponse) response, ResponseResult.createError(HttpStatus.BAD_REQUEST, "登录失败"));
			return false;
		}
		return super.onLoginFailure(token, e, request, response);
	}

	/**
	 * 登录权限跳转，分析请求是否是Ajax请求返回json格式
	 */
	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		if (RequestUtils.isAjaxRequest((HttpServletRequest) request)) {
			if (SecurityUtils.getSubject().getPrincipal() == null) {
				ResponseUtils.renderJson((HttpServletResponse) response, ResponseResult.createError(HttpStatus.UNAUTHORIZED, "请重新登录"));
			} else {
				ResponseUtils.renderJson((HttpServletResponse) response, ResponseResult.createError(HttpStatus.FORBIDDEN, "权限不足"));
			}
		} else {
			super.redirectToLogin(request, response);
		}
	}

	/**
	 * 登录失败返回错误信息
	 */
	@Override
	protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
		super.setFailureAttribute(request, ae);
	}

	
	
}
