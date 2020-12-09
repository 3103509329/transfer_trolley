package com.zhcx.netcar.pojo.yuzheng;

import java.io.Serializable;
import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/4/24 15:54
 **/
public class YzDriver implements Serializable {

    private static final long serialVersionUID = -121284534533027738L;

    /**
     * 出生日期
     */
    private String birthday;

    /**
     * 更新时间
     */
    private long time;

    /**
     * 资格证
     */
    private List<Certificate> certificate;
    /**
     * 性别
     */
    private String sex;
    /**
     * 状态 1 2 3
     */
    private int flage;
    /**
     * 地址
     */
    private String address;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证号
     */
    private String cardno;
    /**
     * 准驾车型
     */
    private String perdritype;
    /**
     * 从业资格类别
     */
    private String beworscope;
    /**
     * 民族
     */
    private String nation;

    /**
     * 驾驶证初领日期
     */
    private String dristadate;

    /**
     * 社会统一信用代码
     */
    private String busiRegNumber;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<Certificate> getCertificate() {
        return certificate;
    }

    public void setCertificate(List<Certificate> certificate) {
        this.certificate = certificate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getFlage() {
        return flage;
    }

    public void setFlage(int flage) {
        this.flage = flage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getPerdritype() {
        return perdritype;
    }

    public void setPerdritype(String perdritype) {
        this.perdritype = perdritype;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getDristadate() {
        return dristadate;
    }

    public void setDristadate(String dristadate) {
        this.dristadate = dristadate;
    }

    public String getBusiRegNumber() {
        return busiRegNumber;
    }

    public void setBusiRegNumber(String busiRegNumber) {
        this.busiRegNumber = busiRegNumber;
    }

    public String getBeworscope() {
        return beworscope;
    }

    public void setBeworscope(String beworscope) {
        this.beworscope = beworscope;
    }
}
