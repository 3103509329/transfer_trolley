package com.zhcx.auth.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName：AuthMenu
 * @Description: 权限菜单
 * @author：tangding
 * @date：2018-11-23 下午4:01:22 
 * @version
 */
@Data
public class AuthUserRole implements Serializable {
	
	private static final long serialVersionUID = 1L;
    
	/**
	 * 主键id
	 */
    private Long uuid;

    /**
	 * 用户id
	 */
    private Long userId;

    /**
	 * 权限id
	 */
    private Long roleId;

    /**
	 * 有效开始时间
	 */
    private String startTime;

    /**
	 * 有效结束时间
	 */
    private String endTime;

    /**
	 * 创建者
	 */
    private Long creator;
    
    private String createName;

    /**
	 * 修改者
	 */
    private Long whoModified;

    /**
	 * 创建时间
	 */
    @JsonIgnore
    private Date timeCreated;

    /**
	 * 修改时间
	 */
    @JsonIgnore
    private Date timeModified;

    /**
	 * 状态（1：正常，2：停用）
	 */
    private Integer status;    
    
    private String roleName;

    private String sysTypes;

    /**
     * 当前页码
     */
    private String pageNo;
    
    /**
     * 每一页的记录条数
     */
    private String pageSize;
    
    private String orderBy = null;

  	private String sortType = "ASC";

  	private String applicationCode;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

}