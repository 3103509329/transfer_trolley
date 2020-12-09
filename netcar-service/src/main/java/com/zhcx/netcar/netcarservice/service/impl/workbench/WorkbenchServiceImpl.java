package com.zhcx.netcar.netcarservice.service.impl.workbench;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.workbench.WorkbenchService;
import com.zhcx.netcar.netcarservice.constant.WorkbenchStatusEnum;
import com.zhcx.netcar.netcarservice.mapper.workbench.WorkbenchMapper;
import com.zhcx.netcar.netcarservice.utils.*;
import com.zhcx.netcar.pojo.DruidEntity.NetcarStatisticsOrder;
import com.zhcx.netcar.pojo.workbech.WorkbenchDriver;
import com.zhcx.netcar.pojo.workbech.WorkbenchValue;
import com.zhcx.netcar.pojo.workbech.WorkbenchVehicle;
import com.zhcx.netcar.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-15 15:32
 * 工作台
 */
@Service("workbenchService")
public class WorkbenchServiceImpl implements WorkbenchService {

    @Autowired
    WorkbenchMapper workbenchMapper;

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private FastDFSUtil fastDFSUtil;

    @Autowired
    private ExcelUtil excelUtil;

    public void deleteValue() throws Exception {
        workbenchMapper.deleteValue();
    }


