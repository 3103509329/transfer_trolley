package com.zhcx.auth.controller.auth;

import com.zhcx.auth.config.SessionConfig;
import com.zhcx.auth.config.SystemControllerLog;
import com.zhcx.auth.facade.AuthUserRoleService;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.auth.pojo.AuthUserRole;
import com.zhcx.auth.utils.Constants;
import com.zhcx.auth.utils.MessageResp;
import com.zhcx.auth.utils.UserAuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/AuthUserRole")
@Api(value = "用户授权管理", tags = "用户授权请求接口")
public class AuthUserRoleController {

	private static final Logger logger = LoggerFactory.getLogger(AuthUserRoleController.class);

	@Resource
	private AuthUserRoleService authUserRoleService;

//	@Autowired
//	private SessionConfig.SessionHandler sessionHandler;

	/*	*//**
	 * @Title: save
	 * @Description: 保存
	 * @param @param
	 *            record
	 * @param @return
	 * @return int
	 * @throws @author
	 *             tangding
	 * @date 2018年11月24日 下午2:56:57
	 *//*
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "新增授权", notes = "参数为授权对象")
	public MessageResp save(HttpServletRequest request,@RequestBody AuthUserRole record) {
		MessageResp messageResp = new MessageResp();
		AuthUserRole param = new AuthUserRole();
		BeanUtils.copyBasicProperties(record, param);
		 AuthUserResp authUser = UserAuthUtils.getUser();
		Boolean flag = false;
		try {
			if (null != authUserRoleService.saveUserRole(param,authUser.getUserId())) {
				flag = true;
			}
		} catch (Exception e) {
			messageResp.setResult(Boolean.FALSE.toString());
			messageResp.setResultDesc(e.getMessage());
			return messageResp;
		}
		if (flag) {
			messageResp.setResult(flag.toString());
			messageResp.setResultDesc("保存成功");
		} else {
			messageResp.setResult(flag.toString());
			messageResp.setResultDesc("保存失败");

		}
		return messageResp;
	}*/

	/**
	 * @Title: save
	 * @Description: 保存
	 * @param @param
	 *            record
	 * @param @return
	 * @return int
	 * @throws @author
	 *             tangding
	 * @date 2018年11月24日 下午2:56:57
	 */
	@SystemControllerLog(description = "角色授权")
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.PUT)
	@ApiOperation(value = "批量新增授权", notes = "参数为授权对象")
	public MessageResp save(HttpServletRequest request,@RequestBody List<AuthUserRole> record) {
		MessageResp messageResp = new MessageResp();
		 AuthUserResp authUser = UserAuthUtils.getUser();
		Boolean flag = false;
		try {
			if (authUserRoleService.saveUserRoleBatch(record,authUser.getUserId())) {
				flag = true;
			}
		} catch (Exception e) {
			messageResp.setResult(Boolean.FALSE.toString());
			messageResp.setResultDesc(e.getMessage());
			return messageResp;
		}
		if (flag) {
			messageResp.setResult(flag.toString());
			messageResp.setResultDesc("保存成功");
		} else {
			messageResp.setResult(flag.toString());
			messageResp.setResultDesc("保存失败");

		}
		return messageResp;
	}


	/**
	 * @Title: queryPageByParam
	 * @Description: 分页查询
	 * @param @param
	 *            AuthMenuParam
	 * @param @return
	 * @return List<AuthMenuResp>
	 * @throws @author
	 *             tangding
	 * @date 2018年11月24日 下午2:57:13
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ApiOperation(value = "查询角色", notes = "参数为角色对象")
	public MessageResp<List<AuthUserRole>> queryPage(HttpServletRequest request, @ModelAttribute AuthUserRole authUserRoleParam) {
		MessageResp<List<AuthUserRole>> message = new MessageResp<List<AuthUserRole>>();
		 AuthUserResp authUser = UserAuthUtils.getUser();
		// 此处预留参数（用户类型，数据隔离）
		if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
			message.setData(null);
			message.setResult(Boolean.FALSE.toString());
			message.setResultDesc("没有权限，请联系管理员");
			return message;
		}
		List<AuthUserRole> authUserRoleResps = authUserRoleService.queryUserRoleList(authUserRoleParam);
		message.setData(authUserRoleResps);
		message.setResult(Boolean.TRUE.toString());
		message.setResultDesc("查询成功");
		return message;
	}

}
