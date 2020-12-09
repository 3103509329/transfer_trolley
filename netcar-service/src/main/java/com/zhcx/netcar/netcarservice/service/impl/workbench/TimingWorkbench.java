package com.zhcx.netcar.netcarservice.service.impl.workbench;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-20 15:37
 * 工作台数据获取定时任务
 */
@Component
public class TimingWorkbench implements SimpleJob {
    private static final Logger logger = LoggerFactory.getLogger(TimingWorkbench.class);

    @Autowired
    WorkbenchServiceImpl workbenchServiceImpl;
    @Override
    public void execute(ShardingContext shardingContext) {
        try{
            System.out.println("定时任务--------------------------");
            workbenchServiceImpl.deleteValue();
            workbenchServiceImpl.saveWorkbenchVehicleDate();
            workbenchServiceImpl.saveWorkbenchDriverDate();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("工作台定时任务异常:",e);
        }

    }

}
