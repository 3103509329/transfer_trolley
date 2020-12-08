package com.zhcx.netcar.netcarservice.mapper.statistical;

import com.zhcx.netcar.pojo.statistical.KafkaNetcarStatisticsHaulDistanceOutput;

import java.util.List;

/**
* @ClassName: KafkaNetcarStatisticsHaulDistanceOutputMapper
* @Description: 
* @author: Mybatis Generator
* @date 2020/09/28 17:20:53
*/
public interface KafkaNetcarStatisticsHaulDistanceOutputMapper {
    int insert(KafkaNetcarStatisticsHaulDistanceOutput record);

    int insertSelective(KafkaNetcarStatisticsHaulDistanceOutput record);

    List<KafkaNetcarStatisticsHaulDistanceOutput> selectSUMByDate(KafkaNetcarStatisticsHaulDistanceOutput sum);

    List<KafkaNetcarStatisticsHaulDistanceOutput> selectByDateOnDistance(KafkaNetcarStatisticsHaulDistanceOutput distance);

    List<KafkaNetcarStatisticsHaulDistanceOutput> selectMonthDistanceData(KafkaNetcarStatisticsHaulDistanceOutput distance);
}