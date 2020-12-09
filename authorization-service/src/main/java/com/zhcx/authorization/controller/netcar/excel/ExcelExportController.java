package com.zhcx.authorization.controller.netcar.excel;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.zhcx.authorization.utils.DateUtil;
import com.zhcx.basicdata.facade.base.BaseDictionaryService;
import com.zhcx.netcarbasic.facade.netcar.CompanyService;
import com.zhcx.netcarbasic.facade.netcar.DriverService;
import com.zhcx.netcarbasic.facade.netcar.VehicleService;
import com.zhcx.netcarbasic.pojo.excel.CompanyExcel;
import com.zhcx.netcarbasic.pojo.excel.DriverExcel;
import com.zhcx.netcarbasic.pojo.excel.VehileExcel;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoCompany;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoDriver;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoVehicle;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/7/1 0001 16:56
 **/

@RestController
@RequestMapping("/netcar/excel")
public class ExcelExportController {
    private static final Logger logger = LoggerFactory.getLogger(ExcelExportController.class);

    @Autowired
    private CompanyService companyService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private ExcelUtil excelUtil;

    @Autowired
    private VehicleService vehicleService;

    @Resource
    private BaseDictionaryService dictionaryService;

    @GetMapping("/company")
    public void getCompanyExcel(@RequestParam(required = false) String companyId,
                                @RequestParam(defaultValue = "1") Integer pageNo,
                                @RequestParam(defaultValue = "100000") Integer pageSize,
                                @RequestParam(required = false, defaultValue = "update_time_desc") String orderBy,
                                HttpServletResponse response) {
        try {
            List<CompanyExcel> list = companyService.selectCompanyExcelList(companyId, pageNo, pageSize, orderBy).getList();
            Map<String, String> map = dictionaryService.selectDictionaryByCode("JJLX");
            list.forEach(baseInfoCompany -> {
                String economicType = map.get(baseInfoCompany.getEconomicType());
                baseInfoCompany.setEconomicType(StringUtils.isBlank(economicType) ? baseInfoCompany.getEconomicType() : economicType);
            });

            //生成一个工作簿
            XSSFWorkbook workbook = new XSSFWorkbook();
            //创建工作表1
            excelUtil.createExcelSheet(workbook, list, "sheet1", CompanyExcel.class);
            String fileName = "公司基本信息-" + DateUtil.getDateFormatYmd(new Date()) + ".xlsx";
            excelUtil.writeExcelResponse(response, fileName, workbook);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("excel文件下载出现异常", e);
        }
    }


    @GetMapping("/driver")
    public void getDriverExcel(@RequestParam(required = false) String companyId,
                               @RequestParam(defaultValue = "1") Integer pageNo,
                               @RequestParam(defaultValue = "100000") Integer pageSize,
                               @RequestParam(required = false) String orderBy,
                               HttpServletResponse response) {

        try {
            List<DriverExcel> list = driverService.selectDriverExcelList(companyId, null, null, pageNo, pageSize, orderBy).getList();
            Map<String, String> nationMap = dictionaryService.selectDictionaryByCode("MZ");
            Map<String, String> genderMap = dictionaryService.selectDictionaryByCode("XB");
            Map<String, String> commercialMap = Maps.newHashMap();
            commercialMap.put("1", "网络预约出租汽车");
            commercialMap.put("2", "巡游出租汽车");
            commercialMap.put("3", "私人小客车合乘");
            list.forEach(baseInfoDriver -> {
                String driverNation = nationMap.get(baseInfoDriver.getDriverNation());
                String driverGender = genderMap.get(baseInfoDriver.getDriverGender());
                String commercialType = commercialMap.get(baseInfoDriver.getCommercialType());
                baseInfoDriver.setDriverNation(StringUtils.isBlank(driverNation) ? baseInfoDriver.getDriverNation() : driverNation);
                baseInfoDriver.setDriverGender(StringUtils.isBlank(driverGender) ? baseInfoDriver.getDriverGender() : driverGender);
                baseInfoDriver.setTaxiDriver(StringUtils.equals(baseInfoDriver.getTaxiDriver(), "0") ? "否" : "是");
                baseInfoDriver.setFullTimeDriver(StringUtils.equals(baseInfoDriver.getFullTimeDriver(), "0") ? "否" : "是");
                baseInfoDriver.setInDriverBlacklist(StringUtils.equals(baseInfoDriver.getInDriverBlacklist(), "0") ? "否" : "是");
                baseInfoDriver.setCommercialType(StringUtils.isBlank(commercialType) ? baseInfoDriver.getCommercialType() : commercialType);
                baseInfoDriver.setState(StringUtils.equals(baseInfoDriver.getState(), "0") ? "有效" : "无效");
            });
            //生成一个工作簿
            XSSFWorkbook workbook = new XSSFWorkbook();
            //创建工作表1
            excelUtil.createExcelSheet(workbook, list, "sheet1", DriverExcel.class);
            String fileName = "驾驶员基本信息-" + DateUtil.getDateFormatYmd(new Date()) + ".xlsx";
            excelUtil.writeExcelResponse(response, fileName, workbook);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("excel文件下载出现异常", e);
        }
    }


