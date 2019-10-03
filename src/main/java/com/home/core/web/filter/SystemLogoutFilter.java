package com.home.core.web.filter;

import com.home.common.http.ResponseResult;
import com.home.common.utils.RequestUtils;
import com.home.common.utils.ResponseUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SystemLogoutFilter extends LogoutFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemLogoutFilter.class);
	private static final String REDIRECT_URL = "/login";

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		Subject subject = super.getSubject(request, response);
		super.getRedirectUrl(request, response, subject);

		try {
			subject.logout();
		} catch (SessionException se) {
			LOGGER.debug("Encountered session exception during logout.  This can generally safely be ignored.", se);
		}
		if (RequestUtils.isAjaxRequest((HttpServletRequest) request)) {
			ResponseUtils.renderJson((HttpServletResponse) response, ResponseResult.createSuccess(subject)); 
		}
		else { 
			super.issueRedirect(request, response, REDIRECT_URL);
		}
		return false;
	}

}
