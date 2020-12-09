package com.zhcx.auth.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.dubbo.AuthUserDubboService;
import com.zhcx.auth.dubbo.OperationLogDubboService;
import com.zhcx.auth.facade.OperationLogService;
import com.zhcx.auth.mapper.OperationLogMapper;
import com.zhcx.auth.pojo.OperationLog;
import com.zhcx.auth.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-24 14:09
 * 登陆操作日志
 */
@Service(version = "${zhcx.business.dubbo.version}",interfaceClass = OperationLogDubboService.class)
public class OperationLogDubboServiceImpl implements OperationLogDubboService {

    @Autowired
    OperationLogMapper operationLogMapper;

    @Autowired
    private UUIDUtils uuidUtils;
    /**
     * 添加日志
     * @param operationLog
     * @throws Exception
     */
    @Override
    public void addOperationLog(OperationLog operationLog) throws Exception {
        operationLog.setUuid(uuidUtils.getLongUUID("operation_log"));
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
