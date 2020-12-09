//package com.zhcx.netcar.netcarservice.controller.mapvideo;
//
//import com.google.gson.Gson;
//import com.zhcx.netcar.constant.ThirdPartyURL;
//import com.zhcx.netcar.facade.yunzheng.YunZhengCompanyService;
//import com.zhcx.netcar.facade.yunzheng.YunZhengVehicleService;
//import com.zhcx.netcar.netcarservice.utils.CommonUtils;
//import com.zhcx.netcar.netcarservice.utils.HttpClientHelper;
//import com.zhcx.netcar.netcarservice.utils.MessageResp;
//import com.zhcx.netcar.pojo.mapAndVideo.*;
//import com.zhcx.netcar.pojo.yuzheng.YunZhengCompany;
//import com.zhcx.netcar.pojo.yuzheng.YunZhengVehicle;
//import net.sf.json.JSONObject;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * 地图服务：
// * 1.实时定位
// * 2.历史轨迹
// *
// * @author Administrator
// * @email 570815140@qq.com
// * @date 2019/5/27 0027 17:06
// **/
//@RestController
//@RequestMapping("/netcar/map_video")
//public class MapAndVideoServiceController {
//
//    private Logger log = LoggerFactory.getLogger(MapAndVideoServiceController.class);
//
//    @Resource(name = "redisTemplate4Json")
//    private RedisTemplate<String, String> redisTemplate;
//
//    @Value("${netcar.map_video.redis.key}")
//    public String map_video_key;
//
//    @Autowired
//    private YunZhengVehicleService yunZhengVehicleService;
//
//    @Autowired
//    private YunZhengCompanyService yunZhengCompanyService;
//
//    @Autowired
//    private ThirdPartyLogin thirdPartyLogin;
//
//    @GetMapping("/company")
//    public MessageResp getCompany(HttpServletRequest request) {
//        MessageResp messageResp = new MessageResp();
//        List<YunZhengCompany> yunZhengCompanies = new ArrayList<>();
//        try {
//            yunZhengCompanies = yunZhengCompanyService.selectAll();
//            messageResp.setData(yunZhengCompanies);
//            messageResp.setResult(Boolean.TRUE.toString());
//            messageResp.setResultDesc("查询成功");
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            e.printStackTrace();
//            return CommonUtils.returnErrorInfo("查询失败");
//        }
//        return messageResp;
//    }
//
//    /**
//     * 获取实时定位数据：
//     * 1.企业下所有在线车辆定位数据
//     * 2.指定车辆的定位数据
//     *
//     * @param request
//     * @return
//     */
//    @GetMapping("/map_current")
//    public MessageResp getCurrentLocation(HttpServletRequest request,
//                                          @RequestParam(required = false) String companyId,
//                                          @RequestParam(required = false) String vehicleNo) {
//        MessageResp messageResp = new MessageResp();
//        try {
//            List<CurrentEntity> results = new ArrayList<>();
//            Map<String, String> map = new HashMap<>();
//            //获取车辆所对应的驾驶员信息（驾驶员名称，驾驶员电话）
//            String thirdparty_url = ThirdPartyURL.VEHICLEANDDRIVER.getDesc();
//            Map<String, String> thirdparty_map = new HashMap<>();
//            thirdparty_map.put("jsession", getRedis());
//            String thirdparty_result = HttpClientHelper.sendGet(thirdparty_url, thirdparty_map);
//            thirdparty_result = judgeResult(thirdparty_result, thirdparty_url, thirdparty_map);
//            JSONObject dataResult = JSONObject.fromObject(thirdparty_result);
//            if (null != dataResult.get("vehicles")) {
//                List<Thirdparty_VehicleAndDriver> vehicles_driver = com.alibaba.fastjson.JSONObject.parseArray(dataResult.get("vehicles").toString(), Thirdparty_VehicleAndDriver.class);
//                Map<String, List<Thirdparty_VehicleAndDriver>> thirdparty_resultMap = vehicles_driver.stream().collect(Collectors.groupingBy(Thirdparty_VehicleAndDriver::getNm));
//                List<YunZhengVehicle> vehicleList = getVehicleList(companyId, vehicleNo);
//                for (YunZhengVehicle vehicle : vehicleList) {
//                    String url = ThirdPartyURL.STATE.getDesc();
//                    map.put("jsession", getRedis());
//                    map.put("vehiIdno", vehicle.getBranum());
//                    map.put("toMap", "2");
//                    map.put("driver", "1");
//                    map.put("geoaddress", "1");
//                    String requestData = HttpClientHelper.sendGet(url, map);
//                    requestData = judgeResult(requestData, url, map);
//                    JSONObject locationResult = JSONObject.fromObject(requestData);
//                    if (null != locationResult.get("status")) {
//                        List<CurrentEntity> result = com.alibaba.fastjson.JSONObject.parseArray(locationResult.get("status").toString(), CurrentEntity.class);
//                        String name = thirdparty_resultMap.get(result.get(0).getVid()).get(0).getDn();
//                        String phone = thirdparty_resultMap.get(result.get(0).getVid()).get(0).getDt();
//                        result.get(0).setTm(result.get(0).getGt());
//                        result.get(0).setJd(result.get(0).getMlng());
//                        result.get(0).setWd(result.get(0).getMlat());
//                        result.get(0).setVi(result.get(0).getVid());
//                        result.get(0).setPos(result.get(0).getPs());
//                        result.get(0).setOnline(Integer.valueOf(result.get(0).getOl()));
//                        result.get(0).setDn(name);
//                        result.get(0).setDt(phone);
//                        results.addAll(result);
//                    }
//                }
//            }
//            messageResp.setData(results);
//            messageResp.setResult(Boolean.TRUE.toString());
//            messageResp.setResultDesc("查询成功");
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            e.printStackTrace();
//            return CommonUtils.returnErrorInfo("查询失败");
//        }
//        return messageResp;
//    }
//
//    /**
//     * 获取指定车辆指定时间区间的轨迹数据
//     *
//     * @param request
//     * @return
//     */
//    @GetMapping("/map_track")
//    public MessageResp getTrack(HttpServletRequest request,
//                                @RequestParam String vehiIdno,
//                                @RequestParam String begintime,
//                                @RequestParam String endtime
//    ) {
//        MessageResp messageResp = new MessageResp();
//        Object obj = new Object();
//        MapRsult result = new MapRsult();
//        try {
//            if (StringUtils.isNotEmpty(vehiIdno)) {
//                //获取设备编号
//                List<Map<String, Object>> data = (List<Map<String, Object>>) getEquipmentNumber(vehiIdno, getRedis());
//                if (null == data) {
//                    messageResp.setResultDesc("车辆" + vehiIdno + "未安装设备");
//                    return messageResp;
//                }
//                Map<String, String> map = new HashMap<>();
//                map.put("jsession", getRedis());
//                map.put("vehiIdno", vehiIdno);
//                map.put("begintime", begintime);
//                map.put("endtime", endtime);
//
//                map.put("devIdno", String.valueOf(data.get(0).get("did")));
//                String url = ThirdPartyURL.MAP_TRACK.getDesc();
//                String httpResult = HttpClientHelper.sendGet(url, map);
//                httpResult = judgeResult(httpResult, url, map);
//                Map<String, Object> resultMap = new Gson().fromJson(httpResult, Map.class);
//                messageResp.setData(resultMap.get("tracks"));
//                messageResp.setResult(Boolean.TRUE.toString());
//                messageResp.setResultDesc("查询成功");
//            }else {
//                messageResp.setResult(Boolean.FALSE.toString());
//                messageResp.setResultDesc("查询失败");
//            }
//
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            e.printStackTrace();
//            return CommonUtils.returnErrorInfo("查询失败");
//        }
//        return messageResp;
//    }
//
//    /**
//     * 实时视频业务：
//     * 1.本页无主要实现居于前端的视频插件
//     * 2.后端的服务主要作用是提供所需的 会话标识，设备编号等
//     *
//     * @return
//     */
//    @GetMapping("/video_current")
//    public MessageResp getCurrentVideo(HttpServletRequest request,
//                                       @RequestParam String vehicleNo) {
//        MessageResp messageResp = new MessageResp();
//        Map<String, Object> map = new HashMap<>();
//        try {
//            String jsession = getRedis();
//            List<Map<String, Object>> data = (List<Map<String, Object>>) getEquipmentNumber(vehicleNo, jsession);
//            map.put("jsession=", jsession);
//            map.put("vehiIdno", vehicleNo);
//            map.put("devIdno", String.valueOf(data.get(0).get("did")));
//            messageResp.setData(map);
//            messageResp.setResult(Boolean.TRUE.toString());
//            messageResp.setResultDesc("查询成功");
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            e.printStackTrace();
//            return CommonUtils.returnErrorInfo("查询失败");
//        }
//        return messageResp;
//    }
//
//    /**
//     * 视频文件服务：
//     * 1.返回指定时间内的说有视频历史文件
//     */
//    @GetMapping("/video_list")
//    @ResponseBody
//    public MessageResp getVideoList(HttpServletRequest request,
//                                    @RequestParam String vehicleNo,
//                                    @RequestParam String year,
//                                    @RequestParam String mon,
//                                    @RequestParam String day,
//                                    @RequestParam(required = false) Integer currentPage,
//                                    @RequestParam(required = false) Integer pageRecords) {
//        MessageResp messageResp = new MessageResp();
//        String requestData = new String();
//        try {
//            //获取设备编号
//            String jsession = getRedis();
//            Map<String, String> map = new HashMap<>();
//            map.put("DownType", "2");
//            map.put("DevIDNO", vehicleNo);
//            map.put("LOC", "2");
//            map.put("CHN", "-1");
//            map.put("YEAR", year);
//            map.put("MON", mon);
//            map.put("DAY", day);
//            map.put("RECTYPE", "-1");
//            map.put("FILEATTR", "2");
//            map.put("BEG", "0");
//            map.put("END", "86399");
//            map.put("ARM1", "0");
//            map.put("ARM2", "0");
//            map.put("RES", "0");
//            map.put("STREAM", "0");
//            map.put("STORE", "0");
//            map.put("jsession", jsession);
//            String url = ThirdPartyURL.VICEO_LIST.getDesc();
//
//            VideoRsult result = new VideoRsult();
//            requestData = HttpClientHelper.sendGet(url, map);
//            requestData = judgeResult(requestData, url, map);
//            result = new Gson().fromJson(requestData, VideoRsult.class);
//            List<FeilsEntity> feilsEntityList = result.files;
//            if (feilsEntityList != null && feilsEntityList.get(0) instanceof FeilsEntity) {
//                feilsEntityList.forEach(feils -> {
//                    String location = "http://121.43.107.22:6604/3/5?DownType=3&jsession=";
//                    String str = feils.getDownUrl().replace(location, "");
//                    feils.setDownUrl(location + jsession + str + vehicleNo + ".mp4");
//                });
//            }
//            messageResp.setData(feilsEntityList);
//            messageResp.setResult(Boolean.TRUE.toString());
//            messageResp.setResultDesc("查询成功");
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            e.printStackTrace();
//            return CommonUtils.returnErrorInfo("查询失败");
//        }
//
//
//        return messageResp;
//    }
//
//
//    /**
//     * 获取设备ID
//     * 通过车辆号牌获取设备编号
//     *
//     * @param vehiIdno
//     * @param jsession
//     * @return
//     */
//    private Object getEquipmentNumber(String vehiIdno, String jsession) throws Exception {
//        String url = ThirdPartyURL.EQUIPMENT_NUMBER.getDesc();
//        Map<String, String> map = new HashMap<>();
//        map.put("jsession", jsession);
//        map.put("vehiIdno", vehiIdno);
//        String httpResult = HttpClientHelper.sendGet(url, map);
//        httpResult = judgeResult(httpResult, url, map);
//        Map<String, Object> result = JSONObject.fromObject(httpResult);
//        return result.get("devices");
//    }
//
//
//    /**
//     * 获取指定公司的所有车辆
//     *
//     * @param companyId
//     * @return
//     */
//    private List<YunZhengVehicle> getVehicleList(String companyId, String vehicleNo) {
//        List<YunZhengVehicle> vehicleList = yunZhengVehicleService.selectListByCompanyId(companyId, vehicleNo);
//        return vehicleList;
//    }
//
//    private String getRedis() {
//        String result = redisTemplate.opsForValue().get(map_video_key);
//        return result;
//    }
//
//    /**
//     * 返回值判断
//     *
//     * @param httpRsult
//     * @param url
//     * @param map
//     * @return
//     * @throws Exception
//     */
//    private String judgeResult(String httpRsult, String url, Map<String, String> map) throws Exception {
//        if (httpRsult != null && (httpRsult.equals("{\"result\":5}") || httpRsult.equals("{\"result\":4}"))) {
//            thirdPartyLogin.run();
//            httpRsult = HttpClientHelper.sendGet(url, map);
//        }
//        return httpRsult;
//    }
//}
