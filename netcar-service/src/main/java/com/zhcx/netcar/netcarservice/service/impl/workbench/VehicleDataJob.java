package com.zhcx.netcar.netcarservice.service.impl.workbench;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.github.pagehelper.PageInfo;
import com.zhcx.basic.dubbo.BaseInfoCompanyDubboService;
import com.zhcx.basic.dubbo.BaseInfoVehiclelicenceDubboService;
import com.zhcx.basic.model.BaseInfoCompany;
import com.zhcx.netcar.facade.base.VehicleService;
import com.zhcx.netcar.netcarservice.utils.UUIDUtils;
import com.zhcx.netcar.vo.BaseInfoVehiclelicence;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 三级协同车辆数据定时任务
 * @author: ll
 * @date 2020-06-4 15:09
 **/
@Component
public class VehicleDataJob implements SimpleJob {

    private static final Logger logger = LoggerFactory.getLogger(VehicleDataJob.class);

    @Autowired
    private UUIDUtils uuidUtils;

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoVehiclelicenceDubboService baseInfoVehiclelicenceService;
    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoCompanyDubboService baseInfoCompanyService;

    @Autowired
    private VehicleService vehicleService;

    @Value("${oracle.url}")
    private String url;

    @Value("${oracle.username}")
    private String user;

    @Value("${oracle.password}")
    private String password;


