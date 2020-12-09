package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.DateUtil;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.authorization.utils.excel.ExcelUtils;
import com.zhcx.basicdata.facade.taxi.TaxiMileageService;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoCompany;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoVehicle;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

/**
 * @program: authorization-service
 * @ClassName:TaxiMielageController
 * @description: 出租车里程类
 * @author: ZhangKai
 * @create: 2019-02-26 09:45
 **/
@RestController
@RequestMapping("/taxi/mileage")
public class TaxiMileageController {

    @Autowired
    private TaxiMileageService taxiMileageService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    /**
     * 同步里程
     *
     * @param param 数据字符串
     * @return
     */
    @PostMapping("/syncMileage")
    public MessageResp syncMileage(@RequestBody JSONArray param) {
        MessageResp resp = new MessageResp();
        boolean flag = false;
        try {
            flag = taxiMileageService.syncMileage(param.toJSONString());
            resp.setResult(String.valueOf(flag));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setResult(Boolean.FALSE.toString());
        }


        return resp;
    }

    /**
     * 企业营运统计
     */
    @GetMapping("/getMileageReport")
    public MessageResp getMonthlyReport(@ModelAttribute TaxiBaseInfoCompany company) {
        MessageResp resp = new MessageResp();
        PageInfo pageInfo = null;
        try {
            pageInfo = taxiMileageService.getMileageReport(company);
            resp.setData(pageInfo.getList());
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setResult(Boolean.FALSE.toString());
        }
        return resp;
    }


    @GetMapping("/getMileageStatistics")
    public MessageResp getMileageStatistics(String startTime, String uuid, String isYear) {
        MessageResp resp = new MessageResp();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> queryRes = null;
        try {
            if (StringUtils.isNotBlank(uuid)) param.put("corpId", uuid);
            param.put("date", startTime);
            param.put("isYear", isYear);
            queryRes = taxiMileageService.getMileageStatistics(param);
            resp.setData(queryRes);
            resp.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setResult(Boolean.FALSE.toString());
        }
        return resp;
    }

    /**
     * 车辆里程统计
     * dateType year 年 month 月 day 选择日期时间段
     */
    @GetMapping("/getVehicleMileageReport")
    public MessageResp getMonthlyReport(@ModelAttribute TaxiBaseInfoVehicle vehicle, @RequestParam("dateType") String dateType) {
        MessageResp resp = new MessageResp();
        PageInfo pageInfo = null;
        try {
            pageInfo = taxiMileageService.getVehicleMileageReport(vehicle, dateType);
            resp.setData(pageInfo.getList());
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setResult(Boolean.FALSE.toString());
        }
        return resp;
    }

    /**
     * 车辆里程统计 导出
     */
    @ResponseBody
    @RequestMapping("/getVehicleMileageReportExport")
    public void driverDataExport(HttpServletRequest request, HttpServletResponse response,
                                 @ModelAttribute TaxiBaseInfoVehicle vehicle, @RequestParam("dateType") String dateType) throws Exception {
        vehicle.setPageNo(1);
        vehicle.setPageSize(60000);
        PageInfo pageInfo = taxiMileageService.getVehicleMileageReport(vehicle, dateType);
        List<JSONObject> list = new ArrayList<>();
        list = (List<JSONObject>) pageInfo.getList();
        String newTime = DateUtil.getYMDHMSFormat(new Date());
        String[][] content = new String[list.size()][];
        //大标题
        String spreadhead = null;
        if (StringUtils.equals("year", dateType)) {
            spreadhead = vehicle.getStartTime() + " 车辆里程统计";
        }
        if (StringUtils.equals("month", dateType)) {
            spreadhead = vehicle.getStartTime() + " 车辆里程统计";
        }
        if (StringUtils.equals("day", dateType)) {
            spreadhead = vehicle.getStartTime() + " 至 " + vehicle.getEndTime() + " 车辆里程统计";
        }
        //excel小标题
        String[] title = {"序号", "企业名称", "车牌号", "总里程/km", "运营里程/km"};
        //excel文件名
        String fileName = "车辆里程统计" + newTime + ".xls";
        //sheet名
        String sheetName = "车辆里程";
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            JSONObject jsonObject = list.get(i);
            content[i][0] = String.valueOf(i + 1);
            content[i][1] = jsonObject.getString("corpName");
            content[i][2] = jsonObject.getString("plateNum");
            content[i][3] = String.valueOf(jsonObject.get("mileage"));
            content[i][4] = String.valueOf(jsonObject.get("eff_mileage"));

        }
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook2(spreadhead, sheetName, title, content, null);
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

}
