package com.zhcx.authorization.controller.netcar.illegal;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.controller.netcar.base.VehicleController;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.netcarbasic.facade.netcar.DriverService;
import com.zhcx.netcarbasic.facade.netcar.VehicleService;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoCompany;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoDriver;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoVehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/7/25 0025 15:10
 **/
@RestController
@RequestMapping("/netcar/illegal")
public class IllegalYunZhengController {

    private Logger log = LoggerFactory.getLogger(IllegalYunZhengController.class);

    @Autowired
    private DriverService driverService;

    @Autowired
    private VehicleService vehicleService;

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
            PageInfo<NetcarBaseInfoCompany> pageInfo = vehicleService.getCompanys();
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
            PageInfo<NetcarBaseInfoCompany> pageInfo = vehicleService.getCompanyList(companyId, pageNo, pageSize, orderBy);
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
            PageInfo<NetcarBaseInfoDriver> pageInfo = driverService.getDriverIllegal(keyword, type, companyId, pageNo, pageSize, orderBy);
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
            PageInfo<NetcarBaseInfoVehicle> pageInfo = vehicleService.getVehilceIllegal(companyId, vehicleNo, pageNo, pageSize, orderBy);
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
