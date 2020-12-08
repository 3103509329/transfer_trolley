//package com.zhcx.netcar.netcarservice.controller.base;
//
//import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompany;
//import com.zhcx.netcar.pojo.base.NetcarBaseInfoDriver;
//import com.zhcx.netcar.pojo.base.NetcarBaseInfoVehicle;
//import com.zhcx.netcar.facade.base.CompanyService;
//import com.zhcx.netcar.facade.base.DriverService;
//import com.zhcx.netcar.facade.base.VehicleService;
//import com.zhcx.netcar.netcarservice.utils.MessageResp;
//import com.zhcx.netcar.netcarservice.utils.RoleAuthenticationUtils;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * @author Lee
// * @email 570815140@qq.com
// * @date 2018/11/25 16:28
// * 综合信息
// **/
//@RestController
//@RequestMapping("/netcar/general")
//@Api(value = "综合信息",tags = "综合信息")
//public class GeneralController {
//
//    private Logger log = LoggerFactory.getLogger(GeneralController.class);
//
//    @Autowired
//    private VehicleService vehicleService;
//
//    @Autowired
//    private CompanyService companyService;
//
//    @Autowired
//    private DriverService driverService;
//
//
//    @Autowired
//    private RoleAuthenticationUtils roleAuthenticationUtils;
//
//    @ApiOperation(value = "车辆信息查询", notes = "")
//    @GetMapping("/vehicleinfo")
//    public MessageResp vehicleInfo(HttpServletRequest request,
//                                   @RequestParam(required = false) String companyId,
//                                   @RequestParam(required = false) String vehicleNo) {
//
//        MessageResp messageResp = new MessageResp<>();
//        try{
//            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
//            NetcarBaseInfoVehicle netcarBaseInfoVehicle = vehicleService.selectVehicleInfo(companyId,vehicleNo);
//            messageResp.setData(netcarBaseInfoVehicle);
//            messageResp.setResult(Boolean.TRUE.toString());
//            messageResp.setResultDesc("查询成功");
//        }catch (Exception e){
//            log.error(e.getMessage());
//            messageResp.setStatusCode("-50");
//            messageResp.setResult(Boolean.FALSE.toString());
//            messageResp.setResultDesc("查询异常");
//        }
//        return messageResp;
//    }
//
//    @ApiOperation(value = "企业信息查询", notes = "")
//    @GetMapping("/companyinfo")
//    public MessageResp companyInfo(HttpServletRequest request, @RequestParam String companyId, @RequestParam(required = false) Integer address){
//        MessageResp messageResp = new MessageResp<>();
//        try{
//            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
//            NetcarBaseInfoCompany netcarBaseInfoCompany = companyService.selectByCompanyId(companyId);
//            messageResp.setData(netcarBaseInfoCompany);
//            messageResp.setResult(Boolean.TRUE.toString());
//            messageResp.setResultDesc("查询成功");
//        }catch (Exception e){
//            log.error(e.getMessage());
//            messageResp.setStatusCode("-50");
//            messageResp.setResult(Boolean.FALSE.toString());
//            messageResp.setResultDesc("查询异常");
//        }
//        return messageResp;
//    }
//
//    @ApiOperation(value = "驾驶员信息查询", notes = "")
//    @GetMapping("/driverinfo")
//    public MessageResp driverInfo(HttpServletRequest request,
//                                  @RequestParam(required = false) String companyId,
//                                  @RequestParam(required = false)String licenseId) {
//        MessageResp messageResp = new MessageResp<>();
//        try{
//            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
//            NetcarBaseInfoDriver netcarBaseInfoDriver = driverService.selectDriverInfoByCompanyId(companyId, licenseId);
//            messageResp.setData(netcarBaseInfoDriver);
//            messageResp.setResult(Boolean.TRUE.toString());
//            messageResp.setResultDesc("查询成功");
//        }catch (Exception e){
//            log.error(e.getMessage());
//            messageResp.setStatusCode("-50");
//            messageResp.setResult(Boolean.FALSE.toString());
//            messageResp.setResultDesc("查询异常");
//        }
//        return messageResp;
//    }
//
//
//}
