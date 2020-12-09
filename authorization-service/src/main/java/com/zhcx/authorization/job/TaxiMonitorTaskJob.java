//package com.zhcx.authorization.job;
//
//import com.dangdang.ddframe.job.api.ShardingContext;
//import com.dangdang.ddframe.job.api.simple.SimpleJob;
//import com.zhcx.authorization.utils.Constants;
//import com.zhcx.basicdata.facade.taxi.TaxiMonitorTaskService;
//import com.zhcx.basicdata.pojo.taxi.TaxiMonitorTask;
//import com.zhcx.common.util.UUIDUtils;
//import com.zhcx.spring.boot.job.annotation.Job;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.annotation.Resource;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
///**
// * @description:
// * @author: qiuziqiang
// * @date 2019-05-24 10:05
// **/
//@Job(cron = "0 0/1 * * * ? ",shardingTotalCount = 1)
//public class TaxiMonitorTaskJob implements SimpleJob {
//
//    private static final Logger logger = LoggerFactory.getLogger(TaxiMonitorTaskJob.class);
//
//    @Autowired
//    private TaxiMonitorTaskService taxiMonitorTaskService;
//
//    @Resource
//    private UUIDUtils uuidUtils;
//
//    @Override
//    public void execute(ShardingContext shardingContext) {
//        logger.info("----开始扫描视频监控定时任务----");
//        try {
//            TaxiMonitorTask task = new TaxiMonitorTask();
//            //未开始的任务状态01
//            task.setStatus(Constants.MONITOR_TASK_NOT);
//            long now = System.currentTimeMillis();//当前系统时间
//            List<TaxiMonitorTask> tasks = taxiMonitorTaskService.queryTask(task);
//            if (tasks != null && tasks.size() > 0) {
//                TaxiMonitorTask monitorTask = tasks.get(0);
//                if (monitorTask != null) {
//                    long startTime = monitorTask.getStartTime().getTime();
//                    if (startTime <= now) {
//                        //修改任务状态
//                        monitorTask.setStatus(Constants.MONITOR_TASK_ING);
//                        int res = taxiMonitorTaskService.updateRecord(monitorTask);
//                        if (res > 0) {
//                            logger.info(" 修改任务:" + monitorTask.getUuid() + "  状态为进行中");
//                        } else {
//                            logger.error(" 修改任务:" + monitorTask.getUuid() + "  状态失败");
//                        }
//                    }
//                }
//            }
//            //进行中的任务
//            task.setStatus(Constants.MONITOR_TASK_ING);
//            List<TaxiMonitorTask> tasks2 = taxiMonitorTaskService.queryTask(task);
//            if (tasks2 != null && tasks2.size() > 0) {
//                TaxiMonitorTask monitorTask2 = tasks2.get(0);
//                if (monitorTask2 != null) {
//                    long endTime = monitorTask2.getEndTime().getTime();
//                    if (endTime <= now) {
//                        monitorTask2.setStatus(Constants.MONITOR_TASK_OVER);
//                        int res = taxiMonitorTaskService.updateRecord(monitorTask2);
//                        if (res > 0) {
//                            logger.info(" 修改任务:" + monitorTask2.getUuid() + "  状态为已结束");
//                            if (monitorTask2.getWhetherSelf() == 1) {
//                                //开启自动创建任务
//                                TaxiMonitorTask autoTask = new TaxiMonitorTask();
//                                autoTask.setUuid(uuidUtils.getLongUUID());//uuid
//                                autoTask.setMonitorDuration(monitorTask2.getMonitorDuration());
//                                Date startTime = monitorTask2.getEndTime();
//                                //任务轮询时长
//                                Float duration = monitorTask2.getDuration();
//                                long b = (long) (duration * 1000 * 60 * 60); //轮询时长毫秒值
//                                long a = startTime.getTime(); //任务启动毫秒值
//                                autoTask.setStartTime(startTime);
//                                autoTask.setEndTime(new Date(a+b));
//                                autoTask.setDuration(duration);
//                                autoTask.setStatus(Constants.MONITOR_TASK_NOT);
//                                autoTask.setWhetherSelf(1);
//                                autoTask.setCreateBy(monitorTask2.getCreateBy());
//                                int p = taxiMonitorTaskService.addRecord(autoTask);
//                                if (p > 0) {
//                                    logger.debug(" 自动创建任务:" + autoTask.getUuid() + "  成功 !");
//                                } else {
//                                    logger.error(" 自动创建任务:" + autoTask.getUuid() + "  失败 !");
//                                }
//                            }
//                        }
//
//                    }
//
//                }
//            }
//
//
//        } catch (Exception e) {
//            logger.error("扫描视频监控任务异常: "+e.getMessage());
//        }
//
//    }
//
//
//    //获取明天(24小时后)的现在时间
//    public static Date getNextDay(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(Calendar.DAY_OF_MONTH, +1);//+1今天的时间加一天
//        date = calendar.getTime();
//        return date;
//    }
//}
