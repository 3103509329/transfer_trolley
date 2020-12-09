package com.zhcx.authorization.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * 
 * @title 
 * @author 龚进
 * @date 2017年9月8日
 * @version 1.0
 */
public class PageBeanUtil {

	/**
	 * 根据page构建pagebean对象
	 * @param page
	 * @return
	 */
	public static PageBean createPageBean(Page<Object> page) {
		PageBean pageBean = new PageBean();
		pageBean.setPageCount(page.getPages()+"");
		pageBean.setPageDataCount(page.getTotal()+"");
		pageBean.setPageNo(page.getPageNum()+"");
		pageBean.setPageSize(page.getPageSize()+"");
		return pageBean;
	}

	/**
	 * 根据page构建pagebean对象
	 * @param pageInfo
	 * @return
	 */
	public static PageBean createPageBean(PageInfo pageInfo) {
		PageBean pageBean = new PageBean();
		pageBean.setPageCount(pageInfo.getPages()+"");
		pageBean.setPageDataCount(pageInfo.getTotal()+"");
		pageBean.setPageNo(pageInfo.getPageNum()+"");
		pageBean.setPageSize(pageInfo.getPageSize()+"");
		return pageBean;
	}
}
