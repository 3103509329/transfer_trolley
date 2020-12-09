package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.common.IotOperationCommon;
import com.zhcx.authorization.config.IotConfig;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.*;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoVehicleService;
import com.zhcx.basicdata.facade.taxi.TaxiInformationDeliveryService;
import com.zhcx.basicdata.facade.taxi.TaxiInformationDeliveryTaskService;
import com.zhcx.basicdata.facade.taxi.TaxiTtsRecordService;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoVehicle;
import com.zhcx.basicdata.pojo.taxi.TaxiInformationDelivery;
import com.zhcx.basicdata.pojo.taxi.TaxiTtsRecord;
import com.zhcx.common.util.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @version V1.0
 * @autor ht(15616537979 @ 126.com)
 * @date 2018/12/24
 * @description <arg column="update_time" jdbcType="TIMESTAMP" javaType="java.util.Date" />controller
 **/
@RequestMapping("/taxi/message")
@RestController
@Api(value = "taxi", tags = "出租车消息发布")
public class TaxiInformationDeliveryController {

    private final Logger log = LoggerFactory.getLogger(TaxiInformationDeliveryController.class);

    @Autowired
    private TaxiInformationDeliveryService taxiInformationDeliveryService;

    @Autowired
    private UUIDUtils uuidUtils;

    public static final String UUIDUTILS_KEY_CANCEL = "taxi_information_delivery";

    @Autowired
    private IotConfig iotConfig;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Autowired
    private TaxiBaseInfoVehicleService taxiBaseInfoVehicleService;

    @Autowired
    private TaxiInformationDeliveryTaskService taxiInformationDeliveryTaskService;

    @Autowired
    private TaxiTtsRecordService taxiTtsRecordService;

    @Autowired
    private IotOperationCommon iotOperationCommon;

