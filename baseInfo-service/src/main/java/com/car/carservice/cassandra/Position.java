//package com.car.carservice.cassandra;
//
//
//import org.springframework.data.cassandra.core.mapping.Column;
//import org.springframework.data.cassandra.core.mapping.PrimaryKey;
//import org.springframework.data.cassandra.core.mapping.Table;
//
//import java.io.Serializable;
//
//@Table("ts_kv_cf")
//public class Position implements Serializable {
//
//    private static final long serialVersionUID = 2246178168837128225L;
//
//    /**
//     * 复合主键
//     */
//    @PrimaryKey
//    private PositionKey positionKey;
//
//    /**
//     * 车辆定位信息json集合
//     */
//    @Column("str_v")
//    private String strV;
//
//    public PositionKey getPositionKey() {
//        return positionKey;
//    }
//
//    public void setPositionKey(PositionKey positionKey) {
//        this.positionKey = positionKey;
//    }
//
//    public String getStrV() {
//        return strV;
//    }
//
//    public void setStrV(String strV) {
//        this.strV = strV;
//    }
//
//    @Override
//    public String toString() {
//        return "Position{" +
//                "positionKey=" + positionKey +
//                ", strV='" + strV + '\'' +
//                '}';
//    }
//}