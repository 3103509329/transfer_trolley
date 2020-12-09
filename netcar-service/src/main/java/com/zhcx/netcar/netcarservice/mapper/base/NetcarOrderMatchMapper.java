package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarOrderMatch;
import com.zhcx.netcar.params.OrderParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface NetcarOrderMatchMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarOrderMatch record);

    int insertSelective(NetcarOrderMatch record);

    NetcarOrderMatch selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarOrderMatch record);

    int updateByPrimaryKey(NetcarOrderMatch record);

    List<NetcarOrderMatch> queryOrderMatchListByCondition(OrderParam param);

    @Select("select address from netcar_order_match where company_id = #{companyId} and order_id = #{orderId}")
    Integer selectAddressByOrderId(@Param("companyId") String companyId, @Param("orderId") String orderId);

    @Select("select address from netcar_order_match where company_id = #{companyId} and order_id = #{orderId}")
    Integer selectAddressByLicenseId(@Param("companyId") String companyId, @Param("orderId") String orderId);
}