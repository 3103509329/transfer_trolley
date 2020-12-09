package com.zhcx.authorization.controller.netcar.portal;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.facade.AuthUserRoleService;
import com.zhcx.auth.facade.AuthUserService;
import com.zhcx.auth.pojo.AuthUser;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.auth.pojo.AuthUserRole;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.platformtonet.facade.PermitCompanyService;
import com.zhcx.platformtonet.pojo.base.NetcarPermitCompanyInfo;
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
@Api(value = "企业经营许可信息", tags = "企业经营许可信息")
public class CompanyPermissionController {

    private Logger log = LoggerFactory.getLogger(CompanyPermissionController.class);
    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Autowired
    private PermitCompanyService permitCompanyService;

    @Autowired
    private AuthUserService userService;

    @Autowired
    private AuthUserRoleService userRoleService;
    /**
     * 企业经营许可信息列表查询
     * @return
     */
    @ApiOperation(value = "企业经营许可信息列表查询", notes = "")
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
            AuthUserResp user = sessionHandler.getUser(request);
            if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
                if(user.getCorpId() != null && user.getCorpId() != 0L){
                    corpId = user.getCorpId();
                }
            }
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
    @ApiOperation(value = "根据UUID查询企业经营许可信息详情", notes = "")
    @GetMapping("/detail")
    public MessageResp selectPermitCompanyInfoDetail(HttpServletRequest request){
        MessageResp messageResp = new MessageResp();
        try{
            Long corpId = null;
            AuthUserResp user = sessionHandler.getUser(request);
            if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
                if(user.getCorpId() != null && user.getCorpId() != 0L){
                    corpId = user.getCorpId();
                }
            }
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
    @ApiOperation(value = "企业经营许可完善", notes = "")
    @PostMapping
    public MessageResp updatePermitCompanyInfo(HttpServletRequest request, @RequestBody NetcarPermitCompanyInfo netcarPermitCompanyInfo){
        MessageResp messageResp = new MessageResp();
        AuthUserResp user = sessionHandler.getUser(request);
        if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
            if(user.getCorpId() != null && user.getCorpId() != 0L){
                netcarPermitCompanyInfo.setUuid(user.getCorpId());
            }
        }
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
    @ApiOperation(value = "企业经营许可审批", notes = "")
    @PostMapping("/{uuid}/{flag}")
    public MessageResp updatePermitCompanyInfoStatus(HttpServletRequest request, @PathVariable Long uuid,
                                                     @PathVariable Integer flag, @RequestParam(required = false) String param ){
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
    @ApiOperation(value = "企业经营许可信息删除", notes = "")
    @DeleteMapping("/{uuid}")
    public MessageResp deletePermitCompanyInfo(HttpServletRequest request, @PathVariable Integer uuid){
        MessageResp messageResp = new MessageResp();
        AuthUserResp user = sessionHandler.getUser(request);
        if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("没有权限，请联系管理员");
            return messageResp;
        }
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
