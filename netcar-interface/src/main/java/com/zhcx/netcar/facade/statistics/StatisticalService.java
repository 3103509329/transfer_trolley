package com.zhcx.netcar.facade.statistics;

import com.zhcx.netcar.pojo.statistical.*;

import java.util.List;
import java.util.Map;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-09 11:11
 * 统计分析
 */
public interface StatisticalService {


    /**
     * 从业人员车辆分析
     * @return
     * @throws Exception
     */
    Map<String,Object> getAnalysis()throws Exception;

    List<KafkaNetcarStatisticsOperatePay> selectByDate(String sDate, String eDate, String dateType, String join);

    List<KafkaNetcarStatisticsHaulDistanceOutput> selectSUMByDate(String start, String end, String Joint);

    List<KafkaNetcarStatisticsHaulDistanceOutput> selectByDateOnDistance(String startTime, String dateType, String stringJoint);

    List<KafkaNetcarStatisticsHaulDistanceOutput> selectMonthDistanceData(String startTime, String stringJoint);

    List<KafkaNetcarPassengerComplaint> selectByDateOnComplaint(String sDate, String eDate, String dateType, String stringJoint);

    List<KafkaNetcarPassengerComplaint> getSUMorderDate(String sDate,String ids, String stringJoint);

    List<KafkaNetcarOperateLoginout> selectByLoInOut(String startTime);

    List<KafkaNetcarRevocationOut> getDriverRevocation(String startTime, String endTime, String dateType);
}
