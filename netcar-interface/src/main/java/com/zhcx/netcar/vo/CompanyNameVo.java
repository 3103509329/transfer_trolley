package com.zhcx.netcar.vo;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/19 15:44
 **/
public class CompanyNameVo implements Serializable {
    private static final long serialVersionUID = -8020306216562474544L;
    private String companyId;

    private String companyName;

    private Integer address;

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
