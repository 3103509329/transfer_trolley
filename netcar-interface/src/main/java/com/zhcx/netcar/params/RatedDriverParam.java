package com.zhcx.netcar.params;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/8 14:53
 * 驾驶员评价
 **/
public class RatedDriverParam extends BaseParam implements Serializable {
    private static final long serialVersionUID = -4085933366395170586L;
    /**
     * 驾驶证号
     */
    private String licenseId;

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }
}
