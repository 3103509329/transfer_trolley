package com.zhcx.netcar.netcarservice.mapper.statistical;

import com.zhcx.netcar.pojo.statistical.KafkaResultNetcarOnOffLine;

/**
* @ClassName: KafkaResultNetcarOnOffLineMapper
* @Description: 
* @author: Mybatis Generator
* @date 2020/09/28 17:20:53
*/
public interface KafkaResultNetcarOnOffLineMapper {
    int deleteByPrimaryKey(String id);

    int insert(KafkaResultNetcarOnOffLine record);

    int insertSelective(KafkaResultNetcarOnOffLine record);

    KafkaResultNetcarOnOffLine selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(KafkaResultNetcarOnOffLine record);

    int updateByPrimaryKey(KafkaResultNetcarOnOffLine record);
}