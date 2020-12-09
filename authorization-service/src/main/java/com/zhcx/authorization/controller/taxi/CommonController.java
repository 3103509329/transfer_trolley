package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.*;
import com.zhcx.authorization.utils.excel.ExcelUtils;

import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoCompanyService;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoDriverService;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoTerminalService;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoVehicleService;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoCompany;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoDriver;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoVehicle;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: authorization-service
 * @ClassName:CommonController
 * @description: 出租-通用控制类
 * @author: ZhangKai
 * @create: 2018-12-01 16:57
 **/
@RestController
@RequestMapping("/taxi/common")
@Api(value = "common", tags = "出租车通用类接口")
public class CommonController {
    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);


    private static final int MAX_FILE_SIZE = 204800;

    @Autowired
    private FastDFSUtil fastDFSUtil;

    @Autowired
    private TaxiBaseInfoDriverService driverService;

    @Autowired
    private TaxiBaseInfoCompanyService companyService;

    @Autowired
    private TaxiBaseInfoVehicleService vehicleService;

    @Autowired
    private TaxiBaseInfoTerminalService terminalService;

    @Value("${zhcx.sync.streamax.url}")
    private String syncUrl;

    @Value("${zhcx.sync.streamax.userId}")
    private String userId;

    @Value("${zhcx.sync.streamax.userKey}")
    private String userKey;

    @PostMapping("/uploadImg")
    @ApiOperation(value = "上传图片", notes = "上传图片")
    public MessageResp uploadImg(@RequestParam MultipartFile file, String oldUrl) {
        MessageResp result = new MessageResp();
        String fileId = null;
        int delRes = 999;
        InputStream sourceFileStream = null;
        try {
            if (file.getBytes().length > MAX_FILE_SIZE) {
                sourceFileStream = zipImage(file);
            } else {
                sourceFileStream = file.getInputStream();
            }
            if (null != oldUrl && !"".equals(oldUrl)) {
                delRes = fastDFSUtil.deleteFile(oldUrl);
                if (delRes != 0) {
                    logger.warn("原图片删除失败");
                }
            }
            fileId = fastDFSUtil.uploadFile(sourceFileStream, file.getOriginalFilename());
            if (null == fileId || "".equals(fileId)) {
                return CommonUtils.returnErrorInfo("上传失败");
            }
            result.setData(fileId);
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("上传成功");
        } catch (IOException e) {
            logger.error("上传失败,{}", e);
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("上传失败");
        } finally {
            try {
                sourceFileStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * @param @param request
     * @param @param response
     * @param @param fileName
     * @return void
     * @throws
     * @Title: downloadExcleTemplate
     * @Description: 下载excel模板
     * @author lyl
     */
    @ResponseBody
    @RequestMapping("/downloadExcleTemplate")
    public void downloadExcleTemplate(HttpServletRequest request, HttpServletResponse response, String fileName) {
        try {
            ExcelUtils EexcelUtils = new ExcelUtils();
            if (StringUtils.isNotBlank(fileName)) {
                if (CommonUtils.isMessyCode(fileName)) {
                    fileName = new String(fileName.getBytes("iso-8859-1"), "utf-8");
                }
                EexcelUtils.downloadExcleTemplate(fileName, response, request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 同步公司、车辆、终端、驾驶员信息到锐明
     */
    @RequestMapping("/syncDataToStreaMax")
    public String syncData(String corpId, String sim, String vehicleId, String driverId) {

        Map<String, Object> param = new HashMap<>();
        if (StringUtils.isNotBlank(corpId) && StringUtils.isNotEmpty(corpId)) {
            param.put("corpId", corpId);
        }
        if (StringUtils.isNotBlank(sim) && StringUtils.isNotEmpty(sim)) {
            param.put("sim", sim);
        }
        if (StringUtils.isNotBlank(vehicleId) && StringUtils.isNotEmpty(vehicleId)) {
            param.put("vehicleId", vehicleId);
        }
        if (StringUtils.isNotBlank(driverId) && StringUtils.isNotEmpty(driverId)) {
            param.put("driverId", driverId);
        }

        Map<String, String> header = new HashMap<>();
        header.put("userId", userId);
        header.put("userKey", userKey);
        header.put("Content-Type", "application/Json");

        /**
         * 数据同步流程（按锐明方要求）
         * 公司→终端→车辆→司机
         * 以同步公司数据为起点，若前一个环节未返回成功，后面均不再同步
         */
        String dataStr = null;
        String result = null;

        StringBuilder res = new StringBuilder();


        List<Map<String, Object>> companyInfos = companyService.syncCompany();
        dataStr = JSONArray.toJSONString(companyInfos);
        result = HttpClientUtils.httpRequest(syncUrl + "synCompany", "POST", header, dataStr);
        if (!isSuccess(result)) {
            logger.info("同步企业信息到锐明端异常");
            return res.append("同步企业信息到锐明端异常").toString();
        }
        logger.info("同步企业信息成功：" + companyInfos.size() + "条,数据内容：" + dataStr);
        res.append("同步企业信息成功：" + companyInfos.size() + "条");

        List<Map<String, Object>> terminalInfos = terminalService.syncTerminal();
        dataStr = JSONArray.toJSONString(terminalInfos);
        result = HttpClientUtils.httpRequest(syncUrl + "synMdt", "POST", header, dataStr);
        if (!isSuccess(result)) {
            logger.info("同步终端设备信息到锐明端异常");
            return res.append("同步终端设备信息到锐明端异常").toString();
        }
        logger.info("同步终端设备信息成功：" + terminalInfos.size() + "条,数据内容：" + dataStr);
        res.append("同步终端设备信息成功：" + terminalInfos.size() + "条");

        List<Map<String, Object>> vehicleInfos = vehicleService.syncVehicle(param);
        dataStr = JSONArray.toJSONString(vehicleInfos);
        result = HttpClientUtils.httpRequest(syncUrl + "synCar", "POST", header, dataStr);
        if (!isSuccess(result)) {
            logger.info("同步车辆信息到锐明端异常");
            return res.append("同步车辆信息到锐明端异常").toString();
        }
        logger.info("同步车辆信息成功：" + vehicleInfos.size() + "条,数据内容：" + dataStr);
        res.append("同步车辆信息成功：" + vehicleInfos.size() + "条");


        param.put("pageNo", 1);
        param.put("pageSize", 30);
        PageInfo<Map<String, Object>> driverInfoPage = driverService.syncDriverInfoByPage(param);
        List<Map<String, Object>> driverInfoList = null;
        int allCount = 0;
        int pageCount = driverInfoPage.getPages();
        for (int i = 0; i < pageCount; i++) {
            param.put("pageNo", i+1);
            driverInfoList = driverService.syncDriverInfoByPage(param).getList();
            String driverPhoto = null;
            String photoBase64 = null;
            for (Map<String, Object> driver : driverInfoList) {
                if (null != driver.get("photoFile")) {
                    driverPhoto = driver.get("photoFile").toString();
                    if (null != driverPhoto) {
                        photoBase64 = fastDFSUtil.getFileToBase64(driverPhoto);
                        driver.put("photoFile", photoBase64);
                    }
                }
            }
            dataStr = JSONArray.toJSONString(driverInfoList);
            result = HttpClientUtils.httpRequest(syncUrl + "synDriver", "POST", header, dataStr);
            if (!isSuccess(result)) {
                logger.info("同步驾驶员信息到锐明端异常");
                return res.append("同步驾驶员信息到锐明端异常").toString();
            }
            allCount = allCount + driverInfoList.size();
        }
        logger.info("同步驾驶员信息成功：" + allCount + "条");
        res.append("同步驾驶员信息成功：" + allCount + "条");


//        List<Map<String,Object>> driverInfos = driverService.syncDriverInfo(param);
//        String driverPhoto = null;
//        String photoBase64 = null;
//        for(Map<String,Object> driver : driverInfos){
//            if(null != driver.get("photoFile")){
//                driverPhoto = driver.get("photoFile").toString();
//                if(null != driverPhoto){
//                    photoBase64 = fastDFSUtil.getFileToBase64(driverPhoto);
//                    driver.put("photoFile",photoBase64);
//                }
//            }
//        }
//        dataStr = JSONArray.toJSONString(driverInfos);
//        result = HttpClientUtils.httpRequest(syncUrl+"synDriver","POST",header,dataStr);
//        if(!isSuccess(result)){
//            logger.info("同步驾驶员信息到锐明端异常");
//            return res.append("同步驾驶员信息到锐明端异常").toString();
//        }
//        logger.info("同步驾驶员信息成功："+driverInfos.size()+"条");
//        res.append("同步驾驶员信息成功："+driverInfos.size()+"条");
        return res.toString();
    }

    
    /**
     * @param @param request
     * @param @param response
     * @return String
     * @throws
     * @Title: getSysTime
     * @Description: 系统当前时间
     * @author td
     */
    @ResponseBody
    @RequestMapping("/getSysTime")
    public Date getSysTime(HttpServletRequest request, HttpServletResponse response) {
      return new Date();
    }
    
    /**
     * 同步数据到锐明后锐明返回的结果分析公用类s
     *
     * @param result
     * @return
     */
    protected boolean isSuccess(String result) {
        boolean flag = false;
        if (null == result || "".equals(result)) {
            return flag;
        }
        JSONObject obj = JSONObject.parseObject(result);
        String success = obj.getString("success");
        if (success.equals("1")) {
            flag = true;
        } else {
            logger.error(obj.getString("msg"));
        }
        return flag;
    }


    /**
     * 图片压缩
     *
     * @param file
     * @return
     */
    protected InputStream zipImage(MultipartFile file) {
        BufferedImage thumbnail = null;
        InputStream zipResult = null;
        ByteArrayOutputStream bs = null;
        ImageOutputStream imgOut = null;
        try {
//            thumbnail = Thumbnails.of(file.getInputStream()).scale(0.25f).asBufferedImage();
            thumbnail = Thumbnails.of(file.getInputStream()).size(810, 1440).asBufferedImage();
            bs = new ByteArrayOutputStream();
            imgOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(thumbnail, "jpg", imgOut);
            zipResult = new ByteArrayInputStream(bs.toByteArray());
        } catch (IOException e) {
            logger.error("图片压缩异常");
            e.printStackTrace();
        } finally {
            try {
                bs.close();
                imgOut.close();
                zipResult.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return zipResult;
    }

}
