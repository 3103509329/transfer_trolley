package com.zhcx.netcar.netcarservice.service.impl.app;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.app.TrafficAssessmentService;
import com.zhcx.netcar.netcarservice.constant.StatusEnum;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarAppTrafficAssessmentMapper;
import com.zhcx.netcar.netcarservice.utils.UUIDUtils;
import com.zhcx.netcar.pojo.app.NetcarAppTrafficAssessment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/5/18 15:23
 **/
@Service
public class TrafficAssessmentServiceImpl implements TrafficAssessmentService {

    public static final String UUIDUTILS_KEY = "netcar_app_traffic_assessment";
    @Autowired
    private UUIDUtils uuidUtils;
    @Autowired
    private NetcarAppTrafficAssessmentMapper trafficAssessmentMapper;

    @Override
    public PageInfo<NetcarAppTrafficAssessment> queryTrafficAssessmentList(Integer pageNo, Integer pageSize, String orderBy, String keyword, String branum) throws Exception{
        PageHelper.startPage(pageNo, pageSize);
        List<NetcarAppTrafficAssessment> list = trafficAssessmentMapper.selectListByKeyword(keyword, branum);
        return new PageInfo<>(list);
    }

    @Override
    public int insertTrafficAssessment(NetcarAppTrafficAssessment param) throws Exception{
        param.setUuid(uuidUtils.getLongUUID(UUIDUTILS_KEY));
        param.setCreateTime(new Date());
        param.setStatus(StatusEnum.ENABLE.getCode());
        return trafficAssessmentMapper.insert(param);
    }
}
