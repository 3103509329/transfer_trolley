package com.zhcx.netcar.pojo.statistical;

import lombok.Data;

import java.io.Serializable;

/**
 * 在离线次数统计
 * kafka_netcar_operate_loginout
 */
@Data
public class KafkaNetcarOperateLoginout extends KafkaNetcarOperateLoginoutKey implements Serializable {
    /**
     * 登录状态
     */
    private Integer logType;

    /**
     * 登录次数
     */
    private Integer logCount;

    /**
     * kafka_netcar_operate_loginout
     */
    private static final long serialVersionUID = 1L;

    private String dataType;

    private String SDate;

    private String EDate;

    private String join;
}