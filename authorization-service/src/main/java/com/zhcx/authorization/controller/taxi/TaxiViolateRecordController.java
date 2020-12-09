package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig.SessionHandler;
import com.zhcx.authorization.utils.*;
import com.zhcx.authorization.utils.excel.ExportExcel;
import com.zhcx.basicdata.facade.taxi.CreditBehaviorService;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoCompanyService;
import com.zhcx.basicdata.facade.taxi.TaxiOperationLogService;
import com.zhcx.basicdata.facade.taxi.TaxiViolateRecordService;
import com.zhcx.basicdata.pojo.taxi.*;
import com.zhcx.common.util.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @description: 违规行为 controller
 * @author: qiuziqiang
 * @date 2019-05-14 11:33
 **/
@RestController
@RequestMapping("/taxi/violateRecord")
@Api(value = "violateRecord", tags = "违规行为接口")
public class TaxiViolateRecordController {

    private Logger log = LoggerFactory.getLogger(TaxiViolateRecordController.class);

    private static final String YYYYMMDD_FORMAT_PATTERN = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))";

    @Autowired
    private TaxiViolateRecordService taxiViolateRecordService;

    @Autowired
    private TaxiOperationLogService logService;

    @Autowired
    private TaxiBaseInfoCompanyService companyService;

    @Resource
    private UUIDUtils uuidUtils;

    @Autowired
    private SessionHandler sessionHandler;

    @Autowired
    private CreditBehaviorService creditBehaviorService;

    @GetMapping("")
    @ApiOperation(value = "查询违规行为信息", notes = "参数为违规行为信息对象")
    public MessageResp getRecord(HttpServletRequest request, @ModelAttribute TaxiViolateRecord param) {
        MessageResp result = new MessageResp();
        PageInfo<TaxiViolateRecord> pageInfo = null;
        try {
            pageInfo = taxiViolateRecordService.selectByParam(param);
            result.setData(pageInfo.getList());
            result.setResultDesc("查询成功");
            result.setResult(Boolean.TRUE.toString());
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询失败,{}", e.getMessage());
            return CommonUtils.returnErrorInfo(null);
        }
        return result;
    }

    @GetMapping("/queryViolationOfStatistics")
    @ApiOperation(value = "查询违规行为统计", notes = "参数为违规行为信息对象")
    public MessageResp queryViolationOfStatistics(HttpServletRequest request, @ModelAttribute TaxiViolateRecord param) {
        MessageResp result = new MessageResp();
        PageInfo<TaxiViolationOfStatistics> pageInfo = null;
        try {
            Map<String, Object> resMap = taxiViolateRecordService.queryViolationOfStatistics(param);

            result.setData((List<TaxiViolationOfStatistics>) resMap.get("result"));
            PageBean pageBean = new PageBean();
            pageBean.setPageNo(String.valueOf(param.getPageNo()));
            pageBean.setPageSize(String.valueOf(param.getPageSize()));
            pageBean.setPageCount((String) resMap.get("pageCount"));
            pageBean.setPageDataCount((String) resMap.get("pageDataCount"));
            result.setResultDesc("查询成功");
            result.setResult(Boolean.TRUE.toString());
            result.setPageBean(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询失败,{}", e.getMessage());
            return CommonUtils.returnErrorInfo(null);
        }
        return result;
    }


    /**
     * 营运行为分析 拼客 不打表
     */
    @GetMapping("/queryOperationOfTheAnalysis")
    @ApiOperation(value = "查询营运行为分析", notes = "参数为违规行为信息对象")
    public MessageResp queryOperationOfTheAnalysis(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
                                                   @RequestParam(value = "corpId", required = false) Long corpId) {
        MessageResp resp = new MessageResp();

        try {
            String corpName = "全部";
            if (corpId != null) {
                TaxiBaseInfoCompany company = companyService.selectCompanyInfo(String.valueOf(corpId));
                if (company != null) corpName = company.getName();
            }
            String sTime = packgingDate(startTime, true);
            String eTime = packgingDate(endTime, false);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(startTime));
            //初始化返回list
            Map<String, TaxiOperationOfTheAnalysisExport> map = new LinkedHashMap<>();
            for (long d = cal.getTimeInMillis(); d <= sdf.parse(endTime).getTime(); d = get_D_Plaus_1(cal)) {
                TaxiOperationOfTheAnalysisExport operation = new TaxiOperationOfTheAnalysisExport();
                //每一天
                String time = sdf.format(d);
                operation.setTime(time);
                operation.setCarpoolTotal(0);
                operation.setNoMeter(0);
                operation.setCorpName(corpName);
                map.put(time, operation);
            }

            List<TaxiOperationOfTheAnalysisExport> queryList = taxiViolateRecordService.queryOperationOfTheAnalysis(sTime, eTime, corpId);
            for (TaxiOperationOfTheAnalysisExport operation : queryList) {
                if (map.containsKey(operation.getTime())) {
                    operation.setCorpName(corpName);
                    map.put(operation.getTime(), operation);
                }
            }

            resp.setData(map);
            resp.setResultDesc("查询成功");
            resp.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("添加失败");
        }
        return resp;
    }

    /**
     * 导出营运行为分析数据
     */
    @ResponseBody
    @RequestMapping("/operationOfTheAnalysisExport")
    public void driverDataExport(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
                                 @RequestParam(value = "corpId", required = false) Long corpId) throws Exception {

        String corpName = "全部";
        if (corpId != null) {
            TaxiBaseInfoCompany company = companyService.selectCompanyInfo(String.valueOf(corpId));
            if (company != null) corpName = company.getName();
        }
        String sTime = packgingDate(startTime, true);
        String eTime = packgingDate(endTime, false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(startTime));
        //初始化返回list中的每条数据
        Map<String, TaxiOperationOfTheAnalysisExport> map = new LinkedHashMap<>();
        for (long d = cal.getTimeInMillis(); d <= sdf.parse(endTime).getTime(); d = get_D_Plaus_1(cal)) {
            TaxiOperationOfTheAnalysisExport operation = new TaxiOperationOfTheAnalysisExport();
            //每一天
            String time = sdf.format(d);
            operation.setTime(time);
            operation.setCarpoolTotal(0);
            operation.setNoMeter(0);
            operation.setCorpName(corpName);
            map.put(time, operation);
        }

        List<TaxiOperationOfTheAnalysisExport> queryList = taxiViolateRecordService.queryOperationOfTheAnalysis(sTime, eTime, corpId);
        for (TaxiOperationOfTheAnalysisExport operation : queryList) {
            if (map.containsKey(operation.getTime())) {
                operation.setCorpName(corpName);
                map.put(operation.getTime(), operation);
            }
        }
        List<TaxiOperationOfTheAnalysisExport> resList = new ArrayList<>();
        for (String key : map.keySet()) {
            resList.add(map.get(key));
        }

        //需要导出的数据
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String xlsname = "运营行为分析表";
        String filepath = this.getClass().getClassLoader().getResource("").getPath() + "exceltemplate/" + xlsname + ".xlsx";
        // 导出数据到模板
        ExportExcel<TaxiOperationOfTheAnalysisExport> exportExcel = new ExportExcel<>(TaxiOperationOfTheAnalysisExport.class);
        exportExcel.exportExcelByExcelTemplateStyle2(filepath, xlsname, resList, "yyyy-MM-dd HH:mm:ss", request, response);
    }


    @PostMapping("/addViolateRecord")
    @ApiOperation(value = "添加违规行为信息", notes = "参数为违规行为信息对象")
    public MessageResp insertTaxiViolateRecord(HttpServletRequest request, @RequestBody TaxiViolateRecord record) {
        MessageResp result = new MessageResp();

        AuthUserResp user = sessionHandler.getUser(request);
        if (user != null) {
            record.setCreateBy(user.getUserId());
        }
        record.setUuid(uuidUtils.getLongUUID());
        try {
            int res = taxiViolateRecordService.addRecord(record);
            if (res < 1) {
                return CommonUtils.returnErrorInfo("添加失败");
            }

            result.setResultDesc("新增成功");
            result.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            log.info("添加违规行为信息异常:" + e.getMessage());
            return CommonUtils.returnErrorInfo("添加失败");
        }
        return result;
    }

    @PostMapping("/updateViolateRecord")
    @ApiOperation(value = "修改违规行为信息", notes = "参数为违规行为信息对象")
    public MessageResp updateTaxiViolateRecord(HttpServletRequest request, @RequestBody TaxiViolateRecord record) {
        MessageResp result = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_U);

        try {
            AuthUserResp authUser = sessionHandler.getUser(request);
            if (authUser != null) {
                logInfo.setOperatorName(authUser.getUserName());
            }
            logInfo.setOperation(record.toString());
            logInfo.setLoginIp("/taxi/violateRecord/updateViolateRecord");

            TaxiViolateRecord oldBean = taxiViolateRecordService.selectByUuid(record.getUuid());
            if (oldBean != null && oldBean.getDriverId() == null && record.getDriverId() != null) {
                //添加信用信息记录
                record.setEvaluationYear(oldBean.getEvaluationYear());
                record.setCorpId(oldBean.getCorpId());
                taxiViolateRecordService.saveCreditBehavior(record);
            }
            if (oldBean != null && oldBean.getDriverId() != null && !Objects.equals(oldBean.getDriverId(), record.getDriverId())) {
                //修改了考核对象
                record.setEvaluationYear(oldBean.getEvaluationYear());
                taxiViolateRecordService.updateCreditBehavior(record);
            }

            int res = taxiViolateRecordService.updateRecord(record);
            if (res > 0) {
                result.setResultDesc("修改成功");
                logInfo.setResult(Constants.RESULT_TYPE_TRUE);
                logService.insertInfo(logInfo);
                result.setResult(Boolean.TRUE.toString());
            }

        } catch (Exception e) {
            log.info("修改违规行为信息异常:" + e.getMessage());
            return CommonUtils.returnErrorInfo("修改失败");
        }
        return result;
    }

    @DeleteMapping("/deleteViolateRecord/{uuid}")
    @ApiOperation(value = "删除违规行为信息", notes = "参数UUID")
    public MessageResp deleteTaxiViolateRecord(@PathVariable(value = "uuid") Long uuid) {
        MessageResp result = new MessageResp();
        try {
            int res = taxiViolateRecordService.deleRecord(uuid);

            //根据记录id查找信用信息id
            List<Long> creditBehaviorIds = creditBehaviorService.findUuidByRecordId(uuid);
            if (CommonUtils.isListNotNull(creditBehaviorIds)) {
                for (Long creditBehaviorId : creditBehaviorIds) {
                    creditBehaviorService.delete(creditBehaviorId);
                }
            }

            if (res > 0) {
                result.setResultDesc("删除成功");
                result.setResult(Boolean.TRUE.toString());
            }

        } catch (NumberFormatException e) {
            log.info("删除违规行为信息异常:" + e.getMessage());
            return CommonUtils.returnErrorInfo("删除失败");
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

    protected static long get_D_Plaus_1(Calendar c) {
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
        return c.getTimeInMillis();
    }

}
