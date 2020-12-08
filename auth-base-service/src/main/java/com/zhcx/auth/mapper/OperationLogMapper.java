package com.zhcx.auth.mapper;

import com.zhcx.auth.pojo.OperationLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-24 14:10
 * 登陆操作日志
 */
@Mapper
public interface OperationLogMapper {

    /**
     * 添加登录日志
     *
     * @param operationLog
     * @return
     */
    @Insert(" INSERT INTO operation_log(uuid,operator_id,operator_name,login_ip,log_type,operation,create_time,remark,target_id,target_name,source,url) " +
            " VALUES(#{uuid},#{operatorId},#{operatorName},#{loginIp},#{logType},#{operation},now(),#{remark},#{targetId},#{targetName},#{source},#{url})")
    int addOperationLog(OperationLog operationLog);

    /**
     * 查询登录日志
     *
     * @return
     */
    @Select(" <script>" +
            " SELECT uuid,operator_id as operatorId,operator_name as operatorName,login_ip as loginIp,log_type as logType, " +
            " operation,create_time as createTime,remark,target_id as targetId,target_name as targetName,source,url " +
            " FROM operation_log " +
            " WHERE 1 = 1 " +
            " <if test=\"keyword != null and keyword != '' \"> " +
            "  and (operator_name like #{keyword} or operation like  #{keyword})  " +
            " </if> " +
            " <if test=\"logType != null and logType != '' \"> " +
            "  and log_type = #{logType}" +
            " </if> " +
            " <if test=\"startTime != null and startTime != '' \"> " +
            "  and STR_TO_DATE( create_time, '%Y-%m-%d' ) &gt;= #{startTime}  " +
            " </if> " +
            " <if test=\"endTime != null and endTime != '' \"> " +
            "  and STR_TO_DATE( create_time, '%Y-%m-%d' ) &lt;= #{endTime}  " +
            " </if> " +
            " ORDER BY create_time DESC" +
            " </script> ")
    List<OperationLog> getOperationLogList(@Param("keyword") String keyword, @Param("logType") String logType,
                                           @Param("startTime") String startTime, @Param("endTime") String endTime);
}
