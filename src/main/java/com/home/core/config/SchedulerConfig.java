package com.home.core.config;

import java.io.IOException;
import java.util.Properties;

import org.quartz.Scheduler;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 配置Quartz任务调度
 * 
 * @author zhengming
 *
 */
//@Configuration
public class SchedulerConfig {

	//@Bean(name = "schedulerFactory")
	public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setQuartzProperties(quartzProperties());
		factory.setJobFactory(new AdaptableJobFactory() {
			@Autowired
			private AutowireCapableBeanFactory capableBeanFactory; 
			@Override
			protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
				Object jobInstance = super.createJobInstance(bundle);
				capableBeanFactory.autowireBean(jobInstance); // 这一步解决不能spring注入bean的问题
				return jobInstance;
			}
		});
		return factory;
	}

	//@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/scheduler/quartz.properties"));
		// 在quartz.properties中的属性被读取并注入后再初始化对象
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}

	/**
	 * quartz初始化监听器
	 */
	//@Bean
	public QuartzInitializerListener executorListener() {
		return new QuartzInitializerListener();
	}

	/**
	 * 通过SchedulerFactoryBean获取Scheduler的实例
	 */
	//@Bean(name = "scheduler")
	public Scheduler scheduler() throws IOException {
		return schedulerFactoryBean().getScheduler();
	}

}