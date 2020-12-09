package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.*;
import com.zhcx.authorization.utils.excel.ExcelUtils;
import com.zhcx.basicdata.facade.taxi.TaxiOrderReportService;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoCompany;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.zhcx.authorization.utils.excel.ExcelUtils.setResponseHeader;

@RestController
@RequestMapping("/taxi/order")
@Api(value = "order", tags = "出租车--运营订单统计controller")
public class TaxiOrderController {

    private Logger log = LoggerFactory.getLogger(TaxiOrderController.class);

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Autowired
    private TaxiOrderReportService orderReportService;

    @Value("${druid.order.table}")
    private String orderTable;

    @Value("${morning.peak.start}")
    private String morningStart;

    @Value("${morning.peak.end}")
    private String morningEnd;

    @Value("${evening.peak.start}")
    private String eveningStart;

    @Value("${evening.peak.end}")
    private String eveningEnd;


    @GetMapping("/singleVehicleIncomeCount")
    @ApiOperation(value = "营运收入--单车月收入统计", notes = "营运收入--单车月收入统计")
    public MessageResp singleVehicleIncomeCount(HttpServletRequest request, String type, String month) {
        MessageResp resp = new MessageResp();
        StringBuilder sqlBuilder = new StringBuilder();
        String sql = null;
        try {
            //非管理员用户只能够查看自己企业
            AuthUserResp authUser = sessionHandler.getUser(request);
//            String corpId = CommonUtils.getUserCorpId(authUser);
            //单车日均收入金额，按天统计一个月
            if (StringUtils.equals(type, "AVD")) {
                //拼接sql
                sqlBuilder.append("SELECT TRUNCATE(SUM(FEE)/COUNT(DISTINCT PLATE_NUM), 2) totalFee, TIME_FORMAT(__time,'yyyy-MM-dd') moment FROM ");
            } else if (StringUtils.equals(type, "AVC")) { //单车次均收入金额，按天统计一个月
                //拼接sql
                sqlBuilder.append("SELECT TRUNCATE(SUM(FEE)/COUNT(FEE), 2) totalFee, TIME_FORMAT(__time,'yyyy-MM-dd') moment FROM ");
            } else if (StringUtils.equals(type, "AVS")) {
                //平均公里产值
                sqlBuilder.append("SELECT TRUNCATE(SUM(FEE)/(SUM(EFF_MILEAGE)+SUM(EMPTY_MILEAGE)), 2) totalFee, TIME_FORMAT(__time,'yyyy-MM-dd') moment FROM ");
            } else {//单车最高收入排名,按月排名
                sqlBuilder.append("SELECT TRUNCATE(SUM(FEE), 2) totalFee, PLATE_NUM plate_num, TIME_FORMAT(__time,'yyyy-MM') moment FROM ");
            }
            sqlBuilder.append(orderTable);
            sqlBuilder.append(" WHERE TIME_FORMAT(__time, 'yyyy-MM') = '");
            sqlBuilder.append(month.substring(0, 7)).append("' ");
//            if (StringUtils.isNotBlank(corpId)) {
//                sqlBuilder.append("  and CORP_ID = '");
//                sqlBuilder.append(corpId).append("' ");
//            }

            if (StringUtils.equals(type, "AVD") || StringUtils.equals(type, "AVC") || StringUtils.equals(type, "AVS")) {
                sqlBuilder.append(" GROUP BY TIME_FORMAT(__time,'yyyy-MM-dd')");
            } else {
                sqlBuilder.append(" GROUP BY PLATE_NUM, TIME_FORMAT(__time,'yyyy-MM')");
                sqlBuilder.append(" order by totalFee desc limit 100");
            }
            sql = sqlBuilder.toString();
            log.info("singleVehicleIncomeCount SQL: " + sql);
            resp.setData(httpUtils.doPostSqlUrl("sql", sql));
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
            resp.setResultDesc("查询成功");
        } catch (
                Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }
        return resp;
    }


