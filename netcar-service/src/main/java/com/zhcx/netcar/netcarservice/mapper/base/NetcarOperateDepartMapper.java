package com.zhcx.netcar.netcarservice.mapper.base;


import com.zhcx.netcar.pojo.base.NetcarOperateDepart;
import com.zhcx.netcar.params.OperateParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface NetcarOperateDepartMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarOperateDepart record);

    int insertSelective(NetcarOperateDepart record);

    NetcarOperateDepart selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarOperateDepart record);

    int updateByPrimaryKey(NetcarOperateDepart record);

    List<NetcarOperateDepart> queryOperateDepartListByCondition(OperateParam param);

}