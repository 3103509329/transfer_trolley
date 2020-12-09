package com.zhcx.authorization.controller.auth;

import java.util.ArrayList;
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
import com.zhcx.auth.facade.AuthMenuRelationService;
import com.zhcx.auth.facade.AuthMenuService;
import com.zhcx.auth.facade.AuthUserRoleService;
import com.zhcx.auth.pojo.AuthMenu;
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
@RequestMapping("/AuthMenu")
@Api(value = "菜单管理", tags = "菜单请求接口")
public class AuthMenuController {

	private static final Logger logger = LoggerFactory.getLogger(AuthMenuController.class);

	@Resource
	private AuthMenuService authMenuService;

	@Resource
	private AuthMenuRelationService authMenuRelationService;

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
	@SystemControllerLog(description = "新增菜单")
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "新增菜单", notes = "参数为菜单对象")
	public MessageResp save(@RequestBody AuthMenu record) {
		AuthMenu AuthMenu = new AuthMenu();
		BeanUtils.copyBasicProperties(record, AuthMenu);
		MessageResp messageResp = new MessageResp();
		Boolean flag = false;
		try {
			if (authMenuService.save(AuthMenu) > 0) {
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
	@SystemControllerLog(description = "修改菜单")
	@ResponseBody
	@RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
	@ApiOperation(value = "修改菜单", notes = "参数为菜单对象")
	public MessageResp update(@RequestBody AuthMenu record) {
		MessageResp messageResp = new MessageResp();
		AuthMenu AuthMenu = new AuthMenu();
		BeanUtils.copyBasicProperties(record, AuthMenu);
		Boolean flag = false;
		try {
			if (authMenuService.modifyByUuid(AuthMenu) > 0) {
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
	 * @Title: queryList
	 * @Description: 查询集合
	 * @param @param
	 *            AuthMenuParam
	 * @param @return
	 * @return List<AuthMenuResp>
	 * @throws @author
	 *             tangding
	 * @date 2018年11月24日 下午2:57:03
	 */
	@ResponseBody
	@RequestMapping(value = "/queryList", method = RequestMethod.GET)
	@ApiOperation(value = "菜单集合，不带分页", notes = "参数为菜单对象")
	public MessageResp<List<AuthMenu>> queryList(@ModelAttribute AuthMenu authMenuParam) {
		MessageResp<List<AuthMenu>> data = new MessageResp<List<AuthMenu>>();
		List<AuthMenu> AuthMenuResps = authMenuService.queryList(authMenuParam);
		data.setData(AuthMenuResps);
		return data;
	}

	/**
	 * @Title: queryListAndOperation
	 * @Description: 查询菜单和功能点(把功能点封装成菜单的格式)
	 * @param @param
	 *            AuthMenuParam
	 * @param @return
	 * @return Data
	 * @throws @author
	 *             tangding
	 * @date 2018年11月24日 下午6:14:56
	 */
	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping(value = "/queryListAndOperation", method =
	 * RequestMethod.GET) public MessageResp<List<AuthMenu>>
	 * queryListAndOperation(@ModelAttribute AuthMenu AuthMenuParam) {
	 * MessageResp<List<AuthMenu>> data = new MessageResp<List<AuthMenu>>();
	 * List<AuthMenu> AuthMenuResps =
	 * authMenuService.queryListAndOperation(AuthMenuParam);
	 * data.setData(AuthMenuResps); return data; }
	 */

	/**
	 * @Title: queryFirst
	 * @Description: 查询第一条数据
	 * @param @param
	 *            AuthMenuParam
	 * @param @return
	 * @return AuthMenuResp
	 * @throws @author
	 *             tangding
	 * @date 2018年11月24日 下午2:57:06
	 */
	@ResponseBody
	@RequestMapping(value = "/queryFirst", method = RequestMethod.GET)
	public MessageResp<List<AuthMenu>> queryFirst(@ModelAttribute AuthMenu authMenuParam) {
		MessageResp<List<AuthMenu>> data = new MessageResp<List<AuthMenu>>();
		AuthMenu AuthMenuResp = authMenuService.queryFirst(authMenuParam);
		List<AuthMenu> AuthMenuResps = new ArrayList<AuthMenu>();
		AuthMenuResps.add(AuthMenuResp);
		data.setData(AuthMenuResps);
		return data;
	}

	/**
	 * @Title: queryByUuid
	 * @Description: 根据uuid查询
	 * @param @param
	 *            uuid
	 * @param @return
	 * @return AuthMenuResp
	 * @throws @author
	 *             tangding
	 * @date 2018年11月24日 下午2:57:08
	 */
	@ResponseBody
	@RequestMapping(value = "{uuid}", method = RequestMethod.GET)
	public MessageResp<List<AuthMenu>> queryByUuid(@PathVariable Long uuid) {
		MessageResp<List<AuthMenu>> data = new MessageResp<List<AuthMenu>>();
		AuthMenu AuthMenuResp = authMenuService.queryByUuid(uuid);
		List<AuthMenu> AuthMenuResps = new ArrayList<AuthMenu>();
		AuthMenuResps.add(AuthMenuResp);
		data.setData(AuthMenuResps);
		return data;
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
	@ApiOperation(value = "分页查询菜单", notes = "参数为菜单对象")
	public MessageResp<List<AuthMenu>> queryPage(HttpServletRequest request, @ModelAttribute AuthMenu authMenuParam) {

		MessageResp<List<AuthMenu>> message = new MessageResp<List<AuthMenu>>();
		PageInfo<AuthMenu> pageInfo = null;
		AuthUserResp authUser = sessionHandler.getUser(request);
		// 此处预留参数（用户类型，数据隔离）
		if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
			message.setData(null);
			message.setResult(Boolean.FALSE.toString());
			message.setResultDesc("没有权限，请联系管理员");
			return message;
		}

		pageInfo = authMenuService.queryPageByParam(authMenuParam);
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
	@SystemControllerLog(description = "删除菜单")
	@ResponseBody
	@RequestMapping(value = "{uuid}", method = RequestMethod.DELETE)
	public MessageResp delete(@PathVariable Long uuid) {
		  MessageResp messageResp = new MessageResp();
		  AuthMenu authMenu =new AuthMenu();
		  authMenu.setUuid(uuid);
		  Boolean flag = false; 
		  if (authMenuService.delete(authMenu) > 0){
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

	/**
	 * @Title: queryUserMenu
	 * @Description: 查询当前用户菜单
	 * @param @param
	 *            request
	 * @param @return
	 * @return message
	 * @throws @author
	 *             tangding
	 * @date 2018年12月11日 下午2:16:28
	 */
	
	  @ResponseBody
	  @RequestMapping(value = "/queryUserMenu", method = RequestMethod.GET)
	  public MessageResp<List<AuthMenu>> queryUserMenu(HttpServletRequest request) {
		  MessageResp<List<AuthMenu>> message = new MessageResp<List<AuthMenu>>();
		  AuthUserResp authUser = sessionHandler.getUser(request);
		  AuthUserRole authUserRole = new AuthUserRole();
		  authUserRole.setUserId(authUser.getUserId());
		  List<AuthUserRole> userRoles = authUserRoleService.queryUserRoleList(authUserRole);
		  if(null == userRoles||userRoles.size()<1){
			   message.setData(null);
			   message.setStatusCode("-50");
			   message.setResult(Boolean.FALSE.toString());
			   message.setResultDesc("没有权限访问，请联系管理员！");
			   return message;
		  }
		  Long[] registerIds = new Long[userRoles.size()];
		  for (int i = 0; i < userRoles.size(); i++) {
			  registerIds[i] = userRoles.get(i).getRoleId();
		  }
		  List<AuthMenu> list = authMenuService.queryMenusByRegisterId(registerIds);
		  message.setData(list);
		   message.setResult(Boolean.TRUE.toString());
		   message.setResultDesc("查询成功");
		   return message;
	 }


	/*
	 * @RequestMapping("path")
	 * 
	 * @ResponseBody public MessageResp path(HttpServletRequest request) {
	 * MessageResp resp = new MessageResp(); AuthMenuParam authMenuParam = new
	 * AuthMenuParam(); List<AuthMenuResp> authMenuResps =
	 * authMenuService.queryList(authMenuParam);
	 * 
	 * List<AuthMenuResp> authMenuResps2 = authMenuResps;
	 * 
	 * List<AuthMenuResp> authMenuResps3 = new ArrayList<AuthMenuResp>();
	 * 
	 * List<AuthMenuResp> authMenuResps4 = new ArrayList<AuthMenuResp>();
	 * 
	 * AuthMenuRelationParam param = new AuthMenuRelationParam();
	 * List<AuthMenuRelationResp> list =
	 * AuthMenuRelationService.queryList(param);
	 * 
	 * for (int i = 0; i < authMenuResps2.size(); i++) { AuthMenuResp beani =
	 * authMenuResps2.get(i); Long uuid = (long) (i+1); for (int j = 0; j <
	 * authMenuResps2.size(); j++) { AuthMenuResp beanj = authMenuResps2.get(j);
	 * if(beani.getUuid().equals(beanj.getParentId())){ beanj.setParentId(uuid);
	 * authMenuResps4.add(beanj); } } for (int e = 0; e < list.size(); e++) {
	 * AuthMenuRelationResp mr = list.get(e); String menustr = mr.getMenuIds();
	 * String newstr = menustr.replace((beani.getUuid()+","), (uuid+","));
	 * mr.setMenuIds(newstr); } beani.setUuid(uuid); authMenuResps3.add(beani);
	 * }
	 * 
	 * 
	 * for (int i = 0; i < authMenuResps3.size(); i++) { AuthMenuResp modelresp
	 * = authMenuResps3.get(i); AuthMenu model =new AuthMenu();
	 * BeanUtils.copyProperties(modelresp, model); authMenuService.save(model);
	 * }
	 * 
	 * for (int e = 0; e < list.size(); e++) { AuthMenuRelationResp mr =
	 * list.get(e);
	 * System.out.println("id:"+mr.getUuid()+"menuids"+mr.getMenuIds()); }
	 * return resp; }
	 */
}
