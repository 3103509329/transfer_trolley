package com.zhcx.netcar.pojo.statistical;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单数据统计（载客里程，载客时间，空驶里程，空驶时间，订单金额，订单数量）
 * kafka_netcar_statistics_operatePay
 */
@Data
public class KafkaNetcarStatisticsOperatePayKey implements Serializable {
    /**
     * 企业标识
     */
    private String companyId;

    /**
     * 时间
     */
    private String time;

    /**
     * 车辆标识
     */
    private String vehicleNo;

    /**
     * 金额
     */
    private Double factPriceSum;

    /**
     * kafka_netcar_statistics_operatePay
     */
    private static final long serialVersionUID = 1L;
}