package com.zhcx.netcar.netcarservice.service.impl.kafka;

import com.zhcx.netcar.facade.kafkaStream.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/6/9 15:22
 **/
@Component
public class ApplicationStartupStreamRunner implements CommandLineRunner {

    @Autowired
    private StreamService streamService;

    @Override
    public void run(String... strings) {
        streamService.startOrderStream();
        streamService.startDistanceStream();
        streamService.startComplaintStream();
        streamService.startLogInStream();
        streamService.startLogOutStream();
        streamService.startRevocationStream();
        streamService.startVehileCassandraSteam();
        streamService.startDriverCassandraStream();
    }

}
