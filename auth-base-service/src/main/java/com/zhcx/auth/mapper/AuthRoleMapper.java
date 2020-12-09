package com.zhcx.auth.mapper;

import com.zhcx.auth.pojo.AuthRole;
import com.zhcx.auth.vo.ApplicationBaseInfoVO;
import com.zhcx.auth.vo.ApplicationRoleBaseInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface AuthRoleMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(AuthRole record);

    int insertSelective(AuthRole record);

    AuthRole selectByPrimaryKey(Long uuid);
    
    List<AuthRole> selectAuthRoleByParam(AuthRole recode);
    
    List<AuthRole> selectAuthRoleListByParam(AuthRole recode);

    int updateByPrimaryKeySelective(AuthRole record);

    int updateByPrimaryKey(AuthRole record);

    AuthRole selectByApplication(ApplicationRoleBaseInfoVO role);

    List<ApplicationRoleBaseInfoVO> selectApplicationAndRole(ApplicationBaseInfoVO applicationBaseInfoVO);

    List<AuthRole> selectByUserIdAndCode(@Param("userId") Long userId, @Param("applicationCode") String applicationCode);
}