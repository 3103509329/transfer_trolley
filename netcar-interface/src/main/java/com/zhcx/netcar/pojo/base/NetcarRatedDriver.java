package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

public class NetcarRatedDriver extends NetcarBasePojo implements Serializable {
    private String companyId;

    private String licenseId;

    private String testDate;

    private Double level;

    private String testDepartment;

    private String reserved;

    private static final long serialVersionUID = 1L;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public Double getLevel() {
        return level;
    }

    public void setLevel(Double level) {
        this.level = level;
    }

    public String getTestDepartment() {
        return testDepartment;
    }

    public void setTestDepartment(String testDepartment) {
        this.testDepartment = testDepartment;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        NetcarRatedDriver other = (NetcarRatedDriver) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getLicenseId() == null ? other.getLicenseId() == null : this.getLicenseId().equals(other.getLicenseId()))
            && (this.getTestDate() == null ? other.getTestDate() == null : this.getTestDate().equals(other.getTestDate()))
            && (this.getLevel() == null ? other.getLevel() == null : this.getLevel().equals(other.getLevel()))
            && (this.getTestDepartment() == null ? other.getTestDepartment() == null : this.getTestDepartment().equals(other.getTestDepartment()))
            && (this.getReserved() == null ? other.getReserved() == null : this.getReserved().equals(other.getReserved()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getLicenseId() == null) ? 0 : getLicenseId().hashCode());
        result = prime * result + ((getTestDate() == null) ? 0 : getTestDate().hashCode());
        result = prime * result + ((getLevel() == null) ? 0 : getLevel().hashCode());
        result = prime * result + ((getTestDepartment() == null) ? 0 : getTestDepartment().hashCode());
        result = prime * result + ((getReserved() == null) ? 0 : getReserved().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", companyId=").append(companyId);
        sb.append(", licenseId=").append(licenseId);
        sb.append(", testDate=").append(testDate);
        sb.append(", level=").append(level);
        sb.append(", testDepartment=").append(testDepartment);
        sb.append(", reserved=").append(reserved);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}