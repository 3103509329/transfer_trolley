package com.zhcx.authorization.controller.netcar.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.*;
import com.zhcx.netcarbasic.facade.netcar.DriverEducateService;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoDriverEducate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 11:38
 */
@RestController
@RequestMapping("/netcar/baseinfo/drivereducate")
@Api(value = "drivereducate", tags = "驾驶员培训信息")
public class DriverEducateController {

    private Logger log = LoggerFactory.getLogger(DriverEducateController.class);

    @Autowired
    private DriverEducateService driverEducateService;

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;

    /**
     * 驾驶员培训信息查询
     *
     * @return
     */
    @ApiOperation(value = "驾驶员培训信息查询", notes = "")
    @GetMapping
    public MessageResp selectDriverEducateList(HttpServletRequest request,
                                               @RequestParam(required = false) String companyId,
                                               @RequestParam(required = false) String licenseId,
                                               @RequestParam(defaultValue = "1") Integer pageNo,
                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                               @RequestParam(required = false) String orderBy) {

        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoDriverEducate> pageInfo = null;
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            pageInfo = driverEducateService.selectDriverEducateList(companyId, licenseId, pageNo, pageSize, orderBy);
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
     * 根据驾照获取驾驶员培训信息查询
     *
     * @return
     */
    @ApiOperation(value = "根据驾照获取驾驶员培训信息查询", notes = "")
    @GetMapping("/licenseId")
    public MessageResp selectDriverEducateByLicenseId(HttpServletRequest request,
                                                      @RequestParam String companyId,
                                                      @RequestParam String licenseId,
                                                      @RequestParam(defaultValue = "1") Integer pageNo,
                                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                                      @RequestParam(required = false) String orderBy) {

        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoDriverEducate> pageInfo = null;
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            pageInfo = driverEducateService.selectDriverEducateByLicenseId(companyId, licenseId, pageNo, pageSize, orderBy);
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
