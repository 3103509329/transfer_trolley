package com.zhcx.authorization.controller.netcar.kafka;

import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.netcarbasic.facade.kafka.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/6/9 11:52
 **/
@RestController
@RequestMapping("/netcar/kafka/stream")
public class KafkaStreamController {

    @Autowired
    private StreamService streamService;

    @GetMapping("/startOrderStream")
    public MessageResp startOrderStream(){
        MessageResp messageResp = new MessageResp();
        int result = streamService.startOrderStream();
        messageResp.setData(result);
        return messageResp;
    }

    @GetMapping("/startDistanceStream")
    public MessageResp startDistanceStream(){
        MessageResp messageResp = new MessageResp();
        int result = streamService.startDistanceStream();
        messageResp.setData(result);
        return messageResp;
    }

    @GetMapping("/startComplaintStream")
    public MessageResp startComplaintStream(){
        MessageResp messageResp = new MessageResp();
        int result = streamService.startComplaintStream();
        messageResp.setData(result);
        return messageResp;
    }
}
