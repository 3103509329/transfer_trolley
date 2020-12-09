package com.zhcx.netcar.params;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/7 15:23
 * 条件查询通用实体类
 **/
public class OperateParam extends BaseParam implements Serializable {
    private static final long serialVersionUID = -6938577308403778759L;

    /**
     * 营运状态 1-出发 2-到达 3-支付
     * 在线状态 1-上线 2- 下线
     */
    private Integer status;
    /**
     * 订单号-orderId 驾驶证号-licenseId 车牌号-vehicleNo 驾驶员手机号-driverPhone 行程编号-routeId
     */
    private String searchType;
    /**
     * 查询关键字（模糊查询）
     */
    private String keyword;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
