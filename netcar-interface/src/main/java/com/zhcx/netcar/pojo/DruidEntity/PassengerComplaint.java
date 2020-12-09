package com.zhcx.netcar.pojo.DruidEntity;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/6/10 0010 16:13
 **/
public class PassengerComplaint implements Serializable {

    private static final long serialVersionUID = -1192260400623305152L;
    private String COMPANY_ID;

    private Integer complaint_count;

    public String getCOMPANY_ID() {
        return COMPANY_ID;
    }

    public void setCOMPANY_ID(String COMPANY_ID) {
        this.COMPANY_ID = COMPANY_ID;
    }

    public Integer getComplaint_count() {
        return complaint_count;
    }

    public void setComplaint_count(Integer complaint_count) {
        this.complaint_count = complaint_count;
    }

    @Override
    public String toString() {
        return "PassengerComplaint{" +
                "COMPANY_ID='" + COMPANY_ID + '\'' +
                ", complaint_count=" + complaint_count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassengerComplaint that = (PassengerComplaint) o;
        return Objects.equals(COMPANY_ID, that.COMPANY_ID) &&
                Objects.equals(complaint_count, that.complaint_count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(COMPANY_ID, complaint_count);
    }
}
