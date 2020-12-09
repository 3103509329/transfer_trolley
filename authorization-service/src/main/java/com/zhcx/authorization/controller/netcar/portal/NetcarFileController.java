package com.zhcx.authorization.controller.netcar.portal;

import com.google.common.collect.Lists;
import com.zhcx.authorization.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/2/25 17:33
 **/
@RestController
@RequestMapping("/netcar/files")
public class NetcarFileController {

    private static Logger log = LoggerFactory.getLogger(NetcarFileController.class);

    private String domain;

    @Value("${ftp.domain}")
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Autowired
    private FastDFSUtil fastDFSUtil;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @PostMapping("/upload")
    public MessageResp upload(HttpServletRequest request,@RequestParam("files")MultipartFile[] files,@RequestParam(required = false)String fileType){
        MessageResp messageResp = new MessageResp();
        try {
            String path = request.getSession().getServletContext().getRealPath("upload");
            log.info("图片上传开始.....");
            List<String> fileNameList = Lists.newArrayList();
            if (null != files && files.length > 0) {
                fileNameList = fileUploadUtil.upload(files, path);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("fileNames",fileNameList);
            messageResp.setResMap(map);
        }catch (Exception e){
            e.printStackTrace();
            log.error("上传图片异常");
        }
        return messageResp;
    }


    /**
     * base64 图片文件
     * @param url 文件绝对地址
     * @param response
     * @throws Exception
     */
    @GetMapping("/downloadBase64")
    public MessageResp downloadBase64File(@RequestParam (value = "url",required = false,defaultValue = "")String url,
                             HttpServletResponse response) throws Exception{
        MessageResp messageResp = new MessageResp();
        try {
            String buffer = fastDFSUtil.getFileToBase64(url);
            messageResp.setData(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("excel文件下载出现异常", e);
        }
        return messageResp;
    }

    /**
     * 下载文件
     * @param fileName 导出之后的文件名
     * @param fileId 文件绝对地址
     * @param response
     * @throws Exception
     */
    @GetMapping("/download")
    public void downloadFile(@RequestParam (value = "fileName",required = false,defaultValue = "")String fileName,
                              @RequestParam (value = "fileId",required = false,defaultValue = "")String fileId,
                              HttpServletResponse response) throws Exception{
        try {
            fileUploadUtil.download(fileName,fileId,response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件下载出现异常", e);
        }
    }

}
