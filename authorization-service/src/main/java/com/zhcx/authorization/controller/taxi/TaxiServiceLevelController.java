package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.DateUtil;
import com.zhcx.authorization.utils.HttpUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoCompanyService;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoDriverService;
import com.zhcx.basicdata.facade.taxi.TaxiCheckInOutService;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoCompany;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoDriver;
import com.zhcx.basicdata.pojo.taxi.TaxiWorkingTime;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @description: 报表统计-服务质量
 * @author: qzq
 * @date 2020-06-11 10:56
 **/
@RequestMapping("/taxi/serviceLevel")
@RestController
@Api(value = "serviceLevel", tags = "出租车服务质量")
public class TaxiServiceLevelController {

    private Logger log = LoggerFactory.getLogger(TaxiServiceLevelController.class);


    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private TaxiBaseInfoCompanyService companyService;

    @Autowired
    private TaxiBaseInfoDriverService driverService;

    @Autowired
    private TaxiCheckInOutService taxiCheckInOutService;

    @Value("${druid.order.table}")
    private String orderTable;


    /**
     * 综合服务质量统计 指定年按月统计
     * year : yyyy
     * type : 1 满意度 2 投诉率
     */
    @GetMapping("/synthesis/findByYear")
    public MessageResp synthesisFindByYear(@RequestParam(value = "year") String year,
                                           @RequestParam(value = "type") Integer type) {
        MessageResp resp = new MessageResp();
        List<JSONObject> result = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT tmp.mth, SUM( CASE '0' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) AS no_eval, ");
            sqlBuilder.append("SUM( CASE '1' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) AS satisfying, ");
            sqlBuilder.append("SUM( CASE '2' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) AS common,");
            sqlBuilder.append("SUM( CASE '3' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) AS yawp, ");
            sqlBuilder.append("SUM( CASE '4' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) AS complain FROM ");
            sqlBuilder.append("( SELECT TIME_FORMAT(__time, 'MM') as mth, EVALUATE_ID, EVALUATE, count( EVALUATE_ID ) AS evaluate_count FROM ");
            sqlBuilder.append(orderTable);
            sqlBuilder.append(" WHERE TIME_FORMAT(__time, 'yyyy') = '");
            sqlBuilder.append(year).append("' ");
            sqlBuilder.append("GROUP BY TIME_FORMAT(__time, 'MM'), EVALUATE_ID, EVALUATE ) tmp ");
            sqlBuilder.append("GROUP BY tmp.mth");
            String sql = sqlBuilder.toString();
            log.info("synthesisFindByYear SQL: " + sql);

            JSONObject satisfactionObj = null;
            JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", sql);

            double score = 0;
            Map<Integer, Double> resMap = new LinkedHashMap<>();
            for (Object obj : jsonArray) {
                satisfactionObj = (JSONObject) obj;
                int mth = Integer.valueOf(satisfactionObj.get("mth").toString());
                double no_eval = Double.parseDouble(satisfactionObj.get("no_eval").toString());
                double satisfying = Double.parseDouble(satisfactionObj.get("satisfying").toString());
                double common = Double.parseDouble(satisfactionObj.get("common").toString());
                double yawp = Double.parseDouble(satisfactionObj.get("yawp").toString());
                double complain = Double.parseDouble(satisfactionObj.get("complain").toString());

                if (type == 1) score = satisfying / (no_eval + common + yawp + complain + satisfying);//满意度
                if (type == 2) score = complain / (no_eval + common + yawp + complain + satisfying);//投诉率
                resMap.put(mth, score);
            }

            DecimalFormat df = new DecimalFormat("0.00");
            for (int i = 1; i <= 12; i++) {
                jsonObject = new JSONObject();
                if (resMap.containsKey(i)) {
                    if (i >= 10) {
                        jsonObject.put("time", year + "-" + i);
                        jsonObject.put("count", df.format(resMap.get(i)));
                    } else {
                        jsonObject.put("time", year + "-0" + i);
                        jsonObject.put("count", df.format(resMap.get(i)));
                    }
                } else {
                    if (i >= 10) {
                        jsonObject.put("time", year + "-" + i);
                        jsonObject.put("count", 0);
                    } else {
                        jsonObject.put("time", year + "-0" + i);
                        jsonObject.put("count", 0);
                    }
                }
                result.add(jsonObject);
            }


            resp.setData(result);
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
            resp.setResultDesc("查询成功");

        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }

