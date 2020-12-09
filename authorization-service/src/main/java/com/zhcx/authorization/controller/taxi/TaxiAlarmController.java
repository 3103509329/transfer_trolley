package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.common.IotOperationCommon;
import com.zhcx.authorization.config.IotConfig;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.*;
import com.zhcx.basicdata.facade.taxi.TaxiAlarmInfoService;
import com.zhcx.basicdata.facade.taxi.TaxiAlarmOperateService;
import com.zhcx.basicdata.pojo.taxi.TaxiAlarmInfo;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @program: authorization-service
 * @ClassName:TaxiAlarmController
 * @description: 报警信息控制层
 * @author: ZhangKai
 * @create: 2018-12-03 09:19
 **/
@RequestMapping("/taxi/alarm")
@RestController
@Api(value = "alarm", tags = "出租车报警信息")
public class TaxiAlarmController {

    private static final Logger logger = LoggerFactory.getLogger(TaxiAlarmController.class);

    @Autowired
    private TaxiAlarmInfoService taxiAlarmInfoService;

    @Autowired
    private TaxiAlarmOperateService taxiAlarmOperateService;


    @Autowired
    private IotConfig iotConfig;

    @Autowired
    private IotOperationCommon iotOperationCommon;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @GetMapping("")
    public MessageResp queryAlarmInfo(HttpServletRequest request, @ModelAttribute TaxiAlarmInfo param) {
        MessageResp result = new MessageResp();
        AuthUserResp authUser = sessionHandler.getUser(request);
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
            if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                param.setCorpId(String.valueOf(authUser.getCorpId()));
            }
        }
        PageInfo pageInfo = null;
        try {
            pageInfo = taxiAlarmInfoService.queryAlarmInfo(param);
            result.setData(pageInfo.getList());
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(Boolean.FALSE.toString());
        }
        return result;
    }

    @GetMapping("/alarmToRemind")
    public MessageResp AlarmToRemind(HttpServletRequest request, @ModelAttribute TaxiAlarmInfo param) {
        MessageResp result = new MessageResp();
        AuthUserResp authUser = sessionHandler.getUser(request);
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
            if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                param.setCorpId(String.valueOf(authUser.getCorpId()));
            }
        }
        PageInfo pageInfo = null;
        try {
            pageInfo = taxiAlarmInfoService.queryAlarmInfoByHistory(param);
            result.setData(pageInfo.getList());
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(Boolean.FALSE.toString());
        }
        return result;
    }

    @GetMapping("/getFifteenSecondsAgo")
    public MessageResp getFifteenSecondsAgoAlarmInfo(HttpServletRequest request, @ModelAttribute TaxiAlarmInfo param) {
        MessageResp result = new MessageResp();
        AuthUserResp authUser = sessionHandler.getUser(request);
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
            if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                param.setCorpId(String.valueOf(authUser.getCorpId()));
            }
        }
        PageInfo pageInfo = null;
        try {
            pageInfo = taxiAlarmInfoService.getFifteenSecondsAgo(param);
            result.setData(pageInfo.getList());
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(Boolean.FALSE.toString());
        }
        return result;
    }

    @GetMapping("/getAlarmForMonitor")
    public MessageResp getAlarmForMonitor(String startTime, String endTime) {
        MessageResp result = new MessageResp();
        try {
            Map<String, Object> param = new HashMap<>(2);
            if (null != startTime && "" != startTime) {
                param.put("startTime", startTime + " 00:00:00");
            }
            if (null != endTime && "" != endTime) {
                param.put("endTime", endTime + " 23:59:59");
            }
            List<Map<String, Object>> queryRes = taxiAlarmInfoService.getAlarmFotMonitor(param);
            result.setData(queryRes);
            result.setResultDesc("查询成功");
        } catch (Exception e) {
            logger.error("查询监控界面报警信息异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("查询失败");
        }

        return result;
    }


    @GetMapping("/handleAlarm")
    public MessageResp handleAlarm(HttpServletRequest request, String handleType, String deviceId) {
        MessageResp result = new MessageResp();
        AuthUserResp authUser = sessionHandler.getUser(request);
        TaxiAlarmInfo param = new TaxiAlarmInfo();
        param.setDeviceId(deviceId);
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
            param.setCreateBy(authUser.getUserName());
            if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                param.setCorpId(String.valueOf(authUser.getCorpId()));
            }
        }
        String url = iotConfig.getTtsUrl() + "/" + deviceId;
        try {
            JSONObject ttsParam = packingParam(handleType);
            //获取token
//            String token = getIotToken();
            String token = iotOperationCommon.doLoginIot();
            if (null == token || "".equals(token)) {
                return CommonUtils.returnErrorInfo("IOT鉴权失败");
            }
            //发送请求
            Map<String,String> header = iotOperationCommon.getHeaderMap(token);
            int postRes = HttpClientUtils.getHttpResponseCode( url, "POST", header, ttsParam.toString());

//            okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), ttsParam.toString());
//            Response response = okHttpUtil.doPostRequest(body, url, token);
            if (postRes == 0) {
                result.setResult(Boolean.FALSE.toString());
                result.setResultDesc("指令下发失败");
                return result;
            } else {
                result.setData(postRes);
                result.setResult(Boolean.TRUE.toString());
                result.setResultDesc("发送成功");
            }
            if ("35594".equals(handleType)) {
                //真实报警转警情
                TaxiAlarmInfo alarm = taxiAlarmInfoService.getLatestAlarm(deviceId);
                if (alarm != null) {
                    alarm.setUpdateBy(authUser.getUserName());
                    taxiAlarmOperateService.addAlarmOperate(alarm);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = CommonUtils.returnErrorInfo("发送失败");
        }
        return result;
    }


    protected JSONObject packingParam(String msgType) throws Exception {
        JSONObject result = new JSONObject();
        result.put("method", msgType);
//        result.put("params","");
        return result;
    }

    /**
     * 获取IOT的token
     *
     * @return
     */
//    protected String getIotToken() {
//        String token = null;
//        JSONObject loginParam = new JSONObject();
//        loginParam.put("username", iotConfig.getUsername());
//        loginParam.put("password", iotConfig.getPassword());
//        String paramStr = loginParam.toJSONString();
////        okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramStr);
////        Response response = okHttpUtil.doPostRequest(body, iotConfig.getUrl() + "/api/auth/login", null);
//
//
//        Map<String, String> header = new HashMap<>();
//        header.put("Content-Type", "application/Json; charset=UTF-8");
//
//        String postRes = HttpClientUtils.httpRequest(iotConfig.getUrl() + "/api/auth/login", "POST", header, paramStr);
//        try {
////            if (response.code() == 200) {
//            if (null != postRes) {
////                String tokenStr = response.body().string();
//                JSONObject tokenObj = (JSONObject) JSONObject.parse(postRes);
//                token = tokenObj.getString("token");
//            } else {
//                logger.error("登录IOT异常");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return token;
//    }

}