    @PostMapping("/saveInfomationDelivery")
    @ApiOperation(value = "添加出租车消息发布", notes = "添加出租车消息发布")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "insert", dataType = "TaxiInformationDelivery", name = "taxiInformationDelivery", value = "发布消息bean", required = true)})
    @Transactional(rollbackFor = {Exception.class})
    public MessageResp saveInfomationDelivery(HttpServletRequest request, @RequestBody TaxiInformationDelivery taxiInformationDelivery) {
        MessageResp resp = new MessageResp();
        try {
            AuthUserResp authUser = sessionHandler.getUser(request);
            //只有管理员用户才能够添加消息
            if (Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
                taxiInformationDelivery.setUuid(uuidUtils.getLongUUID(UUIDUTILS_KEY_CANCEL));
                Date date = new Date();
                taxiInformationDelivery.setStop(1);
                taxiInformationDelivery.setCreatTime(date);
                taxiInformationDelivery.setUpdateTime(date);
                taxiInformationDelivery.setCreator(String.valueOf(authUser.getUserId()));
                taxiInformationDelivery.setModifier(String.valueOf(authUser.getUserId()));
//                if(taxiInformationDelivery.getIsReservation()==1){
//                    //如果是预约任务则将stop状态改为1
//                    taxiInformationDelivery.setStop(1);
//                }

                //如果不是预约消息则不需要在这里执行，另起定时任务
                if (taxiInformationDelivery.getIsReservation() == 0) {
                    if (taxiInformationDelivery.getOptionType() == 1) { //如果是受众类型为车辆，直接发送tts指令
                        String[] split = taxiInformationDelivery.getAudienceIndividual().split(",");
                        for (int i = 0; i < split.length; i++) {
                            String text = taxiInformationDelivery.getMessage();
                            String msgType = "";
                            String[] displauType = taxiInformationDelivery.getDisplayType().split(",");
                            //判断发送指令类型，如果只有一条则发送当条指令
                            if (displauType.length == 1) {
                                msgType = taxiInformationDelivery.getDisplayType();
                            } else if (displauType.length > 1) {
                                msgType = "30";
                            }
                            log.info("车辆向IOT发送指令，指令类型为:{},发布信息id:{}", msgType, taxiInformationDelivery.getUuid());
                            sendTTs(text, msgType, authUser, split[i]);
                        }
                    } else if (taxiInformationDelivery.getOptionType() == 2) { //如果是受众类型为企业，需要获取企业下所有车辆，在发送tts指令
                        String[] split = taxiInformationDelivery.getAudienceIndividual().split(",");
                        for (int i = 0; i < split.length; i++) {
                            String text = taxiInformationDelivery.getMessage();
                            String[] displauType = taxiInformationDelivery.getDisplayType().split(",");
                            List<String> corpId = new ArrayList<>();
                            corpId.add(split[i]);
                            //获取企业下所有车辆
                            List<TaxiBaseInfoVehicle> taxiBaseInfoVehicles = taxiBaseInfoVehicleService.queryCompanyVehicleList(corpId);
                            /**
                             * 创建企业消息发布单独线程
                             */
                            /**
                             * 创建企业消息发布单独线程
                             */
                            new Thread(() -> {
                                String msgType = "";
                                for (TaxiBaseInfoVehicle taxi : taxiBaseInfoVehicles) {
                                    //判断发送指令类型，如果只有一条则发送当条指令
                                    if (displauType.length == 1) {
                                        msgType = taxiInformationDelivery.getDisplayType();
                                    } else if (displauType.length > 1) {
                                        msgType = "30";
                                    }
                                    if (StringUtils.isNotEmpty(taxi.getDeviceId())) {
                                        log.info("企业向IOT发送指令，指令类型为:{},发布企业id:{}", msgType, taxi.getCorpId());
                                        sendTTs(text, msgType, authUser, taxi.getDeviceId());
                                    }
                                }
                                Thread.currentThread().interrupt();
                            }, "companySendInformation").start();//开启线程
                        }
                    }
                } else if (taxiInformationDelivery.getIsReservation() == 1) {

                    taxiInformationDeliveryService.saveInfomationDelivery(taxiInformationDelivery);
                    taxiInformationDeliveryTaskService.addRecord(taxiInformationDelivery);
                    resp.setStatusCode("00");
                    resp.setResult(Boolean.TRUE.toString());
                    resp.setResultDesc("添加成功");
                    return resp;
                }
                //非预约任务 发送完指令修改发送时间
                if (taxiInformationDelivery.getIsReservation() == 0) {
                    taxiInformationDelivery.setRealTime(date);
                    taxiInformationDelivery.setStop(4);
                }
                Integer result = taxiInformationDeliveryService.saveInfomationDelivery(taxiInformationDelivery);
                if (result >= 1) {
                    resp.setStatusCode("00");
                    resp.setResult(Boolean.TRUE.toString());
                    resp.setResultDesc("添加成功");
                } else {
                    resp.setStatusCode("-50");
                    resp.setResult(Boolean.FALSE.toString());
                    resp.setResultDesc("出租车消息发布新增失败");
                    throw new Exception();
                }
            } else {
                log.error("添加出租车消息发布新增失败，失败原因，操作员无权限操作，当前用户信息,用户id{}", authUser.getUserId());
                resp.setStatusCode("-50");
                resp.setResult(Boolean.FALSE.toString());
                resp.setResultDesc("无权限操作");
            }
        } catch (Exception e) {
            log.error("添加出租车消息发布新增发生异常，异常信息{}", e.getMessage());
            resp.setStatusCode("-50");
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("出租车消息发布新增异常");
        }
        return resp;
    }

    @GetMapping("/queryAllInfomationDelivery")
    @ApiOperation(value = "查寻全部添加消息", notes = "查寻全部添加消息")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页显示记录数", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageNum", value = "当前页码", required = false)})
    public MessageResp queryAllInfomationDelivery(HttpServletRequest request, @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize, @RequestParam(name = "pageNo", defaultValue = "1", required = false) Integer pageNum) {
        MessageResp resp = new MessageResp();
        try {
            AuthUserResp authUser = sessionHandler.getUser(request);
            Map<String, String> parameters = UrlParamUtil.getParameters(request);
            String msgType = parameters.get("msgType");
            String reservationStartTime = parameters.get("reservationStartTime");
            String reservationEndTime = parameters.get("reservationEndTime");
            String realStartTime = parameters.get("realStartTime");
            String realEndTime = parameters.get("realEndTime");
            //只有管理员用户才能够添加消息
            if (Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
                PageInfo<TaxiInformationDelivery> taxiInformationDeliveryPageInfo = taxiInformationDeliveryService.queryAllInfomationDelivery(pageSize, pageNum, msgType, reservationStartTime, reservationEndTime, realStartTime, realEndTime);
                resp.setData(taxiInformationDeliveryPageInfo);
                resp.setStatusCode("00");
                resp.setResult(Boolean.TRUE.toString());
                resp.setResultDesc("查询成功");
            } else {
                log.error("查询所有出租车消息失败，失败原因，操作员无权限操作，当前用户信息,用户id{}", authUser.getUserId());
                resp.setStatusCode("-50");
                resp.setResult(Boolean.FALSE.toString());
                resp.setResultDesc("无权限操作");
            }
        } catch (Exception e) {
            log.error("查询所有出租车消息发生异常，异常信息{}", e.getMessage());
            resp.setStatusCode("-50");
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("查询所有出租车消息异常");
        }
        return resp;
    }


    @GetMapping("/deleteInfomationDelivery")
    @ApiOperation(value = "终止消息", notes = "终止消息")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页显示记录数", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageNum", value = "当前页码", required = false)})
    public MessageResp deleteInfomationDelivery(HttpServletRequest request) {
        MessageResp resp = new MessageResp();
        try {
            AuthUserResp authUser = sessionHandler.getUser(request);
            Map<String, String> parameters = UrlParamUtil.getParameters(request);
            String informationId = parameters.get("informationId");
            //只有管理员用户才能够添加消息
            if (Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
                taxiInformationDeliveryTaskService.deleRecord(Long.parseLong(informationId));
                resp.setStatusCode("00");
                resp.setResult(Boolean.TRUE.toString());
                resp.setResultDesc("终止成功");
            } else {

                log.error("终止消息失败，失败原因，操作员无权限操作，当前用户信息,用户id{}", authUser.getUserId());
                resp.setStatusCode("-50");
                resp.setResult(Boolean.FALSE.toString());
                resp.setResultDesc("无权限操作");
            }
        } catch (Exception e) {
            log.error("终止消息发生异常，异常信息{}", e.getMessage());
            resp.setStatusCode("-50");
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("终止消息异常");
        }
        return resp;
    }

    public MessageResp sendTTs(String text, String msgType, AuthUserResp authUser, String deviceId) {
        TaxiTtsRecord ttsRecord = new TaxiTtsRecord();
        if (null != text && !"".equals(text)) {
            int textLength = text.getBytes().length;
            if (textLength > 499) {
                return CommonUtils.returnErrorInfo("消息长度过长");
            }
        }

        if (null == msgType || "".equals(msgType)) {
            return CommonUtils.returnErrorInfo("消息类型不能为空");
        }
        ttsRecord.setText(text);
        ttsRecord.setMsgType(msgType);
        ttsRecord.setDeviceId(deviceId);
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
            ttsRecord.setCreateBy(authUser.getUserName());
            if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                ttsRecord.setCorpId(String.valueOf(authUser.getCorpId()));
            }
        }
        MessageResp result = new MessageResp();

