package com.zhcx.netcar.pojo.DruidEntity;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/7/8 0008 17:08
 **/
public class RevocationResult implements Serializable {

    private static final long serialVersionUID = -7370730141909754870L;
    private String COMPANY_ID;

    private String ADDRESS;

    private String VEHICLE_NO;

    private Integer REVOCATION_TYPE;

    private Integer revocation_count;

    private String REVOCATION_TIME;

    public String getCOMPANY_ID() {
        return COMPANY_ID;
    }

    public void setCOMPANY_ID(String COMPANY_ID) {
        this.COMPANY_ID = COMPANY_ID;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getVEHICLE_NO() {
        return VEHICLE_NO;
    }

    public void setVEHICLE_NO(String VEHICLE_NO) {
        this.VEHICLE_NO = VEHICLE_NO;
    }

    public Integer getREVOCATION_TYPE() {
        return REVOCATION_TYPE;
    }

    public void setREVOCATION_TYPE(Integer REVOCATION_TYPE) {
        this.REVOCATION_TYPE = REVOCATION_TYPE;
    }

    public Integer getRevocation_count() {
        return revocation_count;
    }

    public void setRevocation_count(Integer revocation_count) {
        this.revocation_count = revocation_count;
    }

    public String getREVOCATION_TIME() {
        return REVOCATION_TIME;
    }

    public void setREVOCATION_TIME(String REVOCATION_TIME) {
        this.REVOCATION_TIME = REVOCATION_TIME;
    }

    @Override
    public String toString() {
        return "RevocationResult{" +
                "COMPANY_ID='" + COMPANY_ID + '\'' +
                ", ADDRESS='" + ADDRESS + '\'' +
                ", VEHICLE_NO='" + VEHICLE_NO + '\'' +
                ", REVOCATION_TYPE=" + REVOCATION_TYPE +
                ", revocation_count=" + revocation_count +
                ", REVOCATION_TIME='" + REVOCATION_TIME + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RevocationResult that = (RevocationResult) o;
        return Objects.equals(COMPANY_ID, that.COMPANY_ID) &&
                Objects.equals(ADDRESS, that.ADDRESS) &&
                Objects.equals(VEHICLE_NO, that.VEHICLE_NO) &&
                Objects.equals(REVOCATION_TYPE, that.REVOCATION_TYPE) &&
                Objects.equals(revocation_count, that.revocation_count) &&
                Objects.equals(REVOCATION_TIME, that.REVOCATION_TIME);
    }

    @Override
    public int hashCode() {
        return Objects.hash(COMPANY_ID, ADDRESS, VEHICLE_NO, REVOCATION_TYPE, revocation_count, REVOCATION_TIME);
    }
}
