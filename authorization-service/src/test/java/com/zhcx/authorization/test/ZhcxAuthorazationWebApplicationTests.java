package com.zhcx.authorization.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhcx.authorization.utils.HttpUtils;
import com.zhcx.netcarbasic.pojo.netcar.druid.DruidNetcarOrder;
import com.zhcx.platformtonet.pojo.base.NetcarStatisticsOrder;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZhcxAuthorazationWebApplicationTests {

    @Test
    public void contextLoads() {
    }


    @Autowired
    private HttpUtils httpUtils;

    @Test
    public void test()  {
        Calendar ca1 = Calendar.getInstance();
        //设置时间为当前时间
        ca1.setTime(new Date());
        //日期减180天
        ca1.add(Calendar.DATE, -30);
        Date lastDate1 = ca1.getTime();
        StringBuilder month = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        month.append("SELECT sum(order_count) as order_count FROM netcar_statistics_operatePay " +
                "WHERE TIME_FORMAT(__time,'yyyy-MM-dd') > '");
        month.append("2019-01-01");
        month.append("'and COMPANY_ID in ('");
        month.append("4403WSJCWQ7F").append("')");
        JSONArray monthList = null;
        try {
            monthList = httpUtils.doPostSqlUrl("sql", String.valueOf(builder));
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<NetcarStatisticsOrder> operation30List = JSONObject.parseArray(monthList.toJSONString(), NetcarStatisticsOrder.class);
        if (operation30List.size() > 0 && operation30List.get(0).getOrder_count() < 100) {
            System.out.println("未达标");
        } else {
            System.out.println("达标");
        }
    }
}
