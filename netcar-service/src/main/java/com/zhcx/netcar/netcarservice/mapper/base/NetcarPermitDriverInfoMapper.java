package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.app.NetcarPermitDriverInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NetcarPermitDriverInfoMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarPermitDriverInfo record);

    int insertSelective(NetcarPermitDriverInfo record);

    NetcarPermitDriverInfo selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarPermitDriverInfo record);

    int updateByPrimaryKey(NetcarPermitDriverInfo record);

    List<NetcarPermitDriverInfo> selectPermitDriverInfoList(@Param("corpId") Long corpId, @Param("keyword") String keyword, @Param("flag") Integer flag);
}