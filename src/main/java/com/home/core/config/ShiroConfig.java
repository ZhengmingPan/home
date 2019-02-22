package com.home.core.config;

import java.util.Map;

import javax.servlet.Filter;

import com.home.core.web.filter.PasswordAuthenFilter;
import com.home.core.web.filter.SystemLogoutFilter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.home.core.service.ShiroAuthRealm;

/**
 * shiro的配置类
 * 
 * @author Administrator
 *
 */
@Configuration
public class ShiroConfig {


	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

	@Autowired
	private ShiroPreference shiroPref;


	@Bean(name = "lifecycleBeanPostProcessor")
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		logger.info("Shiro Beginning LifecycleBeanPostProcessor..... ");
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * 注册DelegatingFilterProxy（Shiro）
	 */
	@Bean
	public FilterRegistrationBean delegatingFilterProxy() {
		logger.info("Shiro Registering FilterRegistrationBean..... ");
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		DelegatingFilterProxy proxy = new DelegatingFilterProxy();
		proxy.setTargetFilterLifecycle(true);
		proxy.setTargetBeanName("shiroFilter");
		filterRegistrationBean.setFilter(proxy);
		return filterRegistrationBean;
	}


	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") org.apache.shiro.mgt.SecurityManager manager) {
		logger.info("Shiro Registering authorized ShiroFilterFactoryBean..... ");
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		// 配置认证认证过滤器
		Map<String, Filter> filters = bean.getFilters();
		filters.put("authc", new PasswordAuthenFilter());
		filters.put("logout", new SystemLogoutFilter());
		bean.setSecurityManager(manager);
		// 配置登录的url和登录成功的url
		bean.setLoginUrl(shiroPref.getLoginUrl());
		bean.setSuccessUrl(shiroPref.getSuccessUrl());
		bean.setUnauthorizedUrl(shiroPref.getUnauthorizedUrl());
		// 配置访问权限
		bean.setFilterChainDefinitionMap(shiroPref.getFilterChainDefinitionMap());
		return bean;
	}

	// 配置核心安全事务管理器
	@Bean(name = "securityManager")
	public SecurityManager securityManager(@Qualifier("authRealm") ShiroAuthRealm authRealm) {
		logger.info("Shiro Registering core securityManager..... ");
		DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
		manager.setRealm(authRealm);
		return manager;
	}

	// 配置自定义的权限登录器
	@Bean(name = "authRealm")
	public ShiroAuthRealm authRealm(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher,
									@Qualifier("ehCacheManager")EhCacheManager ehCacheManager) {
		ShiroAuthRealm authRealm = new ShiroAuthRealm();
		authRealm.setCredentialsMatcher(matcher);
		authRealm.setCacheManager(ehCacheManager);
		return authRealm;
	}

	/**
	 * 密码匹配凭证管理器
	 * 
	 * @return
	 */
	@Bean(name = "hashedCredentialsMatcher")
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName(shiroPref.getHashAlgorithmName());// 散列算法:这里使用MD5算法;
		hashedCredentialsMatcher.setHashIterations(shiroPref.getHashIterations());// 散列的次数，比如散列两次，相当于
		return hashedCredentialsMatcher;
	}

	// 配置缓存管理
	@Bean(name = "ehCacheManager")
	public EhCacheManager cacheManager() {
		EhCacheManager cacheManager = new EhCacheManager();
		cacheManager.setCacheManagerConfigFile(shiroPref.getCacheManagerConfigFile());
		return cacheManager;
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
		creator.setProxyTargetClass(true);
		return creator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager manager) {
		logger.info("Shiro Registering  AuthorizationAttributeSourceAdvisor..... ");
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(manager);
		return advisor;
	}

}