package com.zhcx.netcar.pojo.statistical;

import lombok.Data;

import java.io.Serializable;

/**
 * 在离线次数统计
 * kafka_netcar_operate_loginout
 */
@Data
public class KafkaNetcarOperateLoginoutKey implements Serializable {
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
     * 企业标识
     */
    private String companyId;

    /**
     * kafka_netcar_operate_loginout
     */
    private static final long serialVersionUID = 1L;
}