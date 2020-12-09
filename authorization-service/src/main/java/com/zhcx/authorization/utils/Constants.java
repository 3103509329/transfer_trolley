package com.zhcx.authorization.utils;


/**
 * @ClassName：Constants
 * @Description: 静态常量
 * @author：tangding
 * @date：2018年11月24日 上午9:49:16
 */
public class Constants {

    /**
     * 用户信息常量
     */
    public static final int AUTH_USER_STATUS = 1;// 正常

    public static final int AUTH_USER_STATUS_NO = 0;// 禁用

    public static final int AUTH_USER_STATUS_DELETE = 3;// 正常

    public static final String AUTH_USER_TYPE_SUPPLIER = "07";

    public static final String AUTH_USER_TYPE_COMPANY = "06";

    public static final String AUTH_USER_TYPE_PERSONAGE = "05";

    public static final String AUTH_USER_TYPE_SYS = "01";

    public static final String AUTH_USER_TYPE_ORG = "02";

    //管理员（任务监控）
    public static final String AUTH_USER_TYPE_SYS_Ext = "01";

    //APP用户权限
    public static final String AUTH_USER_TYPE_APP = "99";

    //用户账号默认密码
    public static final String AUTH_USER_DEFAULT_PASSWORD = "123456";

    public static final String DEAULT_CITY_NAME = "常德市";

    public static final String DEAULT_NET_CAR_CITY_NAME = "益阳市";

    public static final String UPLOAD_FILE_TYPE_VEHICLE = "VEHICLE";

    public static final String UPLOAD_FILE_TYPE_DRIVER = "DRIVER";

    public static final String DEAULT_TAXI_SOURCE = "taxi";

    public static final String GEO_BD09 = "bd09";

    public static final String GEO_WGS84 = "wgs84";

    public static final String GEO_GCJ02 = "gcj02";

    public static final String STATUS_YES = "1";

    public static final String STATUS_NO = "2";

    //public static final String BINDSTATUS_YES = "";

    public static final String DRIVER_BINDSTATUS_YES = "01";

    public static final String DRIVER_BINDSTATUS_NO = "02";

    public static final String VEHICLE_BINDSTATUS_NO = "04";

    public static final String TERMINAL_BINDSTATUS_YES = "01";

    public static final String TERMINAL_BINDSTATUS_NO = "02";

    /**
     * 驾驶员班次状态-白班
     */
    public static final String DRIVER_WORK_TYPE_DAY = "01";


    /**
     * 驾驶员班次状态-晚班
     */
    public static final String DRIVER_WORK_TYPE_NIGHT = "02";

    public static final String EXCLENAME_VEHICLE = "车辆基础信息";

    public static final String EXCLENAME_DRIVER = "驾驶员基础信息";

    /**
     * 监控任务状态
     */
    public static final String MONITOR_TASK_NOT = "01";    //未开始

    public static final String MONITOR_TASK_ING = "02";    //进行中

    public static final String MONITOR_TASK_OVER = "03"; //已结束

    /**
     * 车辆监控任务状态
     */
    public static final String CAR_MONITOR_TASK_ING = "01";    //进行中

    public static final String CAR_MONITOR_TASK_OVER = "02";//已完成

    public static final String CAR_MONITOR_TASK_STOP = "03";//已终止

    public static final int CAR_MONITOR_TASK = 1;//任务

    public static final int CAR_MONITOR_RECORD = 2;//记录

    /**
     * 操作日志 操作类型
     */
    public static final String LOG_TYPE_C = "1";    //添加

    public static final String LOG_TYPE_D = "2";    //删除

    public static final String LOG_TYPE_U = "3";    //修改

    public static final String LOG_TYPE_R = "4";    //查询

    public static final String RESULT_TYPE_TRUE = "1";    // 操作结果 成功

    public static final String RESULT_TYPE_FALSE = "2"; // 操作结果 失败

    /**
     * 报警信息来源 处理警情状态
     */
    public static final String ALARM_TYPE_ONE_KEY = "10";//一键报警

    public static final String ALARM_TYPE_PHONE = "20";//电话报警

    public static final String ALARM_TYPE_RESTS = "30";//其他来源

    public static final String ALARM_STATUS_NOT = "10";//未处理

    public static final String ALARM_STATUS_OVER = "20";//已处理

    public static final String ALARM_AFFIRM_YES = "10";//疑似险情

    public static final String ALARM_AFFIRM_NO = "20";//误报警

    public static final String ALARM_AFFIRM_NOT = "30";//疑似险情

    public static final String ALARM_VAL_NO = "0";//报警状态标示 0:正常

    public static final String ALARM_VAL_YES = "1";//报警状态标示 1:报警

    /**
     * 车辆在离线状态
     */

    public static final int CAR_ONLINE = 1;//在线
    public static final int CAR_OFFLINE = 0;//离线

}
