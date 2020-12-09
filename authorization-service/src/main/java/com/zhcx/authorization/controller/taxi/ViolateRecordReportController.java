package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.JsonArray;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.DateUtil;
import com.zhcx.authorization.utils.HttpUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.excel.ExcelPlusUtils;
import com.zhcx.authorization.utils.excel.ExcelUtils;
import com.zhcx.authorization.utils.excel.SheetPojo;
import com.zhcx.basicdata.facade.taxi.TaxiCarMonitorTaskService;
import com.zhcx.basicdata.facade.taxi.TaxiViolateRecordService;
import com.zhcx.basicdata.pojo.druid.TaxiOrderProductionPojo;
import com.zhcx.basicdata.pojo.taxi.TaxiCarMonitorTask;
import com.zhcx.basicdata.pojo.taxi.TaxiViolateRecord;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 视频监控记录和违规行为记录导出到excel
 * @author: qiuziqiang
 * @date 2019-05-24 16:07
 **/

@Controller
@RequestMapping("/taxi/report")
public class ViolateRecordReportController {
    private Logger log = LoggerFactory.getLogger(ViolateRecordReportController.class);

    @Autowired
    private TaxiViolateRecordService taxiViolateRecordService;

    @Autowired
    private TaxiCarMonitorTaskService carMonitorTaskService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Value("${druid.order.table}")
    private String orderTable;

    @Autowired
    private HttpUtils httpUtils;

    /**
     * 违规行为记录导出
     */
    @RequestMapping("/export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response, @ModelAttribute TaxiViolateRecord param) throws Exception {
        //获取数据
        List<TaxiViolateRecord> list = taxiViolateRecordService.queryRecord(param);
        String newTime = DateUtil.getYMDHMSFormat(new Date());
        String[][] content = new String[list.size()][];
        //大标题
        String spreadhead = "违规行为记录表";
        //excel小标题
        String[] title = {"公司名称", "车牌号", "驾驶员", "违规行为", "违规地点", "类别", "上报人员", "时间", "备注"};
        //excel文件名
        String fileName = "违规行为记录表" + newTime + ".xls";
        //sheet名
        String sheetName = "违规行为记录表";
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            TaxiViolateRecord obj = list.get(i);
            content[i][0] = obj.getCorpName();
            content[i][1] = obj.getPlateNumber();
            content[i][2] = obj.getDriverName();
            content[i][3] = obj.getViolateBehavior();
            content[i][4] = obj.getViolateAddress();
            if (obj.getType() != null && obj.getType() == 1) {
                content[i][5] = "违规";
            } else {
                content[i][5] = "";
            }
            content[i][6] = obj.getCreateBname();
            String ymdhmsFormat = DateUtil.getYMDHMSFormat(obj.getCreateTime());
            content[i][7] = ymdhmsFormat;
            if (obj.getRemark() != null) {
                content[i][8] = obj.getRemark() + " ";
            } else {
                content[i][8] = "";
            }
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook2(spreadhead, sheetName, title, content, null);
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
     * 视频监控记录导出
     */
    @RequestMapping("/carMonitorExport")
    @ResponseBody
    public void carMonitorExport(HttpServletRequest request, HttpServletResponse response, @ModelAttribute TaxiCarMonitorTask param) throws Exception {
        //获取数据
        List<TaxiCarMonitorTask> list = carMonitorTaskService.queryCarMonitorTaskViList(param);
        String[][] content = new String[list.size()][];
        String ymdhmsFormat = DateUtil.getYMDHMSFormat(new Date());
        //excel标题
        String[] title = {"时间", "监控人员", "公司名称", "车牌号", "驾驶员", "类别", "任务状态", "监控打开时间", "监控关闭时间", "监控时长(秒)", "违规行为"};
        //excel文件名
        String fileName = "视频监控记录表" + ymdhmsFormat + ".xls";
        //sheet名
        String sheetName = "视频监控记录表";
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            TaxiCarMonitorTask obj = list.get(i);
            content[i][0] = obj.getCreateTime();
            content[i][1] = obj.getCreateBname();
            content[i][2] = obj.getCorpName();
            content[i][3] = obj.getPlateNumber();
            content[i][4] = obj.getDriverName();
            if (obj.getType() != null && obj.getType() == 1) {
                content[i][5] = "任务";
                if ("03".equals(obj.getStatus())) {
                    content[i][6] = "已终止";
                } else if ("02".equals(obj.getStatus())) {
                    content[i][6] = "已完成";
                } else {
                    content[i][6] = "进行中";
                }
            } else {
                content[i][5] = "记录";
                if ("03".equals(obj.getStatus())) {
                    content[i][6] = "--";
                } else if ("02".equals(obj.getStatus())) {
                    content[i][6] = "已完成";
                } else {
                    content[i][6] = "进行中";
                }
            }
            content[i][7] = obj.getTaskStartTime();
            content[i][8] = obj.getTaskEndTime();
            content[i][9] = obj.getMonitorDuration().toString();
            List<TaxiViolateRecord> violateRecords = obj.getViolateRecords();
            StringBuilder violateBehavior = new StringBuilder();
            if (violateRecords.size() > 0) {
                for (TaxiViolateRecord violateRecord : violateRecords) {
                    if (violateRecord.getViolateBehavior() == null || "".equals(violateRecord.getViolateBehavior())) {
                        violateBehavior.append(" ");
                    } else {
                        violateBehavior.append(violateRecord.getViolateBehavior() + ";");
                    }
                }
                content[i][10] = violateBehavior.toString();
            }

        }
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook2(sheetName, sheetName, title, content, null);
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
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*public static String getCron(final Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);
        String formatTimeStr = "";
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }*/


