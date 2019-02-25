package com.home.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 远程HTTP控制参数
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "httpclient.pool")
public class HttpClientProperties {

    private Integer maxTotal;
    private Integer defaultMaxPerRoute;
    private Integer connectTimeout;
    private Integer connectionRequestTimeout;
    private Integer socketTimeout;
    private Boolean staleConnectionCheckEnabled;
    private Integer retryTime;
    private Long timeToLive;
    private Integer keepAliveTime;

}
