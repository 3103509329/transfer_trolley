package com.zhcx.auth.mapper;

import com.zhcx.auth.pojo.ApplicationBaseInfo;
import com.zhcx.auth.pojo.ApplicationUserBaseInfo;
import com.zhcx.auth.pojo.AuthMenuRelation;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.auth.vo.ApplicationBaseInfoVO;
import com.zhcx.auth.vo.ApplicationUserBaseInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @ClassName: ApplicationBaseInfoMapper
* @Description: 
* @author: Mybatis Generator
* @date 2020/04/02 15:15:48
*/
@Mapper
public interface ApplicationBaseInfoMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(ApplicationBaseInfo record);

    int insertSelective(ApplicationBaseInfo record);

    ApplicationBaseInfo selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(ApplicationBaseInfo record);

    int updateByPrimaryKey(ApplicationBaseInfo record);

    List<ApplicationBaseInfo> select(ApplicationBaseInfoVO applicationBaseInfo);

    List<ApplicationBaseInfoVO> selectByRole(ApplicationUserBaseInfo applicationUserBaseInfo);

    List<ApplicationBaseInfoVO> selectByUser(ApplicationBaseInfoVO applicationBaseInfoVO);

    List<AuthMenuRelation> jurisdiction(AuthUserResp user);

    List<ApplicationBaseInfoVO> selectUserList(ApplicationUserBaseInfoVO applicationBaseInfoVO);
}