    @GetMapping("/singleVehicleIncomeCount")
    @ApiOperation(value = "营运收入--单车月收入统计", notes = "营运收入--单车月收入统计")
    public MessageResp singleVehicleIncomeCount(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime) {
        MessageResp resp = new MessageResp();
        List<SheetPojo> sheetPojoList = new ArrayList<>();

        try {
            //非管理员用户只能够查看自己企业
            //总数据获取
            AuthUserResp authUser = sessionHandler.getUser(request);
            String corpId = CommonUtils.getUserCorpId(authUser);

            //Sheet1表格数据
            SheetPojo sheetPojo1 = new SheetPojo();
            sheetPojo1.setTitle(new String[]{"日期", "日均收入/元", "次均收入/元", "公里产值/元"});
            sheetPojo1.setHeadline(startTime.substring(0, 7) + "-单车月收入统计表");
            sheetPojo1.setSheetName("收入统计");

            //表格数据填充
            Integer day = DateUtil.getDayOfMonth(startTime.substring(0, 7));
            String[][] values = new String[day][];
            for (int i = 0; i < day; i++) {
                String[] dayStatistics;
                String date = DateUtil.getOffsetsDay(startTime.substring(0, 8) + "00", i + 1);

                JSONArray jsonArray = selelctData(corpId, date, date, null);
                if (jsonArray.size() > 0) {
                    List<Map> taxiOrderProduction = JSON.parseObject(jsonArray.toJSONString(), new TypeReference<List<Map>>() {
                    });
//                    //总金额
//                    BigDecimal fee = data.stream().map(TaxiOrderProductionPojo::getFEE).reduce(BigDecimal.ZERO, BigDecimal::add);
//                    //车辆数
//                    Long vehicleAmount = data.stream().filter(a -> a.getVEHICLE_ID() != null).mapToLong(TaxiOrderProductionPojo::getVEHICLE_ID).distinct().count();
//                    //载客里程
//                    Double effMileage = data.stream().mapToDouble(TaxiOrderProductionPojo::getEFF_MILEAGE).sum();
//                    //空驶里程
//                    Double emptyMileage = data.stream().mapToDouble(TaxiOrderProductionPojo::getEMPTY_MILEAGE).sum();
//                    //订单数量
//                    Integer orderAmount = data.size();
//                    //车均收入
//                    String a = String.valueOf(fee.divide(BigDecimal.valueOf(vehicleAmount), 2, BigDecimal.ROUND_HALF_UP));
//                    //次均收入
//                    String b = String.valueOf(fee.divide(BigDecimal.valueOf(orderAmount), 2, BigDecimal.ROUND_HALF_UP));
//                    //公里产值
//                    String c = String.valueOf(fee.divide(BigDecimal.valueOf(effMileage + emptyMileage), 2, BigDecimal.ROUND_HALF_UP));

                    String a = String.valueOf(taxiOrderProduction.get(0).get("b"));
                    String b = String.valueOf(taxiOrderProduction.get(0).get("a"));
                    String c = String.valueOf(taxiOrderProduction.get(0).get("d"));

                    dayStatistics = new String[]{date, a, b, c};
                } else {
                    dayStatistics = new String[]{date, "0", "0", "0"};
                }
                values[i] = dayStatistics;
            }
            sheetPojo1.setType(1);
            sheetPojo1.setValues(values);
            sheetPojoList.add(sheetPojo1);

            //Sheet2表格数据
            SheetPojo sheetPojo2 = new SheetPojo();
            sheetPojo2.setTitle(new String[]{"序号", "车牌号", "收入/元", "排名"});
            sheetPojo2.setHeadline(startTime.substring(0, 7) + "-单车月收入排名");
            sheetPojo2.setSheetName("收入排名");
            Integer end = DateUtil.getDayOfMonth(startTime.substring(0, 7));
            String endstr = startTime.substring(0, 8) + String.valueOf(end);
            JSONArray ranking = selelctRanking(corpId, startTime, endstr, null);
            List<Map> rankingMap = JSON.parseObject(ranking.toJSONString(), new TypeReference<List<Map>>() {
            });
            //表格数据填充
            String[][] values2 = new String[rankingMap.size()][];
            int i = 0;
            for (Map map : rankingMap) {
                String data = String.valueOf(i + 1);
                String plateNum = (String) map.get("PLATE_NUM");
                String fee = String.valueOf((BigDecimal) map.get("e"));
                String[] dayStatistics = {data, plateNum, fee, data};
                values2[i] = dayStatistics;
                i++;
            }
            sheetPojo2.setType(1);
            sheetPojo2.setValues(values2);
            sheetPojoList.add(sheetPojo2);
            dispose(response, sheetPojoList, "单车月收入统计导出模板.xls");
        } catch (
                Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }
        return resp;
    }

