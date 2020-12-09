package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.basicdata.facade.taxi.TaxiDispatchLocationService;
import com.zhcx.basicdata.facade.taxi.TaxiDispatchLocationService;
import com.zhcx.basicdata.facade.taxi.TaxiDispatchPlanService;
import com.zhcx.basicdata.pojo.taxi.TaxiDispatchLocation;
import com.zhcx.basicdata.pojo.taxi.TaxiDispatchPlan;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: authorization-service
 * @ClassName:TaxiDispatchLocationController
 * @description: 预设调度位置控制类
 * @author: ZhangKai
 * @create: 2018-12-19 16:31
 **/
@RestController
@RequestMapping("/taxi/dispatchLocation")
@Api(value = "dispatchLocation", tags = "出租车调度预案地址接口")
public class TaxiDispatchLocationController {


    private static final Logger logger = LoggerFactory.getLogger(TaxiDispatchLocationController.class);

    @Autowired
    private TaxiDispatchLocationService taxiDispatchLocationService;

    @Autowired
    private TaxiDispatchPlanService taxiDispatchPlanService;

    @GetMapping("/")
    @ApiOperation(value = "查询出租车辆调度地点信息", notes = "参数为出租车辆调度地点信息对象")
    public MessageResp getRecord(HttpServletRequest request, @ModelAttribute TaxiDispatchLocation param){
        MessageResp resp = new MessageResp();
        PageInfo pageInfo = null;
        try{
            pageInfo = taxiDispatchLocationService.selectByParam(param);
            if(pageInfo != null){
                resp.setData(pageInfo.getList());
                resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            }
            resp.setResultDesc("查询成功");
        }catch (Exception e){
            logger.error("查询异常,{}",e.getMessage());
            return CommonUtils.returnErrorInfo("查询异常");
        }
        return resp;
    }


    @DeleteMapping("/deleteLocation/{uuid}")
    @ApiOperation(value = "删除出租调度地点信息", notes = "参数UUID")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", dataType = "int", name = "uuid", value = "地点UUID", required = true)})
    public MessageResp deleteRecord(@PathVariable(value = "uuid") Integer uuid){
        MessageResp resp = new MessageResp();
        int delRes = 0;
        try{
            int bindPlan = taxiDispatchPlanService.getCountByDispatchId(uuid);
            if(bindPlan>0){
                return CommonUtils.returnErrorInfo("当前预设调度地点存在绑定的调度预案");
            }
            delRes = taxiDispatchLocationService.deleteRecord(uuid);
            resp.setData(delRes);
            resp.setResultDesc("删除成功");
        }catch (Exception e){
            logger.error("删除异常,{}",e.getMessage());
            return CommonUtils.returnErrorInfo("删除失败");
        }
        return resp;
    }


    @PostMapping("/updateLocation")
    @ApiOperation(value = "更新出租车辆调度地点信息", notes = "参数为出租车辆调度地点信息对象")
    public MessageResp updateRecord(HttpServletRequest request, @RequestBody TaxiDispatchLocation record){
        MessageResp resp = new MessageResp();
        int updateRes = 0;
        try {
            boolean checkRes = checkUniqueness(record.getAreaName(),record.getUuid());
            if(checkRes){
                return CommonUtils.returnErrorInfo("预设调度地点已存在");
            }
            updateRes = taxiDispatchLocationService.updateRecord(record);
            resp.setData(updateRes);
            resp.setResultDesc("更新成功");
        }catch (Exception e){
            logger.error("更新异常,{}",e.getMessage());
            return CommonUtils.returnErrorInfo("更新失败");
        }
        return resp;
    }

    @PostMapping("/addLocation")
    @ApiOperation(value = "添加出租车辆调度地点信息", notes = "参数为出租车辆调度地点信息对象")
    public MessageResp addRecord(HttpServletRequest request, @RequestBody TaxiDispatchLocation record){
        MessageResp resp = new MessageResp();
        int addRes = 0;
        try {
            boolean checkRes = checkUniqueness(record.getAreaName(),0);
            if(checkRes){
                return CommonUtils.returnErrorInfo("预设调度地点已存在");
            }
            addRes = taxiDispatchLocationService.addRecord(record);
            resp.setData(addRes);
            resp.setResultDesc("添加成功");
        }catch (Exception e){
            logger.error("添加异常,{}",e.getMessage());
            return CommonUtils.returnErrorInfo("新增调度地点失败");
        }
        return resp;
    }


    /**
     *
     * @param areaName 校验字段
     * @param uuid 0 校验新增  非0 校验更新
     * @return
     */
    protected boolean checkUniqueness(String areaName,int uuid){
        boolean flag = false;
        TaxiDispatchLocation param = new TaxiDispatchLocation();
        param.setAreaName(areaName);
        List<TaxiDispatchLocation> checkRes =  taxiDispatchLocationService.selectByParam(param).getList();
        if(checkRes.size() > 0){
            if(uuid != 0){
                for(TaxiDispatchLocation item : checkRes){
                    if(areaName.equals(item.getAreaName()) && uuid != item.getUuid()){
                        return true;
                    }
                }
            }else{
                return true;
            }
        }
        return flag;
    }

}