    @GetMapping("/dayNightIncomeCount")
    @ApiOperation(value = "营运收入--白晚班收入统计", notes = "01-白班;02-晚班")
    public MessageResp dayNightIncomeCount(HttpServletRequest request, String workType, String startTime, String endTime, String type) {
        MessageResp resp = new MessageResp();
        try {
            //非管理员用户只能够查看自己企业
            AuthUserResp authUser = sessionHandler.getUser(request);
            String corpId = CommonUtils.getUserCorpId(authUser);
            StringBuilder builder = new StringBuilder();
            //最高单车收入
            if (StringUtils.equals(type, "rank")) {
                builder.append("select TRUNCATE(max(fee), 2) fee, moment from ( ");
                builder.append("select sum(FEE) fee, VEHICLE_ID, TIME_FORMAT(__time,'yyyy-MM-dd') moment from ");
            } else if (StringUtils.equals(type, "vehicle")) {
                //车均每天收入金额
                builder.append("select TRUNCATE(sum(FEE)/count(distinct VEHICLE_ID), 2) fee, TIME_FORMAT(__time,'yyyy-MM-dd') moment from ");
            } else if (StringUtils.equals(type, "AVS")) {
                //单车月公里产值
                builder.append("SELECT TRUNCATE(SUM(FEE)/(SUM(EFF_MILEAGE)+SUM(EMPTY_MILEAGE)), 2) fee, TIME_FORMAT(__time,'yyyy-MM-dd') moment FROM ");
            } else {
                //次均收入金额,每次订单收入
                builder.append("select TRUNCATE(sum(FEE)/count(FEE), 2) fee, TIME_FORMAT(__time,'yyyy-MM-dd') moment from ");
            }
            builder.append(orderTable);
            builder.append(" WHERE __time >'");
            builder.append(startTime);
            builder.append("'  AND TIME_FORMAT(__time,'yyyy-MM-dd') <='");
            builder.append(endTime).append("'");
            if (StringUtils.isNotBlank(workType)) {
                builder.append(" AND WORK_TYPE = '");
                builder.append(workType).append("' ");
            }
            if (StringUtils.isNotBlank(corpId)) {
                builder.append("  and CORP_ID = '");
                builder.append(corpId).append("' ");
            }
            builder.append(" group by TIME_FORMAT(__time,'yyyy-MM-dd')");
            if (StringUtils.equals(type, "rank")) {
                builder.append(",VEHICLE_ID ");
                builder.append(" ) group by moment");
            }
            String sql = builder.toString();
            log.info("dayNightIncomeCount SQL: " + sql);
            resp.setData(httpUtils.doPostSqlUrl("sql", sql));
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
            resp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }
        return resp;
    }


