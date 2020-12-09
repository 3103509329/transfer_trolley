package com.zhcx.authorization.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lisai
 * @email 570815140@qq.com
 * @date 2018-08-07 16:46
 */
@Slf4j
public class UploadConfigurationUtil {

    public static String upload(MultipartFile multipartFile, String path) {
        String fileName = multipartFile.getOriginalFilename();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        System.out.println(df.format(new Date()));
        //扩展名
        //abc.jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = fileName.substring(0,fileName.lastIndexOf(".")) +"-"+ df.format(new Date()) + "." + fileExtensionName;
        log.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}", fileName, path, uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);

        try {
            //文件上传到应用服务器
            multipartFile.transferTo(targetFile);

        } catch (IOException e) {
            log.error("上传文件异常:{}", e);
            return null;
        }
        return targetFile.getName();
    }
}
