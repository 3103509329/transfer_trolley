package com.zhcx.netcar.facade.yunzheng;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/6/19 0019 15:27
 **/
public interface YunzhengAmountService {

    /**
     * 获取指定时间的车辆总数
     * @param company
     * @param finalTime
     * @param type
     * @return
     */
    Long selectByTime(String company, String finalTime, String type);
}
