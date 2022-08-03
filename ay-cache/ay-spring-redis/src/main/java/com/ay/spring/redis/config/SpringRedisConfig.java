package com.ay.spring.redis.config;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

@Configuration
@EnableCaching
public class SpringRedisConfig extends CachingConfigurerSupport {

	@Autowired
	private LettuceConnectionFactory lettuceConnectionFactory;

	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuffer sb = new StringBuffer();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}

	// 缓存管理器
	@Bean
	public CacheManager cacheManager() {
		// 初始化一个RedisCacheWriter
		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(lettuceConnectionFactory);
		// 设置CacheManager的值序列化方式为
		// fastJsonRedisSerializer,但其实RedisCacheConfiguration默认使用StringRedisSerializer序列化key，
//		ClassLoader loader = this.getClass().getClassLoader();

		FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
		RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer);
		RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration//
				.defaultCacheConfig()//
				.serializeValuesWith(pair)//
				.entryTtl(Duration.ofHours(12l));
		RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
		return cacheManager;
	}

	private static class FastJsonRedisSerializer<T> implements RedisSerializer<T> {
		private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
		private Class<T> clazz;

		public FastJsonRedisSerializer(Class<T> clazz) {
			this.clazz = clazz;
			ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
		}

		@Override
		public byte[] serialize(T t) throws SerializationException {
			if (t == null) {
				return new byte[0];
			}
			return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
		}

		@Override
		public T deserialize(byte[] bytes) throws SerializationException {
			if (bytes == null || bytes.length <= 0) {
				return null;
			}
			String str = new String(bytes, DEFAULT_CHARSET);
			return JSONObject.parseObject(str, clazz);
		}
	}
//	@Bean
//	public CacheManager cacheManager() {
//		// 初始化一个RedisCacheWriter
//		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(lettuceConnectionFactory);
//
//		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
//
//		RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(serializer);
//
//		RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
//
//		return new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
//	}

}