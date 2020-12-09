package com.zhcx.netcar.netcarservice.controller.app;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.yunzheng.YunZhengVehicleService;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import com.zhcx.netcar.pojo.yuzheng.YunZhengVehicle;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * 车辆
 * 运政数据源
 *
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/3/7 0007 17:16
 **/
@RestController
@RequestMapping("/netcar/app/vehicle")
@Api(value = "vehicl", tags = "车辆基本信息（App）")
public class AppYzVehicleController {

    private Logger log = LoggerFactory.getLogger(AppYzVehicleController.class);

    @Autowired
    private YunZhengVehicleService yunZhengVehicleService;


    /**
     * 车辆查询(运政)
     *
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping
    @ApiOperation(value = "车辆基本信息查询（App）", notes = "")
    public MessageResp selectVehicle(@RequestParam(value = "vehicleNo", defaultValue = "", required = false) String vehicleNo,
                                     @RequestParam(required = false) String busiRegNumber,
                                     @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                     @RequestParam(value = "orderBy", required = false, defaultValue = "") String orderBy) {

        MessageResp messageResp = new MessageResp();
        try {
            PageInfo<YunZhengVehicle> pageInfo = yunZhengVehicleService.selectListByVehicleNo(vehicleNo, busiRegNumber, pageNo, pageSize, orderBy);
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
