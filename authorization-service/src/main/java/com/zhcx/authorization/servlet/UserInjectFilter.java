package com.zhcx.authorization.servlet;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig.SessionHandler;


/**  
 * @Title: UserInjectFilter.java 
 * @Package com.zhcx.platform.servlet 
 * @Description: 拦截移动端token值，并通过token获取redis中用户信息
 * @author 唐定
 * @date 2015-11-21 下午4:50:44 
 * @version 
 */
public class UserInjectFilter extends OncePerRequestFilter {
	
	/**
     * 登录session key
     */
    public final static String SESSION_KEY = "user";
	
	@Autowired
	private SessionHandler sessionHandler;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain){
		try {
			String tonken = request.getParameter("token");
			if(tonken!=null){
				AuthUserResp resp = sessionHandler.getUser(tonken);
				if(null != resp){
					/* HttpSession session = request.getSession();
					 session.setAttribute(SESSION_KEY, resp);*/
					sessionHandler.setUser(request.getSession().getId(), resp);
				}
				
			}
			filterChain.doFilter(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	}

}