//        String token = getIotToken();
        String token = iotOperationCommon.doLoginIot();
        if (token == null) {
            return null;
        }
        String url = iotConfig.getTtsUrl() + "/" + deviceId;
        try {
            JSONObject ttsParam = packingParam(text, msgType);
            Map<String, String> ttsHeader = new HashMap<>();
            ttsHeader.put("X-Authorization", "Bearer " + token);
            ttsHeader.put("Content-Type", "application/Json; charset=UTF-8");
            Map<String, String> ttsRespMap = null;
            int postRes = HttpClientUtils.getHttpResponseCode( url, "POST", ttsHeader, ttsParam.toString());
            log.info("调用iot下发tts返回值为: "+postRes);
            if (postRes == 200) {
                result.setResult(Boolean.TRUE.toString());
                result.setResultDesc("发送成功");
                ttsRecord.setStatus("1");
            }else if (postRes == 408){
                result.setData(postRes);
                result.setResult(Boolean.FALSE.toString());
                result.setResultDesc("发送失败,设备不在线");
                ttsRecord.setStatus("0");
            }else {
                result.setData(postRes);
                result.setResult(Boolean.FALSE.toString());
                result.setResultDesc("发送失败");
                ttsRecord.setStatus("0");
            }
//            String ttsRespStr = HttpClientUtils.httpRequest(url, "POST", ttsHeader, ttsParam.toJSONString());
//            if (null != ttsRespStr && !"".equals(ttsRespStr)) {
//                log.info("调用iot发送tts成功返回值:"+ttsRespStr);
//                result.setResult(Boolean.TRUE.toString());
//                result.setResultDesc("发送成功");
//                ttsRecord.setStatus("1");
//            } else {
//                log.info("调用iot发送tts失败返回值:"+ttsRespStr);
//                ttsRecord.setStatus("0");
//                result = CommonUtils.returnErrorInfo("发送失败");
//            }

        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            ttsRecord.setStatus("0");
//            result = CommonUtils.returnErrorInfo("发送失败");
            log.error(e.getMessage(), e);
            ttsRecord.setStatus("0");
            result = CommonUtils.returnErrorInfo("发送失败");
        }
        //保存到tts表中
        taxiTtsRecordService.addTtsRecord(ttsRecord);
        return result;
    }

    /**
     * 封装查询参数
     *
     * @param text
     * @return
     */
    protected JSONObject packingParam(String text, String msgType) throws Exception {
        JSONObject result = new JSONObject();
        result.put("method", "33536");
        JSONObject subParam = new JSONObject();
        subParam.put("FLAG", msgType);
        subParam.put("TEXT", text);
        result.put("params", subParam);
        return result;
    }

    /**
     * 获取IOT的token
     *
     * @return
     */
    protected String getIotToken() {
        String token = null;
        User user = new User();
        user.setUsername(iotConfig.getUsername());
        user.setPassword(iotConfig.getPassword());
        Map<String, Object> mapJson = null;
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Content-Type", "application/Json; charset=UTF-8");
        String resJson = null;
        try {
            resJson = HttpClientUtils.httpRequest(
                    iotConfig.getUrl() + "/api/auth/login", "POST",
                    headerParams, JsonParseUtils.bean2Json(user));
            mapJson = JsonParseUtils.json2Bean(resJson, Map.class);
            if (null != mapJson && !mapJson.isEmpty()) {
                token = (String) mapJson.get("token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

}
