package com.zhcx.auth.utils;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.zhcx.auth.pojo.AuthUserResp;

import lombok.extern.slf4j.Slf4j;

/**
 * @classname UserAuthUtils
 * @description Auth工具类
 * @date 2019/12/25 14:41
 * @author xhe
 */
@Slf4j
public class UserAuthUtils {
    /**
     * 获取当前登陆用户信息
     * @return
     */
    public static AuthUserResp getUser(){
//        UserAuth user = new UserAuth();
//        user.setId(1L);
//        user.setUserId(29L);
//        user.setIdentifier("admin");
//        //user.setCredential("cb99294620b23fe24a9c8684113df06a");
//        user.setSalt("1558922225444");
//        return user;
        String token = getToken();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String userStr = request.getHeader("Token" + token);
        if(StringUtils.isBlank(userStr)){
            throw new UnauthorizedException("获取用户信息失败，请重新登录");
        }
        AuthUserResp authUser;
        try{
            String decode = URLDecoder.decode(userStr, "utf-8");
            authUser = JSONObject.parseObject(decode, AuthUserResp.class);
            log.info("当前获取用户信息：{}",authUser);
        }catch (Exception e){
            throw new UnauthorizedException("获取当前用户失败，请先登录");
        }
        if(null == authUser){
            throw new UnauthorizedException("获取当前用户失败，请先登录");
        }
        return authUser;
    }

    /**
     * 获取token
     * @return
     */
    public static String getToken(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        if(StringUtils.isBlank(token)){
            token = request.getParameter("Authorization");
        }
        //token为空返回401 请先登录
        if(StringUtils.isBlank(token)){
            throw new UnauthorizedException("请先登录");
        }
        token = token.split(" ")[1];
        log.info("token:{}",token);
        return token;
    }
}
