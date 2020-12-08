package com.zhcx.netcar.netcarservice.service.impl.yunzheng;

import com.zhcx.netcar.facade.yunzheng.YunzhengAmountService;
import com.zhcx.netcar.netcarservice.mapper.yunzheng.YunzhengAmountMapper;
import com.zhcx.netcar.pojo.yuzheng.YunzhengAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/6/19 0019 15:28
 **/
@Service("yunzhengAmountService")
public class YunzhengAmountServiceImpl implements YunzhengAmountService {

    @Autowired
    private YunzhengAmountMapper yunzhengAmountMapper;

    @Override
    public Long selectByTime(String company, String finalTime, String type) {

        List<YunzhengAmount> yunzhengAmounts = yunzhengAmountMapper.selectByTime(company, finalTime, type);
        int amount = yunzhengAmounts.stream().collect(Collectors.summingInt(YunzhengAmount::getAmount));
        if (yunzhengAmounts.size() != 0) {
            amount = amount / yunzhengAmounts.size();
        } else {
            amount = 0;
        }
        return Long.valueOf(amount);
    }
}
