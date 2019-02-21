package com.home.core.vo;

import java.util.LinkedHashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "shiro")
public class ShiroPreference {

	private String loginUrl;
	private String successUrl;
	private String unauthorizedUrl;
	private String cacheManagerConfigFile;
	private String hashAlgorithmName;
	private Integer hashIterations;
	private LinkedHashMap<String, String> filterChainDefinitionMap;

}
