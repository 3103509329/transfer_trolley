package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
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
 * @ClassName:TaxiDispatchPlanController
 * @description: 调度预案控制类
 * @author: ZhangKai
 * @create: 2018-12-19 15:19
 **/
@RestController
@RequestMapping("/taxi/dispatchPlan")
@Api(value = "dispatchPlan", tags = "出租车调度预案接口")
public class TaxiDispatchPlanController {

    private static final Logger logger = LoggerFactory.getLogger(TaxiDispatchPlanController.class);

    @Autowired
    private TaxiDispatchPlanService taxiDispatchPlanService;

    @GetMapping("/")
    @ApiOperation(value = "查询出租车辆调度预案信息", notes = "参数为出租车辆调度预案信息对象")
    public MessageResp getRecord(HttpServletRequest request, @ModelAttribute TaxiDispatchPlan param){
        MessageResp resp = new MessageResp();
        PageInfo pageInfo = null;
        try{
            pageInfo = taxiDispatchPlanService.selectByParam(param);
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


    @DeleteMapping("/delete/{uuid}")
    @ApiOperation(value = "删除出租调度预案信息", notes = "参数UUID")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", dataType = "int", name = "uuid", value = "预案UUID", required = true)})
    public MessageResp deleteRecord(@PathVariable(value = "uuid") Integer uuid){
        MessageResp resp = new MessageResp();
        int delRes = 0;
        try{
            delRes = taxiDispatchPlanService.deletePlan(uuid);
            resp.setData(delRes);
            resp.setResultDesc("删除成功");
        }catch (Exception e){
            logger.error("删除异常,{}",e.getMessage());
            return CommonUtils.returnErrorInfo("删除失败");
        }
        return resp;
    }


    @PostMapping("/update")
    @ApiOperation(value = "更新出租车辆调度预案信息", notes = "参数为出租车辆调度预案信息对象")
    public MessageResp updateRecord(HttpServletRequest request, @RequestBody TaxiDispatchPlan record){
        MessageResp resp = new MessageResp();
        int updateRes = 0;
        try {

            boolean checkRes = checkUniqueness(record.getPlanName(),record.getUuid());
            if(checkRes){
                return CommonUtils.returnErrorInfo("调度预案已存在");
            }
            updateRes = taxiDispatchPlanService.updatePlant(record);
            resp.setData(updateRes);
            resp.setResultDesc("更新成功");
        }catch (Exception e){
            logger.error("更新异常,{}",e.getMessage());
            return CommonUtils.returnErrorInfo("更新失败");
        }
        return resp;
    }

    @PostMapping("/addPlan")
    @ApiOperation(value = "添加出租车辆调度预案信息", notes = "参数为出租车辆调度预案信息对象")
    public MessageResp addRecord(HttpServletRequest request, @RequestBody TaxiDispatchPlan record){
        MessageResp resp = new MessageResp();
        int addRes = 0;
        try {
            boolean checkRes = checkUniqueness(record.getPlanName(),0);
            if(checkRes){
                return CommonUtils.returnErrorInfo("调度预案已存在");
            }
            addRes = taxiDispatchPlanService.addPlan(record);
            resp.setData(addRes);
            resp.setResultDesc("添加成功");
        }catch (Exception e){
            logger.error("添加异常,{}",e.getMessage());
            return CommonUtils.returnErrorInfo("新增调度预案失败");
        }
        return resp;
    }



    /**
     *
     * @param planName 校验字段
     * @param uuid 0 校验新增  非0 校验更新
     * @return
     */
    protected boolean checkUniqueness(String planName,int uuid){
        boolean flag = false;
        TaxiDispatchPlan param = new TaxiDispatchPlan();
        param.setPlanName(planName);
        List<TaxiDispatchPlan> checkRes =  taxiDispatchPlanService.selectByParam(param).getList();
        if(checkRes.size() > 0){
            if(uuid != 0){
                for(TaxiDispatchPlan item : checkRes){
                    if(planName.equals(item.getPlanName()) && uuid != item.getUuid()){
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
