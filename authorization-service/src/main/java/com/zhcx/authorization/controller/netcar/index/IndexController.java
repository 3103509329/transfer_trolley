package com.zhcx.authorization.controller.netcar.index;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhcx.authorization.controller.netcar.base.RoleAuthenticationUtils;
import com.zhcx.authorization.utils.*;
import com.zhcx.netcarbasic.facade.netcar.BaseDictionaryService;
import com.zhcx.netcarbasic.facade.netcar.CompanyServiceService;
import com.zhcx.netcarbasic.pojo.DruidEntity.NetcarStatisticsDistance;
import com.zhcx.netcarbasic.pojo.DruidEntity.NetcarStatisticsOrder;
import com.zhcx.netcarbasic.pojo.DruidEntity.OrderStatisticsComplaint;
import com.zhcx.netcarbasic.pojo.netcar.NetcarStatistics;
import com.zhcx.netcarbasic.pojo.netcar.druid.DruidNetcarOrder;
import com.zhcx.platformtonet.facade.YunZhengCompanyService;
import com.zhcx.platformtonet.facade.YunZhengDriverService;
import com.zhcx.platformtonet.facade.YunZhengVehicleService;
import com.zhcx.platformtonet.facade.YunzhengAmountService;
import com.zhcx.platformtonet.pojo.base.YunZhengVehicle;
import com.zhcx.platformtonet.pojo.base.YunzhengAmount;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.summingLong;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/14 15:05
 **/
