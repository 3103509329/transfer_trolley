package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarOperateInfo;
import com.zhcx.netcar.params.OperateParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NetcarOperateInfoMapper {
    int deleteByPrimaryKey(@Param("companyId") String companyId, @Param("orderId") String orderId);

    int insert(NetcarOperateInfo record);

    int insertSelective(NetcarOperateInfo record);

    NetcarOperateInfo selectByPrimaryKey(@Param("companyId") String companyId, @Param("orderId") String orderId);

    int updateByPrimaryKeySelective(NetcarOperateInfo record);

    int updateByPrimaryKey(NetcarOperateInfo record);

    List<NetcarOperateInfo> queryOperateInfoListByCondition(OperateParam param);

    void addOrUpdateOperationInfo(NetcarOperateInfo netcarOperateInfo);
}