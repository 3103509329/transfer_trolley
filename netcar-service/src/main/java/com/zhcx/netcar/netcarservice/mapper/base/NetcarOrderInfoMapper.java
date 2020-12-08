package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.BookCarLevel;
import com.zhcx.netcar.pojo.base.NetcarOrderInfo;
import com.zhcx.netcar.params.OrderParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NetcarOrderInfoMapper {
    int deleteByPrimaryKey(@Param("companyId") String companyId, @Param("orderId") String orderId);

    int insert(NetcarOrderInfo record);

    int insertSelective(NetcarOrderInfo record);

    NetcarOrderInfo selectByPrimaryKey(@Param("companyId") String companyId, @Param("orderId") String orderId);

    int updateByPrimaryKeySelective(NetcarOrderInfo record);

    int updateByPrimaryKey(NetcarOrderInfo record);

    List<NetcarOrderInfo> queryOrderInfoList(OrderParam param);

    List<BookCarLevel> queryBookCarLevelByOrderCreateAndMatch(@Param("startTime") String startTime, @Param("endTime") String endTime);

    void addOrUpdateOrderInfo(NetcarOrderInfo orderInfo);
}