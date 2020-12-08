package com.zhcx.netcar.netcarservice.controller.app;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.dubbo.AuthUserDubboService;
import com.zhcx.auth.dubbo.AuthUserRoleDubboService;
import com.zhcx.auth.pojo.AuthUser;
import com.zhcx.auth.pojo.AuthUserRole;
import com.zhcx.netcar.facade.app.PermitCompanyService;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.Constants;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import com.zhcx.netcar.pojo.app.NetcarPermitCompanyInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/2/22 17:03
 * 企业经营许可信息
 **/
@RestController
@RequestMapping("/netcar/permit/company")
@Api(value = "企业经营许可信息", tags = "企业经营许可信息（App）")
public class CompanyPermissionController {

    private Logger log = LoggerFactory.getLogger(CompanyPermissionController.class);

    @Autowired
    private PermitCompanyService permitCompanyService;

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private AuthUserDubboService userService;

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private AuthUserRoleDubboService userRoleService;
    /**
     * 企业经营许可信息列表查询
     * @return
     */
    @ApiOperation(value = "企业经营许可信息列表查询（App）", notes = "")
    @GetMapping
    public MessageResp selectPermitCompanyInfoList(HttpServletRequest request,
                                                   @RequestParam(required = false) Integer flag,
                                                   @RequestParam(required = false) String keyword,
                                                   @RequestParam(defaultValue = "1") Integer pageNo,
                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                   @RequestParam(required = false) String orderBy){
        MessageResp messageResp = new MessageResp();
        try{
            Long corpId = null;
            PageInfo<NetcarPermitCompanyInfo> pageInfo = permitCompanyService.selectPermitCompanyInfoList(corpId, flag, keyword, pageNo, pageSize, orderBy);
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
     * 根据UUID查询企业经营许可信息详情
     * @return
     */
    @ApiOperation(value = "根据UUID查询企业经营许可信息详情（App）", notes = "")
    @GetMapping("/detail")
    public MessageResp selectPermitCompanyInfoDetail(HttpServletRequest request){
        MessageResp messageResp = new MessageResp();
        try{
            Long corpId = null;
            NetcarPermitCompanyInfo netcarPermitCompanyInfo = permitCompanyService.selectPermitCompanyInfoDetail(corpId);
            messageResp.setData(netcarPermitCompanyInfo);
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
     * 企业经营许可完善
     * 企业账号注册后，账号信息已经新增了，当企业用户申请经营许可，直接更新原表记录。
     * @return
     */
    @ApiOperation(value = "企业经营许可完善（App）", notes = "")
    @PostMapping
    public MessageResp updatePermitCompanyInfo(HttpServletRequest request, @RequestBody NetcarPermitCompanyInfo netcarPermitCompanyInfo){
        MessageResp messageResp = new MessageResp();
        try{
            int row = permitCompanyService.updatePermitCompanyInfo(netcarPermitCompanyInfo);
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
     * 企业经营许可审批
     * 管理员
     * @return
     */
    @ApiOperation(value = "企业经营许可审批（App）", notes = "")
    @PostMapping("/{uuid}/{flag}")
    public MessageResp updatePermitCompanyInfoStatus(HttpServletRequest request, @PathVariable Long uuid,
                                                     @PathVariable Integer flag, @RequestParam(required = false) String param ){
        MessageResp messageResp = new MessageResp();

        try{
            JSONObject jsonObject = JSONObject.parseObject(param);
            String reason = "";
            if(null != jsonObject){
                reason = jsonObject.getString("reason");
            }
            int row = permitCompanyService.updatePermitCompanyInfoStatus(uuid, flag, reason);

            if(row == 0){
                return CommonUtils.returnErrorInfo("审核失败");
            }

            //todo 公司审批通过，创建公司账号
            //添加企业
//            Long corpId = companyService.insertCompany(company);
            Long corpId = uuid;
            String phone = permitCompanyService.selectPhoneByUuid(uuid);
            if (StringUtils.isBlank(phone)) {
                return CommonUtils.returnErrorInfo("审核失败");
            }
            //添加企业管理员账号
            AuthUser companyUser = new AuthUser();
            companyUser.setCorpId(corpId);
            companyUser.setUserStatus(Constants.AUTH_USER_STATUS);
            companyUser.setUserType(Constants.AUTH_USER_TYPE_SUPPLIER);
            companyUser.setUserName(phone);
            companyUser.setMobilePhone(phone);
            companyUser.setSource(Constants.DEAULT_TAXI_SOURCE);
            companyUser.setPassword(Constants.AUTH_USER_DEFAULT_PASSWORD);
            Long userId = userService.insertAuthUser(companyUser);
            AuthUserRole authUserRole = new AuthUserRole();
            authUserRole.setRoleId(3L);
            authUserRole.setUserId(userId);
            userRoleService.saveUserRole(authUserRole, uuid);
            messageResp.setResultDesc("审批成功");

        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("审批异常");
        }
        return messageResp;
    }


    /**
     * 企业经营许可信息删除
     * @return
     */
    @ApiOperation(value = "企业经营许可信息删除（App）", notes = "")
    @DeleteMapping("/{uuid}")
    public MessageResp deletePermitCompanyInfo(HttpServletRequest request, @PathVariable Integer uuid){
        MessageResp messageResp = new MessageResp();
        try{
            int row = permitCompanyService.deletePermitCompanyInfo(uuid);
            if(row == 0){
                return CommonUtils.returnErrorInfo("删除失败");
            }
            messageResp.setResultDesc("删除成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("删除异常");
        }
        return messageResp;
    }

}