    @GetMapping("/peakNormalIncomeCount")
    @ApiOperation(value = "营运收入--高平峰收入统计", notes = "01-白班，02-晚班")
    public MessageResp peakNormalIncomeCount(HttpServletRequest request, String startTime, String endTime, Boolean isHigh, String type) {
        MessageResp resp = new MessageResp();
        try {
            //非管理员用户只能够查看自己企业
            AuthUserResp authUser = sessionHandler.getUser(request);
            String corpId = CommonUtils.getUserCorpId(authUser);
            StringBuilder builder = new StringBuilder();
            if (StringUtils.equals(type, "vehicle")) {//车均
                builder.append("select TRUNCATE(sum(FEE)/count(distinct PLATE_NUM), 2) money, TIME_FORMAT(__time,'yyyy-MM-dd') dayNum from ");
            } else if (StringUtils.equals(type, "rank")) {//排名，当天最高车收入
                builder.append("select TRUNCATE(max(total), 2) money, dayNum from ( ");
                builder.append("select sum(FEE) total, PLATE_NUM, TIME_FORMAT(__time,'yyyy-MM-dd') dayNum  from ");
            } else if (StringUtils.equals(type, "AVS")) {
                //平均公里产值
                builder.append("select TRUNCATE(SUM(FEE)/(SUM(EFF_MILEAGE)+SUM(EMPTY_MILEAGE)), 2) money, TIME_FORMAT(__time,'yyyy-MM-dd') dayNum from  ");
            } else {//次均

                builder.append("select TRUNCATE(sum(FEE)/count(FEE), 2) money, TIME_FORMAT(__time,'yyyy-MM-dd') dayNum from  ");
            }
            builder.append(orderTable);
            builder.append(" where __time > '");
            builder.append(startTime.substring(0, 10));
            builder.append("' and TIME_FORMAT(__time,'yyyy-MM-dd') <= '");
            builder.append(endTime.substring(0, 10)).append("' ");
            if (StringUtils.isNotBlank(corpId)) {
                builder.append("  and CORP_ID = '");
                builder.append(corpId).append("' ");
            }

            if (null != isHigh && isHigh) {//高峰期
                builder.append(" and ((TIME_FORMAT(__time,'HH:mm:ss') >= '");
                builder.append(morningStart);
                builder.append("' and TIME_FORMAT(__time,'HH:mm:ss') <= '");
                builder.append(morningEnd);
                builder.append("') or (TIME_FORMAT(__time,'HH:mm:ss') >= '");
                builder.append(eveningStart);
                builder.append("' and TIME_FORMAT(__time,'HH:mm:ss') <= '");
                builder.append(eveningEnd).append("'))");
            } else {//平峰期
                builder.append(" and ((TIME_FORMAT(__time,'HH:mm:ss') > '00:00:00' and TIME_FORMAT(__time,'HH:mm:ss') < '");
                builder.append(morningStart);
                builder.append("') or (TIME_FORMAT(__time,'HH:mm:ss') > '");
                builder.append(morningEnd);
                builder.append("' and  TIME_FORMAT(__time,'HH:mm:ss') < '");
                builder.append(eveningStart);
                builder.append("') or (TIME_FORMAT(__time,'HH:mm:ss') > '");
                builder.append(eveningEnd);
                builder.append("' and TIME_FORMAT(__time,'HH:mm:ss') < '23:59:59'))");
            }
            if (StringUtils.equals(type, "rank")) {//排名
                builder.append(" group by TIME_FORMAT(__time,'yyyy-MM-dd'), PLATE_NUM ) ");
                builder.append(" group by dayNum");
            } else {
                builder.append(" group by TIME_FORMAT(__time,'yyyy-MM-dd')");
            }
            String sql = builder.toString();
            log.info("peakNormalIncomeCount SQL: " + sql);
            resp.setData(httpUtils.doPostSqlUrl("sql", sql));
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
            resp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }
        return resp;
    }


    @GetMapping("/fuelAdditionProportionCount")
    @ApiOperation(value = "营运收入--燃油附加费占比统计", notes = "营运收入--燃油附加费占比统计")
    public MessageResp fuelAdditionProportionCount(HttpServletRequest request, String startTime, String timeType) {
        MessageResp resp = new MessageResp();
        try {
            //非管理员用户只能够查看自己企业
            AuthUserResp authUser = sessionHandler.getUser(request);
            String corpId = CommonUtils.getUserCorpId(authUser);
            StringBuilder builder = new StringBuilder();
            builder.append("select  TRUNCATE(sum(ADDITION_FEE)/sum(FEE), 4) efficiency, ");
            if (StringUtils.equals(timeType, "year")) {
                builder.append("TIME_FORMAT(__time,'yyyy-MM') moment  from ");
            } else {
                builder.append("TIME_FORMAT(__time,'yyyy-MM-dd') moment  from ");
            }
            builder.append(orderTable);
            if (StringUtils.equals(timeType, "year")) {
                builder.append(" where TIME_FORMAT(__time,'yyyy') = '");
                builder.append(startTime.substring(0, 4)).append("'");
            } else {
                builder.append(" where TIME_FORMAT(__time,'yyyy-MM') = '");
                builder.append(startTime.substring(0, 7)).append("'");
            }
            if (StringUtils.isNotBlank(corpId)) {
                builder.append("  and CORP_ID = '");
                builder.append(corpId).append("' ");
            }
            if (StringUtils.equals(timeType, "year")) {
                builder.append(" group by TIME_FORMAT(__time,'yyyy-MM')");
            } else {
                builder.append(" group by TIME_FORMAT(__time,'yyyy-MM-dd')");
            }
            log.info("fuelAdditionProportionCount SQL: " + builder.toString());
            resp.setData(httpUtils.doPostSqlUrl("sql", builder.toString()));
            resp.setResultDesc("查询成功");
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }
        return resp;
    }


