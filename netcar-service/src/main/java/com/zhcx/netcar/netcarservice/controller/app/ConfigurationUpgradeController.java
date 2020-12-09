package com.zhcx.netcar.netcarservice.controller.app;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zhcx.netcar.facade.app.ConfigurationUpgradeService;
import com.zhcx.netcar.netcarservice.utils.FastDFSUtil;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import com.zhcx.netcar.netcarservice.utils.UploadConfigurationUtil;
import com.zhcx.netcar.pojo.app.ConfigurationUpgrade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-28 09:50
 * app安装包更新
 */
@RestController
@RequestMapping("/netcar/upgrade")
@Api(value = "", tags = "app安装包管理（App）")
public class ConfigurationUpgradeController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationUpgradeController.class);

    @Autowired
    ConfigurationUpgradeService configurationUpgradeService;

    @Value("${upgrade.path}")
    String path;

    @Autowired
    private FastDFSUtil fastDFSUtil;
    /**
     * 上传配置升级文件
     * @param request
     * @return
     */
//    @SystemControllerLog(description = "上传配置升级文件")
    @RequestMapping("/upload")
    @ApiOperation(value = "", notes = "app安装包上传（App）")
    public MessageResp uploadConfigurationUpgrade(HttpServletRequest request) {
        MessageResp result = new MessageResp();
        try{
            StandardMultipartHttpServletRequest httpServletRequest = (StandardMultipartHttpServletRequest) request;
            Iterator<String> iterator = httpServletRequest.getFileNames();
            List<MultipartFile> multipartFileList = Lists.newArrayList();
            while (iterator.hasNext()) {
                MultipartFile multipartFile = httpServletRequest.getFile(iterator.next());
                String fileNames = multipartFile.getOriginalFilename();
                System.out.println("fileNames = " + fileNames);
                multipartFileList.add(multipartFile);
            }
            String fileName = "";
            for (MultipartFile multipartFile : multipartFileList) {
                fileName = UploadConfigurationUtil.upload(multipartFile, path);
                //扩展名
                String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
                System.out.println("fileName:"+fileName+",fileExtensionName:"+fileExtensionName);
            }

            //上传到文件服务器
            File beforeFile = new File(path+fileName);
            InputStream inputStream = new FileInputStream(beforeFile);
            String exportFileName = "";
            exportFileName = fastDFSUtil.uploadFile(inputStream,fileName);

            result.setData(exportFileName);
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * 保存
     * @param version
     * @param fileName
     * @param content
     * @return
     */
//    @SystemControllerLog(description = "保存升级文件")
    @RequestMapping("/save")
    @ApiOperation(value = "", notes = "app安装包保存（App）")
    public MessageResp saveConfigurationUpgrade(@RequestParam(value = "version",defaultValue = "")String version,
                                                @RequestParam(value = "fileName",defaultValue = "")String fileName,
                                                @RequestParam(value = "content",defaultValue = "")String content) {
        MessageResp result = new MessageResp();
        try{
            configurationUpgradeService.insertConfigurationUpgrade(version,content,fileName);
            result.setData(null);
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * apk下载
     * @param fileName
     * @param url
     * @param response
     * @throws Exception
     */
    @GetMapping("/download")
    @ApiOperation(value = "", notes = "app安装包下载（App）")
    public void downloadExcel(@RequestParam(value = "fileName",required = false,defaultValue = "")String fileName,
                              @RequestParam(value = "url",required = false,defaultValue = "")String url,
                              HttpServletResponse response) throws Exception{
        try {
            byte[] buffer = fastDFSUtil.getFile(url);
            response.reset();
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".apk");
            OutputStream ouputStream = response.getOutputStream();
            ouputStream.write(buffer);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("apk文件下载出现异常", e);
        }
    }


    /**
     * 列表查询
     * @param page
     * @param pageSize
     * @param keyword 查询关键字
     * @return
     */
    @GetMapping("/list")
    public MessageResp getConfigurationUpgrade(@RequestParam(value = "page",defaultValue = "1")int page,
                                               @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
                                               @RequestParam(value = "keyword",defaultValue = "")String keyword) {
        MessageResp result = new MessageResp();
        try {
            PageInfo<ConfigurationUpgrade> pageInfo = configurationUpgradeService.getConfigurationUpgrade(page,pageSize,keyword);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * 删除
     * @param uuid
     * @return
     */
    @DeleteMapping("/delete")
    public MessageResp deleteConfigurationUpgrade(@RequestParam(value = "uuid",defaultValue = "0")int uuid) {
        MessageResp result = new MessageResp();
        try {
            configurationUpgradeService.deleteConfigurationUpgrade(uuid);
            result.setData(null);
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }





}