package com.car.carservice.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据字典表
 * base_dictionary
 */
@Data
public class BaseDictionary implements Serializable {
    /**
     * 
     */
    private Integer uuid;

    /**
     * 类别代码
     */
    private String categoryCode;

    /**
     * 类别
     */
    private String category;

    /**
     * 项目代码
     */
    private String itemCode;

    /**
     * 项目
     */
    private String item;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 状态
     */
    private String status;
    private int pageNo = 0;

    private int pageSize = 10;
    /**
     * base_dictionary
     */
    private static final long serialVersionUID = 1L;
}