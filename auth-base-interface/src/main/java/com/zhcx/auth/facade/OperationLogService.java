package com.zhcx.auth.facade;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.OperationLog;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-24 14:08
 * 登陆操作日志
 */
public interface OperationLogService {

    /**
     * 添加日志
     * @param operationLog
     * @throws Exception
     */
    void addOperationLog(OperationLog operationLog) throws Exception;


    /**
     * 日志查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<OperationLog> getOperationLogList(int pageNo, int pageSize, String keyword, String logType,
                                               String startTime, String endTime)throws Exception;
}
