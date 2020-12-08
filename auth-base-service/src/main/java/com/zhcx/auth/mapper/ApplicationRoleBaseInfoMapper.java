package com.zhcx.auth.mapper;

import com.zhcx.auth.pojo.ApplicationRoleBaseInfo;
import com.zhcx.auth.vo.ApplicationBaseInfoVO;
import com.zhcx.auth.vo.ApplicationRoleBaseInfoVO;
import com.zhcx.auth.vo.ApplicationUserBaseInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @ClassName: ApplicationRoleBaseInfoMapper
* @Description: 
* @author: Mybatis Generator
* @date 2020/04/02 15:15:48
*/
@Mapper
public interface ApplicationRoleBaseInfoMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(ApplicationRoleBaseInfo record);

    int insertSelective(ApplicationRoleBaseInfo record);

    ApplicationRoleBaseInfo selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(ApplicationRoleBaseInfo record);

    int updateByPrimaryKey(ApplicationRoleBaseInfo record);

    List<ApplicationRoleBaseInfo> select(ApplicationRoleBaseInfoVO applicationRoleBaseInfoVO);

    int delete(ApplicationRoleBaseInfoVO applicationRoleBaseInfoVO);

    List<ApplicationRoleBaseInfo> selectBycode(ApplicationRoleBaseInfoVO role);

    int insertList(List<ApplicationRoleBaseInfo> list);

    List<ApplicationRoleBaseInfoVO> selectByUser(ApplicationBaseInfoVO applicationBaseInfoVO);

    List<ApplicationBaseInfoVO> selectUserList(ApplicationUserBaseInfoVO applicationBaseInfoVO);
}