    @GetMapping("/dayNightIncomeCount")
    @ApiOperation(value = "营运收入--白晚班收入统计", notes = "01-白班;02-晚班")
    public MessageResp dayNightIncomeCount(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime, String type) {
        MessageResp resp = new MessageResp();
        try {
            //非管理员用户只能够查看自己企业
            AuthUserResp authUser = sessionHandler.getUser(request);
            String corpId = null;//CommonUtils.getUserCorpId(authUser);
//            JSONArray jsonArray = selelctData(corpId, startTime, endTime);
//            List<TaxiOrderProductionPojo> taxiOrderProductionPojos = JSON.parseObject(jsonArray.toJSONString(), new TypeReference<List<TaxiOrderProductionPojo>>() {
//            });
//            List<TaxiOrderProductionPojo> OrderProduction = taxiOrderProductionPojos.stream().filter(a -> a.getWORK_TYPE() != null || !a.getWORK_TYPE().equals("")).collect(Collectors.toList());
//            Map<String, List<TaxiOrderProductionPojo>> team = OrderProduction.stream().collect(Collectors.groupingBy(TaxiOrderProductionPojo::getWORK_TYPE));
            List<SheetPojo> sheetPojoList = new ArrayList<>();
            SheetPojo sheetPojo1 = new SheetPojo();
            sheetPojo1.setTitle(new String[]{"日期", "白晚班", "次均收入/元", "车均收入/元", "最高单车收入/元", "公里产值/元"});
            sheetPojo1.setHeadline(startTime + "-" + endTime + "  白晚班收入统计");
            sheetPojo1.setSheetName("白班收入统计");
            sheetPojo1.setType(1);
            SheetPojo sheetPojo2 = new SheetPojo();
            sheetPojo2.setTitle(new String[]{"日期", "白晚班", "次均收入/元", "车均收入/元", "最高单车收入/元", "公里产值/元"});
            sheetPojo2.setHeadline(startTime + "-" + endTime + "  白晚班收入统计");
            sheetPojo2.setSheetName("晚班收入统计");
            sheetPojo2.setType(1);
            List<String> dates = getDateDifference(startTime, endTime);
            String[][] sheetValues1 = new String[dates.size()][];//白班
            String[][] sheetValues2 = new String[dates.size()][];//晚班

            int i = 0;
            List<String> typr = new ArrayList<>();
            typr.add("01");
            typr.add("02");
            for (String item : typr) {
//                List<TaxiOrderProductionPojo> group = team.get(item);
//                Map<String, List<TaxiOrderProductionPojo>> dateGroup= new HashMap<>();
//                if (group != null){
//                    dateGroup  = group.stream().collect(Collectors.groupingBy(TaxiOrderProductionPojo::getMoment));
//                }
                int j = 0;
                String[][] dayData = new String[dates.size()][];
                for (String date : dates) {
                    String[] dayStatistics;
//                    List<TaxiOrderProductionPojo> daylist = dateGroup.get(date);

                    JSONArray jsonArray = selelctData(corpId, date, date, item);
                    if (jsonArray.size() > 0) {
                        List<Map> taxiOrderProduction = JSON.parseObject(jsonArray.toJSONString(), new TypeReference< List<Map>>() {
                        });
//                        //总金额
//                        BigDecimal fee = daylist.stream().map(TaxiOrderProductionPojo::getFEE).reduce(BigDecimal.ZERO, BigDecimal::add);
//                        //车辆数
//                        Long vehicleAmount = daylist.stream().filter(a -> a.getPLATE_NUM() != null).distinct().map(TaxiOrderProductionPojo::getPLATE_NUM).count();
//                        //载客里程
//                        Double effMileage = daylist.stream().mapToDouble(TaxiOrderProductionPojo::getEFF_MILEAGE).sum();
//                        //空驶里程
//                        Double emptyMileage = daylist.stream().mapToDouble(TaxiOrderProductionPojo::getEMPTY_MILEAGE).sum();
//                        //订单数量
//                        Integer orderAmount = daylist.size();
//                        //车均收入
//                        String a = String.valueOf(fee.divide(BigDecimal.valueOf(vehicleAmount), 2, BigDecimal.ROUND_HALF_UP));
//                        //次均收入
//                        String b = String.valueOf(fee.divide(BigDecimal.valueOf(orderAmount), 2, BigDecimal.ROUND_HALF_UP));
//                        //公里产值
//                        String c = String.valueOf(fee.divide(BigDecimal.valueOf(effMileage + emptyMileage), 2, BigDecimal.ROUND_HALF_UP));
//                        //收入排名
//                        Map<String,List<TaxiOrderProductionPojo>> map = daylist.stream().collect(Collectors.groupingBy(TaxiOrderProductionPojo::getPLATE_NUM));
//                        Map<String,BigDecimal> vehileMap = new HashMap();
//                        map.keySet().forEach(data -> {
//                            vehileMap.put(data,map.get(data).stream().map(TaxiOrderProductionPojo::getFEE).reduce(BigDecimal.ZERO,BigDecimal::add));
//                        });
//                        //单车最大收入
//                        BigDecimal feeTotal = vehileMap.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
                        //次均收入
                        String a = String.valueOf(taxiOrderProduction.get(0).get("a"));
                        //车均收入
                        String b = String.valueOf(taxiOrderProduction.get(0).get("b"));
                        //公里产值
                        String c = String.valueOf(taxiOrderProduction.get(0).get("d"));
                        //单车最大收入

                        JSONArray ranking = selelctRanking(corpId, date, date, item);
                        String e = "0";
                        if (ranking.size() > 0) {
                            List<Map> rankingMap = JSON.parseObject(ranking.toJSONString(), new TypeReference<List<Map>>() {
                            });
                            e = String.valueOf(rankingMap.get(0).get("e"));
                        }
                        String workType = null;
                        if (item.equals("01")) {
                            workType = "白班";
                        } else {
                            workType = "晚班";
                        }
                        dayStatistics = new String[]{date, workType, a, b, e, c};
                    } else {
                        dayStatistics = new String[]{date, "", "0", "0", "0", "0"};
                    }

                    dayData[j] = dayStatistics;
                    j++;
                }
                if (item.equals("01")) {
                    sheetValues1 = dayData;
                } else {
                    sheetValues2 = dayData;
                }

            }
            sheetPojo1.setValues(sheetValues1);
            sheetPojo2.setValues(sheetValues2);
            sheetPojoList.add(sheetPojo1);
            sheetPojoList.add(sheetPojo2);
            dispose(response, sheetPojoList, "白晚班收入统计.xls");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }
        return resp;
    }


