package com.zhcx.netcar.params;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/6/10 17:19
 **/
@Data
public class VehiclePositionParam implements Serializable {
    private static final long serialVersionUID = -190647321156160560L;
    private String vehicleNo;

    private Long ts;

    private Integer pageNo = 1;

    private Integer pageSize = 1000;

    private Long startTime;

    private Long endTime;

}
