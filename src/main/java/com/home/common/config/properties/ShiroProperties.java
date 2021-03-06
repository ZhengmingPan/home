package com.home.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {

	private String loginUrl;
	private String successUrl;
	private String unauthorizedUrl;
	private String cacheManagerConfigFile;
	private String hashAlgorithmName;
	private Integer hashIterations;
	private String filterChainDefinitionMap;

}
