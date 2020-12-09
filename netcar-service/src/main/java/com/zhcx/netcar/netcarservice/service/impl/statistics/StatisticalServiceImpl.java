package com.zhcx.netcar.netcarservice.service.impl.statistics;

import com.zhcx.netcar.facade.statistics.StatisticalService;
import com.zhcx.netcar.netcarservice.mapper.statistical.*;
import com.zhcx.netcar.netcarservice.mapper.statistics.StatisticalMapper;
import com.zhcx.netcar.netcarservice.utils.DateStringUtil;
import com.zhcx.netcar.pojo.statistical.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-09 11:12
 * 统计分析
 */
@Service("statisticalService")
public class StatisticalServiceImpl implements StatisticalService {

    @Autowired
    StatisticalMapper statisticalMapper;

    @Autowired
    KafkaNetcarStatisticsOperatePayMapper kafkaNetcarStatisticsOperatePayMapper;

    @Autowired
    KafkaNetcarStatisticsHaulDistanceOutputMapper kafkaNetcarStatisticsHaulDistanceOutputMapper;

    @Autowired
    KafkaNetcarPassengerComplaintMapper kafkaNetcarPassengerComplaintMapper;

    @Autowired
    KafkaNetcarOperateLoginoutMapper kafkaNetcarOperateLoginoutMapper;

    @Autowired
    KafkaNetcarRevocationOutMapper kafkaNetcarRevocationOutMapper;
    /**
     * 从业人员车辆分析
     *
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> getAnalysis() throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        //得到一个Calendar的实例
        Calendar ca = Calendar.getInstance();
        //设置时间为当前时间
        ca.setTime(new Date());
        //年份减20
        ca.add(Calendar.YEAR, -20);
        Date year20Date = ca.getTime();
        //设置时间为当前时间
        ca.setTime(new Date());
        //年份减40
        ca.add(Calendar.YEAR, -40);
        Date year40Date = ca.getTime();

        //设置时间为当前时间
        ca.setTime(new Date());
        //年份减60
        ca.add(Calendar.YEAR, -60);
        Date year60Date = ca.getTime();

        String year20 = DateStringUtil.dateToStr(year20Date, "yyyy-MM-dd");
        String year40 = DateStringUtil.dateToStr(year40Date, "yyyy-MM-dd");
        String year60 = DateStringUtil.dateToStr(year60Date, "yyyy-MM-dd");

        int total20 = 0;
        int total20_40 = 0;
        int total40_60 = 0;
        int total60 = 0;
        //20以下的人员
        total20 = statisticalMapper.getDriverByAge20(year20);
        //20到40的人员
        total20_40 = statisticalMapper.getDriverByAge20_40(year20, year40);
        //40到60的人员
        total40_60 = statisticalMapper.getDriverByAge40_60(year40, year60);
        //60以上的人员
        total60 = statisticalMapper.getDriverByAge60(year60);

        resultMap.put("total20", total20);
        resultMap.put("total20_40", total20_40);
        resultMap.put("total40_60", total40_60);
        resultMap.put("total60", total60);

        String[] month = new String[12];
        Integer[] driver = new Integer[12];
        Integer[] vehicle = new Integer[12];
        for (int i = 11; i >= 0; i--) {
            ca.setTime(new Date());
            ca.add(Calendar.MONTH, -i);
            Date date = ca.getTime();
            String dateTime = DateStringUtil.dateToStr(date, "yyyy-MM") + "%";
            int driverTotal = statisticalMapper.getDriverTotalByMonth(dateTime);
            int vehicleTotal = statisticalMapper.getVehicleTotalByMonth(dateTime);
            month[i] = dateTime.substring(0, 7);
            driver[i] = driverTotal;
            vehicle[i] = vehicleTotal;
        }
        resultMap.put("month", month);
        resultMap.put("driverTotal", driver);
        resultMap.put("vehicleTotal", vehicle);
        return resultMap;
    }


    @Override
    public List<KafkaNetcarStatisticsOperatePay> selectByDate(String sDate, String eDate, String dateType, String join) {
        KafkaNetcarStatisticsOperatePay pay = new KafkaNetcarStatisticsOperatePay();
        pay.setEDate(eDate);
        pay.setSDate(sDate);
        pay.setDataType(dateType);
        pay.setJoin(join);
        return kafkaNetcarStatisticsOperatePayMapper.selectByDate(pay);

    }

    @Override
    public List<KafkaNetcarStatisticsHaulDistanceOutput> selectSUMByDate(String eDate, String sDate, String join) {
        KafkaNetcarStatisticsHaulDistanceOutput sum = new KafkaNetcarStatisticsHaulDistanceOutput();
        sum.setEDate(eDate);
        sum.setSDate(sDate);
        sum.setJoin(join);
        return kafkaNetcarStatisticsHaulDistanceOutputMapper.selectSUMByDate(sum);
    }

    @Override
    public List<KafkaNetcarStatisticsHaulDistanceOutput> selectByDateOnDistance(String startTime, String dateType, String join) {
        KafkaNetcarStatisticsHaulDistanceOutput distance = new KafkaNetcarStatisticsHaulDistanceOutput();
        distance.setEDate(startTime);
        distance.setDataType(dateType);
        distance.setJoin(join);
        return kafkaNetcarStatisticsHaulDistanceOutputMapper.selectByDateOnDistance(distance);
    }

    @Override
    public List<KafkaNetcarStatisticsHaulDistanceOutput> selectMonthDistanceData(String startTime, String join) {
        KafkaNetcarStatisticsHaulDistanceOutput distance = new KafkaNetcarStatisticsHaulDistanceOutput();
        distance.setEDate(startTime);
        distance.setJoin(join);
        return kafkaNetcarStatisticsHaulDistanceOutputMapper.selectMonthDistanceData(distance);
    }

    @Override
    public List<KafkaNetcarPassengerComplaint> selectByDateOnComplaint(String sDate, String eDate, String dateType, String stringJoint) {
        KafkaNetcarPassengerComplaint complaint = new KafkaNetcarPassengerComplaint();
        complaint.setSDate(sDate);
        complaint.setEDate(eDate);
        complaint.setDataType(dateType);
        complaint.setJoin(stringJoint);
        return kafkaNetcarPassengerComplaintMapper.selectByDateOnComplaint(complaint);
    }

    @Override
    public List<KafkaNetcarPassengerComplaint> getSUMorderDate(String sDate,String ids,  String stringJoint) {
        KafkaNetcarPassengerComplaint complaint = new KafkaNetcarPassengerComplaint();
        complaint.setSDate(sDate);
        complaint.setJoin(ids);
        complaint.setJoin(stringJoint);
        return kafkaNetcarPassengerComplaintMapper.getSUMorderDate(complaint);
    }

    @Override
    public List<KafkaNetcarOperateLoginout> selectByLoInOut(String startTime) {
        KafkaNetcarOperateLoginout loinout = new KafkaNetcarOperateLoginout();
        loinout.setSDate(startTime);
        return kafkaNetcarOperateLoginoutMapper.selectByLoInOut(loinout);
    }

    @Override
    public List<KafkaNetcarRevocationOut> getDriverRevocation(String startTime, String endTime, String dateType) {
        KafkaNetcarRevocationOut revocationOut = new KafkaNetcarRevocationOut();
        revocationOut.setSDate(startTime);
        revocationOut.setEDate(endTime);
        revocationOut.setDataType(dateType);
        return kafkaNetcarRevocationOutMapper.getDriverRevocation(revocationOut);
    }
}
