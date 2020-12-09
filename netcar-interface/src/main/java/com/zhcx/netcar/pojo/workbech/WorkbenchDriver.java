package com.zhcx.netcar.pojo.workbech;

import java.io.Serializable;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-21 10:23
 * 工作台驾驶员信息监管
 */
public class WorkbenchDriver implements Serializable {

    private int uuid;

    /**
     * 企业标识
     */
    private String companyId;

    /**
     * 社会统一信用码
     */
    private String busiRegNumber;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 负责人
     */
    private String head;

    /**
     * 电话
     */
    private String phone;

    /**
     * 驾照过期
     */
    private int licenseOverdue;

    /**
     * 年龄超标
     */
    private int ageExcessive;

    /**
     * 持证未上岗
     */
    private int notMountGuard;

    /**
     * 数据缺失
     */
    private int lackData;

    /**
     * 统计时间
     */
    private String statisticalTime;

    /**
     * excel文件地址
     */
    private String excelUrl;

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getLicenseOverdue() {
        return licenseOverdue;
    }

    public void setLicenseOverdue(int licenseOverdue) {
        this.licenseOverdue = licenseOverdue;
    }

    public int getAgeExcessive() {
        return ageExcessive;
    }

    public void setAgeExcessive(int ageExcessive) {
        this.ageExcessive = ageExcessive;
    }

    public int getNotMountGuard() {
        return notMountGuard;
    }

    public void setNotMountGuard(int notMountGuard) {
        this.notMountGuard = notMountGuard;
    }

    public int getLackData() {
        return lackData;
    }

    public void setLackData(int lackData) {
        this.lackData = lackData;
    }

    public String getStatisticalTime() {
        return statisticalTime;
    }

    public void setStatisticalTime(String statisticalTime) {
        this.statisticalTime = statisticalTime;
    }

    public String getExcelUrl() {
        return excelUrl;
    }

    public void setExcelUrl(String excelUrl) {
        this.excelUrl = excelUrl;
    }


    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getBusiRegNumber() {
        return busiRegNumber;
    }

    public void setBusiRegNumber(String busiRegNumber) {
        this.busiRegNumber = busiRegNumber;
    }

    @Override
    public String toString() {
        return "WorkbenchDriver{" +
                "uuid=" + uuid +
                ", companyId='" + companyId + '\'' +
                ", busiRegNumber='" + busiRegNumber + '\'' +
                ", enterpriseName='" + enterpriseName + '\'' +
                ", head='" + head + '\'' +
                ", phone='" + phone + '\'' +
                ", licenseOverdue=" + licenseOverdue +
                ", ageExcessive=" + ageExcessive +
                ", notMountGuard=" + notMountGuard +
                ", lackData=" + lackData +
                ", statisticalTime='" + statisticalTime + '\'' +
                ", excelUrl='" + excelUrl + '\'' +
                '}';
    }

}