    /**
     * druid数据查询
     *
     * @param corpId
     * @param startTime
     * @param endTim
     * @return
     * @throws Exception
     */
    private JSONArray selelctData(String corpId, String startTime, String endTim, String workType) throws Exception {
        StringBuilder sqlBuilder = new StringBuilder();
        String sql = null;
        sqlBuilder.append("SELECT " +
                //平均订单金额
                " TRUNCATE (sum( FEE ) / count( 1 ),2) AS a, " +
                //平均单车收入
                " TRUNCATE (sum( FEE ) / count( DISTINCT VEHICLE_ID ),2) AS b, " +
                //最高
                " TRUNCATE ( max( FEE ), 2 ) AS c, " +
                //公里产值
                " TRUNCATE ( SUM( FEE ) / ( SUM( EFF_MILEAGE ) + SUM( EMPTY_MILEAGE ) ), 2 ) AS d " +
                " FROM ");

        sqlBuilder.append(orderTable);
        sqlBuilder.append(" WHERE TIME_FORMAT(__time, 'yyyy-MM-dd') >= '" + startTime + "' " + "and TIME_FORMAT(__time, 'yyyy-MM-dd') <= '" + endTim + "' ");
        if (StringUtils.isNotBlank(workType)) {
            sqlBuilder.append(" and WORK_TYPE = '" + workType+"'");
        }
        if (StringUtils.isNotBlank(corpId)) {
            sqlBuilder.append("  and CORP_ID = '" + corpId + "' ");
        }
        sql = sqlBuilder.toString();
        log.info("singleVehicleIncomeCount SQL: " + sql);
        JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", sql);
        return jsonArray;
    }

