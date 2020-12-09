package com.zhcx.authorization.utils;

import com.zhcx.itbus.utils.StringUtil;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @program: authorization-service
 * @ClassName:FastDFSUtil
 * @description: FastDFS工具类
 * @author: ZhangKai
 * @create: 2018-12-02 10:56
 **/
@Component
public class FastDFSUtil {
    private static final Logger logger = LoggerFactory.getLogger(FastDFSUtil.class);
    private static String HEADER = "FileManagerUtils:------------";

    @Autowired
    private StorageClient1 storageClient;

//    static {
//        try {
////            String conf_filename = FastDFSUtil.class.getClassLoader().getResource("fdfs_client.conf").getPath();
////            logger.info("当前获取的FDFS文件路径"+conf_filename);
////            ClientGlobal.init(conf_filename);
//
//            Properties properties = new Properties();
//            properties.put(ClientGlobal.PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS, connectTimeout);
//            properties.put(ClientGlobal.PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS, networkTimeout);
//            properties.put(ClientGlobal.PROP_KEY_CHARSET, charset);
//            properties.put(ClientGlobal.PROP_KEY_HTTP_ANTI_STEAL_TOKEN, httpAntiStealToken);
//            properties.put(ClientGlobal.PROP_KEY_HTTP_SECRET_KEY, httpSecretKey);
//            properties.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, trackerServer);
//            logger.info("FastDFS properties,{}", properties);
//            ClientGlobal.initByProperties(properties);
//            logger.info("FastDFS配置,{}", ClientGlobal.configInfo());
//        } catch (Exception e) {
//            logger.error("初始化FastDFS工具类异常,{}", e);
//            e.printStackTrace();
//        }
//    }




    public  String uploadFile(InputStream inputStream, String uploadFileName) {
        String fileId = "";
        //建立连接
//        TrackerClient tracker = new TrackerClient();
//        TrackerServer trackerServer = null;
        byte[] fileBuff = null;
        try {
            String fileExtName = "";
            if (uploadFileName.contains(".")) {
                fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
                String fileType = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1, uploadFileName.length());
                if (!Arrays.asList("jpg", "png", "gif", "jpeg","apk","pdf").contains(fileType.toLowerCase())) {
                    logger.warn("Fail to upload file, because the file type is not correct");
                    return fileId;
                }
            } else {
                logger.warn("Fail to upload file, because the format of filename is illegal.");
                return fileId;
            }
            fileBuff = readStream(inputStream);
//            trackerServer = tracker.getConnection();
//            StorageServer storageServer = null;
//            StorageClient1 client = new StorageClient1(trackerServer, storageServer);
            fileId = storageClient.upload_file1(fileBuff, fileExtName.toLowerCase(), null);
        } catch (IOException e) {
            logger.error("上传失败,{}", e);
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("上传失败,{}", e);
            e.printStackTrace();
        } finally {
//            if (trackerServer != null) {
//                try {
//                    trackerServer.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        return fileId;
    }
    
    public String uploadVideoFile(InputStream inputStream, String uploadFileName) {
        String fileId = "";
        //建立连接
//        TrackerClient tracker = new TrackerClient();
//        TrackerServer trackerServer = null;
        byte[] fileBuff = null;
        try {
            String fileExtName = "";
            if (uploadFileName.contains(".")) {
                fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
                String fileType = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1, uploadFileName.length());
                if (!Arrays.asList("avi", "mpg", "mpeg", "wmv","mov","mp4","dat","mkv","flv","vob","rmvb","wmv").contains(fileType.toLowerCase())) {
                    logger.warn("Fail to upload file, because the file type is not correct");
                    return fileId;
                }
            } else {
                logger.warn("Fail to upload file, because the format of filename is illegal.");
                return fileId;
            }
            fileBuff = readStream(inputStream);
            fileId = storageClient.upload_file1(fileBuff, fileExtName.toLowerCase(), null);
        } catch (IOException e) {
            logger.error("上传失败,{}", e);
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("上传失败,{}", e);
            e.printStackTrace();
        } finally {
//            if (trackerServer != null) {
//                try {
//                    trackerServer.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        return fileId;
    }

    /**
     * @param file_id
     * @return
     * @throws
     * @Title: deleteFile
     * @Description: 删除文件 return 0 for success, none zero for fail (error code)
     * @author ganyunjing
     */
    public int deleteFile(String file_id) {

        if (StringUtil.isNullOrEmpty(file_id))
            return 0;
        int i = 0;
        try {
            //建立连接
//            TrackerClient tracker = new TrackerClient();
//            TrackerServer trackerServer = tracker.getConnection();
//            StorageServer storageServer = null;
//            StorageClient1 client = new StorageClient1(trackerServer, storageServer);

            i = storageClient.delete_file1(file_id);
//            trackerServer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(HEADER + "======deleteFile error=======" + ex);
        }
        return i;
    }

    public byte[] getFile(String fileId){
        byte[] result = null;
        try {
            result = storageClient.download_file1(fileId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String getFileToBase64(String fileId){
        String base64Res = null;
        byte [] fileByte = null;
        try {
            fileByte = storageClient.download_file1(fileId);
            BASE64Encoder encoder = new BASE64Encoder();
            if(null != fileByte && fileByte.length > 0){
                base64Res = encoder.encode(fileByte);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return base64Res;
    }

    protected byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        byte[] buffer = new byte[256 * 1024]; // 用于缓存每次读取的数据
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outstream.write(buffer, 0, len);
        }
        // 关闭流一定要记得。
        outstream.close();
        inStream.close();
        return outstream.toByteArray();
    }

}
