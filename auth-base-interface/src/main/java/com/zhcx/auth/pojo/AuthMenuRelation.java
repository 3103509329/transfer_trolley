package com.zhcx.auth.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName：AuthMenu
 * @Description: 权限菜单
 * @author：tangding
 * @date：2018-11-23 下午4:01:22 
 * @version
 */
@Data
public class AuthMenuRelation implements Serializable{
	
	/**
	  * @Fields serialVersionUID : TODO
	  */
	
	private static final long serialVersionUID = 1L;

	private Long uuid;
	/**
	 * 菜单，功能点ID集合，逗号分隔，菜单下的功能点以#开头，eg:2#1,7#1,79#1
	 */
	private String menuIds;
	/**
	 * 角色ID
	 */
	private Long roleId;
	
	private Long[] registerIds;
    
    private String timeCreated;

    private String timeModified;
    
    /**
     * 当前页码
     */
    private String pageNo;
    
    /**
     * 每一页的记录条数
     */
    private String pageSize;
    
}
