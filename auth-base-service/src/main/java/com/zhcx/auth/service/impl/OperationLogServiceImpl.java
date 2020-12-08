package com.zhcx.auth.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.facade.OperationLogService;
import com.zhcx.auth.mapper.OperationLogMapper;
import com.zhcx.auth.pojo.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-24 14:09
 * 登陆操作日志
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    OperationLogMapper operationLogMapper;

    /**
     * 添加日志
     * @param operationLog
     * @throws Exception
     */
    @Override
    public void addOperationLog(OperationLog operationLog) throws Exception {
        operationLogMapper.addOperationLog(operationLog);
    }

    /**
     * 日志查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<OperationLog> getOperationLogList(int pageNo, int pageSize,String keyword,String logType,
                                                      String startTime,String endTime) throws Exception{
        PageHelper.startPage(pageNo,pageSize);
        if(null != keyword && !"".equals(keyword)){
            keyword = "%"+keyword+"%";
        }
        List<OperationLog> list = operationLogMapper.getOperationLogList(keyword,logType,startTime,endTime);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }
}
