package com.zhcx.netcar.pojo.yuzheng;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/4/24 15:58
 **/
public class Certificate implements Serializable {
    private static final long serialVersionUID = -2031014859485734826L;
    /**
     * 资格证初领日期
     */
    private String startdate;

    /**
     * 资格证生效日期
     */
    private String gradate;
    /**
     * 资格证失效日期
     */
    private String enddate;

    private String beworscope;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getGradate() {
        return gradate;
    }

    public void setGradate(String gradate) {
        this.gradate = gradate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getBeworscope() {
        return beworscope;
    }

    public void setBeworscope(String beworscope) {
        this.beworscope = beworscope;
    }
}
