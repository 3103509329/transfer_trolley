package com.zhcx.netcar.netcarservice.service.impl.app;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.app.NewsService;
import com.zhcx.netcar.netcarservice.constant.StatusEnum;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarNewsMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.netcarservice.utils.UUIDUtils;
import com.zhcx.netcar.pojo.app.NetcarNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/2/24 19:32
 **/
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private UUIDUtils uuidUtils;

    private static final String UUIDUTILS_KEY = "netcar_portal_news";

    @Autowired
    private NetcarNewsMapper netcarNewsMapper;

    @Override
    public PageInfo<NetcarNews> selectNewsInfoListByType(Integer type, Integer pageNo, Integer pageSize, String orderBy, String keyword) throws Exception{
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarNews> list = netcarNewsMapper.selectListByType(type,keyword);
        return new PageInfo<>(list);
    }

    @Override
    public NetcarNews insertNewsInfo(NetcarNews param) throws Exception{
        param.setUuid(uuidUtils.getLongUUID(UUIDUTILS_KEY));
        param.setStatus(StatusEnum.ENABLE.getCode());
        param.setCreateTime(new Date());
        param.setUpdateTime(new Date());
        netcarNewsMapper.insert(param);
        return param;
    }

    @Override
    public int deleteNewsInfo(Long uuid) throws Exception{
        NetcarNews netcarNews = new NetcarNews();
        netcarNews.setUuid(uuid);
        netcarNews.setStatus(StatusEnum.DISABLE.getCode());
        netcarNews.setUpdateTime(new Date());
        return netcarNewsMapper.updateByPrimaryKeySelective(netcarNews);
    }

    @Override
    public int updateNewsInfo(NetcarNews param) {
        param.setUpdateTime(new Date());
        return netcarNewsMapper.updateByPrimaryKeySelective(param);
    }
}
