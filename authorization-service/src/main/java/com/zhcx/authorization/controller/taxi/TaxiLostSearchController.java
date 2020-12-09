package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.*;
import com.zhcx.basicdata.facade.taxi.*;
import com.zhcx.basicdata.pojo.taxi.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/taxi/lostSearch")
@Api(value = "taxiLostSearch", tags = "出租车失物查找接口")
public class TaxiLostSearchController {

    private Logger log = LoggerFactory.getLogger(TaxiLostSearchController.class);

    @Autowired
    private TaxiLostObjectSearchService lostSearchService;

    @Autowired
    private TaxiLostSearchAttachService lostAttachService;

    @Autowired
    private TaxiLostSearchAreaService lostAreaService;

    @Value("${druid.lostfound.table}")
    private String lostfoundTable;

    @Autowired
    private TaxiBaseInfoVehicleService taxiBaseInfoVehicleService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private TaxiBaseInfoDriverService driverService;


    @GetMapping("/queryLostList")
    @ApiOperation(value = "失物查找列表", notes = "参数status，取回状态")
    public MessageResp queryLostList(@ModelAttribute TaxiLostObjectSearch param) {
        MessageResp resp = new MessageResp();
        PageInfo<TaxiLostObjectSearch> pageInfo = null;
        try {
            pageInfo = lostSearchService.queryLostList(param);
            List<TaxiLostObjectSearch> searchList = pageInfo.getList();
            List<Long> searchIds = new ArrayList<>();
            searchList.forEach(obj -> {
                searchIds.add(obj.getUuid());
            });
            Map<Long, List<TaxiLostSearchAttach>> map = new HashMap<>();
            //关联匹配车辆
            if (searchIds.size() > 0) {
                List<TaxiLostSearchAttach> attachList = lostAttachService.querySearchAttachList(searchIds);
                if (attachList != null && attachList.size() > 0) {
                    attachList.forEach(attach -> {
                        Long searchId = attach.getSearchId();
                        if (map.containsKey(searchId)) {
                            map.get(searchId).add(attach);
                        } else {
                            List<TaxiLostSearchAttach> list = new ArrayList<>();
                            list.add(attach);
                            map.put(searchId, list);
                        }
                    });
                }
            }
            searchList.forEach(search -> {
                search.setAttachList(map.get(search.getUuid()));
            });
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setData(pageInfo.getList());
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询失物查找列表成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("查询失物查找列表异常");
        }
        return resp;
    }

    @GetMapping("/queryLostInfo")
    @ApiOperation(value = "失物查找详情", notes = "uuid")
    public MessageResp queryLostInfo(@RequestParam Long uuid) {
        MessageResp resp = new MessageResp();
        try {
            TaxiLostObjectSearch lostSearch = lostSearchService.queryLostSearchInfo(uuid);
            TaxiLostSearchAttach param = new TaxiLostSearchAttach();
            param.setSearchId(uuid);
            if (lostSearch != null) {
                List<Long> searchIds = new ArrayList<>();
                searchIds.add(lostSearch.getUuid());
                lostSearch.setAttachList(lostAttachService.querySearchAttachList(searchIds));
                lostSearch.setAreaList(lostAreaService.queryLostAreaList(uuid));
            }
            resp.setData(lostSearch);
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询失物查找详情成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("查询失物查找详情异常");
        }
        return resp;
    }

