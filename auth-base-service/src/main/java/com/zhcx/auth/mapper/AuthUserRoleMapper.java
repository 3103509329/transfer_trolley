package com.zhcx.auth.mapper;

import com.zhcx.auth.pojo.AuthUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户角色关系数据接口
 * @title 
 * @author tangding
 * @date 2018年11月23日
 * @version 1.0
 */
@Mapper
public interface AuthUserRoleMapper {
    
    int deleteByPrimaryKey(Long uuid);

    
    int insert(AuthUserRole record);

    
    int insertSelective(AuthUserRole record);

    
    AuthUserRole selectByPrimaryKey(Long uuid);

    
    int updateByPrimaryKeySelective(AuthUserRole record);

    
    int updateByPrimaryKey(AuthUserRole record);
    
    /**
	 * 根据条件查询用户角色关系数据集合
	 * @param authUserRole
	 * @return
	 */
	List<AuthUserRole> selectUserRoleList(AuthUserRole authUserRole);
	
	/**
	 * 批量插入角色关系数据
	 * @param authUserRoles
	 */
	void insertBatch(List<AuthUserRole> authUserRoles);
	
	/**
	 * 根据条件删除角色关系数据
	 * @param authUserRole
	 */
	int deleteUserRole(AuthUserRole authUserRole);

	List<AuthUserRole> selectByuser(AuthUserRole authUserRole);

	int insertList(List<AuthUserRole> rolelist);
}