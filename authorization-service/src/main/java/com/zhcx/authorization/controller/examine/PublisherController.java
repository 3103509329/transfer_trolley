package com.zhcx.authorization.controller.examine;


import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.regionmonitor.examine.PublisherService;
import com.zhcx.regionmonitor.examine.TaxiBaseInfoDriverExamine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/netcar/examine")
public class PublisherController {

    private Logger log = LoggerFactory.getLogger(ExamineDriverController.class);

    @Autowired
    private PublisherService publisherService;

    @PostMapping("/test")
    public MessageResp sendMessage(@RequestBody TaxiBaseInfoDriverExamine taxiBaseInfoDriverExamine) {

        MessageResp messageResp = new MessageResp();
        try {

            for (int data = 0; data < 100; data++){
                taxiBaseInfoDriverExamine.setUuid(Long.valueOf(data));
                int type = publisherService.sendMessage(taxiBaseInfoDriverExamine);
            }

            int type = publisherService.sendMessage(taxiBaseInfoDriverExamine);
            if (type == 1){
                messageResp.setResultDesc("审核申请成功");
            }else {
                messageResp.setResultDesc("审核申请失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("查询异常");
        }
        return messageResp;
    }
}
