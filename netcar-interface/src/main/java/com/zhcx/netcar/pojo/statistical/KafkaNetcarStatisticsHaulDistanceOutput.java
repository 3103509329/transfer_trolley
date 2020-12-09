package com.zhcx.netcar.pojo.statistical;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单空驶里程，载客里程，订单数量
 * kafka_netcar_statistics_haul_distance_output
 */
@Data
public class KafkaNetcarStatisticsHaulDistanceOutput implements Serializable {
    /**
     * 企业标识
     */
    private String companyId;

    /**
     * 企业标识
     */
    private String handlingType;

    /**
     * 载客里程
     */
    private Integer driveMileSum;

    /**
     * 订单数量
     */
    private Integer operateCount;

    /**
     * 空驶里程
     */
    private Integer waitMileSum;

    /**
     * 时间
     */
    private String time;

    /**
     * 驾驶员标识
     */
    private String licenseId;

    /**
     * 车辆标识
     */
    private String vehicleNo;

    /**
     * kafka_netcar_statistics_haul_distance_output
     */
    private static final long serialVersionUID = 1L;

    private String dataType;

    private String SDate;

    private String EDate;

    private String join;
}