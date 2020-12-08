package com.zhcx.netcar.facade.mapAndVideo;

import com.zhcx.netcar.pojo.mapAndVideo.NetcarThirdParty;

public interface NetcarThirdPartyService {

    /**
     * 基于企业id获取查询地址
     * @param companyId
     * @return
     */
    NetcarThirdParty selectByCompanyId(String companyId);

}
