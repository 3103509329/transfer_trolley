package com.zhcx.auth.config;

import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.auth.utils.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;


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
			AuthUserResp user =  this.getUser(request.getSession().getId());
			if(null == user){
				 //token登录方式
	            String tonken = request.getParameter("token");
	                if(StringUtils.isBlank(tonken)){
	                    // app从 http 请求头中取出 token
	                    tonken = request.getHeader("token");
	                    //验证token是否过期,包含了验证jwt是否正确
	                    if(StringUtils.isNotBlank(tonken)){
	                        boolean flag = JwtTokenUtil.isTokenExpired(tonken);
	                        if (!flag) {
	                        	user = getUser(tonken);
	                        }
	                        	
	                     }
	                }
			}
			return user;
		}
		
	}
}
