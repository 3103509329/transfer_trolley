package com.zhcx.authorization.controller.netcar.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.*;
import com.zhcx.netcarbasic.facade.netcar.*;
import com.zhcx.netcarbasic.params.PassengerComplainParam;
import com.zhcx.netcarbasic.params.QueryParam;
import com.zhcx.netcarbasic.params.RatedDriverParam;
import com.zhcx.netcarbasic.pojo.netcar.NetcarRatedDriver;
import com.zhcx.netcarbasic.pojo.netcar.NetcarRatedDriverPunish;
import com.zhcx.netcarbasic.pojo.netcar.NetcarRatedPassenger;
import com.zhcx.netcarbasic.pojo.netcar.NetcarRatedPassengerComplaint;
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
 * @date 2019/1/10 20:10
 **/
@RestController
@RequestMapping("/netcar/rated")
@Api(value = "网约车服务质量", tags = "网约车服务质量")
public class RatedController {

    private Logger log = LoggerFactory.getLogger(RatedController.class);

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;

    @Autowired
    private RatedDriverService ratedDriverService;

    @Autowired
    private RatedDriverPunishService ratedDriverPunishService;

    @Autowired
    private RatedPassengerComplaintService ratedPassengerComplaintService;

    @Autowired
    private RatedPassengerService ratedPassengerService;

    @ApiOperation(value = "查询驾驶员信誉信息", notes = "查询驾驶员信誉信息")
    @GetMapping("/driver")
    public MessageResp queryDriver(HttpServletRequest request, @ModelAttribute RatedDriverParam param){
        MessageResp messageResp = new MessageResp();
        try {
            param.setCompanyId(roleAuthenticationUtils.getCompanyId(request, param.getCompanyId()));

            PageInfo<NetcarRatedDriver> pageInfo = ratedDriverService.queryDriver(param);
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

    @ApiOperation(value = "查询驾驶员处罚信息", notes = "查询驾驶员处罚信息")
    @GetMapping("/driverpunish")
    public MessageResp queryDriverPunish(HttpServletRequest request, @ModelAttribute RatedDriverParam param){
        MessageResp messageResp = new MessageResp();
        try {
            param.setCompanyId(roleAuthenticationUtils.getCompanyId(request, param.getCompanyId()));

            PageInfo<NetcarRatedDriverPunish> pageInfo = ratedDriverPunishService.queryDriverPunish(param);
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

    @ApiOperation(value = "查询乘客投诉信息", notes = "查询乘客投诉信息")
    @GetMapping("/passengerccomplaint")
    public MessageResp queryPassengerComplaint(HttpServletRequest request, @ModelAttribute PassengerComplainParam param){
        MessageResp messageResp = new MessageResp();
        try {
            param.setCompanyId(roleAuthenticationUtils.getCompanyId(request, param.getCompanyId()));

            PageInfo<NetcarRatedPassengerComplaint> pageInfo = ratedPassengerComplaintService.queryPassengerComplaintList(param);
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

    @ApiOperation(value = "查询乘客评价信息", notes = "查询乘客评价信息")
    @GetMapping("/passenger")
    public MessageResp queryPassengerRated(HttpServletRequest request, @ModelAttribute QueryParam param){
        MessageResp messageResp = new MessageResp();
        try {
            param.setCompanyId(roleAuthenticationUtils.getCompanyId(request, param.getCompanyId()));

            PageInfo<NetcarRatedPassenger> pageInfo = ratedPassengerService.queryPassengerRatedList(param);
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
