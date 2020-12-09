package com.zhcx.authorization.common;

import com.alibaba.fastjson.JSONObject;
import com.zhcx.authorization.config.IotConfig;
import com.zhcx.authorization.utils.HttpClientUtils;
import okhttp3.MediaType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: basic-data-service
 * @ClassName:IOTOperationCommon
 * @description: IOT操作通用类
 * @author: ZhangKai
 * @create: 2018-11-27 13:28
 **/
@Component
public class IotOperationCommon {

    private static final Logger logger = LoggerFactory.getLogger(IotOperationCommon.class);

    public static final MediaType MEDIA_TYPE_JSON  = MediaType.parse("application/json; charset=utf-8");

    @Resource(name="redisTemplate4Json")
	private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private IotConfig iotConfig;

    /**
     * 登录IOT获取Token
     * @return
     */
    public String doLoginIot(){
        
        String token = redisTemplate.opsForValue().get("iot_token");
        if(null!=token){
        	return token;
        }
        JSONObject loginParam = new JSONObject();
        loginParam.put("username", iotConfig.getUsername());
        loginParam.put("password",iotConfig.getPassword());
        String paramStr = loginParam.toJSONString();

        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Content-Type", "application/Json; charset=UTF-8");
//        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON,paramStr);
        String resStr = null;
        try {
            resStr = HttpClientUtils.httpRequest(
                    iotConfig.getUrl()+ "/api/auth/login", "POST",
                    headerParams, JSONObject.toJSONString(loginParam));
            if (StringUtils.isNotBlank(resStr)) {
                JSONObject respObj = JSONObject.parseObject(resStr);
                token = respObj.getString("token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        redisTemplate.opsForValue().set("iot_token", token, 10, TimeUnit.MINUTES);
        return token;
    }




    public boolean sendTts(String deviceId,String text,String sendType){
        boolean result = false;
        String token = doLoginIot();
        String url = iotConfig.getTtsUrl()+"/"+deviceId;
        logger.info("iot调用url:{}",url);
        JSONObject param = new JSONObject();
        param.put("method","33536");
        JSONObject subParam = new JSONObject();
        subParam.put("FLAG",sendType);
        subParam.put("TEXT",text);
        param.put("params",subParam);


        Map<String, String> headerParams = getHeaderMap(token);

        String resStr = null;
        JSONObject resObj = null;
        try {
            resStr = HttpClientUtils.httpRequest(url,"POST",headerParams,param.toJSONString());
        } catch (Exception e) {
            logger.error("TTS下发失败,{}",e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 封装参数
     * @param terminalCode 设备编号
     * @param deviceId 当前车辆的deviceId
     * @param recordDeviceId 查询得到的deviceId
     * @return
     */
    protected JSONObject packagingUpdateParam(String terminalCode,String deviceId,String recordDeviceId){
        JSONObject updateParam = new JSONObject();
        JSONObject subUpdateParam = new JSONObject();
        JSONObject idObj = new JSONObject();
        updateParam.put("credentialsId",terminalCode);
        updateParam.put("credentialsType","ACCESS_TOKEN");
        subUpdateParam.put("entityType","DEVICE");
        subUpdateParam.put("id", deviceId);
        idObj.put("id",recordDeviceId);
        updateParam.put("deviceId", subUpdateParam);
        updateParam.put("id", idObj);
        return updateParam;
    }

    public Map<String,String> getHeaderMap(String token){
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Content-Type", "application/Json; charset=UTF-8");
        if(null != token && !"".equals(token)){
            headerParams.put("X-Authorization", "Bearer " + token);
        }
        return headerParams;
    }

}
