package com.zhcx.netcar.netcarservice.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.app.PermitDriverService;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import com.zhcx.netcar.pojo.app.NetcarPermitDriverInfo;
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
 * 驾驶员许可信息
 **/
@RestController
@RequestMapping("/netcar/permit/driver")
@Api(value = "company", tags = "驾驶员许可信息")
public class DriverPermissionController {

    private Logger log = LoggerFactory.getLogger(DriverPermissionController.class);

    @Autowired
    private PermitDriverService permitDriverService;
    /**
     * 驾驶员许可信息列表查询
     * @return
     */
    @ApiOperation(value = "驾驶员许可信息列表查询", notes = "")
    @GetMapping
    public MessageResp selectPermitDriverInfoList(HttpServletRequest request,
                                                  @RequestParam(required = false) Integer flag,
                                                  @RequestParam(required = false) String keyword,
                                                  @RequestParam(defaultValue = "1") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(required = false) String orderBy){
        MessageResp messageResp = new MessageResp();
        try{
            Long corpId = null;
            PageInfo<NetcarPermitDriverInfo> pageInfo = permitDriverService.selectPermitDriverInfoList(corpId, keyword, flag, pageNo, pageSize, orderBy);
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
     * 驾驶员许可信息新增
     * @return
     */
    @ApiOperation(value = "驾驶员许可信息新增", notes = "")
    @PostMapping
    public MessageResp insertPermitDriverInfo(HttpServletRequest request, @RequestBody NetcarPermitDriverInfo netcarPermitDriverInfo){
        MessageResp messageResp = new MessageResp();
        try{
            int row = permitDriverService.insertPermitDriverInfo(netcarPermitDriverInfo);
            if(row > 0){
                messageResp.setResultDesc("新增成功");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("新增异常");
        }
        return messageResp;
    }

    /**
     * 驾驶员许可信息更新
     * @return
     */
    @ApiOperation(value = "驾驶员许可信息更新", notes = "")
    @PutMapping
    public MessageResp updatePermitDriverInfo(HttpServletRequest request, @RequestBody NetcarPermitDriverInfo netcarPermitDriverInfo){
        MessageResp messageResp = new MessageResp();
        try{
            int row = permitDriverService.updatePermitDriverInfo(netcarPermitDriverInfo);
            if(row > 0){
                messageResp.setResultDesc("更新成功");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("更新异常");
        }
        return messageResp;
    }

    /**
     * 驾驶员许可审核
     *
     * @return
     */
    @ApiOperation(value = "驾驶员许可审核", notes = "")
    @PostMapping("/{uuid}/{flag}")
    public MessageResp updatePermitDriverInfo(HttpServletRequest request, @PathVariable Long uuid, @PathVariable Integer flag, @RequestBody(required = false) String param) {

        MessageResp messageResp = new MessageResp();
        try {
            JSONObject jsonObject = JSONObject.parseObject(param);
            String reason = "";
            if(null != jsonObject){
                reason = jsonObject.getString("reason");
            }
            int row = permitDriverService.updatePermitDriverInfoStatus(uuid, flag, reason);
            if (row ==0) {
                return CommonUtils.returnErrorInfo("审核失败");
            }
            messageResp.setResultDesc("审核成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("审核异常");
        }
        return messageResp;
    }

}
