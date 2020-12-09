package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.*;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoCompanyService;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoDriverService;
import com.zhcx.basicdata.facade.taxi.TaxiOperationLogService;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoDriver;
import com.zhcx.basicdata.pojo.taxi.TaxiOperationLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/taxi/driver")
@Api(value = "driver", tags = "出租车驾驶员driver接口")
public class TaxiDriverController {

    private Logger log = LoggerFactory.getLogger(TaxiDriverController.class);

    @Autowired
    private TaxiBaseInfoDriverService driverService;

    @Autowired
    private FastDFSUtil fastDFSUtil;

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Autowired
    private TaxiBaseInfoCompanyService taxiBaseInfoCompanyService;

    @Autowired
    private TaxiOperationLogService logService;

    @Value("${zhcx.sync.streamax.url}")
    private String syncUrl;

    @Value("${zhcx.sync.streamax.userId}")
    private String userId;

    @Value("${zhcx.sync.streamax.userKey}")
    private String userKey;

    String oldCertificateNumber = null;

    @GetMapping("/queryDriverList")
    @ApiOperation(value = "查询驾驶员信息列表", notes = "参数Driver")
    public MessageResp queryDriverList(HttpServletRequest request, @ModelAttribute TaxiBaseInfoDriver param) {
        MessageResp resp = new MessageResp();
        PageInfo<TaxiBaseInfoDriver> pageInfo = null;
        try {
            AuthUserResp authUser = sessionHandler.getUser(request);
            //非管理员用户只能够查看自己企业下所属驾驶员
            if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
                param.setCorpId(String.valueOf(authUser.getCorpId()));
            }
            pageInfo = driverService.queryDriverList(param);
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setData(pageInfo.getList());
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询驾驶员列表成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询驾驶员列表异常");
        }
        return resp;
    }

    @GetMapping("/queryDriverInfo")
    @ApiOperation(value = "查询驾驶员详情信息", notes = "参数uuid")
    public MessageResp queryDriverInfo(@RequestParam String uuid) {
        MessageResp resp = new MessageResp();
        try {
            TaxiBaseInfoDriver driver = driverService.selectDriver(uuid);
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
            resp.setResultDesc("查询驾驶员信息成功");
            resp.setData(driver);
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
//            resp.setResultDesc("查询驾驶员信息异常");
        }
        return resp;
    }

    @PostMapping("/insertDriver")
    @ApiOperation(value = "添加驾驶员信息", notes = "参数Driver")
    public MessageResp insertDriver(HttpServletRequest request, @RequestBody TaxiBaseInfoDriver driver) {
        MessageResp resp = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_C);
        try {
            //驾驶员身份证号码唯一性侥幸
            TaxiBaseInfoDriver param = new TaxiBaseInfoDriver();
            param.setIdCertificateNumber(driver.getIdCertificateNumber());
            PageInfo<TaxiBaseInfoDriver> pageInfo = driverService.queryDriverList(param);
            List<TaxiBaseInfoDriver> driverList = pageInfo.getList();
            if (driverList != null && driverList.size() > 0) {
                resp.setResult(Boolean.FALSE.toString());
                resp.setStatusCode("-50");
                resp.setResultDesc("驾驶员身份证件号码已存在，请核对");
                return resp;
            }
            param.setIdCertificateNumber(null);
            param.setPhoneNumber(driver.getPhoneNumber());
            pageInfo = driverService.queryDriverList(param);
            driverList = pageInfo.getList();
            if (driverList != null && driverList.size() > 0) {
                resp.setResult(Boolean.FALSE.toString());
                resp.setStatusCode("-50");
                resp.setResultDesc("驾驶员手机号码已存在，请核对");
                return resp;
            }
            param.setPhoneNumber(null);
            param.setDrivingLicenseNumber(driver.getDrivingLicenseNumber());
            pageInfo = driverService.queryDriverList(param);
            driverList = pageInfo.getList();
            if (driverList != null && driverList.size() > 0) {
                resp.setResult(Boolean.FALSE.toString());
                resp.setStatusCode("-50");
                resp.setResultDesc("驾驶员驾驶证号码已存在，请核对");
                return resp;
            }
            param.setDrivingLicenseNumber(null);
            param.setCertificateNumber(driver.getCertificateNumber());
            pageInfo = driverService.queryDriverList(param);
            driverList = pageInfo.getList();
            if (driverList != null && driverList.size() > 0) {
                resp.setResult(Boolean.FALSE.toString());
                resp.setStatusCode("-50");
                resp.setResultDesc("驾驶员从业资格证号码已存在，请核对");
                return resp;
            }
            AuthUserResp authUser = sessionHandler.getUser(request);

            if (authUser.getUserType().equals("01")) {
                if (null == driver.getCorpId() || "".equals(driver.getCorpId())) {
                    return CommonUtils.returnErrorInfo("请选择驾驶员所属企业");
                }
            } else {
                driver.setCorpId(String.valueOf(authUser.getCorpId()));
            }
            driver.setCreateBy(authUser.getUserName());
            logInfo.setOperatorName(authUser.getUserName());
            driverService.insertDriver(driver);
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("添加成功");
            resp.setStatusCode("00");
            resp.setData(driver);
            //同步驾驶员数据到锐明
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        syncDataDriver(driver, true, null, logInfo);
                    }
                }).start();
            } catch (Exception e) {
                log.info("调用同步驾驶员数据的方法异常:" + e.getMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("添加驾驶员信息失败");
        }
        return resp;
    }

    @PutMapping("/updateDriver")
    @ApiOperation(value = "更新驾驶员信息", notes = "参数Driver")
    public MessageResp updateDriver(HttpServletRequest request, @RequestBody TaxiBaseInfoDriver driver) {
        MessageResp resp = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_U);
        try {
            //由于需要同步数据至锐明处，对于驾驶员从业资格证号修改需要将修改前的数据也同步过去，故多一步查询操作

            TaxiBaseInfoDriver oldDriver = driverService.selectDriver(driver.getUuid());
            if (null != oldDriver && !oldDriver.getCertificateNumber().equals(driver.getCertificateNumber())) {
                oldCertificateNumber = oldDriver.getCertificateNumber();
            }

            //修改企业 先解绑车辆和驾驶员
            if (null != oldDriver && !oldDriver.getCorpId().equals(driver.getCorpId())) {
                if (!"02".equals(driver.getBindStatus())) {
                    resp.setResult(Boolean.FALSE.toString());
                    resp.setStatusCode("-50");
                    resp.setResultDesc("更新失败,请先解绑车辆");
                    return resp;
                }
            }

            //驾驶员身份证号码唯一性侥幸
            TaxiBaseInfoDriver param = new TaxiBaseInfoDriver();
            param.setIdCertificateNumber(driver.getIdCertificateNumber());
            TaxiBaseInfoDriver dbDriver = null;
            PageInfo<TaxiBaseInfoDriver> pageInfo = driverService.queryDriverList(param);
            List<TaxiBaseInfoDriver> driverList = pageInfo.getList();
            if (driverList != null && driverList.size() > 0) {
                dbDriver = driverList.get(0);
                if (StringUtils.equals(dbDriver.getIdCertificateNumber(), driver.getIdCertificateNumber())
                        && !StringUtils.equals(dbDriver.getUuid(), driver.getUuid())) {
                    resp.setResult(Boolean.FALSE.toString());
                    resp.setStatusCode("-50");
                    resp.setResultDesc("驾驶员身份证件号码已存在，请核对");
                    return resp;
                }
            }
            param.setIdCertificateNumber(null);
            param.setPhoneNumber(driver.getPhoneNumber());
            pageInfo = driverService.queryDriverList(param);
            driverList = pageInfo.getList();
            if (driverList != null && driverList.size() > 0) {
                dbDriver = driverList.get(0);
                if (StringUtils.equals(dbDriver.getPhoneNumber(), driver.getPhoneNumber())
                        && !StringUtils.equals(dbDriver.getUuid(), driver.getUuid())) {
                    resp.setResult(Boolean.FALSE.toString());
                    resp.setStatusCode("-50");
                    resp.setResultDesc("驾驶员手机号码已存在，请核对");
                    return resp;
                }
            }
            param.setPhoneNumber(null);
            param.setDrivingLicenseNumber(driver.getDrivingLicenseNumber());
            pageInfo = driverService.queryDriverList(param);
            driverList = pageInfo.getList();
            if (driverList != null && driverList.size() > 0) {
                dbDriver = driverList.get(0);
                if (StringUtils.equals(dbDriver.getDrivingLicenseNumber(), driver.getDrivingLicenseNumber())
                        && !StringUtils.equals(dbDriver.getUuid(), driver.getUuid())) {
                    resp.setResult(Boolean.FALSE.toString());
                    resp.setStatusCode("-50");
                    resp.setResultDesc("驾驶员驾驶证号码已存在，请核对");
                    return resp;
                }
            }
            param.setDrivingLicenseNumber(null);
            param.setCertificateNumber(driver.getCertificateNumber());
            pageInfo = driverService.queryDriverList(param);
            driverList = pageInfo.getList();
            if (driverList != null && driverList.size() > 0) {
                dbDriver = driverList.get(0);
                if (StringUtils.equals(dbDriver.getCertificateNumber(), driver.getCertificateNumber())
                        && !StringUtils.equals(dbDriver.getUuid(), driver.getUuid())) {
                    resp.setResult(Boolean.FALSE.toString());
                    resp.setStatusCode("-50");
                    resp.setResultDesc("驾驶员从业资格证号码已存在，请核对");
                    return resp;
                }
            }
            AuthUserResp authUser = sessionHandler.getUser(request);
            driver.setUpdateBy(authUser.getUserName());
            logInfo.setOperatorName(authUser.getUserName());
            driverService.updateDriver(driver);
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("更新驾驶员信息成功");
            resp.setStatusCode("00");
            resp.setData(driver);

            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        syncDataDriver(driver, false, oldCertificateNumber, logInfo);
                    }
                }).start();
            } catch (Exception e) {
                log.info("调用同步驾驶员数据的方法异常:" + e.getMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("更新驾驶员信息失败");
        }
        return resp;
    }

    @DeleteMapping("/deleteDriver")
    @ApiOperation(value = "删除驾驶员信息", notes = "参数Driver")
    public MessageResp deleteDriver(@RequestParam String uuid) {
        MessageResp resp = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_D);
        try {
            //查询驾驶员绑定车辆记录数
            int count = driverService.queryBindVehicleCount(uuid);
            if (count > 0) {
                resp.setResult(Boolean.FALSE.toString());
                resp.setStatusCode("-50");
                resp.setResultDesc("该驾驶员已关联车辆和企业，请勿删除");
                return resp;
            }

            TaxiBaseInfoDriver driver = driverService.selectDriver(uuid);
            driverService.deleteDriver(uuid);
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("删除成功");
            resp.setStatusCode("00");
            //删除司机数据同步到锐明
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        syncDriverDelete(driver, logInfo);
                    }
                }).start();
            } catch (Exception e) {
                log.error("删除司机数据同步到锐明异常" + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("删除车辆信息失败");
        }
        return resp;
    }


    //
    @GetMapping("/getDriverForMointor")
    @ApiOperation(value = "实时监控查询驾驶员信息", notes = "实时监控查询驾驶员信息")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", dataType = "String", name = "deviceId", value = "车辆deviceId", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "vehicleId", value = "车辆UUID", required = true)})
    public MessageResp getDriverForMointor(String deviceId, String vehicleId) {
        MessageResp result = new MessageResp();
        JSONObject data = null;
        String driverUuid = null;
        String checkinTime = null;//打卡时间
        try {
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("查询成功");
            //查询
            String res = getWorkInDriverUuid(vehicleId);
            if (StringUtils.isBlank(res)) {
                log.error("当前车辆的没有驾驶员打卡信息");
//                return result;
            }
            if (StringUtils.isNotBlank(res)) {
                String[] split = res.split(",");
                driverUuid = split[0];
                checkinTime = split[1];
                Long time = Long.valueOf(checkinTime);
                Date d = new Date(time);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                checkinTime = sdf.format(d);

            }
            //查询关联驾驶员,关联设备
            data = driverService.qeuryDriverForMointor(deviceId, driverUuid);
            if (data != null && !"".equals(data)) {
                data.put("checkinTime", checkinTime);
            }
            result.setData(data);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("实时监控查询驾驶员异常:" + e.getMessage());
            result = CommonUtils.returnErrorInfo(null);
        }
        return result;
    }


    protected String getWorkInDriverUuid(String vehicleId) throws Exception {

//        plateNum = plateNum.substring(plateNum.indexOf("湘")+1,plateNum.length());
        String result = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -5);// 5分钟之前的时间
        Date beforeD = beforeTime.getTime();
        String time = sdf.format(beforeD);

        StringBuilder sql = new StringBuilder();
        sql.append(" select tmp.DRIVER_ID,tmp.CHECKIN_TIME from ( ");
        sql.append("select CHECKIN_TIME,DRIVER_ID,max(__time) as checkin_time1 from taxi_check_in where ");
        sql.append(" VEHICLE_ID = '").append(vehicleId).append("'");
        sql.append(" group by DRIVER_ID,CHECKIN_TIME ) tmp  order by tmp.checkin_time1 DESC limit 1");
        JSONArray res = null;
        try {
            res = httpUtils.doPostSqlUrl("sql", sql.toString());
            if (res != null && res.size() > 0) {
                if (res.size() > 1) {
                    log.error("数据异常,当前车辆" + vehicleId + "同一时间存在两个驾驶员打卡记录");
                } else {
                    JSONObject obj = res.getJSONObject(0);
                    String checkinTime = obj.get("CHECKIN_TIME").toString();
                    result = obj.get("DRIVER_ID").toString() + "," + checkinTime;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    //同步司机数据
    public void syncDataDriver(TaxiBaseInfoDriver driver, boolean isAdd, String oldKey, TaxiOperationLog logInfo) {
        Map<String, String> header = new HashMap<>(3);
        header.put("userId", userId);
        header.put("userKey", userKey);
        header.put("Content-Type", "application/Json");
        Map<String, Object> syncData = new HashMap<>();
        String dataStr = null;
        String result = null;
        Map<String, Object> syncDataDriver = new HashMap<>();
        try {
            //新增驾驶员构造数据，修改驾驶员直接根据uuid查询数据
            if (isAdd) {
                syncDataDriver.put("name", driver.getName());
                syncDataDriver.put("phone", driver.getPhoneNumber());
                syncDataDriver.put("personalId", driver.getIdCertificateNumber());
                syncDataDriver.put("certificateId", driver.getCertificateNumber());
                syncDataDriver.put("oldCertificateId", "");
                syncDataDriver.put("validityStartTime", driver.getCertificateEffectiveDate());
                syncDataDriver.put("validityTime", driver.getCertificateExpireData());
                syncDataDriver.put("companyName", taxiBaseInfoCompanyService.selectCompanyInfo(driver.getCorpId()).getName());
                syncDataDriver.put("carNo", "");
                if (null != driver.getPhoto()) {
                    String photoBase64 = fastDFSUtil.getFileToBase64(driver.getPhoto());
                    syncDataDriver.put("photoFile", photoBase64);
                }
                dataStr = "[" + JSONObject.toJSONString(syncDataDriver) + "]";
                logInfo.setOperation(dataStr);

            } else {
                Map<String, Object> param = new HashMap<>();
                param.put("driverId", driver.getUuid());
                List<Map<String, Object>> driverInfo = driverService.syncDriverInfo(param);
                if (null != oldKey && !"".equals(oldKey)) {
                    for (Map<String, Object> driverInfo1 : driverInfo) {
                        driverInfo1.put("oldCertificateId", oldKey);
                        if (null != driver.getPhoto()) {
                            String photoBase64 = fastDFSUtil.getFileToBase64(driver.getPhoto());
                            driverInfo1.put("photoFile", photoBase64);
                        }
                    }
                }
                dataStr = JSONArray.toJSONString(driverInfo);
                logInfo.setOperation(dataStr);
            }
            logInfo.setLoginIp(syncUrl + "synDriver");
            result = HttpClientUtils.httpRequest(syncUrl + "synDriver", "POST", header, dataStr);
            if (!isSuccess(result)) {
                log.info("同步司机数据异常" + dataStr);
                logInfo.setResult(Constants.RESULT_TYPE_FALSE);
                logService.insertInfo(logInfo);
            }
            log.info("同步司机数据成功" + dataStr);
            logInfo.setResult(Constants.RESULT_TYPE_TRUE);
            logService.insertInfo(logInfo);

        } catch (Exception e) {
            log.info("同步司机数据异常" + dataStr);
            e.printStackTrace();
            logInfo.setResult(Constants.RESULT_TYPE_FALSE);
            logInfo.setRemark(e.getMessage());
            logService.insertInfo(logInfo);
        }

    }

    //同步删除司机数据
    private void syncDriverDelete(TaxiBaseInfoDriver driver, TaxiOperationLog logInfo) {
        Map<String, String> header = new HashMap<>(3);
        header.put("userId", userId);
        header.put("userKey", userKey);
        header.put("Content-Type", "application/Json");
        Map<String, Object> syncData = new HashMap<>();
        String dataStr = null;
        String result = null;
        try {
            syncData.put("name", driver.getName());
            syncData.put("phone", driver.getPhoneNumber());
            syncData.put("personalId", driver.getIdCertificateNumber());
            syncData.put("certificateId", driver.getCertificateNumber());
            syncData.put("oldCertificateId", "");
            syncData.put("validityStartTime", driver.getCertificateEffectiveDate());
            syncData.put("validityTime", driver.getCertificateExpireData());
            syncData.put("companyName", taxiBaseInfoCompanyService.selectCompanyInfo(driver.getCorpId()).getName());
            syncData.put("carNo", "");
            syncData.put("delete", Boolean.TRUE.toString());
            dataStr = "[" + JSONObject.toJSONString(syncData) + "]";
            logInfo.setOperation(dataStr);
            logInfo.setLoginIp(syncUrl + "synDriver");
            result = HttpClientUtils.httpRequest(syncUrl + "synDriver", "POST", header, dataStr);
            JSONObject obj = JSONObject.parseObject(result);
            if (obj.get("success").equals(1)) {
                log.info("同步司机删除数据成功：" + dataStr);
                logInfo.setResult(Constants.RESULT_TYPE_TRUE);
                logService.insertInfo(logInfo);
            }
        } catch (Exception e) {
            log.info("同步司机删除数据失败：" + dataStr);
            e.printStackTrace();
            logInfo.setResult(Constants.RESULT_TYPE_FALSE);
            logInfo.setRemark(e.getMessage());
            logService.insertInfo(logInfo);
        }
    }

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
            log.error(obj.getString("msg"));
        }
        return flag;
    }

}
