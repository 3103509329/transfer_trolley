package com.zhcx.authorization.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


/**
 * Created by Administrator on 2017/6/5.
 */
@Slf4j
@Component
public class HttpUtils {

    private Logger log = LoggerFactory.getLogger(HttpUtils.class);

    @Value("${druid.sql.url}")
    private String druidSqlUrl;

    @Value("${druid.pretty.url}")
    private String druidPrettyUrl;

    public static String DEFAULT_CONTENT_TYPE = "application/json";
    public static String DEFAULT_ENCODE_TYPE = "UTF-8";
    public static String DEFAULT_SQL_TIME_ZONE = "Asia/Hong_Kong";


    //调用druid查询接口，返回JSONArray
    public JSONArray doPostSqlUrl(String queryType, String sql) throws Exception{
        JSONObject query = this.strToJsonObject(queryType, sql);
        JSONArray result = this.doPostSqlUrl(query);
        return result;
    }

    private JSONObject strToJsonObject(String queryType, String sql) {
        JSONObject query = new JSONObject();
        JSONObject context = new JSONObject();
        //封装查询条件
        query.put("queryType", queryType);
        query.put("query", sql);
        context.put("sqlTimeZone", HttpUtils.DEFAULT_SQL_TIME_ZONE);
        query.put("context", context);
        return query;
    }

    //调用druid查询接口，返回JSONArray
   public JSONArray doPostSqlUrl(JSONObject jsonObject) throws Exception{
       String result = this.getPostJson(druidSqlUrl, jsonObject.toString(), 5, null);
       if (StringUtils.contains(result, "error") && StringUtils.contains(result, "errorMessage")) {
           return new JSONArray();
       }
       return JSON.parseArray(result);
   }

    //调用druid查询接口，返回String
    public String getPostSqlUrlStr(JSONObject jsonObject) throws Exception{
        return this.getPostJson(druidSqlUrl, jsonObject.toString(), 5,null);
    }

    //调用druid查询接口，返回String
    public String getPostSqlUrlStr(String queryType, String sql) throws Exception{
        JSONObject query = strToJsonObject(queryType, sql);
        return this.getPostSqlUrlStr(query);
    }

    //调用druid查询接口
    public JSONArray doPostPrettyUrl(String jsonStr) throws Exception{
        return JSONArray.parseArray(this.getPostJson(druidPrettyUrl, jsonStr, 5,null));
    }

    //调用druid查询接口
    public JSONArray doPostPrettyUrl(JSONObject jsonObject) throws Exception{
        return JSONArray.parseArray(this.getPostJson(druidPrettyUrl, jsonObject.toString(), 5,null));
    }

    //调用druid查询接口
    public String getPostPrettyUrlStr(JSONObject jsonObject) throws Exception{
        return this.getPostJson(druidPrettyUrl, jsonObject.toString(), 5,null);
    }

    //调用druid查询接口
    public String getPostPrettyUrlStr(String jsonStr) throws Exception{
        return this.getPostJson(druidPrettyUrl, jsonStr, 5,null);
    }

    //post请求方法
    public String getPostJson(String url, String data,int retime,Map<String,String> headerMap) {
        String response = null;

//        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(setRequestConfig()).build();
        CloseableHttpResponse res = null;
        try {
            response = this.execute(client,res,url,data,headerMap);
        } catch (Exception e) {
            log.error("sendPostJson request fail : " + e.getMessage());
//            while (retime > 0) {
//                log.info("开始重连。。。重连 次数倒数 : " + retime);
//                try {
//                    retime -- ;
//                    Thread.sleep(100l);
//                    response = execute(client,res,url,data,headerMap);
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                } catch (IOException e1) {
//                    log.error("重连失败");
//                }
//                if (null != response) {
//                    retime = 0;
//                } else if (retime <=0 ) {
//                    return response;
//                }
//            }
        } finally {
            try {
                if (client != null) {
                    client.close();
                }
                if (res != null) {
                    res.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }


    //调用Post请求
    private static String  execute(CloseableHttpClient client, CloseableHttpResponse res, String url, String data,Map<String,String> headerMap) throws IOException {
        HttpPost httppost = new HttpPost(url);
        if( headerMap != null && !headerMap.isEmpty()){
            for(String key:headerMap.keySet()){
                httppost.addHeader(key,headerMap.get(key));
            }
        }
        StringEntity stringentity = new StringEntity(data, ContentType.create(DEFAULT_CONTENT_TYPE, DEFAULT_ENCODE_TYPE));
        httppost.setEntity(stringentity);
        res = client.execute(httppost);
        String response = EntityUtils.toString(res.getEntity());
        if (StringUtils.isNotBlank(response)) {
            response = response.replace("\n","");
        }
        return response;
    }

    //调用Post请求
//    private static String  execute(CloseableHttpClient client, CloseableHttpResponse res, String url, String data) throws IOException {
//        HttpPost httppost = new HttpPost(url);
//        StringEntity stringentity = new StringEntity(data, ContentType.create(DEFAULT_CONTENT_TYPE, DEFAULT_ENCODE_TYPE));
//        httppost.setEntity(stringentity);
//        res = client.execute(httppost);
//        String response = EntityUtils.toString(res.getEntity());
//        if (StringUtils.isNotBlank(response)) {
//            response = response.replace("\n","");
//        }
//        return response;
//    }

    public RequestConfig setRequestConfig(){
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(15000)
                .setConnectTimeout(15000)
                .setConnectionRequestTimeout(15000)
                .build();
        return defaultRequestConfig;
    }

}
