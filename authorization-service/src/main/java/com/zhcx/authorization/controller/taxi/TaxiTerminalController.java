package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig.SessionHandler;
import com.zhcx.authorization.utils.*;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoTerminalService;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoVehicleService;
import com.zhcx.basicdata.facade.taxi.TaxiOperationLogService;
import com.zhcx.basicdata.facade.taxi.TaxiTermianlVehicleService;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoTerminal;
import com.zhcx.basicdata.pojo.taxi.TaxiOperationLog;
import com.zhcx.basicdata.pojo.taxi.TaxiVehicleTerminal;
import com.zhcx.common.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: authorization-service
 * @ClassName:TaxiTerminalController
 * @description: 出租车-设备Controller
 * @author: ZhangKai
 * @create: 2018-11-26 20:08
 **/
@RestController
@RequestMapping("/taxi/terminal")
@Api(value = "terminal", tags = "出租车终端设备terminal接口")
public class TaxiTerminalController {

    private static final Logger logger = LoggerFactory.getLogger(TaxiTerminalController.class);

    @Autowired
    private TaxiBaseInfoTerminalService taxiBaseInfoTerminalService;

    @Autowired
    private TaxiTermianlVehicleService taxiTermianlVehicleService;

    @Autowired
    private TaxiBaseInfoVehicleService taxiBaseInfoVehicleService;

    @Autowired
    private SessionHandler sessionHandler;

    @Autowired
    private TaxiOperationLogService logService;

    @Value("${zhcx.sync.streamax.url}")
    private String syncUrl;

    @Value("${zhcx.sync.streamax.userId}")
    private String userId;

    @Value("${zhcx.sync.streamax.userKey}")
    private String userKey;

