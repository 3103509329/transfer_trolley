package com.car.carservice.dubbo;

import com.car.carservice.params.Demo;
import com.car.carservice.pojo.BaseDictionary;
import com.github.pagehelper.PageInfo;

public interface DemoDubboService {
    PageInfo<BaseDictionary> select(BaseDictionary str);
}