    /**
     * 益阳网约车车辆监管情况
     *
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo<WorkbenchVehicle> getVehicleMonitoring(int pageNo, int pageSize) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        List<WorkbenchVehicle> resultList = workbenchMapper.getVehicleMonitoring();
        PageInfo pageInfo = new PageInfo(resultList);
        return pageInfo;
    }

    /**
     * 益阳网约车驾驶员监管情况
     *
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo<WorkbenchDriver> getDriverMonitoring(int pageNo, int pageSize) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        List<WorkbenchDriver> resultList = workbenchMapper.getDriverMonitoring();
        PageInfo pageInfo = new PageInfo(resultList);
        return pageInfo;
    }

    /**
     * 年检过期
     *
     * @param busiRegNumber
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo<VehicleAnnualInspectionExpired> getAnnualInspectionOverdue(String busiRegNumber, int pageNo, int pageSize) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        List<String> annualInspectionOverdueList = workbenchMapper.getValueByType(busiRegNumber, WorkbenchStatusEnum.ANNUAL_INSPECTION_OVERDUE.getCode());
        //年检过期车辆信息
        List<VehicleAnnualInspectionExpired> inspectionOverdueList = new ArrayList<>();
        if (null != annualInspectionOverdueList && annualInspectionOverdueList.size() > 0) {
            inspectionOverdueList = workbenchMapper.getVehicleAnnualInspectionExpired(annualInspectionOverdueList, busiRegNumber);
        }
        PageInfo pageInfo = new PageInfo(annualInspectionOverdueList);
        pageInfo.setList(inspectionOverdueList);
        return pageInfo;
    }

    /**
     * 无车载终端
     *
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<VehicleNotTerminal> getNotCarTerminal(String busiregnumber, String companyId, int pageNo, int pageSize) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        //无终端的车辆
        List<String> norCarTerminalList = workbenchMapper.getValueByType(busiregnumber, WorkbenchStatusEnum.NOT_TERMINAL.getCode());
        List<VehicleNotTerminal> notTerminalList = new ArrayList<>();
        if (null != norCarTerminalList && norCarTerminalList.size() > 0) {
            notTerminalList = workbenchMapper.getNetcarBaseInfoNotTerminal(norCarTerminalList, busiregnumber);
        }
        PageInfo pageInfo = new PageInfo(norCarTerminalList);
        pageInfo.setList(notTerminalList);
        return pageInfo;
    }

    /**
     * 保险金额不合格
     *
     * @param busiRegNumber
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<VehicleInsurExpired> getInsuredAmountUnqualified(String busiRegNumber, int pageNo, int pageSize) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        List<String> insuredAmountUnqualifiedList = workbenchMapper.getValueByType(busiRegNumber, WorkbenchStatusEnum.INSURED_AMOUNT_UNQUALIFIED.getCode());
        List<VehicleInsurExpired> insuranceUnqualifiedList = new ArrayList<>();
        if (null != insuredAmountUnqualifiedList && insuredAmountUnqualifiedList.size() > 0) {
            insuranceUnqualifiedList = workbenchMapper.getNetcarBaseInfoByInsurance(insuredAmountUnqualifiedList, busiRegNumber);
        }
        PageInfo pageInfo = new PageInfo(insuredAmountUnqualifiedList);
        pageInfo.setList(insuranceUnqualifiedList);
        return pageInfo;
    }

    /**
     * 保险到期
     *
     * @param busiRegNumber
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<VehicleInsurExpired> getInsuranceDue(String busiRegNumber, int pageNo, int pageSize) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        List<String> insuranceIsDueList = workbenchMapper.getValueByType(busiRegNumber, WorkbenchStatusEnum.INSURANCE_DUE.getCode());
        List<VehicleInsurExpired> insuranceDueList = new ArrayList<>();
        if (null != insuranceIsDueList && insuranceIsDueList.size() > 0) {
            insuranceDueList = workbenchMapper.getNetcarBaseInfoByInsurance(insuranceIsDueList, busiRegNumber);
        }
        PageInfo pageInfo = new PageInfo(insuranceIsDueList);
        pageInfo.setList(insuranceDueList);
        return pageInfo;
    }

    /**
     * 里程超限
     *
     * @param busiRegNumber
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<VehicleMileageBeyond> getMileageTransfinite(String busiRegNumber, int pageNo, int pageSize) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        List<String> mileageList = workbenchMapper.getValueByType(busiRegNumber, WorkbenchStatusEnum.MILEAGE_TRANSFINITE.getCode());
        //里程超限车辆信息
        List<VehicleMileageBeyond> mileageTransfiniteList = new ArrayList<>();
        if (null != mileageList && mileageList.size() > 0) {
            mileageTransfiniteList = workbenchMapper.getNetcarBaseInfoByMile(mileageList, busiRegNumber);
        }
        PageInfo pageInfo = new PageInfo(mileageList);
        pageInfo.setList(mileageTransfiniteList);
        return pageInfo;
    }

    /**
     * 未运营
     *
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<VehicleNotOperating> getNotOperating(String busiRegNumber, String companyId, int pageNo, int pageSize) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        //未运营的车辆
        List<String> notOperatingList = workbenchMapper.getValueByType(busiRegNumber, WorkbenchStatusEnum.NOT_OPERATING.getCode());
        List<VehicleNotOperating> notOperatingVehicleList = new ArrayList<>();
        if (null != notOperatingList && notOperatingList.size() > 0) {
            notOperatingVehicleList = workbenchMapper.getYunZhengVehicleNotOperating(notOperatingList, busiRegNumber);
        }
        PageInfo pageInfo = new PageInfo(notOperatingList);
        pageInfo.setList(notOperatingVehicleList);
        return pageInfo;
    }

    /**
     * 数据缺失车辆
     *
     * @param busiRegNumber
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<VehicleInfoLack> getLackData(String busiRegNumber, String companyId, int pageNo, int pageSize) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        //运政多出来的数据
        List<String> redundantList = workbenchMapper.getValueByType(busiRegNumber, WorkbenchStatusEnum.LACK_VEHICLE_DATA.getCode());
        List<VehicleInfoLack> lackDataVehicleList = new ArrayList<>();
        if (null != redundantList && redundantList.size() > 0) {
            lackDataVehicleList = workbenchMapper.getYunZhengVehicleLack(redundantList, busiRegNumber);
        }
        PageInfo pageInfo = new PageInfo(redundantList);
        pageInfo.setList(lackDataVehicleList);
        return pageInfo;
    }


    /**
     * 获取保存车辆监管统计信息
     *
     * @throws Exception
     */
    public void saveWorkbenchVehicleDate() throws Exception {
        //删除旧的数据
        workbenchMapper.deleteWorkbenchVehicle();
        /**
         * 获取所有的运政企业
         */
        List<Map<String, Object>> enterpriseList = getEnterprise();

        if (null != enterpriseList && enterpriseList.size() > 0) {
            for (int i = 0; i < enterpriseList.size(); i++) {
                WorkbenchVehicle workbenchVehicle = new WorkbenchVehicle();

                String companyId = String.valueOf(enterpriseList.get(i).get("companyId"));
                String busiregnumber = String.valueOf(enterpriseList.get(i).get("busiregnumber"));
                String ownerName = String.valueOf(enterpriseList.get(i).get("clitname"));
                String clitname = String.valueOf(enterpriseList.get(i).get("personLiable"));
                String phone = String.valueOf(enterpriseList.get(i).get("phone"));

                //社会统一信用代码 busiregnumber
                workbenchVehicle.setBusiRegNumber(busiregnumber);
                //公司名称
                workbenchVehicle.setEnterpriseName(ownerName);
                //负责人
                workbenchVehicle.setHead(clitname);
                //联系电话
                workbenchVehicle.setPhone(phone);

                /**
                 * 部平台车辆数据缺失 ************************************************************************************
                 */
                int redundantTotal = 0;
                //根据企业获取运政的所有车辆
                List<String> yunZhengVehicleList = workbenchMapper.getYunZhengVehicleByBusiRegNumber(busiregnumber);
                //根据企业获取部级的所有车辆
                List<String> vehicleList = workbenchMapper.getVehicleByCompanyId(busiregnumber);
                //运政多出来的数据
                List<String> redundantList = new ArrayList<>();
                if (null == yunZhengVehicleList) {
                    workbenchVehicle.setLackData(0);
                } else {
                    redundantList = getDiffrent(yunZhengVehicleList, vehicleList);
                    //多出来的车辆数量
                    if (null != redundantList) {
                        redundantTotal = redundantList.size();
                        List<WorkbenchValue> workbenchValueArrayList = new ArrayList<>();
                        for (int k = 0; k < redundantList.size(); k++) {
                            WorkbenchValue workbenchValue = new WorkbenchValue();
                            workbenchValue.setBusiRegNumber(busiregnumber);
                            workbenchValue.setType(WorkbenchStatusEnum.LACK_VEHICLE_DATA.getCode());
                            workbenchValue.setValue(redundantList.get(k));
                            workbenchValueArrayList.add(workbenchValue);
                        }
                        if (workbenchValueArrayList.size() > 0) {
                            workbenchMapper.insertValue(workbenchValueArrayList);
                        }
                    }
                    workbenchVehicle.setLackData(redundantTotal);
                }


                /**
                 * 已取证未运营车辆数 ************************************************************************************
                 */
                int notOperatingTotal = 0;
                //查询企业的所有运营车辆
                List<String> operatingList = new ArrayList<>();
                operatingList = workbenchMapper.getOperatingVehicleByCompanyId(busiregnumber);
                //未运营的车辆数据
                List<String> notOperatingList = new ArrayList<>();
                if (null == yunZhengVehicleList) {
                    workbenchVehicle.setNotOperating(0);
                } else {
                    notOperatingList = getDiffrent(yunZhengVehicleList, operatingList);
                    //多出来的车辆数量
                    if (null != notOperatingList) {
                        notOperatingTotal = notOperatingList.size();
                        List<WorkbenchValue> workbenchValueArrayList = new ArrayList<>();
                        for (int k = 0; k < notOperatingList.size(); k++) {
                            WorkbenchValue workbenchValue = new WorkbenchValue();
                            workbenchValue.setBusiRegNumber(busiregnumber);
                            workbenchValue.setType(WorkbenchStatusEnum.NOT_OPERATING.getCode());
                            workbenchValue.setValue(notOperatingList.get(k));
                            workbenchValueArrayList.add(workbenchValue);
                        }
                        if (workbenchValueArrayList.size() > 0) {
                            workbenchMapper.insertValue(workbenchValueArrayList);
                        }
                    }
                    workbenchVehicle.setNotOperating(notOperatingTotal);
                }


                /**
                 * 车辆里程超限车辆数 ************************************************************************************
                 */
                int transfiniteTotal = 0;
                //查询里程表里所有里程超限的运政车辆数
                List<String> mileageList = new ArrayList<>();
                mileageList = workbenchMapper.getMileageTransfiniteByCompanyId(600000, busiregnumber);
                if (null != mileageList) {
                    transfiniteTotal = mileageList.size();
                    List<WorkbenchValue> workbenchValueArrayList = new ArrayList<>();
                    for (int k = 0; k < mileageList.size(); k++) {
                        WorkbenchValue workbenchValue = new WorkbenchValue();
                        workbenchValue.setBusiRegNumber(busiregnumber);
                        workbenchValue.setType(WorkbenchStatusEnum.MILEAGE_TRANSFINITE.getCode());
                        workbenchValue.setValue(mileageList.get(k));
                        workbenchValueArrayList.add(workbenchValue);
                    }
                    if (workbenchValueArrayList.size() > 0) {
                        workbenchMapper.insertValue(workbenchValueArrayList);
                    }
                }
                workbenchVehicle.setMileageTransfinite(transfiniteTotal);

                /**
                 * 保险到期车辆数 ****************************************************************************************
                 */
                int insuranceIsDueTotal = 0;
                List<String> insuranceIsDueList = new ArrayList<>();
                insuranceIsDueList = workbenchMapper.getInsuranceIsDueByCompanyId(DateStringUtil.dateToStr(new Date(), "yyyyMMdd"),
                        busiregnumber);
                if (null != insuranceIsDueList) {
                    insuranceIsDueTotal = insuranceIsDueList.size();
                    List<WorkbenchValue> workbenchValueArrayList = new ArrayList<>();
                    for (int k = 0; k < insuranceIsDueList.size(); k++) {
                        WorkbenchValue workbenchValue = new WorkbenchValue();
                        workbenchValue.setBusiRegNumber(busiregnumber);
                        workbenchValue.setType(WorkbenchStatusEnum.INSURANCE_DUE.getCode());
                        workbenchValue.setValue(insuranceIsDueList.get(k));
                        workbenchValueArrayList.add(workbenchValue);
                    }
                    if (workbenchValueArrayList.size() > 0) {
                        workbenchMapper.insertValue(workbenchValueArrayList);
                    }
                }
                workbenchVehicle.setInsuranceDue(insuranceIsDueTotal);
                /**
                 * 保险金额不合格 ****************************************************************************************
                 * 小于100万
                 */
                int insuredAmountUnqualifiedTotal = 0;
                List<String> insuredAmountUnqualifiedList = new ArrayList<>();
                insuredAmountUnqualifiedList = workbenchMapper.getInsuredAmountUnqualifiedByCompanyId(1000000.00,
                        busiregnumber);
                if (null != insuredAmountUnqualifiedList) {
                    insuredAmountUnqualifiedTotal = insuredAmountUnqualifiedList.size();
                    List<WorkbenchValue> workbenchValueArrayList = new ArrayList<>();
                    for (int k = 0; k < insuredAmountUnqualifiedList.size(); k++) {
                        WorkbenchValue workbenchValue = new WorkbenchValue();
                        workbenchValue.setBusiRegNumber(busiregnumber);
                        workbenchValue.setType(WorkbenchStatusEnum.INSURED_AMOUNT_UNQUALIFIED.getCode());
                        workbenchValue.setValue(insuredAmountUnqualifiedList.get(k));
                        workbenchValueArrayList.add(workbenchValue);
                    }
                    if (workbenchValueArrayList.size() > 0) {
                        workbenchMapper.insertValue(workbenchValueArrayList);
                    }
                }
                workbenchVehicle.setInsuredAmountUnqualified(insuredAmountUnqualifiedTotal);
                /**
                 * 无车载终端 *******************************************************************************************
                 */
                int notCarTerminalTotal = 0;
                List<String> norCarTerminalList = new ArrayList<>();
                //无终端的车辆
                norCarTerminalList = workbenchMapper.getNotCarTerminalByCompanyId(busiregnumber);
                if (null != norCarTerminalList) {
                    notCarTerminalTotal = norCarTerminalList.size();
                    List<WorkbenchValue> workbenchValueArrayList = new ArrayList<>();
                    for (int k = 0; k < norCarTerminalList.size(); k++) {
                        WorkbenchValue workbenchValue = new WorkbenchValue();
                        workbenchValue.setBusiRegNumber(busiregnumber);
                        workbenchValue.setType(WorkbenchStatusEnum.NOT_TERMINAL.getCode());
                        workbenchValue.setValue(norCarTerminalList.get(k));
                        workbenchValueArrayList.add(workbenchValue);
                    }
                    if (workbenchValueArrayList.size() > 0) {
                        workbenchMapper.insertValue(workbenchValueArrayList);
                    }
                }
                workbenchVehicle.setNotTerminal(notCarTerminalTotal);

                /**
                 * 年检过期的车辆数 **************************************************************************************
                 */
                int annualInspectionOverdueTotal = 0;
                List<String> annualInspectionOverdueList = new ArrayList<>();
                annualInspectionOverdueList = workbenchMapper.getAnnualInspectionOverdueByCompanyId(DateStringUtil.dateToStr(new Date(), "yyyyMMdd"),
                        busiregnumber);
                if (null != annualInspectionOverdueList) {
                    annualInspectionOverdueTotal = annualInspectionOverdueList.size();
                    List<WorkbenchValue> workbenchValueArrayList = new ArrayList<>();
                    for (int k = 0; k < annualInspectionOverdueList.size(); k++) {
                        WorkbenchValue workbenchValue = new WorkbenchValue();
                        workbenchValue.setBusiRegNumber(busiregnumber);
                        workbenchValue.setType(WorkbenchStatusEnum.ANNUAL_INSPECTION_OVERDUE.getCode());
                        workbenchValue.setValue(annualInspectionOverdueList.get(k));
                        workbenchValueArrayList.add(workbenchValue);
                    }
                    if (workbenchValueArrayList.size() > 0) {
                        workbenchMapper.insertValue(workbenchValueArrayList);
                    }
                }
                workbenchVehicle.setAnnualInspectionOverdue(annualInspectionOverdueTotal);
                /**
                 * 判断企业是否 180天未上车 ******************************************************************************
                 * 180天内没有订单
                 * 0；企业有订单数据产生
                 * 1：企业没有订单数据产生
                 */
                //得到一个Calendar的实例
                Calendar ca = Calendar.getInstance();
                //设置时间为当前时间
                ca.setTime(new Date());
                //日期减180天
                ca.add(Calendar.DATE, -180);
                Date lastDate = ca.getTime();

                StringBuilder builder = new StringBuilder();

                builder.append("SELECT sum(order_count) as order_count FROM netcar_statistics_operatePay " +
                        "WHERE TIME_FORMAT(__time,'yyyy-MM-dd') > '");
                builder.append(DateStringUtil.dateToStr(lastDate, "yyyy-MM-dd"));
                builder.append("'and COMPANY_ID in ('");
                builder.append(companyId).append("')");
                JSONArray jsonArray = httpUtils.doPostSqlUrl("sql", String.valueOf(builder));

                List<NetcarStatisticsOrder> operation180List = JSONObject.parseArray(jsonArray.toJSONString(), NetcarStatisticsOrder.class);

                if (operation180List.size() > 0 && operation180List.get(0).getOrder_count() > 0) {
                    workbenchVehicle.setNotBusiness180(0);
                } else {
                    workbenchVehicle.setNotBusiness180(1);
                }
                /**
                 * 判断企业是否 活跃不达标 *******************************************************************************
                 * 最近一个月内，订单少于100
                 * 1：是活跃企业
                 * 0：不是活跃企业
                 */
                //得到一个Calendar的实例
                Calendar ca1 = Calendar.getInstance();
                //设置时间为当前时间
                ca1.setTime(new Date());
                //日期减180天
                ca1.add(Calendar.DATE, -30);
                Date lastDate1 = ca1.getTime();
                StringBuilder month = new StringBuilder();

                month.append("SELECT sum(order_count) as order_count FROM netcar_statistics_operatePay " +
                        "WHERE TIME_FORMAT(__time,'yyyy-MM-dd') > '");
                month.append(DateStringUtil.dateToStr(lastDate1, "yyyy-MM-dd"));
                month.append("'and COMPANY_ID in ('");
                month.append(companyId).append("')");
                JSONArray monthList = httpUtils.doPostSqlUrl("sql", String.valueOf(builder));

                List<NetcarStatisticsOrder> operation30List = JSONObject.parseArray(monthList.toJSONString(), NetcarStatisticsOrder.class);

                if (operation30List.size() == 0 || operation30List.get(0).getOrder_count() < 100) {
                    workbenchVehicle.setNotActive(1);
                } else {
                    workbenchVehicle.setNotActive(0);
                }


                /**
                 * 获取数据详情,并生产excel文件 **************************************************************************
                 */
                //部平台车辆数据缺失的车辆信息
                List<VehicleInfoLack> lackDataVehicleList = new ArrayList<>();
                if (redundantTotal > 0) {
                    lackDataVehicleList = workbenchMapper.getYunZhengVehicleLack(redundantList, busiregnumber);
                }
                //已取证未运营的车辆信息
                List<VehicleNotOperating> notOperatingVehicleList = new ArrayList<>();
                if (notOperatingTotal > 0) {
                    notOperatingVehicleList = workbenchMapper.getYunZhengVehicleNotOperating(notOperatingList, busiregnumber);
                }
                //里程超限车辆信息
                List<VehicleMileageBeyond> mileageTransfiniteList = new ArrayList<>();
                if (transfiniteTotal > 0) {
                    mileageTransfiniteList = workbenchMapper.getNetcarBaseInfoByMile(mileageList, busiregnumber);
                }
                //保险到期数
                List<VehicleInsurExpired> insuranceDueList = new ArrayList<>();
                if (insuranceIsDueTotal > 0) {
                    insuranceDueList = workbenchMapper.getNetcarBaseInfoByInsurance(insuranceIsDueList, busiregnumber);
                }
                //保险金额不合格
                List<VehicleInsurExpired> insuranceUnqualifiedList = new ArrayList<>();
                if (insuredAmountUnqualifiedTotal > 0) {
                    insuranceUnqualifiedList = workbenchMapper.getNetcarBaseInfoByInsurance(insuredAmountUnqualifiedList, busiregnumber);
                }
                //无车载终端的车辆
                List<VehicleNotTerminal> notTerminalList = new ArrayList<>();
                if (notCarTerminalTotal > 0) {
                    notTerminalList = workbenchMapper.getNetcarBaseInfoNotTerminal(norCarTerminalList, busiregnumber);
                }
                //年检过期车辆信息
                List<VehicleAnnualInspectionExpired> inspectionOverdueList = new ArrayList<>();
                if (annualInspectionOverdueTotal > 0) {
                    inspectionOverdueList = workbenchMapper.getVehicleAnnualInspectionExpired(annualInspectionOverdueList, busiregnumber);
                }
                /**
                 * 生成excel
                 */
                XSSFWorkbook workbook = new XSSFWorkbook();
                //创建工作表1
                excelUtil.createExcelSheet(workbook, inspectionOverdueList, "年检过期", VehicleAnnualInspectionExpired.class);
                excelUtil.createExcelSheet(workbook, notTerminalList, "无车载终端", VehicleNotTerminal.class);
                excelUtil.createExcelSheet(workbook, insuranceUnqualifiedList, "保险金额不合格", VehicleInsurExpired.class);
                excelUtil.createExcelSheet(workbook, insuranceDueList, "保险已到期", VehicleInsurExpired.class);
                excelUtil.createExcelSheet(workbook, mileageTransfiniteList, "车辆里程已经限额", VehicleMileageBeyond.class);
                excelUtil.createExcelSheet(workbook, notOperatingVehicleList, "已取证未运营", VehicleNotOperating.class);
                excelUtil.createExcelSheet(workbook, lackDataVehicleList, "部平台车辆数据缺失", VehicleInfoLack.class);


                ownerName = StringUtils.replaceChars(ownerName, "/\\:*\"<>|?", "");
                String excelFileName = ownerName + "车辆信息-" + DateStringUtil.dateToStr(new Date(), "yyyyMMdd") + ".xls";
                ByteArrayOutputStream ops = new ByteArrayOutputStream();
                try {
                    workbook.write(ops);
                    System.out.println("导出成功!");
                } catch (Exception e) {
                    System.out.println("导出失败!");
                    e.printStackTrace();
                }
                byte[] b =ops.toByteArray();
                ByteArrayInputStream in = new ByteArrayInputStream(b);
                String exportFileName = fastDFSUtil.uploadFile(in, excelFileName);
                workbenchVehicle.setExcelUrl(exportFileName);
                /**
                 * 保存数据概要 *****************************************************************************************
                 */
                //保存新的数据
                workbenchMapper.saveWorkbenchVehicle(workbenchVehicle);

            }
        }
    }

