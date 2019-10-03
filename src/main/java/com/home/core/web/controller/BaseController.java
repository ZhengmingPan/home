package com.home.core.web.controller;

/**
 * 页面路由Controller基类
 */
public class BaseController { 
	
	private String templatePrefix;

    protected String renderTemplate(String targetName) {
        return templatePrefix + targetName;
    }

	public String getTemplatePrefix() {
		return templatePrefix;
	}

	public void setTemplatePrefix(String templatePrefix) {
		this.templatePrefix = templatePrefix;
	}

}
