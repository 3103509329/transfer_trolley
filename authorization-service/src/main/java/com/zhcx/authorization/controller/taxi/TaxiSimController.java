package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoSimService;
import com.zhcx.basicdata.facade.taxi.TaxiVehicleSimService;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoSim;
import com.zhcx.basicdata.pojo.taxi.TaxiVehicleSim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: authorization-service
 * @ClassName:TaxiSimController
 * @description: SIM卡管理控制类
 * @author: ZhangKai
 * @create: 2019-04-10 09:56
 **/

@RestController
@RequestMapping("/taxi/sim")
public class TaxiSimController {
    private static final Logger logger = LoggerFactory.getLogger(TaxiSimController.class);


    @Autowired
    private TaxiBaseInfoSimService taxiBaseInfoSimService;

    @Autowired
    private TaxiVehicleSimService taxiVehicleSimService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;


    @GetMapping("/querySimList")
    public MessageResp querySimList(HttpServletRequest request,@ModelAttribute TaxiBaseInfoSim param){
        MessageResp result = new MessageResp();
        PageInfo pageInfo = null;
        AuthUserResp user = sessionHandler.getUser(request);
        try{
            //权限校验 非管理员只能查看本用户下的sim卡
            if (!Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())) {
                if (user.getCorpId() != null && user.getCorpId() != 0L) {
                    param.setCorpId(String.valueOf(user.getCorpId()));
                }
            }
            pageInfo = taxiBaseInfoSimService.selectByParam(param);
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            result.setData(pageInfo.getList());
            result.setStatusCode("00");
            result.setResult(Boolean.TRUE.toString());
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return result;
    }

