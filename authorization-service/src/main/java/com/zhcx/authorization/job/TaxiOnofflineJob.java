//package com.zhcx.authorization.job;
//
//import com.dangdang.ddframe.job.api.ShardingContext;
//import com.dangdang.ddframe.job.api.simple.SimpleJob;
//import com.zhcx.authorization.utils.Constants;
//import com.zhcx.authorization.utils.DateUtil;
//import com.zhcx.basicdata.facade.taxi.KafkaResultTaxiOnofflineService;
//import com.zhcx.basicdata.pojo.taxi.KafkaResultTaxiOnoffline;
//import com.zhcx.basicdata.pojo.taxi.TaxiOnoffline;
//import com.zhcx.common.util.UUIDUtils;
//import com.zhcx.spring.boot.job.annotation.Job;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.annotation.Resource;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * @description: 定时任务将kafka_result_taxi_onoffline 表持久化到 taxi_onoffline
// * @author: qzq
// * @date 2019-05-24 10:05
// **/
//@Job(cron = "30 10 22 * * ? ", shardingTotalCount = 1)
//public class TaxiOnofflineJob implements SimpleJob {
//
//    private static final Logger logger = LoggerFactory.getLogger(TaxiOnofflineJob.class);
//
//    @Resource
//    private KafkaResultTaxiOnofflineService kafkaResultTaxiOnofflineService;
//
//    @Resource
//    private UUIDUtils uuidUtils;
//
//    @Override
//    public void execute(ShardingContext shardingContext) {
//        logger.info("----TaxiOnofflineJob开始----");
//        try {
//            //获取当前日的凌晨和24点时间
//            Date time = new Date();
//            long startDate = DateUtil.getTimesMorning(time).getTime();
//            long endDate = DateUtil.getTimesnight(time).getTime();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            String format = sdf.format(new Date());
//            List<KafkaResultTaxiOnoffline> list1 = kafkaResultTaxiOnofflineService.queryTodayData();
//            List<TaxiOnoffline> list2 = new ArrayList<>();
//            TaxiOnoffline taxiOnoffline;
//            for (KafkaResultTaxiOnoffline onoffline : list1) {
//
//                taxiOnoffline = new TaxiOnoffline();
//                taxiOnoffline.setId(uuidUtils.getLongUUID());
//                taxiOnoffline.setCarId(onoffline.getId());
//                long date = onoffline.getEventTime().getTime();
//                if (startDate <= date && date < endDate) {
//                    taxiOnoffline.setActive(Constants.CAR_ONLINE);
//                } else {
//                    taxiOnoffline.setActive(Constants.CAR_OFFLINE);
//                }
//                taxiOnoffline.setEventTime(onoffline.getEventTime());
//                taxiOnoffline.setCreateTime(format);
//                taxiOnoffline.setCorpId(onoffline.getCorpId());
//                list2.add(taxiOnoffline);
//            }
//            kafkaResultTaxiOnofflineService.saveToTaxiOnoffline(list2);
//        } catch (Exception e) {
//            logger.error("TaxiOnofflineJob异常: " + e.getMessage());
//        }
//        logger.info("----TaxiOnofflineJob完成----");
//    }
//
//}
