package com.zhcx.auth.utils;import java.io.Serializable;/*** Copyright dayhr2013-2014 All rights reserved* @ClassName: PageBean* @Description: 分页辅助参数设置* @author Chenpz<pengzhen_chen@dayhr.com>* @date 2014-4-25 下午4:32:54**/public class PageBean implements Serializable {	/**	* @Fields serialVersionUID : TODO	*/	private static final long serialVersionUID = 1L;	// 当前页码	private String pageNo = null;	// 每一页的记录条数	private String pageSize = null;	// 总记录	private String pageDataCount = null;	// 总页数	private String pageCount = null;	public static  PageBean DEFAULT = new PageBean();		/**	* @return pageNo	*/	public String getPageNo() {		return pageNo;	}	/**	* @param pageNo 要设置的 pageNo	*/	public void setPageNo(String pageNo) {		this.pageNo = pageNo;	}	/**	* @return pageSize	*/	public String getPageSize() {		return pageSize;	}	/**	* @param pageSize 要设置的 pageSize	*/	public void setPageSize(String pageSize) {		this.pageSize = pageSize;	}	/**	* @return pageDataCount	*/	public String getPageDataCount() {		return pageDataCount;	}	/**	* @param pageDataCount 要设置的 pageDataCount	*/	public void setPageDataCount(String pageDataCount) {		this.pageDataCount = pageDataCount;	}	/**	* @return pageCount	*/	public String getPageCount() {		return pageCount;	}	/**	* @param pageCount 要设置的 pageCount	*/	public void setPageCount(String pageCount) {		this.pageCount = pageCount;	}}