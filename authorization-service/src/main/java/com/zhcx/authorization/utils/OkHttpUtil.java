package com.zhcx.authorization.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @program: authorization-service
 * @ClassName:OkHttpUtil
 * @description: OkHttpUtil
 * @author: ZhangKai
 * @create: 2018-12-24 17:13
 **/
@Component
public class OkHttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);

    private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36";

    @Autowired
    private OkHttpClient okHttpClient;


    public Response doPostRequest(RequestBody requestBody, String url, String token) {
        Response response = null;
        Request.Builder builder = createRequestBuilder(url, token);
        Request request = builder.post(requestBody).build();
        try{
            response = okHttpClient.newCall(request).execute();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Do post " + url + " Exception :" + e);
        }
        return response;
    }


    protected Request.Builder createRequestBuilder(String url, String token) {
        Request.Builder builder = new Request.Builder().url(url);
        builder.addHeader("User-Agent", USER_AGENT)
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader("Accept-Charset", "application/json")
                .addHeader("Charset", "utf-8")
                .addHeader("Transfer-Encoding", "chunked")
                .addHeader("Vary", "Accept-Encoding")
                .addHeader("X-Content-Type-Options", "nosniff,nosniff")
                .addHeader("Content-Encoding", "gzip")
                .addHeader("Connection", "close");
        if (null != token && !"".equals(token)) {
            builder.addHeader("X-Authorization", "Bearer" + " " + token);
        }
        return builder;
    }


}
