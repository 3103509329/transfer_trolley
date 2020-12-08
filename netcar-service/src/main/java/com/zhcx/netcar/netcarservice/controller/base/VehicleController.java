package com.zhcx.netcar.netcarservice.controller.base;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zhcx.basic.dubbo.BaseInfoVehiclelicenceDubboService;
import com.zhcx.basic.facade.BaseInfoVehiclelicenceService;
import com.zhcx.basic.model.BaseInfoCompany;
import com.zhcx.basic.model.BaseInfoVehiclelicence;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import com.zhcx.netcar.netcarservice.utils.RoleAuthenticationUtils;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoVehicle;
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
import java.util.List;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 11:24
 */
@RestController
@RequestMapping("/netcar/baseinfo/vehicle")
@Api(value = "vehicle", tags = "车辆信息")
public class VehicleController {
    private Logger log = LoggerFactory.getLogger(VehicleController.class);

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoVehiclelicenceDubboService baseInfoVehiclelicenceService;
    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;


    @ApiOperation(value = "车辆基本信息获取", notes = "")
    @GetMapping
    public MessageResp select(HttpServletRequest request,
                              @RequestParam(required = false) String companyId,
                              @RequestParam(required = false) String vehicleNo,
                              @RequestParam(required = false) Integer source,
                              @RequestParam(defaultValue = "1") Integer pageNo,
                              @RequestParam(defaultValue = "30") Integer pageSize,
                              @RequestParam(required = false) String orderBy
    ) {

        MessageResp messageResp = new MessageResp();
        PageInfo<BaseInfoVehiclelicence> pageInfo = null;
        try {
            source = null;
            String corp = roleAuthenticationUtils.getCompanyId(request, null);
            if (corp != null) {
                pageInfo = baseInfoVehiclelicenceService.select(null,corp, vehicleNo, source, pageNo, pageSize, orderBy);
            } else {
                pageInfo = baseInfoVehiclelicenceService.select(null,companyId, vehicleNo, source, pageNo, pageSize, orderBy);

            }
            messageResp.setData(pageInfo.getList());
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
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
     * 根据公司ID查询车辆号牌
     *
     * @return
     */
    @ApiOperation(value = "根据公司ID查询车辆号牌", notes = "")
    @GetMapping("/vehicleNos")
    public MessageResp queryVehicleNoList(HttpServletRequest request,
                                          @RequestParam(required = false) String companyId) {
        MessageResp messageResp = new MessageResp();
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);

            List<String> vehicleNoList = baseInfoVehiclelicenceService.selectVehicleNoListByCompanyId(companyId);
            messageResp.setData(vehicleNoList);
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
     * 车辆信息查询
     *
     * @return
     */
    @ApiOperation(value = "车辆信息查询", notes = "")
    @GetMapping("/illegal")
    public MessageResp selectIllegalVehicleList(HttpServletRequest request,
                                                @RequestParam(required = false) String companyId,
                                                @RequestParam(required = false) String vehicleNo,
                                                @RequestParam(defaultValue = "1") Integer pageNo,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        PageInfo<BaseInfoVehiclelicence> pageInfo = null;
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);

            pageInfo = baseInfoVehiclelicenceService.selectIllegalVehicleList(companyId, vehicleNo, pageNo, pageSize, orderBy);
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