    @PutMapping("/updateLostInfo")
    @ApiOperation(value = "更新失物查找信息", notes = "参数lostSearch")
    public MessageResp updateLostInfo(HttpServletRequest request, @RequestBody TaxiLostObjectSearch lostSearch) {
        MessageResp resp = new MessageResp();
        try {
            AuthUserResp authUser = sessionHandler.getUser(request);
            lostSearch.setUpdateBy(authUser.getUserName());
            lostSearch.setUpdateTime(new Date());
            String status = lostSearch.getStatus();
            if (StringUtils.equals(status, "10")) {
                lostSearch.setResult("未取回");
            }
            if (StringUtils.equals(status, "20")) {
                lostSearch.setResult("已取回");
            }
            lostSearchService.updateLostSearch(lostSearch);

            //更新匹配车辆，先删除老的，再新增
            List<TaxiLostSearchAttach> attachList = lostSearch.getAttachList();
            if (attachList != null && attachList.size() > 0) {
                Long searchId = lostSearch.getUuid();
                lostAttachService.deleteAttachBySearch(searchId);
                attachList.forEach(attach -> {
                    attach.setSearchId(searchId);
                    try {
                        lostAttachService.addLostAttach(attach);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        resp.setStatusCode("00");
                        resp.setResult(Boolean.FALSE.toString());
                        resp.setResultDesc("更新失物查找匹配车辆信息异常");
                    }
                });
            }
            resp.setData(lostSearch);
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("更新失物查找信息成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("更新失物查找信息异常");
        }
        return resp;
    }

    @PostMapping("/addLostInfo")
    @ApiOperation(value = "添加失物查找信息", notes = "参数lostSearch")
    public MessageResp addLostInfo(HttpServletRequest request, @RequestBody TaxiLostObjectSearch lostSearch) {
        MessageResp resp = new MessageResp();
        try {
            AuthUserResp authUser = sessionHandler.getUser(request);
            Date current = new Date();
            lostSearch.setStatus("10");
            lostSearch.setResult("处理中");
            lostSearch.setCreateBy(authUser.getUserName());
            lostSearch.setCreateTime(current);
            lostSearch.setUpdateBy(authUser.getUserName());
            lostSearch.setUpdateTime(current);
            lostSearchService.addLostSearch(lostSearch);

            //新增匹配车辆
            List<TaxiLostSearchAttach> attachList = lostSearch.getAttachList();
            if (attachList != null && attachList.size() > 0) {
                attachList.forEach(attach -> {
                    attach.setSearchId(lostSearch.getUuid());
                    try {
                        lostAttachService.addLostAttach(attach);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        resp.setStatusCode("00");
                        resp.setResult(Boolean.FALSE.toString());
                        resp.setResultDesc("添加失物查找匹配车辆信息异常");
                    }
                });
            }
            resp.setData(lostSearch);
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("添加失物查找信息成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("添加失物查找信息异常");
        }
        return resp;
    }

    @DeleteMapping("/deleteLostInfo")
    @ApiOperation(value = "删除失物查找信息", notes = "uuid")
    public MessageResp deleteLostInfo(@RequestParam Long uuid) {
        MessageResp resp = new MessageResp();
        try {
            lostSearchService.deleteLostSearch(uuid);
            //删除匹配车辆信息
            lostAttachService.deleteAttachBySearch(uuid);
            //删除匹配的区域信息
            lostAreaService.deleteLostSearchArea(uuid);
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("删除失物查找信息成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("删除失物查找信息异常");
        }
        return resp;
    }

    @PutMapping("/dealWithLostSearch")
    @ApiOperation(value = "改变失物查找状态", tags = "参数 uuid， status")
    public MessageResp dealWithLostSearch(HttpServletRequest request, @RequestParam Long uuid, @RequestParam String status) {
        MessageResp resp = new MessageResp();
        try {
            AuthUserResp authUser = sessionHandler.getUser(request);
            TaxiLostObjectSearch search = new TaxiLostObjectSearch();
            search.setUpdateBy(authUser.getUserName());
            if (StringUtils.equals(status, "10")) {
                search.setResult("未取回");
            }
            if (StringUtils.equals(status, "20")) {
                search.setResult("已取回");
            }
            search.setUpdateTime(new Date());
            search.setUuid(uuid);
            search.setStatus(status);
            lostSearchService.dealWithLostSearch(search);
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("失物查找处理成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("失物查找处理异常");
        }
        return resp;
    }

    @PostMapping("/addAttachInfo")
    @ApiOperation(value = "添加失物查找匹配车辆,区域信息", notes = "参数lostSearch")
    public MessageResp addAttachInfo(@RequestBody TaxiLostObjectSearch lostSearch) {
        MessageResp resp = new MessageResp();
        try {
            Long searchId = lostSearch.getUuid();
            List<TaxiLostSearchAttach> attachList = lostSearch.getAttachList();
            if (attachList != null) {
                lostAttachService.deleteAttachBySearch(searchId);
                if (attachList.size() > 0) {
                    lostAttachService.addLostAttachs(searchId, attachList);
                }
            }
            List<TaxiLostSearchArea> areaList = lostSearch.getAreaList();
            if (areaList != null) {
                lostAreaService.deleteLostSearchArea(searchId);
                if (areaList.size() > 0) {
                    lostAreaService.addLostSearchAreas(searchId, areaList);
                }
            }
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("处理成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("处理异常");
        }
        return resp;
    }

    @PostMapping("/searchAreaVehicles")
    @ApiOperation(value = "区域搜索车辆", tags = "参数 areaList")
    public MessageResp searchAreaVehicles(@RequestBody TaxiLostObjectSearch lostSearch) {
        MessageResp resp = new MessageResp();
        log.debug("搜索车辆开始：" + (new Date()).getTime());
        try {
            List<TaxiLostSearchArea> areaList = lostSearch.getAreaList();
            if (areaList == null || areaList.size() == 0) {
                resp.setStatusCode("00");
                resp.setResult(Boolean.TRUE.toString());
                resp.setResultDesc("区域搜索车辆成功");
                return resp;
            }

            String startTime = lostSearch.getStartTime();
            String endTime = lostSearch.getEndTime();

            if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
                resp.setStatusCode("-50");
                resp.setResult(Boolean.FALSE.toString());
                resp.setResultDesc("时间范围不能为空");
                return resp;
            }
            for (TaxiLostSearchArea area : areaList) {
                List<Map<String, Object>> data = new ArrayList<>();
                StringBuilder sqlBuilder = new StringBuilder();
                String sql = null;
                String coordinate = area.getCoordinate();
                Gson gson = new Gson();
                List<Double[]> list = gson.fromJson(coordinate, new TypeToken<List<Double[]>>() {
                }.getType());
                Double longitudeparam1 = list.get(3)[0];//经度(长方形左下角)
                Double latitudeparam1 = list.get(3)[1];//纬度(长方形左下角)
                Double longitudeparam2 = list.get(1)[0];//经度(长方形右上角)
                Double latitudeparam2 = list.get(1)[1];//纬度(长方形右上角)
                log.debug("转换前：("+longitudeparam1+","+latitudeparam1+")");
                //坐标转换（百度坐标转GPS坐标）
                Map<String, Double> mapgps = TransferAxesUitl.bd09ToWGS84(latitudeparam1, longitudeparam1);
                longitudeparam1 = mapgps.get("lng");
                latitudeparam1 = mapgps.get("lat");
                log.debug("转换后：("+longitudeparam1+","+latitudeparam1+")");
                Map<String, Double> mapgps2 = TransferAxesUitl.bd09ToWGS84(latitudeparam2, longitudeparam2);
                longitudeparam2 = mapgps2.get("lng");
                latitudeparam2 = mapgps2.get("lat");
                sqlBuilder.append("SELECT DEVICE_ID  FROM ");
                sqlBuilder.append("\"" + lostfoundTable + "\"");
                sqlBuilder.append(" WHERE LONGITUDE >=");
                sqlBuilder.append(longitudeparam1);
                sqlBuilder.append(" AND LONGITUDE<");
                sqlBuilder.append(longitudeparam2);
                sqlBuilder.append(" AND LATITUDE>=");
                sqlBuilder.append(latitudeparam1);
                sqlBuilder.append(" AND LATITUDE<");
                sqlBuilder.append(latitudeparam2);
                sqlBuilder.append(" AND \"TIMESTAMP\">=");
                sqlBuilder.append(DateUtil.getYMDHMSFormat(startTime).getTime());
                sqlBuilder.append(" AND \"TIMESTAMP\"<");
                sqlBuilder.append(DateUtil.getYMDHMSFormat(endTime).getTime());
                sqlBuilder.append(" GROUP BY DEVICE_ID");
                sql = sqlBuilder.toString();
                log.info("singleVehicleIncomeCount SQL: " + sql);
                JSONArray json = httpUtils.doPostSqlUrl("sql", sql);
                String[] dlist = new String[json.size()];
                for (int i = 0; i < json.size(); i++) {
                    Map<String, String> map = (Map<String, String>) json.get(i);
                    dlist[i] = map.get("DEVICE_ID").toString();
                }
                if (dlist.length < 1) {
                    continue;
                }
                TaxiBaseInfoVehicle param = new TaxiBaseInfoVehicle();
                param.setDeviceIds(dlist);
                List<TaxiBaseInfoVehicle> vehicles = taxiBaseInfoVehicleService.queryVehicles(param);
                for (TaxiBaseInfoVehicle vehicle : vehicles) {
                    Map<String, Object> temp = new HashMap<>();
                    temp.put("corpId", vehicle.getCorpId());
                    temp.put("corpName", vehicle.getCorpName());
                    temp.put("plateNum", vehicle.getPlateNumber());
                    String driStr = getWorkInVehicleId(vehicle.getUuid());//查询打卡驾驶员
                    if (!StringUtils.isBlank(driStr)) {
                        String[] split = driStr.split(",");
                        String driverId = split[0];
                        TaxiBaseInfoDriver driver = driverService.selectDriver(driverId);
                        if (driver != null) {
                            temp.put("driverId", driverId);
                            temp.put("driverName", driver.getName());
                            temp.put("driverPhone", driver.getPhoneNumber());
                        }
                    }
                    data.add(temp);
                }
                area.setData(data);
            }

            log.debug("搜索车辆结束：" + (new Date()).getTime());
            resp.setData(lostSearch);
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("区域搜索车辆成功");

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("区域搜索车辆异常");
        }
        return resp;
    }
    

/*    @PostMapping("/searchAreaVehicles")
    @ApiOperation(value = "区域搜索车辆", tags = "参数 areaList")
    public MessageResp searchAreaVehicles(@RequestBody TaxiLostObjectSearch lostSearch){
        MessageResp resp = new MessageResp();
        log.debug("搜索车辆开始："+(new Date()).getTime());
        List<Map<String, Object>> data = new ArrayList<>();
        try {
            List<TaxiLostSearchArea> areaList = lostSearch.getAreaList();
            if(areaList == null || areaList.size() == 0){
                resp.setStatusCode("00");
                resp.setResult(Boolean.TRUE.toString());
                resp.setResultDesc("区域搜索车辆成功");
                return resp;
            }
          
            String startTime = lostSearch.getStartTime();
            String endTime = lostSearch.getEndTime();
            
            if(StringUtils.isEmpty(startTime)||StringUtils.isEmpty(endTime)){
            	resp.setStatusCode("-50");
                resp.setResult(Boolean.FALSE.toString());
                resp.setResultDesc("时间范围不能为空");
                return resp;
            }
            List<TaxiBaseInfoVehicle>  list = taxiBaseInfoVehicleService.queryVehicles();
            String token = iotOperationCommon.doLoginIot();
            for (TaxiBaseInfoVehicle taxiBaseInfoVehicle : list) {
            	String url = iotConfig.getUrl()+"/api/plugins/telemetry/DEVICE/"
        				+ taxiBaseInfoVehicle.getDeviceId() + "/values/" + "timeseries?" + "startTs=" + DateUtil.getYMDHMSFormat(startTime).getTime()
        				+ "&endTs=" + DateUtil.getYMDHMSFormat(endTime).getTime() + "&keys=" + iotConfig.getKeys() + "&agg=" + iotConfig.getAgg()
        				+ "&interval=" + iotConfig.getInterval() + "&limit=" + iotConfig.getLimit();
            	Map<String, String> map = new HashMap<String, String>();
        		map.put("X-Authorization", "Bearer " + token);
        		map.put("Content-Type", "application/Json; charset=UTF-8");
        		String result = null;
        		result = HttpClientUtils.httpRequest(url, "GET", map, null);
        		JSONObject rObj = JSONObject.parseObject(result);
        		String str = rObj.getString("K512");
        		List resultList = JSONObject.parseArray(str);
        		if(null != resultList){
        			Map<String, TaxiBaseInfoVehicle> resultMap = this.getAreaDriverMap(areaList, resultList,taxiBaseInfoVehicle);
        			for(String key : resultMap.keySet()) {
        				TaxiBaseInfoVehicle vehicle = resultMap.get(key);
                        Map<String, Object> temp = new HashMap<>();
                        temp.put("corpId", vehicle.getCorpId());
                        temp.put("corpName", vehicle.getCorpName());
                        temp.put("plateNum", vehicle.getPlateNumber());
                        String driStr = getWorkInVehicleId(vehicle.getUuid());//查询打卡驾驶员
                        if (!StringUtils.isBlank(driStr)){
                        	 String[] split = driStr.split(",");
                             String driverId = split[0];
                             TaxiBaseInfoDriver driver = driverService.selectDriver(driverId);
                             if (driver != null) {
                            	 temp.put("driverId", driverId);
                                 temp.put("driverName",driver.getName());
                                 temp.put("driverPhone", driver.getPhoneNumber());
                             }
                        }
                        data.add(temp);
                    }
        		}
        		
			}
            log.debug("搜索车辆结束："+(new Date()).getTime());
            resp.setData(data);
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("区域搜索车辆成功");
            
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("区域搜索车辆异常");
        }
        return resp;
    }*/

    /*    @PostMapping("/searchAreaVehicles")
        @ApiOperation(value = "区域搜索车辆", tags = "参数 areaList")
        public MessageResp searchAreaVehicles(@RequestBody TaxiLostObjectSearch lostSearch){
            MessageResp resp = new MessageResp();
            try {
                List<TaxiLostSearchArea> areaList = lostSearch.getAreaList();
                if(areaList == null || areaList.size() == 0){
                    resp.setStatusCode("00");
                    resp.setResult(Boolean.TRUE.toString());
                    resp.setResultDesc("区域搜索车辆成功");
                    return resp;
                }
                String startTime = lostSearch.getStartTime();
                String endTime = lostSearch.getEndTime();
                String cityName = Constants.DEAULT_CITY_NAME;
                JSONArray jsonArray = null;
                try {
                    jsonArray = orderService.queryOrderList(cityName, startTime, endTime);
                } catch (Exception e1) {
                    log.error(e1.getMessage());
                    resp.setResult(Boolean.FALSE.toString());
                    resp.setResultDesc("查询订单异常");
                    return resp;
                }
                if(jsonArray == null || jsonArray.size() == 0){
                    resp.setResult(Boolean.TRUE.toString());
                    resp.setResultDesc("搜索车辆成功");
                    return resp;
                }
                //保存已经匹配的驾驶员ID
                List<Map<String, Object>> result = new ArrayList<>();
                Map<String, Map<String, Object>> driverMap = this.getAreaDriverMap(areaList, jsonArray);
                for(String key : driverMap.keySet()) {
                    Map<String, Object> map = driverMap.get(key);
                    Map<String, Object> temp = new HashMap<>();
                    temp.put("corpId", map.get("CORP_ID"));
                    temp.put("corpName", map.get("CORP_NAME"));
                    temp.put("plateNum", map.get("PLATE_NUM"));
                    temp.put("driverId", map.get("DRIVER_ID"));
                    temp.put("driverName", map.get("DRIVER_NAME"));
                    temp.put("driverPhone", map.get("DRIVER_PHONE_NUMBER"));
                    result.add(temp);
                }
                resp.setData(result);
                resp.setResult(Boolean.TRUE.toString());
                resp.setResultDesc("区域搜索车辆成功");
            }catch (Exception e){
                log.error(e.getMessage());
                resp.setResult(Boolean.FALSE.toString());
                resp.setResultDesc("区域搜索车辆异常");
            }
            return resp;
        }
    */
    private Map<String, Map<String, Object>> getAreaDriverMap(List<TaxiLostSearchArea> areaList, JSONArray jsonArray) {
        Map<String, Map<String, Object>> driverMap = new HashMap<>();
        for (Object obj : jsonArray) {
            //已添加的驾驶员，不再做处理
            Map<String, Object> map = (Map<String, Object>) obj;
            String driverId = map.get("DRIVER_ID").toString();
            if (driverMap.containsKey(driverId)) {
                continue;
            }
            Double onLatitude = Double.parseDouble(map.get("ON_LATITUDE").toString());
            Double onLongitude = Double.parseDouble(map.get("ON_LONGITUDE").toString());
            Double offLatitude = Double.parseDouble(map.get("OFF_LATITUDE").toString());
            Double offLongitude = Double.parseDouble(map.get("OFF_LONGITUDE").toString());
            for (TaxiLostSearchArea area : areaList) {
                String type = area.getType();
                String cordinate = area.getCoordinate();
                //圆形区域
                if (StringUtils.equals("1", type)) {
                    String center = area.getCenterCoordinate();
                    Double radiusStr = Double.parseDouble(area.getCoordinate());
                    Boolean contains = GeometryUtils.circleContainPoint(onLatitude, onLongitude, offLatitude, offLongitude, center, radiusStr, Constants.GEO_BD09);
                    if (contains != null && contains) {
                        driverMap.put(driverId, map);
                        break;
                    }
                } else {//多边形区域
                    Boolean contains = GeometryUtils.polygonContainPoint(onLatitude, onLongitude, offLatitude, offLongitude, cordinate, Constants.GEO_BD09);
                    if (contains != null && contains) {
                        driverMap.put(driverId, map);
                        break;
                    }
                }
            }
        }
        return driverMap;
    }

    private Map<String, TaxiBaseInfoVehicle> getAreaDriverMap(List<TaxiLostSearchArea> areaList, List resultList, TaxiBaseInfoVehicle taxiBaseInfoVehicle) {
        Map<String, TaxiBaseInfoVehicle> carMap = new HashMap<>();
        String carId = taxiBaseInfoVehicle.getUuid();
        for (Object object : resultList) {
            JSONObject jt = JSONObject.parseObject(JSON.toJSONString(object));
            JSONObject gps = JSONObject.parseObject(jt.getString("value"));
            //log.debug("deviceID:"+taxiBaseInfoVehicle.getDeviceId()+" LONGITUDE:  "+gps.getString("LONGITUDE"));

            Double onLatitude = Double.parseDouble(gps.getString("LATITUDE").toString());
            Double onLongitude = Double.parseDouble(gps.getString("LONGITUDE").toString());
            for (TaxiLostSearchArea area : areaList) {
                String type = area.getType();
                String cordinate = area.getCoordinate();
                //圆形区域
                if (StringUtils.equals("1", type)) {
                    String center = area.getCenterCoordinate();
                    Double radiusStr = Double.parseDouble(area.getCoordinate());
                    Boolean contains = GeometryUtils.circleContainPoint(onLatitude, onLongitude, center, radiusStr, Constants.GEO_BD09);
                    if (contains != null && contains) {
                        carMap.put(carId, taxiBaseInfoVehicle);
                        break;
                    }
                } else {//多边形区域
                    Boolean contains = GeometryUtils.polygonContainPoint(onLatitude, onLongitude, cordinate, Constants.GEO_BD09);
                    if (contains != null && contains) {
                        carMap.put(carId, taxiBaseInfoVehicle);
                        break;
                    }
                }
            }
        }
        return carMap;
    }


    /**
     * 查询当前车辆打卡驾驶员信息
     */
    protected String getWorkInVehicleId(String vehicleId) throws Exception {
        String result = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -5);// 5分钟之前的时间
        Date beforeD = beforeTime.getTime();
        String time = sdf.format(beforeD);

        StringBuilder sql = new StringBuilder();
        sql.append(" select tmp.DRIVER_ID,tmp.CHECKIN_TIME from ( ");
        sql.append("select CHECKIN_TIME,DRIVER_ID,max(__time) as checkin_time1 from taxi_check_in where ");
        sql.append(" VEHICLE_ID = '").append(vehicleId).append("'");
        sql.append(" group by DRIVER_ID,CHECKIN_TIME ) tmp  order by tmp.checkin_time1 DESC limit 1");
        JSONArray res = null;
        try {
            res = httpUtils.doPostSqlUrl("sql", sql.toString());
            if (res != null && res.size() > 0) {
                if (res.size() > 1) {
                    log.error("数据异常,当前车辆" + vehicleId + "同一时间存在两个驾驶员打卡记录");
                } else {
                    JSONObject obj = res.getJSONObject(0);
                    String checkinTime = obj.get("CHECKIN_TIME").toString();
                    result = obj.get("DRIVER_ID").toString() + "," + checkinTime;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
