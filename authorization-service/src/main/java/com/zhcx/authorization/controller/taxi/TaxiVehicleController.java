package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig.SessionHandler;
import com.zhcx.authorization.utils.*;
import com.zhcx.authorization.utils.excel.ExportExcel;
import com.zhcx.basicdata.facade.taxi.*;
import com.zhcx.basicdata.pojo.taxi.*;
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
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @program: authorization-service
 * @ClassName:TaxiVehicleController
 * @description: 出租车-车辆信息控制层
 * @author: ZhangKai
 * @create: 2018-11-26 15:30
 **/
@RestController
@RequestMapping("/taxi/vehicle")
@Api(value = "vehicle", tags = "出租车车辆vehicle接口")
public class TaxiVehicleController {


    private static final Logger logger = LoggerFactory.getLogger(TaxiVehicleController.class);


    @Autowired
    private TaxiBaseInfoVehicleService taxiBaseInfoVehicleService;

    @Autowired
    private SessionHandler sessionHandler;

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private TaxiBaseInfoCompanyService taxiBaseInfoCompanyService;

    @Autowired
    private TaxiBaseInfoDriverService taxiBaseInfoDriverService;

    @Autowired
    private TaxiVehicleSimService taxiVehicleSimService;

    @Autowired
    private TaxiOperationLogService logService;

    @Autowired
    private KafkaResultTaxiOnofflineService onofflineService;

    @Autowired
    private TaxiAlarmBlackListService taxiAlarmBlackListService;

    @Value("${druid.table.taxi.status}")
    private String taxiStatusTable;


    @Value("${zhcx.sync.streamax.url}")
    private String syncUrl;

    @Value("${zhcx.sync.streamax.userId}")
    private String userId;

    @Value("${zhcx.sync.streamax.userKey}")
    private String userKey;

    String oldPlateNumber = null; //同步数据 新建线程需要用到 (解决内部类需要final)


