package com.zhcx.netcar.pojo.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NetcarNews implements Serializable {
    private Long uuid;

    private String title;

    private String mainImage;

    private String attachment;

    private Integer type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private String creator;

    private Integer status;

    private String content;

    private String subImages;

    private static final long serialVersionUID = 1L;

}