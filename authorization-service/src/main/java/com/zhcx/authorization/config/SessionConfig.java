package com.zhcx.authorization.config;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.zhcx.auth.pojo.AuthUserResp;



@Configuration
public class SessionConfig {
	
	@Bean
	public SessionHandler sessionHandlerFactory(RedisTemplate<String, AuthUserResp> redisTemplate) {
		return new SessionHandler(redisTemplate);
	}
	
	public static class SessionHandler {
		
		private final static String SESSION_VALUE_PREFIX = "session:user:";

		@Resource(name="redisTemplate4Json")
		private RedisTemplate<String, AuthUserResp> redisTemplate;
		
		public SessionHandler(RedisTemplate<String, AuthUserResp> redisTemplate) {
			this.redisTemplate = redisTemplate;
		}
		
		public void setUser(String sessionId, AuthUserResp user) {
			String key = SESSION_VALUE_PREFIX + sessionId;
			redisTemplate.opsForValue().set(key, user, 8, TimeUnit.HOURS);
		}
		
		public AuthUserResp getUser(String sessionId) {
			String key = SESSION_VALUE_PREFIX + sessionId;
//			AuthUser user = redisTemplate.execute(new RedisCallback<AuthUser>() {
//
//				@Override
//				public AuthUser doInRedis(RedisConnection connection) throws DataAccessException {
//					//connection.select(1);
//					byte[] value = connection.get(redisTemplate.getStringSerializer().serialize(key));
//					connection.close();
//					AuthUser user = (AuthUser) redisTemplate.getDefaultSerializer().deserialize(value);
//					return user;
//				}
//			});
			return redisTemplate.opsForValue().get(key);
//			return user;
		}
		
		public void removeSession(String sessionId) {
			String key = SESSION_VALUE_PREFIX + sessionId;
			/*redisTemplate.expire(key, 1, TimeUnit.MILLISECONDS);*/
			redisTemplate.delete(key);
		}
		
		public AuthUserResp getUser(HttpServletRequest request) {
			return this.getUser(request.getSession().getId());
		}
		
	}
	
}
