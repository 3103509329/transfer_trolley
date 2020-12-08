package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarShareRoute;
import com.zhcx.netcar.params.QueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface NetcarShareRouteMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarShareRoute record);

    int insertSelective(NetcarShareRoute record);

    NetcarShareRoute selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarShareRoute record);

    int updateByPrimaryKey(NetcarShareRoute record);

    List<NetcarShareRoute> queryShareRouteListByCondition(QueryParam param);

}