package com.zhcx.auth.mapper;

import com.zhcx.auth.pojo.ApplicationBaseInfo;
import com.zhcx.auth.pojo.ApplicationUserBaseInfo;
import com.zhcx.auth.vo.ApplicationBaseInfoVO;
import com.zhcx.auth.vo.ApplicationUserBaseInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @ClassName: ApplicationUserBaseInfoMapper
* @Description: 
* @author: Mybatis Generator
* @date 2020/04/02 15:15:48
*/
@Mapper
public interface ApplicationUserBaseInfoMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(ApplicationUserBaseInfo record);

    int insertSelective(ApplicationUserBaseInfo record);

    ApplicationUserBaseInfo selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(ApplicationUserBaseInfo record);

    int updateByPrimaryKey(ApplicationUserBaseInfo record);

    List<ApplicationUserBaseInfo> selectByUserId(ApplicationUserBaseInfo applicationUserBaseInfo);

    int insertList(List<ApplicationUserBaseInfo> userList);

    int delete(ApplicationUserBaseInfoVO userDel);

    List<ApplicationBaseInfoVO> selectByPermissions(ApplicationUserBaseInfo applicationUserBaseInfo);
}