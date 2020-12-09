package com.zhcx.netcar.vo;

import java.io.Serializable;

public class NetcatAccountVo implements Serializable {
    private static final long serialVersionUID = 921124228932823182L;
    private Long uuid;

    private String username;

    private String password;

    private String phone;

    private String legalName;

    private String businessLicense;

    private String companyName;

    private String driverIdCard;

    private String vehicleNo;

    private String accountType;

    private String licenseId;

    private String idCard;

    private String realName;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDriverIdCard() {
        return driverIdCard;
    }

    public void setDriverIdCard(String driverIdCard) {
        this.driverIdCard = driverIdCard;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }


    @Override
    public String toString() {
        return "NetcatAccountVo{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", legalName='" + legalName + '\'' +
                ", businessLicense='" + businessLicense + '\'' +
                ", companyName='" + companyName + '\'' +
                ", driverIdCard='" + driverIdCard + '\'' +
                ", vehicleNo='" + vehicleNo + '\'' +
                ", accountType='" + accountType + '\'' +
                '}';
    }
}