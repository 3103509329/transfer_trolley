package com.zhcx.auth.controller.auth;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.config.SystemControllerLog;
import com.zhcx.auth.facade.AccountInfoService;
import com.zhcx.auth.facade.AuthRoleService;
import com.zhcx.auth.facade.AuthUserService;
import com.zhcx.auth.pojo.AccountInfo;
import com.zhcx.auth.pojo.AuthRole;
import com.zhcx.auth.pojo.AuthUser;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.auth.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/auth/user")
@Api(value = "authUser", tags = "用户接口")
public class AuthUserContrllor {

    Logger logger = LoggerFactory.getLogger(AuthUserContrllor.class);

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private AuthRoleService authRoleService;
//    @Autowired
//    private SessionConfig.SessionHandler sessionHandler;

    /**
     * 跳转登录地址
     */
    @Value("${login.url}")
    public String loginUrl;

    @ApiOperation(value = "获取用户列表", notes = "")
    @ApiImplicitParam(name = "param", value = "用户对象(参：AuthUser)"/*, dataType = "AuthUser"*/)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public MessageResp<List<AuthUserResp>> queryUser(HttpServletRequest request, @ModelAttribute AuthUser param) {
        MessageResp<List<AuthUserResp>> message = new MessageResp<List<AuthUserResp>>();
        PageInfo<AuthUserResp> pageInfo = null;
        AuthUserResp authUser = UserAuthUtils.getUser();

        List<AuthRole> a = authRoleService.selectByUserIdAndCode(authUser.getUserId(), "xtgl");
        a.forEach(item -> {
            //企业角色获取当前企业的数据
            if (item.getRoleType().equals("2")) {
                param.setCorpId(authUser.getCorpId());
                return;
            }
        });
        param.setUserId(authUser.getUserId());
        pageInfo = authUserService.queryAuthUser(param);
        message.setPageBean(PageBeanUtil.createPageBean(pageInfo));
        message.setData(pageInfo.getList());
        message.setResult(Boolean.TRUE.toString());
        message.setResultDesc("查询成功");
        return message;
    }

