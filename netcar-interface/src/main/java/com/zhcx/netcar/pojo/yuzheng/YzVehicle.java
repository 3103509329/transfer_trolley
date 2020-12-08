package com.zhcx.netcar.pojo.yuzheng;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/4/24 16:50
 **/
public class YzVehicle implements Serializable {

    private static final long serialVersionUID = 5842645185860374872L;
    /**
     * 车牌号
     */
    private String branum;
    /**
     * 厂牌
     */
    private String factype;
    /**
     * 型号
     */
    private String model;
    /**
     * 车牌颜色
     */
    private String bracolor;
    /**
     * 发动机号
     */
    private String engnum;
    /**
     * VIN
     */
    private String franum;
    /**
     * 业户名称
     */
    private String ownerName;
    /**
     * 行驶证档案编号
     */
    private String driverNum;
    /**
     * 行驶证注册日期
     */
    private String registrationdate;
    /**
     * 行驶证发证日期
     */
    private String lssuingdate;
    /**
     * 道路运输证字
     */
    private String traword;
    /**
     * 道路运输证号
     */
    private String trano;
    /**
     * 核发机构
     */
    private String gradepcode;
    /**
     * 核发日期
     */
    private String gradate;
    /**
     * 运输证有效期始
     */
    private String stadate;
    /**
     * 运输证有效期止
     */
    private String enddate;
    /**
     * 燃油类型
     */
    private String eldtype;
    /**
     * 车长（mm）
     */
    private String veclength;
    /**
     * 车宽（mm）
     */
    private String vecwide;
    /**
     * 车高（mm）
     */
    private String vechigh;
    /**
     * 核定载客数
     */
    private String cheseats;
    /**
     * 总质量
     */
    private String totalmass;
    /**
     * 经营方式
     */
    private String bustype;
    /**
     * 经营范围
     */
    private String bnscope;
    /**
     * 车辆类型
     */
    private String vectype;
    /**
     * 车身颜色
     */
    private String veccolor;
    /**
     * 1,新增;2,修改;3,删除
     */
    private Integer flage;
    /**
     * 更新时间
     */
    private long time;

    /**
     * 社会统一信用代码
     */
    private String busiRegNumber;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getBranum() {
        return branum;
    }

    public void setBranum(String branum) {
        this.branum = branum;
    }

    public String getFactype() {
        return factype;
    }

    public void setFactype(String factype) {
        this.factype = factype;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBracolor() {
        return bracolor;
    }

    public void setBracolor(String bracolor) {
        this.bracolor = bracolor;
    }

    public String getEngnum() {
        return engnum;
    }

    public void setEngnum(String engnum) {
        this.engnum = engnum;
    }

    public String getFranum() {
        return franum;
    }

    public void setFranum(String franum) {
        this.franum = franum;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDriverNum() {
        return driverNum;
    }

    public void setDriverNum(String driverNum) {
        this.driverNum = driverNum;
    }

    public String getRegistrationdate() {
        return registrationdate;
    }

    public void setRegistrationdate(String registrationdate) {
        this.registrationdate = registrationdate;
    }

    public String getLssuingdate() {
        return lssuingdate;
    }

    public void setLssuingdate(String lssuingdate) {
        this.lssuingdate = lssuingdate;
    }

    public String getTraword() {
        return traword;
    }

    public void setTraword(String traword) {
        this.traword = traword;
    }

    public String getTrano() {
        return trano;
    }

    public void setTrano(String trano) {
        this.trano = trano;
    }

    public String getGradepcode() {
        return gradepcode;
    }

    public void setGradepcode(String gradepcode) {
        this.gradepcode = gradepcode;
    }

    public String getGradate() {
        return gradate;
    }

    public void setGradate(String gradate) {
        this.gradate = gradate;
    }

    public String getStadate() {
        return stadate;
    }

    public void setStadate(String stadate) {
        this.stadate = stadate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getEldtype() {
        return eldtype;
    }

    public void setEldtype(String eldtype) {
        this.eldtype = eldtype;
    }

    public String getVeclength() {
        return veclength;
    }

    public void setVeclength(String veclength) {
        this.veclength = veclength;
    }

    public String getVecwide() {
        return vecwide;
    }

    public void setVecwide(String vecwide) {
        this.vecwide = vecwide;
    }

    public String getVechigh() {
        return vechigh;
    }

    public void setVechigh(String vechigh) {
        this.vechigh = vechigh;
    }

    public String getCheseats() {
        return cheseats;
    }

    public void setCheseats(String cheseats) {
        this.cheseats = cheseats;
    }

    public String getTotalmass() {
        return totalmass;
    }

    public void setTotalmass(String totalmass) {
        this.totalmass = totalmass;
    }

    public String getBustype() {
        return bustype;
    }

    public void setBustype(String bustype) {
        this.bustype = bustype;
    }

    public String getBnscope() {
        return bnscope;
    }

    public void setBnscope(String bnscope) {
        this.bnscope = bnscope;
    }

    public String getVectype() {
        return vectype;
    }

    public void setVectype(String vectype) {
        this.vectype = vectype;
    }

    public String getVeccolor() {
        return veccolor;
    }

    public void setVeccolor(String veccolor) {
        this.veccolor = veccolor;
    }

    public Integer getFlage() {
        return flage;
    }

    public void setFlage(Integer flage) {
        this.flage = flage;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getBusiRegNumber() {
        return busiRegNumber;
    }

    public void setBusiRegNumber(String busiRegNumber) {
        this.busiRegNumber = busiRegNumber;
    }
}
