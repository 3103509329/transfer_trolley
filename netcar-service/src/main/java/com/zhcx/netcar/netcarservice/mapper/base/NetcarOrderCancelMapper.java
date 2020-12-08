package com.zhcx.netcar.netcarservice.mapper.base;


import com.zhcx.netcar.pojo.base.NetcarOrderCancel;
import com.zhcx.netcar.params.OrderParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface NetcarOrderCancelMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarOrderCancel record);

    int insertSelective(NetcarOrderCancel record);

    NetcarOrderCancel selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarOrderCancel record);

    int updateByPrimaryKey(NetcarOrderCancel record);

    List<NetcarOrderCancel> queryOrderCancelListByCondition(OrderParam param);
}