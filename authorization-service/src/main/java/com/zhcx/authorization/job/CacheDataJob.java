package com.zhcx.authorization.job;

import com.alibaba.fastjson.JSONArray;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoCompanyService;
import com.zhcx.basicdata.facade.taxi.TaxiOrderStatisticService;
import com.zhcx.basicdata.pojo.taxi.TaxiStatisticsData;
import com.zhcx.spring.boot.job.annotation.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Job(cron = "0 0 1 * * ?", shardingTotalCount = 1)
public class CacheDataJob implements SimpleJob {

	private static final Logger logger = LoggerFactory.getLogger(CacheDataJob.class);
	
	 @Resource(name="redisTemplate4Json")
	 private RedisTemplate<String, List<TaxiStatisticsData>> redisTemplateTaxiStatisticsData;
	 
     @Autowired
     private TaxiBaseInfoCompanyService companyService;
     
     @Autowired
     private TaxiOrderStatisticService orderService;

	@Override
	public void execute(ShardingContext shardingContext) {
		logger.debug("----首页统计数据定时任务启动----");
		String cityName = Constants.DEAULT_CITY_NAME;
		List<TaxiStatisticsData> resultList = new ArrayList<>();
		try {
        List<TaxiStatisticsData> districtList = companyService.countDistrictBasicData(cityName);
        //按区域统计订单数量
        JSONArray jsonArray = orderService.queryOrderCount("D", null, false);
        Map<String, Object> countMap = new HashMap<>();
        if(jsonArray != null && jsonArray.size() > 0){
            jsonArray.forEach(obj -> {
                Map<String, Object> map = (Map<String, Object>) obj;
//                countMap.put(String.valueOf(map.get("DISTRICT_ID")), Long.parseLong(String.valueOf(map.get("total"))));
                countMap.put(String.valueOf(map.get("DISTRICT_ID")), map.get("total")+","+map.get("sum_fee"));
            });
        }
        if(districtList != null && districtList.size() > 0){
            districtList.forEach(data -> {
                String totalAndSumFee = null;
                if(null != countMap.get(data.getCityId())){
                    totalAndSumFee =countMap.get(data.getCityId()).toString();
                    data.setOrderCount(Long.parseLong(totalAndSumFee.split(",")[0]));
                    data.setFeeAmont(totalAndSumFee.split(",")[1]);
                }
                if(data.getCompanyCount() != null && data.getCompanyCount() > 0){
                    resultList.add(data);
                }
            });
        }
        redisTemplateTaxiStatisticsData.opsForList().leftPush("taxiStatisticsData",  resultList);
        logger.debug("----首页统计数据定时任务结束--缓存数据大小--"+resultList.size());
		}catch (Exception e){
          e.printStackTrace();
          logger.error("定时任务统计首页数据异常");
      }

	}

}
