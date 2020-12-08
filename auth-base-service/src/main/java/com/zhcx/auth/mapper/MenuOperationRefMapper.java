package com.zhcx.auth.mapper;

import com.zhcx.auth.pojo.MenuOperationRef;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuOperationRefMapper {
    int deleteByPrimaryKey(String uuid);

    int insert(MenuOperationRef record);

    int insertSelective(MenuOperationRef record);

    MenuOperationRef selectByPrimaryKey(String uuid);

    int updateByPrimaryKeySelective(MenuOperationRef record);

    int updateByPrimaryKey(MenuOperationRef record);
}