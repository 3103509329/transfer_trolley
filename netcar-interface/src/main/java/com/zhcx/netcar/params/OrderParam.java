package com.zhcx.netcar.params;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/7 15:23
 * 订单查询通用实体类
 **/
@Data
public class  OrderParam extends BaseParam implements Serializable {
    private static final long serialVersionUID = -6938577308403778759L;

    /**
     * 订单状态 1- 发起 2-成功 3-撤销
     */
    private Integer orderStatus;
    /**
     * 订单号-orderId 驾驶证号-licenseId 车牌号-vehicleNo 驾驶员手机号-driverPhone 行程编号-routeId
     */
    private String searchType;
    /**
     * 查询关键字（模糊查询）
     */
    private String keyword;

    /**
     * 车牌号
     */
    private String VehicleNo;

}
