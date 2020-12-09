package com.zhcx.netcar.netcarservice.service.impl.app;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.app.PermitDriverService;
import com.zhcx.netcar.netcarservice.constant.StatusEnum;
import com.zhcx.netcar.netcarservice.constant.VerifyStatusEnum;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarPermitCompanyInfoMapper;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarPermitDriverInfoMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.netcarservice.utils.UUIDUtils;
import com.zhcx.netcar.pojo.app.NetcarPermitDriverInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/2/22 17:56
 **/
@Service
public class PermitDriverServiceImpl implements PermitDriverService {

    private static final String UUIDUTILS_KEY = "netcar_permit_driver_info";

    @Autowired
    private UUIDUtils uuidUtils;

    @Autowired
    private NetcarPermitDriverInfoMapper netcarPermitDriverInfoMapper;

    @Autowired
    private NetcarPermitCompanyInfoMapper netcarPermitCompanyInfoMapper;

    @Override
    public PageInfo<NetcarPermitDriverInfo> selectPermitDriverInfoList(Long corpId, String keyword, Integer flag, Integer pageNo, Integer pageSize, String orderBy) throws Exception{
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarPermitDriverInfo> list = netcarPermitDriverInfoMapper.selectPermitDriverInfoList(corpId, keyword, flag);
        return new PageInfo<>(list);
    }

    @Override
    public int insertPermitDriverInfo(NetcarPermitDriverInfo param) throws Exception{
        String companyName =  netcarPermitCompanyInfoMapper.selectCompanyNameByPrimaryKey(param.getCorpId());
        param.setCompanyName(companyName);
        param.setUuid(uuidUtils.getLongUUID(UUIDUTILS_KEY));
        param.setCreateTime(new Date());
        param.setFlag(VerifyStatusEnum.WAIT.getCode());
        param.setStatus(StatusEnum.ENABLE.getCode());
        return netcarPermitDriverInfoMapper.insert(param);
    }

    @Override
    public int updatePermitDriverInfo(NetcarPermitDriverInfo netcarPermitDriverInfo) throws Exception{
        netcarPermitDriverInfo.setUpdateTime(new Date());
        return netcarPermitDriverInfoMapper.updateByPrimaryKeySelective(netcarPermitDriverInfo);
    }

    @Override
    public int updatePermitDriverInfoStatus(Long uuid, Integer flag, String reason) {
        NetcarPermitDriverInfo netcarPermitDriverInfo = new NetcarPermitDriverInfo();
        netcarPermitDriverInfo.setUpdateTime(new Date());
        netcarPermitDriverInfo.setUuid(uuid);
        netcarPermitDriverInfo.setFlag(flag);
        netcarPermitDriverInfo.setReason(reason);
        return netcarPermitDriverInfoMapper.updateByPrimaryKeySelective(netcarPermitDriverInfo);
    }
}