    /**
     * 违规企业数据Excel下载
     *
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @param response
     */
    @GetMapping("/illegal/company")
    public void getIllegalCompanyExcel(@RequestParam(required = false) String companyId,
                                       @RequestParam(defaultValue = "1") Integer pageNo,
                                       @RequestParam(defaultValue = "100000") Integer pageSize,
                                       @RequestParam(required = false, defaultValue = "update_time_desc") String orderBy,
                                       HttpServletResponse response) {
        try {

            PageInfo<NetcarBaseInfoCompany> pageInfo = vehicleService.getCompanyList(companyId, pageNo, pageSize, orderBy);
            List<CompanyExcel> list = new ArrayList<>();
            pageInfo.getList().forEach(company -> {
                CompanyExcel companyExcel = new CompanyExcel();
                BeanUtils.copyProperties(company, companyExcel);
                list.add(companyExcel);
            });
            Map<String, String> map = dictionaryService.selectDictionaryByCode("JJLX");
            list.forEach(baseInfoCompany -> {
                String economicType = map.get(baseInfoCompany.getEconomicType());
                baseInfoCompany.setEconomicType(StringUtils.isBlank(economicType) ? baseInfoCompany.getEconomicType() : economicType);
            });

            //生成一个工作簿
            XSSFWorkbook workbook = new XSSFWorkbook();
            //创建工作表1
            excelUtil.createExcelSheet(workbook, list, "sheet1", CompanyExcel.class);
            String fileName = "违规公司信息-" + DateUtil.getDateFormatYmd(new Date()) + ".xlsx";
            excelUtil.writeExcelResponse(response, fileName, workbook);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("excel文件下载出现异常", e);
        }
    }

