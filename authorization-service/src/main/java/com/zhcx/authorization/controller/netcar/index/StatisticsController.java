package com.zhcx.authorization.controller.netcar.index;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhcx.authorization.controller.netcar.base.RoleAuthenticationUtils;
import com.zhcx.authorization.utils.*;
import com.zhcx.netcarbasic.facade.netcar.*;
import com.zhcx.netcarbasic.pojo.DruidEntity.LogInOutResult;
import com.zhcx.netcarbasic.pojo.DruidEntity.NetcarStatisticsOrder;
import com.zhcx.netcarbasic.pojo.DruidEntity.OrderStatisticsResult;
import com.zhcx.netcarbasic.pojo.DruidEntity.RevocationResult;
import com.zhcx.netcarbasic.pojo.netcar.BookCarLevel;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoCompanyService;
import com.zhcx.netcarbasic.pojo.netcar.NetcarOrderCreate;
import com.zhcx.platformtonet.facade.YunZhengVehicleService;
import com.zhcx.platformtonet.facade.YunzhengAmountService;
import com.zhcx.platformtonet.facade.statistical.StatisticalService;
import com.zhcx.platformtonet.pojo.base.SignInBack;
import com.zhcx.platformtonet.pojo.base.YunZhengVehicle;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.summingInt;

/**
 * @version V1.0
 * @autor ht(15616537979 @ 126.com)
 * @date 2018/12/20
 * @description
 **/
@RestController
@RequestMapping("/netcar/statistics")
@Api(value = "statistics", tags = "报表统计接口")
public class StatisticsController {

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;

    @Autowired
    private CompanyStatService companyStatService;

    @Autowired
    private BaseDictionaryService dictionaryService;

    @Autowired
    private StatisticalService statisticalService;

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private CompanyServiceService companyServiceService;

    @Autowired
    private YunZhengVehicleService yunZhengVehicleService;

    @Autowired
    private YunzhengAmountService yunzhengAmountService;

    private String ids;

    private static Logger log = LoggerFactory.getLogger(StatisticsController.class);


    @Autowired
    private StatisticalUtil statisticalUtil;

