package com.zhcx.netcar.netcarservice.controller.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoDriverApp;
import com.zhcx.netcar.facade.base.DriverAppService;
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
 * @date 2018-11-24 13:29
 */
@Api(value = "驾驶员移动终端信息", tags = "驾驶员移动终端信息接口")
@RestController
@RequestMapping("/netcar/baseinfo/driverapp")
public class DriverAppController {

    private Logger log = LoggerFactory.getLogger(DriverAppController.class);

    @Autowired
    private DriverAppService driverAppService;

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;

    /**
     * 驾驶员移动终端信息查询
     *
     * @return
     */
    @ApiOperation(value = "驾驶员移动终端信息查询", notes = "")
    @GetMapping
    public MessageResp selectDriverAppList(HttpServletRequest request,
                                           @RequestParam(required = false) String companyId,
                                           @RequestParam(required = false) String keyword,
                                           @RequestParam(defaultValue = "1") Integer pageNo,
                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                           @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoDriverApp> pageInfo = null;
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            pageInfo = driverAppService.selectDriverAppList(companyId, keyword, pageNo, pageSize, orderBy);
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
     * 驾驶员移动终端信息查询
     *
     * @return
     */
    @ApiOperation(value = "驾驶员移动终端信息查询", notes = "")
    @GetMapping("/licenseId")
    public MessageResp selectDriverAppByLicenseId(HttpServletRequest request,
                                                  @RequestParam String companyId,
                                                  @RequestParam String licenseId,
                                                  @RequestParam(defaultValue = "1") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoDriverApp> pageInfo = null;
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            pageInfo = driverAppService.selectDriverAppByLicenseId(companyId, licenseId, pageNo, pageSize, orderBy);
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
