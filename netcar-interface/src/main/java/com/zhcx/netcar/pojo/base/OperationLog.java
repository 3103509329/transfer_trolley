package com.zhcx.netcar.pojo.base;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-24 14:03
 */
public class OperationLog implements Serializable {

    private long uuid;

    /**
     * 用户ID
     */
    private long operatorId;

    /**
     * 用户名
     */
    private String operatorName;

    /**
     * 访问IP
     */
    private String loginIp;

    /**
     * 操作类型(1:登录系统，2：创建账号，3：修改账号，4：重置密码，5：修改密码)
     */
    private String logType;

    /**
     * 操作内容
     */
    private String operation;

    /**
     * 操作时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 目标对象
     */
    private long targetId;

    /**
     * 目标对象名字
     */
    private String targetName;

    /**
     * 来源(taxi/netcar)
     */
    private String source;

    /**
     * 接口地址
     */
    private String url;

    private String systemType;

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    @Override
    public String toString() {
        return "OperationLog{" +
                "uuid=" + uuid +
                ", operatorId=" + operatorId +
                ", operatorName='" + operatorName + '\'' +
                ", loginIp='" + loginIp + '\'' +
                ", logType='" + logType + '\'' +
                ", operation='" + operation + '\'' +
                ", createTime=" + createTime +
                ", remark='" + remark + '\'' +
                ", targetId=" + targetId +
                ", targetName='" + targetName + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                ", systemType='" + systemType + '\'' +
                '}';
    }
}
