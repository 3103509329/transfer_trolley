package com.zhcx.netcar.netcarservice.controller.workbench;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zhcx.basic.dubbo.BaseInfoEmplDubboService;
import com.zhcx.basic.dubbo.BaseInfoVehiclelicenceDubboService;
import com.zhcx.basic.model.BaseInfoCompany;
import com.zhcx.basic.model.BaseInfoEmpl;
import com.zhcx.basic.model.BaseInfoVehiclelicence;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/7/25 0025 15:10
 **/
@RestController
@RequestMapping("/netcar/illegal")
public class IllegalYunZhengController {

    private Logger log = LoggerFactory.getLogger(IllegalYunZhengController.class);

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoEmplDubboService baseInfoEmplService;

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoVehiclelicenceDubboService baseInfoVehiclelicenceService;

    /**
     * 获取违规企业列表（工作台）
     * 只返回不合规的企业
     *
     * @param request
     * @return
     */
    @GetMapping("/companys")
    public MessageResp getCompanys(HttpServletRequest request) {
        MessageResp messageResp = new MessageResp();
        try {
            PageInfo<BaseInfoCompany> pageInfo = baseInfoVehiclelicenceService.getCompanys();
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
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
     * 获取违规企业（工作台）
     *
     * @param request
     * @return
     */
    @GetMapping("/company")
    public MessageResp getCompanyIllegal(HttpServletRequest request,
                                         @RequestParam(defaultValue = "1") Integer pageNo,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(required = false) String companyId,
                                         @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        try {
            PageInfo<BaseInfoCompany> pageInfo = baseInfoVehiclelicenceService.getCompanyList(companyId, pageNo, pageSize, orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
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
     * 获取违规驾驶员（工作台）
     *
     * @param request
     * @return
     */
    @GetMapping("/driver")
    public MessageResp getDriverIllegal(HttpServletRequest request,
                                        @RequestParam(defaultValue = "1") Integer pageNo,
                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                        @RequestParam(required = false) String companyId,
                                        @RequestParam(required = false) String keyword,
                                        @RequestParam(required = false) String type,
                                        @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        try {
            PageInfo<BaseInfoEmpl> pageInfo = baseInfoEmplService.getDriverIllegal(keyword, type, companyId, pageNo, pageSize, orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
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
     * 获取违规车辆（工作台）
     *
     * @param request
     * @param companyId
     * @param vehicleNo
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping("/vehicle")
    public MessageResp getVehilceIllegal(HttpServletRequest request,
                                         @RequestParam(required = false) String companyId,
                                         @RequestParam(required = false) String vehicleNo,
                                         @RequestParam(defaultValue = "1") Integer pageNo,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        try {
            PageInfo<BaseInfoVehiclelicence> pageInfo = baseInfoVehiclelicenceService.getVehilceIllegal(companyId, vehicleNo, pageNo, pageSize, orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }

        return messageResp;

    }
}
