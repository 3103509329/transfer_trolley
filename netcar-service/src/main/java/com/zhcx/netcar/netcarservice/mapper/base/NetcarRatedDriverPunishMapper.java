package com.zhcx.netcar.netcarservice.mapper.base;



import com.zhcx.netcar.pojo.base.NetcarRatedDriverPunish;
import com.zhcx.netcar.params.RatedDriverParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface NetcarRatedDriverPunishMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarRatedDriverPunish record);

    int insertSelective(NetcarRatedDriverPunish record);

    NetcarRatedDriverPunish selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarRatedDriverPunish record);

    int updateByPrimaryKey(NetcarRatedDriverPunish record);

    List<NetcarRatedDriverPunish> selectListByKeyword(RatedDriverParam param);
}