package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarOperateArrive;
import com.zhcx.netcar.params.OperateParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface NetcarOperateArriveMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarOperateArrive record);

    int insertSelective(NetcarOperateArrive record);

    NetcarOperateArrive selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarOperateArrive record);

    int updateByPrimaryKey(NetcarOperateArrive record);

    List<NetcarOperateArrive> queryOperateArriveListByCondition(OperateParam param);

    //营运信息查询
    @Select("select * from netcar_operate_arrive where order_id = #{orderId}")
    NetcarOperateArrive selectByOrderId(String orderId);
}