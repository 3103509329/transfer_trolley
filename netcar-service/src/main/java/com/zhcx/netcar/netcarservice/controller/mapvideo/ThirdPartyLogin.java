package com.zhcx.netcar.netcarservice.controller.mapvideo;

import com.zhcx.netcar.constant.ThirdPartyURL;
import com.zhcx.netcar.netcarservice.utils.HttpClientHelper;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 本方法针对第三方平台进行登陆以及相关处理
 *
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/5/31 0031 9:23
 **/
@EnableScheduling
@Service
public class ThirdPartyLogin{

    private static final Logger log = LoggerFactory.getLogger(ThirdPartyLogin.class);

    @Value("${netcar.map_video.account}")
    public String account;

    @Value("${netcar.map_video.password}")
    public String password;

    @Value("${netcar.map_video.redis.key}")
    public String map_video_key;

    @Resource(name = "redisTemplate4Json")
    private RedisTemplate<String, String> redisTemplate;

    public Map<String, String> param = new HashMap<>();


    @Scheduled(cron = "${thirdparty.schedule.cron}")
    @Async
    public void run() throws Exception {
        this.param.put("account", account);
        this.param.put("password", password);
        http();
    }

    public String uodateJsession() throws InterruptedException {
        return http();
    }

    private String http() throws InterruptedException {
        String request = HttpClientHelper.sendGet(ThirdPartyURL.LOGIN.getDesc(), param);
        Map<String, Object> jsession = JSONObject.fromObject(request);
        String result = String.valueOf(jsession.get("jsession"));
        insertRedis(map_video_key, result);
        return result;
    }

    private void insertRedis(String key, String resp) throws InterruptedException {
        try {
            redisTemplate.opsForValue().set(key, resp);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("write redis异常:{}", e.getMessage());
            Thread.sleep(1000);
            redisTemplate.opsForValue().set(key, resp);
        }
    }
}
