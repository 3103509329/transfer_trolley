package com.zhcx.authorization.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

/**
 * @author lisai
 * @email 570815140@qq.com
 * @date 2018-08-07 16:46
 */
@Component
public class FileUploadUtil {

    private static Logger log = LoggerFactory.getLogger(FileUploadUtil.class);

    @Autowired
    private FastDFSUtil fastDFSUtil;

    public List<String> upload(MultipartFile[] multipartFiles, String path) {

        List<String> fileNameList = Lists.newArrayList();
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                String fileName = multipartFile.getOriginalFilename();
                //扩展名
                //abc.jpg
                String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
                String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
                log.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}", fileName, path, uploadFileName);

                InputStream sourceFileStream = multipartFile.getInputStream();
                String fileId = fastDFSUtil.uploadFile(sourceFileStream, multipartFile.getOriginalFilename());
                fileNameList.add(fileId);
            }

        } catch (IOException e) {
            log.error("上传文件异常:{}", e);
            return null;
        }
        return fileNameList;
    }


    /**
     * 文件下载
     * @param fileName
     * @param fileId
     * @param response
     * @throws Exception
     */
    public void download(String fileName, String fileId, HttpServletResponse response) throws Exception {
        byte[] buffer = fastDFSUtil.getFile(fileId);
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.reset();
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + "." + fileExtensionName);
        OutputStream ouputStream = response.getOutputStream();
        ouputStream.write(buffer);
        ouputStream.flush();
        ouputStream.close();
    }
}
