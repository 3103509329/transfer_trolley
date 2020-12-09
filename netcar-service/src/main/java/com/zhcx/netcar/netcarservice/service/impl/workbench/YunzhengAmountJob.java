//package com.zhcx.netcar.netcarservice.service.impl.workbench;
//
//import com.zhcx.netcar.netcarservice.mapper.yunzheng.YunZhengCompanyMapper;
//import com.zhcx.netcar.netcarservice.mapper.yunzheng.YunZhengVehicleMapper;
//import com.zhcx.netcar.netcarservice.mapper.yunzheng.YunzhengAmountMapper;
//import com.zhcx.netcar.netcarservice.utils.DateStringUtil;
//import com.zhcx.netcar.pojo.yuzheng.YunZhengCompany;
//import com.zhcx.netcar.pojo.yuzheng.YunzhengAmount;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * @author Administrator
// * @email 570815140@qq.com
// * @date 2019/6/19 0019 15:15
// **/
//@EnableScheduling
//@Service
//public class YunzhengAmountJob {
//
//    private static final Logger log = LoggerFactory.getLogger(YunzhengAmountJob.class);
//
//    @Autowired
//    private YunzhengAmountMapper yunzhengAmountMapper;
//
//    @Autowired
//    private YunZhengCompanyMapper yunZhengCompanyMapper;
//
//    @Autowired
//    private YunZhengVehicleMapper yunZhengVehicleMapper;
//    /**
//     * 持久化车辆总数
//     */
//    @Scheduled(cron = "${yuznheng.timed_task.amount}")
//    @Async
//    public void insert() {
//            List<YunZhengCompany> yunzhengList = yunZhengCompanyMapper.selectAll();
//            try{
//                yunzhengList.forEach(yunzheng ->{
//                    Long vehicleAmount = yunZhengVehicleMapper.selectCountByCompanyId(yunzheng.getBusiregnumber());
//                    YunzhengAmount yunzhengAmount = new YunzhengAmount();
//                    yunzhengAmount.setBusiregnumber(yunzheng.getBusiregnumber());
//                    yunzhengAmount.setAmount(Math.toIntExact(vehicleAmount));
//                    yunzhengAmount.setTime(DateStringUtil.dateToStr(new Date()));
//                    yunzhengAmount.setType(1);
//                    yunzhengAmount.setFlage(1);
//                    yunzhengAmountMapper.insertSelective(yunzhengAmount);
//                });
//            }catch (Exception e){
//                log.error("持久化车辆总数定时任务处理异常" + e.getMessage());
//                e.printStackTrace();
//            }
//        }
//}
