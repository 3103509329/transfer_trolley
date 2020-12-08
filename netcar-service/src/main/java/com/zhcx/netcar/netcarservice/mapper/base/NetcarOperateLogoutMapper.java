package com.zhcx.netcar.netcarservice.mapper.base;


import com.zhcx.netcar.pojo.base.NetcarOperateLogout;
import com.zhcx.netcar.params.OperateParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface NetcarOperateLogoutMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarOperateLogout record);

    int insertSelective(NetcarOperateLogout record);

    NetcarOperateLogout selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarOperateLogout record);

    int updateByPrimaryKey(NetcarOperateLogout record);

    List<NetcarOperateLogout> queryOperateLogoutListByCondition(OperateParam param);
}