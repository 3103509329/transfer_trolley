package com.zhcx.authorization.controller.monitor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.zhcx.authorization.common.IotOperationCommon;
import com.zhcx.authorization.utils.*;
import okhttp3.MediaType;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zhcx.authorization.config.IotConfig;
import com.zhcx.authorization.config.SessionConfig.SessionHandler;

import io.swagger.annotations.Api;
import sun.net.www.http.HttpClient;

/**
 * 下发指令通用接口
 *
 * @author tangding
 */


@RequestMapping("/taxi/instruct")
@RestController
@Api(value = "Instruct", tags = "设备指令下发")
public class InstructController {

    private static final Logger logger = LoggerFactory.getLogger(TtsController.class);
    @Autowired
    private IotConfig iotConfig;

    @Autowired
    private SessionHandler sessionHandler;

    @Autowired
    private IotOperationCommon iotOperationCommon;


    @Autowired
    private OkHttpUtil okHttpUtil;

    @PostMapping("/issue")
    public MessageResp issue(@RequestBody JSONObject param) {
        MessageResp messageResp = new MessageResp();

        String deviceId = param.get("deviceId").toString();
        String paramStr = JSONObject.toJSONString(param.get("param"));

        logger.info("指令下发参数,{}", param);

        String json = null;
        String token = null;
        JSONObject respObj = null;
        try {
            //获取token
            token = getIotToken();
            if (null == token || "".equals(token)) {
                return CommonUtils.returnErrorInfo("IOT鉴权失败");
            }
            //发送请求
//            okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramStr);
//            Response response = okHttpUtil.doPostRequest(body, iotConfig.getInsUrl() + "/" + deviceId, token);


            Map<String,String> header = iotOperationCommon.getHeaderMap(token);
            int postRes = HttpClientUtils.getHttpResponseCode( iotConfig.getInsUrl() + "/" + deviceId, "POST", header, paramStr);
            if(postRes == 0){
                messageResp.setResult(Boolean.FALSE.toString());
                messageResp.setResultDesc("发送失败");
            }else{
                messageResp.setData(postRes);
                messageResp.setResult(Boolean.TRUE.toString());
                messageResp.setResultDesc("发送成功");
            }
        } catch (Exception e) {
            logger.warn("指令下发异常,{}", e.getMessage());
            e.printStackTrace();
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("发送失败");
        }
        return messageResp;
    }

    /**
     * 获取IOT的token
     *
     * @return
     */
    protected String getIotToken() {
        String token = null;
        JSONObject loginParam = new JSONObject();
        loginParam.put("username", iotConfig.getUsername());
        loginParam.put("password", iotConfig.getPassword());
        String paramStr = loginParam.toJSONString();

        Map<String,String> header = new HashMap<>();
        header.put("Content-Type", "application/Json; charset=UTF-8");
        String postRes = HttpClientUtils.httpRequest( iotConfig.getUrl() + "/api/auth/login", "POST", header, paramStr);
//        okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramStr);
//        Response response = okHttpUtil.doPostRequest(body, iotConfig.getUrl() + "/api/auth/login", null);
        try {
//            if (response.code() == 200) {
            if(null != postRes){
//                String tokenStr = response.body().string();
               JSONObject tokenObj = (JSONObject) JSONObject.parse(postRes);
               token = tokenObj.getString("token");
            } else {
                logger.error("登录IOT异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        finally {
//            if (null != response) {
//                response.body().close();
//                response.close();
//            }
//        }
        return token;
    }

}