    String oldSim = null;


    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "查询出租终端信息", notes = "参数为出租终端信息对象")
    public MessageResp queryTerminalByParam(HttpServletRequest request, @ModelAttribute TaxiBaseInfoTerminal param) {
        MessageResp result = new MessageResp();
        PageInfo<TaxiBaseInfoTerminal> pageInfo = null;
        AuthUserResp user = sessionHandler.getUser(request);
        try {
            if (null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())) {
                if (user.getCorpId() != null && user.getCorpId() != 0L) {
                    param.setCropId(String.valueOf(user.getCorpId()));
                }
            }
            pageInfo = taxiBaseInfoTerminalService.selectTerminalInfoByParam(param);
            result.setResultDesc("查询成功");
            result.setResult(Boolean.TRUE.toString());
            result.setData(pageInfo.getList());
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败,{}", e.getMessage());
            return CommonUtils.returnErrorInfo(null);
        }
        return result;
    }

    //查询可绑定的计价器接口
    @GetMapping("/queryJJQTerminalForBind")
    public MessageResp<List<TaxiBaseInfoTerminal>> queryJJQTerminalForBind(HttpServletRequest request, @ModelAttribute TaxiBaseInfoTerminal param) {
        MessageResp result = new MessageResp();
        PageInfo<TaxiBaseInfoTerminal> pageInfo = null;
        AuthUserResp user = sessionHandler.getUser(request);
        try {
            if (null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())) {
                if (user.getCorpId() != null && user.getCorpId() != 0L) {
                    param.setCropId(String.valueOf(user.getCorpId()));
                }
            }
            pageInfo = taxiBaseInfoTerminalService.selectTerminalInfoByParam(param);
            result.setResultDesc("查询成功");
            result.setResult(Boolean.TRUE.toString());
            result.setData(pageInfo.getList());
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败,{}", e.getMessage());
            return CommonUtils.returnErrorInfo(null);
        }
        return result;
    }


    @PostMapping("/addTerminal")
    @ApiOperation(value = "新增出租设备信息", notes = "参数为出租设备信息对象")
    public MessageResp insertTaxBaseInfoTerminal(HttpServletRequest request, @RequestBody TaxiBaseInfoTerminal record) {
        MessageResp result = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_C);
        int addResult = 0;
        AuthUserResp user = sessionHandler.getUser(request);
        try {
            if (null != user) {
                record.setCreateBy(String.valueOf(user.getUserName()));
                logInfo.setOperatorName(user.getUserName());
                if (user.getUserType().equals("01")) {
                    if (null == record.getCropId() || "".equals(record.getCropId())) {
                        return CommonUtils.returnErrorInfo("请选择终端所属企业");
                    }
                }
                if (user.getCorpId() != null && user.getCorpId() != 0L) {
                    record.setCropId(String.valueOf(user.getCorpId()));
                }
            }

            int count = taxiBaseInfoTerminalService.selectBySim(record.getSim());
            if (count > 0) {
                return CommonUtils.returnErrorInfo("设备号已存在");
            }
            if (null != record && !"".equals(record)) {
                addResult = taxiBaseInfoTerminalService.insertTerminal(record);
            }
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("成功添加" + addResult + "台设备信息");
            //调用方法同步数据到终端
            if (addResult > 0) {
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            syncDataTerminal(record.getSim(), null, false, null, logInfo);
                        }
                    }).start();

                } catch (Exception e) {
                    logger.info("调用同步终端数据的方法异常" + e.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加失败,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("添加失败");
        }
        return result;
    }


    @PutMapping("/updateTerminal")
    @ApiOperation(value = "修改出租设备信息", notes = "参数为出租设备信息对象")
    public MessageResp updateTaxBaseInfoTerminal(HttpServletRequest request, @RequestBody TaxiBaseInfoTerminal record) {

        MessageResp result = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_U);
        int updResult = 0;
        AuthUserResp user = sessionHandler.getUser(request);
        try {
            if (null != user) {
                record.setUpdateBy(user.getUserName());
                record.setUpdateTime(DateUtil.getNowDateTime());
                logInfo.setOperatorName(user.getUserName());
            }

            TaxiBaseInfoTerminal param = new TaxiBaseInfoTerminal();
            param.setUuid(record.getUuid());
            param.setCropId(record.getCropId());
            PageInfo oldTerminalPageInfo = taxiBaseInfoTerminalService.selectTerminalInfoByParam(param);
            if (null != oldTerminalPageInfo && oldTerminalPageInfo.getList().size() > 0) {
                TaxiBaseInfoTerminal oldTerminal = (TaxiBaseInfoTerminal) oldTerminalPageInfo.getList().get(0);
                if (null != oldTerminal && !oldTerminal.getSim().equals(record.getSim())) {
                    oldSim = oldTerminal.getSim();
                    int count = taxiBaseInfoTerminalService.selectBySim(record.getSim());
                    if (count > 0) {
                        return CommonUtils.returnErrorInfo("设备号重复");
                    }
                }
            }
            updResult = taxiBaseInfoTerminalService.updateTerminal(record, oldSim);
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("成功变更" + updResult + "台设备信息");
            //调用方法同步数据到终端
            if (updResult > 0) {
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            syncDataTerminal(record.getSim(), null, false, oldSim, logInfo);
                        }
                    }).start();

                } catch (Exception e) {
                    logger.info("调用同步终端数据的方法异常" + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新失败,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("更新失败,当前设备已绑定车辆");
        }
        return result;
    }

    @DeleteMapping("/deleteTerminal")
    @ApiOperation(value = "删除出租设备信息", notes = "参数UUID")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", dataType = "String", name = "uuid", value = "设备UUID", required = true)})
    public MessageResp deleteTaxBaseInfoTerminal(String uuid) {
        MessageResp result = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_D);
        int delResult = 0;
        try {
            //删除之前先查询
            TaxiBaseInfoTerminal param = new TaxiBaseInfoTerminal();
            param.setUuid(uuid);
            PageInfo<TaxiBaseInfoTerminal> terminalPageInfo = taxiBaseInfoTerminalService.selectTerminalInfoByParam(param);
            TaxiBaseInfoTerminal terminal = terminalPageInfo.getList().get(0);

            delResult = taxiBaseInfoTerminalService.deleteTerminal(uuid);
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("成功删除" + delResult + "台设备信息");
            //同步删除设备数据到锐明
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        syncTerminalDelete(terminal, logInfo);
                    }
                }).start();

            } catch (Exception e) {
                logger.info("调用同步终端数据的方法异常" + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除失败,{}", e.getMessage());
            return CommonUtils.returnErrorInfo(e.getMessage());
        }
        return result;
    }


    @PostMapping("/bindTerminal")
    @ApiOperation(value = "绑定出租设备", notes = "设备与车辆关系对象")
    public MessageResp bindTerminalToVehicle(HttpServletRequest request, @RequestBody TaxiVehicleTerminal param) {
        MessageResp result = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_C);
        AuthUserResp user = sessionHandler.getUser(request);
        try {
            if (null != user) {
                param.setCreateBy(user.getUserName());
                logInfo.setOperatorName(user.getUserName());
                if (null != user.getCorpId() && !"".equals(user.getCorpId())) {
                    param.setCorpId(String.valueOf(user.getCorpId()));
                }
            }

            boolean flag = taxiTermianlVehicleService.terminalBindVehicle(param);
            if (flag) {
                result.setResultDesc("绑定成功");
                result.setResult(Boolean.TRUE.toString());
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            syncDataTerminal(param.getTerminalCode(), param.getVehicleId(), true, null, logInfo);
                        }
                    }).start();
                } catch (Exception e) {
                    logger.info("调用同步车辆绑定终端数据的方法异常" + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("绑定失败,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("绑定失败");
        }
        return result;
    }

    @PostMapping("/unbindTerminal")
    @ApiOperation(value = "解绑出租设备", notes = "设备与车辆关系对象")
    public MessageResp unBindTerminalToVehicle(HttpServletRequest request, @RequestBody TaxiVehicleTerminal param) {
        MessageResp result = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_D);
        AuthUserResp user = sessionHandler.getUser(request);
        try {
            if (null != user) {
                param.setUpdateBy(user.getUserName());
                logInfo.setOperatorName(user.getUserName());
            }
            boolean flag = taxiTermianlVehicleService.termianlUnBindVehicle(param);
            if (flag) {
                result.setResultDesc("解绑成功");
                result.setResult(Boolean.TRUE.toString());
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            syncDataTerminal(param.getTerminalCode(), param.getVehicleId(), true, null, logInfo);
                        }
                    }).start();
                } catch (Exception e) {
                    logger.info("调用同步车辆解绑终端数据的方法异常" + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("解绑失败,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("解绑失败");
        }
        return result;
    }


    //同步数据到终端
    private void syncDataTerminal(String sim, String vehicleId, boolean isBind, String oldKey, TaxiOperationLog logInfo) {
        Map<String, String> header = new HashMap<>();
        header.put("userId", userId);
        header.put("userKey", userKey);
        header.put("Content-Type", "application/Json");
        String sim1 = null;
        String oldKey1 = null;
        String dataStr = null;
        String result = null;
        StringBuilder res = new StringBuilder();
        if (sim != null && !sim.equals("") && sim.length() > 10) {
            sim1 = sim.substring(sim.length() - 10);
        } else {
            sim1 = sim;
        }
        if (oldKey != null && !oldKey.equals("") && oldKey.length() > 10) {
            oldKey1 = oldKey.substring(oldKey.length() - 10);
        } else {
            if (oldKey == null) {
                oldKey1 = "";
            } else {
                oldKey1 = oldKey;
            }
        }


        Map<String, Object> terminalInfo = new HashMap<>();
        try {
            terminalInfo.put("oldMdtid", "");
            terminalInfo.put("mdtid", sim1);
            if (oldKey != null && !oldKey.equals("")) {
                terminalInfo.put("oldMdtid", oldKey1);
            }
            dataStr = "[" + JSONObject.toJSONString(terminalInfo) + "]";
            logInfo.setOperation(dataStr);
            logInfo.setLoginIp(syncUrl + "synMdt");
            result = HttpClientUtils.httpRequest(syncUrl + "synMdt", "POST", header, dataStr);
            if (isSuccess(result)) {
                logger.info("同步终端数据成功" + dataStr);
                logInfo.setResult(Constants.RESULT_TYPE_TRUE);
                logService.insertInfo(logInfo);
                if (isBind) {
                    Map<String, Object> param = new HashMap<>();
                    param.put("vehicleId", vehicleId);
                    List<Map<String, Object>> vehicleInfo = taxiBaseInfoVehicleService.syncVehicle(param);
                    dataStr = JSONArray.toJSONString(vehicleInfo);
                    logInfo.setOperation(dataStr);
                    logInfo.setLoginIp(syncUrl + "synCar");
                    result = HttpClientUtils.httpRequest(syncUrl + "synCar", "POST", header, dataStr);
                    JSONObject obj1 = JSONObject.parseObject(result);
                    if (obj1.get("success").equals(1)) {
                        logger.info("同步终端、车辆数据成功" + dataStr);
                        logInfo.setResult(Constants.RESULT_TYPE_TRUE);
                        logService.insertInfo(logInfo);
                    } else {
                        logger.info("同步终端数据成功，同步车辆数据失败" + dataStr);
                        logInfo.setResult(Constants.RESULT_TYPE_FALSE);
                        logService.insertInfo(logInfo);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("同步终端数据异常" + dataStr);
            e.printStackTrace();
            logInfo.setResult(Constants.RESULT_TYPE_FALSE);
            logInfo.setRemark(e.getMessage());
            logService.insertInfo(logInfo);
        }
    }

    //同步删除数据
    private void syncTerminalDelete(TaxiBaseInfoTerminal terminal, TaxiOperationLog logInfo) {
        Map<String, String> header = new HashMap<>(3);
        header.put("userId", userId);
        header.put("userKey", userKey);
        header.put("Content-Type", "application/Json");
        Map<String, Object> syncData = new HashMap<>();
        String sim1 = null;
        String dataStr = null;
        String result = null;
        try {
            String sim = terminal.getSim();
            if (sim.length() > 10) {
                sim1 = sim.substring(sim.length() - 10);
            } else {
                sim1 = sim;
            }
            syncData.put("mdtid", sim1);
            syncData.put("delete", Boolean.TRUE.toString());
            dataStr = "[" + JSONObject.toJSONString(syncData) + "]";
            logInfo.setOperation(dataStr);
            logInfo.setLoginIp(syncUrl + "synMdt");
            result = HttpClientUtils.httpRequest(syncUrl + "synMdt", "POST", header, dataStr);
            JSONObject obj = JSONObject.parseObject(result);
            if (obj.get("success").equals(1)) {
                logger.info("同步终端删除数据成功：" + dataStr);
                logInfo.setResult(Constants.RESULT_TYPE_TRUE);
                logService.insertInfo(logInfo);
            }
        } catch (Exception e) {
            logger.info("同步终端删除数据失败：" + dataStr);
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
            logger.error(obj.getString("msg"));
        }
        return flag;
    }

}
