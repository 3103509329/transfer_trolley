package com.zhcx.authorization.controller.netcar.workbench;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.FastDFSUtil;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.platformtonet.facade.workbench.WorkbenchService;
import com.zhcx.platformtonet.pojo.base.WorkbenchDriver;
import com.zhcx.platformtonet.pojo.base.WorkbenchVehicle;
import com.zhcx.platformtonet.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;


/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-15 15:27
 * 工作台
 */
@RestController
@RequestMapping("/netcar/workbench")
public class WorkbenchController {

    private static final Logger logger = LoggerFactory.getLogger(WorkbenchController.class);

    @Autowired
    WorkbenchService workbenchService;

    @Autowired
    FastDFSUtil fastDFSUtil;
    /**
     * 查看车辆监管平台
     * @return
     */
    @GetMapping("/vehicle")
    public MessageResp getVehicleMonitoring(@RequestParam(value = "pageNo",defaultValue = "1")int pageNo,
                                            @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        MessageResp result = new MessageResp();
        try {
            PageInfo<WorkbenchVehicle> pageInfo = workbenchService.getVehicleMonitoring(pageNo,pageSize);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * 年检过期
     */
    @GetMapping("/vehicle/annual/overdue")
    public MessageResp getAnnualInspectionOverdue(@RequestParam(value = "busiRegNumber",defaultValue = "")String busiRegNumber,
                                                  @RequestParam(value = "page",defaultValue = "1")int pageNo,
                                                  @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        MessageResp result = new MessageResp();
        try {
            PageInfo<VehicleAnnualInspectionExpired> pageInfo = workbenchService.getAnnualInspectionOverdue(busiRegNumber,pageNo,pageSize);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * 无车载终端的车辆
     */
    @GetMapping("/vehicle/not/terminal")
    public MessageResp getNotCarTerminal(@RequestParam(value = "busiRegNumber",defaultValue = "")String busiRegNumber,
                                         @RequestParam(value = "companyId",defaultValue = "")String companyId,
                                         @RequestParam(value = "page",defaultValue = "1")int pageNo,
                                         @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        MessageResp result = new MessageResp();
        try {
            PageInfo<VehicleNotTerminal> pageInfo = workbenchService.getNotCarTerminal(busiRegNumber,companyId,pageNo,pageSize);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * 保险金额不合格
     */
    @GetMapping("/vehicle/insured/amount/unqualified")
    public MessageResp getInsuredAmountUnqualified(@RequestParam(value = "busiRegNumber",defaultValue = "")String busiRegNumber,
                                                   @RequestParam(value = "page",defaultValue = "1")int pageNo,
                                                   @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        MessageResp result = new MessageResp();
        try {
            PageInfo<VehicleInsurExpired> pageInfo = workbenchService.getInsuredAmountUnqualified(busiRegNumber,pageNo,pageSize);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * 里程超限
     */
    @GetMapping("/vehicle/mileage/transfinite")
    public MessageResp getMileageTransfinite(@RequestParam(value = "busiRegNumber",defaultValue = "")String busiRegNumber,
                                             @RequestParam(value = "page",defaultValue = "1")int pageNo,
                                             @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        MessageResp result = new MessageResp();
        try {
            PageInfo<VehicleMileageBeyond> pageInfo = workbenchService.getMileageTransfinite(busiRegNumber,pageNo,pageSize);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }


    /**
     * 未运营
     */
    @GetMapping("/vehicle/not/operating")
    public MessageResp getNotOperating(@RequestParam(value = "busiRegNumber",defaultValue = "")String busiRegNumber,
                                       @RequestParam(value = "companyId",defaultValue = "")String companyId,
                                       @RequestParam(value = "page",defaultValue = "1")int pageNo,
                                       @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        MessageResp result = new MessageResp();
        try {
            PageInfo<VehicleNotOperating> pageInfo = workbenchService.getNotOperating(busiRegNumber,companyId,pageNo,pageSize);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * 数据缺失
     */
    @GetMapping("/vehicle/lack/data")
    public MessageResp getLackData(@RequestParam(value = "busiRegNumber",defaultValue = "")String busiRegNumber,
                                   @RequestParam(value = "companyId",defaultValue = "")String companyId,
                                   @RequestParam(value = "page",defaultValue = "1")int pageNo,
                                   @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        MessageResp result = new MessageResp();
        try {
            PageInfo<VehicleInfoLack> pageInfo = workbenchService.getLackData(busiRegNumber,companyId,pageNo,pageSize);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * 保险到期
     */
    @GetMapping("/vehicle/insurance/due")
    public MessageResp getInsuranceDue(@RequestParam(value = "busiRegNumber",defaultValue = "")String busiRegNumber,
                                       @RequestParam(value = "page",defaultValue = "1")int pageNo,
                                       @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        MessageResp result = new MessageResp();
        try {
            PageInfo<VehicleInsurExpired> pageInfo = workbenchService.getInsuranceDue(busiRegNumber,pageNo,pageSize);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * 查看驾驶员监管平台
     * @return
     */
    @GetMapping("/driver")
    public MessageResp getDriverMonitoring(@RequestParam(value = "pageNo",defaultValue = "1")int pageNo,
                                           @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        MessageResp result = new MessageResp();
        try {
            PageInfo<WorkbenchDriver> pageInfo = workbenchService.getDriverMonitoring(pageNo,pageSize);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * 驾照过期
     * @return
     */
    @GetMapping("/driver/license/overdue")
    public MessageResp getLicenseOverdue(@RequestParam(value = "busiRegNumber",defaultValue = "")String busiRegNumber,
                                         @RequestParam(value = "page",defaultValue = "1")int pageNo,
                                         @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        MessageResp result = new MessageResp();
        try {
            PageInfo<DriverLicenseExpired> pageInfo = workbenchService.getLicenseOverdue(busiRegNumber,pageNo,pageSize);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * 年龄超标
     * @return
     */
    @GetMapping("/driver/age/excessive")
    public MessageResp getAgeExcessive(@RequestParam(value = "busiRegNumber",defaultValue = "")String busiRegNumber,
                                         @RequestParam(value = "page",defaultValue = "1")int pageNo,
                                         @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        MessageResp result = new MessageResp();
        try {
            PageInfo<DriverAgeBeyond> pageInfo = workbenchService.getAgeExcessive(busiRegNumber,pageNo,pageSize);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * 持证未上岗
     * @return
     */
    @GetMapping("/driver/not/mount/guard")
    public MessageResp getNotMountGuard(@RequestParam(value = "busiRegNumber",defaultValue = "")String busiRegNumber,
                                        @RequestParam(value = "companyId",defaultValue = "")String companyId,
                                        @RequestParam(value = "page",defaultValue = "1")int pageNo,
                                        @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        MessageResp result = new MessageResp();
        try {
            PageInfo<DriverNotOperating> pageInfo = workbenchService.getNotMountGuard(busiRegNumber,companyId,pageNo,pageSize);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * 数据缺失
     * @return
     */
    @GetMapping("/driver/lack/data")
    public MessageResp getDriverLackData(@RequestParam(value = "busiRegNumber",defaultValue = "")String busiRegNumber,
                                         @RequestParam(value = "companyId",defaultValue = "")String companyId,
                                         @RequestParam(value = "page",defaultValue = "1")int pageNo,
                                         @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        MessageResp result = new MessageResp();
        try {
            PageInfo<DriverInfoLack> pageInfo = workbenchService.getDriverLackData(busiRegNumber,companyId,pageNo,pageSize);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    @GetMapping("/update")
    public MessageResp update() {
        MessageResp result = new MessageResp();
        try {
            workbenchService.update();
            result.setData(null);
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }

    /**
     * excel导出
     * @param fileName 导出之后的文件名
     * @param url 文件绝对地址
     * @param response
     * @throws Exception
     */
    @GetMapping("/download")
    public void downloadExcel(@RequestParam (value = "fileName",required = false,defaultValue = "")String fileName,
                                                 @RequestParam (value = "url",required = false,defaultValue = "")String url,
                                                 HttpServletResponse response) throws Exception{
        try {
            logger.info("工作台下载请求url:" + url);
            logger.info("工作台下载请求fileName:" + fileName);
             byte[] buffer = fastDFSUtil.getFile(url);
             fileName = URLEncoder.encode(fileName, "UTF-8");
             response.reset();
             response.setContentType("application/octet-stream;charset=UTF-8");
             response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
             OutputStream ouputStream = response.getOutputStream();
             ouputStream.write(buffer);
             ouputStream.flush();
             ouputStream.close();
         } catch (Exception e) {
             e.printStackTrace();
             logger.error("excel文件下载出现异常", e);
         }
    }



}