    @ApiOperation(value = "获取用户列表(不带分页)", notes = "")
    @ApiImplicitParam(name = "param", value = "用户对象(参：AuthUser)"/*, dataType = "AuthUser"*/)
    @RequestMapping(value = "/queryUserList", method = RequestMethod.GET)
    public MessageResp<List<AuthUserResp>> queryUserList(HttpServletRequest request, @ModelAttribute AuthUser param) {
        MessageResp<List<AuthUserResp>> message = new MessageResp<List<AuthUserResp>>();
        AuthUserResp authUser = UserAuthUtils.getUser();
//		 此处预留参数（用户类型，数据隔离）
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType()) && !Constants.AUTH_USER_TYPE_ORG.equals(authUser.getUserType())) {
            message.setResult(Boolean.FALSE.toString());
            message.setResultDesc("没有权限查询用户信息");
            return message;
        }
        List<AuthUserResp> list = authUserService.queryAuthUserList(param);
        message.setData(list);
        message.setResult(Boolean.TRUE.toString());
        message.setResultDesc("查询成功");
        return message;
    }

    /**
     * @param @param  record
     * @param @return
     * @return int
     * @throws @author tangding
     * @Title: save
     * @Description: 保存
     * @date 2018年11月24日 下午2:56:57
     */
    @SystemControllerLog(description = "新增系统用户", actionType = "2")
    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "新增用户", notes = "参数为用户对象")
    public MessageResp save(HttpServletRequest request, @RequestBody AuthUser record) {
        MessageResp messageResp = new MessageResp();
        AuthUser param = new AuthUser();
        BeanUtils.copyBasicProperties(record, param);
        Boolean flag = false;
        AuthUserResp resp = authUserService.verificationUserName(record.getUserName());
        int mobieflag = authUserService.verificationMobilePhone(record.getMobilePhone());
        if (mobieflag > 0) {
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("手机号已经存在");
            return messageResp;
        }
        if (null != resp) {
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("用户名已经存在");
            return messageResp;
        }
        AuthUserResp authUser = UserAuthUtils.getUser();
        param.setPassword(Constants.AUTH_USER_DEFAULT_PASSWORD);
//		param.setUserType(authUser.getUserType());
        param.setUserStatus(Constants.AUTH_USER_STATUS);
        try {
            if (authUserService.insertAuthUser(param) > 0) {
                flag = true;
            }
        } catch (Exception e) {
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("新增用户失败");
            return messageResp;
        }
        if (flag) {
            messageResp.setResult(flag.toString());
            messageResp.setResultDesc("新增成功");
        } else {
            messageResp.setResult(flag.toString());
            messageResp.setResultDesc("新增失败");

        }
        return messageResp;
    }

    /**
     * @param @param  request
     * @param @param  param
     * @param @return
     * @return MessageResp
     * @throws
     * @Title: modifyPassword
     * @Description:用户自己修改密码
     */
    @SystemControllerLog(description = "用户自己修改密码", actionType = "5")
    @ApiOperation(value = "用户自己修改密码", notes = "")
    @RequestMapping(value = "modifyPwd", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    public MessageResp modifyPwd(HttpServletRequest request, HttpServletResponse response, @RequestParam String oldPwd, @RequestParam String password/*,@RequestParam String account*/) {
//    public MessageResp modifyPwd(HttpServletRequest request, HttpServletResponse response, @RequestBody AccountInfo accountInfo) {

        MessageResp resp = new MessageResp();
        AuthUserResp authUser = UserAuthUtils.getUser();
        int temp = accountInfoService.verifyAccount(authUser.getAccountName(), oldPwd);
        if (-1 == temp) {
            resp.setResult("false");
            resp.setResultDesc("旧密码不正确!");
            return resp;
        }

        try {
            AccountInfo info = new AccountInfo();
            info.setAccountName(authUser.getAccountName());
            info.setAccountPwd(password);
            AccountInfo result = accountInfoService.updateUserAccoutnInfo(info);
            if (null != result) {
//                sessionHandler.setUser(request.getSession().getId(), null);
//				response.sendRedirect(loginUrl);
                resp.setResult("true");
                resp.setResultDesc("用户密码修改成功!");
            } else {
                resp.setResult("false");
                resp.setResultDesc("用户密码修改失败！");
            }
        } catch (Exception e) {
            logger.error("AuthUserController--modifyPwd--修改用户密码异常！", e);
            resp.setResult("false");
            resp.setResultDesc("用户密码修改失败！");
        }

        return resp;
    }

    /**
     * @param @param  request
     * @param @param  param
     * @param @return
     * @return MessageResp
     * @throws
     * @Title: modifyPassword
     * @Description:管理重置用户密码
     */
    @SystemControllerLog(description = "管理重置用户密码", actionType = "4")
    @ApiOperation(value = "管理重置用户密码", notes = "")
    @RequestMapping(value = "sysModifyPwd", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    public MessageResp sysModifyPwd(HttpServletRequest request, @RequestBody AccountInfo data) {
        MessageResp resp = new MessageResp();
        AuthUserResp authUser = UserAuthUtils.getUser();
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
            resp.setResult("false");
            resp.setResultDesc("没有权限重置密码，请联系管理员!");
            return resp;
        }
		/*int temp = accountInfoService.verifyAccount(authUser.getAccountName(),oldpwd);
		if(-1==temp){
				resp.setResult("false");
				resp.setResultDesc("账号或者密码不正确!");
				return resp;
	    }*/

        AccountInfo info = new AccountInfo();
//		info.setAccountName(account);
        info.setUserId(data.getUserId());
        info.setAccountPwd(Constants.AUTH_USER_DEFAULT_PASSWORD);

        try {
            AccountInfo result = accountInfoService.updateUserAccoutnInfo(info);
            if (null != result) {
                resp.setResult("true");
                resp.setResultDesc("用户密码已重置!");
            } else {
                resp.setResult("false");
                resp.setResultDesc("用户密码重置失败！");
            }
        } catch (Exception e) {
            logger.error("AuthUserController--sysModifyPwd--重置用户密码异常！", e);
            resp.setResult("false");
            resp.setResultDesc("重置用户密码失败！");
        }

        return resp;
    }

    /**
     * 修改用户信息
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SystemControllerLog(description = "修改用户信息", actionType = "3")
    @ResponseBody
    @ApiOperation(value = "修改用户信息", notes = "")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public MessageResp modifyUser(HttpServletRequest request, HttpServletResponse response,
                                  @RequestBody AuthUser authUser) throws Exception {
        MessageResp result = new MessageResp();
        try {
            // 查询用户是否存在
            AuthUser user = authUserService.queryAuthUserByUuid(authUser.getUuid());
            if (null == user) {
                result.setStatusCode("-50");
                result.setResult("false");
                result.setResultDesc("用户不存在");
                return result;
            }
            int temp = authUserService.updateAuthUser(authUser);
            if (temp > 0) {
                result.setResult("true");
                result.setResultDesc("修改成功");
            } else {
                result.setStatusCode("-50");
                result.setResult("false");
                result.setResultDesc("修改用户信息失败");
            }
        } catch (Exception e) {
            result.setStatusCode("-50");
            result.setResult("false");
            result.setResultDesc("修改用户信息失败");
            logger.error("修改用户信息异常" + e.getMessage());
        }
        return result;
    }

    /**
     * @param @param  uuid
     * @param @return
     * @return MessageResp
     * @throws @author tangding
     * @Title: delete
     * @Description: 删除
     * @date 2018年11月27日 下午1:43:58
     */
    @SystemControllerLog(description = "删除用户")
    @ResponseBody
    @RequestMapping(value = "{uuid}", method = RequestMethod.DELETE)
    public MessageResp delete(@PathVariable Long uuid) {
        MessageResp messageResp = new MessageResp();
        Boolean flag = false;
        try {
            if (authUserService.deleteAuthUser(uuid) > 0) {
                flag = true;
            }
        } catch (Exception e) {
            messageResp.setResult(flag.toString());
            messageResp.setResultDesc("删除失败");
        }
        if (flag) {
            messageResp.setResult(flag.toString());
            messageResp.setResultDesc("删除成功");
        } else {
            messageResp.setResult(flag.toString());
            messageResp.setResultDesc("删除失败");

        }

        return messageResp;
    }

    @ApiOperation(value = "获取登录用户信息", notes = "")
    @RequestMapping(value = "getUser", method = RequestMethod.GET)
    public MessageResp<AuthUserResp> getUser(HttpServletRequest request) {
//        System.out.println("--------获取登录用户信息----------");
        MessageResp<AuthUserResp> message = new MessageResp<AuthUserResp>();
        AuthUserResp authUser = UserAuthUtils.getUser();
        message.setData(authUser);
        message.setResult(Boolean.TRUE.toString());
        message.setResultDesc("查询成功");
        return message;
    }

    /**
     * 基于用户id获取用户的全量数据
     *
     * @param userId
     * @return
     */
    private AuthUser getUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return authUserService.queryAuthUserByUuid(userId);
    }

//    private AuthRole getRole(Long userId,String applicationCode){
//
//        AuthRole authRole = authRoleService.selectByUserIdAndCode(userId, applicationCode);
//    }
}