//package com.zhcx.authorization.job;
//
//
//import com.dangdang.ddframe.job.api.ShardingContext;
//import com.dangdang.ddframe.job.api.simple.SimpleJob;
//import com.zhcx.authorization.utils.CommonUtils;
//import com.zhcx.authorization.utils.DateUtil;
//import com.zhcx.basicdata.domain.taxi.CreditDriverEvaluationDo;
//import com.zhcx.basicdata.domain.taxi.CreditDriverEvaluationRealtimeDo;
//import com.zhcx.basicdata.domain.taxi.CreditEnterpriseEvaluationDo;
//import com.zhcx.basicdata.domain.taxi.CreditEnterpriseEvaluationRealtimeDo;
//import com.zhcx.basicdata.facade.taxi.*;
//import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoCompany;
//import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoDriver;
//import com.zhcx.basicdata.response.taxi.CreditDriverEvaluationRealtimeResp;
//import com.zhcx.basicdata.response.taxi.CreditEnterpriseEvaluationRealtimeResp;
//import com.zhcx.basicdata.response.taxi.CreditRatingResp;
//import com.zhcx.common.util.UUIDUtils;
//import com.zhcx.spring.boot.job.annotation.Job;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Job(cron = "0 0 1 1 1 ?", shardingTotalCount = 1)
//public class TaxiEvaluationJob implements SimpleJob {
//
//
//    private static final Logger logger = LoggerFactory.getLogger(TaxiEvaluationJob.class);
//
//    @Autowired
//    private CreditEnterpriseEvaluationService creditEnterpriseEvaluationService;
//
//    @Autowired
//    private CreditEnterpriseEvaluationRealtimeService creditEnterpriseEvaluationRealtimeService;
//
//    @Autowired
//    private CreditDriverEvaluationService creditDriverEvaluationService;
//
//    @Autowired
//    private CreditDriverEvaluationRealtimeService creditDriverEvaluationRealtimeService;
//
//    @Autowired
//    private TaxiBaseInfoCompanyService companyService;
//
//    @Autowired
//    private TaxiBaseInfoDriverService driverService;
//
//    @Autowired
//    private CreditRatingService creditRatingService;
//
//    @Autowired
//    private CreditIndicatorClassifyService creditIndicatorClassifyService;
//
//    @Autowired
//    private UUIDUtils uuidUtils;
//
//    @Override
//    public void execute(ShardingContext shardingContext) {
//        logger.info("----TaxiEvaluationJob开始----");
//        try {
//
//            //考核周期为上一年
//            String evaluationYear = DateUtil.getPastYear();
//
//            //企业信誉考核数据
//            List<CreditEnterpriseEvaluationDo> creditEnterpriseEvaluationList = new ArrayList<>();
//            //驾驶员信誉考核数据
//            List<CreditDriverEvaluationDo> creditDriverEvaluationList = new ArrayList<>();
//
//            //设置预警数据
//            setCreditEnterpriseEvaluationList(creditEnterpriseEvaluationList, evaluationYear);
//            setCreditDriverEvaluationList(creditDriverEvaluationList, evaluationYear);
//
//            //批量保存
//            if (CommonUtils.isListNotNull(creditEnterpriseEvaluationList))
//                creditEnterpriseEvaluationService.saveBatch(creditEnterpriseEvaluationList);
//
//            if (CommonUtils.isListNotNull(creditDriverEvaluationList)) {
//                creditDriverEvaluationService.saveBatch(creditDriverEvaluationList);
//                //批量更新驾驶员表信誉评级
//                driverService.updateBatchByEvaluationLevel(creditDriverEvaluationList);
//            }
//
//
//        } catch (Exception e) {
//            logger.error("TaxiEvaluationJob异常: " + e.getMessage());
//        }
//        logger.info("----TaxiEvaluationJob执行完成----");
//
//    }
//
//    /**
//     * 设置驾驶员考核数据
//     */
//    private void setCreditDriverEvaluationList(List<CreditDriverEvaluationDo> driverList, String evaluationYear) {
//        Date date = new Date();
//        CreditDriverEvaluationRealtimeDo cderDo = new CreditDriverEvaluationRealtimeDo();
//        cderDo.setEvaluationYear(evaluationYear);
//        List<CreditDriverEvaluationRealtimeResp> driverResps = creditDriverEvaluationRealtimeService.queryCreditEnterpriseEvaluationRealtime(cderDo);
//        //已计算的驾驶员id
//        List<Long> driverIds = new ArrayList<>();
//        for (CreditDriverEvaluationRealtimeResp driverResp : driverResps) {
//
//            CreditDriverEvaluationDo evaluationDo = new CreditDriverEvaluationDo();
//            BeanUtils.copyProperties(driverResp, evaluationDo);
//            evaluationDo.setUuid(uuidUtils.getLongUUID("driver_credit_uuid"));
//            evaluationDo.setTimeCreated(date);
//            evaluationDo.setCreditGrade(driverResp.getCreditGrade());
//            evaluationDo.setEvaluationYear(driverResp.getEvaluationYear());
//            //添加到list
//            driverList.add(evaluationDo);
//            driverIds.add(driverResp.getDriverId());
//
//        }
//        //没有记录的驾驶员
//        List<TaxiBaseInfoDriver> taxiDriverExtends = driverService.selectByNotDriverIds(driverIds);
//        if (CommonUtils.isListNotNull(taxiDriverExtends)) {
//            //查询满分及对应的等级
//            int scoreSum = creditIndicatorClassifyService.findInitialValueSum((short) 2);
//            String gradeName = null;
//            CreditRatingResp creditRatingResp = creditRatingService.findGradeNameByScore((short) 2, scoreSum);
//            if (creditRatingResp != null) gradeName = creditRatingResp.getGradeName();
//
//            for (TaxiBaseInfoDriver driver : taxiDriverExtends) {
//                CreditDriverEvaluationDo evaluationDo = new CreditDriverEvaluationDo();
//                evaluationDo.setUuid(uuidUtils.getLongUUID("driver_credit_uuid"));
//                evaluationDo.setDriverId(Long.parseLong(driver.getUuid().trim()));
//                evaluationDo.setDriverName(driver.getName());
//                evaluationDo.setCertificateNumber(driver.getCertificateNumber());
//                evaluationDo.setCorpId(Long.parseLong(driver.getCorpId()));
//                evaluationDo.setCorpName(driver.getCorpName());
//                evaluationDo.setEvaluationYear(evaluationYear);
//                evaluationDo.setComprehensiveScore(scoreSum);
//                evaluationDo.setCreditGrade(gradeName);
//                evaluationDo.setCreditGradeId(creditRatingResp.getUuid());
//                evaluationDo.setTimeCreated(date);
//
//                driverList.add(evaluationDo);
//            }
//        }
//
//    }
//
//
//    /**
//     * 设置企业考核数据
//     */
//    private void setCreditEnterpriseEvaluationList(List<CreditEnterpriseEvaluationDo> corpList, String evaluationYear) {
//        Date date = new Date();
//        CreditEnterpriseEvaluationRealtimeDo ceerDo = new CreditEnterpriseEvaluationRealtimeDo();
//        ceerDo.setEvaluationYear(evaluationYear);
//        List<CreditEnterpriseEvaluationRealtimeResp> corpResps = creditEnterpriseEvaluationRealtimeService.queryCreditEnterpriseEvaluationRealtime(ceerDo);
//        //已计算的企业id
//        List<String> corpIds = new ArrayList<>();
//        for (CreditEnterpriseEvaluationRealtimeResp corpResp : corpResps) {
//
//            CreditEnterpriseEvaluationDo evaluationDo = new CreditEnterpriseEvaluationDo();
//            BeanUtils.copyProperties(corpResp, evaluationDo);
//            evaluationDo.setUuid(uuidUtils.getLongUUID("enterprise_credit_uuid"));
//            evaluationDo.setTimeCreated(date);
//            //添加到list
//            corpList.add(evaluationDo);
//            corpIds.add(corpResp.getCorpId().toString());
//        }
//        //实时监控表没有记录的企业
//        List<TaxiBaseInfoCompany> taxiCompanyExtends = companyService.selectByNotCorpIds(corpIds);
//        if (CommonUtils.isListNotNull(taxiCompanyExtends)) {
//            //查询满分及对应的等级
//            int scoreSum = creditIndicatorClassifyService.findInitialValueSum((short) 1);
//            String gradeName = null;
//            CreditRatingResp creditRatingResp = creditRatingService.findGradeNameByScore((short) 1, scoreSum);
//            if (creditRatingResp != null) gradeName = creditRatingResp.getGradeName();
//
//            for (TaxiBaseInfoCompany company : taxiCompanyExtends) {
//                CreditEnterpriseEvaluationDo evaluationDo = new CreditEnterpriseEvaluationDo();
//                evaluationDo.setUuid(uuidUtils.getLongUUID("enterprise_credit_uuid"));
//                evaluationDo.setTimeCreated(date);
//                evaluationDo.setCorpId(Long.parseLong(company.getUuid()));
//                evaluationDo.setCorpName(company.getName());
//                evaluationDo.setCorpAbbr(company.getEnterpriseName());
//                evaluationDo.setOrganizationName(company.getOrganizationName());
//                evaluationDo.setEgalRepName(company.getLegalRepresentativa());
//                evaluationDo.setEvaluationYear(evaluationYear);
//                evaluationDo.setCreditGrade(gradeName);
//                evaluationDo.setCreditGradeId(creditRatingResp.getUuid());
//                evaluationDo.setComprehensiveScore(scoreSum);
//
//                corpList.add(evaluationDo);
//            }
//        }
//    }
//
//}