        return resp;
    }

    /**
     * 综合服务质量统计  按日统计
     * date : yyyy-MM
     * type : 1 满意度 2 投诉率
     */
    @GetMapping("/synthesis/findByDate")
    public MessageResp synthesisFindByDate(@RequestParam(value = "date") String date,
                                           @RequestParam(value = "type") Integer type) {
        MessageResp resp = new MessageResp();
        List<JSONObject> result = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT tmp.dd, SUM( CASE '0' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) AS no_eval, ");
            sqlBuilder.append("SUM( CASE '1' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) AS satisfying, ");
            sqlBuilder.append("SUM( CASE '2' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) AS common,");
            sqlBuilder.append("SUM( CASE '3' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) AS yawp, ");
            sqlBuilder.append("SUM( CASE '4' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) AS complain FROM ");
            sqlBuilder.append("( SELECT TIME_FORMAT(__time, 'dd') as dd, EVALUATE_ID, EVALUATE, count( EVALUATE_ID ) AS evaluate_count FROM ");
            sqlBuilder.append(orderTable);
            sqlBuilder.append(" WHERE TIME_FORMAT(__time, 'yyyy-MM') = '");
            sqlBuilder.append(date).append("' ");
            sqlBuilder.append("GROUP BY TIME_FORMAT(__time, 'dd'), EVALUATE_ID, EVALUATE ) tmp ");
            sqlBuilder.append("GROUP BY tmp.dd");
            String sql = sqlBuilder.toString();
            log.info("synthesisFindByDate SQL: " + sql);

            JSONObject satisfactionObj = null;
            JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", sql);
            Map<Integer, Double> resMap = new LinkedHashMap<>();
            int dayCount = DateUtil.getDayOfMonth(date);
            double score = 0;
            for (Object obj : jsonArray) {
                satisfactionObj = (JSONObject) obj;
                int dd = Integer.valueOf(satisfactionObj.get("dd").toString());
                double no_eval = Double.parseDouble(satisfactionObj.get("no_eval").toString());
                double satisfying = Double.parseDouble(satisfactionObj.get("satisfying").toString());
                double common = Double.parseDouble(satisfactionObj.get("common").toString());
                double yawp = Double.parseDouble(satisfactionObj.get("yawp").toString());
                double complain = Double.parseDouble(satisfactionObj.get("complain").toString());

                if (type == 1) score = satisfying / (no_eval + common + yawp + complain + satisfying);//满意度
                if (type == 2) score = complain / (no_eval + common + yawp + complain + satisfying);//投诉率
                resMap.put(dd, score);

            }
            DecimalFormat df = new DecimalFormat("0.00");
            for (int i = 1; i <= dayCount; i++) {
                jsonObject = new JSONObject();
                if (resMap.containsKey(i)) {
                    if (i >= 10) {
                        jsonObject.put("time", date + "-" + i);
                        jsonObject.put("count", df.format(resMap.get(i)));
                    } else {
                        jsonObject.put("time", date + "-0" + i);
                        jsonObject.put("count", df.format(resMap.get(i)));
                    }
                } else {
                    if (i >= 10) {
                        jsonObject.put("time", date + "-" + i);
                        jsonObject.put("count", 0);
                    } else {
                        jsonObject.put("time", date + "-0" + i);
                        jsonObject.put("count", 0);
                    }
                }
                result.add(jsonObject);
            }

            resp.setData(result);
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
            resp.setResultDesc("查询成功");

        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }

        return resp;
    }


    /**
     * 企业服务质量统计  指定年 按月统计
     * year : yyyy
     * type : 1 满意度 2 投诉率
     */
    @GetMapping("/company/findByYear")
    public MessageResp companyFindByYear(@RequestParam(value = "year") String year,
                                         @RequestParam(value = "type") Integer type,
                                         @RequestParam(value = "economicCategoryCode", required = false) String economicCategoryCode) {
        MessageResp resp = new MessageResp();

        List<JSONObject> result = new ArrayList<>();
        List<JSONObject> jsonObjectList = null;
        JSONObject jsonObject = null;

        Map<Long, Map<Integer, Double>> resMap = new HashMap<>();
        StringBuilder corpIdsBuilder = new StringBuilder();
        String corpIdsSql = null;
        //查询企业
        if (StringUtils.isNotBlank(economicCategoryCode)) {
            List<Long> corpIds = companyService.findCorpIdsByEconomicCategoryCode(economicCategoryCode);
            if (corpIds == null || corpIds.size() == 0) {
                resp.setData(result);
                resp.setResult(Boolean.TRUE.toString());
                resp.setStatusCode("00");
                resp.setResultDesc("查询成功");
                return resp;
            }
            for (Long corpId : corpIds) {
                corpIdsBuilder.append("'").append(corpId).append("',");
            }
            corpIdsBuilder.deleteCharAt(corpIdsBuilder.lastIndexOf(","));
            corpIdsSql = " AND CORP_ID IN (" + corpIdsBuilder.toString() + ") ";

        }

        //获取所有企业map
        Map<Long, String> corpMap = new HashMap<>();
        List<TaxiBaseInfoCompany> companyList = companyService.findCompanyNameList();
        for (TaxiBaseInfoCompany companyExtend : companyList) {
            corpMap.put(Long.parseLong(companyExtend.getUuid()), companyExtend.getName());
        }

        try {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT tmp.CORP_ID, tmp.mth, ");
            if (type == 1)
                sqlBuilder.append("(SUM( CASE '1' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END )+0.0)/ ");
            if (type == 2)
                sqlBuilder.append("(SUM( CASE '4' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END )+0.0)/ ");
            sqlBuilder.append("(SUM( CASE '0' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '1' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '2' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '3' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '4' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END )) as coefficient FROM ");
            sqlBuilder.append("(SELECT TIME_FORMAT(__time, 'MM') as mth, EVALUATE_ID, EVALUATE, count( EVALUATE_ID ) AS evaluate_count, CORP_ID FROM ");
            sqlBuilder.append(orderTable);
            sqlBuilder.append(" WHERE TIME_FORMAT(__time, 'yyyy') = '");
            sqlBuilder.append(year).append("' ");
            if (corpIdsSql != null) sqlBuilder.append(corpIdsSql);
            sqlBuilder.append("GROUP BY TIME_FORMAT(__time, 'MM'), CORP_ID, EVALUATE_ID, EVALUATE) tmp GROUP BY tmp.CORP_ID, tmp.mth ");

            String sql = sqlBuilder.toString();
            log.info("companyFindByYear SQL: " + sql);


            JSONObject resObj = null;
            JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", sql);
            for (Object obj : jsonArray) {
                resObj = (JSONObject) obj;
                int mth = Integer.valueOf(resObj.get("mth").toString().trim());
                double coefficient = Double.parseDouble(resObj.get("coefficient").toString());
                String corpIdStr = resObj.get("CORP_ID").toString();
                if (StringUtils.isBlank(corpIdStr)) continue;
                Long corpId = Long.parseLong(corpIdStr);
                if (resMap.containsKey(corpId)) {
                    resMap.get(corpId).put(mth, coefficient);
                } else {

                    Map<Integer, Double> map = new HashMap<>();
                    map.put(mth, coefficient);
                    resMap.put(corpId, map);
                }
            }

            DecimalFormat df = new DecimalFormat("0.00");
            for (Long corpId : resMap.keySet()) {

                if (corpMap.containsKey(corpId)) {
                    jsonObjectList = new ArrayList<>();
                    jsonObject = new JSONObject();
                    jsonObject.put("corpId", corpId);
                    jsonObject.put("corpName", corpMap.get(corpId));
                    Map<Integer, Double> map = resMap.get(corpId);

                    for (int i = 1; i <= 12; i++) {
                        JSONObject objectJson = new JSONObject();
                        if (map.containsKey(i)) {
                            if (i >= 10) {
                                objectJson.put("time", year + "-" + i);
                                objectJson.put("count", df.format(map.get(i)));
                            } else {
                                objectJson.put("time", year + "-0" + i);
                                objectJson.put("count", df.format(map.get(i)));
                            }
                        } else {
                            if (i >= 10) {
                                objectJson.put("time", year + "-" + i);
                                objectJson.put("count", 0);
                            } else {
                                objectJson.put("time", year + "-0" + i);
                                objectJson.put("count", 0);
                            }
                        }
                        jsonObjectList.add(objectJson);
                    }
                    jsonObject.put("dateList", jsonObjectList);
                    result.add(jsonObject);
                }

            }

            resp.setData(result);
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
            resp.setResultDesc("查询成功");

        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }

        return resp;
    }

    /**
     * 企业服务质量统计  按日统计
     * date : yyyy-MM
     * type : 1 满意度 2 投诉率
     */
    @GetMapping("/company/findByDate")
    public MessageResp companyFindByDate(@RequestParam(value = "date") String date,
                                         @RequestParam(value = "type") Integer type,
                                         @RequestParam(value = "economicCategoryCode", required = false) String economicCategoryCode) {
        MessageResp resp = new MessageResp();
        List<JSONObject> result = new ArrayList<>();
        List<JSONObject> jsonObjectList = null;
        JSONObject jsonObject = null;

        Map<Long, Map<Integer, Double>> resMap = new HashMap<>();
        StringBuilder corpIdsBuilder = new StringBuilder();
        String corpIdsSql = null;
        //查询企业
        if (StringUtils.isNotBlank(economicCategoryCode)) {
            List<Long> corpIds = companyService.findCorpIdsByEconomicCategoryCode(economicCategoryCode);
            if (corpIds == null || corpIds.size() == 0) {
                resp.setData(result);
                resp.setResult(Boolean.TRUE.toString());
                resp.setStatusCode("00");
                resp.setResultDesc("查询成功");
                return resp;
            }
            for (Long corpId : corpIds) {
                corpIdsBuilder.append("'").append(corpId).append("',");
            }
            corpIdsBuilder.deleteCharAt(corpIdsBuilder.lastIndexOf(","));
            corpIdsSql = " AND CORP_ID IN (" + corpIdsBuilder.toString() + ") ";

        }

        //获取所有企业map
        Map<Long, String> corpMap = new HashMap<>();
        List<TaxiBaseInfoCompany> companyList = companyService.findCompanyNameList();
        for (TaxiBaseInfoCompany companyExtend : companyList) {
            corpMap.put(Long.parseLong(companyExtend.getUuid()), companyExtend.getName());
        }

        try {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT tmp.CORP_ID, tmp.dd, ");
            if (type == 1)
                sqlBuilder.append("(SUM( CASE '1' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END )+0.0)/ ");
            if (type == 2)
                sqlBuilder.append("(SUM( CASE '4' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END )+0.0)/ ");
            sqlBuilder.append("(SUM( CASE '0' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '1' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '2' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '3' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '4' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END )) as coefficient FROM ");
            sqlBuilder.append("(SELECT TIME_FORMAT(__time, 'dd') as dd, EVALUATE_ID, EVALUATE, count( EVALUATE_ID ) AS evaluate_count, CORP_ID FROM ");
            sqlBuilder.append(orderTable);
            sqlBuilder.append(" WHERE TIME_FORMAT(__time, 'yyyy-MM') = '");
            sqlBuilder.append(date).append("' ");
            if (corpIdsSql != null) sqlBuilder.append(corpIdsSql);
            sqlBuilder.append("GROUP BY TIME_FORMAT(__time, 'dd'), CORP_ID, EVALUATE_ID, EVALUATE) tmp GROUP BY tmp.CORP_ID, tmp.dd ");

            String sql = sqlBuilder.toString();
            log.info("companyFindByDate SQL: " + sql);
            JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", sql);
            JSONObject resObj = null;
            int dayCount = DateUtil.getDayOfMonth(date);
            for (Object obj : jsonArray) {
                resObj = (JSONObject) obj;
                int dd = Integer.valueOf(resObj.get("dd").toString().trim());
                double coefficient = Double.parseDouble(resObj.get("coefficient").toString());
                String corpIdStr = resObj.get("CORP_ID").toString();
                if (StringUtils.isBlank(corpIdStr)) continue;
                Long corpId = Long.parseLong(corpIdStr);
                if (resMap.containsKey(corpId)) {
                    resMap.get(corpId).put(dd, coefficient);
                } else {
                    Map<Integer, Double> map = new HashMap<>();
                    map.put(dd, coefficient);
                    resMap.put(corpId, map);
                }
            }

            DecimalFormat df = new DecimalFormat("0.00");
            for (Long corpId : resMap.keySet()) {
                if (corpMap.containsKey(corpId)) {
                    jsonObjectList = new ArrayList<>();
                    jsonObject = new JSONObject();
                    jsonObject.put("corpId", corpId);
                    jsonObject.put("corpName", corpMap.get(corpId));
                    Map<Integer, Double> map = resMap.get(corpId);

                    for (int i = 1; i <= dayCount; i++) {
                        JSONObject objectJson = new JSONObject();
                        if (map.containsKey(i)) {
                            if (i >= 10) {
                                objectJson.put("time", date + "-" + i);
                                objectJson.put("count", df.format(map.get(i)));
                            } else {
                                objectJson.put("time", date + "-0" + i);
                                objectJson.put("count", df.format(map.get(i)));
                            }
                        } else {
                            if (i >= 10) {
                                objectJson.put("time", date + "-" + i);
                                objectJson.put("count", 0);
                            } else {
                                objectJson.put("time", date + "-0" + i);
                                objectJson.put("count", 0);
                            }
                        }
                        jsonObjectList.add(objectJson);
                    }
                    jsonObject.put("dateList", jsonObjectList);
                    result.add(jsonObject);
                }

            }


            resp.setData(result);
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
            resp.setResultDesc("查询成功");

        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }

        return resp;
    }


    /**
     * 驾驶员服务质量统计  指定年 按月统计
     * year : yyyy
     * type : 1 满意度 2 投诉率
     */
    @GetMapping("/driver/findByYear")
    public MessageResp driverFindByYear(@RequestParam(value = "year") String year,
                                        @RequestParam(value = "type") Integer type,
                                        @RequestParam(value = "age", required = false) Integer age,
                                        @RequestParam(value = "yearsOfWorking", required = false) Integer yearsOfWorking,
                                        @RequestParam(value = "degreeCode", required = false) String degreeCode,
                                        @RequestParam(value = "evaluationLevel", required = false) String evaluationLevel) {
        MessageResp resp = new MessageResp();
        List<JSONObject> result = new ArrayList<>();

        Map<Integer, Double> resMap = new HashMap<>();
        StringBuilder driverIdsBuilder = new StringBuilder();
        String driverIdsSql = null;
        //查询驾驶员
        if (StringUtils.isNotBlank(degreeCode) || age != null || yearsOfWorking != null || StringUtils.isNotBlank(evaluationLevel)) {
            Integer sAge = null;
            Integer eAge = null;
            Integer sYearsOfWorking = null;
            Integer eYearsOfWorking = null;
            if (age != null) {
                switch (age) {
                    case 1:
                        sAge = 0;
                        eAge = 25;
                        break;
                    case 2:
                        sAge = 26;
                        eAge = 35;
                        break;
                    case 3:
                        sAge = 36;
                        eAge = 45;
                        break;
                    case 4:
                        sAge = 46;
                        eAge = 55;
                        break;
                    case 5:
                        sAge = 56;
                        eAge = 999;
                        break;
                }
            }

            if (yearsOfWorking != null) {
                switch (yearsOfWorking) {
                    case 1:
                        sYearsOfWorking = 0;
                        eYearsOfWorking = 2;
                        break;
                    case 2:
                        sYearsOfWorking = 3;
                        eYearsOfWorking = 5;
                        break;
                    case 3:
                        sYearsOfWorking = 6;
                        eYearsOfWorking = 10;
                        break;
                    case 4:
                        sYearsOfWorking = 11;
                        eYearsOfWorking = 15;
                        break;
                    case 5:
                        sYearsOfWorking = 16;
                        eYearsOfWorking = 20;
                        break;
                    case 6:
                        sYearsOfWorking = 21;
                        eYearsOfWorking = 999;
                        break;
                }
            }

            if (StringUtils.isNotBlank(evaluationLevel)) evaluationLevel = evaluationLevel.trim();

            List<Long> driverIds = driverService.findDriverIds(sAge, eAge, sYearsOfWorking, eYearsOfWorking, degreeCode, evaluationLevel);
            if (driverIds == null || driverIds.size() == 0) {
                resp.setData(result);
                resp.setResult(Boolean.TRUE.toString());
                resp.setStatusCode("00");
                resp.setResultDesc("查询成功");
                return resp;
            }
            for (Long driverId : driverIds) {
                driverIdsBuilder.append("'").append(driverId).append("',");
            }
            driverIdsBuilder.deleteCharAt(driverIdsBuilder.lastIndexOf(","));
            driverIdsSql = " AND DRIVER_ID IN (" + driverIdsBuilder.toString() + ") ";

        }

        try {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT tmp.mth, ");
            if (type == 1)
                sqlBuilder.append("(SUM( CASE '1' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END )+0.0)/ ");
            if (type == 2)
                sqlBuilder.append("(SUM( CASE '4' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END )+0.0)/ ");
            sqlBuilder.append("(SUM( CASE '0' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '1' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '2' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '3' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '4' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END )) as coefficient FROM ");
            sqlBuilder.append("(SELECT TIME_FORMAT(__time, 'MM') as mth, EVALUATE_ID, EVALUATE, count( EVALUATE_ID ) AS evaluate_count FROM ");
            sqlBuilder.append(orderTable);
            sqlBuilder.append(" WHERE TIME_FORMAT(__time, 'yyyy') = '");
            sqlBuilder.append(year).append("' ");
            if (driverIdsSql != null) sqlBuilder.append(driverIdsSql);
            sqlBuilder.append("GROUP BY TIME_FORMAT(__time, 'MM'), EVALUATE_ID, EVALUATE) tmp GROUP BY tmp.mth ");

            String sql = sqlBuilder.toString();
            log.info("driverFindByYear SQL: " + sql);


            JSONObject resObj = null;
            JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", sql);
            for (Object obj : jsonArray) {
                resObj = (JSONObject) obj;
                int mth = Integer.valueOf(resObj.get("mth").toString().trim());
                double coefficient = Double.parseDouble(resObj.get("coefficient").toString());
                resMap.put(mth, coefficient);
            }

            DecimalFormat df = new DecimalFormat("0.00");

            for (int i = 1; i <= 12; i++) {
                JSONObject objectJson = new JSONObject();
                if (resMap.containsKey(i)) {
                    if (i >= 10) {
                        objectJson.put("time", year + "-" + i);
                        objectJson.put("count", df.format(resMap.get(i)));
                    } else {
                        objectJson.put("time", year + "-0" + i);
                        objectJson.put("count", df.format(resMap.get(i)));
                    }
                } else {
                    if (i >= 10) {
                        objectJson.put("time", year + "-" + i);
                        objectJson.put("count", 0.00);
                    } else {
                        objectJson.put("time", year + "-0" + i);
                        objectJson.put("count", 0.00);
                    }
                }
                result.add(objectJson);
            }

            resp.setData(result);
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
            resp.setResultDesc("查询成功");

        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }

        return resp;
    }


    /**
     * 驾驶员服务质量统计   按日统计
     * date : yyyy-MM
     * type : 1 满意度 2 投诉率
     */
    @GetMapping("/driver/findByDate")
    public MessageResp driverFindByDate(@RequestParam(value = "date") String date,
                                        @RequestParam(value = "type") Integer type,
                                        @RequestParam(value = "age", required = false) Integer age,
                                        @RequestParam(value = "yearsOfWorking", required = false) Integer yearsOfWorking,
                                        @RequestParam(value = "degreeCode", required = false) String degreeCode,
                                        @RequestParam(value = "evaluationLevel", required = false) String evaluationLevel) {
        MessageResp resp = new MessageResp();
        List<JSONObject> result = new ArrayList<>();

        Map<Integer, Double> resMap = new HashMap<>();
        StringBuilder driverIdsBuilder = new StringBuilder();
        String driverIdsSql = null;
        //查询驾驶员
        if (StringUtils.isNotBlank(degreeCode) || age != null || yearsOfWorking != null || StringUtils.isNotBlank(evaluationLevel)) {
            Integer sAge = null;
            Integer eAge = null;
            Integer sYearsOfWorking = null;
            Integer eYearsOfWorking = null;
            if (age != null) {
                switch (age) {
                    case 1:
                        sAge = 0;
                        eAge = 25;
                        break;
                    case 2:
                        sAge = 26;
                        eAge = 35;
                        break;
                    case 3:
                        sAge = 36;
                        eAge = 45;
                        break;
                    case 4:
                        sAge = 46;
                        eAge = 55;
                        break;
                    case 5:
                        sAge = 56;
                        eAge = 999;
                        break;
                }
            }

            if (yearsOfWorking != null) {
                switch (yearsOfWorking) {
                    case 1:
                        sYearsOfWorking = 0;
                        eYearsOfWorking = 2;
                        break;
                    case 2:
                        sYearsOfWorking = 3;
                        eYearsOfWorking = 5;
                        break;
                    case 3:
                        sYearsOfWorking = 6;
                        eYearsOfWorking = 10;
                        break;
                    case 4:
                        sYearsOfWorking = 11;
                        eYearsOfWorking = 15;
                        break;
                    case 5:
                        sYearsOfWorking = 16;
                        eYearsOfWorking = 20;
                        break;
                    case 6:
                        sYearsOfWorking = 21;
                        eYearsOfWorking = 999;
                        break;
                }
            }

            if (StringUtils.isNotBlank(evaluationLevel)) evaluationLevel = evaluationLevel.trim();
            List<Long> driverIds = driverService.findDriverIds(sAge, eAge, sYearsOfWorking, eYearsOfWorking, degreeCode, evaluationLevel);
            if (driverIds == null || driverIds.size() == 0) {
                resp.setData(result);
                resp.setResult(Boolean.TRUE.toString());
                resp.setStatusCode("00");
                resp.setResultDesc("查询成功");
                return resp;
            }
            for (Long driverId : driverIds) {
                driverIdsBuilder.append("'").append(driverId).append("',");
            }
            driverIdsBuilder.deleteCharAt(driverIdsBuilder.lastIndexOf(","));
            driverIdsSql = " AND DRIVER_ID IN (" + driverIdsBuilder.toString() + ") ";

        }

        try {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT tmp.dd, ");
            if (type == 1)
                sqlBuilder.append("(SUM( CASE '1' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END )+0.0)/ ");
            if (type == 2)
                sqlBuilder.append("(SUM( CASE '4' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END )+0.0)/ ");
            sqlBuilder.append("(SUM( CASE '0' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '1' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '2' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '3' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '4' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END )) as coefficient FROM ");
            sqlBuilder.append("(SELECT TIME_FORMAT(__time, 'dd') as dd, EVALUATE_ID, EVALUATE, count( EVALUATE_ID ) AS evaluate_count FROM ");
            sqlBuilder.append(orderTable);
            sqlBuilder.append(" WHERE TIME_FORMAT(__time, 'yyyy-MM') = '");
            sqlBuilder.append(date).append("' ");
            if (driverIdsSql != null) sqlBuilder.append(driverIdsSql);
            sqlBuilder.append("GROUP BY TIME_FORMAT(__time, 'dd'), EVALUATE_ID, EVALUATE) tmp GROUP BY tmp.dd ");

            String sql = sqlBuilder.toString();
            log.info("driverFindByYear SQL: " + sql);


            JSONObject resObj = null;
            JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", sql);
            for (Object obj : jsonArray) {
                resObj = (JSONObject) obj;
                int dd = Integer.valueOf(resObj.get("dd").toString().trim());
                double coefficient = Double.parseDouble(resObj.get("coefficient").toString());
                resMap.put(dd, coefficient);
            }

            int dayCount = DateUtil.getDayOfMonth(date);
            DecimalFormat df = new DecimalFormat("0.00");

            for (int i = 1; i <= dayCount; i++) {
                JSONObject objectJson = new JSONObject();
                if (resMap.containsKey(i)) {
                    if (i >= 10) {
                        objectJson.put("time", date + "-" + i);
                        objectJson.put("count", df.format(resMap.get(i)));
                    } else {
                        objectJson.put("time", date + "-0" + i);
                        objectJson.put("count", df.format(resMap.get(i)));
                    }
                } else {
                    if (i >= 10) {
                        objectJson.put("time", date + "-" + i);
                        objectJson.put("count", 0.00);
                    } else {
                        objectJson.put("time", date + "-0" + i);
                        objectJson.put("count", 0.00);
                    }
                }
                result.add(objectJson);
            }

            resp.setData(result);
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
            resp.setResultDesc("查询成功");

        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }

        return resp;
    }


    /**
     * 服务质量排行统计  指定年统计
     * year : yyyy
     * type : 1 企业 2 驾驶员
     */
    @GetMapping("/top/findByYear")
    public MessageResp topFindByYear(@RequestParam(value = "year") String year,
                                     @RequestParam(value = "type") Integer type,
                                     @RequestParam(value = "limit") Integer limit) {
        MessageResp resp = new MessageResp();
        List<JSONObject> result = new ArrayList<>();
        JSONObject jsonObject = null;

        try {
            StringBuilder sqlBuilder = new StringBuilder();
            if (type == 1) sqlBuilder.append("SELECT tmp.CORP_ID AS id, ");
            if (type == 2) sqlBuilder.append("SELECT tmp.DRIVER_ID AS id, ");
            sqlBuilder.append("(SUM( CASE '1' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END )+0.0)/ ");
            sqlBuilder.append("(SUM( CASE '0' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '1' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '2' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '3' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END ) + ");
            sqlBuilder.append("SUM( CASE '4' WHEN tmp.EVALUATE THEN tmp.evaluate_count ELSE 0 END )) as coefficient FROM ");
            sqlBuilder.append("(SELECT EVALUATE_ID, EVALUATE, count( EVALUATE_ID ) AS evaluate_count, ");
            if (type == 1) sqlBuilder.append("CORP_ID FROM ");
            if (type == 2) sqlBuilder.append("DRIVER_ID FROM ");
            sqlBuilder.append(orderTable);
            sqlBuilder.append(" WHERE TIME_FORMAT(__time, 'yyyy') = '");
            sqlBuilder.append(year).append("' ");
            if (type == 1)
                sqlBuilder.append("GROUP BY CORP_ID, EVALUATE_ID, EVALUATE) tmp GROUP BY tmp.CORP_ID ORDER BY coefficient desc limit ");
            if (type == 2)
                sqlBuilder.append("GROUP BY DRIVER_ID, EVALUATE_ID, EVALUATE) tmp GROUP BY tmp.DRIVER_ID ORDER BY coefficient desc limit ");
            sqlBuilder.append(limit);
            String sql = sqlBuilder.toString();
            log.info("topFindByYear SQL: " + sql);
            Map<Long, String> driverMap = new HashMap<>();
            Map<Long, String> corpMap = new HashMap<>();
            if (type == 1) {
                List<TaxiBaseInfoCompany> companyNameList = companyService.findCompanyNameList();
                for (TaxiBaseInfoCompany companyExtend : companyNameList) {
                    corpMap.put(Long.parseLong(companyExtend.getUuid()), companyExtend.getName());
                }
            }
            if (type == 2) {
                List<TaxiBaseInfoDriver> driverNameList = driverService.findDriverNameList();
                for (TaxiBaseInfoDriver driverExtend : driverNameList) {
                    driverMap.put(Long.parseLong(driverExtend.getUuid()), driverExtend.getName());
                }
            }
            JSONObject resObj = null;
            JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", sql);
            Map<String, Object> resMap = new LinkedHashMap<>();
            DecimalFormat df = new DecimalFormat("0.00");
            for (Object obj : jsonArray) {
                jsonObject = new JSONObject();
                resObj = (JSONObject) obj;
                String idStr = resObj.get("id").toString();
                if (StringUtils.isBlank(idStr)) continue;
                Long id = Long.parseLong(idStr);
                double coefficient = Double.parseDouble(resObj.get("coefficient").toString());
                if (type == 1 && corpMap.containsKey(id)) {
                    jsonObject.put("corpId", id);
                    jsonObject.put("corpName", corpMap.get(id));
                    jsonObject.put("count", df.format(coefficient));

                } else if (type == 2 && driverMap.containsKey(id)) {
                    jsonObject.put("driverId", id);
                    jsonObject.put("driverName", driverMap.get(id));
                    jsonObject.put("count", df.format(coefficient));
                } else {
                    continue;
                }
                result.add(jsonObject);
            }
            resp.setData(result);
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
            resp.setResultDesc("查询成功");

        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }

        return resp;
    }


    /**
     * 工作时长统计 指定年按月统计
     * year : yyyy
     */
    @GetMapping("/workingTime/findByYear")
    public MessageResp workingTimeFindByYear(@RequestParam(value = "year") String year) {

        MessageResp resp = new MessageResp();
        List<JSONObject> result = new ArrayList<>();
        List<JSONObject> jsonObjectList = null;
        JSONObject jsonObject = null;

        Map<Long, Map<Integer, Double>> resMap = new HashMap<>();
        try {
            List<TaxiWorkingTime> list = taxiCheckInOutService.workingTimeFindByYear(year);
            for (TaxiWorkingTime taxiWorkingTime : list) {
                String corpIdStr = taxiWorkingTime.getCorpId();
                if (StringUtils.isBlank(corpIdStr) || taxiWorkingTime.getCountTime() == null || taxiWorkingTime.getTime() == null
                        || taxiWorkingTime.getCountTime() < 0)
                    continue;
                Long corpId = Long.parseLong(corpIdStr);
                if (resMap.containsKey(corpId)) {
                    resMap.get(corpId).put(taxiWorkingTime.getTime(), taxiWorkingTime.getCountTime());

                } else {
                    Map<Integer, Double> map = new HashMap<>();
                    map.put(taxiWorkingTime.getTime(), taxiWorkingTime.getCountTime());
                    resMap.put(corpId, map);
                }

            }

            //获取所有企业map
            Map<Long, String> corpMap = new HashMap<>();
            List<TaxiBaseInfoCompany> companyList = companyService.findCompanyNameList();
            for (TaxiBaseInfoCompany companyExtend : companyList) {
                corpMap.put(Long.parseLong(companyExtend.getUuid()), companyExtend.getName());
            }

            DecimalFormat df = new DecimalFormat("0.00");
            for (Long corpId : resMap.keySet()) {

                if (corpMap.containsKey(corpId)) {
                    jsonObjectList = new ArrayList<>();
                    jsonObject = new JSONObject();
                    jsonObject.put("corpId", corpId);
                    jsonObject.put("corpName", corpMap.get(corpId));
                    Map<Integer, Double> map = resMap.get(corpId);

                    for (int i = 1; i <= 12; i++) {
                        JSONObject objectJson = new JSONObject();
                        if (map.containsKey(i)) {
                            if (i >= 10) {
                                objectJson.put("time", year + "-" + i);
                                objectJson.put("count", df.format(map.get(i)));
                            } else {
                                objectJson.put("time", year + "-0" + i);
                                objectJson.put("count", df.format(map.get(i)));
                            }
                        } else {
                            if (i >= 10) {
                                objectJson.put("time", year + "-" + i);
                                objectJson.put("count", 0);
                            } else {
                                objectJson.put("time", year + "-0" + i);
                                objectJson.put("count", 0);
                            }
                        }
                        jsonObjectList.add(objectJson);
                    }
                    jsonObject.put("dateList", jsonObjectList);
                    result.add(jsonObject);
                }
            }

            resp.setData(result);
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
            resp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }

        return resp;
    }

    /**
     * 工作时长统计 按日统计
     * date : yyyy-MM
     */
    @GetMapping("/workingTime/findByDate")
    public MessageResp workingTimeFindByDate(@RequestParam(value = "date") String date) {

        MessageResp resp = new MessageResp();
        List<JSONObject> result = new ArrayList<>();
        List<JSONObject> jsonObjectList = null;
        JSONObject jsonObject = null;

        Map<Long, Map<Integer, Double>> resMap = new HashMap<>();
        try {
            List<TaxiWorkingTime> list = taxiCheckInOutService.workingTimeFindByDate(date);
            for (TaxiWorkingTime taxiWorkingTime : list) {
                String corpIdStr = taxiWorkingTime.getCorpId();
                if (StringUtils.isBlank(corpIdStr) || taxiWorkingTime.getCountTime() == null || taxiWorkingTime.getTime() == null)
                    continue;
                Long corpId = Long.parseLong(corpIdStr);
                if (resMap.containsKey(corpId)) {
                    resMap.get(corpId).put(taxiWorkingTime.getTime(), taxiWorkingTime.getCountTime());

                } else {
                    Map<Integer, Double> map = new HashMap<>();
                    map.put(taxiWorkingTime.getTime(), taxiWorkingTime.getCountTime());
                    resMap.put(corpId, map);
                }

            }

            int dayCount = DateUtil.getDayOfMonth(date);

            //获取所有企业map
            Map<Long, String> corpMap = new HashMap<>();
            List<TaxiBaseInfoCompany> companyList = companyService.findCompanyNameList();
            for (TaxiBaseInfoCompany companyExtend : companyList) {
                corpMap.put(Long.parseLong(companyExtend.getUuid()), companyExtend.getName());
            }


            DecimalFormat df = new DecimalFormat("0.00");
            for (Long corpId : resMap.keySet()) {

                if (corpMap.containsKey(corpId)) {
                    jsonObjectList = new ArrayList<>();
                    jsonObject = new JSONObject();
                    jsonObject.put("corpId", corpId);
                    jsonObject.put("corpName", corpMap.get(corpId));
                    Map<Integer, Double> map = resMap.get(corpId);

                    for (int i = 1; i <= dayCount; i++) {
                        JSONObject objectJson = new JSONObject();
                        if (map.containsKey(i)) {
                            if (i >= 10) {
                                objectJson.put("time", date + "-" + i);
                                objectJson.put("count", df.format(map.get(i)));
                            } else {
                                objectJson.put("time", date + "-0" + i);
                                objectJson.put("count", df.format(map.get(i)));
                            }
                        } else {
                            if (i >= 10) {
                                objectJson.put("time", date + "-" + i);
                                objectJson.put("count", 0);
                            } else {
                                objectJson.put("time", date + "-0" + i);
                                objectJson.put("count", 0);
                            }
                        }
                        jsonObjectList.add(objectJson);
                    }
                    jsonObject.put("dateList", jsonObjectList);
                    result.add(jsonObject);
                }
            }

            resp.setData(result);
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
            resp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }

        return resp;
    }

    /**
     * 收入关联统计 月工作时长 指定年按月统计
     * year : yyyy
     */
    @GetMapping("/incomeRelevanceWorkingTime/findByYear")
    public MessageResp incomeRelevanceWorkingTimeFindByYear(@RequestParam(value = "year") String year) {

        MessageResp resp = new MessageResp();
        List<JSONObject> result = new ArrayList<>();
        JSONObject jsonObject = null;

        Map<Integer, Double> resMap = new HashMap<>();
        try {
            List<TaxiWorkingTime> list = taxiCheckInOutService.incomeRelevanceWorkingTimeFindByYear(year);
            for (TaxiWorkingTime taxiWorkingTime : list) {
                if (taxiWorkingTime.getCountTime() == null || taxiWorkingTime.getTime() == null || taxiWorkingTime.getCountTime() < 0)
                    continue;
                resMap.put(taxiWorkingTime.getTime(), taxiWorkingTime.getCountTime());

            }

            DecimalFormat df = new DecimalFormat("0.00");
            for (int i = 1; i <= 12; i++) {
                jsonObject = new JSONObject();
                if (resMap.containsKey(i)) {
                    if (i >= 10) {
                        jsonObject.put("time", year + "-" + i);
                        jsonObject.put("count", df.format(resMap.get(i)));
                    } else {
                        jsonObject.put("time", year + "-0" + i);
                        jsonObject.put("count", df.format(resMap.get(i)));
                    }
                } else {
                    if (i >= 10) {
                        jsonObject.put("time", year + "-" + i);
                        jsonObject.put("count", 0);
                    } else {
                        jsonObject.put("time", year + "-0" + i);
                        jsonObject.put("count", 0);
                    }
                }
                result.add(jsonObject);
            }

            resp.setData(result);
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
            resp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }

        return resp;
    }


    /**
     * 月营运收入 指定年按月统计
     * year : yyyy
     */
    @GetMapping("/income/findByYear")
    public MessageResp incomeFindByYear(@RequestParam(value = "year") String year) {

        MessageResp resp = new MessageResp();
        List<JSONObject> result = new ArrayList<>();
        JSONObject jsonObject = null;
        Map<Integer, Double> resMap = new HashMap<>();
        try {
            List<TaxiWorkingTime> list = taxiCheckInOutService.incomeFindByYear(year);
            for (TaxiWorkingTime taxiWorkingTime : list) {
                if (taxiWorkingTime.getTotalMoney() == null || taxiWorkingTime.getTime() == null)
                    continue;
                resMap.put(taxiWorkingTime.getTime(), taxiWorkingTime.getTotalMoney());

            }
            DecimalFormat df = new DecimalFormat("0.00");
            for (int i = 1; i <= 12; i++) {
                jsonObject = new JSONObject();
                if (resMap.containsKey(i)) {
                    if (i >= 10) {
                        jsonObject.put("time", year + "-" + i);
                        jsonObject.put("count", df.format(resMap.get(i)));
                    } else {
                        jsonObject.put("time", year + "-0" + i);
                        jsonObject.put("count", df.format(resMap.get(i)));
                    }
                } else {
                    if (i >= 10) {
                        jsonObject.put("time", year + "-" + i);
                        jsonObject.put("count", 0);
                    } else {
                        jsonObject.put("time", year + "-0" + i);
                        jsonObject.put("count", 0);
                    }
                }
                result.add(jsonObject);
            }

            resp.setData(result);
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
            resp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }

        return resp;
    }

}
