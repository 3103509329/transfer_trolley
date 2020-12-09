package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.*;
import com.zhcx.basicdata.facade.base.BaseDictionaryService;
import com.zhcx.basicdata.facade.taxi.*;
import com.zhcx.basicdata.pojo.base.BaseDistrict;
import com.zhcx.basicdata.pojo.taxi.TaxiAlarmOperate;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoCompany;
import com.zhcx.basicdata.pojo.taxi.TaxiStatisticsData;
import com.zhcx.regionmonitor.constant.MonitorRedisConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/taxi/statistic")
@Api(value = "statistic", tags = "出租车统计数据控制层")
public class StatisticController {

    private Logger log = LoggerFactory.getLogger(StatisticController.class);

    @Autowired
    private TaxiBaseInfoCompanyService companyService;

    @Autowired
    private TaxiBaseInfoDriverService driverService;

    @Autowired
    private TaxiBaseInfoVehicleService vehicleService;

    @Autowired
    private TaxiOrderStatisticService orderService;

    @Autowired
    private BaseDictionaryService dictionaryService;

    @Autowired
    private TaxiAlarmInfoService taxiAlarmInfoService;

    @Autowired
    private TaxiAlarmOperateService alarmOperateService;

    @Autowired
    private TaxiIndustrySituationService taxiIndustrySituationService;

    @Resource(name = "redisTemplate4Json")
    private RedisTemplate<String, String> redisTemplate;

    @Resource(name = "redisTemplate4Json")
    private RedisTemplate<String, List<TaxiStatisticsData>> redisTemplateTaxiStatisticsData;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Value("${druid.table.taxi.checkin}")
    private String checkInTable;

    @Autowired
    private HttpUtils httpUtils;

    @Value("${druid.order.table}")
    private String orderTable;


