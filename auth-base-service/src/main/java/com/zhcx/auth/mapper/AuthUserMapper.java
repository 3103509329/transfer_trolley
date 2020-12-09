package com.zhcx.auth.mapper;

import com.zhcx.auth.pojo.AuthUser;
import com.zhcx.auth.pojo.AuthUserResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthUserMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(AuthUser record);
    
    AuthUser selectByPrimaryKey(Long uuid);

    List<AuthUser> selectAll();
    
    List<AuthUserResp> selectAuthUser(AuthUser record);

    int updateByPrimaryKey(AuthUser record);
    
    int updateUserById(AuthUser record);
    
    AuthUser selectAuthUserById(Long uuid);
    
    AuthUserResp selectDataByAccount(@Param(value = "account") String account);

    int verificationMobilePhone(@Param(value = "mobilePhone") String mobilePhone);

    AuthUserResp verificationUserName(@Param(value = "userName") String loginName);
    
}