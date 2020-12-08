package com.zhcx.netcar.pojo.base;

import com.zhcx.netcar.annotation.FieldMeta;
import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/15 15:58
 * 基础实体类
 **/
public class NetcarBasePojo implements Serializable {

    private static final long serialVersionUID = 163431182509292234L;
    /**
     * 公司名称
     */
    @FieldMeta(name = "公司名称", description = "公司名称", index = 0)
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "NetcarBasePojo{" +
                "companyName='" + companyName + '\'' +
                '}';
    }
}