    public void changeStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] bs = new byte[]{1, 2, 3, 4, 5};
        out.write(bs);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        byte[] bs1 = new byte[1024];
        int len = in.read(bs1);
        for (int i = 0; i < len; i++) {
            System.out.println(bs[i]);
        }
    }


    /**
     * 驾照过期
     *
     * @param busiRegNumber
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<DriverLicenseExpired> getLicenseOverdue(String busiRegNumber, int pageNo, int pageSize) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        List<String> licenseOverdueList = workbenchMapper.getValueByType(busiRegNumber, WorkbenchStatusEnum.LICENSE_OVERDUE.getCode());
        List<DriverLicenseExpired> licenseOverdueInfoList = new ArrayList<>();
        if (null != licenseOverdueList && licenseOverdueList.size() > 0) {
            licenseOverdueInfoList = workbenchMapper.getNetcarBaseInfoDriverExpired(licenseOverdueList, busiRegNumber);
        }
        PageInfo pageInfo = new PageInfo(licenseOverdueList);
        pageInfo.setList(licenseOverdueInfoList);
        return pageInfo;
    }

    /**
     * 年龄超标
     *
     * @param busiRegNumber
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo<DriverAgeBeyond> getAgeExcessive(String busiRegNumber, int pageNo, int pageSize) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        List<String> ageExcessiveList = workbenchMapper.getValueByType(busiRegNumber, WorkbenchStatusEnum.AGE_EXCESSIVE.getCode());
        List<DriverAgeBeyond> ageExcessiveInfoList = new ArrayList<>();
        if (null != ageExcessiveList && ageExcessiveList.size() > 0) {
            ageExcessiveInfoList = workbenchMapper.getNetcarBaseInfoDriverAgeBeyond(ageExcessiveList, busiRegNumber);
        }
        PageInfo pageInfo = new PageInfo(ageExcessiveList);
        pageInfo.setList(ageExcessiveInfoList);
        return pageInfo;
    }

    /**
     * 持证未上岗
     *
     * @param busiRegNumber
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo<DriverNotOperating> getNotMountGuard(String busiRegNumber, String companyId, int pageNo, int pageSize) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        //所有驾驶员减去订单表的驾驶员
        List<String> notMountGuardList = workbenchMapper.getValueByType(busiRegNumber, WorkbenchStatusEnum.NOT_MOUNT_GUARD.getCode());
        List<DriverNotOperating> notMountGuardInfoList = new ArrayList<>();
        if (null != notMountGuardList && notMountGuardList.size() > 0) {
            notMountGuardInfoList = workbenchMapper.getYunZhengDriverNotOperating(notMountGuardList, busiRegNumber);
        }
        PageInfo pageInfo = new PageInfo(notMountGuardList);
        pageInfo.setList(notMountGuardInfoList);
        return pageInfo;
    }

    /**
     * 数据缺失
     *
     * @param busiRegNumber
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<DriverInfoLack> getDriverLackData(String busiRegNumber, String companyId, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        //部级缺失的驾驶员
        List<String> lackDataList = workbenchMapper.getValueByType(busiRegNumber, WorkbenchStatusEnum.LACK_DRIVER_DATA.getCode());
        List<DriverInfoLack> lackDataInfoList = new ArrayList<>();
        if (null != lackDataList && lackDataList.size() > 0) {
            lackDataInfoList = workbenchMapper.getYunZhengDriverLack(lackDataList, busiRegNumber);
        }
        PageInfo pageInfo = new PageInfo(lackDataList);
        pageInfo.setList(lackDataInfoList);
        return pageInfo;
    }

    /**
     * 更新数据
     *
     * @throws Exception
     *
     */
    @Override
    public void update() throws Exception {
        System.out.println("接口启动------------------------------------------------");
        workbenchMapper.deleteValue();
        saveWorkbenchVehicleDate();
        saveWorkbenchDriverDate();
    }


    /**
     * 获取保存驾驶员监管统计信息
     *
     * @throws Exception
     */
    public void saveWorkbenchDriverDate() throws Exception {

        //删除旧的统计信息
        workbenchMapper.deleteWorkbenchDriver();

        /**
         * 获取所有的运政企业
         */
        List<Map<String, Object>> enterpriseList = new ArrayList<>();
        enterpriseList = getEnterprise();

        if (null != enterpriseList && enterpriseList.size() > 0) {
            for (int i = 0; i < enterpriseList.size(); i++) {

                String companyId = String.valueOf(enterpriseList.get(i).get("companyId"));
                String busiregnumber = String.valueOf(enterpriseList.get(i).get("busiregnumber"));
                String ownerName = String.valueOf(enterpriseList.get(i).get("clitname"));
                String clitname = String.valueOf(enterpriseList.get(i).get("personLiable"));
                String phone = String.valueOf(enterpriseList.get(i).get("phone"));


                WorkbenchDriver workbenchDriver = new WorkbenchDriver();
                //社会统一信用代码 busiregnumber
                workbenchDriver.setBusiRegNumber(busiregnumber);
                //公司名称
                workbenchDriver.setEnterpriseName(ownerName);
                //负责人
                workbenchDriver.setHead(clitname);
                //联系电话
                workbenchDriver.setPhone(phone);

                /**
                 * 驾照过期 *********************************************************************************************
                 */
                int licenseOverdue = 0;
                List<String> licenseOverdueList = new ArrayList<>();
                licenseOverdueList = workbenchMapper.getLicenseOverdueByCompanyId(busiregnumber, DateStringUtil.dateToStr(new Date(), "yyyyMMdd"));
                if (null != licenseOverdueList) {
                    licenseOverdue = licenseOverdueList.size();
                    List<WorkbenchValue> workbenchValueArrayList = new ArrayList<>();
                    for (int k = 0; k < licenseOverdueList.size(); k++) {
                        WorkbenchValue workbenchValue = new WorkbenchValue();
                        workbenchValue.setBusiRegNumber(busiregnumber);
                        workbenchValue.setType(WorkbenchStatusEnum.LICENSE_OVERDUE.getCode());
                        workbenchValue.setValue(licenseOverdueList.get(k));
                        workbenchValueArrayList.add(workbenchValue);
                    }
                    if (workbenchValueArrayList.size() > 0) {
                        workbenchMapper.insertValue(workbenchValueArrayList);
                    }
                }
                workbenchDriver.setLicenseOverdue(licenseOverdue);
                /**
                 * 年龄超大 *********************************************************************************************
                 */
                int ageExcessive = 0;
                //得到一个Calendar的实例
                Calendar ca = Calendar.getInstance();
                //设置时间为当前时间
                ca.setTime(new Date());
                //年份减45
                ca.add(Calendar.YEAR, -45);
                Date lastDate = ca.getTime();
                List<String> ageExcessiveList = new ArrayList<>();
                ageExcessiveList = workbenchMapper.getAgeExcessiveList(busiregnumber,
                        DateStringUtil.dateToStr(lastDate, "yyyy-MM-dd"));
                if (null != ageExcessiveList) {
                    ageExcessive = ageExcessiveList.size();
                    List<WorkbenchValue> workbenchValueArrayList = new ArrayList<>();
                    for (int k = 0; k < ageExcessiveList.size(); k++) {
                        WorkbenchValue workbenchValue = new WorkbenchValue();
                        workbenchValue.setBusiRegNumber(busiregnumber);
                        workbenchValue.setType(WorkbenchStatusEnum.AGE_EXCESSIVE.getCode());
                        workbenchValue.setValue(ageExcessiveList.get(k));
                        workbenchValueArrayList.add(workbenchValue);
                    }
                    if (workbenchValueArrayList.size() > 0) {
                        workbenchMapper.insertValue(workbenchValueArrayList);
                    }
                }
                workbenchDriver.setAgeExcessive(ageExcessive);
                /**
                 * 持证未上岗 *******************************************************************************************
                 */
                //查询该公司所有的驾驶员
                //该公司所有运政驾驶人员
                int notMountGuard = 0;
                List<String> yunzhengDriverList = workbenchMapper.getYunzhengDriver(busiregnumber);
                //查询该公司订单表的驾驶员
                List<String> orderDriverList = workbenchMapper.getOrderDriver(busiregnumber);
                //所有驾驶员减去订单表的驾驶员
                List<String> notMountGuardList = new ArrayList<>();
                if (null == yunzhengDriverList) {
                    workbenchDriver.setNotMountGuard(0);
                } else {
                    notMountGuardList = getDiffrent(yunzhengDriverList, orderDriverList);
                    if (null != notMountGuardList) {
                        notMountGuard = notMountGuardList.size();
                        List<WorkbenchValue> workbenchValueArrayList = new ArrayList<>();
                        for (int k = 0; k < notMountGuardList.size(); k++) {
                            WorkbenchValue workbenchValue = new WorkbenchValue();
                            workbenchValue.setBusiRegNumber(busiregnumber);
                            workbenchValue.setType(WorkbenchStatusEnum.NOT_MOUNT_GUARD.getCode());
                            workbenchValue.setValue(notMountGuardList.get(k));
                            workbenchValueArrayList.add(workbenchValue);
                        }
                        if (workbenchValueArrayList.size() > 0) {
                            workbenchMapper.insertValue(workbenchValueArrayList);
                        }
                    }
                    workbenchDriver.setNotMountGuard(notMountGuard);
                }

                /**
                 * 部平台驾驶员信息缺失 **********************************************************************************
                 */
                int lackData = 0;

                //该公司所有的部级驾驶人员
                List<String> fullYunzhengDriverList = workbenchMapper.selectAllYunzhengDriverList(busiregnumber);
                List<String> driverList = workbenchMapper.getNetCarDriver(busiregnumber);
                List<String> lackDataList = new ArrayList<>();
                //部级缺失的驾驶员
                if (null == fullYunzhengDriverList) {
                    workbenchDriver.setLackData(0);
                } else {
                    lackDataList = getDiffrent(fullYunzhengDriverList, driverList);
                    if (null != lackDataList) {
                        lackData = lackDataList.size();
                        List<WorkbenchValue> workbenchValueArrayList = new ArrayList<>();
                        for (int k = 0; k < lackDataList.size(); k++) {
                            WorkbenchValue workbenchValue = new WorkbenchValue();
                            workbenchValue.setBusiRegNumber(busiregnumber);
                            workbenchValue.setType(WorkbenchStatusEnum.LACK_DRIVER_DATA.getCode());
                            workbenchValue.setValue(lackDataList.get(k));
                            workbenchValueArrayList.add(workbenchValue);
                        }
                        if (workbenchValueArrayList.size() > 0) {
                            workbenchMapper.insertValue(workbenchValueArrayList);
                        }
                    }
                    workbenchDriver.setLackData(lackData);
                }

                /**
                 * 获取数据详情,并生产excel文件 **************************************************************************
                 */
                //驾照过期
                List<DriverLicenseExpired> licenseOverdueInfoList = new ArrayList<>();
                if (licenseOverdue > 0) {
                    licenseOverdueInfoList = workbenchMapper.getNetcarBaseInfoDriverExpired(licenseOverdueList, busiregnumber);
                }
                //年龄超大
                List<DriverAgeBeyond> ageExcessiveInfoList = new ArrayList<>();
                if (ageExcessive > 0) {
                    ageExcessiveInfoList = workbenchMapper.getNetcarBaseInfoDriverAgeBeyond(ageExcessiveList, busiregnumber);
                }
                //持证未上岗
                List<DriverNotOperating> notMountGuardInfoList = new ArrayList<>();
                if (notMountGuard > 0) {
                    notMountGuardInfoList = workbenchMapper.getYunZhengDriverNotOperating(notMountGuardList, busiregnumber);
                }
                //部平台驾驶员信息缺失
                List<DriverInfoLack> lackDataInfoList = new ArrayList<>();
                if (lackData > 0) {
                    lackDataInfoList = workbenchMapper.getYunZhengDriverLack(lackDataList, busiregnumber);
                }

                /**
                 * 生成excel
                 */
                //生成一个工作簿
                XSSFWorkbook workbook = new XSSFWorkbook();
                //创建工作表1
                excelUtil.createExcelSheet(workbook, licenseOverdueInfoList, "驾照过期", DriverLicenseExpired.class);
                excelUtil.createExcelSheet(workbook, ageExcessiveInfoList, "年龄超大", DriverAgeBeyond.class);
                excelUtil.createExcelSheet(workbook, notMountGuardInfoList, "持证未上岗", DriverNotOperating.class);
                excelUtil.createExcelSheet(workbook, lackDataInfoList, "部平台驾驶员信息缺失", DriverInfoLack.class);

                ownerName = StringUtils.replaceChars(ownerName, "/\\:*\"<>|?", "");
                String excelFileName = ownerName + "驾驶员信息-" + DateStringUtil.dateToStr(new Date(), "yyyyMMdd") + ".xls";

                ByteArrayOutputStream ops = new ByteArrayOutputStream();
                try {
                    workbook.write(ops);
                    System.out.println("导出成功!");
                } catch (Exception e) {
                    System.out.println("导出失败!");
                    e.printStackTrace();
                }
                byte[] b =ops.toByteArray();
                ByteArrayInputStream in = new ByteArrayInputStream(b);
                String exportFileName = fastDFSUtil.uploadFile(in, excelFileName);
                workbenchDriver.setExcelUrl(exportFileName);
                //插入统计数据
                workbenchMapper.saveWorkbenchDriver(workbenchDriver);

            }
        }
    }


    /**
     * 获取运政企业信息
     *
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getEnterprise() throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        list = workbenchMapper.getEnterprise();
        return list;
    }


    /**
     * 拿出list1比list2 多出来的数据
     *
     * @param list1
     * @param list2
     * @return
     */
    public static List<String> getDiffrent(List<String> list1, List<String> list2) {
        List<String> diff = list1.stream().filter(data -> !list2.contains(data)).collect(Collectors.toList());
        return diff;
    }

}
