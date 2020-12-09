package com.zhcx.authorization.controller.monitor;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.common.IotOperationCommon;
import com.zhcx.authorization.config.IotConfig;
import com.zhcx.authorization.config.SessionConfig.SessionHandler;
import com.zhcx.authorization.utils.*;
import com.zhcx.basicdata.facade.taxi.TaxiTtsRecordService;
import com.zhcx.basicdata.pojo.taxi.TaxiTtsRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: authorization-service
 * @ClassName:TtsController
 * @description: TTS下发控制类
 * @author: ZhangKai
 * @create: 2018-12-03 14:07
 **/
@RequestMapping("/taxi/tts")
@RestController
@Api(value = "tts", tags = "出租车TTS下发")
public class TtsController {

    private static final Logger logger = LoggerFactory.getLogger(TtsController.class);
    @Autowired
    private IotConfig iotConfig;

    @Autowired
    private SessionHandler sessionHandler;

    @Autowired
    private IotOperationCommon iotOperationCommon;

    @Autowired
    private TaxiTtsRecordService taxiTtsRecordService;

    @Autowired
    private OkHttpUtil okHttpUtil;

    @GetMapping("/sendTts")
    @ApiOperation(value = "发送TTS消息", notes = "发送TTS消息")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", dataType = "String", name = "deviceId", value = "车辆deviceId", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "text", value = "TTS文本", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "msgType", value = "消息类型(30:语音,20:文本,30:两者同时)", required = true)})
    public MessageResp sendTts(HttpServletRequest request,String deviceId, String text,String msgType){
        TaxiTtsRecord ttsRecord = new TaxiTtsRecord();
        if(null != text && !"".equals(text)){
            int textLength = text.getBytes().length;
            if(textLength > 499){
                return CommonUtils.returnErrorInfo("消息长度过长");
            }
        }

        if(null == msgType || "".equals(msgType)){
            return CommonUtils.returnErrorInfo("消息类型不能为空");
        }

        AuthUserResp authUser = sessionHandler.getUser(request);
        ttsRecord.setText(text);
        ttsRecord.setMsgType(msgType);
        ttsRecord.setDeviceId(deviceId);
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
            ttsRecord.setCreateBy(authUser.getUserName());
            if(authUser.getCorpId() != null && authUser.getCorpId() != 0L){
                ttsRecord.setCorpId(String.valueOf(authUser.getCorpId()));
            }
        }
        MessageResp result = new MessageResp();

//        String token = getIotToken();
        String token = iotOperationCommon.doLoginIot();
        if (token == null) {

            return null;
        }
        String url = iotConfig.getTtsUrl()+"/"+deviceId;
        try{
            JSONObject ttsParam = packingParam(text,msgType);

            Map<String,String> header =getHeader(token);
            int postRes = HttpClientUtils.getHttpResponseCode( url, "POST", header, ttsParam.toString());
//            okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), ttsParam.toString());
//            Response response = okHttpUtil.doPostRequest(body, url, token);
//            if(response == null){
            if (postRes == 408) {
                result.setResult(Boolean.FALSE.toString());
                result.setResultDesc("发送失败,设备不在线");
                ttsRecord.setStatus("0");
            }else if (postRes == 200){
                result.setData(postRes);
                result.setResult(Boolean.TRUE.toString());
                result.setResultDesc("发送成功");
                ttsRecord.setStatus("1");
            } else {
                result.setResult(Boolean.FALSE.toString());
                result.setResultDesc("发送失败");
                ttsRecord.setStatus("0");
            }
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            ttsRecord.setStatus("0");
            result = CommonUtils.returnErrorInfo("发送失败");
        }
        taxiTtsRecordService.addTtsRecord(ttsRecord);
        return result;
    }




    @PostMapping("/closeVedio")
    public MessageResp closeVedio(@RequestBody JSONObject param){
        MessageResp messageResp = new MessageResp();

        String deviceId = param.get("deviceId").toString();
        String paramStr = JSONObject.toJSONString(param.get("param"));

        logger.info("视频下发指令参数,{}",param);

        String json = null;
        String token = null;
        JSONObject respObj = null;
        try {
            //获取token
//            token = getIotToken();
            token = iotOperationCommon.doLoginIot();
            if(null == token || "".equals(token)){
                return CommonUtils.returnErrorInfo("IOT鉴权失败");
            }
            //发送请求
            String closeUrl = iotConfig.getTtsUrl()+"/"+deviceId;

            Map<String,String> header = getHeader(token);

//            okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramStr);
//            Response response = okHttpUtil.doPostRequest(body, closeUrl, token);

            int postRes = HttpClientUtils.getHttpResponseCode( closeUrl, "POST", header, paramStr);
//            if(response == null){
            if(postRes == 0){
                messageResp.setResult(Boolean.FALSE.toString());
                messageResp.setResultDesc("发送失败");
            }else{
                messageResp.setData(postRes);
                messageResp.setResult(Boolean.TRUE.toString());
                messageResp.setResultDesc("发送成功");
            }
        } catch (Exception e) {
            logger.warn("视频下发异常,{}",e.getMessage());
            e.printStackTrace();
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("发送失败");
        }
        return messageResp;
    }

    @GetMapping("/queryRecord")
    public MessageResp selectRecord(HttpServletRequest request, @ModelAttribute TaxiTtsRecord param){
        MessageResp result = new MessageResp();
        AuthUserResp authUser = sessionHandler.getUser(request);
        PageInfo pageInfo = null;
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
            param.setCreateBy(authUser.getUserName());
            if(authUser.getCorpId() != null && authUser.getCorpId() != 0L){
                param.setCorpId(String.valueOf(authUser.getCorpId()));
            }
        }
        try{
            pageInfo = taxiTtsRecordService.selectTtsRecord(param);
            result.setResultDesc("查询成功");
            result.setResult(Boolean.TRUE.toString());
            result.setData(pageInfo.getList());
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
        }catch (Exception e){
            e.printStackTrace();
            result = CommonUtils.returnErrorInfo("查询失败");
        }
        return result;
    }

    /**
     * 封装查询参数
     * @param text
     * @return
     */
    protected JSONObject packingParam(String text,String msgType) throws Exception {
        JSONObject result = new JSONObject();
        result.put("method","33536");
        JSONObject subParam = new JSONObject();
        subParam.put("FLAG",msgType);
        subParam.put("TEXT",text);
        result.put("params",subParam);
        return result;
    }

    protected Map<String,String> getHeader(String token){
        Map<String,String> header = new HashMap<>(2);
        header.put("Content-Type", "application/Json; charset=UTF-8");
        if(null != token && !"".equals(token)){
            header.put("X-Authorization", "Bearer" + " " + token);
        }
        return header;
    }

    /**
     * 获取IOT的token
     * @return
     */
//    protected String getIotToken(){
//        String token = null;
//        JSONObject loginParam = new JSONObject();
//        loginParam.put("username", iotConfig.getUsername());
//        loginParam.put("password", iotConfig.getPassword());
//        String paramStr = loginParam.toJSONString();
//        okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramStr);
//        Response response = okHttpUtil.doPostRequest(body, iotConfig.getUrl() + "/api/auth/login", null);
//        try {
//            if (response.code() == 200) {
//                String tokenStr = response.body().string();
//                JSONObject tokenObj = (JSONObject) JSONObject.parse(tokenStr);
//                token = tokenObj.getString("token");
//            } else {
//                logger.error("登录IOT异常");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            if (null != response) {
//                response.body().close();
//                response.close();
//            }
//        }
//        return token;
//    }

}
