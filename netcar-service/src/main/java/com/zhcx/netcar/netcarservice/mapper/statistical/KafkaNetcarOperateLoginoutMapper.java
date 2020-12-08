package com.zhcx.netcar.netcarservice.mapper.statistical;

import com.zhcx.netcar.pojo.statistical.KafkaNetcarOperateLoginout;
import com.zhcx.netcar.pojo.statistical.KafkaNetcarOperateLoginoutKey;

import java.util.List;

/**
* @ClassName: KafkaNetcarOperateLoginoutMapper
* @Description: 
* @author: Mybatis Generator
* @date 2020/09/28 17:20:53
*/
public interface KafkaNetcarOperateLoginoutMapper {
    int deleteByPrimaryKey(KafkaNetcarOperateLoginoutKey key);

    int insert(KafkaNetcarOperateLoginout record);

    int insertSelective(KafkaNetcarOperateLoginout record);

    KafkaNetcarOperateLoginout selectByPrimaryKey(KafkaNetcarOperateLoginoutKey key);

    int updateByPrimaryKeySelective(KafkaNetcarOperateLoginout record);

    int updateByPrimaryKey(KafkaNetcarOperateLoginout record);

    List<KafkaNetcarOperateLoginout> selectByLoInOut(KafkaNetcarOperateLoginout loinout);
}