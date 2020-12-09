
package com.zhcx.netcar.netcarservice.controller.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarOrderCancel;
import com.zhcx.netcar.pojo.base.NetcarOrderCreate;
import com.zhcx.netcar.pojo.base.NetcarOrderInfo;
import com.zhcx.netcar.pojo.base.NetcarOrderMatch;
import com.zhcx.netcar.facade.base.OrderService;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import com.zhcx.netcar.netcarservice.utils.RoleAuthenticationUtils;
import com.zhcx.netcar.params.OrderParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/11/28 14:01
 **/

@RestController
@RequestMapping("/netcar/order")
@Api(value = "order", tags = "网约车订单业务接口")
public class OrderController {

    private Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;

    @ApiOperation(value = "查询订单发起基本信息", notes = "查询订单发起基本信息")
    @GetMapping("/create")
    public MessageResp queryOrderCreateList(HttpServletRequest request, @ModelAttribute OrderParam param){
        MessageResp messageResp = new MessageResp();
        try {
            param.setCompanyId(roleAuthenticationUtils.getCompanyId(request, param.getCompanyId()));

            PageInfo<NetcarOrderCreate> pageInfo = orderService.queryOrderCreateList(param);
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

    @ApiOperation(value = "查询订单成功列表", notes = "查询订单成功列表")
    @GetMapping("/match")
    public MessageResp queryOrderMatchList(HttpServletRequest request, @ModelAttribute OrderParam param){
        MessageResp messageResp = new MessageResp();
        try {
            param.setCompanyId(roleAuthenticationUtils.getCompanyId(request, param.getCompanyId()));

            PageInfo<NetcarOrderMatch> pageInfo = orderService.queryOrderMatchList(param);
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

    @ApiOperation(value = "查询订单撤销列表", notes = "查询订单撤销列表")
    @GetMapping("/cancel")
    public MessageResp queryOrderCancelList(HttpServletRequest request, @ModelAttribute OrderParam param){
        MessageResp messageResp = new MessageResp();
        try {
            param.setCompanyId(roleAuthenticationUtils.getCompanyId(request, param.getCompanyId()));

            PageInfo<NetcarOrderCancel> pageInfo = orderService.queryOrderCancelList(param);
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

    @ApiOperation(value = "查询订单综合信息", notes = "查询订单综合信息")
    @GetMapping("/detail")
    public MessageResp queryOrderInfoList(HttpServletRequest request, @ModelAttribute OrderParam param){
        MessageResp messageResp = new MessageResp();
        try {
            param.setCompanyId(roleAuthenticationUtils.getCompanyId(request, param.getCompanyId()));

            param.setSearchType("orderId");
            PageInfo<NetcarOrderInfo> pageInfo = orderService.queryOrderInfoList(param);
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

