package com.zhcx.netcar.pojo.statistical;

import lombok.Data;

import java.io.Serializable;

/**
 * 乘客投诉统计
 * kafka_netcar_passenger_complaint
 */
@Data
public class KafkaNetcarPassengerComplaint implements Serializable {
    /**
     *
     */
    private String orderid;

    /**
     * 企业标识
     */
    private String companyId;

    /**
     * 统计字段
     */
    private Integer complaintCount;

    /**
     * 时间
     */
    private String time;

    /**
     * kafka_netcar_passenger_complaint
     */
    private static final long serialVersionUID = 1L;

    private String dataType;

    private String SDate;

    private String EDate;

    private String join;
}