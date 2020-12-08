package com.zhcx.netcar.pojo.statistical;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单取消统计
 * kafka_netcar_revocation_out
 */
@Data
public class KafkaNetcarRevocationOut implements Serializable {
    /**
     * 订单号
     */
    private String orderid;

    /**
     * 行政区划
     */
    private String address;

    /**
     * 企业标识
     */
    private String companyId;

    /**
     * 取消类型
     */
    private String revocationType;

    /**
     * 时间
     */
    private Long time;

    /**
     * kafka_netcar_revocation_out
     */
    private static final long serialVersionUID = 1L;

    private String dataType;

    private String SDate;

    private String EDate;

    private String join;
}