package com.zhcx.authorization.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: authorization-service
 * @ClassName:FastDFSProperties
 * @description: FastDFS属性
 * @author: ZhangKai
 * @create: 2018-12-02 10:50
 **/
@Component("fastDFSProperties")
public class FastDFSProperties {

    public static int connectTimeout;

    public static int networkTimeout ;

    public static String charset;

    public static int httpTrackerHttpPort;

    public static String httpAntiStealToken;

    public static String httpSecretKey;

    public static String trackerServer;

    public static Long imageFileSize;

    public static Long fileSize;


    @Value("${fastdfs.connect_timeout}")
    public void setConnectTimeout(int connectTimeout) {
        FastDFSProperties.connectTimeout = connectTimeout;
    }

    @Value("${fastdfs.network_timeout}")
    public void setNetworkTimeout(int networkTimeout) {
        FastDFSProperties.networkTimeout = networkTimeout;
    }

    @Value("${fastdfs.charset}")
    public void setCharset(String charset) {
        FastDFSProperties.charset = charset;
    }

    @Value("${fastdfs.http.tracker_http_port}")
    public void setHttpTrackerHttpPort(int httpTrackerHttpPort) {
        FastDFSProperties.httpTrackerHttpPort = httpTrackerHttpPort;
    }

    @Value("${fastdfs.http.anti_steal_token}")
    public void setHttpAntiStealToken(String httpAntiStealToken) {
        FastDFSProperties.httpAntiStealToken = httpAntiStealToken;
    }

    @Value("${fastdfs.http.secret_key}")
    public void setHttpSecretKey(String httpSecretKey) {
        FastDFSProperties.httpSecretKey = httpSecretKey;
    }

    @Value("${fastdfs.tracker_server}")
    public void setTrackerServer(String trackerServer) {
        FastDFSProperties.trackerServer = trackerServer;
    }

    @Value("${fastdfs.image_file_size}")
    public void setImageFileSize(Long imageFileSize) {
        FastDFSProperties.imageFileSize = imageFileSize;
    }

    @Value("${fastdfs.file_size}")
    public void setFileSize(Long fileSize) {
        FastDFSProperties.fileSize = fileSize;
    }
}
