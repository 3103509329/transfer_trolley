package com.zhcx.auth.facade;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthMenu;

import java.util.List;


public interface AuthMenuService {

	/**
	  * @Title: save
	  * @Description: 保存
	  * @param @param record
	  * @param @return
	  * @return int
	  * @throws
	  */
	int save(AuthMenu record);

	/**
	  * @Title: modify
	  * @Description: 更新
	  * @param @param record
	  * @param @return
	  * @return int
	  */
	int modifyByUuid(AuthMenu record);

	/**
	  * @Title: queryList
	  * @Description: 查询集合
	  * @param @param AuthMenu
	  * @param @return
	  * @return List<AuthMenu>
	  */
	List<AuthMenu> queryList(AuthMenu authMenu);

	/**
	  * @Title: queryListAndOperation
	  * @Description: 查询菜单和功能点
	  * @param @param AuthMenu
	  * @param @return
	  * @return List<AuthMenu>
	  */
	//List<AuthMenu> queryListAndOperation(AuthMenu AuthMenu);

	/**
	  * @Title: queryFirst
	  * @Description: 查询集合中第一条数据
	  * @param @param AuthMenu
	  * @param @return
	  * @return AuthMenu
	  */
	AuthMenu queryFirst(AuthMenu authMenu);

	/**
	  * @Title: queryByUuid
	  * @Description: 根据uuid查询
	  * @param @param uuid
	  * @param @return
	  */
	AuthMenu queryByUuid(Long uuid);

	/**
	  * @Title: queryCountByParam
	  * @Description: 记录条数
	  * @param @param AuthMenu
	  * @param @return
	  * @return int
	  */
	//int queryCountByParam(AuthMenu authMenu);

	/**
	  * @Title: queryPageByParam
	  * @Description: 分页查询
	  * @param @param AuthMenu
	  * @param @return
	  * @return List<AuthMenu>
	  */
//	List<AuthMenu> queryPageByParam(AuthMenu authMenu);
	PageInfo<AuthMenu> queryPageByParam(AuthMenu authMenu);
	/**
	 * 根据权限ID查询菜单集合
	 * 
	 */
	List<AuthMenu> queryMenusByRegisterId(Long[] registerIds);

	int delete(AuthMenu record);
}
