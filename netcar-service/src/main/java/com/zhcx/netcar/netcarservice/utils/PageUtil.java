package com.zhcx.netcar.netcarservice.utils;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;

/**
 * 分页工具类
 * 
 * @title
 * @author 龚进
 * @date 2017年8月18日
 * @version 1.0
 */
public class PageUtil {

	/**
	 * 默认分页条数
	 */
	private static final int DEFAULT_PAGE_NUM = 1;

	/**
	 * 默认分页条数
	 */
	private static final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * 默认进行count查询
	 */
	private static final boolean DEFAULT_COUNT = Boolean.TRUE;

	/**
	 * 开始分页
	 * 
	 * @param
	 */
	public static Page<Object> startPage(String pageNo,String pageSize) {
		PageBean pageBean = new PageBean();
		pageBean.setPageNo(pageNo);
		pageBean.setPageSize(pageSize);
		int pageSizeNew = StringUtils.isBlank(pageBean.getPageSize()) ? 0 : Integer.parseInt(pageBean.getPageSize());
		if (pageSizeNew <= 0) {
			pageSizeNew = DEFAULT_PAGE_SIZE;
		}
		int pageNum = getPageNo(pageBean);
		return PageHelper.startPage(pageNum, pageSizeNew, DEFAULT_COUNT);
	}

	/**
	 * @Title: getPageNo @Description: 获取当前页码 @author ChenPengzhen @param
	 *         pageBean @return @throws
	 */
	private static int getPageNo(PageBean pageBean) {
		// 初始化pageNo
		int pageNo = StringUtils.isBlank(pageBean.getPageNo()) ? 0 : Integer.parseInt(pageBean.getPageNo());
		if (pageNo <= 0) {
			pageNo = DEFAULT_PAGE_NUM;
		}
		return pageNo;
	}

}
