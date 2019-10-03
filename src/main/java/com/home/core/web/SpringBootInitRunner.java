package com.home.core.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * SpringBoot启动初始化资源（Spring Beans初始化之后进行）
 */
@Component
@Order(1)  //当有多个CommandLineRunner时，确定初始化资源的执行顺序
public class SpringBootInitRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SpringBootInitRunner.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info("Beginning SpringBootInitRunner: init other operation...");
    }

}
