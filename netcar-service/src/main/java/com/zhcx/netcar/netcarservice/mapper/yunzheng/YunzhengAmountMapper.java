package com.zhcx.netcar.netcarservice.mapper.yunzheng;

import com.zhcx.netcar.pojo.yuzheng.YunzhengAmount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface YunzhengAmountMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(YunzhengAmount record);

    int insertSelective(YunzhengAmount record);

    YunzhengAmount selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(YunzhengAmount record);

    int updateByPrimaryKey(YunzhengAmount record);


    List<YunzhengAmount> selectByTime(@Param("company") String company, @Param("finalTime") String finalTime, @Param("type") String type);
}