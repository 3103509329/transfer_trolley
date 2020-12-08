package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.app.NetcarAppTrafficAssessment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NetcarAppTrafficAssessmentMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarAppTrafficAssessment record);

    int insertSelective(NetcarAppTrafficAssessment record);

    NetcarAppTrafficAssessment selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarAppTrafficAssessment record);

    int updateByPrimaryKey(NetcarAppTrafficAssessment record);

    List<NetcarAppTrafficAssessment> selectListByKeyword(@Param("keyword") String keyword, @Param("branum") String branum);
}