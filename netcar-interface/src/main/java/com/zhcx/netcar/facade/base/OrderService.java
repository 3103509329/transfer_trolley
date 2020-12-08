package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.params.OrderParam;
import com.zhcx.netcar.pojo.base.*;

import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/10 21:01
 **/
public interface OrderService {
    /**
     * 根据订单状态（发起）查询订单信息
     * @param param
     * @return
     */
    PageInfo<NetcarOrderCreate> queryOrderCreateList(OrderParam param) throws Exception;

    /**
     * 根据订单状态（成功）查询订单信息
     * @param param
     * @return
     */
    PageInfo<NetcarOrderMatch> queryOrderMatchList(OrderParam param) throws Exception;

    /**
     * 根据订单状态（撤销）查询订单信息
     * @param param
     * @return
     */
    PageInfo<NetcarOrderCancel> queryOrderCancelList(OrderParam param) throws Exception;

    /**
     * 查询订单详情信息
     * @param param
     * @return
     */
    PageInfo<NetcarOrderInfo> queryOrderInfoList(OrderParam param) throws Exception;

    /**
     * 根据公司ID和驾驶证号查询address
     * @param companyId
     * @param licenseId
     * @return
     */
    Integer selectAddressByCompanyIdAndLicenseId(String companyId, String licenseId);

    /**
     * 查询订单发起地行政代码
     * @param companyId
     * @param orderId
     * @return
     */
    Integer selectAddressByCompanyIdAndOrderId(String companyId, String orderId);

    /**
     * 根据时间查询订单位置
     * @param startTime
     * @param endTime
     * @return
     */
    List<NetcarOrderCreate> queryOrderPositionListByOrderCreate(String startTime, String endTime) throws Exception;

    /*
     * 根据订单成功和发起时间计算打车时长，数据分类
     * @return
     */
    List<BookCarLevel> queryBookCarLevelByOrderCreateAndMatch(String startTime, String endTime);
}
