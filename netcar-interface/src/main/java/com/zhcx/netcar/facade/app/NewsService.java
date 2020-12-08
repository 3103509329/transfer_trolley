package com.zhcx.netcar.facade.app;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.app.NetcarNews;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/2/24 19:32
 **/
public interface NewsService {
    /**
     * 根据类型查询新闻资讯信息列表
     * @param type
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @param keyword
     * @return
     */
    PageInfo<NetcarNews> selectNewsInfoListByType(Integer type, Integer pageNo, Integer pageSize, String orderBy, String keyword) throws Exception;

    /**
     * 新闻资讯信息增加
     * @param param
     * @return
     */
    NetcarNews insertNewsInfo(NetcarNews param) throws Exception;

    /**
     * 新闻资讯信息删除
     * @param uuid
     * @return
     */
    int deleteNewsInfo(Long uuid) throws Exception;

    /**
     * 新闻资讯信息修改
     * @param param
     * @return
     */
    int updateNewsInfo(NetcarNews param);
}
