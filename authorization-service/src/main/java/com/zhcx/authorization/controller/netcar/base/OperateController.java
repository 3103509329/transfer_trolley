
package com.zhcx.authorization.controller.netcar.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.*;
import com.zhcx.netcarbasic.facade.netcar.CompanyService;
import com.zhcx.netcarbasic.facade.netcar.OperateService;
import com.zhcx.netcarbasic.params.OperateParam;
import com.zhcx.netcarbasic.pojo.netcar.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/netcar/operate")
@Api(value = "网约车企业营运数据", tags = "网约车企业营运数据")
public class OperateController {

    private Logger log = LoggerFactory.getLogger(OperateController.class);

    @Autowired
    private OperateService operateService;

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;

    @Autowired
    private CompanyService companyService;
    @ApiOperation(value = "查询车辆经营上线列表", notes = "查询车辆经营上线列表")
    @GetMapping("/login")
    public MessageResp queryOperateLoginList(HttpServletRequest request,
                                             @ModelAttribute OperateParam param) {
        MessageResp messageResp = new MessageResp();
        try {
            param.setCompanyId(roleAuthenticationUtils.getCompanyId(request, param.getCompanyId()));

            PageInfo<NetcarOperateLogin> pageInfo = operateService.queryOperateLoginList(param);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    @ApiOperation(value = "查询车辆经营下线列表", notes = "查询车辆经营下线列表")
    @GetMapping("/logout")
    public MessageResp queryOperateLogoutList(HttpServletRequest request,
                                              @ModelAttribute OperateParam param) {
        MessageResp messageResp = new MessageResp();
        try {
            param.setCompanyId(roleAuthenticationUtils.getCompanyId(request, param.getCompanyId()));

            PageInfo<NetcarOperateLogout> pageInfo = operateService.queryOperateLogoutList(param);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    @ApiOperation(value = "查询车辆经营出发列表", notes = "查询车辆经营出发列表")
    @GetMapping("/depart")
    public MessageResp queryOperateDepartList(HttpServletRequest request,
                                              @ModelAttribute OperateParam param) {
        MessageResp messageResp = new MessageResp();
        try {
            param.setCompanyId(roleAuthenticationUtils.getCompanyId(request, param.getCompanyId()));

            PageInfo<NetcarOperateDepart> pageInfo = operateService.queryOperateDepartList(param);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    @ApiOperation(value = "查询车辆经营到达列表", notes = "查询车辆经营到达列表")
    @GetMapping("/arrive")
    public MessageResp queryOperateArriveList(HttpServletRequest request, @ModelAttribute OperateParam param) {
        MessageResp messageResp = new MessageResp();
        try {
            param.setCompanyId(roleAuthenticationUtils.getCompanyId(request, param.getCompanyId()));

            PageInfo<NetcarOperateArrive> pageInfo = operateService.queryOperateArriveList(param);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    @ApiOperation(value = "查询车辆经营支付列表", notes = "查询车辆经营支付列表")
    @GetMapping("/pay")
    public MessageResp queryOperatePayList(HttpServletRequest request, @ModelAttribute OperateParam param) {
        MessageResp messageResp = new MessageResp();
        try {
            param.setCompanyId(roleAuthenticationUtils.getCompanyId(request, param.getCompanyId()));

            PageInfo<NetcarOperatePay> pageInfo = operateService.queryOperatePayList(param);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    @ApiOperation(value = "查询车辆经营详情列表", notes = "查询车辆经营详情列表")
    @GetMapping("/detail")
    public MessageResp queryOperateInfoList(HttpServletRequest request, @ModelAttribute OperateParam param) {
        MessageResp messageResp = new MessageResp();
        try {
            param.setCompanyId(roleAuthenticationUtils.getCompanyId(request, param.getCompanyId()));

            PageInfo<NetcarOperateInfo> pageInfo = operateService.queryOperateInfoList(param);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }


}

