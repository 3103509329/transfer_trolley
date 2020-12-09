package com.zhcx.authorization.controller.taxi;

import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.BeanUtils;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.excel.ExcelReader;
import com.zhcx.authorization.utils.excel.ExcelUtils;
import com.zhcx.authorization.utils.excel.ExportExcel;
import com.zhcx.authorization.utils.excel.ImportExcel;
import com.zhcx.basicdata.facade.taxi.KafkaResultTaxiOnofflineService;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoCompanyService;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoDriverService;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoVehicleService;
import com.zhcx.basicdata.pojo.taxi.*;
import com.zhcx.common.util.UUIDUtils;
import com.zhcx.authorization.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @ClassName：驾驶员导入
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author：tangding
 * @date：2019年3月4日
 */
@Controller
@RequestMapping("/taxi/vehicleExt")
public class VehicleImportExportController {

    private static final Logger logger = LoggerFactory.getLogger(VehicleImportExportController.class);

    @Autowired
    private TaxiBaseInfoVehicleService vehicleService;

    @Autowired
    private TaxiBaseInfoDriverService driverService;

    @Autowired
    private TaxiBaseInfoCompanyService taxiBaseInfoCompanyService;

    @Autowired
    private KafkaResultTaxiOnofflineService onofflineService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Resource(name = "redisTemplate4Json")
    private RedisTemplate<String, List<TaxiBaseInfoVehicleExtend>> redisTemplate;

    @Resource
    private UUIDUtils uuidUtils;

    /**
     * @param @param  file
     * @param @return
     * @return MessageResp
     * @throws
     * @Title: importDate
     * @Description: 导入数据
     */
    @ResponseBody
    @RequestMapping("/importExcelData")
    public MessageResp importExcelData(@RequestParam MultipartFile file, HttpServletRequest request, String handleType, String corpId1) {
        long start = System.currentTimeMillis();
        MessageResp resp = new MessageResp();
        AuthUserResp user = sessionHandler.getUser(request);
        Long corpId = null;
        //非管理员用户只能够查看自己企业下所属车辆
        if (!Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())) {
            if (user.getCorpId() != null && user.getCorpId() != 0L) {
                corpId = user.getCorpId();
            }
        } else {
            corpId = Long.valueOf(corpId1);
        }

        ImportExcel<TaxiBaseInfoVehicleExtend> ie = new ImportExcel(TaxiBaseInfoVehicleExtend.class);
        // 验证导入的表格是否符合
        resp = ExcelReader.validateImportExcel(file);
        if ("false".equals(resp.getResult())) {
            return resp;
        }
        //String xlsname = Constants.EXCLENAME_DRIVING;
        // 验证标题头
        //resp = ExcelReader.validateExcelHeader(xlsname, file);
        List<TaxiBaseInfoVehicleExtend> vehicleExtends = new ArrayList<TaxiBaseInfoVehicleExtend>();
        List<TaxiBaseInfoVehicle> vehicles = new ArrayList<TaxiBaseInfoVehicle>();
        List<TaxiDriverVehicle> taxiDriverVehicles = new ArrayList<TaxiDriverVehicle>();
        List<TaxiVehicleAdditionalRelation> relations = new ArrayList<TaxiVehicleAdditionalRelation>();

        TaxiBaseInfoVehicleExtend vehicleExtend = new TaxiBaseInfoVehicleExtend();
        vehicleExtends = (List<TaxiBaseInfoVehicleExtend>) ie.importExcel(file, "yyyy-MM-dd HH:mm:ss");
        int cbList_size = vehicleExtends == null || vehicleExtends.size() <= 0 ? 0 : vehicleExtends.size();
        if (cbList_size <= 0) {
            resp.setResult("false");
            resp.setStatusCode("-50");
            resp.setResultDesc("上传的文件内容不能为空");
            return resp;
        }

