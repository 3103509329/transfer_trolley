package com.zhcx.netcar.pojo.workbech;

import java.io.Serializable;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-17 11:34
 * 工作台车辆监管
 */
public class WorkbenchVehicle implements Serializable {

    private int uuid;

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
     * 活跃不达标 1 达标 0 不达标
     */
    private int notActive;

    /**
     * 180天未上车 0 否 1是
     */
    private int notBusiness180;

    /**
     * 年检过期
     */
    private int annualInspectionOverdue;

    /**
     * 无车载终端
     */
    private int notTerminal;

    /**
     * 保险金额不合格
     */
    private int insuredAmountUnqualified;

    /**
     * 保险到期
     */
    private int insuranceDue;

    /**
     * 里程超限
     */
    private int mileageTransfinite;

    /**
     * 未运营
     */
    private int notOperating;

    /**
     * 数据缺失
     */
    private int lackData;

    /**
     * 公司标识
     */
    private String companyId;

    /**
     * 社会统一信用代码
     */
    private String busiRegNumber;

    /**
     * 统计时间
     */
    private String statisticalTime;

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

    public int getNotActive() {
        return notActive;
    }

    public void setNotActive(int notActive) {
        this.notActive = notActive;
    }

    public int getNotBusiness180() {
        return notBusiness180;
    }

    public void setNotBusiness180(int notBusiness180) {
        this.notBusiness180 = notBusiness180;
    }

    public int getAnnualInspectionOverdue() {
        return annualInspectionOverdue;
    }

    public void setAnnualInspectionOverdue(int annualInspectionOverdue) {
        this.annualInspectionOverdue = annualInspectionOverdue;
    }

    public int getNotTerminal() {
        return notTerminal;
    }

    public void setNotTerminal(int notTerminal) {
        this.notTerminal = notTerminal;
    }

    public int getInsuredAmountUnqualified() {
        return insuredAmountUnqualified;
    }

    public void setInsuredAmountUnqualified(int insuredAmountUnqualified) {
        this.insuredAmountUnqualified = insuredAmountUnqualified;
    }

    public int getInsuranceDue() {
        return insuranceDue;
    }

    public void setInsuranceDue(int insuranceDue) {
        this.insuranceDue = insuranceDue;
    }

    public int getMileageTransfinite() {
        return mileageTransfinite;
    }

    public void setMileageTransfinite(int mileageTransfinite) {
        this.mileageTransfinite = mileageTransfinite;
    }

    public int getNotOperating() {
        return notOperating;
    }

    public void setNotOperating(int notOperating) {
        this.notOperating = notOperating;
    }

    public int getLackData() {
        return lackData;
    }

    public void setLackData(int lackData) {
        this.lackData = lackData;
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

    public String getStatisticalTime() {
        return statisticalTime;
    }

    public void setStatisticalTime(String statisticalTime) {
        this.statisticalTime = statisticalTime;
    }

    @Override
    public String toString() {
        return "WorkbenchVehicle{" +
                "uuid=" + uuid +
                ", enterpriseName='" + enterpriseName + '\'' +
                ", head='" + head + '\'' +
                ", phone='" + phone + '\'' +
                ", notActive=" + notActive +
                ", notBusiness180=" + notBusiness180 +
                ", annualInspectionOverdue=" + annualInspectionOverdue +
                ", notTerminal=" + notTerminal +
                ", insuredAmountUnqualified=" + insuredAmountUnqualified +
                ", insuranceDue=" + insuranceDue +
                ", mileageTransfinite=" + mileageTransfinite +
                ", notOperating=" + notOperating +
                ", lackData=" + lackData +
                ", companyId='" + companyId + '\'' +
                ", busiRegNumber='" + busiRegNumber + '\'' +
                ", statisticalTime='" + statisticalTime + '\'' +
                ", excelUrl='" + excelUrl + '\'' +
                '}';
    }

    public String getExcelUrl() {
        return excelUrl;
    }

    public void setExcelUrl(String excelUrl) {
        this.excelUrl = excelUrl;
    }

}
