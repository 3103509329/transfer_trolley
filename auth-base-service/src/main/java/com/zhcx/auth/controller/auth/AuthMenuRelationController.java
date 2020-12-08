package com.zhcx.auth.controller.auth;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.config.SessionConfig;
import com.zhcx.auth.config.SystemControllerLog;
import com.zhcx.auth.facade.AuthMenuRelationService;
import com.zhcx.auth.pojo.AuthMenuRelation;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.auth.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/AuthMenuRelation")
@Api(value = "权限授权菜单功能点管理", tags = "权限授权菜单功能点请求接口")
public class AuthMenuRelationController {

	@Resource
	private AuthMenuRelationService authMenuRelationService;

//	@Autowired
//	private SessionConfig.SessionHandler sessionHandler;

	/**
	 * @param @param  record
	 * @param @return
	 * @return int
	 * @throws
	 * @Title: save
	 * @Description: 保存
	 * @author tangding
	 * @date 2016年8月4日 下午2:56:57
	 */
	@SystemControllerLog(description = "新增角色权限单功能点关系")
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "新增角权限单功能点关系", notes = "权限菜单功能点关系对象")
	public MessageResp save(@RequestBody AuthMenuRelation record) {
		AuthMenuRelation authMenuRelation = new AuthMenuRelation();
		BeanUtils.copyBasicProperties(record, authMenuRelation);
		MessageResp messageResp = new MessageResp();
		Boolean flag = false;
		try {
			if (authMenuRelationService.save(authMenuRelation) > 0) {
				flag = true;
			}
		} catch (Exception e) {
			messageResp.setResult(Boolean.FALSE.toString());
			messageResp.setResultDesc("保存失败");
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
	 * @param @param  record
	 * @param @return
	 * @return int
	 * @throws
	 * @Title: updateByParam
	 * @Description: 更新
	 * @author tangding
	 * @date 2016年8月4日 下午2:57:00
	 */
	@SystemControllerLog(description = "修改角色权限单功能点关系")
	@ResponseBody
	@RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
	@ApiOperation(value = "修改", notes = "权限菜单功能点关系对象")
	public MessageResp update(@RequestBody AuthMenuRelation record) {
		MessageResp messageResp = new MessageResp();
		AuthMenuRelation AuthMenuRelation = new AuthMenuRelation();
		BeanUtils.copyBasicProperties(record, AuthMenuRelation);
		Boolean flag = false;
		try {
			if (authMenuRelationService.modifyByUuid(AuthMenuRelation) > 0) {
				flag = true;
			}
		} catch (Exception e) {
			messageResp.setResult(Boolean.FALSE.toString());
			messageResp.setResultDesc("更新异常");
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
	 * @param @param  AuthMenuRelationParam
	 * @param @return
	 * @return List<AuthMenuRelationResp>
	 * @throws
	 * @Title: queryList
	 * @Description: 查询集合
	 * @author tangding
	 * @date 2016年8月4日 下午2:57:03
	 */
	@ResponseBody
	@RequestMapping(value = "/queryList", method = RequestMethod.GET)
	@ApiOperation(value = "权限菜单功能点集合，不带分页", notes = "权限菜单功能点关系对象")
	public MessageResp<List<AuthMenuRelation>> queryList(@ModelAttribute AuthMenuRelation AuthMenuRelationParam) {
		MessageResp<List<AuthMenuRelation>> data = new MessageResp<List<AuthMenuRelation>>();
		List<AuthMenuRelation> AuthMenuRelationResps = authMenuRelationService.queryList(AuthMenuRelationParam);
		data.setData(AuthMenuRelationResps);
		return data;
	}

	/**
	 * @param @param  AuthMenuRelationParam
	 * @param @return
	 * @return AuthMenuRelationResp
	 * @throws
	 * @Title: queryFirst
	 * @Description: 查询第一条数据
	 * @author tangding
	 * @date 2016年8月4日 下午2:57:06
	 */
	@ResponseBody
	@RequestMapping(value = "/queryFirst", method = RequestMethod.GET)
	@ApiOperation(value = "查询第一条数据", notes = "参数为菜单对象")
	public MessageResp<List<AuthMenuRelation>> queryFirst(@ModelAttribute AuthMenuRelation AuthMenuRelationParam) {
		MessageResp<List<AuthMenuRelation>> data = new MessageResp<List<AuthMenuRelation>>();
		AuthMenuRelation AuthMenuRelationResp = authMenuRelationService.queryFirst(AuthMenuRelationParam);

		List<AuthMenuRelation> AuthMenuRelationResps = new ArrayList<AuthMenuRelation>();
		AuthMenuRelationResps.add(AuthMenuRelationResp);
		data.setData(AuthMenuRelationResps);
		return data;
	}

	/**
	 * @param @param  uuid
	 * @param @return
	 * @return AuthMenuRelationResp
	 * @throws
	 * @Title: queryByUuid
	 * @Description: 根据uuid查询
	 * @author tangding
	 * @date 2016年8月4日 下午2:57:08
	 */
	@ResponseBody
	@RequestMapping(value = "{uuid}", method = RequestMethod.GET)
	public MessageResp<List<AuthMenuRelation>> queryByUuid(@PathVariable Long uuid) {
		MessageResp<List<AuthMenuRelation>> data = new MessageResp<List<AuthMenuRelation>>();
		AuthMenuRelation AuthMenuRelationResp = authMenuRelationService.queryByUuid(uuid);
		List<AuthMenuRelation> AuthMenuRelationResps = new ArrayList<AuthMenuRelation>();
		AuthMenuRelationResps.add(AuthMenuRelationResp);
		data.setData(AuthMenuRelationResps);
		return data;
	}

	/**
	 * @param @param  AuthMenuRelationParam
	 * @param @return
	 * @return List<AuthMenuRelationResp>
	 * @throws
	 * @Title: queryPageByParam
	 * @Description: 分页查询
	 * @author tangding
	 * @date 2016年8月4日 下午2:57:13
	 */

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ApiOperation(value = "分页查询权限菜单功能点集合", notes = "权限菜单功能点集合")
	public MessageResp<List<AuthMenuRelation>> queryPage(HttpServletRequest request, @ModelAttribute AuthMenuRelation authMenuRelationParam) {

		MessageResp<List<AuthMenuRelation>> message = new MessageResp<List<AuthMenuRelation>>();
		PageInfo<AuthMenuRelation> pageInfo = null;
		 AuthUserResp authUser = UserAuthUtils.getUser();
		// 此处预留参数（用户类型，数据隔离）
		if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
			message.setData(null);
			message.setResult(Boolean.FALSE.toString());
			message.setResultDesc("没有权限，请联系管理员");
			return message;
		}

		pageInfo = authMenuRelationService.queryPageByParam(authMenuRelationParam);
		message.setPageBean(PageBeanUtil.createPageBean(pageInfo));
		message.setData(pageInfo.getList());
		message.setResult(Boolean.TRUE.toString());
		message.setResultDesc("查询成功");
		return message;
	}

	/**
	 * @param @param  uuid
	 * @param @return
	 * @return MessageResp
	 * @throws
	 * @Title: delete
	 * @Description: 删除
	 * @author tangding
	 * @date 2018年11月25日 下午1:43:58
	 */
	@SystemControllerLog(description = "删除角色权限单功能点关系")
	@ResponseBody
	@RequestMapping(value = "{uuid}", method = RequestMethod.DELETE)
	public MessageResp delete(@PathVariable Long uuid) {
		MessageResp messageResp = new MessageResp();
		/*Boolean flag = false;
		try {
			if (AuthMenuRelationService.delete(Long.valueOf(uuid)) > 0) {
				flag = true;
			}
		} catch (BusinessException e) {
			messageResp.setResult(Boolean.FALSE.toString());
			messageResp.setResultDesc(e.getMsg());
			return messageResp;
		}
		if (flag) {
			messageResp.setResult(flag.toString());
			messageResp.setResultDesc("删除成功");
		} else {
			messageResp.setResult(flag.toString());
			messageResp.setResultDesc("删除失败");

		}*/
		return messageResp;
	}

}