        //全量查出部分必要验证字段
        List<TaxiBaseInfoVehicle> data = vehicleService.queryAllCheckData();
        //获取企业信息
        Map<String, Object> corpInfo = taxiBaseInfoCompanyService.queryCityAndDisByCompany(corpId + "");
        corpInfo.put("corpId", corpId);
        Set<String> plateNumber = new HashSet<String>();
        Set<String> vin = new HashSet<String>();
        Set<String> engineNumber = new HashSet<>();
        Set<String> transportationCertificateNumber = new HashSet<>();
        for (int i = 0; i < cbList_size; i++) {
            vehicleExtend = (TaxiBaseInfoVehicleExtend) vehicleExtends.get(i);
            // 验证并处理excle数据
            vehicleExtend = processExcelData(vehicleExtend, plateNumber, vin, engineNumber, transportationCertificateNumber, data, corpInfo, user.getUserId(), handleType, corpId);
            if ("yes".equals(vehicleExtend.getIfCanSave())) {
                plateNumber.add(vehicleExtend.getPlateNumber());
                vin.add(vehicleExtend.getVinRecognitionCode());
                engineNumber.add(vehicleExtend.getEngineNumber());
                transportationCertificateNumber.add(vehicleExtend.getTransportationCertificateNumber());
                vehicles.add(vehicleExtend.getTaxiBaseInfoVehicle());
                if (null != vehicleExtend.getTaxiDriverVehicles()) {
                    if (vehicleExtend.getTaxiDriverVehicles().size() > 0) {
                        taxiDriverVehicles.addAll(vehicleExtend.getTaxiDriverVehicles());
                    }
                }
                if (null != vehicleExtend.getRelations()) {
                    relations.addAll(vehicleExtend.getRelations());
                }
            }
        }

