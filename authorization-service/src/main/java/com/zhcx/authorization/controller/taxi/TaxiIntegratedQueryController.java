package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.DateUtil;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.authorization.utils.excel.ExcelUtils;
import com.zhcx.authorization.utils.excel.ExportExcel;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoDriverService;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoVehicleService;
import com.zhcx.basicdata.facade.taxi.TaxiCheckInOutService;
import com.zhcx.basicdata.pojo.taxi.TaxiBasicDataExport;
import com.zhcx.basicdata.pojo.taxi.TaxiCheckInOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @program: authorization-service
 * @ClassName:IntegratedQueryController
 * @description: 出租-综合查询接口
 * @author: ZhangKai
 * @create: 2018-12-02 16:38
 **/
@RequestMapping("/taxi/integrated")
@RestController
@Api(value = "integrated", tags = "出租车综合查询")
public class TaxiIntegratedQueryController {

    private static final Logger logger = LoggerFactory.getLogger(TaxiIntegratedQueryController.class);

    private static final String YYYYMMDD_FORMAT_PATTERN = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))";

    @Autowired
    private TaxiBaseInfoVehicleService taxiBaseInfoVehicleService;

    @Autowired
    private TaxiCheckInOutService taxiCheckInOutService;

    @Autowired
    private TaxiBaseInfoDriverService driverService;


    @GetMapping("/query")
    @ApiOperation(value = "query", tags = "基础档案")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", dataType = "int", name = "pageNo", value = "pageNo", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "pageSize", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "startTime", value = "开始时间", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "endTime", value = "结束时间", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "corpId", value = "企业", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "plateNumber", value = "车牌号", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "driverName", value = "驾驶员姓名", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "queryType", value = "查询类型(basicData:基础档案,operationData:营运数据,onoffData:上下班打卡)", required = true)})
    public MessageResp basicData(HttpServletRequest request, int pageNo, int pageSize, String startTime, String endTime,
                                 String corpId, String plateNumber, String driverName, String queryType, String keyWord) {
        MessageResp result = new MessageResp();
        PageInfo<Object> pageInfo = null;
        Map<String, String> param = new HashMap<>();
        param.put("pageNo", String.valueOf(pageNo));
        param.put("pageSize", String.valueOf(pageSize));
        boolean dateCheck = false;
        try {
            if (null != startTime && !"".equals(startTime)) {
                param.put("startTime", packgingDate(startTime, true));
            }
            if (null != endTime && !"".equals(endTime)) {
                param.put("endTime", packgingDate(endTime, false));
            }
            if (null != corpId && !"".equals(corpId)) {
                param.put("corpId", corpId);
            }
            if (null != plateNumber && !"".equals(plateNumber)) {
                param.put("plateNumber", plateNumber);
            }
            if (null != driverName && !"".equals(driverName)) {
                param.put("driverName", driverName);
            }
            if (null == queryType || "".equals(queryType)) {
                logger.error("查询类型不能为空");
                return CommonUtils.returnErrorInfo("查询失败");
            }
            if (null != keyWord && !"".equals(keyWord)) {
                param.put("keyWord", keyWord);
            }
            if (!queryType.equals("ONOFFDATA") && queryType.equals("basicData")) {
                param.put("queryType", queryType);
                pageInfo = taxiBaseInfoVehicleService.queryVehicleForReport(param);
            } else if (queryType.equals("ONOFFDATA")) {

            } else {
                param.put("queryType", queryType);
                pageInfo = taxiBaseInfoVehicleService.queryVehicleByCorpForReport(param);
            }
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("basicData查询异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return result;
    }


    /**
     * 导出车辆营运统计记录
     */
    @ResponseBody
    @RequestMapping("/basicDataExport")
    public MessageResp export(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "startTime") String startTime, @RequestParam(value = "endTime") String endTime,
                              String corpId, String keyWord) throws Exception {
        Map<String, String> param = new HashMap<>();
        if (StringUtils.isNotBlank(corpId)) param.put("corpId", corpId);
        if (StringUtils.isNotBlank(keyWord)) param.put("keyWord", keyWord);
        param.put("startTime", packgingDate(startTime, true));
        param.put("endTime", packgingDate(endTime, false));
        param.put("queryType", "basicData");
//        param.put("limit", "5000");
        //需要导出的数据
        List<TaxiBasicDataExport> exportList = taxiBaseInfoVehicleService.findTaxiBasicDataExportList(param);
        if (exportList != null && exportList.size() > 5000)
            return CommonUtils.returnErrorInfo("导出数据量不能超过5000条,请添加筛选条件");

        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String xlsname = "车辆营运统计记录";
        String filepath = this.getClass().getClassLoader().getResource("").getPath() + "exceltemplate/" + xlsname + ".xlsx";
        // 导出数据到模板
        ExportExcel<TaxiBasicDataExport> exportExcel = new ExportExcel<>(TaxiBasicDataExport.class);
        exportExcel.exportExcelByExcelTemplateStyle2(filepath, xlsname, exportList, "yyyy-MM-dd HH:mm:ss", request, response);
        return null;
    }


    /**
     * 营运数据对比
     */
    @GetMapping("/yyDataCompare")
    public MessageResp yyDataCompare(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize,
                                     @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
                                     @RequestParam(value = "corpId", required = false) Long corpId) {
        MessageResp result = new MessageResp();
        Map<String, String> param = new HashMap<>();
        PageInfo<Object> pageInfo = null;
        try {
            if (corpId != null) param.put("corpId", String.valueOf(corpId));
            String sTime = packgingDate(startTime, true);
            String eTime = packgingDate(endTime, false);
            param.put("startTime", sTime);
            param.put("endTime", eTime);
            String pastStartTime = String.valueOf(Integer.parseInt(sTime.substring(0, 4)) - 1) + sTime.substring(4);
            String pastEndTime = String.valueOf(Integer.parseInt(eTime.substring(0, 4)) - 1) + eTime.substring(4);
            param.put("pastStartTime", pastStartTime);
            param.put("pastEndTime", pastEndTime);
            param.put("pageNo", String.valueOf(pageNo));
            param.put("pageSize", String.valueOf(pageSize));

            pageInfo = taxiBaseInfoVehicleService.yyDataCompare(param);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("查询成功");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("yyDataCompare查询异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return result;
    }

    /**
     * 时段对比分析
     */
    @GetMapping("/timeDataCompare")
    public MessageResp timeDataCompare(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize,
                                       @RequestParam("date") String date,
                                       @RequestParam(value = "corpId", required = false) Long corpId) {
        MessageResp result = new MessageResp();
        Map<String, String> param = new HashMap<>();
        PageInfo<JSONObject> pageInfo = null;
        try {
            if (corpId != null) param.put("corpId", String.valueOf(corpId));
            param.put("date", date);
            param.put("pageNo", String.valueOf(pageNo));
            param.put("pageSize", String.valueOf(pageSize));

            pageInfo = taxiBaseInfoVehicleService.timeDataCompare(param);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("查询成功");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("yyDataCompare查询异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return result;
    }


    /**
     * 驾驶员营运数据统计
     * fuelTypeCode 燃料类型
     * dateNum 天数
     * startTime endTime 格式 yyyy-MM-dd
     * corpId
     */
    @GetMapping("/driverData")
    @ApiOperation(value = "查询驾驶员营运统计", notes = "参数为出租车辆信息对象")
    public MessageResp getDriverData(@RequestParam(value = "startTime") String startTime, @RequestParam(value = "endTime") String endTime,
                                     @RequestParam(value = "dateNum") String dateNum, String fuelTypeCode,
                                     String corpId, String keyWord, int pageNo, int pageSize) {
        MessageResp result = new MessageResp();
        Map<String, String> param = new HashMap<>();
        PageInfo<Object> pageInfo = null;
        try {
            if (StringUtils.isNotBlank(corpId)) param.put("corpId", corpId);
            if (StringUtils.isNotBlank(keyWord)) param.put("keyWord", keyWord);
            if (StringUtils.isNotBlank(fuelTypeCode)) param.put("fuelTypeCode", fuelTypeCode);
            param.put("pageNo", String.valueOf(pageNo));
            param.put("pageSize", String.valueOf(pageSize));
            param.put("startTime", packgingDate(startTime, true));
            param.put("endTime", packgingDate(endTime, false));
            param.put("dateNum", dateNum);
            pageInfo = driverService.queryDriverDataReport(param);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("basicData查询异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("查询失败");
        }

        return result;
    }

    /**
     * 导出驾驶员营运数据
     */
    @ResponseBody
    @RequestMapping("/driverDataExport")
    public void driverDataExport(HttpServletResponse response,
                                 @RequestParam(value = "startTime") String startTime, @RequestParam(value = "endTime") String endTime,
                                 @RequestParam(value = "dateNum") String dateNum, String fuelTypeCode,
                                 String corpId, String keyWord) throws Exception {
        Map<String, String> param = new HashMap<>();

        if (StringUtils.isNotBlank(corpId)) param.put("corpId", corpId);
        if (StringUtils.isNotBlank(keyWord)) param.put("keyWord", keyWord);
        if (StringUtils.isNotBlank(fuelTypeCode)) param.put("fuelTypeCode", fuelTypeCode);
        param.put("startTime", packgingDate(startTime, true));
        param.put("endTime", packgingDate(endTime, false));
        param.put("dateNum", dateNum);

        //需要导出的数据
        JSONArray exportArr = driverService.findTaxiDriverDataExportList(param);
        String ymdhmsFormat = DateUtil.getYMDHMSFormat(new Date());
        //excel文件名
        String fileName = "驾驶员营运统计表 " + ymdhmsFormat + ".xls";
        String[][] content = new String[exportArr.size()][];
        //excel标题
        String[] title1 = {"驾驶员", "所属企业", "车牌号", "燃油类型", "营运收入/元", "订单笔数", "营运里程/km", "次均收入/元", "日均收入/元", "公里产值/元", "载运时长/h"};
        for (int i = 0; i < exportArr.size(); i++) {
            content[i] = new String[title1.length];
            JSONObject jsonObject = (JSONObject) exportArr.get(i);
            content[i][0] = jsonObject.getString("driverName");
            content[i][1] = jsonObject.getString("corpName");
            content[i][2] = jsonObject.getString("plateNum");
            content[i][3] = jsonObject.getString("fuelType");
            content[i][4] = jsonObject.getString("totalFee");
            content[i][5] = jsonObject.getString("totalOrder");
            content[i][6] = jsonObject.getString("totalMileage");
            content[i][7] = jsonObject.getString("cjFee");
            content[i][8] = jsonObject.getString("dayFee");
            content[i][9] = jsonObject.getString("mileageFee");
            content[i][10] = jsonObject.getString("totalTime");
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook2("驾驶员营运统计表", "驾驶员营运统计表", title1, content, null);
        //响应到客户端
        try {
            ExcelUtils.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/onoffData")
    @ApiOperation(value = "查询出租车辆信息", notes = "参数为签到对象")
    public MessageResp getOnOffData(@ModelAttribute TaxiCheckInOut param) {
        MessageResp result = new MessageResp();
        PageInfo<TaxiCheckInOut> pageInfo = null;
        try {
            pageInfo = taxiCheckInOutService.selectByParam(param);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("getOnOffData查询异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return result;
    }


    /**
     * 重构时间格式
     *
     * @param time    时间
     * @param isStart 是否开始时间
     * @return
     * @throws Exception
     */
    protected String packgingDate(String time, boolean isStart) throws Exception {
        boolean matche = Pattern.matches(YYYYMMDD_FORMAT_PATTERN, time);
        if (!matche) {
            throw new Exception("日期格式不正确");
        }
        if (isStart) {
            time = time + " 00:00:00";
        } else {
            time = time + " 23:59:59";
        }
        return time;
    }
}
