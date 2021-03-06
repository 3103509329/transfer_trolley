package com.zhcx.netcar.netcarservice.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * http和https请求客户端工具类
 *
 * @author 龚进
 * @version 1.0
 * @title
 * @date 2017年12月21日
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * 发起http请求
     *
     * @param requestUrl    请求url
     * @param requestMethod 请求方式 GET POST
     * @param outputStr     参数内容
     * @return
     */
    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuilder builder = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(requestMethod);
            conn.connect();
            // 往服务器端写内容 也就是发起http请求需要带的参数
            if (StringUtils.isNotBlank(outputStr)) {
                OutputStream os = conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }

            // 读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            builder = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


    public static HttpURLConnection getResponse(String requestUrl, String requestMethod, Map<String, String> headerParam,
                                                String outputStr) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection) url.openConnection();
            // 设置请求头
            if (null != headerParam) {
                for (Entry<String, String> entry : headerParam.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(requestMethod);
            conn.setConnectTimeout(600000);
            conn.setReadTimeout(600000);
            conn.connect();
            // 往服务器端写内容 也就是发起http请求需要带的参数
            if (StringUtils.isNotBlank(outputStr)) {
                OutputStream os = conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }
        } catch (Exception e) {
            logger.error("请求发送异常," + requestUrl + "参数：" + outputStr);
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 获取HTTP请求返回的状态码
     *
     * @param requestUrl
     * @param requestMethod
     * @param headerParam
     * @param outputStr
     * @return
     */
    public static int getHttpResponseCode(String requestUrl, String requestMethod, Map<String, String> headerParam,
                                          String outputStr) {
        int respCode = 0;
        HttpURLConnection conn = getResponse(requestUrl, requestMethod, headerParam, outputStr);
        try {
            respCode = conn.getResponseCode();
        } catch (IOException e) {
            logger.error("请求" + requestUrl + ",请求类型：" + requestMethod + "异常,返回结果：" + e.getMessage());
            e.printStackTrace();
        }
        return respCode;
    }


    /**
     * 发起http请求(带请求头设置)
     *
     * @param requestUrl
     * @param requestMethod
     * @param headerParam
     * @param outputStr
     * @return
     */
    public static String httpRequest(String requestUrl, String requestMethod, Map<String, String> headerParam,
                                     String outputStr) {
        StringBuilder builder = null;
        HttpURLConnection conn = null;
        try {
//			URL url = new URL(requestUrl);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			// 设置请求头
//			if (null != headerParam) {
//				for (Entry<String, String> entry : headerParam.entrySet()) {
//					conn.setRequestProperty(entry.getKey(), entry.getValue());
//				}
//			}
//			conn.setDoOutput(true);
//			conn.setDoInput(true);
//			conn.setRequestMethod(requestMethod);
//			conn.setConnectTimeout(600000);
//			conn.setReadTimeout(600000);
//			conn.connect();
//			// 往服务器端写内容 也就是发起http请求需要带的参数
//			if (StringUtils.isNotBlank(outputStr)) {
//				OutputStream os = conn.getOutputStream();
//				os.write(outputStr.getBytes("utf-8"));
//				os.close();
//			}

            conn = getResponse(requestUrl, requestMethod, headerParam, outputStr);
            logger.info("请求地址：" + requestUrl + ",请求方法：" + requestMethod + ",响应状态码：" + conn.getResponseCode());
            // 读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            builder = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            try {
                logger.error("POST请求发送异常," + requestUrl + ",响应状态码：" + conn.getResponseCode() + "参数：" + outputStr);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return builder.toString();
    }

    /**
     * 发起https请求
     *
     * @param requestUrl    请求url
     * @param requestMethod 请求方式 GET POST
     * @param outputStr     参数内容
     * @return
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuilder builder = null;
        try {
            // 创建SSLContext
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] tm = {new CustomX509TrustManager()};
            // 初始化
            sslContext.init(null, tm, new java.security.SecureRandom());
            ;
            // 获取SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            // 设置当前实例使用的SSLSoctetFactory
            conn.setSSLSocketFactory(ssf);
            conn.connect();
            // 往服务器端写内容
            if (StringUtils.isNotBlank(outputStr)) {
                OutputStream os = conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }

            // 读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            builder = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    /**
     * 发起http post json请求
     *
     * @param requestUrl
     * @param outputStr
     * @return
     */
    public static String httpPostJsonRequest(String requestUrl, String outputStr) {
        StringBuilder builder = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("accept", "application/json");
            // 往服务器端写内容 也就是发起http请求需要带的参数
            if (StringUtils.isNotBlank(outputStr)) {
                byte[] writebytes = outputStr.getBytes();
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = conn.getOutputStream();
                outwritestream.write(outputStr.getBytes());
                outwritestream.flush();
                outwritestream.close();
            }

            // 读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            builder = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
    public static void main(String[] args) {

        //druid数据库对应的表名称
        String dataSource = "netcar_statistics_driving_time";
        String time = "2020-07-01";
        int day = getDayOfMonth(time);
        Map map = new HashMap();
        map.put("type", "compact");
        map.put("dataSource", dataSource);
        for (int i = 1; i < day; i++) {

            String startTime = null;
            String endTime = null;
            String str = time.substring(0, 8);
            if (i < 10) {
                startTime = str + "0" + i;
                if ((i + 1) == 10) {
                    endTime = str + (i + 1);
                } else {
                    endTime = str + "0" + (i + 1);
                }
				map.put("interval", startTime + "/" + endTime);
            } else {

                startTime = str + i;
                endTime = str + (i + 1);
                map.put("interval", startTime + "/" + endTime);
            }
            System.out.print(JSON.toJSONString(map) + "\n");
//
            String url = "http://172.16.200.91:18081/druid/indexer/v1/task";
            String result = httpPostJsonRequest(url, JSON.toJSONString(map));
			System.out.print(result + "\n");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }

    /**
     * 指定年月 获取当月有多少天
     */
    public static int getDayOfMonth(String date) {
        String[] split = date.split("-");
        int year = Integer.parseInt(split[0]);
        int mth = Integer.parseInt(split[1]);
        Calendar c = Calendar.getInstance();
        c.set(year, mth, 0);
        return c.get(Calendar.DAY_OF_MONTH);
    }
}
