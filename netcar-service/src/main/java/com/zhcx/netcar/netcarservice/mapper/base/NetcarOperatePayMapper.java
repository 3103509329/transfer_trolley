package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarOperatePay;
import com.zhcx.netcar.params.OperateParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface NetcarOperatePayMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarOperatePay record);

    int insertSelective(NetcarOperatePay record);

    NetcarOperatePay selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarOperatePay record);

    int updateByPrimaryKey(NetcarOperatePay record);

    List<NetcarOperatePay> queryOperatePayListByCondition(OperateParam param);

}