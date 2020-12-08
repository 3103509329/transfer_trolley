package com.zhcx.netcar.netcarservice.mapper.statistical;

import com.zhcx.netcar.pojo.statistical.KafkaNetcarStatisticsHaulDistanceOutput;
import com.zhcx.netcar.pojo.statistical.KafkaNetcarStatisticsOperatePay;
import com.zhcx.netcar.pojo.statistical.KafkaNetcarStatisticsOperatePayKey;

import java.util.List;

/**
* @ClassName: KafkaNetcarStatisticsOperatePayMapper
* @Description: 
* @author: Mybatis Generator
* @date 2020/09/28 17:20:53
*/
public interface KafkaNetcarStatisticsOperatePayMapper {
    int deleteByPrimaryKey(KafkaNetcarStatisticsOperatePayKey key);

    int insert(KafkaNetcarStatisticsOperatePay record);

    int insertSelective(KafkaNetcarStatisticsOperatePay record);

    KafkaNetcarStatisticsOperatePay selectByPrimaryKey(KafkaNetcarStatisticsOperatePayKey key);

    int updateByPrimaryKeySelective(KafkaNetcarStatisticsOperatePay record);

    int updateByPrimaryKey(KafkaNetcarStatisticsOperatePay record);

    List<KafkaNetcarStatisticsOperatePay> selectByDate(KafkaNetcarStatisticsOperatePay record);

}