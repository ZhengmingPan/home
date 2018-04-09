package com.home;
 
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.didispace.swagger.EnableSwagger2Doc;

import de.codecentric.boot.admin.config.EnableAdminServer;

//注销默认数据源配置
@SpringBootApplication(scanBasePackages = { "com.home", "com.home.core.web" })
@EnableTransactionManagement
@MapperScan("com.home.*.repository.mybatis")
@EnableScheduling   //开启定时任务
//@EnableAdminServer
@EnableAsync
@EnableSwagger2Doc //接口文档生成
@EnableCaching //缓存
public class HomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeApplication.class, args);
	} 
	 
	//tomcatEmbedded这段代码是为了解决，上传文件大于10M出现连接重置的问题
    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbedded() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) { 
                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
            }
        });
        return tomcat;
    } 
     

}