    //查询离线车辆 Offline vehicles
    @RequestMapping(value = "/queryOfflineVehicles", method = RequestMethod.GET)
    @ApiOperation(value = "查询离线车辆信息", notes = "离线车辆信息对象")
    public MessageResp<List<KafkaResultTaxiOnoffline>> queryOfflineVehicles(HttpServletRequest request, @ModelAttribute KafkaResultTaxiOnoffline param) {

        MessageResp<List<KafkaResultTaxiOnoffline>> result = new MessageResp<>();
        PageInfo<KafkaResultTaxiOnoffline> pageInfo = null;
        try {
            onofflineService.queryOfflineVehList(param);//先处理没有上报过在离线状态的车辆
            pageInfo = onofflineService.queryOfflineVehicles(param);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("查询成功");
        } catch (Exception e) {
            logger.error("查询离线车辆异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return result;
    }

    //查询在离线统计
    @RequestMapping(value = "/queryOfflineData", method = RequestMethod.GET)
    @ApiOperation(value = "查询离线统计信息", notes = "离线车辆信息对象")
    public MessageResp queryOfflineData(HttpServletRequest request, @ModelAttribute KafkaResultTaxiOnoffline param) {
        if (param == null) {
            return CommonUtils.returnErrorInfo("参数不能为空");
        }
        if (StringUtils.isBlank(param.getStartTime()) || StringUtils.isBlank(param.getEndTime())) {
            return CommonUtils.returnErrorInfo("请传递时间参数");
        }
        MessageResp result = new MessageResp();
        try {
            Map<String, Object> resMap = onofflineService.queryOfflineData(param);

            result.setData((List<TaxiOnofflineBean>) resMap.get("result"));
            PageBean pageBean = new PageBean();
            pageBean.setPageNo(String.valueOf(param.getPageNo()));
            pageBean.setPageSize(String.valueOf(param.getPageSize()));
            pageBean.setPageCount((String) resMap.get("pageCount"));
            pageBean.setPageDataCount((String) resMap.get("pageDataCount"));
            result.setPageBean(pageBean);
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("查询成功");
        } catch (Exception e) {
            logger.error("查询离线统计信息异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/onOffLineDataExport")
    public MessageResp export(HttpServletRequest request, HttpServletResponse response, @ModelAttribute KafkaResultTaxiOnoffline param) throws Exception {

        if (param == null) {
            return CommonUtils.returnErrorInfo("参数不能为空");
        }
        if (StringUtils.isBlank(param.getStartTime()) || StringUtils.isBlank(param.getEndTime())) {
            return CommonUtils.returnErrorInfo("请传递时间参数");
        }

        //需要导出的数据
        List<TaxiOnofflineExport> exportList = onofflineService.findExportData(param);

        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String xlsname = "车辆在线情况表";
        String filepath = this.getClass().getClassLoader().getResource("").getPath() + "exceltemplate/" + xlsname + ".xlsx";
        // 导出数据到模板
        ExportExcel<TaxiOnofflineExport> exportExcel = new ExportExcel<>(TaxiOnofflineExport.class);
        exportExcel.exportExcelByExcelTemplateStyle2(filepath, xlsname, exportList, "yyyy-MM-dd HH:mm:ss", request, response);

        return null;
    }

    @RequestMapping(value = "/List", method = RequestMethod.GET)
    @ApiOperation(value = "查询出租车辆信息", notes = "参数为出租车辆信息对象")
    public MessageResp<List<TaxiBaseInfoVehicle>> queryTaxiBaseInfoVehicleList(HttpServletRequest request, @ModelAttribute TaxiBaseInfoVehicle param) {
        MessageResp<List<TaxiBaseInfoVehicle>> result = new MessageResp<>();
        AuthUserResp authUser = sessionHandler.getUser(request);
        //非管理员用户只能够查看自己企业下所属车辆
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
            if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                param.setCorpId(String.valueOf(authUser.getCorpId()));
            }
        }
        try {
            List<TaxiBaseInfoVehicle> list = taxiBaseInfoVehicleService.queryVehicleList(param);
            result.setData(list);
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("查询成功");
        } catch (Exception e) {
            logger.error("查询异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return result;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "查询出租车辆信息", notes = "参数为出租车辆信息对象")
    public MessageResp<List<TaxiBaseInfoVehicle>> queryTaxiBaseInfoVehicle(HttpServletRequest request, @ModelAttribute TaxiBaseInfoVehicle param) {
        MessageResp<List<TaxiBaseInfoVehicle>> result = new MessageResp<>();
        PageInfo<TaxiBaseInfoVehicle> pageInfo = null;
        AuthUserResp authUser = sessionHandler.getUser(request);
        //非管理员用户只能够查看自己企业下所属车辆
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
            if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                param.setCorpId(String.valueOf(authUser.getCorpId()));
            }
        }
        try {
            pageInfo = taxiBaseInfoVehicleService.queryVehicleInfoByParam(param);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("查询成功");
        } catch (Exception e) {
            logger.error("查询异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return result;
    }

    @GetMapping("/queryVehicleForBind")
    public MessageResp<List<TaxiBaseInfoVehicle>> queryVehicleForBind(HttpServletRequest request, String bindType, String corpId) {
        MessageResp<List<TaxiBaseInfoVehicle>> result = new MessageResp<>();

        AuthUserResp authUser = sessionHandler.getUser(request);
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
            if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                corpId = String.valueOf(authUser.getCorpId());
            }
        }
        PageInfo<TaxiBaseInfoVehicle> pageInfo = null;
        try {

            pageInfo = taxiBaseInfoVehicleService.queryVehicleBybindType(bindType, corpId);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("查询成功");
        } catch (Exception e) {
            logger.error("查询异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return result;
    }


    @RequestMapping(value = "/addVehicle", method = RequestMethod.POST)
    @ApiOperation(value = "新增出租车辆信息", notes = "参数为出租车辆信息对象")
    public MessageResp insertTaxBaseInfoVehicle(HttpServletRequest request, @RequestBody TaxiBaseInfoVehicle record) {
        MessageResp result = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_C);
        int addResult = 0;
        AuthUserResp authUser = sessionHandler.getUser(request);
        //非管理员用户只能够查看自己企业下所属车辆
        if (authUser.getUserType().equals("01")) {
            if (null == record.getCorpId() || "".equals(record.getCorpId())) {
                return CommonUtils.returnErrorInfo("请选择车辆所属企业");
            }
        } else {
            record.setCorpId(String.valueOf(authUser.getCorpId()));
        }
        record.setCreateBy(authUser.getUserName());
        logInfo.setOperatorName(authUser.getUserName());
        Map<String, Object> checkRes = null;
        try {
            checkRes = checkUniqueness(record, "update");
            if (Boolean.valueOf(checkRes.get("flag").toString())) {
                return CommonUtils.returnErrorInfo(checkRes.get("msg").toString());
            }
            addResult = taxiBaseInfoVehicleService.insertVehicle(record);
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("添加成功");

            //同步车辆数据至锐明
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        syncVehicleData(record, true, null, logInfo);
                    }
                }).start();
            } catch (Exception e) {
                logger.info("调用同步车辆数据方法异常:" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("添加异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("添加失败");
        }
        return result;
    }


    @RequestMapping(value = "/updateVehicle", method = RequestMethod.PUT)
    @ApiOperation(value = "修改出租车辆信息", notes = "参数为出租车辆信息对象")
    public MessageResp updateTaxBaseInfoVehicle(HttpServletRequest request, @RequestBody TaxiBaseInfoVehicle record) {
        MessageResp result = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_U);
        int updResult = 0;
        AuthUserResp authUser = sessionHandler.getUser(request);

        record.setUpdateBy(authUser.getUserName());
        logInfo.setOperatorName(authUser.getUserName());
        Map<String, Object> checkRes = null;
        try {
            //由于需要同步数据至锐明处，对于车牌号修改需要将修改前的数据也同步过去，故多一步查询操作
            TaxiBaseInfoVehicle param = new TaxiBaseInfoVehicle();
            param.setUuid(record.getUuid());
            PageInfo oldVehiclePageInfo = taxiBaseInfoVehicleService.queryVehicleInfoByParam(param);

            TaxiVehicleSim vehicleSim = new TaxiVehicleSim();
            vehicleSim.setVehicleUuid(record.getUuid());
            vehicleSim.setStatus("01");
            int exsit = taxiVehicleSimService.isExsit(vehicleSim);

            if (null != oldVehiclePageInfo.getList() && oldVehiclePageInfo.getList().size() > 0) {
                TaxiBaseInfoVehicle oldVehicle = (TaxiBaseInfoVehicle) oldVehiclePageInfo.getList().get(0);
                //修改所属企业之前判断是否已解绑终端或驾驶员
                if (null != oldVehicle && !oldVehicle.getCorpId().equals(record.getCorpId())) {
                    if (!oldVehicle.getTerminalBind().equals("02") || !oldVehicle.getDriverBind().equals("04") || exsit > 0) {
                        return CommonUtils.returnErrorInfo("修改失败，请先解绑设备和驾驶员才能修改企业");
                    }
                }
                if (null != oldVehicle && !oldVehicle.getPlateNumber().equals(record.getPlateNumber())) {
                    //修改车辆车牌号前判断是否已绑定终端或驾驶员
                    if (!oldVehicle.getTerminalBind().equals("02") || !oldVehicle.getDriverBind().equals("04") || exsit > 0) {
                        return CommonUtils.returnErrorInfo("修改失败，请先解绑设备和驾驶员才能修改车牌号");
                    }
                    oldPlateNumber = oldVehicle.getPlateNumber();
                }
            }
            //车辆惟一性验证
            checkRes = checkUniqueness(record, "update");
            if (Boolean.valueOf(checkRes.get("flag").toString())) {
                return CommonUtils.returnErrorInfo(checkRes.get("msg").toString());
            }
            updResult = taxiBaseInfoVehicleService.updateVehicle(record);
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("修改成功");

            //同步车辆数据至锐明
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        syncVehicleData(record, false, oldPlateNumber, logInfo);
                    }
                }).start();
            } catch (Exception e) {
                logger.info("调用同步车辆数据方法异常:" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("修改异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("修改失败");
        }
        return result;
    }

    @DeleteMapping("/deleteVehicle")
    @ApiOperation(value = "删除出租车辆信息", notes = "参数UUID")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", dataType = "String", name = "uuid", value = "车辆UUID", required = true)})
    public MessageResp deleteTaxBaseInfoVehicle(String uuid) {
        MessageResp result = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_D);
        int delResult = 0;
        try {
            TaxiBaseInfoVehicle param = new TaxiBaseInfoVehicle();
            param.setUuid(uuid);
            //查询车辆
            PageInfo<TaxiBaseInfoVehicle> vehiclePageInfo = taxiBaseInfoVehicleService.queryVehicleInfoByParam(param);
            TaxiBaseInfoVehicle vehicle = vehiclePageInfo.getList().get(0);
            //删除前先判断未绑定sim卡
            TaxiVehicleSim vehicleSim = new TaxiVehicleSim();
            vehicleSim.setVehicleUuid(vehicle.getUuid());
            vehicleSim.setStatus("01");
            int exsit = taxiVehicleSimService.isExsit(vehicleSim);
            if (exsit > 0) {
                return CommonUtils.returnErrorInfo("删除失败,请先解绑sim卡");
            }
            delResult = taxiBaseInfoVehicleService.deleteVehicle(uuid);
            if (delResult > 0) {
                result.setResult(Boolean.TRUE.toString());
                result.setResultDesc("删除成功");
                //删除黑名单车辆
                TaxiAlarmBlackList veh = new TaxiAlarmBlackList();
                veh.setPlateNumber(vehicle.getPlateNumber());
                List<TaxiAlarmBlackList> blackLists = taxiAlarmBlackListService.queryAlarmBlackList(veh);
                if (blackLists.size() > 0) {
                    TaxiAlarmBlackList blackVeh = blackLists.get(0);
                    taxiAlarmBlackListService.deleteByUuid(blackVeh.getUuid());
                }
                //删除车辆信息同步到锐明
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            syncVehicleDelete(vehicle, logInfo);
                        }
                    }).start();

                } catch (Exception e) {
                    logger.info("调用删除车辆信息同步的方法异常:" + e.getMessage());
                }
            }

        } catch (Exception e) {
            logger.error("删除异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/bindDrivers", method = RequestMethod.POST)
    @ApiOperation(value = "车辆绑定驾驶员信息", notes = "参数为出租车辆信息对象")
    public MessageResp vehicleBindDrivers(HttpServletRequest request, @RequestBody TaxiBaseInfoVehicle record) {
        MessageResp result = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_C);
        int saveResult = 0;
        AuthUserResp authUser = sessionHandler.getUser(request);
        record.setCreateBy(String.valueOf(authUser.getUserName()));
        logInfo.setOperatorName(authUser.getUserName());
        try {
            saveResult = taxiBaseInfoVehicleService.insertVehicleDriver(record);
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("绑定成功");

            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        syncVehicleDriver(record, logInfo);
                    }
                }).start();

            } catch (Exception e) {
                logger.info("调用同步车辆驾驶员数据的方法异常" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("绑定异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("绑定失败");
        }
        return result;
    }

    @RequestMapping(value = "/unbindDrivers", method = RequestMethod.POST)
    @ApiOperation(value = "车辆解绑驾驶员信息", notes = "参数为出租车辆信息对象")
    public MessageResp vehicleUnBindDrivers(HttpServletRequest request, @RequestBody TaxiBaseInfoVehicle record) {
        MessageResp result = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_U);
        boolean unbindRes = false;
        AuthUserResp authUser = sessionHandler.getUser(request);
        record.setUpdateBy(authUser.getUserName());
        logInfo.setOperatorName(authUser.getUserName());
        try {
            unbindRes = taxiBaseInfoVehicleService.unbindDriverToVehicle(record);
            if (unbindRes) {
                result.setResultDesc("解绑成功");

                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            syncVehicleDriver(record, logInfo);
                        }
                    }).start();

                } catch (Exception e) {
                    logger.info("调用同步车辆驾驶员数据的方法异常" + e.getMessage());
                }
            }
            result.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            logger.error("解绑异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("解绑失败");
        }
        return result;
    }

    @RequestMapping(value = "/bindTerminal", method = RequestMethod.POST)
    @ApiOperation(value = "车辆绑定设备信息", notes = "参数为出租车辆信息对象")
    public MessageResp vehicleBindTerminal(HttpServletRequest request, @RequestBody TaxiBaseInfoVehicle record) {
        MessageResp result = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_C);
        int saveResult = 0;
        AuthUserResp authUser = sessionHandler.getUser(request);
        record.setCreateBy(authUser.getUserName());
        logInfo.setOperatorName(authUser.getUserName());
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
            if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                record.setCorpId(String.valueOf(authUser.getCorpId()));
            }
        }
        try {
            saveResult = taxiBaseInfoVehicleService.insertVehicleTerminal(record);
            if (saveResult > 0) {
                result.setResult(Boolean.TRUE.toString());
                result.setResultDesc("绑定成功");
                //同步车辆数据至锐明
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            syncVehicleTerminal(record, logInfo);
                        }
                    }).start();
