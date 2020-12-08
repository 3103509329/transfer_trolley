package com.zhcx.netcar.netcarservice.service.impl.workbench;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.github.pagehelper.PageInfo;
import com.zhcx.basic.dubbo.BaseInfoCompanyDubboService;
import com.zhcx.basic.dubbo.BaseInfoEmplDubboService;
import com.zhcx.basic.model.BaseInfoCompany;
import com.zhcx.netcar.facade.base.DriverService;
import com.zhcx.netcar.netcarservice.utils.UUIDUtils;
import com.zhcx.netcar.vo.BaseInfoEmpl;
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
 * @description: 三级协同 驾驶员定时任务
 * @author: ll
 * @date 2020-06-23 16:04
 **/
@Component
public class DriverDataJob implements SimpleJob {

    private static final Logger logger = LoggerFactory.getLogger(DriverDataJob.class);

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoCompanyDubboService baseInfoCompanyService;
    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoEmplDubboService baseInfoEmplService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private UUIDUtils uuidUtils;

    @Value("${oracle.url}")
    private String url;

    @Value("${oracle.username}")
    private String user;

    @Value("${oracle.password}")
    private String password;

    @Override
    public void execute(ShardingContext shardingContext) {
        logger.info("----DriverDataJob开始----");
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象
        ResultSet result = null;// 创建一个结果集对象


        try {
            SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //需要新增的数据
            List<BaseInfoEmpl> insertEmplList = new ArrayList<>();
            //需要更新的数据
            List<BaseInfoEmpl> updateEmplList = new ArrayList<>();

            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
            con = DriverManager.getConnection(url, user, password);// 获取连接
            logger.info("-------连接oracle成功！-------");
            //查询出租车企业
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT a.NAME, a.PHONE, tmp.BUSI_REG_NUMBER, a.CARDNO, a.SEX, ");
            sqlBuilder.append("a.ADDRESS, a.DRICARDNO, a.PERDRITYPE, a.DRISTADATE, a.LHTITNO, a.LHSTADATE, ");
            sqlBuilder.append("a.REG_STARTDATE, a.REG_ENDDATE, a.LIAUTHORITY, a.DOCUMENTTYPE, ");
            sqlBuilder.append("a.CREDATE, a.CRETIME, a.EDIDATE, a.EDITIME ");
            sqlBuilder.append("FROM bas.V_SYWYC_TF_PT_CMPSN a INNER JOIN ( ");
            sqlBuilder.append("SELECT a.OWNER_ID, b.BUSI_REG_NUMBER FROM bas.V_SYWYC_TF_PT_OWNER a ");
            sqlBuilder.append("LEFT JOIN bas.V_CD_TF_BS_INDUSTRY_INFO b ON a.OWNER_ID = b.OWNER_ID ");
            sqlBuilder.append("LEFT JOIN bas.V_CD_TF_BS_JURCTC c ON a.OWNER_ID = c.OWNER_ID ");
            sqlBuilder.append("WHERE a.OWNER_STATUS = '1' AND c.OWNER_TYPE = '5' ");
            sqlBuilder.append("AND c.OPERATING_STATE = '1' AND b.BUSI_REG_NUMBER IS NOT NULL ");
            sqlBuilder.append("AND instr( a.CANCODE, '4305' ) = 1 ) tmp ON a.CMCLTID = tmp.OWNER_ID ");
            sqlBuilder.append("WHERE a.ISEFFECT = 0 ");
            pre = con.prepareStatement(sqlBuilder.toString());
            result = pre.executeQuery();//查询结果集


            //组装Map key 企业信用统一社会代码
            List<BaseInfoCompany> companyList = baseInfoCompanyService.selelctByNetcar();
            Map<String, List<BaseInfoCompany>> companyIdMap = companyList.stream().collect(Collectors.groupingBy(BaseInfoCompany::getIdentifier));
            List<com.zhcx.basic.model.BaseInfoEmpl> netcarEmplList = baseInfoEmplService.selectByNetcar();
            Map<String, List<com.zhcx.basic.model.BaseInfoEmpl>> emplMap = netcarEmplList.stream().collect(Collectors.groupingBy(com.zhcx.basic.model.BaseInfoEmpl::getLicenseId));

            BaseInfoEmpl empl = null;

            while (result.next() != false) {
                //是否更新数据
                boolean flag = false;
                empl = new BaseInfoEmpl();
                String identifier = result.getString("BUSI_REG_NUMBER");
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
                String idcNum = result.getString("CARDNO");

                if (StringUtils.isBlank(idcNum) || emplMap.keySet().contains(idcNum) || emplMap.get(idcNum) == null || emplMap.get(idcNum).get(0).getTimeModified().getTime() >= timeModified.getTime()) {
                    continue;
                } else {
                    flag = true;
                }
                BaseInfoEmpl baseInfoEmpl = new BaseInfoEmpl();
                baseInfoEmpl.setName(result.getString("NAME"));
                baseInfoEmpl.setPhoneNo(result.getString("PHONE"));
                if (StringUtils.equals("1", result.getString("SEX"))) {
                    baseInfoEmpl.setGender("F");
                } else {
                    baseInfoEmpl.setGender("M");
                }
                baseInfoEmpl.setPresentAddr(result.getString("ADDRESS"));
                baseInfoEmpl.setCorpId(null);
                baseInfoEmpl.setLicenseId(result.getString("DRICARDNO"));//驾驶证号
                String perdritype = result.getString("PERDRITYPE");
//                if (StringUtils.isNotBlank(perdritype)) baseInfoEmpl.setPermittedType(perdritype.split(",")[0]);
                baseInfoEmpl.setGetDriverLicenseDate(result.getString("DRISTADATE"));//初次领取驾驶证日期
                baseInfoEmpl.setCertificateNo(result.getString("LHTITNO"));//从业资格证号
                baseInfoEmpl.setGetNetworkCarProofDate(result.getString("LHSTADATE"));//初领从业资格证日期
                baseInfoEmpl.setNetworkCarProofOn(result.getString("REG_STARTDATE"));//有效期起
                baseInfoEmpl.setNetworkCarProofOff(result.getString("REG_ENDDATE"));//有效期止
                baseInfoEmpl.setNetworkCarIssueOrganization(result.getString("LIAUTHORITY"));//从业资格证发证机关
//                baseInfoEmpl.setQualificationCategory(result.getString("DOCUMENTTYPE"));//从业资格类型
                baseInfoEmpl.setTimeModified(timeModified);
                baseInfoEmpl.setEmplType("4");
                baseInfoEmpl.setStatus((short) 1);
                baseInfoEmpl.setSource((short) 4);
                //新增
                Date timeCreated = null;
                if (StringUtils.isBlank(cretime)) {
                    timeCreated = ymd.parse(credate);
                } else {
                    timeCreated = ymdhms.parse(credate + " " + cretime);
                }
                baseInfoEmpl.setTimeCreated(timeCreated);
                baseInfoEmpl.setIdcNum(idcNum);
                Long uuid = uuidUtils.getLongUUID();
                baseInfoEmpl.setUuid(uuid);
                if (flag) {
                    updateEmplList.add(empl);
                } else {
                    insertEmplList.add(empl);
                }
            }
            if (insertEmplList != null && insertEmplList.size() > 0) {
                driverService.saveBatchEmpl(insertEmplList);
                logger.info("-------新增驾驶员数据 " + insertEmplList.size() + " 条-------");
            }
            if (updateEmplList != null && updateEmplList.size() > 0) {
                driverService.updateBatchEmpl(updateEmplList);
                logger.info("-------更新驾驶员数据 " + updateEmplList.size() + " 条-------");
            }
        } catch (Exception e) {
            logger.error("DriverDataJob异常: " + e.getMessage());
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
        logger.info("----DriverDataJob完成----");
    }
}
