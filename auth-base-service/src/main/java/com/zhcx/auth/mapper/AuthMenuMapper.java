package com.zhcx.auth.mapper;

import com.zhcx.auth.pojo.AuthMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthMenuMapper {

	int save(AuthMenu record);

	int update(AuthMenu record);
	
	int delete(AuthMenu record);

	List<AuthMenu> selectList(AuthMenu param);
	
	/*int selectCountByParam(AuthMenu param);*/

	List<AuthMenu> selectPageByParam(AuthMenu param);
	
	//List<AuthMenu> selectListAndOperation(AuthMenu param);
	
}