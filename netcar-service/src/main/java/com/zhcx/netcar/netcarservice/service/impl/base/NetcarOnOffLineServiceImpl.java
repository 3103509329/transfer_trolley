package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.params.QueryParam;
import com.zhcx.netcar.pojo.base.NetcarOnOffLine;
import com.zhcx.netcar.facade.base.NetcarOnOffLineService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarOnOffLineMapper;
import com.zhcx.netcar.pojo.base.NetcarOperateInfo;
import com.zhcx.netcar.pojo.base.NetcarOperateLogin;
import com.zhcx.netcar.pojo.base.NetcarOperateLogout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("netcarOnOffLineService")
public class NetcarOnOffLineServiceImpl implements NetcarOnOffLineService {

    @Autowired
    private NetcarOnOffLineMapper netcarOnOffLineMapper;

    @Override
    public List<NetcarOnOffLine> getVehicleOnOffLineList(String vehicleNos) throws Exception{
        List<String> vehicleNoList = Splitter.on(",").splitToList(vehicleNos);
        return netcarOnOffLineMapper.getNetcarOnOffLineList(vehicleNoList);
    }

    @Override
    public void deleteLogIn(NetcarOperateLogin netcarOperateInfo) {
        netcarOnOffLineMapper.deleteLogIn(netcarOperateInfo);
    }

    @Override
    public void insertLogIn(NetcarOperateLogin netcarOperateInfo) {
        netcarOnOffLineMapper.insertLogIn(netcarOperateInfo);
    }

    @Override
    public void deleteLogOut(NetcarOperateLogout netcarOperateInfo) {
        netcarOnOffLineMapper.deleteLogOut(netcarOperateInfo);
    }

    @Override
    public void insertLogOut(NetcarOperateLogout netcarOperateInfo) {
        netcarOnOffLineMapper.insertLogOut(netcarOperateInfo);
    }


    /**
     * 查询车辆在离线数据（在线，离线记录）
     * @param param
     * @return
     */
    @Override
    public PageInfo<NetcarOperateLogin> queryloginoutVehicleNo(QueryParam param) {

        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarOperateLogin> loginout = netcarOnOffLineMapper.queryloginoutVehicleNo(param);
        return new PageInfo<>(loginout);
    }
}
