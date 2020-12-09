package com.zhcx.netcar.netcarservice.mapper.mapAndVideo;


import com.zhcx.netcar.pojo.mapAndVideo.NetcarThirdParty;

/**
* @ClassName: NetcarThirdPartyMapper
* @Description: 
* @author: Mybatis Generator
* @date 2020/10/27 10:48:59
*/
public interface NetcarThirdPartyMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarThirdParty record);

    int insertSelective(NetcarThirdParty record);

    NetcarThirdParty selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarThirdParty record);

    int updateByPrimaryKey(NetcarThirdParty record);

    NetcarThirdParty selectByCompanyId(NetcarThirdParty netcarThirdParty);
}