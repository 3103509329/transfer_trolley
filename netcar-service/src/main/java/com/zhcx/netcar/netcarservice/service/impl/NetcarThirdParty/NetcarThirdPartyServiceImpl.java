package com.zhcx.netcar.netcarservice.service.impl.NetcarThirdParty;

import com.zhcx.netcar.facade.mapAndVideo.NetcarThirdPartyService;
import com.zhcx.netcar.netcarservice.mapper.mapAndVideo.NetcarThirdPartyMapper;
import com.zhcx.netcar.pojo.mapAndVideo.NetcarThirdParty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName：NetcarThirdPartyServiceImpl
 * @Description:
 * @author：李亮
 * @date：2020/10/2710:55
 */
@Service
public class NetcarThirdPartyServiceImpl implements NetcarThirdPartyService {

    @Autowired
    private NetcarThirdPartyMapper netcarThirdPartyMapper;

    @Override
    public NetcarThirdParty selectByCompanyId(String companyId) {

        NetcarThirdParty netcarThirdParty = new NetcarThirdParty();
        netcarThirdParty.setCompanyId(companyId);
        return netcarThirdPartyMapper.selectByCompanyId(netcarThirdParty);
    }
}
