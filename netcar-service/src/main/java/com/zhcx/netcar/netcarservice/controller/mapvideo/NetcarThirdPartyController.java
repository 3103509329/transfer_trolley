package com.zhcx.netcar.netcarservice.controller.mapvideo;

import com.google.gson.Gson;
import com.zhcx.netcar.constant.ThirdPartyURL;
import com.zhcx.netcar.facade.mapAndVideo.NetcarThirdPartyService;
import com.zhcx.netcar.facade.yunzheng.YunZhengCompanyService;
import com.zhcx.netcar.facade.yunzheng.YunZhengVehicleService;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.HttpClientHelper;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.pojo.mapAndVideo.*;
import com.zhcx.netcar.pojo.yuzheng.YunZhengCompany;
import com.zhcx.netcar.pojo.yuzheng.YunZhengVehicle;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 地图服务：
 * 1.实时定位
 * 2.历史轨迹
 *
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/5/27 0027 17:06
 **/
@RestController
@RequestMapping("/netcar/map_video")
public class NetcarThirdPartyController {

    private Logger log = LoggerFactory.getLogger(NetcarThirdPartyController.class);

    @Resource(name = "redisTemplate4Json")
    private RedisTemplate<String, String> redisTemplate;

    @Value("${netcar.map_video.redis.key}")
    public String map_video_key;

    @Autowired
    private YunZhengVehicleService yunZhengVehicleService;

    @Autowired
    private YunZhengCompanyService yunZhengCompanyService;

    @Autowired
    private ThirdPartyLogin thirdPartyLogin;

    @Autowired
    private NetcarThirdPartyService netcarThirdPartyService;


    @Value("${netcar.third-party.account}")
    public String account;

    @Value("${netcar.third-party.password}")
    public String password;


    @GetMapping("/company")
    public MessageResp getCompany(HttpServletRequest request) {
        MessageResp messageResp = new MessageResp();
        List<YunZhengCompany> yunZhengCompanies = new ArrayList<>();
        try {
            yunZhengCompanies = yunZhengCompanyService.selectAll();
            messageResp.setData(yunZhengCompanies);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    /**
     * 获取实时定位数据：
     * 1.企业下所有在线车辆定位数据
     * 2.指定车辆的定位数据
     *
     * @param request
     * @return
     */
    @GetMapping("/map_current")
    public MessageResp getCurrentLocation(HttpServletRequest request,
                                          @RequestParam(required = false) String companyId,
                                          @RequestParam(required = false) String vehicleNo) {
        MessageResp messageResp = new MessageResp();
        try {
            NetcarThirdParty netcarThirdParty = new NetcarThirdParty();
            netcarThirdParty = netcarThirdPartyService.selectByCompanyId(companyId);
            String token = getRedis();
            Map<String, String> map = new HashMap<>();
            map.put("jsession", token);
            if (!vehicleNo.isEmpty() && !vehicleNo.equals("")){
                map.put("", getEquipmentNumber(vehicleNo, token));
            }
            String url = ThirdPartyURL.REALLOCATION.getDesc();
            String httpResult = HttpClientHelper.sendGet(url, map);
            httpResult = judgeResult(httpResult, url, map);
            Map<String, Object> resultMap = new Gson().fromJson(httpResult, Map.class);
            messageResp.setData(resultMap);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    /**
     * 获取指定车辆指定时间区间的轨迹数据
     *
     * @param request
     * @return
     */
    @GetMapping("/map_track")
    public MessageResp getTrack(HttpServletRequest request,
                                @RequestParam String vehiIdno,
                                @RequestParam String begintime,
                                @RequestParam String endtime
    ) {
        MessageResp messageResp = new MessageResp();
        Object obj = new Object();
        MapRsult result = new MapRsult();
        try {

                messageResp.setResult(Boolean.TRUE.toString());
                messageResp.setResultDesc("查询成功");

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    /**
     * 实时视频业务：
     * 1.本页无主要实现居于前端的视频插件
     * 2.后端的服务主要作用是提供所需的 会话标识，设备编号等
     *
     * @return
     */
    @GetMapping("/video_current")
    public MessageResp getCurrentVideo(HttpServletRequest request,
                                       @RequestParam String vehicleNo) {
        MessageResp messageResp = new MessageResp();
        Map<String, Object> map = new HashMap<>();
        try {

            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    /**
     * 视频文件服务：
     * 1.返回指定时间内的说有视频历史文件
     */
    @GetMapping("/video_list")
    @ResponseBody
    public MessageResp getVideoList(HttpServletRequest request,
                                    @RequestParam String vehicleNo,
                                    @RequestParam String year,
                                    @RequestParam String mon,
                                    @RequestParam String day,
                                    @RequestParam(required = false) Integer currentPage,
                                    @RequestParam(required = false) Integer pageRecords) {
        MessageResp messageResp = new MessageResp();
        String requestData = new String();
        try {

            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }


        return messageResp;
    }


    /**
     * 获取设备ID
     * 通过车辆号牌获取设备编号
     *
     * @param vehiIdno
     * @param jsession
     * @return
     */
    private String getEquipmentNumber(String vehiIdno, String jsession) throws Exception {
        String url = ThirdPartyURL.DEVICEID.getDesc();
        Map<String, String> param = new HashMap<>();
        param.put("jsession", jsession);
        param.put("vehiIdno", vehiIdno);
        String httpResult = HttpClientHelper.sendGet(url, param);
        httpResult = judgeResult(httpResult, url, param);
        Map<String, Object> map = JSONObject.fromObject(httpResult);

        String result = null;
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("data");
        if (!list.isEmpty() && list.size() > 0) {
            Map<String, Object> vehicleData = list.get(0);
            result = (String) vehicleData.get("deviceId");
        }
        return result;
    }


    /**
     * 获取指定公司的所有车辆
     *
     * @param companyId
     * @return
     */
    private List<YunZhengVehicle> getVehicleList(String companyId, String vehicleNo) {
        List<YunZhengVehicle> vehicleList = yunZhengVehicleService.selectListByCompanyId(companyId, vehicleNo);
        return vehicleList;
    }

    private String getRedis() throws InterruptedException {
        String result = redisTemplate.opsForValue().get(map_video_key);
        if (result.isEmpty()) {
            Map<String, String> param = new HashMap<>();
            param.put("account", account);
            param.put("password", password);
            result = HttpClientHelper.sendGet(ThirdPartyURL.LOGIN.getDesc(), param);
            Map<String, Object> jsession = JSONObject.fromObject(result);
            insertRedis(map_video_key, String.valueOf(jsession.get("jsession")));
        }
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

    /**
     * 返回值判断
     *
     * @param httpRsult
     * @param url
     * @param map
     * @return
     * @throws Exception
     */
    private String judgeResult(String httpRsult, String url, Map<String, String> map) throws Exception {
        if (httpRsult != null && (httpRsult.equals("{\"result\":5}") || httpRsult.equals("{\"result\":4}"))) {
            thirdPartyLogin.run();
            httpRsult = HttpClientHelper.sendGet(url, map);
        }
        return httpRsult;
    }
}
