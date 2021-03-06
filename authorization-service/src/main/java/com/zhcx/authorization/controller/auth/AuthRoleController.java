package com.zhcx.authorization.controller.auth;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.zhcx.authorization.config.SystemControllerLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.facade.AuthRoleService;
import com.zhcx.auth.facade.AuthUserRoleService;
import com.zhcx.auth.pojo.AuthRole;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.auth.pojo.AuthUserRole;
import com.zhcx.authorization.config.SessionConfig.SessionHandler;
import com.zhcx.authorization.utils.BeanUtils;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/AuthRole")
@Api(value = "角色管理", tags = "角色请求接口")
public class AuthRoleController {

	private static final Logger logger = LoggerFactory.getLogger(AuthRoleController.class);

	@Resource
	private AuthRoleService authRoleService;

	@Autowired
	private SessionHandler sessionHandler;
	
	@Resource
	private AuthUserRoleService authUserRoleService;

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
	@SystemControllerLog(description = "新增角色")
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "新增角色", notes = "参数为角色对象")
	public MessageResp save(@RequestBody AuthRole record) {
		MessageResp messageResp = new MessageResp();
		AuthRole param = new AuthRole();
		BeanUtils.copyBasicProperties(record, param);
		Boolean flag = false;
		try {
			List<AuthRole> list = authRoleService.queryAuthRoleByParam(param);
			 if(null != list&&list.size()>0){
				  messageResp.setResult(flag.toString());
				  messageResp.setResultDesc("角色已存在");
				  return messageResp;
			  }
			if (authRoleService.saveAuthRole(param) > 0) {
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
	 * @Title: updateByParam
	 * @Description: 更新
	 * @param @param
	 *            record
	 * @param @return
	 * @return int
	 * @throws @author
	 *             tangding
	 * @date 2018年11月24日 下午2:57:00
	 */
	@SystemControllerLog(description = "修改角色")
	@ResponseBody
	@RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
	@ApiOperation(value = "修改角色", notes = "参数为角色对象")
	public MessageResp update(@RequestBody AuthRole record) {
		MessageResp messageResp = new MessageResp();
		AuthRole param = new AuthRole();
		BeanUtils.copyBasicProperties(record, param);
		Boolean flag = false;
		try {
			List<AuthRole> list = authRoleService.queryAuthRoleByParam(param);
			 if(null != list&&list.size()>0){
				 if(list.get(0).getUuid()!=record.getUuid()){
					 messageResp.setResult(flag.toString());
					  messageResp.setResultDesc("角色已存在");
					  return messageResp;
				 }
			  }
			if (authRoleService.modifyAuthRole(param) > 0) {
				flag = true;
			}
		} catch (Exception e) {
			messageResp.setResult(Boolean.FALSE.toString());
			messageResp.setResultDesc(e.getMessage());
			return messageResp;
		}
		if (flag) {
			messageResp.setResult(flag.toString());
			messageResp.setResultDesc("更新成功");
		} else {
			messageResp.setResult(flag.toString());
			messageResp.setResultDesc("更新失败");

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
	@ApiOperation(value = "分页查询角色", notes = "参数为角色对象")
	public MessageResp<List<AuthRole>> queryPage(HttpServletRequest request, @ModelAttribute AuthRole authRoleParam) {

		MessageResp<List<AuthRole>> message = new MessageResp<List<AuthRole>>();
		PageInfo<AuthRole> pageInfo = null;
		AuthUserResp authUser = sessionHandler.getUser(request);
		// 此处预留参数（用户类型，数据隔离）
		if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
			message.setData(null);
			message.setResult(Boolean.FALSE.toString());
			message.setResultDesc("没有权限，请联系管理员");
			return message;
		}

		pageInfo = authRoleService.queryAuthRoleListByParam(authRoleParam);
		message.setPageBean(PageBeanUtil.createPageBean(pageInfo));
		message.setData(pageInfo.getList());
		message.setResult(Boolean.TRUE.toString());
		message.setResultDesc("查询成功");
		return message;
	}

	/**
	 * @Title: delete
	 * @Description: 删除
	 * @param @param
	 *            uuid
	 * @param @return
	 * @return MessageResp
	 * @throws @author
	 *             tangding
	 * @date 2016年8月17日 下午1:43:58
	 */
	@SystemControllerLog(description = "删除角色")
	@ResponseBody
	@RequestMapping(value = "{uuid}", method = RequestMethod.DELETE)
	public MessageResp delete(@PathVariable Long uuid) {
		  MessageResp messageResp = new MessageResp();
		  Boolean flag = false; 
		  AuthUserRole authUserRole =new AuthUserRole();
		  authUserRole.setRoleId(uuid);
		  List<AuthUserRole> list = authUserRoleService.queryUserRoleList(authUserRole);
		  if(null != list&&list.size()>0){
			  messageResp.setResult(flag.toString());
			  messageResp.setResultDesc("该角色已绑定用户，删除失败");
			  return messageResp;
		  }
		  if (authRoleService.deleteRole(uuid) > 0){
			  flag = true; 
		  }  
		 if (flag) {
			 messageResp.setResult(flag.toString());
			 messageResp.setResultDesc("删除成功"); 
		  }else {
			  messageResp.setResult(flag.toString());
			  messageResp.setResultDesc("删除失败");
		  
		  }
		 
		return messageResp;
	}
}
