package com.zhcx.authorization.controller.taxi.net;/**
 * @Classname TaxiNetOderController
 * @Description TODO
 * @Date 2020/11/12 16:25
 * @Created by 李亮
 */

import com.caucho.services.message.MessageSender;
import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.controller.netcar.base.CompanyPayController;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.basicdata.facade.taxi.TaxiNetOderService;
import com.zhcx.basicdata.pojo.taxi.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName：TaxiNetOderController
 * @Description:
 * @author：李亮
 * @date：2020/11/1216:25
 */
@RestController
@RequestMapping("/taxi/net")
@Api(value = "net", tags = "出租车电召业务")
public class TaxiNetOderController {

    private Logger log = LoggerFactory.getLogger(CompanyPayController.class);

    @Autowired
    private TaxiNetOderService taxiNetOderService;

    /*******************************************************定位数据入库（Tile38）**********************************************************/
    @PostMapping("/app/location")
    @ApiOperation(value = "车辆定位数据")
    public MessageResp addVehicleLocation(@RequestBody VehicleLocation vehicleLocation) {
        MessageResp messageResp = new MessageResp();
        try {
            int result = taxiNetOderService.addVehicleLocation(vehicleLocation);
            if (result == 0) {
                messageResp.setResult(Boolean.FALSE.toString());
                messageResp.setResultDesc("新增失败");
            } else {
                messageResp.setResult(Boolean.TRUE.toString());
                messageResp.setResultDesc("新增");
            }
            messageResp.setData(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("新增失败");
        }
        return messageResp;
    }

    /*******************************************************乘客操作**********************************************************/

    @GetMapping("/passenger/underway")
    @ApiOperation(value = "乘客进行中的订单")
    public MessageResp getpassengerOrderUnderway(@RequestParam String userId) {
        MessageResp messageResp = new MessageResp();
        try {
            List<TaxiNetWorkOder> result = taxiNetOderService.getpassengerOrderUnderway(userId);
            messageResp.setData(result);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("查询失败");
        }
        return messageResp;
    }


    /**
     * app端指定乘客订单统计
     *
     * @param
     * @return
     */
    @GetMapping("/passenger/total")
    @ApiOperation(value = "指定乘客订单统计", notes = "")
    public MessageResp getPassengerTotal(@RequestParam String userId,
                                         @RequestParam(required = false) String vehiclNo,
                                         @RequestParam(defaultValue = "1") Integer type,
                                         @RequestParam(defaultValue = "1") Integer pageNo,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        try {
            UserTotal result = taxiNetOderService.getPassengerTotal(userId, vehiclNo, type);
            messageResp.setData(result);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("查询失败");
        }
        return messageResp;
    }

    /**
     * app端乘客订单列表
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping("/passenger/ord")
    @ApiOperation(value = "指定乘客订单列表", notes = "")
    public MessageResp getPassengerOrder(@RequestParam String userId,
                                         @RequestParam(required = false) Integer state,
                                         @RequestParam(defaultValue = "1") Integer pageNo,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        try {
            PageInfo<TaxiNetWorkOder> result = taxiNetOderService.getPassengerOrder(userId, state, pageNo, pageSize, orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(result));
            messageResp.setData(result.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("查询失败");
        }
        return messageResp;

    }

    /**
     * 乘客指定范围内车辆数
     *
     * @param request
     * @param longitude
     * @param latitude
     * @return
     */
    @GetMapping("/passenger/sel")
    @ApiOperation(value = "乘客指定范围内车辆数", notes = "")
    public MessageResp getVehicle(HttpServletRequest request,
                                  @RequestParam String longitude,
                                  @RequestParam String latitude) {
        MessageResp messageResp = new MessageResp();
        try {
            List<Vehicle> orderList = taxiNetOderService.getVehicle(longitude, latitude);
            messageResp.setData(orderList);
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
     * 订单发起
     * 当存在订单未结算的情况下，不允许发起新的订单
     *
     * @param taxiNetOder
     * @return
     */
    @PostMapping("/passenger/order")
    @ApiOperation(value = "乘客订单发起", notes = "")
    public MessageResp addOrder(@RequestBody TaxiNetWorkOder taxiNetOder) {
        MessageResp messageResp = new MessageResp();
        try {
            TaxiNetWorkOder result = taxiNetOderService.addOrder(taxiNetOder);
            if (result == null) {
                messageResp.setResultDesc("订单未结算");
            } else {

                messageResp.setResultDesc("订单创建成功");
                messageResp.setData(result);
            }
            messageResp.setData(result);
            messageResp.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("订单创建失败");
        }
        return messageResp;
    }

    /**
     * 查询当前订单的状态
     * 订单发起成功后，轮询获取指定订单是否被接单，接单的车辆信息
     *
     * @param orderId
     * @return
     */
    @GetMapping("/passenger/order")
    @ApiOperation(value = "乘客订单状态查询", notes = "")
    public MessageResp selectOrderState(@RequestParam String orderId) {
        MessageResp messageResp = new MessageResp();
        try {
            TaxiNetWorkOder result = taxiNetOderService.selectOrderState(orderId);
            messageResp.setData(result);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("查询失败");
        }
        return messageResp;
    }


    /**
     * 1.乘客订单操作
     *
     * @param taxiNetOder
     * @return
     */
    @PutMapping("/passenger/order/put")
    @ApiOperation(value = "乘客操作订单", notes = "")
    public MessageResp updateOrder(@RequestBody TaxiNetWorkOder taxiNetOder) {
        MessageResp messageResp = new MessageResp();
        try {
            int result = taxiNetOderService.updateOrder(taxiNetOder);
            if (result == -1) {
                messageResp.setData(result);
                messageResp.setResultDesc("当日取消次数已满");
                messageResp.setResult(Boolean.TRUE.toString());
            } else {
                messageResp.setData(result);
                messageResp.setResult(Boolean.TRUE.toString());
                messageResp.setResultDesc("操作成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("操作失败");
        }
        return messageResp;
    }

    /**
     * 查询指定订单指派了哪些车辆
     * 由于司机端是查询的方式，所以目前保存的是可以订单范围内的所有车辆的数据
     *
     * @param orderId
     * @return
     */
    @GetMapping("/passenger/order/push")
    @ApiOperation(value = "待接单车辆数据", notes = "")
    public MessageResp getOrderPushByOrderId(@RequestParam String orderId) {
        MessageResp messageResp = new MessageResp();
        try {
            List<TaxiOrderPush> result = taxiNetOderService.getOrderPushByOrderId(orderId);
            messageResp.setData(result);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("查询失败");
        }
        return messageResp;
    }

    /*******************************************************司机操作**********************************************************/

    @GetMapping("/driver/underway")
    @ApiOperation(value = "司机进行中的订单")
    public MessageResp getDriverOrderUnderway(@RequestParam String vehcileNo) {
        MessageResp messageResp = new MessageResp();
        try {
            List<TaxiNetWorkOder> result = taxiNetOderService.getDriverOrderUnderway(vehcileNo);
            messageResp.setData(result);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("查询失败");
        }
        return messageResp;
    }


    /**
     * 司机范围订单查询接口
     *
     * @param request
     * @param longitude 经度
     * @param latitude  维度
     * @return
     */
    @GetMapping("/driver/sel")
    @ApiOperation(value = "司机范围订单查询接口", notes = "")
    public MessageResp getOder(HttpServletRequest request,
                               @RequestParam String longitude,
                               @RequestParam String latitude,
                               @RequestParam String vehicleNo) {

        MessageResp messageResp = new MessageResp();
        try {
            List<TaxiNetWorkOder> orderList = taxiNetOderService.getOrder(longitude, latitude, vehicleNo);
            if (orderList == null){
                orderList = new ArrayList<>();
            }
            messageResp.setData(orderList);
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
     * 司机订单操作
     *
     * @param taxiNetOder
     * @return
     */
    @PutMapping("/driver/order/put")
    @ApiOperation(value = "司机订单操作", notes = "")
    public MessageResp updataOrderVehicle(@RequestBody TaxiNetWorkOder taxiNetOder) {
        MessageResp messageResp = new MessageResp();
        try {
            TaxiNetWorkOder result = taxiNetOderService.updataOrderVehicle(taxiNetOder);
            if (result == null) {
                messageResp.setResultDesc("订单已被接单");
            } else {
                messageResp.setResultDesc("操作成功");
            }
            messageResp.setData(result);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("操作成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("操作失败");
        }
        return messageResp;
    }


    /**
     * 司机订单列表查询
     *
     * @param vehicleNo
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping("/driver/ord")
    @ApiOperation(value = "司机订单列表查询", notes = "")
    public MessageResp getDriverOrder(@RequestParam String vehicleNo,
                                      @RequestParam(defaultValue = "1") Integer pageNo,
                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                      @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        try {
            List<TaxiNetWorkOder> result = taxiNetOderService.getDriverOrder(vehicleNo, pageNo, pageSize, orderBy);
            messageResp.setData(result);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("查询失败");
        }
        return messageResp;
    }

    @GetMapping("/driver/evaluation/total")
    @ApiOperation(value = "司机统计")
    public MessageResp getDriverRvaluationTotal(@RequestParam String vehicleNo) {
        MessageResp messageResp = new MessageResp();
        try {
            List<DriverRvaluation> result = taxiNetOderService.getDriverRvaluationTotal(vehicleNo);
            messageResp.setData(result);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("查询失败");
        }
        return messageResp;
    }

    /******************web端*********************************/
    @GetMapping("/web/passenger/list")
    @ApiOperation(value = "web端，乘客列表查询", notes = "")
    public MessageResp getPassengerList(@RequestParam String key,
                                        @RequestParam String sTime, @RequestParam String eTime,
                                        @RequestParam(defaultValue = "1") Integer pageNo,
                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                        @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        try {
            PageInfo<PassengerTotal> result = taxiNetOderService.getPassengerList(key, sTime, eTime, pageNo, pageSize, orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(result));
            messageResp.setData(result.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("查询失败");
        }
        return messageResp;
    }


    @GetMapping("/web/site")
    @ApiOperation(value = "常用约车地点")
    public MessageResp getSite(@RequestParam(required = false) String key,
                               @RequestParam(defaultValue = "1") Integer pageNo,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        try {
            PageInfo<TaxiNetWorkOder> result = taxiNetOderService.getSite(key, pageNo, pageSize, orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(result));
            messageResp.setData(result.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("查询失败");
        }
        return messageResp;
    }

    @GetMapping("/web/order/list")
    @ApiOperation(value = "web端，订单列表查询", notes = "")
    public MessageResp getOrderList(@RequestParam(required = false) String key,
                                    @RequestParam(required = false) Integer state,
                                    @RequestParam String sTime, @RequestParam String eTime,
                                    @RequestParam(defaultValue = "1") Integer pageNo,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        try {
            PageInfo<TaxiNetWorkOder> result = taxiNetOderService.getOrderList(key, state, sTime, eTime, pageNo, pageSize, orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(result));
            messageResp.setData(result.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("查询失败");
        }
        return messageResp;
    }

    @PostMapping("/web/order/add")
    @ApiOperation(value = "新增电召行程")
    public MessageResp webAddOrder(@RequestBody TaxiNetWorkOder taxiNetWorkOder) {
        MessageResp messageResp = new MessageResp();
        try {
            int result = taxiNetOderService.webAddOrder(taxiNetWorkOder);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("新增成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("新增失败");
        }
        return messageResp;
    }


    /**
     * 订单统计页面查询（统计结果查询）
     *
     * @param key
     * @param state
     * @param sTime
     * @param eTime
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping("/web/ord/total")
    @ApiOperation(value = "web端，订单统计查询", notes = "")
    public MessageResp getOrderTotal(@RequestParam(required = false) String key, @RequestParam(required = false) int state,
                                     @RequestParam String sTime, @RequestParam String eTime,
                                     @RequestParam(defaultValue = "1") Integer pageNo,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        try {
            List<OrderTotal> result = taxiNetOderService.getOrderTotal(key, state, sTime, eTime, pageNo, pageSize, orderBy);
            messageResp.setData(result);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("查询失败");
        }
        return messageResp;
    }

    /**
     * web端乘客评价统计（饼状图）
     *
     * @param key
     * @param sTime
     * @param eTime
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping("/web/evaluate/total/pie")
    @ApiOperation(value = "web端，乘客评价饼图")
    public MessageResp getEvaluateTotal(@RequestParam(required = false) String key,
                                        @RequestParam String sTime,
                                        @RequestParam String eTime,
                                        @RequestParam(defaultValue = "1") Integer pageNo,
                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                        @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        try {
            List<PieTotal> result = taxiNetOderService.getEvaluateTotal(key, sTime, eTime, pageNo, pageSize, orderBy);
            messageResp.setData(result);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("查询失败");
        }
        return messageResp;

    }

    @GetMapping("/web/evaluate/total/wire")
    @ApiOperation(value = "web端，乘客评价折")
    public MessageResp getEvalueateWireTotal(@RequestParam(required = false) String key,
                                             @RequestParam String sTime,
                                             @RequestParam String eTime,
                                             @RequestParam(defaultValue = "1") Integer pageNo,
                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                             @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        try {
            List<OrderTotal> result = taxiNetOderService.getEvalueateWireTotal(key, sTime, eTime, pageNo, pageSize, orderBy);
            messageResp.setData(result);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("查询失败");
        }
        return messageResp;
    }

    @GetMapping("/web/order/push")
    @ApiOperation(value = "订单推送车辆")
    public MessageResp getOrderPush(@RequestParam String orderId,
                                    @RequestParam(defaultValue = "1") Integer pageNo,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        try {
            PageInfo<TaxiOrderPush> result = taxiNetOderService.getOrderPush(orderId, pageNo, pageSize, orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(result));
            messageResp.setData(result.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setResultDesc("查询失败");
        }
        return messageResp;
    }
}
