package com.zhcx.authorization.controller.netcar.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.*;
import com.zhcx.netcarbasic.facade.netcar.VehicleService;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoVehicle;
import io.swagger.annotations.Api;
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
 * @date 2018-11-24 11:24
 */
@RestController
@RequestMapping("/netcar/baseinfo/vehicle")
@Api(value ="vehicle" ,tags = "车辆信息")
public class VehicleController {

    private Logger log = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;
    /**
     * 车辆信息查询
     * @return
     */
    @ApiOperation(value = "车辆信息查询", notes = "")
    @GetMapping
    public MessageResp selectVehicleList(HttpServletRequest request,
                                         @RequestParam(required = false) String companyId,
                                         @RequestParam(required = false)String vehicleNo,
                                         @RequestParam(defaultValue = "1") Integer pageNo,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(required = false) String orderBy ){
        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoVehicle> pageInfo = null;
        try{
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);

            pageInfo =  vehicleService.selectVehicleList(companyId, vehicleNo, pageNo, pageSize, orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    /**
     * 根据公司ID查询车辆号牌
     * @return
     */
    @ApiOperation(value = "根据公司ID查询车辆号牌", notes = "")
    @GetMapping("/vehicleNos")
    public MessageResp queryVehicleNoList(HttpServletRequest request,
                                         @RequestParam(required = false) String companyId){
        MessageResp messageResp = new MessageResp();
        try{
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);

            List<String> vehicleNoList =  vehicleService.selectVehicleNoListByCompanyId(companyId);
            messageResp.setData(vehicleNoList);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    /**
     * 车辆信息查询
     * @return
     */
    @ApiOperation(value = "车辆信息查询", notes = "")
    @GetMapping("/illegal")
    public MessageResp selectIllegalVehicleList(HttpServletRequest request,
                                         @RequestParam(required = false) String companyId,
                                         @RequestParam(required = false)String vehicleNo,
                                         @RequestParam(defaultValue = "1") Integer pageNo,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(required = false) String orderBy ){
        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoVehicle> pageInfo = null;
        try{
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);

            pageInfo =  vehicleService.selectIllegalVehicleList(companyId, vehicleNo, pageNo, pageSize, orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }



}
