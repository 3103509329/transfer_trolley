package com.zhcx.netcar.pojo.yuzheng;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/4/24 17:23
 **/
public class YzCompany {

    /**
     * 业户名称
     */
    private String clitname;
    /**
     * 经营范围
     */
    private String bnscope;

    /**
     * 通信地址
     */
    private String address;

    /**
     * 负责人
     */
    private String personLiable;

    /**
     * 电话
     */
    private String phone;

    /**
     * 经营许可证号
     */
    private String businessLicense;

    /**
     * 经营许证可有效期始
     */
    private String startDate;

    /**
     * 经营许证可有效期止
     */
    private String endDate;

    /**
     * 社会统一信用代码
     */
    private String busiRegNumber;

    /**
     * 注册资本
     */
    private String registeredCapital;

    /**
     * 成立日期
     */
    private String establishmentTime;

    /**
     * 更新时间
     */
    private long time;

    /**
     * 1,新增;2,修改;3,删除
     */
    private Integer flage;

    public String getClitname() {
        return clitname;
    }

    public void setClitname(String clitname) {
        this.clitname = clitname;
    }

    public String getBnscope() {
        return bnscope;
    }

    public void setBnscope(String bnscope) {
        this.bnscope = bnscope;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPersonLiable() {
        return personLiable;
    }

    public void setPersonLiable(String personLiable) {
        this.personLiable = personLiable;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBusiRegNumber() {
        return busiRegNumber;
    }

    public void setBusiRegNumber(String busiRegNumber) {
        this.busiRegNumber = busiRegNumber;
    }

    public String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public String getEstablishmentTime() {
        return establishmentTime;
    }

    public void setEstablishmentTime(String establishmentTime) {
        this.establishmentTime = establishmentTime;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Integer getFlage() {
        return flage;
    }

    public void setFlage(Integer flage) {
        this.flage = flage;
    }
}