        try {
            if ("add".equals(handleType)) {
                if (vehicles.size() > 0) {
                    vehicleService.saveBatch(vehicles, taxiDriverVehicles, relations);
                }
            }
            String key = "vehicle_export" + corpId + "_" + (new Date()).getTime();
            redisTemplate.opsForValue().set(key, vehicleExtends, 30, TimeUnit.MINUTES);
            String resultDesc = "批量导入车辆数据,成功" + vehicles.size() + "条,失败" + (cbList_size - vehicles.size()) + "条;其中批量导入车辆与驾驶员信息的,成功" + taxiDriverVehicles.size() + "条;";
            resp.setResultDesc(resultDesc);
            resp.setData(key);
            resp.setResult("true");
            long endTime = System.currentTimeMillis();
            logger.debug("---------耗时----------" + (endTime - start) + "--------------------------------");
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.setResult("false");
            resp.setResultDesc("导入异常");
            return resp;
        }

    }


    public TaxiBaseInfoVehicleExtend processExcelData(TaxiBaseInfoVehicleExtend vehicleExtend, Set<String> plateNumber, Set<String> vin, Set<String> engineNumber,
                                                      Set<String> transportationCertificateNumber, List<TaxiBaseInfoVehicle> data, Map<String, Object> corpInfo, Long userUuid,
                                                      String handleType, Long currentCorpId) {
        String msg = "";
        //计量绑定驾驶员的状态变量
        String status = "04";
        int flag = 0;
        try {
            msg += CommonUtils.checkStringParamEmptyReturn(vehicleExtend.getPlateNumber(), "车牌号码", 10, false);
            msg += CommonUtils.checkStringParamEmptyReturn(vehicleExtend.getVinRecognitionCode(), "车辆识别VIN码", 20, false);
            msg += CommonUtils.checkStringParamEmptyReturn(vehicleExtend.getRightTypeCode(), "出租汽车经营权类型", 2, false);
            msg += CommonUtils.checkStringParamEmptyReturn(vehicleExtend.getStatusCode(), "车辆状况代码", 2, false);
            msg += CommonUtils.checkStringParamEmptyReturn(vehicleExtend.getEngineNumber(), "车辆发动机号", 30, false);
            msg += CommonUtils.checkStringParamEmptyReturn(vehicleExtend.getTransportationCertificateNumber(), "道路运输证号", 50, false);
			
			
			
			/*if (StringUtils.isNotBlank(drivinglicenceExtend.getDrivingIcenseCode())&& !CommonUtils.validataIdNumPattern(drivinglicenceExtend.getDrivingIcenseCode())) {
				msg += "驾驶证号格式不正确,";
			}*/
			/*if (!CommonUtils.isMobileNO(drivinglicenceExtend.getPhone())) {
				msg += "驾驶员电话格式不正确,";
			}*/
            if (plateNumber.contains(vehicleExtend.getPlateNumber()) || vin.contains(vehicleExtend.getVinRecognitionCode())) {
                msg += "文档中车牌号或车架号重复";
            }

            if (engineNumber.contains(vehicleExtend.getEngineNumber()) || transportationCertificateNumber.contains(vehicleExtend.getTransportationCertificateNumber())) {
                msg += "文档中发动机号或道路运输证号重复";
            }
			/*if (drverCodeAndUuid.contains(drivinglicenceExtend.getPhone())) {
				msg += "文档中手机号重复,";
			}*/
            if (!CommonUtils.isCarNumSOEAndColor(vehicleExtend.getPlateNumber())) {
                msg += "车牌号格式不正确,";
            }

            if (StringUtils.isBlank(msg)) {
                if ("add".equals(handleType)) {
                    for (TaxiBaseInfoVehicle taxiBaseInfoVehicle : data) {
                        if (taxiBaseInfoVehicle.getPlateNumber().equals(vehicleExtend.getPlateNumber())) {
                            msg += "该车牌号已经被其他用户录入,";
                        }
                        if (taxiBaseInfoVehicle.getVinRecognitionCode().equals(vehicleExtend.getVinRecognitionCode())) {
                            msg += "该车辆VIN码已经被其他用户录入,";
                        }
                        if (taxiBaseInfoVehicle.getEngineNumber().equals(vehicleExtend.getEngineNumber())) {
                            msg += "该车辆发动机号已经被其他用户录入";
                        }
                        if (taxiBaseInfoVehicle.getTransportationCertificateNumber().equals(vehicleExtend.getTransportationCertificateNumber())) {
                            msg += "该车辆道路运输证号已经被其他用户录入";
                        }
                    }


                }
				/*if ("update".equals(handleType)) {
					if (!drverCodeAndUuidMap.containsKey(drivinglicenceExtend.getDrivingIcenseCode())) {
						msg += "没有此驾驶员不能覆盖,";
					} else {
						drivinglicenceExtend.setUuid(drverCodeAndUuidMap.get(drivinglicenceExtend.getDrivingIcenseCode()));
					}
				} else {
					if (phones.contains(drivinglicenceExtend.getPhone())) {
						msg += "该手机号码已经被其他用户录入,";
					}
					if (driverIdCertificateNumber.contains(drivinglicenceExtend.getIdCertificateNumber())) {
						msg += "该驾驶身份证号已经被其他用户录入,";
					}
				}*/
            }
            TaxiVehicleAdditionalRelation baseRelation = null;
            if (StringUtils.isNotBlank(msg)) {
                vehicleExtend.setValidateMsg(msg);
                vehicleExtend.setIfCanSave("no");
            } else {
                String corpId = corpInfo.get("corpId").toString();
                String time = DateUtil.getYMDHMSFormat(new Date());
                TaxiBaseInfoVehicle record = new TaxiBaseInfoVehicle();
                BeanUtils.copyProperties(vehicleExtend, record);
                String vehicleId = uuidUtils.getLongUUID() + "";
                record.setUuid(vehicleId);
                record.setCorpId(corpId);
                record.setCreateBy(userUuid + "");
                record.setUpdateBy(userUuid + "");
                record.setCreateTime(time);
                record.setUpdateTime(time);
                record.setStatus(Constants.STATUS_YES);
                record.setDriverBind(Constants.VEHICLE_BINDSTATUS_NO);
                record.setTerminalBind(Constants.TERMINAL_BINDSTATUS_NO);

                List<TaxiVehicleAdditionalRelation> relations = new ArrayList<TaxiVehicleAdditionalRelation>();
                //List<TaxiBaseInfoDriver> drivers =null;
//				TaxiVehicleAdditionalRelation relation = new TaxiVehicleAdditionalRelation();
                baseRelation = new TaxiVehicleAdditionalRelation();
                baseRelation.setVehicleUuid(vehicleId);
                baseRelation.setVehicleDeviceId(null);
                baseRelation.setVehiclePlateNumber(vehicleExtend.getPlateNumber());
                baseRelation.setCorpId(corpId + "");
                baseRelation.setCorpName(corpInfo.get("corp_name").toString());
                baseRelation.setCityId(corpInfo.get("city_id").toString());
                baseRelation.setCityName(corpInfo.get("city_name").toString());
                baseRelation.setDistrictId(corpInfo.get("district_id").toString());
                baseRelation.setDistrictName(corpInfo.get("dis_name").toString());
                baseRelation.setCreateTime(new Date());
                baseRelation.setUpdateTime(new Date());

                //判断是否绑定白班驾驶员
                List<TaxiDriverVehicle> taxiDriverVehicles = new ArrayList<TaxiDriverVehicle>();
                if (!StringUtils.isBlank(vehicleExtend.getDayDriverIdCardNum())) {
                    Map<String, Long> map = driverService.queryCheckDiverBound(vehicleExtend.getDayDriverIdCardNum(), String.valueOf(currentCorpId));
                    if (null != map && map.get("status") == 0) {
                        TaxiDriverVehicle taxiDriverVehicle = new TaxiDriverVehicle();
                        taxiDriverVehicle.setUuid(uuidUtils.getLongUUID());
                        taxiDriverVehicle.setDriverId(map.get("uuid") + "");
                        taxiDriverVehicle.setVehicleId(vehicleId);
                        taxiDriverVehicle.setCreateBy(userUuid + "");
                        taxiDriverVehicle.setUpdateBy(userUuid + "");
                        taxiDriverVehicle.setCreateTime(time);
                        taxiDriverVehicle.setUpdateTime(time);
                        taxiDriverVehicle.setStatus(Constants.DRIVER_BINDSTATUS_YES);
                        taxiDriverVehicle.setWorkType(Constants.DRIVER_WORK_TYPE_DAY);
                        taxiDriverVehicles.add(taxiDriverVehicle);
                        status = "02";
                        flag = flag + 1;

                        TaxiBaseInfoDriver param = new TaxiBaseInfoDriver();
                        String driverId = map.get("uuid") + "";
                        param.setUuid(driverId);
                        TaxiBaseInfoDriver driver = driverService.queryDriverByParam(param).get(0);
                        TaxiVehicleAdditionalRelation dayRelation = new TaxiVehicleAdditionalRelation();
                        BeanUtils.copyBasicProperties(baseRelation, dayRelation);
                        dayRelation.setDriverId(driverId);
                        dayRelation.setDriverWorkType("01");
                        dayRelation.setDriverCertificateNumber(driver.getCertificateNumber());
                        dayRelation.setDriverPhoneNumber(driver.getPhoneNumber());
                        dayRelation.setDriverName(driver.getName());
                        relations.add(dayRelation);
                    }

                }

                //判断是否绑定副班驾驶员
                if (!StringUtils.isBlank(vehicleExtend.getNightDriverIdCardNum())) {
                    Map<String, Long> map = driverService.queryCheckDiverBound(vehicleExtend.getNightDriverIdCardNum(), String.valueOf(currentCorpId));
                    if (null != map && map.get("status") == 0) {
                        TaxiDriverVehicle taxiDriverVehicle = new TaxiDriverVehicle();
                        taxiDriverVehicle.setUuid(uuidUtils.getLongUUID());
                        taxiDriverVehicle.setDriverId(map.get("uuid") + "");
                        taxiDriverVehicle.setVehicleId(vehicleId);
                        taxiDriverVehicle.setCreateBy(userUuid + "");
                        taxiDriverVehicle.setUpdateBy(userUuid + "");
                        taxiDriverVehicle.setCreateTime(time);
                        taxiDriverVehicle.setUpdateTime(time);
                        taxiDriverVehicle.setStatus(Constants.DRIVER_BINDSTATUS_YES);
                        taxiDriverVehicle.setWorkType(Constants.DRIVER_WORK_TYPE_NIGHT);
                        taxiDriverVehicles.add(taxiDriverVehicle);
                        status = "03";
                        flag = flag + 1;

                        TaxiBaseInfoDriver param = new TaxiBaseInfoDriver();
                        String driverId = map.get("uuid") + "";
                        param.setUuid(driverId);
                        TaxiBaseInfoDriver driver = driverService.queryDriverByParam(param).get(0);
                        TaxiVehicleAdditionalRelation nightRelation = new TaxiVehicleAdditionalRelation();
                        BeanUtils.copyBasicProperties(baseRelation, nightRelation);
                        nightRelation.setDriverId(driverId);
                        nightRelation.setDriverWorkType("02");
                        nightRelation.setDriverCertificateNumber(driver.getCertificateNumber());
                        nightRelation.setDriverPhoneNumber(driver.getPhoneNumber());
                        nightRelation.setDriverName(driver.getName());
                        relations.add(nightRelation);
                    }
                }
                if (flag == 2) {
                    status = "01";
                }
                record.setDriverBind(status);
                vehicleExtend.setTaxiBaseInfoVehicle(record);
                if (taxiDriverVehicles.size() > 0) {
                    vehicleExtend.setTaxiDriverVehicles(taxiDriverVehicles);
                }
                if (taxiDriverVehicles.size() > 0) {
                    vehicleExtend.setRelations(relations);
                } else {
                    relations.add(baseRelation);

                    vehicleExtend.setRelations(relations);
                }
                vehicleExtend.setValidateMsg("成功");
                vehicleExtend.setIfCanSave("yes");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vehicleExtend;
    }


    /**
     * @param @param  file
     * @param @return
     * @return MessageResp
     * @throws
     * @Title: importDate
     * @Description: 导出redis数据
     */
    @ResponseBody
    @RequestMapping("/exportRedisData")
    public MessageResp exportRedisData(HttpServletRequest request, HttpServletResponse response, String redisKey) {
        MessageResp messageResp = new MessageResp();
        List<TaxiBaseInfoVehicleExtend> vehicleExtends = redisTemplate.opsForValue().get(redisKey);

        try {
            if (null != vehicleExtends && vehicleExtends.size() > 0) {
                response.setCharacterEncoding("UTF-8");
                request.setCharacterEncoding("UTF-8");
                String xlsname = Constants.EXCLENAME_VEHICLE;
                String filepath = this.getClass().getClassLoader().getResource("").getPath() + "exceltemplate/" + xlsname + ".xlsx";
                // 导出数据到模板
                ExportExcel<TaxiBaseInfoVehicleExtend> exportExcel = new ExportExcel<TaxiBaseInfoVehicleExtend>(TaxiBaseInfoVehicleExtend.class);
                exportExcel.exportBasicExcelByExcelTemplate(filepath, xlsname, vehicleExtends, "yyyy-MM-dd HH:mm:ss", request, response);
            } else {
                messageResp.setResult("false");
                messageResp.setResultDesc("缓存数据已失效");
                return messageResp;
            }
        } catch (Exception e) {
            messageResp.setResult("false");
            messageResp.setResultDesc("导出缓存数据异常");
            return messageResp;
        }
        return messageResp;
    }


    /**
     * 导出异常监控离线车辆数据
     */
    @RequestMapping("/export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response, @ModelAttribute KafkaResultTaxiOnoffline param) throws Exception {

        String ymdhmsFormat = DateUtil.getYMDHMSFormat(new Date());
        //获取数据
        List<KafkaResultTaxiOnoffline> list = onofflineService.queryExportVehList(param);
        String[][] content = new String[list.size()][];
        //excel标题
        String[] title = {"终端号", "终端厂商", "车牌号", "所属公司", "车主名称", "联系电话", "最后一次在线时间", "离线时长(h)", "离线车辆(" + list.size() + "台)"};
        //excel文件名
        String fileName = "离线车辆(" + list.size() + "台)记录表 " + ymdhmsFormat + ".xls";
        //sheet名
        String sheetName = "离线车辆记录表";
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            KafkaResultTaxiOnoffline obj = list.get(i);
            content[i][0] = obj.getTerminalCode();
            content[i][1] = obj.getDeviceType();
            content[i][2] = obj.getPlateNumber();
            content[i][3] = obj.getCorpName();
            content[i][4] = obj.getName();
            content[i][5] = obj.getPhoneNumber();
            String time = DateUtil.getYMDHMSFormat(obj.getEventTime());
            content[i][6] = time;
            content[i][7] = obj.getOffLine();
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook2(sheetName, sheetName, title, content, null);
        //响应到客户端
        try {
            setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
