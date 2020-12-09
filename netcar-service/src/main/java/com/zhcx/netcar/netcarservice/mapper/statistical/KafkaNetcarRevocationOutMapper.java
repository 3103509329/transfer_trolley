package com.zhcx.netcar.netcarservice.mapper.statistical;

import com.zhcx.netcar.pojo.statistical.KafkaNetcarRevocationOut;

import java.util.List;

/**
* @ClassName: KafkaNetcarRevocationOutMapper
* @Description: 
* @author: Mybatis Generator
* @date 2020/09/28 17:20:53
*/
public interface KafkaNetcarRevocationOutMapper {
    int deleteByPrimaryKey(String orderid);

    int insert(KafkaNetcarRevocationOut record);

    int insertSelective(KafkaNetcarRevocationOut record);

    KafkaNetcarRevocationOut selectByPrimaryKey(String orderid);

    int updateByPrimaryKeySelective(KafkaNetcarRevocationOut record);

    int updateByPrimaryKey(KafkaNetcarRevocationOut record);

    List<KafkaNetcarRevocationOut> getDriverRevocation(KafkaNetcarRevocationOut revocationOut);

}