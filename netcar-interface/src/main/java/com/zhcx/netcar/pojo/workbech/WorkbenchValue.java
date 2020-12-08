package com.zhcx.netcar.pojo.workbech;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-07-05 10:15
 */
public class WorkbenchValue {
    private int id;

    /**
     * 类型
     * 1部平台车辆数据缺失
     * 2已取证未运营车辆数
     * 3车辆里程超限车辆数
     * 4保险到期车辆数
     * 5保险金额不合格
     * 6无车载终端
     * 7年检过期的车辆数
     *
     * 8驾照过期
     * 9年龄超大
     * 10持证未上岗
     * 11部平台驾驶员信息缺失
     */
    private int type;

    private String value;

    private String busiRegNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getBusiRegNumber() {
        return busiRegNumber;
    }

    public void setBusiRegNumber(String busiRegNumber) {
        this.busiRegNumber = busiRegNumber;
    }
}