//                    syncVehicleTerminal(record, logInfo);
                } catch (Exception e) {
                    logger.info("调用同步车辆终端数据的方法异常" + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("绑定异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("绑定失败");
        }
        return result;
    }


    @RequestMapping(value = "/unbindTerminal", method = RequestMethod.POST)
    @ApiOperation(value = "车辆解绑终端信息", notes = "参数为出租车辆信息对象")
    public MessageResp vehicleUnBindTerminal(HttpServletRequest request, @RequestBody TaxiBaseInfoVehicle record) {
        MessageResp result = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_D);
        boolean unbindRes = false;
        AuthUserResp authUser = sessionHandler.getUser(request);
        record.setUpdateBy(authUser.getUserName());
        logInfo.setOperatorName(authUser.getUserName());
        try {
            unbindRes = taxiBaseInfoVehicleService.unbindTerminalToVehicle(record);
            if (unbindRes) {
                result.setResultDesc("解绑成功");
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            syncVehicleTerminal(record, logInfo);
                        }
                    }).start();

                } catch (Exception e) {
                    logger.info("调用同步车辆终端数据的方法异常" + e.getMessage());
                }
            }
            result.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            logger.error("解绑异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("解绑失败");
        }
        return result;
    }

    @GetMapping("/getConditionInfoByDeviceId")
    public MessageResp getConditionInfoByDeviceId(String deviceId) {
        MessageResp result = new MessageResp();
        Map<String, Object> queryResult = null;
        try {
            queryResult = taxiBaseInfoVehicleService.getVehicleConditionInfoByDeviceId(deviceId);
            result.setData(queryResult);
            result.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            logger.error("查询异常车辆", e.getMessage());
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return result;
    }


    @GetMapping("/vehicleForBigScreen")
    @ApiOperation(value = "大屏实时重车空车率", notes = "大屏实时重车空车率")
    public MessageResp vehicleForBigScreen(HttpServletRequest request) {
        MessageResp result = new MessageResp();
        StringBuilder eStr = new StringBuilder();
        eStr.append("SELECT max( t1.__time ) firstTime,t1.VEHICLE_ID,t1.IS_OCCUPIED FROM ").append(taxiStatusTable).append(" t1 WHERE t1.IS_OCCUPIED = 0 GROUP BY t1.VEHICLE_ID,t1.IS_OCCUPIED ");
        String empty = eStr.toString();
        StringBuilder lStr = new StringBuilder();
        lStr.append("SELECT max( t1.__time ) firstTime,t1.VEHICLE_ID,t1.IS_OCCUPIED FROM ").append(taxiStatusTable).append(" t1 WHERE t1.IS_OCCUPIED = 1 GROUP BY t1.VEHICLE_ID,t1.IS_OCCUPIED ");
        String load = lStr.toString();
        try {
            String cityName = Constants.DEAULT_CITY_NAME;//内置城市为常德
            Long totolVeh = taxiBaseInfoVehicleService.queryVehicleCount(null, cityName); //查询mysql中常德市的所有车辆

            JSONArray emptyRes = httpUtils.doPostSqlUrl("sql", empty);//查询druid
            JSONArray loadRes = httpUtils.doPostSqlUrl("sql", load);//查询druid
            JSONArray data = packagingResult(totolVeh.intValue(), emptyRes, loadRes);
            result.setData(data);
            result.setResultDesc("查询成功");
            result.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            e.printStackTrace();
            result = CommonUtils.returnErrorInfo("查询失败");
        }
        return result;
    }


    /*@GetMapping("/vehicleForBigScreen")
    @ApiOperation(value = "大屏实时重车空车率", notes = "大屏实时重车空车率")
    public MessageResp vehicleForBigScreen(HttpServletRequest request) {
        MessageResp result = new MessageResp();
        try {
            String cityName = Constants.DEAULT_CITY_NAME;//内置城市为常德
            Long totolVeh = taxiBaseInfoVehicleService.queryVehicleCount(null, cityName); //查询mysql中常德市的所有车辆
            Map<String, Object> param = packagingTime(); //获取当前小时的 前12个小时 ,最早不超过当日凌晨
            JSONArray res = httpUtils.doPostSqlUrl("sql", getNullVehTrendSQL(param));//查询druid
            JSONArray data = packagingResult(totolVeh.intValue(), res, (List<String>) param.get("allHours"));

            result.setData(data);
            result.setResultDesc("查询成功");
            result.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            e.printStackTrace();
            result = CommonUtils.returnErrorInfo("查询失败");
        }
        return result;
    }*/

    /**
     * 封装查询时间,取当前小时的前12个小时
     *
     * @return
     */
    protected Map<String, Object> packagingTime() {
        Map<String, Object> result = new HashMap<>(2);
        Date today = new Date();
        String currentDate = DateUtil.getYMDHMSFormat(today);
        String[] splitDate = currentDate.split(" ");
        String ymd = splitDate[0];
        String hms = splitDate[1];

        String[] splitHms = hms.split(":");
        String hh = splitHms[0];

        int endHh = Integer.parseInt(hh);
        String endHhStr = String.valueOf(endHh);
        if (endHh >= 0 && endHh < 10) {
            endHhStr = "0" + endHh;
        }

        StringBuilder endTime = new StringBuilder();
        endTime.append(ymd).append(" ").append(endHhStr).append(":59:59");

        int startHh = endHh - 11;
        if (startHh < 0) {
            startHh = 0;
        }
        String startHhStr = String.valueOf(startHh);
        if (startHh > 0 && startHh < 10) {
            startHhStr = "0" + startHh;
        }

        List<String> allHour = new ArrayList<>(12);
        String hour = null;
        for (int i = startHh; i <= endHh; i++) {
            hour = String.valueOf(i);
            if (i >= 0 && i < 10) {
                hour = "0" + i;
            }
            allHour.add(hour);
        }

        StringBuilder startTime = new StringBuilder();
        startTime.append(ymd).append(" ").append(startHhStr).append(":00:00");
        result.put("startTime", startTime);
        result.put("endTime", endTime);
        result.put("allHours", allHour);
        return result;
    }


    protected String getNullVehTrendSQL(Map<String, Object> param) {
        StringBuilder basicSql = new StringBuilder();
        basicSql.append("select max(t1.__time) firstTime, t1.VEHICLE_ID,TIME_EXTRACT(t1.__time,'HOUR') time_hour ,t1.IS_OCCUPIED from ").append(taxiStatusTable).append(" t1 ");
        basicSql.append(" where ").append(" __time >= '").append(param.get("startTime")).append("' and __time <='").append(param.get("endTime")).append("' ");
        basicSql.append(" GROUP BY t1.VEHICLE_ID,TIME_EXTRACT(t1.__time,'HOUR'), t1.IS_OCCUPIED");

        StringBuilder sql = new StringBuilder();
        sql.append(" select  time_hour," +
                "   SUM(CASE '0' WHEN IS_OCCUPIED THEN 1 ELSE 0 END) empty_count," +
                "   SUM(CASE '1' WHEN IS_OCCUPIED THEN 1 ELSE 0 END) load_count from (  ");
        sql.append(basicSql);
        sql.append("    ) GROUP BY time_hour");
        return sql.toString();
    }


    protected JSONArray packagingResult(int totalVeh, JSONArray empty, JSONArray load) {
        JSONArray result = new JSONArray();
        JSONObject nowObj = new JSONObject();
        JSONObject emptyRes = null;
        JSONObject loadRes = null;
        //空车数
        int p = 0;
        //空车率
        float emptyRate = 0;
        //重车率
        float loadRate = 0;

        for (Object obj : empty) {
            p = p + 1;
            //每一条数据
            emptyRes = (JSONObject) obj;
            for (Object obj2 : load) {
                loadRes = (JSONObject) obj2;
                if (loadRes != null && emptyRes != null) {
                    if (loadRes.getString("VEHICLE_ID").equals(emptyRes.getString("VEHICLE_ID"))) {
                        String loadTime = loadRes.getString("firstTime");
                        String[] ts = loadTime.split("T");
                        String a = ts[1].substring(0, 8);
                        String loadTime2 = ts[0] + " " + a;
                        long loadFormat = DateUtil.getYMDHMSFormat(loadTime2).getTime();//空车的时间
                        String emptyTime = emptyRes.getString("firstTime");
                        String[] ts1 = emptyTime.split("T");
                        String b = ts1[1].substring(0, 8);
                        String emptyTime2 = ts1[0] + " " + b;
                        long emptyFormat = DateUtil.getYMDHMSFormat(emptyTime2).getTime();//重车的时间
                        if (loadFormat > emptyFormat) {
                            p = p - 1;
                        }
                    }
                }
            }
        }

        if (p > totalVeh) {
            emptyRate = 1;
        } else {
            emptyRate = (float) p / (float) totalVeh;//空车率
        }

        loadRate = (float) 1 - emptyRate; //重车率
        nowObj.put("emptyRate", emptyRate);
        nowObj.put("loadRate", loadRate);
        result.add(nowObj);
        return result;

    }


    /**
     * 车辆唯一性校验
     *
     * @param orginalData
     * @param type
     * @return
     * @throws Exception
     */
    protected Map<String, Object> checkUniqueness(TaxiBaseInfoVehicle orginalData, String type) throws Exception {
        Map<String, Object> result = new HashMap<>();
        boolean flag = false;
        String msg = null;

        boolean typeFlag = false;
        if ("update".equals(type)) {
            typeFlag = true;
        }
        TaxiBaseInfoVehicle checkParam = new TaxiBaseInfoVehicle();
        PageInfo<TaxiBaseInfoVehicle> resPageInfo = null;
        List<TaxiBaseInfoVehicle> resList = null;
        //检验车牌号
        checkParam.setPlateNumber(orginalData.getPlateNumber());
        resPageInfo = taxiBaseInfoVehicleService.queryVehicleInfoByParam(checkParam);
        resList = resPageInfo.getList();
        TaxiBaseInfoVehicle resVeh = null;
        if (resList != null && resList.size() > 0) {
            if (typeFlag) {
                resVeh = resList.get(0);
                if (resVeh.getPlateNumber().equals(orginalData.getPlateNumber()) && !resVeh.getUuid().equals(orginalData.getUuid())) {
                    flag = true;
                    msg = "车牌号已存在,请核对";
                }
            } else {
                flag = true;
                msg = "车牌号已存在,请核对";
            }
        }
        //检验车辆发动机号
        checkParam.setPlateNumber(null);
        checkParam.setEngineNumber(orginalData.getEngineNumber());
        resPageInfo = taxiBaseInfoVehicleService.queryVehicleInfoByParam(checkParam);
        resList = resPageInfo.getList();
        if (resList != null && resList.size() > 0) {
            if (typeFlag) {
                resVeh = resList.get(0);
                if (resVeh.getEngineNumber().equals(orginalData.getEngineNumber()) && !resVeh.getUuid().equals(orginalData.getUuid())) {
                    flag = true;
                    msg = "车辆发动机号已存在,请核对";
                }
            } else {
                flag = true;
                msg = "车辆发动机号已存在,请核对";
            }
        }

        //检验车辆识别VIN码
        checkParam.setEngineNumber(null);
        checkParam.setVinRecognitionCode(orginalData.getVinRecognitionCode());
        resPageInfo = taxiBaseInfoVehicleService.queryVehicleInfoByParam(checkParam);
        resList = resPageInfo.getList();
        if (resList != null && resList.size() > 0) {
            if (typeFlag) {
                resVeh = resList.get(0);
                if (resVeh.getVinRecognitionCode().equals(orginalData.getVinRecognitionCode()) && !resVeh.getUuid().equals(orginalData.getUuid())) {
                    flag = true;
                    msg = "车辆VIN码已存在,请核对";
                }
            } else {
                flag = true;
                msg = "车辆VIN码已存在,请核对";
            }
        }

        //检验车辆发动机号
        checkParam.setVinRecognitionCode(null);
        checkParam.setTransportationCertificateNumber(orginalData.getTransportationCertificateNumber());
        resPageInfo = taxiBaseInfoVehicleService.queryVehicleInfoByParam(checkParam);
        resList = resPageInfo.getList();
        if (resList != null && resList.size() > 0) {
            if (typeFlag) {
                resVeh = resList.get(0);
                if (resVeh.getTransportationCertificateNumber().equals(orginalData.getTransportationCertificateNumber()) && !resVeh.getUuid().equals(orginalData.getUuid())) {
                    flag = true;
                    msg = "车辆道路运输证号已存在,请核对";
                }
            } else {
                flag = true;
                msg = "车辆道路运输证号已存在,请核对";
            }
        }
        result.put("flag", flag);
        result.put("msg", msg);
        return result;
    }

    /*
     * 同步车辆删除数据到锐明
     * */
    public void syncVehicleDelete(TaxiBaseInfoVehicle vehicle, TaxiOperationLog logInfo) {
        Map<String, String> header = new HashMap<>(3);
        header.put("userId", userId);
        header.put("userKey", userKey);
        header.put("Content-Type", "application/Json");
        Map<String, Object> syncData = new HashMap<>();
        String dataStr = null;
        String result = null;
        try {
            syncData.put("carNo", vehicle.getPlateNumber());
            syncData.put("color", transColor(vehicle.getPlateColorCode()));
            syncData.put("oldCarNo", "");
            syncData.put("delete", Boolean.TRUE.toString());
            syncData.put("companyName", vehicle.getCorpName());

            dataStr = "[" + JSONObject.toJSONString(syncData) + "]";
            logInfo.setOperation(dataStr);
            logInfo.setLoginIp(syncUrl + "synCar");
            result = HttpClientUtils.httpRequest(syncUrl + "synCar", "POST", header, dataStr);
            JSONObject obj = JSONObject.parseObject(result);
            if (obj.get("success").equals(1)) {
                logger.info("同步车辆删除数据成功：" + dataStr);
                logInfo.setResult(Constants.RESULT_TYPE_TRUE);
                logService.insertInfo(logInfo);
            }

        } catch (Exception e) {
            logger.info("同步车辆删除数据失败：" + dataStr);
            e.printStackTrace();
            logInfo.setResult(Constants.RESULT_TYPE_FALSE);
            logInfo.setRemark(e.getMessage());
            logService.insertInfo(logInfo);
        }

    }


    /**
     * 同步增量车辆数据至锐明
     *
     * @param vehicle 车辆
     * @return
     */
    public boolean syncVehicleData(TaxiBaseInfoVehicle vehicle, boolean isAdd, String oldKey, TaxiOperationLog logInfo) {
        boolean flag = false;
        Map<String, String> header = new HashMap<>(3);
        header.put("userId", userId);
        header.put("userKey", userKey);
        header.put("Content-Type", "application/Json");


        Map<String, Object> syncData = new HashMap<>();
        /**
         * 新增或更新,直接从前端给的车辆对象中取值
         * 解绑或绑定，除终端号以外其他数据根据车辆ID查询数据并填充
         */
        String dataStr = null;
        String result = null;
        JSONObject obj = null;


        try {
            if (isAdd) {
                syncData.put("oldCarNo", "");
                syncData.put("carNo", vehicle.getPlateNumber());
                syncData.put("companyName", taxiBaseInfoCompanyService.selectCompanyInfo(vehicle.getCorpId()).getName());
                syncData.put("color", transColor(vehicle.getPlateColorCode()));
                syncData.put("mdtId", vehicle.getTerminalCode());
                dataStr = "[" + JSONObject.toJSONString(syncData) + "]";
                logInfo.setOperation(dataStr);
                logInfo.setLoginIp(syncUrl + "synCar");
                result = HttpClientUtils.httpRequest(syncUrl + "synCar", "POST", header, dataStr);
                obj = JSONObject.parseObject(result);
            } else {
                Map<String, Object> param = new HashMap<>();
                param.put("vehicleId", vehicle.getUuid());
                List<Map<String, Object>> vehicleInfo = taxiBaseInfoVehicleService.syncVehicle(param);
                if (null != oldKey && !"".equals(oldKey)) {
                    for (Map<String, Object> vehicleInfo1 : vehicleInfo) {
                        vehicleInfo1.put("oldCarNo", oldKey);
                    }
                }
                dataStr = JSONArray.toJSONString(vehicleInfo);
                logInfo.setOperation(dataStr);
                logInfo.setLoginIp(syncUrl + "synCar");
                result = HttpClientUtils.httpRequest(syncUrl + "synCar", "POST", header, dataStr);
                obj = JSONObject.parseObject(result);

            }
            if (obj.get("success").equals(1)) {
                flag = true;
                logger.info("同步车辆数据成功：" + dataStr);
                logInfo.setResult(Constants.RESULT_TYPE_TRUE);
                logService.insertInfo(logInfo);

            }

        } catch (Exception e) {
            logger.info("同步车辆数据失败：" + dataStr);
            e.printStackTrace();
            logInfo.setResult(Constants.RESULT_TYPE_FALSE);
            logInfo.setRemark(e.getMessage());
            logService.insertInfo(logInfo);
        }
        return flag;
    }


    /**
     * 绑定终端时候同步终端与车辆数据至锐明
     *
     * @param vehicle
     * @return
     */
    public void syncVehicleTerminal(TaxiBaseInfoVehicle vehicle, TaxiOperationLog logInfo) {
        Map<String, String> header = new HashMap<>(3);
        header.put("userId", userId);
        header.put("userKey", userKey);
        header.put("Content-Type", "application/Json");

        String dataStr = null;
        String result = null;

        Map<String, Object> param = new HashMap<>();
        param.put("vehicleId", vehicle.getUuid());
        String sim = null;
        String sim1 = null;
        if (vehicle.getTerminalCode() != null && !"".equals(vehicle.getTerminalCode())) {
            sim = vehicle.getTerminalCode();
        }
        try {
            //1.先同步终端信息
            Map<String, String> terminalInfo = new HashMap<>();
            if (sim != null && !sim.equals("") && sim.length() > 10) {
                sim1 = sim.substring(sim.length() - 10);
            } else {
                sim1 = sim;
            }
            terminalInfo.put("mdtid", sim1);
            dataStr = "[" + JSONObject.toJSONString(terminalInfo) + "]";
            logInfo.setOperation(dataStr);
            logInfo.setLoginIp(syncUrl + "synMdt");
            result = HttpClientUtils.httpRequest(syncUrl + "synMdt", "POST", header, dataStr);
            JSONObject obj = JSONObject.parseObject(result);
            if (obj.get("success").equals(1)) {
                logger.info("车辆与终端绑定同步终端数据成功：" + dataStr);
                logInfo.setResult(Constants.RESULT_TYPE_TRUE);
                logService.insertInfo(logInfo);
                //2.同步车辆信息
                List<Map<String, Object>> vehicleInfo = taxiBaseInfoVehicleService.syncVehicle(param);
                dataStr = JSONArray.toJSONString(vehicleInfo);
                logInfo.setOperation(dataStr);
                logInfo.setLoginIp(syncUrl + "synCar");
                result = HttpClientUtils.httpRequest(syncUrl + "synCar", "POST", header, dataStr);
                JSONObject obj1 = JSONObject.parseObject(result);
                if (obj1.get("success").equals(1)) {
                    logger.info("车辆与终端绑定同步车辆数据成功：" + dataStr);
                    logInfo.setResult(Constants.RESULT_TYPE_TRUE);
                    logService.insertInfo(logInfo);
                } else {
                    logger.info("车辆与终端绑定同步车辆数据失败：" + dataStr);
                    logInfo.setResult(Constants.RESULT_TYPE_FALSE);
                    logService.insertInfo(logInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logInfo.setResult(Constants.RESULT_TYPE_FALSE);
            logInfo.setRemark(e.getMessage());
            logService.insertInfo(logInfo);
        }
    }

    /**
     * 同步车辆驾驶员数据至锐明
     *
     * @param vehicle 车辆
     * @return
     */
    public void syncVehicleDriver(TaxiBaseInfoVehicle vehicle, TaxiOperationLog logInfo) {
        Map<String, String> header = new HashMap<>(3);
        header.put("userId", userId);
        header.put("userKey", userKey);
        header.put("Content-Type", "application/Json");
        String dataStr = null;
        String result = null;

        Map<String, Object> param = new HashMap<>();
        param.put("vehicleId", vehicle.getUuid());
        param.put("driverId", vehicle.getBindDrivers().get(0).getDriverId());
        try {
            //1.同步车辆数据
            List<Map<String, Object>> vehicleInfo = taxiBaseInfoVehicleService.syncVehicle(param);
            dataStr = JSONArray.toJSONString(vehicleInfo);
            logInfo.setOperation(dataStr);
            logInfo.setLoginIp(syncUrl + "synCar");
            result = HttpClientUtils.httpRequest(syncUrl + "synCar", "POST", header, dataStr);
            JSONObject obj = JSONObject.parseObject(result);
            if (obj.get("success").equals(1)) {
                logger.info("车辆与驾驶员绑定同步车辆数据成功：" + dataStr);
                logInfo.setResult(Constants.RESULT_TYPE_TRUE);
                logService.insertInfo(logInfo);
                //2.同步驾驶员数据
                List<Map<String, Object>> driverInfo = taxiBaseInfoDriverService.syncDriverInfo(param);
                dataStr = JSONArray.toJSONString(driverInfo);
                logInfo.setOperation(dataStr);
                logInfo.setLoginIp(syncUrl + "synDriver");
                result = HttpClientUtils.httpRequest(syncUrl + "synDriver", "POST", header, dataStr);
                JSONObject obj1 = JSONObject.parseObject(result);
                if (obj1.get("success").equals(1)) {
                    logger.info("车辆与驾驶员绑定同步驾驶员数据成功：" + dataStr);
                    logInfo.setResult(Constants.RESULT_TYPE_TRUE);
                    logService.insertInfo(logInfo);
                } else {
                    logger.info("车辆与驾驶员绑定同步驾驶员数据失败：" + dataStr);
                    logInfo.setResult(Constants.RESULT_TYPE_FALSE);
                    logService.insertInfo(logInfo);
                }
            }

        } catch (Exception e) {
            logger.error("绑定驾驶员时同步数据异常");
            e.printStackTrace();
            logInfo.setResult(Constants.RESULT_TYPE_FALSE);
            logInfo.setRemark(e.getMessage());
            logService.insertInfo(logInfo);
        }
    }


    protected int transColor(String vehicleColor) {
        int color = 9;
        if ("10".equals(vehicleColor)) {
            color = 1;
        } else if ("20".equals(vehicleColor)) {
            color = 4;
        } else if ("30".equals(vehicleColor)) {
            color = 9;
        } else if ("40".equals(vehicleColor)) {
            color = 2;
        }
        return color;
    }

}
