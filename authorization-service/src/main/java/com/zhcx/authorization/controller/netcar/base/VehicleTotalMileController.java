package com.zhcx.authorization.controller.netcar.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.*;
import com.zhcx.netcarbasic.facade.netcar.VehicleTotalMileService;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoVehicleTotalMile;
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
 * @date 2018-11-24 11:31
 */
@RestController
@RequestMapping("/netcar/baseinfo/vehicletotalmile")
@Api(value = "vehicletotalmile", tags = "车辆里程信息")
public class VehicleTotalMileController {

    private Logger log = LoggerFactory.getLogger(VehicleTotalMileController.class);

    @Autowired
    private VehicleTotalMileService vehicleTotalMileService;

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;

    /**
     * 车辆里程信息获取
     *
     * @return
     */
    @ApiOperation(value = "车辆里程信息查询", notes = "")
    @GetMapping
    public MessageResp selectVehicleTotalMileList(HttpServletRequest request,
                                                  @RequestParam(required = false) String companyId,
                                                  @RequestParam(required = false) String vehicleNo,
                                                  @RequestParam(defaultValue = "1") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoVehicleTotalMile> pageInfo = null;
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);

            pageInfo = vehicleTotalMileService.selectVehicleTotalMileList(companyId, vehicleNo, pageNo, pageSize, orderBy);
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
     * 根据车牌号查询车辆里程信息
     *
     * @return
     */
    @ApiOperation(value = "根据车牌号查询车辆里程信息", notes = "")
    @GetMapping("/vehicleNo")
    public MessageResp selectVehicleTotalMileByVehicleNo(HttpServletRequest request,
                                                         @RequestParam String companyId,
                                                         @RequestParam String vehicleNo,
                                                         @RequestParam(defaultValue = "1") Integer pageNo,
                                                         @RequestParam(defaultValue = "30") Integer pageSize,
                                                         @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoVehicleTotalMile> pageInfo = null;
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);

            pageInfo = vehicleTotalMileService.selectVehicleTotalMileByVehicleNo(companyId, vehicleNo, pageNo, pageSize, orderBy);
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
