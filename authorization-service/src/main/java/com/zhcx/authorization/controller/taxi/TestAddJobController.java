//package com.zhcx.authorization.controller.taxi;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.dangdang.ddframe.job.lite.api.JobScheduler;
//import com.zhcx.authorization.job.TestManualJob;
//import com.zhcx.spring.boot.job.JobSchedulerFactory;
//
//import io.swagger.annotations.Api;
//
///**
// *
// * @author tangding
// *
// */
//@RequestMapping("/demo/test")
//@RestController
//@Api(value = "addJob", tags = "添加定时任务")
//public class TestAddJobController {
//	private static final Logger logger = LoggerFactory.getLogger(TestAddJobController.class);
//
//	private static final String CRON_DATE_FORMAT = "ss mm HH dd MM ? yyyy";
//
//	@Autowired
//	private JobSchedulerFactory jobSchedulerFactory;
//	@Autowired
//	private TestManualJob testManualJob;
//	private JobScheduler jobScheduler;
//
//    @GetMapping("/job")
//    public void addJob(HttpServletRequest request) {
//    	logger.debug("-------------addJob---start-----");
//    	long now = System.currentTimeMillis();
//    	jobScheduler = jobSchedulerFactory.create("testManualJob1", testManualJob, getCron(new Date(now+60000)));
//    	logger.debug("-------------addJob---end-----");
//    }
//
//    public static void main(String[] args) {
//    	 long now = System.currentTimeMillis();
//    	 String cron = getCron(new Date(now+300000));
//	}
//
//    /***
//     * @param date 时间
//     * @return cron类型的日期
//     */
//    public static String getCron(final Date date) {
//        SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);
//        String formatTimeStr = "";
//        if (date != null) {
//            formatTimeStr = sdf.format(date);
//        }
//        return formatTimeStr;
//    }
//
//}