    @Override
    public void execute(ShardingContext shardingContext) {

        logger.info("----VehicleDataJob开始----");
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象
        ResultSet result = null;// 创建一个结果集对象
        try {
            SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
            con = DriverManager.getConnection(url, user, password);// 获取连接
            logger.info("-------连接oracle成功！-------");
            //查询出租车企业
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT DISTINCT tmp.BUSI_REG_NUMBER, a.BRANUM, a.ELDTYPE, a.VECLENGTH, a.VECWIDE, a.VECHIGH, ");
            sqlBuilder.append("a.ENGNUM, a.FRANUM, a.FACTYPE, a.MODEL, a.ISEFFECT, a.CREDATE, a.CRETIME, a.EDIDATE, a.EDITIME FROM bas.V_SYWYC_TF_BS_VECGOODS a INNER JOIN ( ");
            sqlBuilder.append("SELECT a.OWNER_ID, b.BUSI_REG_NUMBER  FROM bas.V_SYWYC_TF_PT_OWNER a ");
            sqlBuilder.append("LEFT JOIN bas.V_CD_TF_BS_INDUSTRY_INFO b ON a.OWNER_ID = b.OWNER_ID ");
            sqlBuilder.append("LEFT JOIN bas.V_CD_TF_BS_JURCTC c ON a.OWNER_ID = c.OWNER_ID ");
            sqlBuilder.append("WHERE a.OWNER_STATUS = '1' AND c.OWNER_TYPE = '5' AND c.OPERATING_STATE = '1' AND b.BUSI_REG_NUMBER IS NOT NULL ");
            sqlBuilder.append("AND instr( a.CANCODE, '4305' ) = 1 ) tmp ON a.OWNER_ID = tmp.OWNER_ID ");
            sqlBuilder.append("WHERE a.ISEFFECT = 0 ");
            pre = con.prepareStatement(sqlBuilder.toString());
            result = pre.executeQuery();//查询结果集

            //需要新增的数据
            List<BaseInfoVehiclelicence> insertBaseVehicleList = new ArrayList<>();
            //需要修改的数据
            List<BaseInfoVehiclelicence> updateBaseVehicelList = new ArrayList<>();
            BaseInfoVehiclelicence baseVehicle = new BaseInfoVehiclelicence();

            List<BaseInfoCompany> companyList = baseInfoCompanyService.selelctByNetcar();
            Map<String, List<BaseInfoCompany>> companyIdMap = companyList.stream().collect(Collectors.groupingBy(BaseInfoCompany::getIdentifier));

            PageInfo<com.zhcx.basic.model.BaseInfoVehiclelicence> netcraVehicle = baseInfoVehiclelicenceService.select(null,null, null, null, 1, 99999999, null);
            List<BaseInfoVehiclelicence> netcraVehicleList = new ArrayList<>();
            BeanUtils.copyProperties(netcraVehicle.getList(), netcraVehicleList);
            Map<String, List<BaseInfoVehiclelicence>> netcarVehicleMap = netcraVehicleList.stream().collect(Collectors.groupingBy(BaseInfoVehiclelicence::getCarNum));
            Map<String, List<BaseInfoVehiclelicence>> vinMap = netcraVehicleList.stream().collect(Collectors.groupingBy(BaseInfoVehiclelicence::getVin));
            while (result.next() != false) {
                //是否更新数据
                boolean flag = false;
//                vehicleExtend = new TaxiVehicleExtend();
                String identifier = result.getString("BUSI_REG_NUMBER");

                //判定是否包含
                if (!companyIdMap.containsKey(identifier)) {
                    continue;
                }
                String credate = result.getString("CREDATE");
                String cretime = result.getString("CRETIME");
                Date timeModified = null;
                String edidate = result.getString("EDIDATE");
                String editime = result.getString("EDITIME");
                if (StringUtils.isNotBlank(edidate) && StringUtils.isNotBlank(editime)) {
                    timeModified = ymdhms.parse(edidate + " " + editime);
                } else {
                    timeModified = ymd.parse("2018-01-01");
                }
                String vin = result.getString("FRANUM");
                if (StringUtils.isBlank(vin) || vinMap.keySet().contains(vin) || vinMap.get(vin) == null || vinMap.get(vin).get(0).getTimeModified().getTime() >= timeModified.getTime()) {
                    continue;
                } else {
                    flag = true;
                }
                baseVehicle.setCarNum(result.getString("BRANUM"));
                String rllx = null;
                String eldtypeStr = result.getString("ELDTYPE");
                if (StringUtils.isNotBlank(eldtypeStr)) {
                    int eldtype = Integer.parseInt(eldtypeStr);
                    switch (eldtype) {
                        case 1:
                            rllx = "A";
                            break;
                        case 2:
                            rllx = "B";
                            break;
                        case 3:
                            rllx = "E";
                            break;
                        case 4:
                            rllx = "F";
                            break;
                        case 5:
                            rllx = "C";
                            break;
                        case 6:
                            rllx = "O";
                            break;
                        default:
                            rllx = "Z";
                    }
                    baseVehicle.setFuelType(rllx);
                }
                baseVehicle.setCarSizeLong(result.getString("VECLENGTH"));
                baseVehicle.setCarSizeWide(result.getString("VECWIDE"));
                baseVehicle.setCarSizeHigh(result.getString("VECHIGH"));
                baseVehicle.setEin(result.getString("ENGNUM"));
                baseVehicle.setCarBrand(result.getString("FACTYPE"));
                baseVehicle.setCarModel(result.getString("MODEL"));
                baseVehicle.setTimeModified(timeModified);
                baseVehicle.setVehicleType("4");
                baseVehicle.setSource((short) 4);
                if (flag) {
                    //更新
                } else {
                    //新增
                    Date timeCreated = null;
                    if (StringUtils.isBlank(cretime)) {
                        timeCreated = ymd.parse(credate);
                    } else {
                        timeCreated = ymdhms.parse(credate + " " + cretime);
                    }
                    baseVehicle.setTimeCreated(timeCreated);
                    baseVehicle.setVin(vin);
                    Long uuid = uuidUtils.getLongUUID();
                    baseVehicle.setUuid(uuid);
                }
                if (flag) {
                    updateBaseVehicelList.add(baseVehicle);
                } else {
                    insertBaseVehicleList.add(baseVehicle);
                }
            }

            if (insertBaseVehicleList != null && insertBaseVehicleList.size() > 0) {
                vehicleService.insertBatchBaseVehicle(insertBaseVehicleList);
                logger.info("-------新增车辆数据 " + insertBaseVehicleList.size() + " 条-------");
            }
            if (updateBaseVehicelList != null && updateBaseVehicelList.size() > 0) {
                vehicleService.updateBatchBaseVehicle(updateBaseVehicelList);
                logger.info("-------更新车辆数据 " + updateBaseVehicelList.size() + " 条-------");
            }
        } catch (Exception e) {
            logger.error("VehicleDataJob异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
                // 注意关闭的顺序，最后使用的最先关闭
                if (result != null) {
                    result.close();
                }
                if (pre != null) {
                    pre.close();
                }
                if (con != null) {
                    con.close();
                }
                logger.info("-------数据库连接已关闭！-------");
            } catch (Exception e) {
                logger.error("关闭连接时异常: " + e.getMessage());
            }
        }

        logger.info("----VehicleDataJob完成----");
    }

}