    @GetMapping("/queryCityData")
    @ApiOperation(value = "首页—市基础数据统计", notes = "企业，驾驶员，车辆，订单")
    public MessageResp queryCityData(HttpServletRequest request) {
        MessageResp resp = new MessageResp();
        TaxiStatisticsData statistics = new TaxiStatisticsData();
        String corpId = null;
        try {
            String cityName = Constants.DEAULT_CITY_NAME;
            AuthUserResp authUser = sessionHandler.getUser(request);
            //非管理员用户只能够查看自己企业
            if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
                corpId = String.valueOf(authUser.getCorpId());
            } else {
                statistics.setCompanyCount(companyService.queryCompanyCount(cityName));
            }
            statistics.setDriverCount(driverService.queryDriverCount(corpId, cityName));
            statistics.setVehicleCount(vehicleService.queryVehicleCount(corpId, cityName));
            JSONArray jsonArray = orderService.queryOrderCount(null, corpId, false);
            if (jsonArray != null && jsonArray.size() > 0) {
                Map<String, Object> map = (Map<String, Object>) jsonArray.get(0);
                Long orderCount = Long.parseLong(String.valueOf(map.get("total")));
                statistics.setOrderCount(orderCount);
            } else {
                statistics.setOrderCount(0L);
            }
            resp.setData(statistics);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询统计数据成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatusCode("-50");
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("查询统计数据异常");
        }
        return resp;
    }

    @GetMapping("/queryDistrictData")
    @ApiOperation(value = "首页—区域基础数据统计", notes = "企业，驾驶员，车辆，订单")
    public MessageResp queryDistrictData(HttpServletRequest request) {
        MessageResp resp = new MessageResp();
        try {
            String cityName = Constants.DEAULT_CITY_NAME;
            AuthUserResp authUser = sessionHandler.getUser(request);
            //非管理员用户只能够查看自己企业
            if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
                TaxiStatisticsData statisticsData = new TaxiStatisticsData();
                BaseDistrict district = null;
                String corpId = String.valueOf(authUser.getCorpId());
                TaxiBaseInfoCompany company = companyService.selectCompanyInfo(corpId);
                if (StringUtils.isNotBlank(company.getDistrict())) {
                    district = dictionaryService.getDistrictById(company.getDistrict());
                } else {
                    district = new BaseDistrict();
                }

                //获取订单总数
                JSONArray jsonArray = orderService.queryOrderCount(null, corpId, false);
                if (jsonArray != null && jsonArray.size() > 0) {
                    Map<String, Object> map = (Map<String, Object>) jsonArray.get(0);
                    Long orderCount = Long.parseLong(map.get("total").toString());
                    statisticsData.setOrderCount(orderCount);
                } else {
                    statisticsData.setOrderCount(0L);
                }
                //返回统计数据
                statisticsData.setDriverCount(driverService.queryDriverCount(corpId, cityName));
                statisticsData.setVehicleCount(vehicleService.queryVehicleCount(corpId, cityName));
                statisticsData.setCityId(String.valueOf(district.getUuid()));
                statisticsData.setCityName(district.getDisName());
                List<TaxiStatisticsData> result = new ArrayList<>();
                result.add(statisticsData);
                resp.setData(result);
                resp.setStatusCode("00");
                resp.setResult(Boolean.TRUE.toString());
                resp.setResultDesc("查询成功");
                return resp;
            }
            List<TaxiStatisticsData> redisData = null;
            List<List<TaxiStatisticsData>> list = redisTemplateTaxiStatisticsData.opsForList().range("taxiStatisticsData", 0, -1);
            if (null != list && list.size() > 0) {
                redisData = list.get(0);
                if (null != redisData && redisData.size() > 0) {
                    resp.setData(redisData);
                    resp.setStatusCode("00");
                    resp.setResult(Boolean.TRUE.toString());
                    resp.setResultDesc("查询统计数据成功");
                    return resp;
                }
            }
            List<TaxiStatisticsData> resultList = new ArrayList<TaxiStatisticsData>();
            List<TaxiStatisticsData> districtList = companyService.countDistrictBasicData(cityName);
            //按区域统计订单数量
            JSONArray jsonArray = orderService.queryOrderCount("D", null, false);
            Map<String, Object> countMap = new HashMap<>();
            if (jsonArray != null && jsonArray.size() > 0) {
                jsonArray.forEach(obj -> {
                    Map<String, Object> map = (Map<String, Object>) obj;
//                    countMap.put(String.valueOf(map.get("DISTRICT_ID")), Long.parseLong(String.valueOf(map.get("total"))));
                    countMap.put(String.valueOf(map.get("DISTRICT_ID")), map.get("total") + "," + map.get("sum_fee"));
                });
            }
            if (districtList != null && districtList.size() > 0) {
                districtList.forEach(data -> {
                    String totalAndSumFee = null;
                    if (null != countMap.get(data.getCityId())) {
                        totalAndSumFee = countMap.get(data.getCityId()).toString();
                        data.setOrderCount(Long.parseLong(totalAndSumFee.split(",")[0]));
                        data.setFeeAmont(totalAndSumFee.split(",")[1]);
                    }
                    if (data.getCompanyCount() != null && data.getCompanyCount() > 0) {
                        resultList.add(data);
                    }
                });
            }
            resp.setData(resultList);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询统计数据成功");
        } catch (Exception e) {
//            log.error(e.getMessage());
            e.printStackTrace();
            resp.setStatusCode("-50");
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("查询统计数据异常");
        }
        return resp;
    }

    @GetMapping("/queryMonitorDataCount")
    @ApiOperation(value = "监控中心-统计", notes = "驾驶员，车辆，订单，投诉，报警")
    public MessageResp queryMonitorDataCount(HttpServletRequest request) {
        MessageResp resp = new MessageResp();
        TaxiStatisticsData statistics = new TaxiStatisticsData();
        try {
            //车辆在离线
            Map<String, Object> onlineCount = taxiIndustrySituationService.vehOnlineCount();
            statistics.setOnLineCars(Long.parseLong(onlineCount.get("online").toString()));

            //驾驶员签到数
            statistics.setSignInDrivers(Long.parseLong(onlineDriver()));

            //营运次数
            JSONArray jsonArray = orderService.queryOrderCount(null, null, true);
            log.info("orderService.queryOrderCount方法返回结果:" + jsonArray.toString());
            if (jsonArray != null && jsonArray.size() > 0) {
                Map<String, Object> map = (Map<String, Object>) jsonArray.get(0);
                statistics.setOrderCount(Long.parseLong(map.get("total").toString()));
                log.info("orderService.queryOrderCount方法返回结果不为null");
            } else {
                statistics.setOrderCount(0L);
                log.info("orderService.queryOrderCount方法返回结果为null");
            }
            //投诉次数
            JSONArray compArray = orderService.queryComplaintCount(true);
            if (compArray != null && compArray.size() > 0) {
                Map<String, Object> compMap = (Map<String, Object>) compArray.get(0);
                statistics.setComplaintCount(Long.parseLong(String.valueOf(compMap.get("total"))));
            } else {
                statistics.setComplaintCount(0L);
            }

            //预警、报警
//            Map<String,Object> alarmWarnInfo = taxiAlarmInfoService.getAlaramWarnCount();
//            Long.parseLong(String.valueOf(alarmWarnInfo.get("warning")))
            try {
                //统计一键报警当日一键报警总数
                TaxiAlarmOperate alarmOperate = new TaxiAlarmOperate();
                alarmOperate.setAlarmSource("10");
                alarmOperate.setStartTime(DateUtil.getYMDHMSFormat(DateUtil.getTimesMorning(new Date())));//开始时间
                alarmOperate.setEndTime(DateUtil.getYMDHMSFormat(DateUtil.getTimesnight(new Date())));//结束时间
                List<TaxiAlarmOperate> list = alarmOperateService.queryAlarmCount(alarmOperate);
                statistics.setPreventionAlarmCount((long) list.size());
                //统计当日确认报警 (疑似警情)数
                alarmOperate.setConfirmationType("10");
                List<TaxiAlarmOperate> list2 = alarmOperateService.queryAlarmCount(alarmOperate);
                statistics.setAlarmCount((long) list2.size());
            } catch (Exception e) {
                log.debug("统计一键报警/疑似警情数异常");
            }


            //异常聚集报警
            String assembleAlarmCount = redisTemplate.opsForValue().get(MonitorRedisConst.ASSEMBLE_ALARM_COUNT);
            statistics.setUnusualGatherAlarmCount(Long.parseLong(assembleAlarmCount));

            //重点区域报警
            String keyAlarmCount = redisTemplate.opsForValue().get(MonitorRedisConst.KEY_ALARM_COUNT);
            statistics.setKeyAreaAlarmCount(Long.parseLong(keyAlarmCount));

            resp.setData(statistics);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询统计数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            resp.setStatusCode("-50");
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("查询统计数据异常");
        }
        return resp;
    }


    @GetMapping("/vehicleEfficiencyCount")
    @ApiOperation(value = "车辆效率统计", notes = "车辆效率统计")
    public MessageResp vehicleEfficiencyCount(HttpServletRequest request, String type, String startTime) {
        MessageResp resp = new MessageResp();
        try {
            if (StringUtils.isNotBlank(type) && StringUtils.isBlank(startTime)) {
                resp.setStatusCode("-50");
                resp.setResult(Boolean.FALSE.toString());
                resp.setResultDesc("统计时间不能为空");
                return resp;
            }
            //非管理员用户只能够查看自己企业
            AuthUserResp authUser = sessionHandler.getUser(request);
            String corpId = CommonUtils.getUserCorpId(authUser);
            StringBuilder builder = new StringBuilder();
            if (StringUtils.equals(type, "quarter")) {//季度日均
                builder.append("select sum(total)/count(total) total, quarter countTime from (");
                builder.append("select COUNT(distinct VEHICLE_ID) total , QUARTER(__time) quarter, TIME_FORMAT(__time, 'yyyy-MM-dd') moment from ");
                builder.append(orderTable);
                builder.append(" where TIME_FORMAT(__time, 'yyyy') = '").append(startTime.substring(0, 4)).append("' ");
                if (StringUtils.isNotBlank(corpId)) {
                    builder.append(" and CORP_ID = '");
                    builder.append(corpId).append("' ");
                }
                builder.append(" group by  QUARTER(__time), TIME_FORMAT(__time, 'yyyy-MM-dd')");
                builder.append(") group by quarter");
            } else {
                if (StringUtils.equals(type, "month")) {//月日均
                    builder.append("select sum(total)/count(total) total, SUBSTRING(moment, 1, 7) countTime from (");
                } else {//年日均
                    builder.append("select sum(total)/count(total) total, SUBSTRING(moment, 1, 4) countTime from (");
                }
                builder.append(" select count(distinct VEHICLE_ID) total, TIME_FORMAT(__time, 'yyyy-MM-dd') moment from ");
                builder.append(orderTable);
                builder.append(" where 1 = 1 ");
                if (StringUtils.equals(type, "month")) {//月日均
                    builder.append(" and TIME_FORMAT(__time, 'yyyy') = '").append(startTime.substring(0, 4)).append("' ");
                }
                if (StringUtils.isNotBlank(corpId)) {
                    builder.append(" and CORP_ID = '");
                    builder.append(corpId).append("' ");
                }
                builder.append(" group by TIME_FORMAT(__time, 'yyyy-MM-dd')");
                if (StringUtils.equals(type, "month")) {//月日均
                    builder.append(" ) group by SUBSTRING(moment, 1, 7)");
                } else {
                    builder.append(" ) group by SUBSTRING(moment, 1, 4)");
                }
            }
            log.info("vehicleEfficiency SQL : " + builder.toString());
            resp.setData(httpUtils.doPostSqlUrl("sql", builder.toString()));
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询车辆效率统计成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatusCode("-50");
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("查询车辆效率统计异常");
        }
        return resp;
    }

    @GetMapping("/timeSlotEfficiency")
    @ApiOperation(value = "时段效率统计", notes = "时段效率统计")
    public MessageResp timeSlotEfficiency(HttpServletRequest request, String startTime, String endTime, String type) {
        MessageResp resp = new MessageResp();
        try {
            //非管理员用户只能够查看自己企业
            AuthUserResp authUser = sessionHandler.getUser(request);
            String corpId = CommonUtils.getUserCorpId(authUser);
            StringBuilder builder = new StringBuilder();
            if (StringUtils.equals(type, "company")) {
                builder.append("select count(distinct VEHICLE_ID) total, TIME_FORMAT(__time,'yyyy-MM-dd') moment, CORP_NAME name  from ");
            } else {
                builder.append("select count(distinct VEHICLE_ID) total, TIME_FORMAT(__time,'yyyy-MM-dd') moment from ");
            }
            builder.append(orderTable);
            builder.append(" where __time > '").append(startTime).append("' AND ");
            builder.append(" TIME_FORMAT(__time,'yyyy-MM-dd') <= '").append(endTime).append("' ");
            if (StringUtils.isNotBlank(corpId)) {
                builder.append("  and CORP_ID = '");
                builder.append(corpId).append("' ");
            }
            builder.append(" group by TIME_FORMAT(__time, 'yyyy-MM-dd')");
            if (StringUtils.equals(type, "company")) {
                builder.append(" , CORP_NAME");
            }
            log.info("timeSlotEfficiency SQL : " + builder.toString());
            JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", builder.toString());
            if (StringUtils.equals(type, "day")) {
                resp.setData(jsonArray);
            } else if (StringUtils.equals(type, "company")) {
                List<Map> result = new ArrayList<>();
                if (jsonArray != null && jsonArray.size() > 0) {
                    Map<String, List<Map<String, Object>>> objMap = new HashMap<>();
                    jsonArray.forEach(obj -> {
                        Map<String, Object> map = (Map<String, Object>) obj;
                        String corpName = map.get("name").toString();
                        if (objMap.containsKey(corpName)) {
                            objMap.get(corpName).add(map);
                        } else {
                            List<Map<String, Object>> list = new ArrayList();
                            list.add(map);
                            objMap.put(corpName, list);
                        }
                    });
                    for (String key : objMap.keySet()) {
                        Map<String, Object> dataMap = new HashMap<>();
                        dataMap.put("corpName", key);
                        dataMap.put("data", objMap.get(key));
                        result.add(dataMap);
                    }
                }
                resp.setData(result);
            } else {
                JSONArray totalArray = httpUtils.doPostSqlUrl("sql", "select count(distinct VEHICLE_ID) total from " + orderTable);
                Integer totalCars;
                List<Map<String, Object>> result = new ArrayList<>();
                if (totalArray != null && totalArray.size() > 0) {
                    totalCars = Integer.parseInt(String.valueOf(((Map<String, Object>) totalArray.get(0)).get("total")));
                    if (totalCars == 0) {
                        resp.setStatusCode("00");
                        resp.setResultDesc("查询时段效率统计成功");
                        resp.setResult(Boolean.TRUE.toString());
                        return resp;
                    }
                    jsonArray.forEach(obj -> {
                        Map<String, Object> map = new HashMap<>();
                        Map<String, Object> objMap = (Map<String, Object>) obj;
                        Double efficiency = Double.parseDouble(objMap.get("total").toString()) / totalCars;
                        map.put("moment", objMap.get("moment").toString());
                        map.put("efficiency", efficiency);
                        result.add(map);
                    });
                }
                resp.setData(result);
            }
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询时段效率统计成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatusCode("-50");
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("查询时段效率统计异常");
        }
        return resp;
    }


    @GetMapping("/mileageEfficiencyCount")
    @ApiOperation(value = "里程效率统计", notes = "01-白班，02-晚班")
    public MessageResp mileageEfficiencyCount(HttpServletRequest request, String workType, String startTime, String timeType, String type) {
        MessageResp resp = new MessageResp();
        try {
            //非管理员用户只能够查看自己企业
            AuthUserResp authUser = sessionHandler.getUser(request);
            String corpId = CommonUtils.getUserCorpId(authUser);
            StringBuilder builder = new StringBuilder();
            if (StringUtils.equals(type, "efficiency")) {
                builder.append("select TRUNCATE(sum(EFF_MILEAGE)/(sum(EFF_MILEAGE) + sum(EMPTY_MILEAGE)), 4) efficiency,");
            } else {
                builder.append("select TRUNCATE(sum(EFF_MILEAGE), 2) serviceMile, TRUNCATE(sum(EMPTY_MILEAGE), 2) emptyMile, TRUNCATE((sum(EFF_MILEAGE) + sum(EMPTY_MILEAGE)), 2) totalMile,");
            }
            this.mileageServiceNumberSql(corpId, workType, startTime, timeType, builder);
            String sql = builder.toString();
            log.info("mileageEfficiency SQL: " + sql);
            resp.setData(httpUtils.doPostSqlUrl("sql", sql));
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询里程数统计成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatusCode("-50");
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("查询里程数统计异常");
        }
        return resp;
    }

    @GetMapping("/serviceNumberEfficiency")
    @ApiOperation(value = "营运次数效率统计", notes = "01-白班，02-晚班")
    public MessageResp serviceNumberEfficiency(HttpServletRequest request, String workType, String startTime, String timeType, String type) {
        MessageResp resp = new MessageResp();
        try {
            //非管理员用户只能够查看自己企业
            AuthUserResp authUser = sessionHandler.getUser(request);
            String corpId = CommonUtils.getUserCorpId(authUser);
            StringBuilder builder = new StringBuilder();
            if (StringUtils.equals(type, "order")) {//营运次数平均
                if (StringUtils.equals(timeType, "month")) {
                    builder.append(" select count(FEE) total, ");
                    builder.append(" TIME_FORMAT(__time, 'yyyy-MM-dd') moment from ");
                    builder.append(orderTable);
                    builder.append(" where TIME_FORMAT(__time,'yyyy-MM') = '");
                    builder.append(startTime.substring(0, 7)).append("' ");
                    if (StringUtils.isNotBlank(workType)) {
                        builder.append(" AND WORK_TYPE = '");
                        builder.append(workType).append("' ");
                    }
                    if (StringUtils.isNotBlank(corpId)) {
                        builder.append("  and CORP_ID = '");
                        builder.append(corpId).append("' ");
                    }
                    builder.append(" group by TIME_FORMAT(__time, 'yyyy-MM-dd')");
                } else if (StringUtils.equals(timeType, "day")) {//按日24小时
                    builder.append("select count(FEE) total,");
                    this.mileageServiceNumberSql(corpId, workType, startTime, timeType, builder);
                } else {
                    builder.append("select sum(total)/count(days) total, months moment from (");
                    builder.append(" select count(FEE) total, TIME_FORMAT(__time, 'yyyy-MM') months, TIME_FORMAT(__time, 'yyyy-MM-dd') days from ");
                    builder.append(orderTable);
                    builder.append("  where TIME_FORMAT(__time, 'yyyy') = '");
                    builder.append(startTime.substring(0, 4)).append("' ");
                    if (StringUtils.isNotBlank(workType)) {
                        builder.append(" AND WORK_TYPE = '");
                        builder.append(workType).append("' ");
                    }
                    if (StringUtils.isNotBlank(corpId)) {
                        builder.append("  and   CORP_ID = '");
                        builder.append(corpId).append("' ");
                    }
                    builder.append(" group by TIME_FORMAT(__time, 'yyyy-MM'), TIME_FORMAT(__time, 'yyyy-MM-dd')");
                    builder.append(" ) group by months");
                }
            } else if (StringUtils.equals(type, "shift")) {//班次平均
                if (StringUtils.equals(timeType, "month")) {
                    builder.append(" select count(FEE)/count(distinct WORK_TYPE) total, TIME_FORMAT(__time, 'yyyy-MM-dd') moment from ");
                    builder.append(orderTable);
                    builder.append(" where TIME_FORMAT(__time, 'yyyy-MM') = '");
                    builder.append(startTime.substring(0, 7)).append("' ");
                    if (StringUtils.isNotBlank(workType)) {
                        builder.append(" AND  WORK_TYPE = '");
                        builder.append(workType).append("' ");
                    }
                    if (StringUtils.isNotBlank(corpId)) {
                        builder.append("  and CORP_ID = '");
                        builder.append(corpId).append("' ");
                    }
                    builder.append(" group by TIME_FORMAT(__time, 'yyyy-MM-dd')");
                } else if (StringUtils.equals(timeType, "day")) {//按日24小时
                    builder.append("select count(FEE)/count(distinct WORK_TYPE) total, EXTRACT(HOUR FROM __time) moment from ");
                    builder.append(orderTable);
                    builder.append(" where TIME_FORMAT(__time, 'yyyy-MM-dd') = '");
                    builder.append(startTime.substring(0, 10)).append("' ");
                    if (StringUtils.isNotBlank(workType)) {
                        builder.append(" AND  WORK_TYPE = '");
                        builder.append(workType).append("'  ");
                    }
                    if (StringUtils.isNotBlank(corpId)) {
                        builder.append("  and CORP_ID = '");
                        builder.append(corpId).append("' ");
                    }
                    builder.append(" group by EXTRACT(HOUR FROM __time)");
                } else {
                    builder.append("select sum(total)/count(workType) total, months moment from  (");
                    builder.append(" select count(FEE) total, WORK_TYPE workType, ");
                    builder.append("TIME_FORMAT(__time, 'yyyy-MM-dd') days, TIME_FORMAT(__time, 'yyyy-MM') months from ");
                    builder.append(orderTable);
                    builder.append(" where TIME_FORMAT(__time, 'yyyy') = '");
                    builder.append(startTime.substring(0, 4)).append("' ");
                    if (StringUtils.isNotBlank(workType)) {
                        builder.append(" AND WORK_TYPE = '");
                        builder.append(workType).append("'  ");
                    }
                    if (StringUtils.isNotBlank(corpId)) {
                        builder.append("  and CORP_ID = '");
                        builder.append(corpId).append("' ");
                    }
                    builder.append(" group by TIME_FORMAT(__time, 'yyyy-MM'), TIME_FORMAT(__time, 'yyyy-MM-dd'), WORK_TYPE)");
                    builder.append(" group by months");
                }
            } else {//次数趋势
                builder.append("select count(FEE) total,");
                this.mileageServiceNumberSql(corpId, workType, startTime, timeType, builder);
            }
            String sql = builder.toString();
            log.info("serviceNumberEfficiency SQL: " + sql);
            resp.setData(httpUtils.doPostSqlUrl("sql", sql));
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询营运次数效率成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatusCode("-50");
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("查询营运次数效率异常");
        }
        return resp;
    }

    private void mileageServiceNumberSql(String corpId, String workType, String startTime, String timeType, StringBuilder builder) {
        if (StringUtils.equals(timeType, "month")) {//按月统计
            builder.append(" TIME_FORMAT(__time,'yyyy-MM-dd') moment from ");
            builder.append(orderTable);
            builder.append(" where TIME_FORMAT(__time,'yyyy-MM') = '");
            builder.append(startTime.substring(0, 7)).append("'");
            if (StringUtils.isNotBlank(workType)) {
                builder.append("  AND WORK_TYPE = '");
                builder.append(workType).append("' ");
            }
            if (StringUtils.isNotBlank(corpId)) {
                builder.append("  and CORP_ID = '");
                builder.append(corpId).append("' ");
            }
            builder.append(" group by TIME_FORMAT(__time,'yyyy-MM-dd')");
        } else if (StringUtils.equals(timeType, "day")) {//按日统计
            builder.append(" EXTRACT(HOUR FROM __time) moment from ");
            builder.append(orderTable);
            builder.append(" where TIME_FORMAT(__time,'yyyy-MM-dd') = '");
            builder.append(startTime.substring(0, 10)).append("'");
            if (StringUtils.isNotBlank(workType)) {
                builder.append(" AND WORK_TYPE = '");
                builder.append(workType).append("' ");
            }
            if (StringUtils.isNotBlank(corpId)) {
                builder.append("  and  CORP_ID = '");
                builder.append(corpId).append("' ");
            }
            builder.append(" group by EXTRACT(HOUR FROM __time)");
        } else {//按年统计
            builder.append("TIME_FORMAT(__time,'yyyy-MM') moment from ");
            builder.append(orderTable);
            builder.append(" where TIME_FORMAT(__time,'yyyy') = '");
            builder.append(startTime.substring(0, 4)).append("'");
            if (StringUtils.isNotBlank(workType)) {
                builder.append(" AND WORK_TYPE = '");
                builder.append(workType).append("' ");
            }
            if (StringUtils.isNotBlank(corpId)) {
                builder.append("  and CORP_ID  = '");
                builder.append(corpId).append("' ");
            }
            builder.append(" group by TIME_FORMAT(__time,'yyyy-MM')");
        }
    }

    protected String onlineDriver() {
        String result = "0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -5);// 3分钟之前的时间
        Date beforeD = beforeTime.getTime();
        String time = sdf.format(beforeD);

        String today = time.split(" ")[0];
        StringBuilder sql = new StringBuilder();

        sql.append(" select count(DRIVER_ID) as onlineDriver from ( ");
        sql.append(" select distinct DRIVER_ID,max(__time) from ").append(checkInTable).append(" where 1=1 ");
//        sql.append(" and __time<= '").append(time).append("'");
        sql.append(" and TIME_FORMAT(__time,'yyyy-MM-dd') = '").append(today).append("'");
        sql.append(" group by DRIVER_ID )");

//        sql.append("select count(driver_id) as onlineDriver from taxi_check_in");
//        sql.append(" where 1=1 ");
//        sql.append(" and __time<= '2018-07-01T10:48:41Z' and TIME_FORMAT(__time,'yyyy-MM-dd') = '2018-07-01'");
//        sql.append(" and __time<= '").append(time).append("'");
//        sql.append(" and TIME_FORMAT(__time,'yyyy-MM-dd') = '").append(today).append("'");
        JSONArray res = null;
        try {
            res = httpUtils.doPostSqlUrl("sql", sql.toString());
            if (res != null && res.size() > 0) {
                JSONObject obj = res.getJSONObject(0);
                result = obj.get("onlineDriver").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
