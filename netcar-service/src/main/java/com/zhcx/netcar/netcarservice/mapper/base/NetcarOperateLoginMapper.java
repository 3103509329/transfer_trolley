package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarOperateLogin;
import com.zhcx.netcar.params.OperateParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface NetcarOperateLoginMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarOperateLogin record);

    int insertSelective(NetcarOperateLogin record);

    NetcarOperateLogin selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarOperateLogin record);

    int updateByPrimaryKey(NetcarOperateLogin record);

    List<NetcarOperateLogin> queryOperateLoginListByCondition(OperateParam param);
}