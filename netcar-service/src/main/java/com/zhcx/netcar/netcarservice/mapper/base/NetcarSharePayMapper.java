package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarSharePay;
import com.zhcx.netcar.params.QueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface NetcarSharePayMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarSharePay record);

    int insertSelective(NetcarSharePay record);

    NetcarSharePay selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarSharePay record);

    int updateByPrimaryKey(NetcarSharePay record);

    List<NetcarSharePay> querySharePayListByCondition(QueryParam param);
}