package com.zhcx.auth.controller.auth;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.facade.OperationLogService;
import com.zhcx.auth.pojo.OperationLog;
import com.zhcx.auth.utils.MessageResp;
import com.zhcx.auth.utils.PageBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            result.setData(pageInfo.getList());
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
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
