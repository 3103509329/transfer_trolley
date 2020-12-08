package com.zhcx.netcar.netcarservice.service.impl.kafka;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.zhcx.basic.dubbo.BaseInfoVehiclelicenceDubboService;
import com.zhcx.basic.model.BaseInfoEmpl;
import com.zhcx.basic.model.BaseInfoVehiclelicence;
import com.zhcx.netcar.netcarservice.utils.DateUtil;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoVehicle;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NetcarVehicleMysql {

    private Logger log = LoggerFactory.getLogger(NetcarVehicleMysql.class);

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoVehiclelicenceDubboService baseInfoVehiclelicenceDubboService;

    /**
     * @param content
     */
    @KafkaListener(topics = "${netcar.kafka.mysql.vehicle}")
    public void addComplaintFromIot(String content) {
        try {
            if (StringUtils.isBlank(content)) {
                return;
            }
            NetcarBaseInfoVehicle netcarVehiclelicence = JSON.parseObject(content, NetcarBaseInfoVehicle.class);
            BaseInfoVehiclelicence netcarEmpl = baseInfoVehiclelicenceDubboService.selectByCar_num(netcarVehiclelicence.getVehicleNo(), netcarVehiclelicence.getCompanyId());
            if (netcarEmpl == null) {
                baseInfoVehiclelicenceDubboService.insert(data(netcarVehiclelicence));
            } else if (netcarEmpl.getSource() != null || netcarEmpl.getSource() == 4) {
                netcarEmpl.setCompanyId(netcarEmpl.getCompanyId());
                baseInfoVehiclelicenceDubboService.update(netcarEmpl);
            }
        } catch (
                Exception e) {
            log.error("车辆下发数据入库异常，Error：" + e.getMessage());
        }
    }


    private BaseInfoVehiclelicence data(NetcarBaseInfoVehicle netcarVehiclelicence) {
        BaseInfoVehiclelicence baseInfoVehiclelicence1 = new BaseInfoVehiclelicence();
        baseInfoVehiclelicence1.setCompanyId(netcarVehiclelicence.getCompanyId());
        baseInfoVehiclelicence1.setCarNum(netcarVehiclelicence.getVehicleNo());
        baseInfoVehiclelicence1.setPlateNumberColour(netcarVehiclelicence.getPlateColor());
        baseInfoVehiclelicence1.setCarColor(netcarVehiclelicence.getVehicleColor());
        baseInfoVehiclelicence1.setCheckPerson(netcarVehiclelicence.getSeats() != null ? String.valueOf(netcarVehiclelicence.getSeats()) : null);
        baseInfoVehiclelicence1.setCarModel(netcarVehiclelicence.getModel());
        baseInfoVehiclelicence1.setCarBrand(netcarVehiclelicence.getBrand());
        baseInfoVehiclelicence1.setPossName(netcarVehiclelicence.getOwnerName());
        baseInfoVehiclelicence1.setEin(netcarVehiclelicence.getEngineId());
        baseInfoVehiclelicence1.setVin(netcarVehiclelicence.getVin());
        baseInfoVehiclelicence1.setFirstAddDate(netcarVehiclelicence.getCertifyDateA() != null ? DateUtil.getYMDHMSFormat(netcarVehiclelicence.getCertifyDateA()) : null);
        baseInfoVehiclelicence1.setFuelType(netcarVehiclelicence.getFuelType());
        baseInfoVehiclelicence1.setDischargeCapacity(netcarVehiclelicence.getEngineDisplace());
        baseInfoVehiclelicence1.setCarImg(netcarVehiclelicence.getPhotoId());
        baseInfoVehiclelicence1.setRoadTransportCode(netcarVehiclelicence.getCertificate());
        baseInfoVehiclelicence1.setIssuingCompany(netcarVehiclelicence.getTransAgency());
        baseInfoVehiclelicence1.setRodetransportStart(netcarVehiclelicence.getTransDateStart() != null ? DateUtil.getYMDFormat(netcarVehiclelicence.getTransDateStart()) : null);
        baseInfoVehiclelicence1.setRodetransportEnd(netcarVehiclelicence.getTransDateStop() != null ? DateUtil.getYMDFormat(netcarVehiclelicence.getTransDateStop()) : null);
        baseInfoVehiclelicence1.setCarNativePlace(String.valueOf(netcarVehiclelicence.getAddress()));
        //        baseInfoVehiclelicence1.setCommercialType(netcarVehiclelicence.getCommercialType());
        baseInfoVehiclelicence1.setVehicleType(String.valueOf(4));
        baseInfoVehiclelicence1.setSource((short) 0);

        return baseInfoVehiclelicence1;
    }
}
