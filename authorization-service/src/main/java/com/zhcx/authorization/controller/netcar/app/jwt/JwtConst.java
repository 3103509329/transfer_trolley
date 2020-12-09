package com.zhcx.authorization.controller.netcar.app.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * jwt相关配置
 *
 * @author fengshuonan
 * @date 2017-08-23 9:23
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConst {

    public static String header;

    public static String secret;

    public static Long expiration;

    public static String refreshPath;

    public static String bearer;

    public void setHeader(String header) {
        JwtConst.header = header;
    }

    public void setSecret(String secret) {
        JwtConst.secret = secret;
    }

    public void setExpiration(Long expiration) {
        JwtConst.expiration = expiration;
    }

    public void setRefreshPath(String refreshPath) {
        JwtConst.refreshPath = refreshPath;
    }

    public void setBearer(String bearer) {
        JwtConst.bearer = bearer;
    }
}
