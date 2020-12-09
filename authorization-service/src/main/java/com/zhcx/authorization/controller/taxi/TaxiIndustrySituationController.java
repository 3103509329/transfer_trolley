package com.zhcx.authorization.controller.taxi;

import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.basicdata.facade.taxi.TaxiIndustrySituationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @program: authorization-service
 * @ClassName:IndustrySituationController
 * @description: 出租-行业情况控制类
 * @author: ZhangKai
 * @create: 2018-12-02 16:42
 **/
@RestController
@RequestMapping("/taxi/industry")
@Api(value = "industry", tags = "出租车行业情况查询")
public class TaxiIndustrySituationController {

    private static final Logger logger = LoggerFactory.getLogger(TaxiIndustrySituationController.class);

    @Autowired
    private TaxiIndustrySituationService taxiIndustrySituationService;


    @GetMapping("/corpTotalTrend/{year}")
    @ApiOperation(value = "企业总数趋势", notes = "企业总数趋势")
    public MessageResp corpTotalTrend(@PathVariable("year") String year){
        MessageResp resp = new MessageResp();
        try{
            List<Object> data = taxiIndustrySituationService.corpTotal(year);
            resp.setData(data);
            resp.setResultDesc("查询成功");
            resp.setResult(Boolean.TRUE.toString());
        }catch (Exception e){
            logger.error("查询失败,{}",e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return resp;
    }

    @GetMapping("/corpVehicleTotalTrend/{year}")
    @ApiOperation(value = "企业车辆总数趋势", notes = "企业车辆总数趋势")
    public MessageResp corpVehicleTotalTrend(@PathVariable("year") String year){
        MessageResp resp = new MessageResp();
        try{
            List<Map<String, Object>> data = taxiIndustrySituationService.vehicleTotal(year);
            resp.setData(data);
            resp.setResultDesc("查询成功");
            resp.setResult(Boolean.TRUE.toString());
        }catch (Exception e){
            logger.error("查询失败,{}",e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return resp;
    }


    @GetMapping("/corpEmployeeTotalTrend/{year}")
    @ApiOperation(value = "企业人员规模总数趋势", notes = "企业人员规模总数趋势")
    public MessageResp corpEmployeeTotalTrend(@PathVariable("year") String year){
        MessageResp resp = new MessageResp();
        try{
            List<Map<String, Object>> data = taxiIndustrySituationService.employeeTotal(year);
            resp.setData(data);
            resp.setResultDesc("查询成功");
            resp.setResult(Boolean.TRUE.toString());
        }catch (Exception e){
            logger.error("查询失败,{}",e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return resp;
    }


    @GetMapping("/vehicleTotalTrendForArea/{year}")
    @ApiOperation(value = "车辆地区总数趋势", notes = "车辆地区总数趋势")
    public MessageResp vehicleTotalTrendForArae(@PathVariable("year") String year){
        MessageResp resp = new MessageResp();
        try{
            List<Map<String, Object>> data = taxiIndustrySituationService.vehicleByArea(year);
            resp.setData(data);
            resp.setResultDesc("查询成功");
            resp.setResult(Boolean.TRUE.toString());
        }catch (Exception e){
            logger.error("查询失败,{}",e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }

        return resp;
    }

    @GetMapping("/vehicleFeulTrend/{year}")
    @ApiOperation(value = "车辆燃油类型趋势", notes = "车辆燃油类型趋势")
    public MessageResp vehicleFeulTrend(@PathVariable("year") String year){
        MessageResp resp = new MessageResp();
        try{
            List<Map<String, Object>> data = taxiIndustrySituationService.vehicleByfuelType(year);
            resp.setData(data);
            resp.setResultDesc("查询成功");
            resp.setResult(Boolean.TRUE.toString());
        }catch (Exception e){
            logger.error("查询失败,{}",e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return resp;
    }


    @GetMapping("/driverAreaTotalTrend/{year}")
    @ApiOperation(value = "驾驶员地区总数趋势", notes = "驾驶员地区总数趋势")
    public MessageResp driverAreaTotalTrend(@PathVariable("year") String year){
        MessageResp resp = new MessageResp();
        try{
            List<Map<String, Object>> data = taxiIndustrySituationService.driverByArea(year);
            resp.setData(data);
            resp.setResultDesc("查询成功");
            resp.setResult(Boolean.TRUE.toString());
        }catch (Exception e){
            logger.error("查询失败,{}",e.getMessage());
            return CommonUtils.returnErrorInfo("查询失败");
        }

        return resp;
    }

    @GetMapping("/driverAgeSegmentTrend/{year}")
    @ApiOperation(value = "驾驶员年龄趋势", notes = "驾驶员年龄趋势")
    public MessageResp driverAgeSegmentTrend(@PathVariable("year") String year){
        MessageResp resp = new MessageResp();
        try{
            List<Map<String, Object>> data = taxiIndustrySituationService.driverByAge(year);
            resp.setData(data);
            resp.setResultDesc("查询成功");
            resp.setResult(Boolean.TRUE.toString());
        }catch (Exception e){
            logger.error("查询失败,{}",e.getMessage());
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return resp;
    }

    @GetMapping("/driveingAgeSegmentTrend/{year}")
    @ApiOperation(value = "驾驶员驾龄趋势", notes = "驾驶员驾龄趋势")
    public MessageResp driveingAgeSegmentTrend(@PathVariable("year") String year){
        MessageResp resp = new MessageResp();
        try{
            List<Map<String, Object>> data = taxiIndustrySituationService.driverDrivingAge(year);
            resp.setData(data);
            resp.setResultDesc("查询成功");
            resp.setResult(Boolean.TRUE.toString());
        }catch (Exception e){
            logger.error("查询失败,{}",e.getMessage());
            return CommonUtils.returnErrorInfo("查询失败");
        }

        return resp;
    }


}
