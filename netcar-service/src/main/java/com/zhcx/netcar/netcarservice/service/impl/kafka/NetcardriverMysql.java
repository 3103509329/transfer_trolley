package com.zhcx.netcar.netcarservice.service.impl.kafka;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.zhcx.basic.dubbo.BaseInfoCompanyDubboService;
import com.zhcx.basic.dubbo.BaseInfoEmplDubboService;
import com.zhcx.basic.model.BaseInfoCompany;
import com.zhcx.basic.model.BaseInfoEmpl;
import com.zhcx.netcar.netcarservice.utils.DateUtil;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoDriver;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NetcardriverMysql {

    private Logger log = LoggerFactory.getLogger(NetcardriverMysql.class);

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoEmplDubboService BaseInfoEmplDubboService;

    /**
     * @param content
     */
    @KafkaListener(topics = "${netcar.kafka.mysql.driver}")
    public void addComplaintFromIot(String content) {
        try {
            if (StringUtils.isBlank(content)) {
                return;
            }
            NetcarBaseInfoDriver netcarBaseInfoDriver = JSON.parseObject(content, NetcarBaseInfoDriver.class);
            BaseInfoEmpl netcarEmpl = BaseInfoEmplDubboService.selelctByidcNum(netcarBaseInfoDriver.getLicenseId(), netcarBaseInfoDriver.getCompanyId());
            if (netcarEmpl == null) {
                BaseInfoEmplDubboService.insert(data(netcarBaseInfoDriver));
            } else if (netcarEmpl.getSource() != null || netcarEmpl.getSource() == 4) {
                netcarEmpl.setCompanyId(netcarBaseInfoDriver.getCompanyId());
                BaseInfoEmplDubboService.update(netcarEmpl);
            }
        } catch (Exception e) {
            log.error("驾驶员下发数据入库异常，Error：" + e.getMessage());
        }
    }


    private BaseInfoEmpl data(NetcarBaseInfoDriver netcarBaseInfoDriver) {
        BaseInfoEmpl driver = new BaseInfoEmpl();
        driver.setAddress(netcarBaseInfoDriver.getAddress());
        driver.setName(netcarBaseInfoDriver.getDriverName());
        driver.setPhoneNo(netcarBaseInfoDriver.getDriverPhone());
        driver.setGender(netcarBaseInfoDriver.getDriverGender());
        driver.setBirthDate(netcarBaseInfoDriver.getDriverBirthday() != null ? DateUtil.getYMDHMSFormat(netcarBaseInfoDriver.getDriverBirthday()) : null);
        driver.setNationCode(netcarBaseInfoDriver.getDriverNation());
        driver.setMaritalStatus(netcarBaseInfoDriver.getDriverMaritalStatus() != null ? Short.valueOf(netcarBaseInfoDriver.getDriverMaritalStatus()) : null);
        driver.setDegreeCode(netcarBaseInfoDriver.getDriverEducation());
        driver.setDriverCensus(netcarBaseInfoDriver.getDriverCensus());
        driver.setDriverAddress(netcarBaseInfoDriver.getDriverAddress());
        driver.setDriverContactAddress(netcarBaseInfoDriver.getDriverContactAddress());
        driver.setPresentAddr(netcarBaseInfoDriver.getDriverAddress());
        driver.setPhotoId(netcarBaseInfoDriver.getPhotoId());
        driver.setIdcNum(netcarBaseInfoDriver.getLicenseId());
        driver.setLicenseId(netcarBaseInfoDriver.getLicenseId());
        driver.setLicensePhotoId(netcarBaseInfoDriver.getLicensePhotoId());
        driver.setDriverType(netcarBaseInfoDriver.getDriverType());
        driver.setGetDriverLicenseDate(netcarBaseInfoDriver.getGetDriverLicenseDate());
        driver.setDriverLicenseOff(netcarBaseInfoDriver.getDriverLicenseOff());
        driver.setDriverLicenseOn(netcarBaseInfoDriver.getDriverLicenseOn());
        driver.setTaxiDriver(netcarBaseInfoDriver.getTaxiDriver());
        driver.setCertificateNo(netcarBaseInfoDriver.getCertificateNo());
        driver.setNetworkCarIssueOrganization(netcarBaseInfoDriver.getNetworkCarIssueOrganization());
        driver.setNetworkCarIssueDate(netcarBaseInfoDriver.getNetworkCarIssueDate());
        driver.setGetNetworkCarProofDate(netcarBaseInfoDriver.getGetNetworkCarProofDate());
        driver.setNetworkCarProofOn(netcarBaseInfoDriver.getNetworkCarProofOn());
        driver.setNetworkCarProofOff(netcarBaseInfoDriver.getNetworkCarProofOff());
        driver.setRegisterDate(netcarBaseInfoDriver.getRegisterDate());
        driver.setFullTimeDriver(netcarBaseInfoDriver.getFullTimeDriver());
        driver.setInDriverBlacklist(netcarBaseInfoDriver.getInDriverBlacklist());
        driver.setCommercialType(netcarBaseInfoDriver.getCommercialType());
        driver.setContractCompany(netcarBaseInfoDriver.getContractCompany());
        driver.setContractOn(netcarBaseInfoDriver.getContractOn());
        driver.setContractOff(netcarBaseInfoDriver.getContractOff());
        driver.setEmplType(String.valueOf(4));
        driver.setSource((short) 0);
        driver.setCompanyId(netcarBaseInfoDriver.getCompanyId());
        return driver;
    }
}
