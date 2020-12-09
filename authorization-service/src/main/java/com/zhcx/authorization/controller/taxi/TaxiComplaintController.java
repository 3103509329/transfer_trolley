package com.zhcx.authorization.controller.taxi;


import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.DateUtil;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.authorization.utils.excel.ExportExcel;
import com.zhcx.basicdata.facade.taxi.TaxiComplaintService;
import com.zhcx.basicdata.pojo.taxi.TaxiComplaint;
import com.zhcx.basicdata.pojo.taxi.TaxiComplaintExport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/taxi/complaint")
@Api(value = "taxiComplaint", tags = "出租车投诉接口")
public class TaxiComplaintController {

    private Logger log = LoggerFactory.getLogger(TaxiComplaintController.class);

    @Autowired
    private TaxiComplaintService complaintService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @GetMapping("/queryComplaintList")
    @ApiOperation(value = "投诉列表", notes = "参数status，取回状态")
    public MessageResp queryComplaintList(@ModelAttribute TaxiComplaint param) {
        MessageResp resp = new MessageResp();
        try {
            PageInfo<TaxiComplaint> pageInfo = complaintService.queryComplaintList(param);
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setData(pageInfo.getList());
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询投诉列表成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("查询投诉列表异常");
        }
        return resp;
    }


    /**
     * 导出投诉记录
     */
    @ResponseBody
    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, @ModelAttribute TaxiComplaint param) throws Exception {

        if (param != null && StringUtils.isBlank(param.getStartTime())) {
            //默认近30天数据
            String nowDate = DateUtil.getYMDFormat(new Date());
            param.setEndTime(nowDate + " 23:59:59");
            String date = DateUtil.getOffsetsDay(DateUtil.getYMDFormat(new Date()), -30);
            param.setStartTime(date + " 00:00:00");
        }

        //需要导出的数据
        List<TaxiComplaintExport> list = complaintService.findExportData(param);
        int serialNumber = 1;
        for (TaxiComplaintExport complaintExport : list) {

            String sourceType = complaintExport.getSourceType();
            if (StringUtils.equals("10", sourceType)) {
                complaintExport.setSourceType("电话");
            } else if (StringUtils.equals("20", sourceType)) {
                complaintExport.setSourceType("互联网");
            } else {
                complaintExport.setSourceType("其他");
            }

            String status = complaintExport.getStatus();
            if (StringUtils.equals("10", status)) {
                complaintExport.setStatus("受理");
            } else if (StringUtils.equals("20", status)) {
                complaintExport.setStatus("处理中");
            } else if (StringUtils.equals("30", status)) {
                complaintExport.setStatus("已处理");
            }

            String result = complaintExport.getResult();
            if (StringUtils.equals("10", result)) {
                complaintExport.setResult("情况属实,已处理相关人员");
            } else if (StringUtils.equals("20", result)) {
                complaintExport.setResult("情况不属实,不予处理");
            }
            complaintExport.setSerialNumber(serialNumber);
            serialNumber++;
        }

        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String xlsname = "投诉信息汇总表";
        String filepath = this.getClass().getClassLoader().getResource("").getPath() + "exceltemplate/" + xlsname + ".xlsx";
        // 导出数据到模板
        ExportExcel<TaxiComplaintExport> exportExcel = new ExportExcel<>(TaxiComplaintExport.class);
        exportExcel.exportExcelByExcelTemplateStyle2(filepath, xlsname, list, "yyyy-MM-dd HH:mm:ss", request, response);

    }

    @GetMapping("/queryComplaintInfo")
    @ApiOperation(value = "投诉详情", notes = "uuid")
    public MessageResp queryComplaintInfo(@RequestParam Long uuid) {
        MessageResp resp = new MessageResp();
        try {
            TaxiComplaint complaint = complaintService.queryComplaintInfo(uuid);
            resp.setData(complaint);
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询投诉详情成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatusCode("00");
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("查询投诉详情异常");
        }
        return resp;
    }

    @PutMapping("/updateComplaintInfo")
    @ApiOperation(value = "更新投诉信息", notes = "参数alarm")
    public MessageResp updateComplaintInfo(HttpServletRequest request, @RequestBody TaxiComplaint complaint) {
        MessageResp resp = new MessageResp();
        try {
            AuthUserResp user = sessionHandler.getUser(request);
            complaint.setUpdateBy(user.getUserName());
            complaint.setUpdateTime(new Date());
            complaintService.updateComplaint(complaint);
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("更新投诉信息成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("更新投诉信息异常");
        }
        return resp;
    }

    @PostMapping("/addComplaintInfo")
    @ApiOperation(value = "添加投诉信息", notes = "参数alarm")
    public MessageResp addComplaintInfo(HttpServletRequest request, @RequestBody TaxiComplaint complaint) {
        MessageResp resp = new MessageResp();
        try {
            AuthUserResp user = sessionHandler.getUser(request);
            Date current = new Date();
            complaint.setUpdateBy(user.getUserName());
            complaint.setUpdateTime(current);
            complaint.setCreateBy(user.getUserName());
            complaint.setCreateTime(current);
            complaint.setStatus("10");
            complaintService.addComplaint(complaint);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("添加投诉信息成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("添加投诉信息异常");
        }
        return resp;
    }

    @DeleteMapping("/deleteComplaintInfo")
    @ApiOperation(value = "删除投诉信息", notes = "参数uuid")
    public MessageResp deleteComplaintInfo(@RequestParam Long uuid) {
        MessageResp resp = new MessageResp();
        try {
            complaintService.deleteComplaint(uuid);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("删除投诉信息成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("删除投诉信息异常");
        }
        return resp;
    }

    @PutMapping("/dealWithComplaint")
    @ApiOperation(value = "处理投诉", tags = "参数uuid， status， result")
    public MessageResp dealWithComplaint(HttpServletRequest request, Long uuid, String status, String result) {
        MessageResp resp = new MessageResp();
        try {
            AuthUserResp user = sessionHandler.getUser(request);
            Date current = new Date();
            TaxiComplaint complaint = new TaxiComplaint();
            complaint.setUpdateBy(user.getUserName());
            complaint.setUpdateTime(current);
            complaint.setUuid(uuid);
            complaint.setStatus(status);
            if (StringUtils.isNotBlank(result)) {
                complaint.setResult(result);
                complaint.setResultTime(current);
            }
            complaintService.dealWithComplaint(complaint);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("处理投诉成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("处理投诉异常");
        }
        return resp;
    }

}
