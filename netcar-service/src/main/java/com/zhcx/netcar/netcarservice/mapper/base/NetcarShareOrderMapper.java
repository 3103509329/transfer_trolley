package com.zhcx.netcar.netcarservice.mapper.base;


import com.zhcx.netcar.pojo.base.NetcarShareOrder;
import com.zhcx.netcar.params.QueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface NetcarShareOrderMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarShareOrder record);

    int insertSelective(NetcarShareOrder record);

    NetcarShareOrder selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarShareOrder record);

    int updateByPrimaryKey(NetcarShareOrder record);

    List<NetcarShareOrder> queryShareOrderListByCondition(QueryParam param);
}