package com.zhcx.netcar.netcarservice.service.impl.kafka;

import com.zhcx.netcar.facade.kafkaStream.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/6/9 11:34
 **/
@Service("streamService")
public class StreamServiceImpl implements StreamService {

    @Value("${netcar.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${netcar.kafka.stream.stateDir}")
    private String stateDir;

    @Autowired
    private OrderStreamServiceImpl orderStreamService;

    @Autowired
    private DistanceStreamServiceImpl distanceStreamService;

    @Autowired
    private ComplaintStreamServiceImpl complaintStreamService;

    @Autowired
    private LogInStreamServiceImpl logInStreamService;

    @Autowired
    private LogOutStreamServiceImpl logOutStreamService;

    @Autowired
    private RevocationStreamServiceImpl revocationStreamService;

    @Autowired
    private VehileCassandraStreamServiceImpl vehileCassandraStreamService;

    @Autowired
    private DriverCassandraStreamServiceImpl driverCassandraStreamService;

    @Override
    public int startOrderStream() {
        try {
            orderStreamService.start(bootstrapServers, stateDir);
            KafkaServiceUtil.addShutdownHookAndBlock(orderStreamService);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("启动订单流处理失败");
            return 0;
        }
        System.out.println("启动订单流处理成功");
        return 1;
    }


    @Override
    public int startDistanceStream() {
        try {
            distanceStreamService.start(bootstrapServers, stateDir);
            KafkaServiceUtil.addShutdownHookAndBlock(distanceStreamService);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("启动运距流处理失败");
            return 0;
        }
        System.out.println("启动运距流处理成功");
        return 1;
    }

    @Override
    public int startComplaintStream() {
        try {
            complaintStreamService.start(bootstrapServers, stateDir);
            KafkaServiceUtil.addShutdownHookAndBlock(complaintStreamService);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("启动投诉流处理失败");
            return 0;
        }
        System.out.println("启动投诉流处理成功");
        return 1;
    }

    @Override
    public int startLogInStream() {
        try {
            logInStreamService.start(bootstrapServers, stateDir);
            KafkaServiceUtil.addShutdownHookAndBlock(logInStreamService);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("启动经营上线流处理失败");
            return 0;
        }
        System.out.println("启动经营上线流处理成功");
        return 1;
    }

    @Override
    public int startLogOutStream() {
        try {
            logOutStreamService.start(bootstrapServers, stateDir);
            KafkaServiceUtil.addShutdownHookAndBlock(logOutStreamService);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("启动经营下线流处理失败");
            return 0;
        }
        System.out.println("启动经营下线流处理成功");
        return 1;
    }

    @Override
    public int startRevocationStream() {
        try {
            revocationStreamService.start(bootstrapServers, stateDir);
            KafkaServiceUtil.addShutdownHookAndBlock(revocationStreamService);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("启动订单撤销流处理失败");
            return 0;
        }
        System.out.println("启动订单撤销流处理成功");
        return 1;
    }


    @Override
    public int startVehileCassandraSteam() {
        try {
            vehileCassandraStreamService.start(bootstrapServers, stateDir);
            KafkaServiceUtil.addShutdownHookAndBlock(vehileCassandraStreamService);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("启动车辆定位流处理失败");
            return 0;
        }
        System.out.println("启动车辆定位流处理成功");
        return 1;
    }

    @Override
    public int startDriverCassandraStream() {
        try {
            driverCassandraStreamService.start(bootstrapServers, stateDir);
            KafkaServiceUtil.addShutdownHookAndBlock(driverCassandraStreamService);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("启动驾驶员定位流处理失败");
            return 0;
        }
        System.out.println("启动驾驶员定位流处理成功");
        return 1;
    }
}