@RestController
@RequestMapping("/netcar/index")
@Api(value = "首页企业信息统计", tags = "首页企业信息统计")
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private BaseDictionaryService dictionaryService;

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private CompanyServiceService companyServiceService;

    @Autowired
    private YunZhengCompanyService yunZhengCompanyService;

    @Autowired
    private YunZhengVehicleService yunZhengVehicleService;

    @Autowired
    private YunZhengDriverService yunZhengDriverService;

    @Autowired
    private YunzhengAmountService yunzhengAmountService;

    private String ids;


    /**
     * 查询统计信息（公司/车辆/驾驶员数量）
     *
     * @param request
     * @return
     */
    @GetMapping("/statisticData")
    public MessageResp queryStatisticData(HttpServletRequest request) {
        MessageResp messageResp = new MessageResp();
        try {
            String companyId = roleAuthenticationUtils.getCompanyId(request, null);
            NetcarStatistics netcarStatistics = new NetcarStatistics();

            Long companyCount = yunZhengCompanyService.selectCountByCompanyId(companyId);
            Long vehicleCount = yunZhengVehicleService.selectCountByCompanyId(companyId);
            Long driverCount = yunZhengDriverService.selectCountByCompanyId(companyId);
            netcarStatistics.setCompanyCount(companyCount);
            netcarStatistics.setVehicleCount(vehicleCount);
            netcarStatistics.setDriverCount(driverCount);

            /********************* 当月数据统计 *******************/
            /**
             * 当月总成功订单数
             */
            //修改：基于netcar_statistics_operatePay
            String currentMonth = DateUtil.getYMDFormat(new Date()).substring(0, 7);
            List<NetcarStatisticsOrder> orderList = getorderData(currentMonth, null, "month");

            Long total = orderList.stream().collect(summingLong(NetcarStatisticsOrder::getOrder_count));
            netcarStatistics.setOrderCount(total);
            messageResp.setData(netcarStatistics);
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    /**
     * 根据城市查询数据 此接口废除
     *
     * @param request
     * @return
     */
    @RequestMapping("/distinctData")
    public MessageResp queryDataByCity(HttpServletRequest request) {
        MessageResp messageResp = new MessageResp();
        try {
            List<NetcarStatistics> netcarStatisticsList = Lists.newArrayList();
            messageResp.setData(netcarStatisticsList);
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    /**
     * 查询当月订单和营收
     *
     * @param request
     * @return
     */
    @GetMapping("/monthOrderInfo")
    public MessageResp queryMonthOrderInfo(HttpServletRequest request) {
        MessageResp messageResp = new MessageResp();
        try {
            /********************* 当月数据统计 *******************/
            //修改：基于netcar_statistics_operatePay
            String currentMonth = DateUtil.getYMDFormat(new Date()).substring(0, 7);
            List<NetcarStatisticsOrder> orderList = getorderData(currentMonth, null, "month");

            /**
             * 当月总成功订单数
             */
            Long total = orderList.stream().collect(summingLong(NetcarStatisticsOrder::getOrder_count));

            /**
             * 当月营运金额
             */
            Double price = orderList.stream().collect(summingDouble(NetcarStatisticsOrder::getFact_price_sum));

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("monthOrderTotal", total);
            resultMap.put("monthRevenueTotal", price);
            messageResp.setResMap(resultMap);
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    @GetMapping("/lastDayOrderData")
    public MessageResp queryLastDayOrderData(HttpServletRequest request) {
        MessageResp messageResp = new MessageResp();
        try {
            Map<String, Object> map = new HashMap<>();
            /********************* 昨天数据统计 *******************/

            //修改：基于netcar_statistics_operatePay
            String lastOneDay = DateUtil.getYMDFormat(DateUtil.getSomeDay(-1));
            List<NetcarStatisticsOrder> yesterdayList = getorderData(lastOneDay, null, "day");
            /**
             * 昨天运营车辆统计
             */
            Long lastDayVehicleTotal = yesterdayList.stream().filter(distinctByKey(NetcarStatisticsOrder::getVEHICLE_NO)).count();
            /**
             * 昨天运营订单
             */
            Integer lastDayOrderTotal = yesterdayList.stream().collect(summingInt(NetcarStatisticsOrder::getOrder_count));
            /**
             * 昨天营运金额
             */
            Double lastDayRevenueTotal = yesterdayList.stream().collect(summingDouble(NetcarStatisticsOrder::getFact_price_sum));

            /********************* 前天数据统计 *******************/

            String lastTwoDay = DateUtil.getYMDFormat(DateUtil.getSomeDay(-2));
            List<NetcarStatisticsOrder> anteayerList = getorderData(lastTwoDay, null, "day");
            /**
             * 前天运营订单
             */
            Integer lastTwoDayOrderTotal = anteayerList.stream().collect(summingInt(NetcarStatisticsOrder::getOrder_count));
            /**
             * 前天营运金额
             */
            Double lastTwoDayRevenueTotal = anteayerList.stream().collect(summingDouble(NetcarStatisticsOrder::getFact_price_sum));

            map.put("lastDayVehicleTotal", lastDayVehicleTotal);
            map.put("lastDayOrderTotal", lastDayOrderTotal);
            map.put("lastDayRevenueTotal", lastDayRevenueTotal);
            map.put("lastTwoDayOrderTotal", lastTwoDayOrderTotal);
            map.put("lastTwoDayRevenueTotal", lastTwoDayRevenueTotal);

            messageResp.setResMap(map);
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    /**
     * 前七天营运订单数据
     *
     * @param request
     * @return
     */
    @GetMapping("/sevenDayOrderData")
    public MessageResp querySevenDayOrderData(HttpServletRequest request,
                                              @RequestParam(value = "startTime", required = false) Long startTime,
                                              @RequestParam(value = "endTime", required = false) Long endTime) {

        MessageResp messageResp = new MessageResp();
        try {
            String start;
            String end;
            Date startDate = DateUtil.getSomeDay(-7);
            Date endDate = DateUtil.getSomeDay(-1);
            if (null != startTime) {
                startDate = new Date(startTime);
                endDate = new Date(endTime);
            }
            Date finalStartDate = startDate;
            Date finalEndDate = endDate;
            start = DateUtil.getYMDFormat(startDate);
            end = DateUtil.getYMDFormat(endDate);
            List<NetcarStatisticsOrder> anteayerList = getorderData(start, end, "day");
            //将数据基于公司分组
            List<Map<String, Object>> resultList = new ArrayList<>();
            Map<String, List<NetcarStatisticsOrder>> listMap = anteayerList.stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getCOMPANY_ID));

            listMap.keySet().forEach(companyId -> {
                List<NetcarStatisticsOrder> orders = listMap.get(companyId);
                Map<String, Object> companyMap = new HashMap<>();
                companyMap.put("name", companyServiceService.selectCompanyNameByCompanyId(companyId));
                companyMap.put("id", companyId);
                Map<String, List<NetcarStatisticsOrder>> time = orders.stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getPAY_TIME));

                List<Map<String, Object>> dataList = StatisticalUtil.getSomeDayListOfMonth(finalStartDate, finalEndDate);
                time.keySet().forEach(payTime -> {
                    //获取每个时间段的数据集合
                    List<NetcarStatisticsOrder> timeOrder = time.get(payTime);
                    //获取每个时间段的车辆数（去重）
                    Integer total = timeOrder.stream().collect(summingInt(NetcarStatisticsOrder::getOrder_count));

                    dataList.forEach(map -> {
                        String moment = String.valueOf(map.get("moment"));
                        if (StringUtils.equals(moment, payTime)) {
                            map.put("total", total);
                        }
                    });

                });
                companyMap.put("data", dataList);
                resultList.add(companyMap);
            });
            if (listMap.size() == 0) {
                Map<String, Object> companyMap = new HashMap<>();
                List<Map<String, Object>> dataList = StatisticalUtil.getSomeDayListOfMonth(finalStartDate, finalEndDate);
                companyMap.put("data", dataList);
                resultList.add(companyMap);
            }
            messageResp.setData(resultList);
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    /**
     * 前七天营运收入数据
     *
     * @param request
     * @return
     */
    @GetMapping("/sevenDayRevenueData")
    public MessageResp querySevenDayRevenueData(HttpServletRequest request,
                                                @RequestParam(value = "startTime", required = false) Long startTime,
                                                @RequestParam(value = "endTime", required = false) Long endTime) {

        MessageResp messageResp = new MessageResp();
        try {
            String start;
            String end;
            Date startDate = DateUtil.getSomeDay(-7);
            Date endDate = DateUtil.getSomeDay(-1);
            if (null != startTime) {
                startDate = new Date(startTime);
                endDate = new Date(endTime);
            }
            Date finalStartDate = startDate;
            Date finalEndDate= endDate;
            start = DateUtil.getYMDFormat(startDate);
            end = DateUtil.getYMDFormat(endDate);
            List<NetcarStatisticsOrder> anteayerList = getorderData(start, end, "day");
            //将数据基于公司分组
            List<Map<String, Object>> resultList = new ArrayList<>();
            Map<String, List<NetcarStatisticsOrder>> listMap = anteayerList.stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getCOMPANY_ID));
            listMap.keySet().forEach(companyId -> {
                List<NetcarStatisticsOrder> orders = listMap.get(companyId);
                Map<String, Object> companyMap = new HashMap<>();
                companyMap.put("name", companyServiceService.selectCompanyNameByCompanyId(companyId));
                companyMap.put("id", companyId);
                Map<String, List<NetcarStatisticsOrder>> time = orders.stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getPAY_TIME));
                List<Map<String, Object>> dataList = StatisticalUtil.getSomeDayListOfMonth(finalStartDate, finalEndDate);
                time.keySet().forEach(payTime -> {
                    //获取每个时间段的数据集合
                    List<NetcarStatisticsOrder> timeOrder = time.get(payTime);
                    Double total = timeOrder.stream().collect(summingDouble(NetcarStatisticsOrder::getFact_price_sum));

                    dataList.forEach(map -> {
                        String moment = String.valueOf(map.get("moment"));
                        if (StringUtils.equals(moment, payTime)) {
                            map.put("total", total);
                        }
                    });
                });
                companyMap.put("data", dataList);
                resultList.add(companyMap);
            });
            if (listMap.size() == 0) {
                Map<String, Object> companyMap = new HashMap<>();
                List<Map<String, Object>> dataList = StatisticalUtil.getSomeDayListOfMonth(finalStartDate, finalEndDate);
                companyMap.put("data", dataList);
                resultList.add(companyMap);
            }
            messageResp.setData(resultList);
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    /**
     * 车辆运营里程统计
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "车辆运营里程统计", notes = "")
    @GetMapping("/sevenDayMileData")
    public MessageResp querySevenDayMileData(HttpServletRequest request,
                                             @RequestParam(value = "startTime", required = false) Long startTime,
                                             @RequestParam(value = "endTime", required = false) Long endTime) {
        MessageResp messageResp = new MessageResp();
        try {
            String start;
            String end;
            Date startDate = DateUtil.getSomeDay(-7);
            Date endDate = DateUtil.getSomeDay(-1);
            if (null != startTime) {
                startDate = new Date(startTime);
                endDate = new Date(endTime);
            }
            Date finalStartDate = startDate;
            Date finalEndDate = endDate;

            start = DateUtil.getYMDFormat(startDate);
            end = DateUtil.getYMDFormat(endDate);
            //获取总载客里程
            List<NetcarStatisticsOrder> anteayerList = getorderData(start, end, "day");
            //将数据基于公司分组
            List<Map<String, Object>> result = new ArrayList<>();
            Map<String, List<NetcarStatisticsOrder>> listMap = anteayerList.stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getCOMPANY_ID));
            listMap.keySet().forEach(companyId -> {
                List<NetcarStatisticsOrder> orders = listMap.get(companyId);
                Map<String, List<NetcarStatisticsOrder>> time = orders.stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getPAY_TIME));
                String companyName = companyServiceService.selectCompanyNameByCompanyId(companyId);
                Map<String, Object> companyMap = new HashMap<>();
                companyMap.put("name", companyName);
                companyMap.put("id", companyId);
                List<Map<String, Object>> dataList = StatisticalUtil.getSomeDayListOfMonth(finalStartDate, finalEndDate);
                time.keySet().forEach(key -> {
                    //获取每个时间段的数据集合
                    List<NetcarStatisticsOrder> timeOrder = time.get(key);
                    Double driveMile = timeOrder.stream().collect(summingDouble(NetcarStatisticsOrder::getDrive_mile_sum));
                    Double waitMile = timeOrder.stream().collect(summingDouble(NetcarStatisticsOrder::getWait_mile_sum));
                    dataList.forEach(map -> {
                        String moment = String.valueOf(map.get("moment"));
                        if (StringUtils.equals(moment, key)) {
                            map.put("total", driveMile + waitMile);
                        }
                    });
                });
                companyMap.put("data", dataList);
                result.add(companyMap);
            });
            if (listMap.size() == 0) {
                Map<String, Object> companyMap = new HashMap<>();
                List<Map<String, Object>> dataList = StatisticalUtil.getSomeDayListOfMonth(finalStartDate, finalEndDate);
                companyMap.put("data", dataList);
                result.add(companyMap);
            }
            messageResp.setData(result);
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取车辆运营数据统计结果异常，异常详情：{}", e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("查询异常");
        }
        return messageResp;
    }


    /**
     * 一个月投诉统计
     *
     * @param dataType ： hour day month year
     * @param request
     * @return
     */
    @GetMapping("/complaintData")
    public MessageResp queryComplaintData(HttpServletRequest request, @RequestParam(defaultValue = "month", required = false) String dataType,
                                          @RequestParam(required = false) String startTime) {

        MessageResp messageResp = new MessageResp();
        try {
            if (StringUtils.isBlank(startTime)) {
                startTime = DateUtil.getYMDFormat(new Date()).substring(0, 7);
            }
            JSONArray complaint = getcomplaintData(startTime, null, dataType);
            List<OrderStatisticsComplaint> complaintList = JSONObject.parseArray(complaint.toJSONString(), OrderStatisticsComplaint.class);
            Map<String, List<OrderStatisticsComplaint>> cpmpanys = complaintList.stream().collect(Collectors.groupingBy(OrderStatisticsComplaint::getCompanyId));
            Map<String, Object> resultMap = new HashMap<>();
            List<Map> mapList = Lists.newArrayList();
            cpmpanys.keySet().stream().forEach(companyId -> {
                List<OrderStatisticsComplaint> complaints = cpmpanys.get(companyId);
                //计算一个月的投诉（按公司聚合）
                int amount = complaints.stream().collect(summingInt(OrderStatisticsComplaint::getComplaintCount));
                Map<String, Object> complaintMap = new HashMap<>();
                complaintMap.put("id", companyId);
                complaintMap.put("name", companyServiceService.selectCompanyNameByCompanyId(companyId));
                complaintMap.put("total", amount);
                mapList.add(complaintMap);
            });
            resultMap.put("mapList", mapList);
            resultMap.put("totalComplaint", complaintList.stream().collect(summingInt(OrderStatisticsComplaint::getComplaintCount)));
            messageResp.setResMap(resultMap);
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;

    }

    @Autowired
    private StatisticalUtil statisticalUtil;

    /**
     * 投诉热点分析
     *
     * @param type    ： hour day month year
     * @param request
     * @return
     */
    @GetMapping("/hotComplaint")
    public MessageResp queryHotComplaintData(HttpServletRequest request, @RequestParam(defaultValue = "day", required = false) String type,
                                             @RequestParam String startTime) {

        MessageResp messageResp = new MessageResp();
        try {
            JSONArray complaint = getcomplaintData(startTime, null, type);
            List<OrderStatisticsComplaint> complaintList = JSONObject.parseArray(complaint.toJSONString(), OrderStatisticsComplaint.class);
            Map<String, List<OrderStatisticsComplaint>> complaintMaps = complaintList.stream().collect(Collectors.groupingBy(OrderStatisticsComplaint::getCompanyId));
            List<Map> resultList = Lists.newArrayList();
            complaintMaps.keySet().stream().forEach(companyId -> {
                Map<String, Object> complaintMap = new HashMap<>();
                complaintMap.put("id", companyId);
                complaintMap.put("name", companyServiceService.selectCompanyNameByCompanyId(companyId));
                List<OrderStatisticsComplaint> complaints = complaintMaps.get(companyId);
                Map<String, List<OrderStatisticsComplaint>> maps = complaints.stream().collect(Collectors.groupingBy(OrderStatisticsComplaint::getComplaintTime));

                List<Map<String, Object>> dataList = statisticalUtil.dataInit(startTime, type);
                maps.keySet().stream().forEach(complaintTime -> {
                    int total = maps.get(complaintTime).stream().mapToInt(OrderStatisticsComplaint::getComplaintCount).sum();
                    dataList.forEach(map -> {
                        String moment = String.valueOf(map.get("moment"));
                        if (StringUtils.equals(moment, complaintTime)) {
                            map.put("total", total);
                        }
                    });
                });
                complaintMap.put("data", dataList);
                resultList.add(complaintMap);
            });
            messageResp.setData(resultList);
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;

    }


    /**
     * 运营公里产值统计
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "运营公里产值统计", notes = "")
    @GetMapping("/sevenDayRevenuePerMileData")
    public MessageResp querySevenDayRevenuePerMileData(HttpServletRequest request,
                                                       @RequestParam(value = "startTime", required = false) Long startTime,
                                                       @RequestParam(value = "endTime", required = false) Long endTime) {
        MessageResp messageResp = new MessageResp();
        try {
            Date startDate = DateUtil.getSomeDay(-7);
            Date endDate = DateUtil.getSomeDay(-1);
            if (null != startTime) {
                startDate = new Date(startTime);
                endDate = new Date(endTime);
            }
            Date finalStartDate = startDate;
            Date finalEndDate = endDate;

            String start = DateUtil.getYMDFormat(startDate);
            String end = DateUtil.getYMDFormat(endDate);
            List<NetcarStatisticsOrder> anteayerList = getorderData(start, end, "day");
            //将数据基于公司分组
            List<Map<String, Object>> result = new ArrayList<>();
            Map<String, List<NetcarStatisticsOrder>> listMap = anteayerList.stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getCOMPANY_ID));

            listMap.keySet().forEach(companyId -> {
                List<NetcarStatisticsOrder> orders = listMap.get(companyId);
                Map<String, List<NetcarStatisticsOrder>> time = orders.stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getPAY_TIME));
                String companyName = companyServiceService.selectCompanyNameByCompanyId(companyId);
                Map<String, Object> companyMap = new HashMap<>();
                companyMap.put("id", companyId);
                companyMap.put("name", companyName);

                List<Map<String, Object>> dateList = StatisticalUtil.getSomeDayListOfMonth(finalStartDate, finalEndDate);
                //基于时间排序
                List<String> data = time.keySet().stream().sorted(Comparator.comparing(String::hashCode)).collect(Collectors.toList());
                data.forEach(payTime -> {
                    List<NetcarStatisticsOrder> timeOrder = time.get(payTime);
                    //金额
                    Double totalFee = timeOrder.stream().collect(summingDouble(NetcarStatisticsOrder::getFact_price_sum));
                    //载客里程
                    Double driveMile = timeOrder.stream().collect(summingDouble(NetcarStatisticsOrder::getDrive_mile_sum));
                    //空驶里程
                    Double waitMile = timeOrder.stream().collect(summingDouble(NetcarStatisticsOrder::getWait_mile_sum));
                    dateList.forEach(map -> {
                        String moment = String.valueOf(map.get("moment"));
                        if (StringUtils.equals(moment, payTime)) {
                            map.put("total", BigDecimalUtil.div(totalFee,((driveMile + waitMile))));
                        }

                    });
                });
                companyMap.put("data", dateList);
                result.add(companyMap);
            });
            if (listMap.size() == 0) {
                Map<String, Object> companyMap = new HashMap<>();
                List<Map<String, Object>> dataList = StatisticalUtil.getSomeDayListOfMonth(finalStartDate, finalEndDate);
                companyMap.put("data", dataList);
                result.add(companyMap);
            }
            messageResp.setData(result);
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取车辆运营数据统计结果异常，异常详情：{}", e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("查询异常");
        }
        return messageResp;
    }

    /**
     * 首页大屏当前月份运距数据分布
     *
     * @param request
     * @return
     */
    @GetMapping("/monthDistanceData")
    public MessageResp queryMonthDistanceData(HttpServletRequest request,
                                              @RequestParam(value = "startTime", required = false) String startTime) {

        MessageResp messageResp = new MessageResp();
        try {
            StringBuilder orderSql = new StringBuilder();
            String start;
            if (StringUtils.isNotBlank(startTime)) {
                start = startTime;
            } else {
                start = DateUtil.getCurrMonth();
            }
            orderSql.append("select HANDLING_TYPE type,sum(operate_count) as total from netcar_statistics_haul_distance_output where")
                    .append(" TIME_FORMAT(__time,'yyyy-MM') = '").append(start).append("' ");
            orderSql = stringJoint(orderSql);

            orderSql.append(" group by HANDLING_TYPE ");
            JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", orderSql.toString());
            List<Map<String, Object>> resultList = Lists.newArrayList();
            jsonArray.stream().forEach(obj -> {
                JSONObject jsonObject = (JSONObject) obj;
                Map<String, Object> map = Maps.newHashMap();
                map.put("total", jsonObject.get("total"));
                map.put("type", jsonObject.get("type"));
                resultList.add(map);
            });

            messageResp.setData(resultList);
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    /**
     * 运距分布
     *
     * @param request
     * @return
     */
    @GetMapping("/sevenDayDistanceData")
    public MessageResp querySevenDayDistanceData(HttpServletRequest request,
                                                 @RequestParam(value = "startTime", required = false) String startTime,
                                                 @RequestParam(value = "dateType", defaultValue = "month") String dateType) {

        MessageResp messageResp = new MessageResp();
        try {
            StringBuilder orderSql = new StringBuilder();
//            if (StringUtils.isBlank(startTime)) {
//                startTime = DateUtil.getCurrMonth();
//            }
            switch (dateType){
                case "year":
                    orderSql.append("select COMPANY_ID,HANDLING_TYPE type,sum(operate_count) as total from netcar_statistics_haul_distance_output where")
                            .append(" TIME_FORMAT(__time,'yyyy') = '").append(startTime).append("' ");
                    break;
                case "month":
                    orderSql.append("select COMPANY_ID,HANDLING_TYPE type,sum(operate_count) as total from netcar_statistics_haul_distance_output where")
                            .append(" TIME_FORMAT(__time,'yyyy-MM') = '").append(startTime).append("' ");
                    break;
                case "day":
                    orderSql.append("select COMPANY_ID,HANDLING_TYPE type,sum(operate_count) as total from netcar_statistics_haul_distance_output where")
                            .append(" TIME_FORMAT(__time,'yyyy-MM-dd') = '").append(startTime).append("' ");
                    break;
                default:
            }
            orderSql = stringJoint(orderSql);
            orderSql.append(" group by COMPANY_ID,HANDLING_TYPE ");
            JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", orderSql.toString());
            List<Map<String, Object>> resultList = Lists.newArrayList();
            jsonArray.stream().forEach(obj -> {
                JSONObject jsonObject = (JSONObject) obj;
                String corpId = jsonObject.getString("COMPANY_ID");
                String companyName = companyServiceService.selectCompanyNameByCompanyId(corpId);
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", jsonObject.getString("COMPANY_ID"));
                map.put("name", companyName);
                map.put("total", jsonObject.get("total"));
                map.put("type", jsonObject.get("type"));
                resultList.add(map);
            });

            messageResp.setData(resultList);
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    /**
     * 前七天空驶率数据
     *
     * @param request
     * @return
     */
    @GetMapping("/sevenDayEmptyDrive")
    public MessageResp querySevenDayEmptyDrive(HttpServletRequest request,
                                               @RequestParam(value = "startTime", required = false) Long startTime,
                                               @RequestParam(value = "endTime", required = false) Long endTime) {

        MessageResp messageResp = new MessageResp();
        try {
            String companyId = null;
            StringBuilder orderSql = new StringBuilder();
            Date startDate = DateUtil.getSomeDay(-7);
            Date endDate = DateUtil.getSomeDay(-1);
            if (null != startTime) {
                startDate = new Date(startTime);
                endDate = new Date(endTime);
            }
            Date finalStartDate = startDate;
            Date finalEndDate = endDate;
            String start = DateUtil.getYMDFormat(startDate);
            String end = DateUtil.getYMDFormat(endDate);
            orderSql.append("select COMPANY_ID as companyId,TIME_FORMAT(__time,'yyyy-MM-dd') moment, sum(wait_mile_sum) as waitMileSum,sum(drive_mile_sum) as driveMileSum from netcar_statistics_haul_distance_output where")
                    .append(" TIME_FORMAT(__time,'yyyy-MM-dd') >= '").append(start)
                    .append("' and TIME_FORMAT(__time,'yyyy-MM-dd') <= '").append(end).append("' ");
            if (StringUtils.isNotBlank(companyId)) {
                orderSql.append("and COMPANY_ID = '").append(companyId).append("'");
            }
            String ids = roleAuthenticationUtils.filterCompanyIdCondition();
            if (StringUtils.isNotBlank(ids)) {
                stringJoint(orderSql);
            }
            orderSql.append(" group by COMPANY_ID,TIME_FORMAT(__time,'yyyy-MM-dd')");
            JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", orderSql.toString());

            List<Map<String, Object>> resultList = Lists.newArrayList();
            List<NetcarStatisticsDistance> distanceList = JSONObject.parseArray(jsonArray.toJSONString(), NetcarStatisticsDistance.class);
            Map<String, List<NetcarStatisticsDistance>> listMap = distanceList.stream().collect(Collectors.groupingBy(NetcarStatisticsDistance::getCompanyId));

            listMap.keySet().stream().forEach(id -> {
                String companyName = companyServiceService.selectCompanyNameByCompanyId(id);
                Map<String, Object> resultMap = Maps.newHashMap();
                resultMap.put("id", id);
                resultMap.put("name", companyName);
                List<NetcarStatisticsDistance> list = listMap.get(id);
                List<Map<String, Object>> dataList = StatisticalUtil.getSomeDayListOfMonth(finalStartDate, finalEndDate);

                list.forEach(item -> {
                    dataList.forEach(map -> {
                        String moment = String.valueOf(map.get("moment"));
                        if (StringUtils.equals(moment, item.getMoment())) {
                            map.put("total", BigDecimalUtil.div(item.getWaitMileSum(),(item.getDriveMileSum() + item.getWaitMileSum())));
                        }
                    });

                });
                resultMap.put("data", dataList);
                resultList.add(resultMap);
            });
            if (listMap.size() == 0) {
                Map<String, Object> companyMap = new HashMap<>();
                List<Map<String, Object>> dataList = StatisticalUtil.getSomeDayListOfMonth(finalStartDate, finalEndDate);
                companyMap.put("data", dataList);
                resultList.add(companyMap);
            }
            messageResp.setData(resultList);
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }


    /**
     * @param request
     * @param type    统计维度(year,month,day)
     * @param time
     * @return
     */
    @ApiOperation(value = "运力指标统计", notes = "")
    @GetMapping("/emptyandcarry/{type}/{time}")
    public MessageResp getEmptyAndCarry(HttpServletRequest request, @PathVariable("type") String type, @PathVariable("time") String time) {
        MessageResp messageResp = new MessageResp();
        try {
            switch (type) {
                case "year":
                    time = time.substring(0, 4);
                    break;
                case "month":
                    time = time.substring(0, 7);
                    break;
                case "day":
                    time = time.substring(0, 10);
                    break;
                default:
            }
            //获取总成功量
            JSONArray jsonArray = getemptyandcarryData(time, null, type);
            List<NetcarStatisticsOrder> orderList = JSONObject.parseArray(jsonArray.toJSONString(), NetcarStatisticsOrder.class);
            Map<String, List<NetcarStatisticsOrder>> cpmpanys = orderList
                    .stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getCOMPANY_ID));

            List<Object> companyList = new ArrayList<>();

            String finalTime = time;
            cpmpanys.keySet().stream().forEach(company -> {
                //车辆总数
                Long number = yunzhengAmountService.selectByTime(company,finalTime,type);
                //基于时间分组企业的订单数据
                Map<String, List<NetcarStatisticsOrder>> orderByTime = cpmpanys.get(company)
                        .stream().collect(Collectors.groupingBy(NetcarStatisticsOrder::getPAY_TIME));

                Map<String, Object> map = new HashMap<>();
                List<Map<String, Object>> timeList = getInit(finalTime, type);

                map.put("companyId", company);
                map.put("companyName", companyServiceService.selectCompanyNameByCompanyId(company));
                //将时间相同的数据进行计算
                orderByTime.keySet().stream().forEach(payTime -> {
                    List<NetcarStatisticsOrder> orders = orderByTime.get(payTime);
                    //产生订单的车辆数
                    Long vehicles = orders.stream().filter(distinctByKey(NetcarStatisticsOrder::getVEHICLE_NO)).count();
                    Map<String, Object> companyAggregation = new HashMap<>();
                    if (number == 0) {
                        companyAggregation.put("carry", 0);
                    } else {
                        //重车率
                        companyAggregation.put("carry", (double) (vehicles / number));
                    }
                    //空车数
                    companyAggregation.put("empty", Math.toIntExact(number - vehicles));
                    //时间
                    companyAggregation.put("time", payTime);
                    companyAggregation.put("companyId", company);
                    //将数据放置在指定的位置（基于时间）
                    String data = payTime.substring(payTime.length() - 2, payTime.length());
                    timeList.set(Integer.valueOf(data) - 1, companyAggregation);
                });
                map.put("list", timeList);
                companyList.add(map);
            });


            messageResp.setData(companyList);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("企业营运订单数据统计结果异常，异常详情：{}", e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("查询异常");
        }
        return messageResp;
    }


    /**
     * 获取当前时间，前number个小时,从0点开始
     *
     * @param number
     * @return
     */
    public static List<Map<String, Object>> getPreDayFromNow(Integer number) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        List<String> list = new ArrayList();

        Date current = DateUtil.getSomeDay(-1);
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < number; i++) {
            list.add(DateUtil.getYMDFormat(current));
            calendar.setTime(current);
            if (calendar.get(Calendar.DATE) == 0) {
                break;
            }
            calendar.add(Calendar.DATE, -1);
            current = calendar.getTime();
        }
        for (int i = list.size(); i > 0; i--) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("total", 0);
            result.put("moment", list.get(i - 1));
            resultList.add(result);
        }
        return resultList;
    }


    public JSONArray getemptyandcarryData(String SDate, String EDate, String dateType) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COMPANY_ID,ON_AREA,VEHICLE_NO,drive_mile_sum,drive_time_sum,fact_price_sum,order_count,wait_mile_sum,wait_time_sum");
        if (StringUtils.isNotEmpty(dateType)) {
            //以“年”为单位获取数据
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
            //以“月”为单位获取数据
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
            //以“天”为单位获取数据
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
        System.out.println(builder);
        return httpUtils.doPostSqlUrl("sql", String.valueOf(builder));
    }


    /***********************分割****************************/

    /**
     * 功能：获取指定 时间 或 时间段 内的数据
     * 数据源：druid
     * 表名称：netcar_statistics_operatePay
     *
     * @param SDate
     * @param EDate
     * @return
     * @throws Exception
     */
    public List<NetcarStatisticsOrder> getorderData(String SDate, String EDate, String dateType) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COMPANY_ID,ON_AREA,VEHICLE_NO,drive_mile_sum,drive_time_sum,fact_price_sum,order_count,wait_mile_sum,wait_time_sum");
        if (StringUtils.isNotEmpty(dateType)) {
            //以“年”为单位获取数据
            if (StringUtils.equals(dateType, "year")) {
                builder.append(", TIME_FORMAT(__time,'yyyy') as PAY_TIME ");
                builder.append("FROM netcar_statistics_operatePay WHERE");
                if (StringUtils.isNotEmpty(EDate)) {
                    builder.append(" TIME_FORMAT(__time,'yyyy') >= '").append(SDate).append("'");
                    builder.append(" and TIME_FORMAT(__time,'yyyy') <= '").append(EDate).append("'");
                } else {
                    builder.append(" TIME_FORMAT(__time,'yyyy') = '").append(SDate).append("'");
                }
            }
            //以“月”为单位获取数据
            if (StringUtils.equals(dateType, "month")) {
                builder.append(", TIME_FORMAT(__time,'yyyy-MM') as PAY_TIME ");
                builder.append("FROM netcar_statistics_operatePay WHERE");
                if (StringUtils.isNotEmpty(EDate)) {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM') >= '").append(SDate).append("'");
                    builder.append(" and TIME_FORMAT(__time,'yyyy-MM') <= '").append(EDate).append("'");
                } else {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM') = '").append(SDate).append("'");
                }
            }
            //以“天”为单位获取数据
            if (StringUtils.equals(dateType, "day")) {
                builder.append(", TIME_FORMAT(__time,'yyyy-MM-dd') as PAY_TIME ");
                builder.append("FROM netcar_statistics_operatePay WHERE");
                if (StringUtils.isNotEmpty(EDate)) {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM-dd') >= '").append(SDate).append("'");
                    builder.append(" and TIME_FORMAT(__time,'yyyy-MM-dd') <= '").append(EDate).append("'");
                } else {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM-dd') = '").append(SDate).append("'");
                }
            }
            //以“小时”为单位获取数据
            if (StringUtils.equals(dateType, "hour")) {
                builder.append(", TIME_FORMAT(__time,'yyyy-MM-dd HH') as PAY_TIME ");
                builder.append("FROM netcar_statistics_operatePay WHERE");
                if (StringUtils.isNotEmpty(EDate)) {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM-dd HH') >= '").append(SDate).append("'");
                    builder.append(" and TIME_FORMAT(__time,'yyyy-MM-dd HH') <= '").append(EDate).append("'");
                } else {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM-dd HH') = '").append(SDate).append("'");
                }
            }
        } else {
            builder.append(", TIME_FORMAT(__time,'yyyy-MM-dd') as PAY_TIME ");
        }
        builder = stringJoint(builder);
        builder = builder.append(getVehicleNo());
        System.out.println(builder);
        JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", String.valueOf(builder));
        List<NetcarStatisticsOrder> orderList = JSONObject.parseArray(jsonArray.toJSONString(), NetcarStatisticsOrder.class);
        return orderList;
    }


    /**
     * @param SDate
     * @param EDate
     * @param dateType
     * @return
     * @throws Exception
     */
    /**
     * @param SDate
     * @param EDate
     * @param dateType
     * @return
     * @throws Exception
     */
    public JSONArray getcomplaintData(String SDate, String EDate, String dateType) throws Exception {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COMPANY_ID as companyId,complaint_count as complaintCount");
        if (StringUtils.isNotEmpty(dateType)) {
            //以“年”为单位获取数据
            if (StringUtils.equals(dateType, "year")) {
                builder.append(", TIME_FORMAT(__time,'yyyy-MM') as complaintTime ");
                builder.append("FROM netcar_passenger_complaint WHERE");
                if (StringUtils.isNotEmpty(EDate)) {
                    builder.append(" TIME_FORMAT(__time,'yyyy') >= '").append(SDate).append("'");
                    builder.append(" and TIME_FORMAT(__time,'yyyy') <= '").append(EDate).append("'");
                } else {
                    builder.append(" TIME_FORMAT(__time,'yyyy') = '").append(SDate).append("'");
                }
            }
            //以“月”为单位获取数据
            if (StringUtils.equals(dateType, "month")) {
                builder.append(", TIME_FORMAT(__time,'yyyy-MM-dd') as complaintTime ");
                builder.append("FROM netcar_passenger_complaint WHERE");
                if (StringUtils.isNotEmpty(EDate)) {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM') >= '").append(SDate).append("'");
                    builder.append(" and TIME_FORMAT(__time,'yyyy-MM') <= '").append(EDate).append("'");
                } else {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM') = '").append(SDate).append("'");
                }
            }
            //以“天”为单位获取数据
            if (StringUtils.equals(dateType, "day")) {
                builder.append(", TIME_FORMAT(__time,'yyyy-MM-dd HH') as complaintTime ");
                builder.append("FROM netcar_passenger_complaint WHERE");
                if (StringUtils.isNotEmpty(EDate)) {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM-dd') >= '").append(SDate).append("'");
                    builder.append(" and TIME_FORMAT(__time,'yyyy-MM-dd') <= '").append(EDate).append("'");
                } else {
                    builder.append(" TIME_FORMAT(__time,'yyyy-MM-dd') = '").append(SDate).append("'");
                }
            }
        } else {
            builder.append(", TIME_FORMAT(__time,'yyyy-MM-dd') as complaintTime ");
        }
        builder = stringJoint(builder);
        System.out.println(builder);
        return httpUtils.doPostSqlUrl("sql", String.valueOf(builder));
    }


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
        if (vehicleNos.size()> 0) {
            builder.append("and VEHICLE_NO in (");
            for (YunZhengVehicle vehicleNo : vehicleNos){
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

    private List<Map<String, Object>> getInit(String SDate, String dateType) {
        List<Map<String, Object>> list = null;
        if (dateType.equals("month")) {
            list = Arrays.asList(new Map[30]);
            int i = 1;
            for (Map obj : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("carry", 0);//重车率
                map.put("empty", 0);//空车数
                map.put("time", SDate + "-" + i);//时间
                map.put("companyId", "");
                list.set(i - 1, map);
                i++;
            }
        }
        if (dateType.equals("day")) {
            list = Arrays.asList(new Map[24]);
            int i = 1;
            for (Map obj : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("carry", 0);//重车率
                map.put("empty", 0);//空车数
                map.put("time", SDate + "-" + i);//时间
                map.put("companyId", "");
                list.set(i - 1, map);
                i++;
            }
        }
        if (dateType.equals("year")) {
            list = Arrays.asList(new Map[12]);
            int i = 1;
            for (Map obj : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("carry", 0);//重车率
                map.put("empty", 0);//空车数
                map.put("time", SDate + "-" + i);//时间
                map.put("companyId", "");
                list.set(i - 1, map);
                i++;
            }
        }
        return list;
    }


}
