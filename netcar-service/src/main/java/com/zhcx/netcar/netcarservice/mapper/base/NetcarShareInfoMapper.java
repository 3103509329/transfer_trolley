package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarShareInfo;
import com.zhcx.netcar.params.QueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NetcarShareInfoMapper {
    int deleteByPrimaryKey(@Param("companyId") String companyId, @Param("routeId") String routeId, @Param("orderId") String orderId);

    int insert(NetcarShareInfo record);

    int insertSelective(NetcarShareInfo record);

    NetcarShareInfo selectByPrimaryKey(@Param("companyId") String companyId, @Param("routeId") String routeId, @Param("orderId") String orderId);

    int updateByPrimaryKeySelective(NetcarShareInfo record);

    int updateByPrimaryKey(NetcarShareInfo record);

    List<NetcarShareInfo> queryShareInfoListByCondition(QueryParam param);

    void addOrUpdateShareInfo(NetcarShareInfo netcarShareInfo);
}