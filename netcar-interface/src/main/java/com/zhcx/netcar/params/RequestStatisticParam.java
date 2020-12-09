package com.zhcx.netcar.params;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/12/3 16:25
 **/
public class RequestStatisticParam implements Serializable {

    private static final long serialVersionUID = 337623807493192099L;
    /**
     * 统计维度（0-企业 1-区域）
     */
    private Integer dimension;
    /**
     * 公司标识
     */
    private String keyword;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getDimension() {
        return dimension;
    }

    public void setDimension(Integer dimension) {
        this.dimension = dimension;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
