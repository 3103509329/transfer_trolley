package com.zhcx.authorization.controller.netcar.portal;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.platformtonet.facade.PermitVehicleService;
import com.zhcx.platformtonet.pojo.base.NetcarPermitVehicleInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/2/22 17:03
 * 车辆许可信息
 **/
@RestController
@RequestMapping("/netcar/permit/vehicle")
@Api(value = "车辆许可信息", tags = "车辆许可信息")
public class VehiclePermissionController {

    private Logger log = LoggerFactory.getLogger(VehiclePermissionController.class);
    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Autowired
    private PermitVehicleService permitVehicleService;
    /**
     * 车辆许可信息列表查询
     * @return
     */
    @ApiOperation(value = "车辆许可信息列表查询", notes = "")
    @GetMapping
    public MessageResp selectPermitVehicleInfoList(HttpServletRequest request,
                                                   @RequestParam(required = false) String keyword,
                                                   @RequestParam(required = false) Integer flag,
                                                   @RequestParam(defaultValue = "1") Integer pageNo,
                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                   @RequestParam(required = false) String orderBy){
        MessageResp messageResp = new MessageResp();
        try{
            Long corpId = null;
            AuthUserResp user = sessionHandler.getUser(request);
            if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
                corpId = user.getCorpId();
            }
            PageInfo<NetcarPermitVehicleInfo> pageInfo = permitVehicleService.selectPermitVehicleInfoList(corpId, keyword, flag, pageNo, pageSize, orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询异常");
        }
        return messageResp;
    }


    /**
     * 车辆许可新增
     * @return
     */
    @ApiOperation(value = "车辆许可新增", notes = "")
    @PostMapping
    public MessageResp insertPermitVehicleInfo(HttpServletRequest request, @RequestBody NetcarPermitVehicleInfo netcarPermitVehicleInfo){
        MessageResp messageResp = new MessageResp();
        try{
            AuthUserResp user = sessionHandler.getUser(request);
            if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
                if(user.getCorpId() != null && user.getCorpId() != 0L){
                    netcarPermitVehicleInfo.setCorpId(user.getCorpId());
                }
            }

            int row = permitVehicleService.insertPermitVehicleInfo(netcarPermitVehicleInfo);
            if(row == 0){
                return CommonUtils.returnErrorInfo("新增失败");
            }
            messageResp.setResultDesc("新增成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("新增异常");
        }
        return messageResp;
    }

    /**
     * 车辆许可信息更新
     * @return
     */
    @ApiOperation(value = "车辆许可信息更新", notes = "")
    @PutMapping
    public MessageResp updatePermitVehicleInfo(HttpServletRequest request, @RequestBody NetcarPermitVehicleInfo netcarPermitVehicleInfo){
        MessageResp messageResp = new MessageResp();
        AuthUserResp user = sessionHandler.getUser(request);
        if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("没有权限，请联系管理员");
            return messageResp;
        }
        try{
            int row = permitVehicleService.updatePermitVehicleInfo(netcarPermitVehicleInfo);
            if(row == 0){
                return CommonUtils.returnErrorInfo("更新失败");
            }
            messageResp.setResultDesc("更新成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("更新异常");
        }
        return messageResp;
    }

    /**
     * 车辆许可信息审核
     * @return
     */
    @ApiOperation(value = "车辆许可信息审核", notes = "")
    @PostMapping("/{uuid}/{flag}")
    public MessageResp updatePermitVehicleInfoStatus(HttpServletRequest request, @PathVariable Long uuid,@PathVariable Integer flag,@RequestParam(required = false) String param){
        MessageResp messageResp = new MessageResp();
        AuthUserResp user = sessionHandler.getUser(request);
        if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("没有权限，请联系管理员");
            return messageResp;
        }
        try{
            JSONObject jsonObject = JSONObject.parseObject(param);
            String reason = "";
            if(null != jsonObject){
                reason = jsonObject.getString("reason");
            }
            int row = permitVehicleService.updatePermitVehicleInfoStatus(uuid, flag, reason);
            if(row == 0){
                return CommonUtils.returnErrorInfo("审核失败");
            }
            messageResp.setResultDesc("审核成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("审核异常");
        }
        return messageResp;
    }

}
