package com.zhcx.netcar.facade.kafkaStream;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/6/9 11:34
 **/
public interface StreamService {

    /**
     * 启动订单流处理
     * @return
     */
    int startOrderStream();

    /**
     * 启动运距流处理
     * @return
     */
    int startDistanceStream();

    /**
     * 启动投诉分析流处理
     * @return
     */
    int startComplaintStream();

    /**
     * 启动登陆统计流处理
     * @return
     */
    int startLogInStream();

    /**
     * 启动登出统计流处理
     * @return
     */
    int startLogOutStream();

    /**
     * 订单撤销统计流处理
     * @return
     */
    int startRevocationStream();

    /**
     * 车辆Cassandra数据入库
     * @return
     */
    int startVehileCassandraSteam();

    /**
     * 驾驶员Cassandra数据入库
     * @return
     */
    int startDriverCassandraStream();
}
