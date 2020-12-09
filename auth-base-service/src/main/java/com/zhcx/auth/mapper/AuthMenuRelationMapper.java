package com.zhcx.auth.mapper;

import com.zhcx.auth.pojo.AuthMenuRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthMenuRelationMapper {
	int insert(AuthMenuRelation record);

	int update(AuthMenuRelation record);
	
	int delete(AuthMenuRelation record);

	List<AuthMenuRelation> selectList(AuthMenuRelation param);
	
	int selectCountByParam(AuthMenuRelation param);

	List<AuthMenuRelation> selectPageByParam(AuthMenuRelation param);
}