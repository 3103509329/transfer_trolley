package com.zhcx.netcar.netcarservice.service.impl.workbench;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.zhcx.basic.dubbo.BaseInfoCompanyDubboService;
import com.zhcx.basic.model.BaseInfoCompany;
import com.zhcx.netcar.netcarservice.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 定时任务 新增企业数据
 * @author: qzq
 * @date 2020-06-19 14:41
 **/
@Component
public class CompanyDataJob implements SimpleJob {

    private static final Logger logger = LoggerFactory.getLogger(CompanyDataJob.class);

    @Autowired
    private UUIDUtils uuidUtils;

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoCompanyDubboService companyService;

//    @Autowired
//    private BaseDistrictService districtService;

    @Value("${oracle.url}")
    private String url;

    @Value("${oracle.username}")
    private String user;

    @Value("${oracle.password}")
    private String password;


    @Override
    public void execute(ShardingContext shardingContext) {
        try {
            timer();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("DriverDataJob异常:", e);
        }
    }

    @Scheduled(cron = "${yuznheng.timed_task.amount}")
    public void timer() {

        logger.info("-------CompanyDataJob开始-------");
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象
        ResultSet result = null;// 创建一个结果集对象

        try {
            //需要新增的数据
            List<BaseInfoCompany> companyList = new ArrayList<>();
            //查询当前mysql中已存在企业统一社会信用代码
            List<BaseInfoCompany> identifierSet = companyService.selelctByNetcar();
            Map<String,List<BaseInfoCompany>>identifierMAP = identifierSet.stream().collect(Collectors.groupingBy(BaseInfoCompany::getIdentifier));
            //组装map key 区编码 value 对应uuid
            Map<String, Long> districtMap = new HashMap<>();
//            List<BaseDistrict> districtList = districtService.findAll();
//            for (BaseDistrict district : districtList) {
//                if (district.getCorpId() == null) {
//                    continue;
//                }
//                districtMap.put(String.valueOf(district.getCorpId()), district.getUuid());
//            }

            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
            con = DriverManager.getConnection(url, user, password);// 获取连接
            logger.info("-------连接oracle成功！-------");
            //查询出租车企业
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT a.OWNER_NAME, a.ADDRESS, a.PHONE, a.CANCODE, b.JURPERSON, b.JURCTC_MOBILE, b.CTCNO, ");
            sqlBuilder.append("b.BUSI_REG_NUMBER, c.PERWORD, c.GRADATE, c.START_DATE, c.END_DATE, c.GRADEPCODE, c.LICENSE_NUMBER, c.OWNER_TYPE ");
            sqlBuilder.append("FROM bas.V_SYWYC_TF_PT_OWNER a ");
            sqlBuilder.append("LEFT JOIN bas.V_CD_TF_BS_INDUSTRY_INFO b ON a.OWNER_ID = b.OWNER_ID ");
            sqlBuilder.append("LEFT JOIN bas.V_CD_TF_BS_JURCTC c ON a.OWNER_ID = c.OWNER_ID ");
            sqlBuilder.append("WHERE a.OWNER_STATUS = '1' and c.OPERATING_STATE = '1' AND b.BUSI_REG_NUMBER IS NOT NULL AND instr( a.CANCODE, '4305' ) = 1 ");
            sqlBuilder.append("AND c.OWNER_TYPE in ('5') ");
            pre = con.prepareStatement(sqlBuilder.toString());
            result = pre.executeQuery();//查询结果集

            BaseInfoCompany company = null;
            Date date = new Date();

            while (result.next() != false) {
                //过滤已存在的企业
                if (identifierMAP.containsKey(result.getString("BUSI_REG_NUMBER"))) {
                    continue;
                }
                company = new BaseInfoCompany();
                company.setCorpId(uuidUtils.getLongUUID());
                if (StringUtils.equals("1024", result.getString("OWNER_TYPE"))) {
                    company.setCorpType(2);//出租车
                    company.setSource((short) 9);//三级协同
                }
                if (StringUtils.equals("5", result.getString("OWNER_TYPE"))) {
                    company.setCorpType(4);//网约车
                    company.setSource((short) 4);//三级协同
                }
                company.setStatus(1);
                company.setProvince(17L);//湖南省
                company.setCity(185L);//邵阳市
                if (districtMap.containsKey(result.getString("CANCODE"))) {
                    company.setDistrict(districtMap.get(result.getString("CANCODE")));
                } else {
                    company.setDistrict(3015L);
                }
                company.setTimeCreated(date);
                company.setCompanyName(result.getString("OWNER_NAME"));//企业名称
                company.setContactAddress(result.getString("ADDRESS"));//通信地址
                company.setPhone(result.getString("PHONE"));//联系方式
                company.setEgalRepName(result.getString("JURPERSON"));//公司法人
                company.setLegalPhone(result.getString("JURCTC_MOBILE"));//法人代表电话
                company.setLegalId(result.getString("CTCNO"));//法人代表身份证号
                company.setIdentifier(result.getString("BUSI_REG_NUMBER"));//统一社会信用代码
                company.setOperationPermitWord(result.getString("PERWORD"));//许可证字
                company.setOperationPermitNumber(result.getString("LICENSE_NUMBER"));//许可证号
                company.setOperationLicenseFrom(result.getString("GRADATE"));//核发日期
                company.setOperationPermitFrom(result.getString("START_DATE"));//有效期起
                company.setOperationPermitTo(result.getString("END_DATE"));//有效期止
                company.setOperationLicenseIssuing(result.getString("GRADEPCODE"));//发证机构
                companyList.add(company);
            }

            if (companyList != null && companyList.size() > 0) {
                int i = companyService.saveBatch(companyList);
                logger.info("-------新增企业数据 " + companyList.size() + " 条-------");
            }

        } catch (Exception e) {
            logger.error("DataConversionJob异常: " + e.getMessage());
        } finally {
            try {
                // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
                // 注意关闭的顺序，最后使用的最先关闭
                if (result != null){
                    result.close();
                }
                if (pre != null){
                    pre.close();
                }
                if (con != null){
                    con.close();
                }
                logger.info("-------数据库连接已关闭！-------");
            } catch (Exception e) {
                logger.error("关闭连接时异常: " + e.getMessage());
            }
        }
        logger.info("-------CompanyDataJob完成-------");

    }


}
