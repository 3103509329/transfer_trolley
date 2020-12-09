package com.zhcx.netcar.pojo.statistical;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单数据统计（载客里程，载客时间，空驶里程，空驶时间，订单金额，订单数量）
 * kafka_netcar_statistics_operatePay
 */
@Data
public class KafkaNetcarStatisticsOperatePay extends KafkaNetcarStatisticsOperatePayKey implements Serializable {
    /**
     * 行政区划
     */
    private String onArea;

    /**
     * 载客里程
     */
    private Integer driveMileSum;

    /**
     * 载客时间
     */
    private Integer driveTimeSum;

    /**
     * 订单数量
     */
    private Integer orderCount;

    /**
     * 空驶里程
     */
    private Integer waitMileSum;

    /**
     * 空驶时间
     */
    private Integer waitTimeSum;

    /**
     * kafka_netcar_statistics_operatePay
     */
    private static final long serialVersionUID = 1L;

    private String dataType;

    private String SDate;

    private String EDate;

    private String join;
}