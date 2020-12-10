package com.car.carservice.mapper;

import com.car.carservice.params.Demo;
import com.car.carservice.pojo.BaseDictionary;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @ClassName: BaseDictionaryMapper
* @Description: 
* @author: Mybatis Generator
* @date 2020/12/10 17:13:11
*/
@Mapper
public interface BaseDictionaryMapper {
    int deleteByPrimaryKey(Integer uuid);

    int insert(BaseDictionary record);

    int insertSelective(BaseDictionary record);

    BaseDictionary selectByPrimaryKey(Integer uuid);

    int updateByPrimaryKeySelective(BaseDictionary record);

    int updateByPrimaryKey(BaseDictionary record);

    List<BaseDictionary> selectByAll(BaseDictionary str);
}