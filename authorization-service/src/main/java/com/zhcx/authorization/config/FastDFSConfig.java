package com.zhcx.authorization.config;

import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @program: authorization-service
 * @ClassName:FastDFSConfig
 * @description:
 * @author: ZhangKai
 * @create: 2018-12-12 15:54
 **/
@Configuration
public class FastDFSConfig {

    private static final Logger logger = LoggerFactory.getLogger(FastDFSConfig.class);

    @Value("${fastdfs.connect_timeout}")
    public int connectTimeout;

    @Value("${fastdfs.network_timeout}")
    public int networkTimeout;

    @Value("${fastdfs.charset}")
    public String charset;

    @Value("${fastdfs.http.tracker_http_port}")
    public int httpTrackerHttpPort;

    @Value("${fastdfs.http.anti_steal_token}")
    public String httpAntiStealToken;

    @Value("${fastdfs.http.secret_key}")
    public String httpSecretKey;

    @Value("${fastdfs.tracker_server}")
    public String trackerServer;

    @Value("${fastdfs.image_file_size}")
    public Long imageFileSize;

    @Value("${fastdfs.file_size}")
    public Long fileSize;


    @Bean
    public StorageClient1 initStorageClient()
    {
        StorageClient1 storageClient = null;
        try
        {

            Properties properties = new Properties();
            properties.put(ClientGlobal.PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS, connectTimeout);
            properties.put(ClientGlobal.PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS, networkTimeout);
            properties.put(ClientGlobal.PROP_KEY_CHARSET, charset);
            properties.put(ClientGlobal.PROP_KEY_HTTP_ANTI_STEAL_TOKEN, httpAntiStealToken);
            properties.put(ClientGlobal.PROP_KEY_HTTP_SECRET_KEY, httpSecretKey);
            properties.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, trackerServer);
            logger.info("FastDFS properties,{}", properties);
            ClientGlobal.initByProperties(properties);
            System.out.println("ClientGlobal.configInfo(): " + ClientGlobal.configInfo());
            TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
            TrackerServer trackerServer = trackerClient.getConnection();
            if (trackerServer == null)
            {
                throw new IllegalStateException("getConnection return null");
            }
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            if (storageServer == null)
            {
                throw new IllegalStateException("getStoreStorage return null");
            }
            ProtoCommon.activeTest(storageServer.getSocket());
            storageClient = new StorageClient1(trackerServer, storageServer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return storageClient;
    }
}
