package com.zhcx.netcar.netcarservice.utils;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lisai
 * @email 570815140@qq.com
 * @date 2018-09-17 17:26
 */
public class PageHelperUtil {

    public static void orderBy(String orderBy) {
        if(StringUtils.isNotBlank(orderBy)){
            int index = orderBy.lastIndexOf("_");
            if(index == 0){
                return;
            }
            String column = orderBy.substring(0, index);
            String order = orderBy.substring(index + 1);
            PageHelper.orderBy(column + " " + order);
        }
    }
}
