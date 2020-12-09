package com.zhcx.netcar.netcarservice.service.impl.kafka;

import com.alibaba.fastjson.JSON;
import com.zhcx.netcar.facade.yunzheng.YunZhengCompanyService;
import com.zhcx.netcar.facade.yunzheng.YunZhengDriverService;
import com.zhcx.netcar.facade.yunzheng.YunZhengVehicleService;
import com.zhcx.netcar.netcarservice.constant.YzStatusEnum;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarOperateInfoMapper;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarOrderInfoMapper;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarShareInfoMapper;
import com.zhcx.netcar.netcarservice.utils.DateStringUtil;
import com.zhcx.netcar.pojo.base.NetcarOperateInfo;
import com.zhcx.netcar.pojo.base.NetcarOrderInfo;
import com.zhcx.netcar.pojo.base.NetcarShareInfo;
import com.zhcx.netcar.pojo.yuzheng.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @version V1.0
 * @autor ht(15616537979 @ 126.com)
 * @date 2019/3/4
 * @description kafka消费者处理类
 **/
@Component
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private YunZhengDriverService yunZhengDriverService;

    @Autowired
    private YunZhengVehicleService yunZhengVehicleService;

    @Autowired
    private YunZhengCompanyService yunZhengCompanyService;

    @Autowired
    private NetcarOrderInfoMapper orderInfoMapper;

    @Autowired
    private NetcarOperateInfoMapper vehicleOperationMapper;

    @Autowired
    private NetcarShareInfoMapper netcarShareInfoMapper;

    /**
     * 消费益阳运政系统驾驶员消息
     * @param content
     */
    @KafkaListener(id = "baseInfoDriverPermit", topics = "nifi-netcar-transportSystem-baseInfoDriver")
    public void baseInfoDriverPermitMessage(ConsumerRecord<String, String> content) {
        insertDriverPermitInfo(content);
    }

    private void insertDriverPermitInfo(ConsumerRecord<String,String> content) {
        String value = content.value();
        if (StringUtils.isBlank(value)) {
            return;
        }
        YzDriver yzDriver = JSON.parseObject(value, YzDriver.class);
        YunZhengDriver yunZhengDriver = new YunZhengDriver();
        yunZhengDriver.setCardno(yzDriver.getCardno());
        yunZhengDriver.setFlage(yzDriver.getFlage());
        yunZhengDriver.setTime(DateStringUtil.dateToStr(new Date(yzDriver.getTime())));
        if(yzDriver.getFlage() == YzStatusEnum.DELETE.getCode()){
            yunZhengDriverService.updateByPrimaryKeySelective(yunZhengDriver);
            return;
        }

        yunZhengDriver.setAddress(yzDriver.getAddress());
        yunZhengDriver.setBirthday(yzDriver.getBirthday());
        yunZhengDriver.setSex(yzDriver.getSex());
        yunZhengDriver.setName(yzDriver.getName());
        yunZhengDriver.setNation(yzDriver.getNation());
        yunZhengDriver.setFlage(yzDriver.getFlage());
        yunZhengDriver.setBusiregnumber(yzDriver.getBusiRegNumber());
        yunZhengDriver.setPerdritype(yzDriver.getPerdritype());
        yunZhengDriver.setBeworscope(yzDriver.getBeworscope());
        yunZhengDriver.setDristadate(yzDriver.getDristadate());
        List<Certificate> certificateList = yzDriver.getCertificate();
        Certificate certificate = new Certificate();
        if (null != certificateList && certificateList.size() > 0) {
            certificate = certificateList.get(0);
        }
        if (null != certificate) {
            yunZhengDriver.setStartdate(certificate.getStartdate());
            yunZhengDriver.setGradate(certificate.getGradate());
            yunZhengDriver.setEnddate(certificate.getEnddate());
        }
        if (yzDriver.getFlage() == YzStatusEnum.ADD.getCode()) {
            yunZhengDriverService.insert(yunZhengDriver);
        } else {
            yunZhengDriverService.updateByPrimaryKeySelective(yunZhengDriver);
        }
    }

    /**
     * 消费益阳运政系统车辆消息
     * @param content
     */
    @KafkaListener(id = "baseInfoVehiclePermit", topics = "nifi-netcar-transportSystem-baseInfoVehicle")
    public void baseInfoVehiclePermitMessage(ConsumerRecord<String, String> content) {
        insertVehiclePermitInfo(content);
    }

    private void insertVehiclePermitInfo(ConsumerRecord<String,String> content) {
        String value = content.value();
        if (StringUtils.isBlank(value)) {
            return;
        }

        YzVehicle yzVehicle = JSON.parseObject(value, YzVehicle.class);
        YunZhengVehicle yunZhengVehicle = new YunZhengVehicle();
        yunZhengVehicle.setBranum(yzVehicle.getBranum());
        yunZhengVehicle.setFlage(yzVehicle.getFlage());
        yunZhengVehicle.setTime(DateStringUtil.dateToStr(new Date(yzVehicle.getTime())));
        if (yunZhengVehicle.getFlage() == YzStatusEnum.DELETE.getCode()) {
            yunZhengVehicleService.updateByPrimaryKeySelective(yunZhengVehicle);
            return;
        }
        yunZhengVehicle.setFactype(yzVehicle.getFactype());
        yunZhengVehicle.setBustype(yzVehicle.getBustype());
        yunZhengVehicle.setModel(yzVehicle.getModel());
        yunZhengVehicle.setBracolor(yzVehicle.getBracolor());
        yunZhengVehicle.setGradepcode(yzVehicle.getGradepcode());
        yunZhengVehicle.setTraword(yzVehicle.getTraword());
        yunZhengVehicle.setTrano(yzVehicle.getTrano());
        yunZhengVehicle.setGradate(yzVehicle.getGradate());
        yunZhengVehicle.setDriverNum(yzVehicle.getDriverNum());
        yunZhengVehicle.setEngnum(yzVehicle.getEngnum());
        yunZhengVehicle.setEldtype(yzVehicle.getEldtype());
        yunZhengVehicle.setLssuingdate(yzVehicle.getLssuingdate());
        yunZhengVehicle.setModel(yzVehicle.getModel());
        yunZhengVehicle.setOwnerName(yzVehicle.getOwnerName());
        yunZhengVehicle.setVeccolor(yzVehicle.getVeccolor());
        yunZhengVehicle.setRegistrationdate(yzVehicle.getRegistrationdate());
        yunZhengVehicle.setCheseats(yzVehicle.getCheseats());
        yunZhengVehicle.setTotalmass(yzVehicle.getTotalmass());
        yunZhengVehicle.setGradate(yzVehicle.getGradepcode());
        yunZhengVehicle.setBnscope(yzVehicle.getBnscope());
        yunZhengVehicle.setStadate(yzVehicle.getStadate());
        yunZhengVehicle.setEnddate(yzVehicle.getEnddate());
        yunZhengVehicle.setVechigh(yzVehicle.getVechigh());
        yunZhengVehicle.setVeclength(yzVehicle.getVeclength());
        yunZhengVehicle.setVecwide(yzVehicle.getVecwide());
        yunZhengVehicle.setVeccolor(yzVehicle.getVeccolor());
        yunZhengVehicle.setVectype(yzVehicle.getVectype());
        yunZhengVehicle.setFranum(yzVehicle.getFranum());
        yunZhengVehicle.setBusiregnumber(yzVehicle.getBusiRegNumber());
        if(yunZhengVehicle.getFlage() == YzStatusEnum.ADD.getCode()){
            yunZhengVehicleService.insert(yunZhengVehicle);
        } else {
            yunZhengVehicleService.updateByPrimaryKeySelective(yunZhengVehicle);
        }
    }

    /**
     * 消费益阳运政系统企业消息
     * @param content
     */
    @KafkaListener(id = "baseInfoCompanyPermit", topics = "nifi-netcar-transportSystem-baseInfoCompanyPermit")
    public void baseInfoCompanyPermitMessage(ConsumerRecord<String, String> content) {
        insertCompanyUser(content);
    }

    public void insertCompanyUser(ConsumerRecord<String, String> content) {
        try {
            String value = content.value();
            if (StringUtils.isBlank(value)) {
                return;
            }
            YzCompany yzCompany = JSON.parseObject(value, YzCompany.class);

            yunZhengCompanyService.addOrUpdateCompanyInfo(yzCompany);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    /**
     * 消费经营出发消息
     * @param content
     */
    @KafkaListener(id = "operateArrive", topics = "nifi-netcar-operateArrive")
    public void operateArriveMessage(ConsumerRecord<String, String> content) {
        try {
            handler(content);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 消费经营出发消息
     * @param content
     */
    @KafkaListener(id = "operateDepart", topics = "nifi-netcar-operateDepart")
    public void operateDepartMessage(ConsumerRecord<String, String> content) {
        try {
            handler(content);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 消费经营支付消息
     * @param content
     */
    @KafkaListener(id = "operatePay", topics = "nifi-netcar-operatePay")
    public void operatePayMessage(ConsumerRecord<String, String> content) {
        try {
            handler(content);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 消费订单发起消息
     * @param content
     */
    @KafkaListener(id = "orderCreate", topics = "nifi-netcar-orderCreate")
    public void orderCreateMessage(ConsumerRecord<String, String> content) {
        try {
            handler(content);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 消费订单成功消息
     * @param content
     */
    @KafkaListener(id = "orderMatch", topics = "nifi-netcar-orderMatch")
    public void orderMatchMessage(ConsumerRecord<String, String> content) {
        try {
            handler(content);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 消费订单撤销消息
     * @param content
     */
    @KafkaListener(id = "orderCancel", topics = "nifi-netcar-orderCancel")
    public void orderCancelMessage(ConsumerRecord<String, String> content) {
        try {
            handler(content);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 消费合乘行程消息
     * @param content
     */
    @KafkaListener(id = "shareRoute", topics = "nifi-netcar-shareRoute")
    public void shareRouteMessage(ConsumerRecord<String, String> content) {
        try {
            handler(content);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 消费合乘订单消息
     * @param content
     */
    @KafkaListener(id = "shareOrder", topics = "nifi-netcar-shareOrder")
    public void shareOrderMessage(ConsumerRecord<String, String> content) {
        try {
            handler(content);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 消费合乘支付消息
     * @param content
     */
    @KafkaListener(id = "sharePay", topics = "nifi-netcar-sharePay")
    public void sharePayMessage(ConsumerRecord<String, String> content) {
        try {
            handler(content);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void handler(ConsumerRecord<String, String> content) {
        try {
            if (StringUtils.isBlank(content.value())) {
                log.info("Message content is null，don't send,topic is：" + content.topic());
                return;
            }
            //获取kafka中topic的名字
            String topic = content.topic();
            String value = content.value();
            //益阳市
            switch (topic) {
                case "nifi-netcar-orderCreate":
                case "nifi-netcar-orderMatch":
                case "nifi-netcar-orderCancel":
                    NetcarOrderInfo orderInfo = JSON.parseObject(value, NetcarOrderInfo.class);
                    if (null != orderInfo) {
                        orderInfoMapper.addOrUpdateOrderInfo(orderInfo);
                    }
                    break;
                case "nifi-netcar-operateDepart":
                case "nifi-netcar-operateArrive":
                case "nifi-netcar-operatePay":
                    NetcarOperateInfo vehicleOperation = JSON.parseObject(value, NetcarOperateInfo.class);
                    if (null != vehicleOperation) {
                        vehicleOperationMapper.addOrUpdateOperationInfo(vehicleOperation);
                    }
                    break;
                case "nifi-netcar-shareRoute":
                case "nifi-netcar-shareOrder":
                case "nifi-netcar-sharePay":
                    NetcarShareInfo netcarShareInfo = JSON.parseObject(value, NetcarShareInfo.class);
                    if (null != netcarShareInfo) {
                        netcarShareInfoMapper.addOrUpdateShareInfo(netcarShareInfo);
                    }
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("SEND message exception，message content is:" + content.value());
            log.error("SEND message exception,exception is: {}", e.getMessage());
        }
    }
}