    /**
     * 违规驾驶员数据Excel下载
     *
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @param response
     */
    @GetMapping("/illegal/driver")
    public void getIllegalDriverExcel(@RequestParam(required = false) String companyId,
                                      @RequestParam(required = false) String keyword,
                                      @RequestParam(required = false) String type,
                                      @RequestParam(defaultValue = "1") Integer pageNo,
                                      @RequestParam(defaultValue = "100000") Integer pageSize,
                                      @RequestParam(required = false) String orderBy,
                                      HttpServletResponse response) {

        try {
            PageInfo<NetcarBaseInfoDriver> pageInfo = driverService.getDriverIllegal(keyword, type, companyId, pageNo, pageSize, orderBy);
            List<DriverExcel> list = new ArrayList<>();
            pageInfo.getList().forEach(driver -> {
                DriverExcel driverExcel = new DriverExcel();
                BeanUtils.copyProperties(driver, driverExcel);
                list.add(driverExcel);
            });
            Map<String, String> nationMap = dictionaryService.selectDictionaryByCode("MZ");
            Map<String, String> genderMap = dictionaryService.selectDictionaryByCode("XB");
            Map<String, String> commercialMap = Maps.newHashMap();
            commercialMap.put("1", "网络预约出租汽车");
            commercialMap.put("2", "巡游出租汽车");
            commercialMap.put("3", "私人小客车合乘");
            list.forEach(baseInfoDriver -> {
                String driverNation = nationMap.get(baseInfoDriver.getDriverNation());
                String driverGender = genderMap.get(baseInfoDriver.getDriverGender());
                String commercialType = commercialMap.get(baseInfoDriver.getCommercialType());
                baseInfoDriver.setDriverNation(StringUtils.isBlank(driverNation) ? baseInfoDriver.getDriverNation() : driverNation);
                baseInfoDriver.setDriverGender(StringUtils.isBlank(driverGender) ? baseInfoDriver.getDriverGender() : driverGender);
                baseInfoDriver.setTaxiDriver(StringUtils.equals(baseInfoDriver.getTaxiDriver(), "0") ? "否" : "是");
                baseInfoDriver.setFullTimeDriver(StringUtils.equals(baseInfoDriver.getFullTimeDriver(), "0") ? "否" : "是");
                baseInfoDriver.setInDriverBlacklist(StringUtils.equals(baseInfoDriver.getInDriverBlacklist(), "0") ? "否" : "是");
                baseInfoDriver.setCommercialType(StringUtils.isBlank(commercialType) ? baseInfoDriver.getCommercialType() : commercialType);
                baseInfoDriver.setState(StringUtils.equals(baseInfoDriver.getState(), "0") ? "有效" : "无效");
            });
            //生成一个工作簿
            XSSFWorkbook workbook = new XSSFWorkbook();
            //创建工作表1
            excelUtil.createExcelSheet(workbook, list, "sheet1", DriverExcel.class);
            String fileName = "违规驾驶员信息-" + DateUtil.getDateFormatYmd(new Date()) + ".xlsx";
            excelUtil.writeExcelResponse(response, fileName, workbook);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("excel文件下载出现异常", e);
        }
    }

    /**
     * 违规车辆数据Excel下载
     *
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @param response
     */
    @GetMapping("/illegal/vehile")
    public void getIllegalVehileExcel(@RequestParam(required = false) String companyId,
                                      @RequestParam(required = false) String vehicleNo,
                                      @RequestParam(defaultValue = "1") Integer pageNo,
                                      @RequestParam(defaultValue = "100000") Integer pageSize,
                                      @RequestParam(required = false, defaultValue = "update_time_desc") String orderBy,
                                      HttpServletResponse response) {
        try {

            PageInfo<NetcarBaseInfoVehicle> pageInfo = vehicleService.getVehilceIllegal(companyId, vehicleNo, pageNo, pageSize, orderBy);
            List<VehileExcel> list = new ArrayList<>();
            pageInfo.getList().forEach(company -> {
                VehileExcel vehileExcel = new VehileExcel();
                BeanUtils.copyProperties(company, vehileExcel);
                list.add(vehileExcel);
            });
            Map<String, String> YZCPYS = dictionaryService.selectDictionaryByCode("YZCPYS");
            Map<String, String> RLLX = dictionaryService.selectDictionaryByCode("RLLX");
            Map<String, String> CLLX = dictionaryService.selectDictionaryByCode("CLLX");
            list.forEach(vehileExcel -> {
                if (null != YZCPYS)
                    vehileExcel.setPlateColor(YZCPYS.get(vehileExcel.getPlateColor()));
                if (null != RLLX)
                    vehileExcel.setFuelType(RLLX.get(vehileExcel.getFuelType()));
                if (null != CLLX)
                    vehileExcel.setVehicleType(CLLX.get(vehileExcel.getVehicleType()));
                vehileExcel.setFixState(StringUtils.equals(vehileExcel.getFixState(), "0") ? "未年检" : "已年检");
            });

            //生成一个工作簿
            XSSFWorkbook workbook = new XSSFWorkbook();
            //创建工作表1
            excelUtil.createExcelSheet(workbook, list, "sheet1", VehileExcel.class);
            String fileName = "违规车辆信息-" + DateUtil.getDateFormatYmd(new Date()) + ".xlsx";
            excelUtil.writeExcelResponse(response, fileName, workbook);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("excel文件下载出现异常", e);
        }
    }

}
