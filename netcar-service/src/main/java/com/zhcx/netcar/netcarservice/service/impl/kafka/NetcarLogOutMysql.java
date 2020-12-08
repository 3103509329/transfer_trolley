package com.zhcx.netcar.netcarservice.service.impl.kafka;

import com.alibaba.fastjson.JSON;
import com.zhcx.netcar.facade.base.NetcarOnOffLineService;
import com.zhcx.netcar.pojo.base.NetcarOperateLogin;
import com.zhcx.netcar.pojo.base.NetcarOperateLogout;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 在离线状态维护
 */
@Component
public class NetcarLogOutMysql {

    private Logger log = LoggerFactory.getLogger(NetcarLogOutMysql.class);

    @Autowired
    private NetcarOnOffLineService netcarOnOffLineService;

    /**
     * @param content
     */
    @KafkaListener(topics = "${netcar.kafka.mysql.logout}")
    public void addComplaintFromIot(String content) {
        try {
            if (StringUtils.isBlank(content)) {
                return;
            }
            NetcarOperateLogout netcarOperateInfo = JSON.parseObject(content, NetcarOperateLogout.class);
            if (netcarOperateInfo != null) {
                netcarOnOffLineService.deleteLogOut(netcarOperateInfo);
                netcarOnOffLineService.insertLogOut(netcarOperateInfo);
            }
        } catch (Exception e) {
            log.error("在线数据入库异常，Error：" + e.getMessage());
        }
    }

}
