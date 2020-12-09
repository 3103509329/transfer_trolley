package com.zhcx.netcar.netcarservice.service.impl.app;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.app.PermitCompanyService;
import com.zhcx.netcar.netcarservice.constant.StatusEnum;
import com.zhcx.netcar.netcarservice.constant.VerifyStatusEnum;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarPermitCompanyInfoMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.netcarservice.utils.UUIDUtils;
import com.zhcx.netcar.pojo.app.NetcarPermitCompanyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/2/22 17:12
 **/
@Service
public class PermitCompanyServiceImpl implements PermitCompanyService {

    private static final String UUIDUTILS_KEY = "netcar_permit_company_info";

    @Autowired
    private UUIDUtils uuidUtils;

    @Autowired
    private NetcarPermitCompanyInfoMapper netcarPermitCompanyInfoMapper;

    @Override
    public PageInfo<NetcarPermitCompanyInfo> selectPermitCompanyInfoList(Long corpId, Integer flag, String keyword, Integer pageNo, Integer pageSize, String orderBy) throws Exception{
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarPermitCompanyInfo> list = netcarPermitCompanyInfoMapper.selectPermitCompanyInfoList(corpId, flag, keyword);
        return new PageInfo<>(list);
    }

    @Override
    public NetcarPermitCompanyInfo selectPermitCompanyInfoDetail(Long corpId) {
        return netcarPermitCompanyInfoMapper.selectByPrimaryKey(corpId);
    }

    @Override
    public int insertPermitCompanyInfo(NetcarPermitCompanyInfo netcarPermitCompanyInfo) throws Exception{
        netcarPermitCompanyInfo.setUuid(uuidUtils.getLongUUID(UUIDUTILS_KEY));
        netcarPermitCompanyInfo.setCreateTime(new Date());
        netcarPermitCompanyInfo.setFlag(VerifyStatusEnum.WAIT.getCode());
        netcarPermitCompanyInfo.setStatus(StatusEnum.ENABLE.getCode());
        return netcarPermitCompanyInfoMapper.insert(netcarPermitCompanyInfo);
    }

    @Override
    public int updatePermitCompanyInfo(NetcarPermitCompanyInfo netcarPermitCompanyInfo) throws Exception {
        netcarPermitCompanyInfo.setUpdateTime(new Date());
        return netcarPermitCompanyInfoMapper.updateByPrimaryKeySelective(netcarPermitCompanyInfo);
    }

    @Override
    public int deletePermitCompanyInfo(Integer uuid) throws Exception{
        NetcarPermitCompanyInfo netcarPermitCompanyInfo = new NetcarPermitCompanyInfo();
        netcarPermitCompanyInfo.setUpdateTime(new Date());
        netcarPermitCompanyInfo.setStatus(StatusEnum.DISABLE.getCode());
        return netcarPermitCompanyInfoMapper.updateByPrimaryKeySelective(netcarPermitCompanyInfo);
    }

    @Override
    public int updatePermitCompanyInfoStatus(Long uuid, Integer flag, String reason) throws Exception{
        NetcarPermitCompanyInfo netcarPermitCompanyInfo = new NetcarPermitCompanyInfo();
        netcarPermitCompanyInfo.setUuid(uuid);
        netcarPermitCompanyInfo.setUpdateTime(new Date());
        netcarPermitCompanyInfo.setFlag(flag);
        netcarPermitCompanyInfo.setReason(reason);
        return netcarPermitCompanyInfoMapper.updateByPrimaryKeySelective(netcarPermitCompanyInfo);
    }

    @Override
    public String selectPhoneByUuid(Long uuid) throws Exception{
        return netcarPermitCompanyInfoMapper.selectPhoneByUuid(uuid);
    }
}
