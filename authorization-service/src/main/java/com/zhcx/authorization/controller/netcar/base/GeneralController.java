package com.zhcx.authorization.controller.netcar.base;

import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.netcarbasic.facade.netcar.CompanyService;
import com.zhcx.netcarbasic.facade.netcar.DriverService;
import com.zhcx.netcarbasic.facade.netcar.VehicleService;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoCompany;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoDriver;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoVehicle;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/11/25 16:28
 * 综合信息
 **/
@RestController
@RequestMapping("/netcar/general")
@Api(value = "综合信息",tags = "综合信息")
public class GeneralController {

    private Logger log = LoggerFactory.getLogger(GeneralController.class);

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private DriverService driverService;


    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;

    @ApiOperation(value = "车辆信息查询", notes = "")
    @GetMapping("/vehicleinfo")
    public MessageResp vehicleInfo(HttpServletRequest request,
                                   @RequestParam(required = false) String companyId,
                                   @RequestParam(required = false) String vehicleNo) {

        MessageResp messageResp = new MessageResp<>();
        try{
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            NetcarBaseInfoVehicle netcarBaseInfoVehicle = vehicleService.selectVehicleInfo(companyId,vehicleNo);
            messageResp.setData(netcarBaseInfoVehicle);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        }catch (Exception e){
            log.error(e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("查询异常");
        }
        return messageResp;
    }

    @ApiOperation(value = "企业信息查询", notes = "")
    @GetMapping("/companyinfo")
    public MessageResp companyInfo(HttpServletRequest request, @RequestParam String companyId,@RequestParam(required = false) Integer address){
        MessageResp messageResp = new MessageResp<>();
        try{
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            NetcarBaseInfoCompany netcarBaseInfoCompany = companyService.selectByCompanyId(companyId);
            messageResp.setData(netcarBaseInfoCompany);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        }catch (Exception e){
            log.error(e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("查询异常");
        }
        return messageResp;
    }

    @ApiOperation(value = "驾驶员信息查询", notes = "")
    @GetMapping("/driverinfo")
    public MessageResp driverInfo(HttpServletRequest request,
                                  @RequestParam(required = false) String companyId,
                                  @RequestParam(required = false)String licenseId) {
        MessageResp messageResp = new MessageResp<>();
        try{
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            NetcarBaseInfoDriver netcarBaseInfoDriver = driverService.selectDriverInfoByCompanyId(companyId, licenseId);
            messageResp.setData(netcarBaseInfoDriver);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        }catch (Exception e){
            log.error(e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("查询异常");
        }
        return messageResp;
    }


}
