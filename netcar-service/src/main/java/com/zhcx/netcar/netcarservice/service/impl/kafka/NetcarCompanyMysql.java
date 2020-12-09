package com.zhcx.netcar.netcarservice.service.impl.kafka;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.zhcx.basic.dubbo.BaseInfoCompanyDubboService;
import com.zhcx.basic.model.BaseInfoCompany;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompany;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class NetcarCompanyMysql {

    private Logger log = LoggerFactory.getLogger(NetcarCompanyMysql.class);

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoCompanyDubboService baseInfoCompanyService;

    /**
     * @param content
     */
    @KafkaListener(topics = "${netcar.kafka.mysql.company}")
    public void addComplaintFromIot(String content) {
        try {
            if (StringUtils.isBlank(content)) {
                return;
            }
            NetcarBaseInfoCompany netcarBaseInfoCompany = JSON.parseObject(content, NetcarBaseInfoCompany.class);
            BaseInfoCompany netcarCompany = baseInfoCompanyService.selelctByidentifier(netcarBaseInfoCompany.getIdentifier());
            if (netcarCompany == null) {
                baseInfoCompanyService.insert(data(netcarBaseInfoCompany));
            } else if (netcarCompany.getSource() != null || netcarCompany.getSource() == 4) {
                netcarCompany.setCompanyId(netcarBaseInfoCompany.getCompanyId());
                baseInfoCompanyService.update(netcarCompany);
            }
        } catch (
                Exception e) {
            log.error("下发企业数据入库异常，Error：" + e.getMessage());
        }
    }


    private BaseInfoCompany data(NetcarBaseInfoCompany netcarBaseInfoCompany) {
        BaseInfoCompany baseInfoCompany = new BaseInfoCompany();
        baseInfoCompany.setCompanyName(netcarBaseInfoCompany.getCompanyName());
        baseInfoCompany.setIdentifier(netcarBaseInfoCompany.getIdentifier());
        baseInfoCompany.setOwnerDivisionCode(netcarBaseInfoCompany.getAddress() != null ? String.valueOf(netcarBaseInfoCompany.getAddress()) : null);
        baseInfoCompany.setBusinessScope(netcarBaseInfoCompany.getBusinessScope());
        baseInfoCompany.setContactAddress(netcarBaseInfoCompany.getContactAddress());
        baseInfoCompany.setRegisCapital(netcarBaseInfoCompany.getRegCapital());
        baseInfoCompany.setEgalRepName(netcarBaseInfoCompany.getLegalName());
        baseInfoCompany.setLegalPhone(netcarBaseInfoCompany.getLegalPhone());
        baseInfoCompany.setLegalId(netcarBaseInfoCompany.getLegalId());
        baseInfoCompany.setCorpType(4);
        baseInfoCompany.setSource((short) 0);
        return baseInfoCompany;
    }
}