    @GetMapping("/querySimForBind")
    public MessageResp<List<TaxiBaseInfoSim>> querySimForBind(HttpServletRequest request, @ModelAttribute TaxiBaseInfoSim param){

        MessageResp result = new MessageResp();
        PageInfo<TaxiBaseInfoSim> pageInfo = null;
        AuthUserResp user = sessionHandler.getUser(request);
        try {
            //非管理员 只能查看
            if (null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())) {
                if (user.getCorpId() != null && user.getCorpId() != 0L) {
                    param.setCorpId(String.valueOf(user.getCorpId()));
                }
            }
            pageInfo = taxiBaseInfoSimService.selectSimForBind(param);
            result.setResultDesc("查询成功");
            result.setResult(Boolean.TRUE.toString());
            result.setData(pageInfo.getList());
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败,{}", e.getMessage());
            return CommonUtils.returnErrorInfo(null);
        }
        return result;
    }


    @PostMapping("/addSim")
    public MessageResp addSim(HttpServletRequest request,@RequestBody TaxiBaseInfoSim record){
        MessageResp resp = new MessageResp();
        AuthUserResp authUser = sessionHandler.getUser(request);
        if(authUser.getUserType().equals("01") && (null == record.getCorpId() || "".equals(record.getCorpId()))){
            return CommonUtils.returnErrorInfo("请选择所属企业");
        }
        if (!authUser.getUserType().equals("01")){
            record.setCorpId(String.valueOf(authUser.getCorpId()));
        }

        record.setCreateBy(authUser.getUserName());

        // 根据SIM卡号查询记录是否已经存在
        TaxiBaseInfoSim oldRecord = taxiBaseInfoSimService.selectBySimCode(record.getSimCode(),null,true);
        if(null != oldRecord){
            return CommonUtils.returnErrorInfo("SIM卡号已存在");
        }

        int addResp = taxiBaseInfoSimService.insertSimRecord(record);
        if(addResp <= 0){
            return CommonUtils.returnErrorInfo("添加失败");
        }
        resp.setResultDesc("新增成功");
        resp.setResult(Boolean.TRUE.toString());
        return resp;
    }


    @PutMapping("/updateSim")
    public MessageResp updateSim(HttpServletRequest request,@RequestBody TaxiBaseInfoSim record){
        MessageResp resp = new MessageResp();

        TaxiBaseInfoSim oldRecord = taxiBaseInfoSimService.selectBySimCode(null,record.getUuid(),true);
        // 数据唯一性校验
        if(null != oldRecord){
            if(!oldRecord.getSimCode().equals(record.getSimCode())){
                TaxiBaseInfoSim otherRecord = taxiBaseInfoSimService.selectBySimCode(record.getSimCode(),record.getUuid(),false);
                if(null != otherRecord){
                    return CommonUtils.returnErrorInfo("SIM卡号已存在");
                }
            }
        }

        TaxiVehicleSim vehicleSim = new TaxiVehicleSim();
        vehicleSim.setStatus("01");

        if (oldRecord !=null){
            vehicleSim.setSimCode(Integer.toString(oldRecord.getUuid()));
            int exsit = taxiVehicleSimService.isExsit(vehicleSim);
            //更新企业前需要先解绑sim卡与车辆
            if (!record.getCorpId().equals(oldRecord.getCorpId())){
                if (exsit > 0){
                    return CommonUtils.returnErrorInfo("更新失败,请先解绑车辆");
                }
            }
            //修改卡号前需先解绑车辆
            if (!record.getSimCode().equals(oldRecord.getSimCode())){
                if (exsit > 0){
                    return CommonUtils.returnErrorInfo("更新失败,请先解绑车辆");
                }
            }
        }
        AuthUserResp authUser = sessionHandler.getUser(request);
        record.setUpdateBy(authUser.getUserName());

        int updateResp = taxiBaseInfoSimService.updateSimRecord(record);
        if(updateResp <= 0){
            return CommonUtils.returnErrorInfo("更新失败");
        }
        resp.setResultDesc("修改成功");
        resp.setResult(Boolean.TRUE.toString());
        return resp;
    }



    @DeleteMapping("/deleteSim")
    public MessageResp deleteSim(Integer uuid){
        MessageResp resp = new MessageResp();
        //删除前先查询是否绑定车辆
//        TaxiBaseInfoSim oldSim = taxiBaseInfoSimService.selectByUuid(uuid);
        TaxiBaseInfoSim oldSim = taxiBaseInfoSimService.selectBySimCode(null,uuid,true);
        if (oldSim !=null){
            TaxiVehicleSim vehicleSim = new TaxiVehicleSim();
            vehicleSim.setStatus("01");
            vehicleSim.setSimCode(Integer.toString(oldSim.getUuid()));
            int exsit = taxiVehicleSimService.isExsit(vehicleSim);

            if (exsit > 0){
                return CommonUtils.returnErrorInfo("删除失败,请先解绑车辆");
            }
        }


        int deleteResp = taxiBaseInfoSimService.deleteSimRecord(uuid);
        if(deleteResp <= 0){
            return CommonUtils.returnErrorInfo("删除SIM卡信息失败");
        }
        resp.setResultDesc("删除SIM卡信息成功");
        resp.setResult(Boolean.TRUE.toString());
        return resp;
    }


    @PostMapping("/bindVehicle")
    public MessageResp bindVehicle(@RequestBody TaxiVehicleSim bindRecord){
        MessageResp resp = new MessageResp();

        //数据校验
        bindRecord.setStatus("01");
        int exsit = taxiVehicleSimService.isExsit(bindRecord);
        if(exsit > 0){
            return CommonUtils.returnErrorInfo("当前SIM卡号与车辆已存在绑定关系");
        }
        int bindResp = 0;
        try{
            bindResp =  taxiVehicleSimService.addBind(bindRecord);
            if(bindResp <= 0){
                return CommonUtils.returnErrorInfo("绑定失败");
            }
            resp.setResultDesc("绑定成功");
            resp.setResult(Boolean.TRUE.toString());
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("绑定失败");
        }
        return resp;
    }


    @PostMapping("/unbindVehicle")
    public MessageResp unbindVehicle(@RequestBody TaxiVehicleSim bindRecord){
        MessageResp resp = new MessageResp();

        //数据校验（设置绑定状态查询）
        bindRecord.setStatus("01");
        int exsit = taxiVehicleSimService.isExsit(bindRecord);
        if(exsit <= 0){
            return CommonUtils.returnErrorInfo("当前SIM卡号与车辆不存在绑定关系");
        }
        int unbindResp = 0;
        try{
            bindRecord.setStatus("02");
            unbindResp =  taxiVehicleSimService.unbind(bindRecord);
            if(unbindResp <= 0){
                return CommonUtils.returnErrorInfo("解绑失败");
            }
            resp.setResultDesc("解绑成功");
            resp.setResult(Boolean.TRUE.toString());
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("解绑失败");
        }
        return resp;
    }


}
