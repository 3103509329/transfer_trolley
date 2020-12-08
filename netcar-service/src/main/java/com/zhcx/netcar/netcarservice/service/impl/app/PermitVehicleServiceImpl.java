package com.zhcx.netcar.netcarservice.service.impl.app;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.app.PermitVehicleService;
import com.zhcx.netcar.netcarservice.constant.StatusEnum;
import com.zhcx.netcar.netcarservice.constant.VerifyStatusEnum;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarPermitCompanyInfoMapper;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarPermitVehicleInfoMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.netcarservice.utils.UUIDUtils;
import com.zhcx.netcar.pojo.app.NetcarPermitVehicleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/2/22 17:45
 **/
@Service
public class PermitVehicleServiceImpl implements PermitVehicleService {

    private static final String UUIDUTILS_KEY = "netcar_permit_vehicle_info";

    @Autowired
    private UUIDUtils uuidUtils;

    @Autowired
    private NetcarPermitVehicleInfoMapper netcarPermitVehicleInfoMapper;

    @Autowired
    private NetcarPermitCompanyInfoMapper netcarPermitCompanyInfoMapper;

    @Override
    public PageInfo<NetcarPermitVehicleInfo> selectPermitVehicleInfoList(Long corpId, String keyword, Integer flag, Integer pageNo, Integer pageSize, String orderBy) throws Exception{
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarPermitVehicleInfo> list = netcarPermitVehicleInfoMapper.selectPermitVehicleInfoList(corpId, keyword, flag);
        return new PageInfo<>(list);
    }

    @Override
    public int insertPermitVehicleInfo(NetcarPermitVehicleInfo param) throws Exception{
        String companyName =  netcarPermitCompanyInfoMapper.selectCompanyNameByPrimaryKey(param.getCorpId());
        param.setCompanyName(companyName);
        param.setUuid(uuidUtils.getLongUUID(UUIDUTILS_KEY));
        param.setCreateTime(new Date());
        param.setFlag(VerifyStatusEnum.WAIT.getCode());
        param.setStatus(StatusEnum.ENABLE.getCode());
        return netcarPermitVehicleInfoMapper.insert(param);
    }

    @Override
    public int updatePermitVehicleInfo(NetcarPermitVehicleInfo netcarPermitVehicleInfo) throws Exception{
        netcarPermitVehicleInfo.setUpdateTime(new Date());
        return netcarPermitVehicleInfoMapper.updateByPrimaryKey(netcarPermitVehicleInfo);
    }

    @Override
    public int updatePermitVehicleInfoStatus(Long uuid, Integer flag, String reason) {
        NetcarPermitVehicleInfo netcarPermitVehicleInfo = new NetcarPermitVehicleInfo();
        netcarPermitVehicleInfo.setUuid(uuid);
        netcarPermitVehicleInfo.setFlag(flag);
        netcarPermitVehicleInfo.setReason(reason);
        netcarPermitVehicleInfo.setUpdateTime(new Date());
        return netcarPermitVehicleInfoMapper.updateByPrimaryKeySelective(netcarPermitVehicleInfo);
    }


}
