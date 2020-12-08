package com.zhcx.netcar.netcarservice.controller.yz;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zhcx.basic.dubbo.BaseInfoVehiclelicenceDubboService;
import com.zhcx.basic.model.BaseInfoVehiclelicence;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * 车辆
 * 运政数据源
 *
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/3/7 0007 17:16
 **/
@RestController
@RequestMapping("/netcar/yz/vehicle")
public class YzVehicleController {

    private Logger log = LoggerFactory.getLogger(YzVehicleController.class);

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoVehiclelicenceDubboService baseInfoVehiclelicenceService;

    /**
     * 车辆查询(运政)
     *
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping
    public MessageResp selectVehicle(@RequestParam(required = false) String vehicleNo,
                                     @RequestParam(required = false) String busiRegNumber,
                                     @RequestParam(required = false) Long corpId,
                                     @RequestParam(defaultValue = "1") Integer pageNo,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false) String orderBy,
                                     @RequestParam(required = false) Integer dataType) {
        MessageResp messageResp = new MessageResp();
        PageInfo<BaseInfoVehiclelicence> pageInfo = null;
        try {
            Integer source = null;
            if (dataType != null && !dataType.equals("")) {
                source = dataType;
            }
            orderBy = "regist_date_desc";
            pageInfo = baseInfoVehiclelicenceService.select(corpId, busiRegNumber, vehicleNo, source, pageNo, pageSize, orderBy);
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

}
