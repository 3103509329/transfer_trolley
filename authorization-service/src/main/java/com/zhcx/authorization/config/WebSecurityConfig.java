package com.zhcx.authorization.config;

import com.alibaba.fastjson.JSON;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig.SessionHandler;
import com.zhcx.authorization.controller.netcar.app.jwt.JwtTokenUtil;
import com.zhcx.authorization.utils.MessageResp;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {

    /**
     * 登录session key
     */
    public final static String SESSION_KEY = "user";

    /**
     *  跳转登录地址
     */
    @Value("${login.url}")
    public String loginUrl;

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        // 排除配置
        addInterceptor.excludePathPatterns("/demo/test**");
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/auth/login**");
        addInterceptor.excludePathPatterns("/auth/captcha**");
        addInterceptor.excludePathPatterns("/taxi/common/**");
        addInterceptor.excludePathPatterns("/taxi/mileage/syncMileage/**");
        addInterceptor.excludePathPatterns("/taxi/file/receive**");
        addInterceptor.excludePathPatterns("/taxi/file/upload**");
//        addInterceptor.excludePathPatterns("/taxi/common/syncDataToStreaMax");
        addInterceptor.excludePathPatterns("/**.html","/v2/api-docs","/swagger-resources/**","/webjars/**");
        addInterceptor.excludePathPatterns("/operation/log/**");
//        addInterceptor.excludePathPatterns("/taxi/report/test");
//        addInterceptor.excludePathPatterns("/taxi/**");

        //测试
        addInterceptor.excludePathPatterns("/baseinfo/**");
        addInterceptor.excludePathPatterns("/netcar/**");
        addInterceptor.excludePathPatterns("/taxi/baseDictionary/**");
        addInterceptor.excludePathPatterns("/car/**");
        addInterceptor.excludePathPatterns("/driver/**");
        addInterceptor.excludePathPatterns("/tatistics/**");
        addInterceptor.excludePathPatterns("/monitor/**");
        addInterceptor.excludePathPatterns("/taxi/**");
        addInterceptor.excludePathPatterns("/taxi/net/**");

        addInterceptor.excludePathPatterns("/netcar_statistics/**");

        addInterceptor.excludePathPatterns("/AuthMenu/**");
        // 拦截配置
        addInterceptor.addPathPatterns("/**");
    }
    private class SecurityInterceptor extends HandlerInterceptorAdapter {

        @Autowired
        private SessionHandler sessionHandler;

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            HttpSession session = request.getSession();
            if (session.getAttribute(SESSION_KEY) != null)
                return true;

            AuthUserResp authUser = sessionHandler.getUser(request);
            if(null != authUser){
            	session.setMaxInactiveInterval(28800);//更新会话时间（默认8小时）,由于用户信息存在redis中此设置实际已失效
            	sessionHandler.setUser(session.getId(), authUser);//更新会话时间（默认8小时）
                return true;
            }
            //token登录方式
            String tonken = request.getParameter("token");

            try {
                if(StringUtils.isBlank(tonken)){
                    // app从 http 请求头中取出 token
                    tonken = request.getHeader("token");
                    //验证token是否过期,包含了验证jwt是否正确
                    if(StringUtils.isNotBlank(tonken)){
                        boolean flag = JwtTokenUtil.isTokenExpired(tonken);
                        if (flag) {
                            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            PrintWriter writer = response.getWriter();
                            MessageResp resp = new MessageResp();
                            writer.write(JSON.toJSONString(resp));
                            return false;
                        }else {
                            String currentUserName = JwtTokenUtil.getUsernameFromToken(tonken);
                            return true;
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            if(null != tonken){
                AuthUserResp resp = sessionHandler.getUser(tonken);
                if(null != resp){
                    sessionHandler.setUser(session.getId(), resp);//更新会话时间（默认8小时）
                    session.setMaxInactiveInterval(28800);
                    return true;
                }else
                    return false;
            }
            response.sendRedirect(loginUrl);
            return false;
        }
    }
}