    @ApiOperation(value = "首页--分时订单营收金额统计", notes = "首页--分时订单营收金额统计")
    @GetMapping("/queryHourOrderFeeCount")
    public MessageResp queryHourOrderFeeCount(HttpServletRequest request) {
        MessageResp messageResp = new MessageResp();
        try {
            String sql = "SELECT COMPANY_ID,ON_AREA,VEHICLE_NO,drive_mile_sum,drive_time_sum,fact_price_sum,order_count,wait_mile_sum,wait_time_sum";
            String table = "netcar_statistics_operatePay";
            String SDate = DateUtil.getYMDFormat(DateUtil.getSomeDay(0));
            JSONArray anteayerdata = getorderData(sql, table, SDate, null, "day");
            List<NetcarStatisticsOrder> anteayerList = JSONObject.parseArray(anteayerdata.toJSONString(), NetcarStatisticsOrder.class);
            Map<String, List<NetcarStatisticsOrder>> orderList = anteayerList.stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getPAY_TIME));
            List<Map<String, Object>> dateList = statisticalUtil.dataInit(DateUtil.getYMDFormat(new Date()), "day");
            orderList.keySet().forEach(time -> {
                List<NetcarStatisticsOrder> orders = orderList.get(time);
                Double factPrice = orders.stream().collect(summingDouble(NetcarStatisticsOrder::getFact_price_sum));
                dateList.forEach(map -> {
                    if (StringUtils.equals(time, String.valueOf(map.get("moment")))) {
                        map.put("total", factPrice);
                    }
                });
            });
            messageResp.setData(dateList);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取首页营收金额/实时订单结果异常，异常详情：{}", e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("查询异常");
        }
        return messageResp;
    }


    @ApiOperation(value = "首页--分时订单量统计", notes = "首页--分时订单量统计")
    @GetMapping("/queryHourOrderCount")
    public MessageResp queryHourOrderCount(HttpServletRequest request) {
        MessageResp messageResp = new MessageResp();
        try {
            String sql = "SELECT COMPANY_ID,ON_AREA,VEHICLE_NO,drive_mile_sum,drive_time_sum,fact_price_sum,order_count,wait_mile_sum,wait_time_sum";
            String table = "netcar_statistics_operatePay";
            String SDate = DateUtil.getYMDFormat(DateUtil.getSomeDay(0));
            JSONArray anteayerdata = getorderData(sql, table, SDate, null, "day");
            List<NetcarStatisticsOrder> anteayerList = JSONObject.parseArray(anteayerdata.toJSONString(), NetcarStatisticsOrder.class);
            Map<String, List<NetcarStatisticsOrder>> orderList = anteayerList.stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getPAY_TIME));
            List<Map<String, Object>> dateList = statisticalUtil.dataInit(DateUtil.getYMDFormat(new Date()), "day");
            orderList.keySet().forEach(time -> {
                Map<String, Object> redultMap = new HashMap<>();
                List<NetcarStatisticsOrder> orders = orderList.get(time);
                Double orderCount = orders.stream().collect(summingDouble(NetcarStatisticsOrder::getOrder_count));
                redultMap.put("total", orderCount);
                dateList.forEach(map -> {
                    if (StringUtils.equals(time, String.valueOf(map.get("moment")))) {
                        map.put("total", orderCount);
                    }
                });
                orders.stream().forEach(System.out::println);
            });
            messageResp.setData(dateList);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取首页实时订单结果异常，异常详情：{}", e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("查询异常");
        }
        return messageResp;
    }


    /**
     * 乘客投诉信息统计
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "首页乘客投诉信息统计", notes = "首页乘客投诉信息统计")
    @GetMapping("/ratedPassengerComplaint")
    public MessageResp getRatedPassengerComplaintStatisticDate(HttpServletRequest request) {
        MessageResp messageResp = new MessageResp();
        try {
            StringBuilder sql = new StringBuilder();
            String corpId = roleAuthenticationUtils.getCompanyId(request, null);

            Map<String, String> parameters = UrlParamUtil.getParameters(request);


            //获取时间(统计维度为年月日) （最小粒度为分钟）
            String startTime = parameters.get("startTime");

            //获取每分钟投诉次数
            sql.append("SELECT sum(complaint_count) total FROM netcar_ratedPassengerComplaint where TIME_FORMAT(__time,'yyyy-MM-dd HH:mm')='");
            sql.append(startTime.substring(0, 16));
            sql.append("'");
            if (StringUtils.isNotBlank(corpId)) {
                sql.append("and COMPANY_ID='");
                sql.append(corpId).append("'");
            }
            String ids = roleAuthenticationUtils.filterCompanyIdCondition();
            if (StringUtils.isNotBlank(ids)) {
                sql.append("and COMPANY_ID in (").append(ids).append(")");
            }
            log.info("获取每分钟投诉次数 sql:{}", sql);
            JSONArray orderSumJson = httpUtils.doPostSqlUrl("sql", sql.toString());
            log.info("获取每分钟投诉次数结果:{}", orderSumJson);
            Map<String, Object> map = new HashMap<>();

            if (orderSumJson.size() >= 1) {
                map.put("totalSum", orderSumJson);
            } else {
                map.put("totalSum", 0);
            }
            messageResp.setData(map);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取每分钟投诉次数结果异常，异常详情：{}", e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("查询异常");
        }
        return messageResp;
    }


    @Autowired
    private OrderService orderService;

    /**
     * 用车需求分析:1h内所有订单发起坐标点
     */
    @ApiOperation(value = "用车需求分析", notes = "用车需求分析")
    @GetMapping("/carNeedPerHour")
    public MessageResp queryCarNeedPerHour(@RequestParam(required = false) String startTime,
                                           @RequestParam(required = false) String endTime) {

        MessageResp messageResp = new MessageResp();
        try {
            List<NetcarOrderCreate> list = orderService.queryOrderPositionListByOrderCreate(startTime, endTime);
            messageResp.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询异常");
        }

        return messageResp;
    }

    /**
     * 打车难易度
     * 打车等待时长：
     * 等级1：小于1分钟
     * 等级2：大于1分钟 小于6分钟
     * 等级3：大于6分钟 小于10分钟
     * 等级4：大于10分钟
     */
    @ApiOperation(value = "打车难易度", notes = "打车难易度")
    @GetMapping("/bookCarLevel")
    public MessageResp queryBookCarLevel(@RequestParam(required = false) String startTime,
                                         @RequestParam(required = false) String endTime) {

        MessageResp messageResp = new MessageResp();
        try {
            List<BookCarLevel> resultMap = orderService.queryBookCarLevelByOrderCreateAndMatch(startTime, endTime);
            messageResp.setData(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询异常");
        }

        return messageResp;
    }

    /**
     * 签到签退统计
     *
     * @param startTime
     * @return
     */
    @GetMapping("/sign")
    public MessageResp getSignInBack(@RequestParam(value = "startTime", defaultValue = "") String startTime,
                                     @RequestParam(value = "dateType", defaultValue = "month") String dateType) {
        MessageResp result = new MessageResp();
        try {
            Map<String, Object> resultMap = new HashMap<>();
            String sql = "select COMPANY_ID, LICENSE_ID, VEHICLE_NO, LOG_TYPE, log_count";
            String table = "netcar_operate_loginout";
            if (StringUtils.isBlank(startTime)) {
                startTime = DateUtil.getYMDFormat(new Date()).substring(0, 7);
            }
            JSONArray loginDate = getorderData(sql, table, startTime, null, dateType);
            List<LogInOutResult> logInOutResultList = JSONObject.parseArray(loginDate.toJSONString(), LogInOutResult.class);
            Map<String, List<LogInOutResult>> companyList = logInOutResultList.stream().collect(Collectors.groupingBy(LogInOutResult::getCOMPANY_ID));
            List<SignInBack> resultData = new ArrayList<>();
            String finalStartTime = startTime;
            companyList.keySet().forEach(companyId -> {
                List<LogInOutResult> log = companyList.get(companyId);
                SignInBack signInBack = new SignInBack();
                Map<Integer, List<LogInOutResult>> data = log.stream().collect(Collectors.groupingBy(LogInOutResult::getLOG_TYPE));
                Integer signIn = data.get(1).stream().collect(summingInt(LogInOutResult::getLog_count));
                Integer signBack = data.get(0).stream().collect(summingInt(LogInOutResult::getLog_count));
                signInBack.setSignIn(signIn);
                signInBack.setSignBack(signBack);


                NetcarBaseInfoCompanyService companyService = companyServiceService.selectByCompanyId(companyId);
                signInBack.setCompanyName(companyService.getServiceName());
                signInBack.setTotal(Math.toIntExact(yunzhengAmountService.selectByTime(companyService.getServiceNo(), finalStartTime, dateType)));
                resultData.add(signInBack);
            });
            resultMap.put("signInBackList", resultData);
            result.setData(resultMap);
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * 从业人员车辆分析
     */
    @GetMapping("/analysis")
    public MessageResp getAnalysis() {
        MessageResp result = new MessageResp();
        try {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap = statisticalService.getAnalysis();
            result.setPageBean(null);
            result.setData(resultMap);
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * 业务量分析
     *
     * @param startTime
     * @param dateType
     * @return
     */
    @GetMapping("/summarizing")
    public MessageResp getOrderSummarizing(@RequestParam String startTime,
                                           @RequestParam(value = "dateType", defaultValue = "month") String dateType) {
        MessageResp messageResp = new MessageResp<>();

        try {
            JSONArray getdata = getorderData(startTime, null, dateType);
            List<NetcarStatisticsOrder> orderList = JSONObject.parseArray(getdata.toJSONString(), NetcarStatisticsOrder.class);
            Map<String, List<NetcarStatisticsOrder>> companys = orderList.stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getCOMPANY_ID));
            List<String> companyKeyList = companys.keySet().stream().sorted(Comparator.comparing(String::hashCode)).collect(Collectors.toList());

            List<Map> resultList = Lists.newArrayList();

            companyKeyList.forEach(companyId -> {
                List<NetcarStatisticsOrder> orders = companys.get(companyId);
                Map<String, List<NetcarStatisticsOrder>> orderTimes = orders.stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getPAY_TIME));

                List<Map<String, Object>> dataList = statisticalUtil.dataInit(startTime, dateType);
                Map<String, Object> companyMap = new HashMap<>();
                companyMap.put("name", companyServiceService.selectCompanyNameByCompanyId(companyId));
                companyMap.put("id", companyId);

                orderTimes.keySet().forEach(time -> {
                    List<NetcarStatisticsOrder> order = orderTimes.get(time);
                    //总订单量
                    int amount = order.stream().collect(summingInt(NetcarStatisticsOrder::getOrder_count));
                    //车辆数
                    Long vehicle = order.stream().filter(distinctByKey(NetcarStatisticsOrder::getVEHICLE_NO)).count();
                    dataList.forEach(map -> {
                        String moment = String.valueOf(map.get("moment"));
                        if (StringUtils.equals(moment, time)) {
                            map.put("total", amount);
                            map.put("proportion", amount / vehicle);
                        } else {
                            map.put("proportion", 0);
                        }
                    });
                    companyMap.put("statistics", dataList);
                });
                resultList.add(companyMap);
            });
            messageResp.setData(resultList);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setStatusCode("00");
            messageResp.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setStatusCode("-50");
            messageResp.setResultDesc("操作失败");
        }

        return messageResp;
    }


    /***********************分割****************************/
    /**
     * 功能：添加企业限定
     *
     * @param builder
     * @return
     */
    private StringBuilder stringJoint(StringBuilder builder) {
        this.ids = roleAuthenticationUtils.filterCompanyIdCondition();
        if (StringUtils.isNotBlank(ids)) {
            String[] newStr = ids.split(",");
            builder.append("and COMPANY_ID in (");
            for (String str : newStr) {
                builder.append("'").append(str).append("',");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append(")");
        }
        return builder;
    }

    /**
     * 添加车辆限定
     *
     * @return
     */
    public StringBuilder getVehicleNo() {
        StringBuilder builder = new StringBuilder();
        String[] compans = ids.split(",");
        List<String> companyList = new ArrayList<>();
        for (String company : compans) {
            companyList.add(company);
        }
        List<YunZhengVehicle> vehicleNos = yunZhengVehicleService.filterVehicleNoCondition(companyList);
        if (vehicleNos.size() > 0) {
            builder.append("and VEHICLE_NO in (");
            for (YunZhengVehicle vehicleNo : vehicleNos) {
                builder.append("'").append(vehicleNo.getBranum()).append("',");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append(")");
        }
        return builder;
    }

    /**
     * 功能：去重
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    /***********************分割****************************/

    /**
     * 功能：获取指定时间段内的数据
     * 数据源：druid
     * 表名称：netcar_statistics_operatePay
     *
     * @param SDate
     * @param EDate
     * @return
     * @throws Exception
     */
    public JSONArray getorderData(String sql, String table, String SDate, String EDate, String dateType) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(sql);
        if (StringUtils.isNotEmpty(dateType)) {
            //以“月”为单位获取数据
            if (StringUtils.equals(dateType, "year")) {
                builder.append(", TIME_FORMAT(__time,'yyyy-MM') as PAY_TIME ");
                builder.append("FROM " + table + " WHERE");
                if (StringUtils.isNotEmpty(EDate)) {
                    builder.append(" TIME_FORMAT(__time,'yyyy') >= '").append(SDate).append("'");
                    builder.append(" and TIME_FORMAT(__time,'yyyy') <= '").append(EDate).append("'");
                } else {
                    builder.append(" TIME_FORMAT(__time,'yyyy') = '").append(SDate).append("'");
                }
            }
            //以“天”为单位获取数据
            if (StringUtils.equals(dateType, "month")) {
                builder.append(", TIME_FORMAT(__time,'yyyy-MM-dd') as PAY_TIME ");
                builder.append("FROM " + table + " WHERE");
                if (StringUtils.isNotEmpty(EDate)) {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM') >= '").append(SDate).append("'");
                    builder.append(" and TIME_FORMAT(__time,'yyyy-MM') <= '").append(EDate).append("'");
                } else {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM') = '").append(SDate).append("'");
                }
            }
            //以“小时”为单位获取数据
            if (StringUtils.equals(dateType, "day")) {
                builder.append(", TIME_FORMAT(__time,'yyyy-MM-dd HH') as PAY_TIME ");
                builder.append("FROM " + table + " WHERE");
                if (StringUtils.isNotEmpty(EDate)) {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM-dd') >= '").append(SDate).append("'");
                    builder.append(" and TIME_FORMAT(__time,'yyyy-MM-dd') <= '").append(EDate).append("'");
                } else {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM-dd') = '").append(SDate).append("'");
                }
            }
        } else {
            builder.append(", TIME_FORMAT(__time,'yyyy-MM-dd') as PAY_TIME ");
        }
        builder = stringJoint(builder);
        builder = builder.append(getVehicleNo());
        log.info("订单查询sql" + builder);
        return httpUtils.doPostSqlUrl("sql", String.valueOf(builder));
    }


    /**
     * 报表分析：营运统计
     * 多维度统计
     *
     * @param request
     * @return
     */
    @GetMapping("/order")
    public MessageResp getOrderStatistical(HttpServletRequest request,
                                           @RequestParam String SDate,
                                           @RequestParam(value = "EDate", required = false) String EDate) {
        MessageResp messageResp = new MessageResp();
        try {
            JSONArray getdata = getorderData(SDate, EDate, "month");
            List<NetcarStatisticsOrder> orderList = JSONObject.parseArray(getdata.toJSONString(), NetcarStatisticsOrder.class);
            messageResp.setData(summarizingStatistics(orderList));
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
     * 功能：企业空驶特征分析（指定时间）
     *
     * @param request
     * @param SDate
     * @param dateType
     * @return
     */
    @GetMapping("/empty")
    public MessageResp getcharacteristicEmpty(HttpServletRequest request,
                                              @RequestParam String SDate,
                                              @RequestParam String dateType) {
        MessageResp messageResp = new MessageResp();
        try {
            JSONArray getdata = getorderData(SDate, null, dateType);
            List<NetcarStatisticsOrder> orderList = JSONObject.parseArray(getdata.toJSONString(), NetcarStatisticsOrder.class);
            messageResp.setData(averageEmpty(orderList, dateType, SDate));
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
     * 功能：获取多家公司，每天24个时段的平均空驶时间
     *
     * @param orderList
     * @return
     */
    private List<Map<String, Object>> averageEmpty(List<NetcarStatisticsOrder> orderList, String dateType, String SDate) {
        List<Map<String, Object>> result = new ArrayList<>();
        //将数据基于公司分组
        Map<String, List<NetcarStatisticsOrder>> companys = orderList.stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getCOMPANY_ID));
        companys.keySet().forEach(companyId -> {

                    List<NetcarStatisticsOrder> orders = companys.get(companyId);
                    Map<String, List<NetcarStatisticsOrder>> time = orders.stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getPAY_TIME));


                    Map<String, Object> companyMap = new HashMap<>();
                    companyMap.put("companyName", companyServiceService.selectCompanyNameByCompanyId(companyId));
                    companyMap.put("companyId", companyId);
                    List<Map<String, Object>> averageData = statisticalUtil.dataInit(SDate, dateType);
                    time.keySet().forEach(key -> {
                        //获取每个时间段的数据集合
                        List<NetcarStatisticsOrder> timeOrder = time.get(key);
                        //获取每个时间段的车辆数（去重）
                        Long vehicleAmount = timeOrder.stream().filter(distinctByKey(value -> value.getVEHICLE_NO())).count();
                        //获取每个时间段的总空驶时间
                        int waitMile = timeOrder.stream().collect(summingInt(NetcarStatisticsOrder::getWait_time_sum));
                        //总载客里程
                        Double percentage = timeOrder.stream().collect(summingDouble(NetcarStatisticsOrder::getDrive_mile_sum));
                        //总空驶里程
                        Double empty = timeOrder.stream().collect(summingDouble(NetcarStatisticsOrder::getWait_mile_sum));

                        averageData.forEach(map -> {
                            String moment = String.valueOf(map.get("moment"));
                            map.put("time", moment);
                            if (StringUtils.equals(moment, key)) {
                                //空驶率
                                map.put("percentage", empty / (percentage + empty));
                                //平均空驶时间
                                map.put("waitMile", BigDecimalUtil.div(waitMile, vehicleAmount));
                            } else {
                                map.put("percentage", 0);
                                map.put("waitMile", 0);
                            }
                        });
                    });
                    companyMap.put("data", averageData);
                    result.add(companyMap);
                }
        );
        return result;
    }


    /**
     * 功能：汇总
     * 1.总趟次
     * 2.总金额
     * 3.总营运时间
     * 4.总空驶里程
     * 5.总载客里程
     * 6.总订单数
     * 7.总空驶时间
     *
     * @param orderList
     */
    private List<OrderStatisticsResult> summarizingStatistics(List<NetcarStatisticsOrder> orderList) {
        Map<String, List<NetcarStatisticsOrder>> groupByCompanyID = orderList.stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getCOMPANY_ID));
        List<OrderStatisticsResult> resultList = new ArrayList<>();
        for (String key : groupByCompanyID.keySet()) {
            OrderStatisticsResult orderStatisticsResult = new OrderStatisticsResult();
            List<NetcarStatisticsOrder> companyList = groupByCompanyID.get(key);
            //总趟次/总订单数
            orderStatisticsResult.setOrderCount(companyList.stream().collect(summingInt(NetcarStatisticsOrder::getOrder_count)));
            orderStatisticsResult.setTotal(orderStatisticsResult.getOrderCount());
            //总金额
            orderStatisticsResult.setFactPrice(companyList.stream().collect(summingDouble(NetcarStatisticsOrder::getFact_price_sum)));
            //总载客时间
            orderStatisticsResult.setDriveTime(companyList.stream().collect(summingInt(NetcarStatisticsOrder::getDrive_time_sum)));
            //总空驶时间
            orderStatisticsResult.setWaitTime(companyList.parallelStream().collect(summingInt(NetcarStatisticsOrder::getWait_time_sum)));
            //总载客里程
            orderStatisticsResult.setDriveMile(companyList.parallelStream().collect(summingDouble(NetcarStatisticsOrder::getDrive_mile_sum)));
            //总空驶里程
            orderStatisticsResult.setWaitMile(companyList.parallelStream().collect(summingDouble(NetcarStatisticsOrder::getWait_mile_sum)));
            //获取当前公司本月产生订单的车辆数量（对数据进行去重）
//            Long vehcleAmount = companyList.stream().distinct().count();
            Long vehicleAmount = companyList.stream().filter(distinctByKey(NetcarStatisticsOrder::getVEHICLE_NO)).count();
            //车均营运趟次
            orderStatisticsResult.setAverageCount((int) (orderStatisticsResult.getOrderCount() / vehicleAmount));
            //车均营运金额(月)
            orderStatisticsResult.setAveragePriceMM(orderStatisticsResult.getFactPrice() / vehicleAmount);
            //车均营运金额（日）
            orderStatisticsResult.setAveragePriceDD(orderStatisticsResult.getFactPrice() / vehicleAmount / 30);
            //车均营运时间
            orderStatisticsResult.setAverageTime((int) ((orderStatisticsResult.getDriveTime() + orderStatisticsResult.getWaitTime()) / vehicleAmount));
            //车均空驶时间
            orderStatisticsResult.setAverageTimeEmpty((int) (orderStatisticsResult.getWaitTime() / vehicleAmount));
            //车均营运里程
            orderStatisticsResult.setAverageDriveMile((orderStatisticsResult.getDriveMile() + orderStatisticsResult.getWaitMile()) / vehicleAmount);
            orderStatisticsResult.setCompanyId(key);
            orderStatisticsResult.setCompanyName(companyServiceService.selectCompanyNameByCompanyId(key));
            resultList.add(orderStatisticsResult);
        }
        return resultList;
    }

    /**
     * 功能：获取指定时间段内的数据
     * 数据源：druid
     * 表名称：netcar_statistics_operatePay
     *
     * @param SDate
     * @param EDate
     * @return
     * @throws Exception
     */
    public JSONArray getorderData(String SDate, String EDate, String dateType) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COMPANY_ID,ON_AREA,VEHICLE_NO,drive_mile_sum,drive_time_sum,fact_price_sum,order_count,wait_mile_sum,wait_time_sum");
        if (StringUtils.isNotEmpty(dateType)) {
            //以“月”为单位获取数据
            if (StringUtils.equals(dateType, "year")) {
                builder.append(", TIME_FORMAT(__time,'yyyy-MM') as PAY_TIME ");
                builder.append("FROM netcar_statistics_operatePay WHERE");
                if (StringUtils.isNotEmpty(EDate)) {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM') >= '").append(SDate).append("'");
                    builder.append(" and TIME_FORMAT(__time,'yyyy-MM') <= '").append(EDate).append("'");
                } else {
                    builder.append(" TIME_FORMAT(__time,'yyyy') = '").append(SDate).append("'");
                }
            }
            //以“天”为单位获取数据
            if (StringUtils.equals(dateType, "month")) {
                builder.append(", TIME_FORMAT(__time,'yyyy-MM-dd') as PAY_TIME ");
                builder.append("FROM netcar_statistics_operatePay WHERE");
                if (StringUtils.isNotEmpty(EDate)) {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM-dd') >= '").append(SDate).append("'");
                    builder.append(" and TIME_FORMAT(__time,'yyyy-MM-dd') <= '").append(EDate).append("'");
                } else {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM') = '").append(SDate).append("'");
                }
            }
            //以“小时”为单位获取数据
            if (StringUtils.equals(dateType, "day")) {
                builder.append(", TIME_FORMAT(__time,'yyyy-MM-dd HH') as PAY_TIME ");
                builder.append("FROM netcar_statistics_operatePay WHERE");
                if (StringUtils.isNotEmpty(EDate)) {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM-dd HH') >= '").append(SDate).append("'");
                    builder.append(" and TIME_FORMAT(__time,'yyyy-MM-dd HH') <= '").append(EDate).append("'");
                } else {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM-dd') = '").append(SDate).append("'");
                }
            }
        } else {
            builder.append(", TIME_FORMAT(__time,'yyyy-MM-dd') as PAY_TIME ");
        }
        builder = stringJoint(builder);
        builder = builder.append(getVehicleNo());
        log.info("订单查询sql" + builder);
        return httpUtils.doPostSqlUrl("sql", String.valueOf(builder));
    }


    /**
     * 驾驶员订单撤销统计报表
     *
     * @param request
     * @param startTime
     * @param endTime
     * @param dateType
     * @return
     */
    @GetMapping("/revocation")
    public MessageResp getDriverRevocation(HttpServletRequest request,
                                           @RequestParam String startTime,
                                           @RequestParam(required = false) String endTime,
                                           @RequestParam String dateType,
                                           @RequestParam String revocationType) {
        MessageResp messageResp = new MessageResp();
        try {
            String sql = "select COMPANY_ID, ADDRESS, REVOCATION_TYPE, revocation_count";
            String table = "netcar_revocation_out";
            JSONArray getdata = getRevocationData(sql, table, startTime, endTime, dateType);
            List<RevocationResult> revocationList = JSONObject.parseArray(getdata.toJSONString(), RevocationResult.class);
            //过滤多余数据
            revocationList = filtration(revocationList, revocationType);
            Map<String, List<RevocationResult>> companyList = revocationList.stream().collect(Collectors.groupingBy(RevocationResult::getCOMPANY_ID));
            List<Map<String, Object>> result = new ArrayList<>();
            companyList.keySet().forEach(companyId -> {
                List<RevocationResult> revocations = companyList.get(companyId);
                //初始化时间集合
                List<Map<String, Object>> companyMap = getInit(startTime, dateType);
                Map<String, List<RevocationResult>> revocationMap = revocations.stream().collect(Collectors.groupingBy(RevocationResult::getREVOCATION_TIME));
                Map<String, Object> company = new HashMap<>();
                company.put("companyName", companyServiceService.selectCompanyNameByCompanyId(companyId));
                revocationMap.keySet().forEach(time -> {
                    List<RevocationResult> data = revocationMap.get(time);
                    int count = data.stream().collect(summingInt(RevocationResult::getRevocation_count));
                    Map<String, Object> map = new HashMap<>();
                    map.put("time", time);
                    map.put("count", count);
                    int subscript = Integer.valueOf(time.substring(time.length() - 2, time.length()));
                    companyMap.set(subscript - 1, map);
                });
                company.put("list", companyMap);
                result.add(company);
            });
            messageResp.setData(result);
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
     * 初始化 返回的时间集合（保证返回数据的时间顺序，以及数据的完整性）
     *
     * @param date
     * @param type
     * @return
     */
    private List<Map<String, Object>> getInit(String date, String type) {
        List<Map<String, Object>> resultlist = null;
        switch (type) {
            case "year":
                resultlist = Arrays.asList(new Map[12]);
                break;

            case "month":
                resultlist = Arrays.asList(new Map[30]);
                break;

            case "day":
                resultlist = Arrays.asList(new Map[24]);
                break;
        }
        int i = 1;
        for (Map result : resultlist) {
            Map<String, Object> map = new HashMap<>();
            if (i < 10){
                map.put("time", date + "-" + "0"+i);
            }else {
                map.put("time", date + "-" + i);//时间
            }
            resultlist.set(i - 1, map);
            i++;
        }
        return resultlist;
    }


    /**
     * 过滤多余数据
     * revocationTypr为以下值时：
     * 1：乘客
     * 2：驾驶员
     * 3：平台
     *
     * @param revocationList
     * @param revocationTypr
     * @return
     */
    private List<RevocationResult> filtration(List<RevocationResult> revocationList, String revocationTypr) {
        List<RevocationResult> result = new ArrayList<>();
        switch (revocationTypr) {
            case "1":
                result = revocationList.stream().filter(revocation -> null != revocation.getREVOCATION_TYPE() && 1 == revocation.getREVOCATION_TYPE()  | 4 == revocation.getREVOCATION_TYPE()).collect(Collectors.toList());
                break;
            case "2":
                result = revocationList.stream().filter(revocation -> null != revocation.getREVOCATION_TYPE() && 2 == revocation.getREVOCATION_TYPE() | 5 == revocation.getREVOCATION_TYPE()).collect(Collectors.toList());
                break;
            case "3":
                result = revocationList.stream().filter(revocation -> null != revocation.getREVOCATION_TYPE() && 3 == revocation.getREVOCATION_TYPE()).collect(Collectors.toList());
                break;
        }
        return result;
    }


    public JSONArray getRevocationData(String sql, String table, String SDate, String EDate, String dateType) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(sql);
        if (StringUtils.isNotEmpty(dateType)) {
            //以“月”为单位获取数据
            if (StringUtils.equals(dateType, "year")) {
                builder.append(", TIME_FORMAT(__time,'yyyy-MM') as REVOCATION_TIME ");
                builder.append("FROM " + table + " WHERE");
                if (StringUtils.isNotEmpty(EDate)) {
                    builder.append(" TIME_FORMAT(__time,'yyyy') >= '").append(SDate).append("'");
                    builder.append(" and TIME_FORMAT(__time,'yyyy') <= '").append(EDate).append("'");
                } else {
                    builder.append(" TIME_FORMAT(__time,'yyyy') = '").append(SDate).append("'");
                }
            }
            //以“天”为单位获取数据
            if (StringUtils.equals(dateType, "month")) {
                builder.append(", TIME_FORMAT(__time,'yyyy-MM-dd') as REVOCATION_TIME ");
                builder.append("FROM " + table + " WHERE");
                if (StringUtils.isNotEmpty(EDate)) {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM') >= '").append(SDate).append("'");
                    builder.append(" and TIME_FORMAT(__time,'yyyy-MM') <= '").append(EDate).append("'");
                } else {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM') = '").append(SDate).append("'");
                }
            }
            //以“小时”为单位获取数据
            if (StringUtils.equals(dateType, "day")) {
                builder.append(", TIME_FORMAT(__time,'yyyy-MM-dd HH') as REVOCATION_TIME ");
                builder.append("FROM " + table + " WHERE");
                if (StringUtils.isNotEmpty(EDate)) {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM-dd') >= '").append(SDate).append("'");
                    builder.append(" and TIME_FORMAT(__time,'yyyy-MM-dd') <= '").append(EDate).append("'");
                } else {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM-dd') = '").append(SDate).append("'");
                }
            }
        } else {
            builder.append(", TIME_FORMAT(__time,'yyyy-MM-dd') as REVOCATION_TIME ");
        }
        builder = stringJoint(builder);
//        builder = builder.append(getVehicleNo());
        log.info("撤销记录查询sql" + builder);
        return httpUtils.doPostSqlUrl("sql", String.valueOf(builder));
    }
}
