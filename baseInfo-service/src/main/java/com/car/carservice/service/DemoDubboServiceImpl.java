package com.car.carservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.car.carservice.dubbo.DemoDubboService;
import com.car.carservice.mapper.BaseDictionaryMapper;
import com.car.carservice.pojo.BaseDictionary;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Service(version = "${car.business.dubbo.version}", interfaceClass = DemoDubboService.class)
public class DemoDubboServiceImpl implements DemoDubboService {

    @Autowired
    private BaseDictionaryMapper baseDictionaryMapper;


    @Autowired
    private RedisTemplate<String,  String> redisTemplate;

    @Override
    public PageInfo<BaseDictionary> select(BaseDictionary str) {
        PageInfo<BaseDictionary> pageInfo;
        PageHelper.startPage(str.getPageNo(), str.getPageSize());
        List<BaseDictionary> demoList = baseDictionaryMapper.selectByAll(str);
        pageInfo = new PageInfo<>(demoList);
        return pageInfo;
    }
}
