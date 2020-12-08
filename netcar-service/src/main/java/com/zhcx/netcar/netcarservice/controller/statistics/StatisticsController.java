package com.zhcx.netcar.netcarservice.controller.statistics;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zhcx.basic.dubbo.BaseInfoEmplDubboService;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.facade.base.OrderService;
import com.zhcx.netcar.facade.statistics.StatisticalService;
import com.zhcx.netcar.facade.yunzheng.YunZhengVehicleService;
import com.zhcx.netcar.netcarservice.utils.*;
import com.zhcx.netcar.pojo.DruidEntity.*;
import com.zhcx.netcar.pojo.base.BookCarLevel;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyService;
import com.zhcx.netcar.pojo.base.NetcarOrderCreate;
import com.zhcx.netcar.pojo.statistical.KafkaNetcarOperateLoginout;
import com.zhcx.netcar.pojo.statistical.KafkaNetcarPassengerComplaint;
import com.zhcx.netcar.pojo.statistical.KafkaNetcarRevocationOut;
import com.zhcx.netcar.pojo.statistical.KafkaNetcarStatisticsOperatePay;
import com.zhcx.netcar.pojo.yuzheng.YunZhengVehicle;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
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
    private static Logger log = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private ExcelUtil excelUtil;

    private String ids;

    @Autowired
    private StatisticalUtil statisticalUtil;

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;

    @Autowired
    private StatisticalService statisticalService;

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private CompanyServiceService companyServiceService;

    @Autowired
    private YunZhengVehicleService yunZhengVehicleService;

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoEmplDubboService baseInfoEmplService;


    @Autowired
    private OrderService orderService;


    @ApiOperation(value = "首页--分时订单营收金额统计", notes = "首页--分时订单营收金额统计")
    @GetMapping("/queryHourOrderFeeCount")
    public MessageResp queryHourOrderFeeCount(HttpServletRequest request) {
        MessageResp messageResp = new MessageResp();
        try {
            String SDate = DateUtil.getYMDFormat(DateUtil.getSomeDay(0));
            List<KafkaNetcarStatisticsOperatePay> payList = getorderData(SDate, "", "day");
            Map<String, List<KafkaNetcarStatisticsOperatePay>> orderList = payList.stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getTime));
            List<Map<String, Object>> dateList = statisticalUtil.dataInit(DateUtil.getYMDFormat(new Date()), "day");
            orderList.keySet().forEach(time -> {
                List<KafkaNetcarStatisticsOperatePay> orders = orderList.get(time);
                Double factPrice = orders.stream().collect(summingDouble(KafkaNetcarStatisticsOperatePay::getFactPriceSum));
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
            String SDate = DateUtil.getYMDFormat(DateUtil.getSomeDay(0));
            List<KafkaNetcarStatisticsOperatePay> payList = getorderData(SDate, null, "day");
            Map<String, List<KafkaNetcarStatisticsOperatePay>> orderList = payList.stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getTime));
            List<Map<String, Object>> dateList = statisticalUtil.dataInit(DateUtil.getYMDFormat(new Date()), "day");
            orderList.keySet().forEach(time -> {
                Map<String, Object> redultMap = new HashMap<>();
                List<KafkaNetcarStatisticsOperatePay> orders = orderList.get(time);
                Double orderCount = orders.stream().collect(summingDouble(KafkaNetcarStatisticsOperatePay::getOrderCount));
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
            Map<String, String> parameters = UrlParamUtil.getParameters(request);
            String ids = roleAuthenticationUtils.filterCompanyIdCondition();
            //获取时间(统计维度为年月日) （最小粒度为分钟）
            String startTime = parameters.get("startTime").substring(0, 16);
            List<KafkaNetcarPassengerComplaint> complaintList = getSUMorderDate(startTime, ids);
            Map<String, Object> map = new HashMap<>();
            if (complaintList.size() >= 1) {
                map.put("totalSum", JSON.toJSONString(complaintList));
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

            List<KafkaNetcarOperateLoginout> loinoutList = statisticalService.selectByLoInOut(startTime);
            Map<String, List<KafkaNetcarOperateLoginout>> companyList = loinoutList.stream().collect(Collectors.groupingBy(KafkaNetcarOperateLoginout::getCompanyId));
            List<SignInBack> resultData = new ArrayList<>();
            String finalStartTime = startTime;
            companyList.keySet().forEach(companyId -> {
                List<KafkaNetcarOperateLoginout> log = companyList.get(companyId);
                SignInBack signInBack = new SignInBack();
                Map<Integer, List<KafkaNetcarOperateLoginout>> data = log.stream().collect(Collectors.groupingBy(KafkaNetcarOperateLoginout::getLogType));
                Integer signIn = data.get(1) != null ? data.get(1).stream().collect(summingInt(KafkaNetcarOperateLoginout::getLogCount)) : 0;
                Integer signBack = data.get(0) != null ? data.get(0).stream().collect(summingInt(KafkaNetcarOperateLoginout::getLogCount)) : 0;
                signInBack.setSignIn(signIn);
                signInBack.setSignBack(signBack);

                NetcarBaseInfoCompanyService companyService = companyServiceService.selectByCompanyId(companyId);
                if (companyService != null) {
                    String name = companyService.getServiceName();
                    signInBack.setCompanyName(name != null ? name : "");
                    Integer total = baseInfoEmplService.selectTotalByCompanyId(companyId);
                    signInBack.setTotal(total != null ? Math.toIntExact(total) : 0);
                }
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
            List<KafkaNetcarStatisticsOperatePay> payList = getorderData(startTime, null, dateType);
            Map<String, List<KafkaNetcarStatisticsOperatePay>> companys = payList.stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getCompanyId));
            List<String> companyKeyList = companys.keySet().stream().sorted(Comparator.comparing(String::hashCode)).collect(Collectors.toList());

            List<Map> resultList = Lists.newArrayList();

            companyKeyList.forEach(companyId -> {
                List<KafkaNetcarStatisticsOperatePay> orders = companys.get(companyId);
                Map<String, List<KafkaNetcarStatisticsOperatePay>> orderTimes = orders.stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getTime));

                List<Map<String, Object>> dataList = statisticalUtil.dataInit(startTime, dateType);
                Map<String, Object> companyMap = new HashMap<>();
                companyMap.put("name", companyServiceService.selectCompanyNameByCompanyId(companyId));
                companyMap.put("id", companyId);

                orderTimes.keySet().forEach(time -> {
                    List<KafkaNetcarStatisticsOperatePay> order = orderTimes.get(time);
                    //总订单量
                    int amount = order.stream().collect(summingInt(KafkaNetcarStatisticsOperatePay::getOrderCount));
                    //车辆数
                    Long vehicle = order.stream().filter(distinctByKey(KafkaNetcarStatisticsOperatePay::getVehicleNo)).count();
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
            List<KafkaNetcarStatisticsOperatePay> payList = getorderData(SDate, EDate, "month");
            messageResp.setData(summarizingStatistics(payList));
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    @RequestMapping("/order/export")
    @ResponseBody
    public void export(HttpServletRequest request,
                       HttpServletResponse response,
                       @RequestParam String SDate,
                       @RequestParam(value = "EDate", required = false) String EDate) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        //获取数据
        List<KafkaNetcarStatisticsOperatePay> payList = getorderData(SDate, EDate, "month");
        List<OrderStatisticsResult> list = summarizingStatistics(payList);
        /**
         * 生成excel
         */
        //生成一个工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        String fileName = sdf.format(new Date()) + "-营运汇总";
        //创建工作表1
        excelUtil.createExcelSheet(workbook, list, fileName, OrderStatisticsResult.class);
        //响应到客户端
        try {
            setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            List<KafkaNetcarStatisticsOperatePay> payList = getorderData(SDate, null, dateType);
            messageResp.setData(averageEmpty(payList, dateType, SDate));
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
            List<KafkaNetcarRevocationOut> revocationOutList = statisticalService.getDriverRevocation(startTime, endTime, dateType);


            //过滤多余数据
            List<KafkaNetcarRevocationOut> revocationList = filtration(revocationOutList, revocationType);
            Map<String, List<KafkaNetcarRevocationOut>> companyList = revocationList.stream().collect(Collectors.groupingBy(KafkaNetcarRevocationOut::getCompanyId));
            List<Map<String, Object>> result = new ArrayList<>();
            companyList.keySet().forEach(companyId -> {
                List<KafkaNetcarRevocationOut> revocations = companyList.get(companyId);
                //初始化时间集合
                List<Map<String, Object>> companyMap = getInit(startTime, dateType);
                Map<String, List<KafkaNetcarRevocationOut>> revocationMap = revocations.stream().collect(Collectors.groupingBy(KafkaNetcarRevocationOut::getRevocationType));
                Map<String, Object> company = new HashMap<>();
                company.put("companyName", companyServiceService.selectCompanyNameByCompanyId(companyId));
                revocationMap.keySet().forEach(time -> {
                    List<KafkaNetcarRevocationOut> data = revocationMap.get(time);
                    int count = data.size();
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

    /***********************分割****************************/
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

    /***********************分割****************************/

    /**
     * 功能：获取指定时间段内的数据
     *
     * @param SDate
     * @param EDate
     * @return
     * @throws Exception
     */
    private List<KafkaNetcarStatisticsOperatePay> getorderData(String SDate, String EDate, String dateType) {
        List<KafkaNetcarStatisticsOperatePay> payList = statisticalService.selectByDate(SDate, EDate, dateType, stringJoint());
        return payList;
    }

    private List<KafkaNetcarPassengerComplaint> getSUMorderDate(String SDate, String ids) {
        List<KafkaNetcarPassengerComplaint> payList = statisticalService.getSUMorderDate(SDate, ids, stringJoint());
        return payList;
    }


    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 功能：获取多家公司，每天24个时段的平均空驶时间
     *
     * @param orderList
     * @return
     */
    private List<Map<String, Object>> averageEmpty(List<KafkaNetcarStatisticsOperatePay> orderList, String dateType, String SDate) {
        List<Map<String, Object>> result = new ArrayList<>();
        //将数据基于公司分组
        Map<String, List<KafkaNetcarStatisticsOperatePay>> companys = orderList.stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getCompanyId));
        companys.keySet().forEach(companyId -> {

                    List<KafkaNetcarStatisticsOperatePay> orders = companys.get(companyId);
                    Map<String, List<KafkaNetcarStatisticsOperatePay>> time = orders.stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getTime));


                    Map<String, Object> companyMap = new HashMap<>();
                    companyMap.put("companyName", companyServiceService.selectCompanyNameByCompanyId(companyId));
                    companyMap.put("companyId", companyId);
                    List<Map<String, Object>> averageData = statisticalUtil.dataInit(SDate, dateType);
                    time.keySet().forEach(key -> {
                        //获取每个时间段的数据集合
                        List<KafkaNetcarStatisticsOperatePay> timeOrder = time.get(key);
                        //获取每个时间段的车辆数（去重）
                        Long vehicleAmount = timeOrder.stream().filter(distinctByKey(value -> value.getVehicleNo())).count();
                        //获取每个时间段的总空驶时间
                        int waitMile = timeOrder.stream().collect(summingInt(KafkaNetcarStatisticsOperatePay::getWaitTimeSum));
                        //总载客里程
                        Double percentage = timeOrder.stream().collect(summingDouble(KafkaNetcarStatisticsOperatePay::getDriveTimeSum));
                        //总空驶里程
                        Double empty = timeOrder.stream().collect(summingDouble(KafkaNetcarStatisticsOperatePay::getWaitMileSum));

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
    private List<OrderStatisticsResult> summarizingStatistics(List<KafkaNetcarStatisticsOperatePay> orderList) {
        Map<String, List<KafkaNetcarStatisticsOperatePay>> groupByCompanyID = orderList.stream().collect(Collectors.groupingBy(KafkaNetcarStatisticsOperatePay::getCompanyId));
        List<OrderStatisticsResult> resultList = new ArrayList<>();
        for (String key : groupByCompanyID.keySet()) {
            OrderStatisticsResult orderStatisticsResult = new OrderStatisticsResult();
            List<KafkaNetcarStatisticsOperatePay> companyList = groupByCompanyID.get(key);
            //总趟次/总订单数
            orderStatisticsResult.setOrderCount(companyList.stream().collect(summingInt(KafkaNetcarStatisticsOperatePay::getOrderCount)));
            orderStatisticsResult.setTotal(orderStatisticsResult.getOrderCount());
            //总金额
            orderStatisticsResult.setFactPrice(companyList.stream().collect(summingDouble(KafkaNetcarStatisticsOperatePay::getFactPriceSum)));
            //总载客时间
            orderStatisticsResult.setDriveTime(companyList.stream().collect(summingInt(KafkaNetcarStatisticsOperatePay::getDriveTimeSum)));
            //总空驶时间
            orderStatisticsResult.setWaitTime(companyList.parallelStream().collect(summingInt(KafkaNetcarStatisticsOperatePay::getWaitTimeSum)));
            //总载客里程
            orderStatisticsResult.setDriveMile(companyList.parallelStream().collect(summingDouble(KafkaNetcarStatisticsOperatePay::getDriveMileSum)));
            //总空驶里程
            orderStatisticsResult.setWaitMile(companyList.parallelStream().collect(summingDouble(KafkaNetcarStatisticsOperatePay::getWaitMileSum)));
            //获取当前公司本月产生订单的车辆数量（对数据进行去重）
//            Long vehcleAmount = companyList.stream().distinct().count();
            Long vehicleAmount = companyList.stream().filter(distinctByKey(KafkaNetcarStatisticsOperatePay::getVehicleNo)).count();
            //车均营运趟次
            orderStatisticsResult.setAverageCount((int) (orderStatisticsResult.getOrderCount() / vehicleAmount));
            //车均营运金额(月)
            orderStatisticsResult.setAveragePriceMM(orderStatisticsResult.getFactPrice() / vehicleAmount);
            //车均营运金额（日）
            orderStatisticsResult.setAveragePriceDD((double) Math.round((orderStatisticsResult.getFactPrice() / vehicleAmount / 30) * 100) / 100);
            //车均营运时间
            orderStatisticsResult.setAverageTime((int) ((orderStatisticsResult.getDriveTime() + orderStatisticsResult.getWaitTime()) / vehicleAmount));
            //车均空驶时间
            orderStatisticsResult.setAverageTimeEmpty((int) (orderStatisticsResult.getWaitTime() / vehicleAmount));
            //车均营运里程
            orderStatisticsResult.setAverageDriveMile((orderStatisticsResult.getDriveMile() + orderStatisticsResult.getWaitMile()) / vehicleAmount);
            orderStatisticsResult.setCompanyId(key);
            orderStatisticsResult.setCompanyName(companyServiceService.selectCompanyNameByCompanyId(key));

            orderStatisticsResult.setAveragePrice(orderStatisticsResult.getFactPrice() / orderStatisticsResult.getOrderCount());
            orderStatisticsResult.setCarryMile(orderStatisticsResult.getDriveMile());
            resultList.add(orderStatisticsResult);
        }
        return resultList;
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
            if (i < 10) {
                map.put("time", date + "-" + "0" + i);
            } else {
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
    private List<KafkaNetcarRevocationOut> filtration(List<KafkaNetcarRevocationOut> revocationList, String revocationTypr) {
        List<KafkaNetcarRevocationOut> result = new ArrayList<>();
        switch (revocationTypr) {
            case "1":
                result = revocationList.stream().filter(revocation -> null != revocation.getRevocationType() && "1" == revocation.getRevocationType() | "4" == revocation.getRevocationType()).collect(Collectors.toList());
                break;
            case "2":
                result = revocationList.stream().filter(revocation -> null != revocation.getRevocationType() && "2" == revocation.getRevocationType() | "5" == revocation.getRevocationType()).collect(Collectors.toList());
                break;
            case "3":
                result = revocationList.stream().filter(revocation -> null != revocation.getRevocationType() && "3" == revocation.getRevocationType()).collect(Collectors.toList());
                break;
        }
        return result;
    }

}
