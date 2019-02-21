package com.home;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


//注销默认数据源配置
@SpringBootApplication(scanBasePackages = { "com.home", "com.home.core.web" })
@EnableTransactionManagement
@MapperScan("com.home.*.repository.mybatis")
@EnableScheduling   //开启定时任务
@EnableAsync
@EnableSwagger2Doc //接口文档生成
@EnableCaching //缓存
@EnableJms
public class HomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeApplication.class, args);
	} 

     

}