    /**
     * 收入排行
     *
     * @param corpId
     * @param startTime
     * @param endTim
     * @param workType
     * @return
     * @throws Exception
     */
    private JSONArray selelctRanking(String corpId, String startTime, String endTim, String workType) throws Exception {
        StringBuilder sqlBuilder = new StringBuilder();
        String sql = null;
        sqlBuilder.append("SELECT " +
                " TRUNCATE ( SUM( FEE ), 2 ) AS e, " +
                " PLATE_NUM " +
                " FROM ");
        sqlBuilder.append(orderTable);
        sqlBuilder.append(" WHERE TIME_FORMAT(__time, 'yyyy-MM-dd') >= '" + startTime + "' " + "and TIME_FORMAT(__time, 'yyyy-MM-dd') <= '" + endTim + "' ");
//        if (StringUtils.isNotBlank(workType)) {
//            sqlBuilder.append(" and WORK_TYPE = " + workType);
//        }
        sqlBuilder.append(" GROUP BY PLATE_NUM");
        sqlBuilder.append(" ORDER BY e DESC LIMIT 10");
        sql = sqlBuilder.toString();
        log.info("singleVehicleIncomeCount SQL: " + sql);
        JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", sql);
        return jsonArray;
    }


    /**
     * Excel文件导出（多sheet）
     *
     * @param response
     * @param sheetList
     * @param fileName
     */
    public void dispose(HttpServletResponse response, List<SheetPojo> sheetList, String fileName) {


        HSSFWorkbook resultWB = null;
        try {
            if (sheetList == null) {
                return;
            } else {
                for (SheetPojo sheet : sheetList) {
                    //创建HSSFWorkbook
                    resultWB = ExcelPlusUtils.getHSSFWorkbook(sheet, resultWB);
                }
            }
            //响应到客户端

            setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            resultWB.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private final static String FORMAT_yyyy_MM_dd = "yyyy-MM-dd";
    private final static SimpleDateFormat formatYYYYMMDD = new SimpleDateFormat(FORMAT_yyyy_MM_dd);

    private List<String> getDateDifference(String StartTime, String endTime) throws ParseException {
        Date dt1 = null, dt2 = null;
        dt1 = formatYYYYMMDD.parse(StartTime);
        dt2 = formatYYYYMMDD.parse(endTime);
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(dt1);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(dt2);
        int days = Math.abs(((int) (startCal.getTime().getTime() / 1000) - (int) (endCal.getTime().getTime() / 1000)) / 3600 / 24) + 1;
        //当开始时间和结束时间在同一天时，填补当天作为返回值

        if (days == 0) {
            days = days + 1;
        }
        List<String> dateString = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            dateString.add(DateUtil.getOffsetsDay(StartTime, i));
        }
        return dateString;
    }

//    @GetMapping("/test")
//    @ResponseBody
//    public void test(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String fileName = "这个是一个测试.xls";
//        SheetPojo sheet = new SheetPojo();
//        sheet.setHeadline("这是一个测试");
//        //excel标题
//        String[] title = {"时间", "监控人员", "公司名称", "车牌号", "驾驶员", "类别", "任务状态", "监控打开时间", "监控关闭时间", "监控时长(秒)", "违规行为"};
//        String[][] content = new String[11][];
//        String[] data = {"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
//        content[0] = data;
//        sheet.setValues(content);
//        sheet.setTitle(title);
//        sheet.setSheetName("测试");
//        sheet.setType(1);
//
//
//        SheetPojo sheet1 = new SheetPojo();
//        sheet1.setHeadline("这是一个测试2");
//        //excel标题
//        String[] title1 = {"时间2", "监控人员2", "公司名称2", "车牌号2", "驾驶员2", "类别2", "任务状态2", "监控打开时间2", "监控关闭时间2", "监控时长(秒)2", "违规行为2"};
//        String[][] content1 = new String[11][];
//        String[] data1 = {"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
//        content[0] = data1;
//        sheet1.setValues(content1);
//        sheet1.setTitle(title1);
//        sheet1.setSheetName("测试2");
//        sheet1.setType(1);
//        List<SheetPojo> sheetList = new ArrayList<>();
//        sheetList.add(sheet);
//        sheetList.add(sheet1);
//        dispose(response, sheetList, fileName);
//    }


}
