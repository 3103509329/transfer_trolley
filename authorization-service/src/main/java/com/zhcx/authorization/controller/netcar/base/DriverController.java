package com.zhcx.authorization.controller.netcar.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.*;
import com.zhcx.netcarbasic.facade.netcar.DriverService;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoDriver;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 11:35
 */
@RestController
@RequestMapping("/netcar/baseinfo/driver")
public class DriverController {

    private Logger log = LoggerFactory.getLogger(DriverController.class);

    @Autowired
    private DriverService driverService;

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;

    /**
     * 驾驶员基本信息查询
     *
     * @return
     */
    @ApiOperation(value = "驾驶员基本信息查询", notes = "")
    @GetMapping
    public MessageResp selectDriverList(HttpServletRequest request,
                                        @RequestParam(required = false) String companyId,
                                        @RequestParam(required = false) String type,
                                        @RequestParam(required = false) String keyword,
                                        @RequestParam(defaultValue = "1") Integer pageNo,
                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                        @RequestParam(required = false) String orderBy) {

        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoDriver> pageInfo = null;
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            pageInfo = driverService.selectDriverList(companyId, type, keyword, pageNo, pageSize, orderBy);
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
     * 查询公司下所有驾驶员
     */
    @ApiOperation(value = "查询公司所有驾驶员驾驶证号", notes = "查询公司所有驾驶员驾驶证号")
    @GetMapping("/queryLicenseList")
    public MessageResp queryLicenseIdListByCompanyId(HttpServletRequest request, @RequestParam String companyId) {
        MessageResp messageResp = new MessageResp();
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            List list = driverService.queryLicenseIdListByCompanyId(companyId);
            messageResp.setData(list);
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
     * 驾驶员基本信息查询(不合规)
     *
     * @return
     */
    @ApiOperation(value = "驾驶员基本信息查询(不合规)", notes = "")
    @GetMapping("/illegal")
    public MessageResp selectIllegalDriverList(HttpServletRequest request,
                                               @RequestParam(required = false) String companyId,
                                               @RequestParam(required = false) String keyword,
                                               @RequestParam(required = false) String type,
                                               @RequestParam(defaultValue = "1") Integer pageNo,
                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                               @RequestParam(required = false) String orderBy) {

        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoDriver> pageInfo = null;
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            pageInfo = driverService.selectIllegalDriverList(companyId, type, keyword, pageNo, pageSize, orderBy);
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
