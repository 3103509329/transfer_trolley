package com.zhcx.netcar.pojo.DruidEntity;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/6/10 0010 11:03
 **/
public class LogInOutResult implements Serializable {

    private static final long serialVersionUID = -4898334392112717260L;

    private String COMPANY_ID;

    private String LICENSE_ID;

    private String VEHICLE_NO;

    private Integer LOG_TYPE;

    private String PAY_TIME;

    public String getCOMPANY_ID() {
        return COMPANY_ID;
    }

    public void setCOMPANY_ID(String COMPANY_ID) {
        this.COMPANY_ID = COMPANY_ID;
    }

    public String getLICENSE_ID() {
        return LICENSE_ID;
    }

    public void setLICENSE_ID(String LICENSE_ID) {
        this.LICENSE_ID = LICENSE_ID;
    }

    public String getVEHICLE_NO() {
        return VEHICLE_NO;
    }

    public void setVEHICLE_NO(String VEHICLE_NO) {
        this.VEHICLE_NO = VEHICLE_NO;
    }

    public Integer getLOG_TYPE() {
        return LOG_TYPE;
    }

    public void setLOG_TYPE(Integer LOG_TYPE) {
        this.LOG_TYPE = LOG_TYPE;
    }

    public String getPAY_TIME() {
        return PAY_TIME;
    }

    public void setPAY_TIME(String PAY_TIME) {
        this.PAY_TIME = PAY_TIME;
    }

    public Integer getLog_count() {
        return log_count;
    }

    public void setLog_count(Integer log_count) {
        this.log_count = log_count;
    }

    @Override
    public String toString() {
        return "LogInOutResult{" +
                "COMPANY_ID='" + COMPANY_ID + '\'' +
                ", LICENSE_ID='" + LICENSE_ID + '\'' +
                ", VEHICLE_NO='" + VEHICLE_NO + '\'' +
                ", LOG_TYPE=" + LOG_TYPE +
                ", PAY_TIME='" + PAY_TIME + '\'' +
                ", log_count=" + log_count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogInOutResult that = (LogInOutResult) o;
        return Objects.equals(COMPANY_ID, that.COMPANY_ID) &&
                Objects.equals(LICENSE_ID, that.LICENSE_ID) &&
                Objects.equals(VEHICLE_NO, that.VEHICLE_NO) &&
                Objects.equals(LOG_TYPE, that.LOG_TYPE) &&
                Objects.equals(PAY_TIME, that.PAY_TIME) &&
                Objects.equals(log_count, that.log_count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(COMPANY_ID, LICENSE_ID, VEHICLE_NO, LOG_TYPE, PAY_TIME, log_count);
    }

    private Integer log_count;

}
