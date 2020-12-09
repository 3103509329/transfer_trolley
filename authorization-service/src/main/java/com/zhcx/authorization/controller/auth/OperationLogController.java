package com.zhcx.authorization.controller.auth;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.netcarbasic.facade.base.OperationLogService;
import com.zhcx.netcarbasic.pojo.base.OperationLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-27 11:30
 * 登陆日志
 */
@RestController
@RequestMapping("/operation/log")
public class OperationLogController {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogController.class);

    @Autowired
    OperationLogService operationLogService;

    @GetMapping("/list")
    public MessageResp getOperationLogList(@RequestParam(value = "pageNo",defaultValue = "1")int pageNo,
                                           @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
                                           @RequestParam(value = "keyword",defaultValue = "",required = false)String keyword,
                                           @RequestParam(value = "logType",defaultValue = "",required = false)String logType,
                                           @RequestParam(value = "startTime",defaultValue = "",required = false)String startTime,
                                           @RequestParam(value = "endTime",defaultValue = "",required = false)String endTime) {
        MessageResp result = new MessageResp();
        try {
            PageInfo<OperationLog> pageInfo = operationLogService.getOperationLogList(pageNo,pageSize,keyword,logType,startTime,endTime);
            result.setData(pageInfo);
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("操作失败");
        }
        return result;
    }
}
