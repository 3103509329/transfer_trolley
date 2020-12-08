package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarShareInfo;
import com.zhcx.netcar.pojo.base.NetcarShareOrder;
import com.zhcx.netcar.pojo.base.NetcarSharePay;
import com.zhcx.netcar.pojo.base.NetcarShareRoute;
import com.zhcx.netcar.params.QueryParam;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2018/11/29 0029 14:24
 **/
public interface ShareService {

    /**
     * 查询驾驶员行程发布列表
     *
     * @param queryParam
     * @return
     */
    PageInfo<NetcarShareRoute> queryShareRouteList(QueryParam queryParam) throws Exception;

    /**
     * 私人小客车合乘订单信息查询
     *
     * @param
     * @return
     */
    PageInfo<NetcarShareOrder> queryShareOrderList(QueryParam queryParam) throws Exception;

    /**
     * 私人小客车合乘支付信息查询
     *
     * @param queryParam
     * @return
     */
    PageInfo<NetcarSharePay> querySharePayList(QueryParam queryParam) throws Exception;


    /**
     * 私人小客车合乘信息详情查询
     *
     * @param queryParam
     * @return
     */
    PageInfo<NetcarShareInfo> queryShareInfoList(QueryParam queryParam) throws Exception;
}