    @GetMapping("/queryDayOrderCount")
    @ApiOperation(value = "首页--分天订单量统计", notes = "首页--分时订单量统计")
    public MessageResp queryDayOrderCount(HttpServletRequest request) {
        MessageResp resp = new MessageResp();
        Map<String, Integer> result;
        String corpId = null;
        try {
            //非管理员用户只能够查看自己企业
            AuthUserResp authUser = sessionHandler.getUser(request);
            if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
                if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                    corpId = String.valueOf(authUser.getCorpId());
                }
            }
            resp.setData(this.getDayOrderResult(null, corpId));
            resp.setResultDesc("查询成功");
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }
        return resp;
    }

/*    private List <Map<String, Object>> getHourOrderResult(String type, String corpId) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.HOUR, 1);
        String current = sdf.format(calendar.getTime());
        //统计12个小时订单
        calendar.add(Calendar.HOUR,  -12);
        String before = sdf.format(calendar.getTime());

        String cityName = Constants.DEAULT_CITY_NAME;

        StringBuilder builder = new StringBuilder();
        if(StringUtils.equals(type, "fee")){//统计营收金额
            builder.append("select TRUNCATE(sum(FEE), 2) total, ");
        }else{
            builder.append("select count(FEE) total, ");
        }
        builder.append("TIME_FORMAT(END_TIME, 'yyyy-MM-dd HH') moment from ");
        builder.append(orderTable);
        builder.append(" where TIME_FORMAT(END_TIME, 'yyyy-MM-dd HH') >= '");
        builder.append(before);
        builder.append("' AND TIME_FORMAT(END_TIME, 'yyyy-MM-dd HH') < '");
        builder.append(current);
        builder.append("' AND CITY_NAME = '");
        builder.append(cityName).append("' ");
        if(StringUtils.isNotBlank(corpId)){
            builder.append("  and CORP_ID = '");
            builder.append(corpId).append("' ");
        }
        builder.append(" group by TIME_FORMAT(END_TIME, 'yyyy-MM-dd HH')");
        String sql = builder.toString();
        log.info("getHourOrderSql SQL: " + sql);

        List <Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> resultMap = DateUtil.getPreHoursFromZero(12);
        JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", sql);
        if(jsonArray != null && jsonArray.size() > 0){
            jsonArray.forEach(obj -> {
                Map<String, Object> map = (Map<String, Object>)obj;
                if(resultMap.containsKey(map.get("moment").toString())){
                    resultMap.put(map.get("moment").toString(), map.get("total").toString());
                }
            });
        }
        for(String key : resultMap.keySet()){
            Map<String, Object> map = new HashMap<>();
            map.put("moment", key);
            map.put("total", resultMap.get(key));
            result.add(map);
        }
        return result;
    }*/

    private List<Map<String, Object>> getDayOrderResult(String type, String corpId) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.HOUR, 0);
        String current = sdf.format(calendar.getTime());
        //统计12个小时订单
        calendar.add(Calendar.DAY_OF_MONTH, -12);
        String before = sdf.format(calendar.getTime());

        StringBuilder builder = new StringBuilder();
        if (StringUtils.equals(type, "fee")) {//统计营收金额
            builder.append("select TRUNCATE(sum(FEE), 2) total, ");
        } else {
            builder.append("select count(FEE) total, ");
        }
        builder.append("TIME_FORMAT(END_TIME, 'yyyy-MM-dd') moment from ");
        builder.append(orderTable);
        builder.append(" where TIME_FORMAT(END_TIME, 'yyyy-MM-dd') >= '");
        builder.append(before);
        builder.append("' AND TIME_FORMAT(END_TIME, 'yyyy-MM-dd') <= '");
        builder.append(current).append("' ");
        if (StringUtils.isNotBlank(corpId)) {
            builder.append("  AND CORP_ID = '");
            builder.append(corpId).append("' ");
        }
        builder.append(" group by TIME_FORMAT(END_TIME, 'yyyy-MM-dd')");
        String sql = builder.toString();
        log.info("getHourOrderSql SQL: " + sql);

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> resultMap = DateUtil.getPreDaysFromZero(12);
        JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", sql);
        if (jsonArray != null && jsonArray.size() > 0) {
            jsonArray.forEach(obj -> {
                Map<String, Object> map = (Map<String, Object>) obj;
                if (resultMap.containsKey(map.get("moment").toString())) {
                    resultMap.put(map.get("moment").toString(), map.get("total").toString());
                }
            });
        }
        for (String key : resultMap.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("moment", key);
            map.put("total", resultMap.get(key));
            result.add(map);
        }
        return result;
    }

    @GetMapping("/queryDayOrderFeeCount")
    @ApiOperation(value = "首页--分天订单营收金额统计", notes = "首页--分天订单营收金额统计")
    public MessageResp queryDayOrderFeeCount(HttpServletRequest request) {
        MessageResp resp = new MessageResp();
        Map<String, Integer> result;
        try {
            //非管理员用户只能够查看自己企业
            AuthUserResp authUser = sessionHandler.getUser(request);
            String corpId = null;
            if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
                if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                    corpId = String.valueOf(authUser.getCorpId());
                }
            }
            resp.setData(this.getDayOrderResult("fee", corpId));
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询成功");
            resp.setStatusCode("00");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }
        return resp;
    }

    @GetMapping("/queryHourOrderCount")
    @ApiOperation(value = "监控中心--分时订单量统计", notes = "监控中心--分时订单量统计")
    public MessageResp queryHourOrderCount(HttpServletRequest request) {
        MessageResp resp = new MessageResp();
        Map<String, Integer> result;
        String corpId = null;
        try {
            //非管理员用户只能够查看自己企业
            AuthUserResp authUser = sessionHandler.getUser(request);
            if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
                if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                    corpId = String.valueOf(authUser.getCorpId());
                }
            }
            resp.setData(this.getHourOrderResult(null, corpId));
            resp.setResultDesc("查询成功");
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }
        return resp;
    }

    private List<Map<String, Object>> getHourOrderResult(String type, String corpId) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.HOUR, 1);
        String current = sdf.format(calendar.getTime());
        //统计12个小时订单
        calendar.add(Calendar.HOUR, -12);
        String before = sdf.format(calendar.getTime());

        StringBuilder builder = new StringBuilder();
        if (StringUtils.equals(type, "fee")) {//统计营收金额
            builder.append("select TRUNCATE(sum(FEE), 2) total, ");
        } else {
            builder.append("select count(FEE) total, ");
        }
        builder.append("TIME_FORMAT(END_TIME, 'yyyy-MM-dd HH') moment from ");
        builder.append(orderTable);
        builder.append(" where TIME_FORMAT(END_TIME, 'yyyy-MM-dd HH') >= '");
        builder.append(before);
        builder.append("' AND TIME_FORMAT(END_TIME, 'yyyy-MM-dd HH') < '");
        builder.append(current).append("' ");
        if (StringUtils.isNotBlank(corpId)) {
            builder.append("  AND CORP_ID = '");
            builder.append(corpId).append("' ");
        }
        builder.append(" group by TIME_FORMAT(END_TIME, 'yyyy-MM-dd HH')");
        String sql = builder.toString();
        log.info("getHourOrderSql SQL: " + sql);

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> resultMap = DateUtil.getPreHoursFromZero(12);
        JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", sql);
        if (jsonArray != null && jsonArray.size() > 0) {
            jsonArray.forEach(obj -> {
                Map<String, Object> map = (Map<String, Object>) obj;
                if (resultMap.containsKey(map.get("moment").toString())) {
                    resultMap.put(map.get("moment").toString(), map.get("total").toString());
                }
            });
        }
        for (String key : resultMap.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("moment", key);
            map.put("total", resultMap.get(key));
            result.add(map);
        }
        return result;
    }

    @GetMapping("/queryHourOrderFeeCount")
    @ApiOperation(value = "监控中心--分时订单营收金额统计", notes = "监控中心--分时订单营收金额统计")
    public MessageResp queryHourOrderFeeCount(HttpServletRequest request) {
        MessageResp resp = new MessageResp();
        Map<String, Integer> result;
        try {
            //非管理员用户只能够查看自己企业
            AuthUserResp authUser = sessionHandler.getUser(request);
            String corpId = null;
            if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
                if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                    corpId = String.valueOf(authUser.getCorpId());
                }
            }
            resp.setData(this.getHourOrderResult("fee", corpId));
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询成功");
            resp.setStatusCode("00");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }
        return resp;
    }


    @GetMapping("/complexIncomeRankCount")
    @ApiOperation(value = "营运收入--综合收入排名统计", notes = "营运收入--综合收入排名统计")
    public MessageResp complexIncomeRankCount(HttpServletRequest request, String timeType, String startTime, String type) {
        MessageResp resp = new MessageResp();
        try {
            //非管理员用户只能够查看自己企业
            AuthUserResp authUser = sessionHandler.getUser(request);
            String corpId = CommonUtils.getUserCorpId(authUser);

            StringBuilder builder = new StringBuilder();
            builder.append("select TRUNCATE(sum(FEE), 2) total, ");
            //企业
            if (StringUtils.equals(type, "corp")) {
                builder.append("CORP_ID, CORP_NAME from ");
            } else if (StringUtils.equals(type, "driver")) {//驾驶员
                builder.append("DRIVER_ID, DRIVER_NAME from ");
            } else {//车辆
                builder.append("VEHICLE_ID, PLATE_NUM from ");
            }
            builder.append(orderTable);
            builder.append(" where 1 = 1 ");
            if (StringUtils.equals(timeType, "day")) { //日报表
                builder.append(" and TIME_FORMAT(__time,'yyyy-MM-dd') = '");
                builder.append(startTime.substring(0, 10)).append("' ");

            } else {//月报表
                builder.append(" and TIME_FORMAT(__time,'yyyy-MM') = '");
                builder.append(startTime.substring(0, 7)).append("' ");
            }

            if (StringUtils.isNotBlank(corpId)) builder.append(" and CORP_ID = '").append(corpId).append("' ");

            if (StringUtils.equals(type, "corp")) { //企业
                builder.append(" and CORP_ID is not null ");
                builder.append(" and CORP_NAME is not null ");
                builder.append(" group by CORP_ID, CORP_NAME ");
            } else if (StringUtils.equals(type, "driver")) {//驾驶员
                builder.append(" and DRIVER_ID is not null ");
                builder.append(" and DRIVER_NAME is not null ");
                builder.append(" group by DRIVER_ID, DRIVER_NAME ");
            } else {//车辆
                builder.append(" and VEHICLE_ID is not null ");
                builder.append(" and PLATE_NUM is not null ");
                builder.append(" group by VEHICLE_ID, PLATE_NUM ");
            }
            builder.append(" order by total desc limit 10 ");

            String sql = builder.toString();
            log.info("complexIncomeRankCount SQL: " + sql);
            resp.setData(httpUtils.doPostSqlUrl("sql", sql));
            resp.setResultDesc("查询成功");
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }
        return resp;
    }

    @GetMapping("/complexIncomeRankCountExport")
    @ApiOperation(value = "营运收入--综合收入排名统计", notes = "营运收入--综合收入排名统计")
    public void complexIncomeRankCountExport(HttpServletRequest request, HttpServletResponse response, String timeType, String startTime) throws Exception {

        JSONArray corpJsonArray = new JSONArray();
        JSONArray driverJsonArray = new JSONArray();
        JSONArray vehicleJsonArray = new JSONArray();
        String corpSpreadhead = startTime + " 企业收入排名统计";
        String driverSpreadhead = startTime + " 驾驶员收入排名统计";
        String vehSpreadhead = startTime + " 车辆收入排名统计";

        //非管理员用户只能够查看自己企业
        AuthUserResp authUser = sessionHandler.getUser(request);
        String corpId = CommonUtils.getUserCorpId(authUser);

        if (StringUtils.equals(timeType, "day")) { //日报表
            StringBuilder builder = new StringBuilder();
            builder.append("select TRUNCATE(sum(FEE), 2) total, ");
            builder.append("CORP_ID, CORP_NAME from ");
            builder.append(orderTable);
            builder.append(" where 1 = 1 ");
            builder.append(" and TIME_FORMAT(__time,'yyyy-MM-dd') = '");
            builder.append(startTime.substring(0, 10)).append("' ");
            if (StringUtils.isNotBlank(corpId)) {
                builder.append("  and CORP_ID = '");
                builder.append(corpId).append("' ");
            }
            builder.append(" and CORP_ID is not null ");
            builder.append(" and CORP_NAME is not null ");
            builder.append(" group by CORP_ID, CORP_NAME");
            builder.append(" order by total desc");
            String sql = builder.toString();
            corpJsonArray = httpUtils.doPostSqlUrl("sql", sql);

            builder = new StringBuilder();
            builder.append("select TRUNCATE(sum(FEE), 2) total, ");
            builder.append("DRIVER_ID, DRIVER_NAME from ");
            builder.append(orderTable);
            builder.append(" where 1 = 1 ");
            builder.append(" and TIME_FORMAT(__time,'yyyy-MM-dd') = '");
            builder.append(startTime.substring(0, 10)).append("' ");
            if (StringUtils.isNotBlank(corpId)) {
                builder.append("  and CORP_ID = '");
                builder.append(corpId).append("' ");
            }
            builder.append(" and DRIVER_ID is not null ");
            builder.append(" and DRIVER_NAME is not null ");
            builder.append(" group by DRIVER_ID, DRIVER_NAME");
            builder.append(" order by total desc");
            sql = builder.toString();
            driverJsonArray = httpUtils.doPostSqlUrl("sql", sql);

            builder = new StringBuilder();
            builder.append("select TRUNCATE(sum(FEE), 2) total, ");
            builder.append("VEHICLE_ID, PLATE_NUM from ");
            builder.append(orderTable);
            builder.append(" where 1 = 1 ");
            builder.append(" and TIME_FORMAT(__time,'yyyy-MM-dd') = '");
            builder.append(startTime.substring(0, 10)).append("' ");
            if (StringUtils.isNotBlank(corpId)) {
                builder.append("  and CORP_ID = '");
                builder.append(corpId).append("' ");
            }
            builder.append(" and VEHICLE_ID is not null ");
            builder.append(" and PLATE_NUM is not null ");
            builder.append(" group by VEHICLE_ID, PLATE_NUM");
            builder.append(" order by total desc");
            sql = builder.toString();
            vehicleJsonArray = httpUtils.doPostSqlUrl("sql", sql);

        } else {//月报表

            StringBuilder builder = new StringBuilder();
            builder.append("select TRUNCATE(sum(FEE), 2) total, ");
            builder.append("CORP_ID, CORP_NAME from ");
            builder.append(orderTable);
            builder.append(" where 1 = 1 ");
            builder.append(" and TIME_FORMAT(__time,'yyyy-MM') = '");
            builder.append(startTime.substring(0, 7)).append("' ");
            if (StringUtils.isNotBlank(corpId)) {
                builder.append("  and CORP_ID = '");
                builder.append(corpId).append("' ");
            }
            builder.append(" and CORP_ID is not null ");
            builder.append(" and CORP_NAME is not null ");
            builder.append(" group by CORP_ID, CORP_NAME");
            builder.append(" order by total desc");
            String sql = builder.toString();
            corpJsonArray = httpUtils.doPostSqlUrl("sql", sql);

            builder = new StringBuilder();
            builder.append("select TRUNCATE(sum(FEE), 2) total, ");
            builder.append("DRIVER_ID, DRIVER_NAME from ");
            builder.append(orderTable);
            builder.append(" where 1 = 1 ");
            builder.append(" and TIME_FORMAT(__time,'yyyy-MM') = '");
            builder.append(startTime.substring(0, 7)).append("' ");
            if (StringUtils.isNotBlank(corpId)) {
                builder.append("  and CORP_ID = '");
                builder.append(corpId).append("' ");
            }
            builder.append(" and DRIVER_ID is not null ");
            builder.append(" and DRIVER_NAME is not null ");
            builder.append(" group by DRIVER_ID, DRIVER_NAME");
            builder.append(" order by total desc");
            sql = builder.toString();
            driverJsonArray = httpUtils.doPostSqlUrl("sql", sql);

            builder = new StringBuilder();
            builder.append("select TRUNCATE(sum(FEE), 2) total, ");
            builder.append("VEHICLE_ID, PLATE_NUM from ");
            builder.append(orderTable);
            builder.append(" where 1 = 1 ");
            builder.append(" and TIME_FORMAT(__time,'yyyy-MM') = '");
            builder.append(startTime.substring(0, 7)).append("' ");
            if (StringUtils.isNotBlank(corpId)) {
                builder.append("  and CORP_ID = '");
                builder.append(corpId).append("' ");
            }
            builder.append(" and VEHICLE_ID is not null ");
            builder.append(" and PLATE_NUM is not null ");
            builder.append(" group by VEHICLE_ID, PLATE_NUM");
            builder.append(" order by total desc");
            sql = builder.toString();
            vehicleJsonArray = httpUtils.doPostSqlUrl("sql", sql);

        }
        String ymdhmsFormat = DateUtil.getYMDHMSFormat(new Date());
        //excel文件名
        String fileName = "综合收入排名统计 " + ymdhmsFormat + ".xls";

        String[][] content = new String[corpJsonArray.size()][];
        //excel标题
        String[] title1 = {"排名", "企业名称", "收入/元"};
        for (int i = 0; i < corpJsonArray.size(); i++) {
            content[i] = new String[title1.length];
            JSONObject jsonObject = (JSONObject) corpJsonArray.get(i);
            content[i][0] = String.valueOf(i + 1);
            content[i][1] = jsonObject.getString("CORP_NAME");
            content[i][2] = String.valueOf(jsonObject.get("total"));
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook2(corpSpreadhead, "企业排名", title1, content, null);


        content = new String[vehicleJsonArray.size()][];
        //excel标题
        String[] title2 = {"排名", "车牌号", "收入/元"};

        for (int i = 0; i < vehicleJsonArray.size(); i++) {
            content[i] = new String[title2.length];
            JSONObject jsonObject = (JSONObject) vehicleJsonArray.get(i);
            content[i][0] = String.valueOf(i + 1);
            content[i][1] = jsonObject.getString("PLATE_NUM");
            content[i][2] = String.valueOf(jsonObject.get("total"));
        }
        wb = ExcelUtils.getHSSFWorkbook2(vehSpreadhead, "车辆排名", title2, content, wb);


        content = new String[driverJsonArray.size()][];
        //excel标题
        String[] title3 = {"排名", "驾驶员", "收入/元"};

        for (int i = 0; i < driverJsonArray.size(); i++) {
            content[i] = new String[title3.length];
            JSONObject jsonObject = (JSONObject) driverJsonArray.get(i);
            content[i][0] = String.valueOf(i + 1);
            content[i][1] = jsonObject.getString("DRIVER_NAME");
            content[i][2] = String.valueOf(jsonObject.get("total"));
        }
        wb = ExcelUtils.getHSSFWorkbook2(driverSpreadhead, "驾驶员排名", title3, content, wb);

        //响应到客户端
        try {
            setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 行业营收统计
     */
    @GetMapping("/getOrderReport")
    public MessageResp getOrderReport(@ModelAttribute TaxiBaseInfoCompany vehicle) {
        MessageResp resp = new MessageResp();
        PageInfo<JSONObject> pageInfo = null;
        try {
            pageInfo = orderReportService.getOrderReport(vehicle);
            resp.setData(pageInfo.getList());
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");

        }
        return resp;
    }

    @GetMapping("/getOrderStatistics")
    public MessageResp getOrderReport(String startTime, String uuid, String isYear) {
        MessageResp resp = new MessageResp();
        try {
            Map<String, Object> param = new HashMap<>();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(uuid)) param.put("corpId", uuid);
            param.put("date", startTime);
            param.put("isYear", isYear);
            Map<String, Object> queryRes = orderReportService.getOrderStatistics(param);
            resp.setData(queryRes);
            resp.setResult(Boolean.TRUE.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");

        }
        return resp;
    }
}
