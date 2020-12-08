package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarOrderCreate;
import com.zhcx.netcar.params.OrderParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NetcarOrderCreateMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarOrderCreate record);

    int insertSelective(NetcarOrderCreate record);

    NetcarOrderCreate selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarOrderCreate record);

    int updateByPrimaryKey(NetcarOrderCreate record);

    List<NetcarOrderCreate> queryOrderCreateListByCondition(OrderParam param);

    List<NetcarOrderCreate> queryOrderPositionList(@Param("startTime") String startTime, @Param("endTime") String endTime);
}