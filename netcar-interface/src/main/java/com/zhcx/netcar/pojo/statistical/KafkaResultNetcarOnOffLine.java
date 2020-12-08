package com.zhcx.netcar.pojo.statistical;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * kafka_result_netcar_onoffline
 */
@Data
public class KafkaResultNetcarOnOffLine implements Serializable {
    /**
     * 车辆唯一标识
     */
    private String id;

    /**
     * 0-离线，1-在线
     */
    private Integer active;

    /**
     * 记录时间
     */
    private Date eventTime;

    /**
     * 驾驶证号
     */
    private String licenseid;

    /**
     * kafka_result_netcar_onoffline
     */
    private static final long serialVersionUID = 1L;
}