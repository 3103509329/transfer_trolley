package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.params.QueryParam;
import com.zhcx.netcar.pojo.base.NetcarOnOffLine;
import com.zhcx.netcar.pojo.base.NetcarOperateInfo;
import com.zhcx.netcar.pojo.base.NetcarOperateLogin;
import com.zhcx.netcar.pojo.base.NetcarOperateLogout;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface NetcarOnOffLineMapper {

    Integer getOnOrOffCarSum(@Param("ACTIVE") Integer ACTIVE, @Param("EVENT_TIME") String EVENT_TIME, @Param("corpId") String corpId);

    Integer getOnOrOffCarSumByHour(Map<String, Object> param);

    int deleteByPrimaryKey(String id);

    int insert(NetcarOnOffLine record);

    int insertSelective(NetcarOnOffLine record);

    NetcarOnOffLine selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NetcarOnOffLine record);

    int updateByPrimaryKey(NetcarOnOffLine record);

    List<NetcarOnOffLine> getNetcarOnOffLineList(@Param("vehicleNoList") List<String> vehicleNoList);

    int deleteLogIn(NetcarOperateLogin netcarOperateInfo);

    void insertLogIn(NetcarOperateLogin netcarOperateInfo);

    void deleteLogOut(NetcarOperateLogout netcarOperateInfo);

    void insertLogOut(NetcarOperateLogout netcarOperateInfo);

    List<NetcarOperateLogin> queryloginoutVehicleNo(QueryParam param);
}
