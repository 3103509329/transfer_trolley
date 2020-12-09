package com.zhcx.authorization.config;

import java.io.IOException;
import java.util.Properties;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.support.NullValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.pagehelper.PageHelper;


@Configuration
public class RedisConfig {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Primary
	@ConditionalOnMissingBean(RedisTemplate.class)
	@Bean
    public <K, V> RedisTemplate<K, V> defaultRedisTemplate(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {  
        RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();  
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        
        JavaType javaType = objectMapper.getTypeFactory().constructType(new TypeReference<V>(){}.getType());
        RedisSerializer stringRedisSerializer = new StringRedisSerializer(); 
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        
        return redisTemplate;
    }
	
	@Bean(name="redisTemplate4Json")
    public <K, V> RedisTemplate<K, V> redisTemplate4Json(RedisConnectionFactory redisConnectionFactory) {  
        RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();  
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        
//        JavaType javaType = objectMapper.getTypeFactory().constructType(new TypeReference<V>(){}.getType());
        RedisSerializer stringRedisSerializer = new StringRedisSerializer(); 
//        RedisSerializer<V> jsonRedisSerializer = new Jackson2JsonRedisSerializer<V>(javaType); 
        
        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.fail, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		mapper.registerModule(new SimpleModule().addSerializer(new NullValueSerializer(null)));
		mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
        RedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer(mapper);
        
        redisTemplate.setDefaultSerializer(jsonRedisSerializer);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
        
        return redisTemplate;  
    }


	private class NullValueSerializer extends StdSerializer<NullValue> {

		private static final long serialVersionUID = 1999052150548658808L;
		private final String classIdentifier;

		/**
		 * @param classIdentifier can be {@literal null} and will be defaulted to {@code @class}.
		 */
		NullValueSerializer(String classIdentifier) {

			super(NullValue.class);
			this.classIdentifier = org.springframework.util.StringUtils.hasText(classIdentifier) ? classIdentifier : "@class";
		}

		/*
		 * (non-Javadoc)
		 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
		 */
		@Override
		public void serialize(NullValue value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException {

			jgen.writeStartObject();
			jgen.writeStringField(classIdentifier, NullValue.class.getName());
			jgen.writeEndObject();
		}
	}
	
    //配置mybatis的分页插件pageHelper
      @Bean
      public PageHelper pageHelper(){
          PageHelper pageHelper = new PageHelper();
          Properties properties = new Properties();
          properties.setProperty("offsetAsPageNum","true");
          properties.setProperty("rowBoundsWithCount","true");
          properties.setProperty("reasonable","true");
          properties.setProperty("dialect","oracle");    //配置oracle数据库的方言
         pageHelper.setProperties(properties);
         return pageHelper;
     }
      
  	@Bean
    @Order(Integer.MAX_VALUE)
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CorsConfig());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("corsFilter");
        return registration;
    }

}
