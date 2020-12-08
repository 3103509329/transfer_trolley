package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.app.NetcarNews;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NetcarNewsMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarNews record);

    int insertSelective(NetcarNews record);

    NetcarNews selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarNews record);

    int updateByPrimaryKeyWithBLOBs(NetcarNews record);

    int updateByPrimaryKey(NetcarNews record);

    List<NetcarNews> selectListByType(@Param("type") Integer type, @Param("keyword") String keyword);
}