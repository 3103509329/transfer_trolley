package com.zhcx.authorization.controller.netcar.yunzheng;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.platformtonet.facade.YunZhengVehicleService;
import com.zhcx.platformtonet.pojo.base.YunZhengVehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
@RequestMapping("/netcar/yz/vehicle")
public class YzVehicleController {

    private Logger log = LoggerFactory.getLogger(YzVehicleController.class);

    @Autowired
    private YunZhengVehicleService yunZhengVehicleService;

    /**
     * 车辆新增
     * @param yunZhengVehicle
     * @return
     */
    @PostMapping
    public MessageResp addVehicle(@RequestBody YunZhengVehicle yunZhengVehicle){
        MessageResp messageResp = new MessageResp();
        try{
            int i = yunZhengVehicleService.selectByVehicleNo(yunZhengVehicle.getBranum());
            if (i > 0) {
                return CommonUtils.returnErrorInfo("车辆已经存在");
            }
            YunZhengVehicle pageInfo = yunZhengVehicleService.addVehicle(yunZhengVehicle);
            messageResp.setData(pageInfo);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("新增成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("新增失败");
        }
        return messageResp;
    }

    /**
     * 车辆查询(运政)
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping
    public MessageResp selectVehicle(@RequestParam (required = false) String vehicleNo,
                                     @RequestParam(required = false) String busiRegNumber,
                                     @RequestParam(defaultValue = "1") Integer pageNo,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false) String orderBy){
        MessageResp messageResp = new MessageResp();
        try{
            PageInfo<YunZhengVehicle> pageInfo = yunZhengVehicleService.selectListByVehicleNo(vehicleNo, busiRegNumber, pageNo, pageSize, orderBy);
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
     * 车辆修改（运政）
     * @param yunZhengVehicle
     * @return
     */
    @PutMapping
    public MessageResp updateVehicle(@Valid @RequestBody YunZhengVehicle yunZhengVehicle){
        MessageResp messageResp = new MessageResp();
        try{
            YunZhengVehicle pageInfo = yunZhengVehicleService.updateVehicle(yunZhengVehicle);
            messageResp.setData(pageInfo);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("修改成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("修改失败");
        }
        return messageResp;
    }

    @DeleteMapping
    public MessageResp deleteVehicle(@RequestBody YunZhengVehicle yunZhengVehicle){
        MessageResp messageResp = new MessageResp();
        try{
            int pageInfo = yunZhengVehicleService.deleteVehicle(yunZhengVehicle.getBranum(), yunZhengVehicle.getTime());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("删除成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("删除失败");
        }
        return messageResp;
    }
}
