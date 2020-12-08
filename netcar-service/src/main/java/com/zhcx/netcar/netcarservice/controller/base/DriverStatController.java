package com.zhcx.netcar.netcarservice.controller.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoDriverStat;
import com.zhcx.netcar.facade.base.DriverStatService;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import com.zhcx.netcar.netcarservice.utils.RoleAuthenticationUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 13:33
 */
@RestController
@RequestMapping("/netcar/baseinfo/driverstat")
@Api(value = "driverstat", tags = "驾驶员统计信息")
public class DriverStatController {

    private Logger log = LoggerFactory.getLogger(DriverStatController.class);

    @Autowired
    private DriverStatService driverStatService;

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;

    /**
     * 驾驶员统计信息查询
     *
     * @return
     */
    @ApiOperation(value = "驾驶员统计信息查询", notes = "")
    @GetMapping
    public MessageResp selectDriverStatList(HttpServletRequest request,
                                            @RequestParam(required = false) String companyId,
                                            @RequestParam(required = false) String driverName,
                                            @RequestParam(required = false) String driverPhone,
                                            @RequestParam(required = false) String keyword,
                                            @RequestParam(defaultValue = "1") Integer pageNo,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoDriverStat> pageInfo = null;
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            pageInfo = driverStatService.selectDriverStatList(companyId, driverName, driverPhone, keyword, pageNo, pageSize, orderBy);
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
     * 根据驾驶证号查询驾驶员统计信息
     *
     * @return
     */
    @ApiOperation(value = "根据驾驶证号查询驾驶员统计信息", notes = "")
    @GetMapping("/licenseId")
    public MessageResp selectDriverStatByLicenseId(HttpServletRequest request,
                                                   @RequestParam String companyId,
                                                   @RequestParam String licenseId,
                                                   @RequestParam(defaultValue = "1") Integer pageNo,
                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                   @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoDriverStat> pageInfo = null;
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            pageInfo = driverStatService.selectDriverStatByLicenseId(companyId, licenseId, pageNo, pageSize, orderBy);
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
