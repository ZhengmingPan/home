package com.home.core.config;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 统一自定义错误页面
 * 
 * @author Administrator
 *
 */
@Configuration
public class ErrorPageConfig {

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return container -> {
			ErrorPage error403Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/static/errors/403.html");
			ErrorPage error401Page = new ErrorPage(AuthorizationException.class, "/static/errors/403.html");
			ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/static/errors/404.html");
			ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/static/errors/500.html");
			container.addErrorPages(error401Page, error404Page, error500Page, error403Page);
		};
	}

}
