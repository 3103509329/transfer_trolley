package com.zhcx.auth.facade;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthMenuRelation;

import java.util.List;


public interface AuthMenuRelationService {
	/**
	  * @Title: save
	  * @Description: 保存
	  * @param @param record
	  * @param @return
	  * @return int
	  * @throws
	  * @author tangding
	  * @date 2018年11月23日 下午2:56:57
	  */
	int save(AuthMenuRelation record);

	/**
	  * @Title: modify
	  * @Description: 更新
	  * @param @param record
	  * @param @return
	  * @return int
	  * @throws
	  * @author tangding
	  * @date 2018年11月23日 下午2:57:00
	  */
	int modifyByUuid(AuthMenuRelation record);

	/**
	  * @Title: queryList
	  * @Description: 查询集合
	  * @param @param AuthMenuRelation
	  * @param @return
	  * @return List<AuthMenuRelation>
	  * @throws
	  * @author tangding
	  * @date 2018年11月23日 下午2:57:03
	  */
	List<AuthMenuRelation> queryList(AuthMenuRelation authMenuRelation);

	/**
	  * @Title: queryFirst
	  * @Description: 查询第一条数据
	  * @param @param AuthMenuRelation
	  * @param @return
	  * @return AuthMenuRelation
	  * @throws
	  * @author tangding
	  * @date 2018年11月23日 下午2:57:06
	  */
	AuthMenuRelation queryFirst(AuthMenuRelation authMenuRelation);

	/**
	  * @Title: queryByUuid
	  * @Description: 根据uuid查询
	  * @param @param uuid
	  * @param @return
	  * @return AuthMenuRelation
	  * @throws
	  * @author tangding
	  * @date 2018年11月23日 下午2:57:08
	  */
	AuthMenuRelation queryByUuid(Long uuid);

	/**
	  * @Title: queryCountByParam
	  * @Description: 记录条数
	  * @param @param AuthMenuRelation
	  * @param @return
	  * @return int
	  * @throws
	  * @author tangding
	  * @date 2018年11月23日 下午2:57:11
	  */
	//int queryCountByParam(AuthMenuRelation authMenuRelation);

	/**
	  * @Title: queryPageByParam
	  * @Description: 分页查询
	  * @param @param AuthMenuRelation
	  * @param @return
	  * @return List<AuthMenuRelation>
	  * @throws
	  * @author tangding
	  * @date 2018年11月23日 下午2:57:13
	  */
	PageInfo<AuthMenuRelation> queryPageByParam(AuthMenuRelation authMenuRelation);

}
