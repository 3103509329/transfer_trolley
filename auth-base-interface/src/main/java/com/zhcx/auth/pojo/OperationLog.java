package com.zhcx.auth.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-24 14:03
 */
@Data
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
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String createTime;

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

    private static final long serialVersionUID = 1L;

    public OperationLog() {
        super();
    }
}
