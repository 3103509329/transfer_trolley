package com.zhcx.netcar.netcarservice.controller.statistics;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.facade.statistics.StatisticalService;
import com.zhcx.netcar.facade.yunzheng.YunZhengCompanyService;
import com.zhcx.netcar.facade.yunzheng.YunZhengDriverService;
import com.zhcx.netcar.facade.yunzheng.YunZhengVehicleService;
import com.zhcx.netcar.facade.yunzheng.YunzhengAmountService;
import com.zhcx.netcar.netcarservice.utils.*;
import com.zhcx.netcar.pojo.base.NetcarStatistics;
import com.zhcx.netcar.pojo.statistical.KafkaNetcarPassengerComplaint;
import com.zhcx.netcar.pojo.statistical.KafkaNetcarStatisticsHaulDistanceOutput;
import com.zhcx.netcar.pojo.statistical.KafkaNetcarStatisticsOperatePay;
import com.zhcx.netcar.pojo.yuzheng.YunZhengVehicle;
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

import static java.util.stream.Collectors.*;

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
    private RoleAuthenticationUtils roleAuthenticationUtils;

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

    @Autowired
    private StatisticalService statisticalService;

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
            List<KafkaNetcarStatisticsOperatePay> orderList = getorderData(currentMonth, null, "month");

            Long total = orderList.stream().collect(summingLong(KafkaNetcarStatisticsOperatePay::getOrderCount));
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
            List<KafkaNetcarStatisticsOperatePay> orderList = getorderData(currentMonth, null, "month");

            /**
             * 当月总成功订单数
             */
            Long total = orderList.stream().collect(summingLong(KafkaNetcarStatisticsOperatePay::getOrderCount));

            /**
             * 当月营运金额
             */
            Double price = orderList.stream().collect(summingDouble(KafkaNetcarStatisticsOperatePay::getOrderCount));

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("monthOrderTotal", total);
            resultMap.put("monthRevenueTotal", price);
            messageResp.setData(resultMap);
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
            List<KafkaNetcarStatisticsOperatePay> yesterdayList = getorderData(lastOneDay, null, "day");
            /**
             * 昨天运营车辆统计
             */
            Long lastDayVehicleTotal = yesterdayList.stream().filter(distinctByKey(KafkaNetcarStatisticsOperatePay::getVehicleNo)).count();
            /**
             * 昨天运营订单
             */
            Integer lastDayOrderTotal = yesterdayList.stream().collect(summingInt(KafkaNetcarStatisticsOperatePay::getOrderCount));
            /**
             * 昨天营运金额
             */
            Double lastDayRevenueTotal = yesterdayList.stream().collect(summingDouble(KafkaNetcarStatisticsOperatePay::getOrderCount));

            /********************* 前天数据统计 *******************/

            String lastTwoDay = DateUtil.getYMDFormat(DateUtil.getSomeDay(-2));
            List<KafkaNetcarStatisticsOperatePay> anteayerList = getorderData(lastTwoDay, null, "day");
            /**
             * 前天运营订单
             */
            Integer lastTwoDayOrderTotal = anteayerList.stream().collect(summingInt(KafkaNetcarStatisticsOperatePay::getOrderCount));
            /**
             * 前天营运金额
             */
            Double lastTwoDayRevenueTotal = anteayerList.stream().collect(summingDouble(KafkaNetcarStatisticsOperatePay::getOrderCount));

            map.put("lastDayVehicleTotal", lastDayVehicleTotal);
            map.put("lastDayOrderTotal", lastDayOrderTotal);
            map.put("lastDayRevenueTotal", lastDayRevenueTotal);
            map.put("lastTwoDayOrderTotal", lastTwoDayOrderTotal);
            map.put("lastTwoDayRevenueTotal", lastTwoDayRevenueTotal);

            messageResp.setData(map);
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
            List<KafkaNetcarStatisticsOperatePay> anteayerList = getorderData(start, end, "day");
            //将数据基于公司分组
            List<Map<String, Object>> resultList = new ArrayList<>();
            Map<String, List<KafkaNetcarStatisticsOperatePay>> listMap = anteayerList.stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getCompanyId));

            listMap.keySet().forEach(companyId -> {
                List<KafkaNetcarStatisticsOperatePay> orders = listMap.get(companyId);
                Map<String, Object> companyMap = new HashMap<>();
                companyMap.put("name", companyServiceService.selectCompanyNameByCompanyId(companyId));
                companyMap.put("id", companyId);
                Map<String, List<KafkaNetcarStatisticsOperatePay>> time = orders.stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getTime));

                List<Map<String, Object>> dataList = StatisticalUtil.getSomeDayListOfMonth(finalStartDate, finalEndDate);
                time.keySet().forEach(payTime -> {
                    //获取每个时间段的数据集合
                    List<KafkaNetcarStatisticsOperatePay> timeOrder = time.get(payTime);
                    //获取每个时间段的车辆数（去重）
                    Integer total = timeOrder.stream().collect(summingInt(KafkaNetcarStatisticsOperatePay::getOrderCount));

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
            Date finalEndDate = endDate;
            start = DateUtil.getYMDFormat(startDate);
            end = DateUtil.getYMDFormat(endDate);
            List<KafkaNetcarStatisticsOperatePay> anteayerList = getorderData(start, end, "day");
            //将数据基于公司分组
            List<Map<String, Object>> resultList = new ArrayList<>();
            Map<String, List<KafkaNetcarStatisticsOperatePay>> listMap = anteayerList.stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getCompanyId));
            listMap.keySet().forEach(companyId -> {
                List<KafkaNetcarStatisticsOperatePay> orders = listMap.get(companyId);
                Map<String, Object> companyMap = new HashMap<>();
                companyMap.put("name", companyServiceService.selectCompanyNameByCompanyId(companyId));
                companyMap.put("id", companyId);
                Map<String, List<KafkaNetcarStatisticsOperatePay>> time = orders.stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getTime));
                List<Map<String, Object>> dataList = StatisticalUtil.getSomeDayListOfMonth(finalStartDate, finalEndDate);
                time.keySet().forEach(payTime -> {
                    //获取每个时间段的数据集合
                    List<KafkaNetcarStatisticsOperatePay> timeOrder = time.get(payTime);
                    Double total = timeOrder.stream().collect(summingDouble(KafkaNetcarStatisticsOperatePay::getFactPriceSum));

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
            List<KafkaNetcarStatisticsOperatePay> anteayerList = getorderData(start, end, "day");
            //将数据基于公司分组
            List<Map<String, Object>> result = new ArrayList<>();
            Map<String, List<KafkaNetcarStatisticsOperatePay>> listMap = anteayerList.stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getCompanyId));
            listMap.keySet().forEach(companyId -> {
                List<KafkaNetcarStatisticsOperatePay> orders = listMap.get(companyId);
                Map<String, List<KafkaNetcarStatisticsOperatePay>> time = orders.stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getTime));
                String companyName = companyServiceService.selectCompanyNameByCompanyId(companyId);
                Map<String, Object> companyMap = new HashMap<>();
                companyMap.put("name", companyName);
                companyMap.put("id", companyId);
                List<Map<String, Object>> dataList = StatisticalUtil.getSomeDayListOfMonth(finalStartDate, finalEndDate);
                time.keySet().forEach(key -> {
                    //获取每个时间段的数据集合
                    List<KafkaNetcarStatisticsOperatePay> timeOrder = time.get(key);
                    Double driveMile = timeOrder.stream().collect(summingDouble(KafkaNetcarStatisticsOperatePay::getDriveMileSum));
                    Double waitMile = timeOrder.stream().collect(summingDouble(KafkaNetcarStatisticsOperatePay::getWaitMileSum));
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
            List<KafkaNetcarPassengerComplaint> complaintList = getcomplaintData(startTime, null, dataType);
            Map<String, List<KafkaNetcarPassengerComplaint>> cpmpanys = complaintList.stream().collect(Collectors.groupingBy(KafkaNetcarPassengerComplaint::getCompanyId));
            Map<String, Object> resultMap = new HashMap<>();
            List<Map> mapList = Lists.newArrayList();
            cpmpanys.keySet().stream().forEach(companyId -> {
                List<KafkaNetcarPassengerComplaint> complaints = cpmpanys.get(companyId);
                //计算一个月的投诉（按公司聚合）
                int amount = complaints.stream().collect(summingInt(KafkaNetcarPassengerComplaint::getComplaintCount));
                Map<String, Object> complaintMap = new HashMap<>();
                complaintMap.put("id", companyId);
                complaintMap.put("name", companyServiceService.selectCompanyNameByCompanyId(companyId));
                complaintMap.put("total", amount);
                mapList.add(complaintMap);
            });
            resultMap.put("mapList", mapList);
            resultMap.put("totalComplaint", complaintList.stream().collect(summingInt(KafkaNetcarPassengerComplaint::getComplaintCount)));
            messageResp.setData(resultMap);
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
            List<KafkaNetcarPassengerComplaint> complaintList= getcomplaintData(startTime, null, type);
            Map<String, List<KafkaNetcarPassengerComplaint>> complaintMaps = complaintList.stream().collect(Collectors.groupingBy(KafkaNetcarPassengerComplaint::getCompanyId));
            List<Map> resultList = Lists.newArrayList();
            complaintMaps.keySet().stream().forEach(companyId -> {
                Map<String, Object> complaintMap = new HashMap<>();
                complaintMap.put("id", companyId);
                complaintMap.put("name", companyServiceService.selectCompanyNameByCompanyId(companyId));
                List<KafkaNetcarPassengerComplaint> complaints = complaintMaps.get(companyId);
                Map<String, List<KafkaNetcarPassengerComplaint>> maps = complaints.stream().collect(Collectors.groupingBy(KafkaNetcarPassengerComplaint::getTime));

                List<Map<String, Object>> dataList = statisticalUtil.dataInit(startTime, type);
                maps.keySet().stream().forEach(complaintTime -> {
                    int total = maps.get(complaintTime).stream().mapToInt(KafkaNetcarPassengerComplaint::getComplaintCount).sum();
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
            List<KafkaNetcarStatisticsOperatePay> anteayerList = getorderData(start, end, "day");
            //将数据基于公司分组
            List<Map<String, Object>> result = new ArrayList<>();
            Map<String, List<KafkaNetcarStatisticsOperatePay>> listMap = anteayerList.stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getCompanyId));

            listMap.keySet().forEach(companyId -> {
                List<KafkaNetcarStatisticsOperatePay> orders = listMap.get(companyId);
                Map<String, List<KafkaNetcarStatisticsOperatePay>> time = orders.stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getTime));
                String companyName = companyServiceService.selectCompanyNameByCompanyId(companyId);
                Map<String, Object> companyMap = new HashMap<>();
                companyMap.put("id", companyId);
                companyMap.put("name", companyName);

                List<Map<String, Object>> dateList = StatisticalUtil.getSomeDayListOfMonth(finalStartDate, finalEndDate);
                //基于时间排序
                List<String> data = time.keySet().stream().sorted(Comparator.comparing(String::hashCode)).collect(Collectors.toList());
                data.forEach(payTime -> {
                    List<KafkaNetcarStatisticsOperatePay> timeOrder = time.get(payTime);
                    //金额
                    Double totalFee = timeOrder.stream().collect(summingDouble(KafkaNetcarStatisticsOperatePay::getFactPriceSum));
                    //载客里程
                    Double driveMile = timeOrder.stream().collect(summingDouble(KafkaNetcarStatisticsOperatePay::getDriveMileSum));
                    //空驶里程
                    Double waitMile = timeOrder.stream().collect(summingDouble(KafkaNetcarStatisticsOperatePay::getWaitMileSum));
                    dateList.forEach(map -> {
                        String moment = String.valueOf(map.get("moment"));
                        if (StringUtils.equals(moment, payTime)) {
                            map.put("total", BigDecimalUtil.div(totalFee, ((driveMile + waitMile))));
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
            String start;
            if (StringUtils.isNotBlank(startTime)) {
                start = startTime;
            } else {
                start = DateUtil.getCurrMonth();
            }
            List<KafkaNetcarStatisticsHaulDistanceOutput> sumaResult = statisticalService.selectMonthDistanceData(start, stringJoint());
            List<Map<String, Object>> resultList = Lists.newArrayList();
            sumaResult.stream().forEach(data -> {
                Map<String, Object> map = Maps.newHashMap();
                map.put("total", data.getOperateCount());
                map.put("type", data.getHandlingType());
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
            List<KafkaNetcarStatisticsHaulDistanceOutput> sumaResult = statisticalService.selectByDateOnDistance(startTime, dateType, stringJoint());
            List<Map<String, Object>> resultList = Lists.newArrayList();
            sumaResult.stream().forEach(data -> {
                String company = data.getCompanyId();
                String companyName = companyServiceService.selectCompanyNameByCompanyId(company);
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", company);
                map.put("name", companyName);
                map.put("total", data.getOperateCount());
                map.put("type", data.getDataType());
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
            List<Map<String, Object>> resultList = Lists.newArrayList();

            List<KafkaNetcarStatisticsHaulDistanceOutput> sumaResult = statisticalService.selectSUMByDate(start, end, stringJoint());
            Map<String, List<KafkaNetcarStatisticsHaulDistanceOutput>> listMap = sumaResult.stream().collect(groupingBy(KafkaNetcarStatisticsHaulDistanceOutput::getCompanyId));

            listMap.keySet().stream().forEach(id -> {
                String companyName = companyServiceService.selectCompanyNameByCompanyId(id);
                Map<String, Object> resultMap = Maps.newHashMap();
                resultMap.put("id", id);
                resultMap.put("name", companyName);
                List<KafkaNetcarStatisticsHaulDistanceOutput> list = listMap.get(id);
                List<Map<String, Object>> dataList = StatisticalUtil.getSomeDayListOfMonth(finalStartDate, finalEndDate);
                list.forEach(item -> {
                    dataList.forEach(map -> {
                        String moment = String.valueOf(map.get("TIME"));
                        if (StringUtils.equals(moment, item.getTime())) {
                            map.put("total", BigDecimalUtil.div(item.getWaitMileSum(), (item.getDriveMileSum() + item.getWaitMileSum())));
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
            List<KafkaNetcarStatisticsOperatePay> orderList = getorderData(time, null, type);
            Map<String, List<KafkaNetcarStatisticsOperatePay>> cpmpanys = orderList.stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getCompanyId));

            List<Object> companyList = new ArrayList<>();

            String finalTime = time;
            cpmpanys.keySet().stream().forEach(company -> {
                //车辆总数
                Long number = yunzhengAmountService.selectByTime(company, finalTime, type);
                //基于时间分组企业的订单数据
                Map<String, List<KafkaNetcarStatisticsOperatePay>> orderByTime = cpmpanys.get(company)
                        .stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getTime));

                Map<String, Object> map = new HashMap<>();
                List<Map<String, Object>> timeList = getInit(finalTime, type);

                map.put("companyId", company);
                map.put("companyName", companyServiceService.selectCompanyNameByCompanyId(company));
                //将时间相同的数据进行计算
                orderByTime.keySet().stream().forEach(payTime -> {
                    List<KafkaNetcarStatisticsOperatePay> orders = orderByTime.get(payTime);
                    //产生订单的车辆数
                    Long vehicles = orders.stream().filter(distinctByKey(KafkaNetcarStatisticsOperatePay::getVehicleNo)).count();
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
     * 功能：获取指定 时间 或 时间段 内的数据
     * 数据源：druid
     * 表名称：netcar_statistics_operatePay
     *
     * @param SDate
     * @param EDate
     * @return
     * @throws Exception
     */
    public List<KafkaNetcarStatisticsOperatePay> getorderData(String SDate, String EDate, String dateType) throws Exception {
        List<KafkaNetcarStatisticsOperatePay> list = statisticalService.selectByDate(SDate, EDate, dateType, stringJoint());
        return list;
    }

    public List<KafkaNetcarPassengerComplaint> getcomplaintData(String SDate, String EDate, String dateType) throws Exception {
        List<KafkaNetcarPassengerComplaint> list = statisticalService.selectByDateOnComplaint(SDate, EDate, dateType, stringJoint());
        return list;
    }

    /**
     * 功能：添加企业限定
     *
     * @param
     * @return
     */
    private String stringJoint() {
        return roleAuthenticationUtils.filterCompanyIdCondition();
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


}
