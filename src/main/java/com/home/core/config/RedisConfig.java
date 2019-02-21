package com.home.core.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

/**
 * Redis配置
 */
@Slf4j
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

	/**
	 * 自定义缓存没有指定key的生成的策略
	 */
	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		//  设置自动key的生成规则，配置spring boot的注解，进行方法级别的缓存
		// 使用：进行分割，可以很多显示出层级关系
		return (target, method, params) -> {
			StringBuilder sb = new StringBuilder();
			sb.append(target.getClass().getName());
			sb.append(".");
			sb.append(method.getName());
			sb.append("[");
			for (Object obj : params) {
				sb.append(String.valueOf(obj));
			}
			sb.append("]");
			return sb.toString();
		};
	}

	/**
	 * 设置默认缓存控件
	 * @param factory
	 * @return
	 */
	@Bean(name = "cacheManager")
	public CacheManager cacheManager(RedisConnectionFactory factory) {
		// 生成一个默认配置，通过config对象即可对缓存进行自定义配置
		RedisCacheConfiguration redisConfig = RedisCacheConfiguration.defaultCacheConfig();
		//设置默认的过期时间，30min
		redisConfig = redisConfig.entryTtl(Duration.ofMinutes(30))
				.disableCachingNullValues();

		//设置初始化缓存控件结合，就是注解@Cacheable(value = "my-redis-cache2")中的value值
		Set<String> cacheNames = Sets.newHashSet();
		cacheNames.add("default");
		cacheNames.add("default2");

		//对每个缓存控件应用不同的配置
		Map<String, RedisCacheConfiguration> configMap = Maps.newHashMap();
		configMap.put("default", redisConfig);
		configMap.put("default2", redisConfig.entryTtl(Duration.ofSeconds(30)));  //过期时间30s

		// 使用自定义的缓存配置初始化一个cacheManager
		return RedisCacheManager.builder(factory)
				.initialCacheNames(cacheNames)
				.withInitialCacheConfigurations(configMap)
				.transactionAware()
				.build();

	}

	/**
	 * 缓存数据序列化
	 * @param jedisConnectionFactory
	 * @return
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
		//设置序列化
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

		//配置redisTemplate
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringRedisSerializer); //key序列化
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer); //value序列化
		redisTemplate.setHashKeySerializer(stringRedisSerializer);  //Hash key
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	/**
	 * 获取缓存数据异常处理
	 */
	@Override
	@Bean
	public CacheErrorHandler errorHandler() {
		//异常处理，当Redis异常是，打印日志，并保存程序正常运行
		logger.info("Initializing... -> [{}]", "Redis CacheErrorHandle");
		return new CacheErrorHandler() {
			@Override
			public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
				logger.error("Redis occur handleCacheGetError：key -> [{}]", key, e);
			}

			@Override
			public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
				logger.error("Redis occur handleCachePutError：key -> [{}]；value -> [{}]", key, value, e);
			}

			@Override
			public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
				logger.error("Redis occur handleCacheEvictError：key -> [{}]", key, e);
			}

			@Override
			public void handleCacheClearError(RuntimeException e, Cache cache) {
				logger.error("Redis occur handleCacheClearError：", e);
			}
		};
	}


	/**
	 * 此内部类就是把peoperties的配置数据，进行读取，创建JedisConnectionFactory和JedisPool，以供外部类初始化缓存管理器使用
	 * 不了解的同学可以去看@ConfigurationProperties和@Value的作用
	 **/

	@ConfigurationProperties
	class DataJedisProperties {
		@Value("${spring.redis.host}")
		private String host;
		@Value("${spring.redis.password}")
		private String password;
		@Value("${spring.redis.port}")
		private int port;
		@Value("${spring.redis.timeout}")
		private int timeout;
		@Value("${spring.redis.jedis.pool.max-idle}")
		private int maxIdle;
		@Value("${spring.redis.jedis.pool.max-wait}")
		private long maxWaitMillis;

		@Bean
		JedisConnectionFactory jedisConnectionFactory() {
			logger.info("Create JedisConnectionFactory successful");
			RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
			redisStandaloneConfiguration.setHostName(host);
			redisStandaloneConfiguration.setPort(port);
			redisStandaloneConfiguration.setDatabase(0);
			redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

			JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
			jedisClientConfiguration.connectTimeout(Duration.ofMillis(timeout));
			return new JedisConnectionFactory(redisStandaloneConfiguration,
					jedisClientConfiguration.build());
		}

		@Bean
		public JedisPool redisPoolFactory() {
			logger.info("JedisPool init successful，host -> [{}]；port -> [{}]", host, port);
			JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
			jedisPoolConfig.setMaxIdle(maxIdle);
			jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
			return new JedisPool(jedisPoolConfig, host, port, timeout, password);
		}
	}
}
