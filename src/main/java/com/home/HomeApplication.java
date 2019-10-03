package com.home;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.spring4all.swagger.EnableSwagger2Doc;


//注销默认数据源配置
@SpringBootApplication(scanBasePackages = { "com.home", "com.home.core.web" })
@EnableTransactionManagement
@MapperScan("com.home.*.repository.mybatis")
@EnableScheduling   //开启定时任务
@EnableAsync
@EnableSwagger2Doc //接口文档生成
@EnableCaching //缓存
//@EnableJms
//@EnableAdminServer
public class HomeApplication {

	private static final Logger logger = LoggerFactory.getLogger(HomeApplication.class);

	static {
		logger.info("Starting SpringBoot HomeApplication.....");
	}

	public static void main(String[] args) {
		SpringApplication.run(HomeApplication.class, args);
		logger.info("Start SpringBoot HomeApplication Successfully");
	} 

     

}
