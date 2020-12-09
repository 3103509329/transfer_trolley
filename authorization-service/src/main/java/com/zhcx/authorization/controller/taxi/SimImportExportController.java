package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.BeanUtils;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.excel.ExcelReader;
import com.zhcx.authorization.utils.excel.ExportExcel;
import com.zhcx.authorization.utils.excel.ImportExcel;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoSimService;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoVehicleService;
import com.zhcx.basicdata.facade.taxi.TaxiVehicleSimService;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoSim;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoSimExtend;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoVehicle;
import com.zhcx.basicdata.pojo.taxi.TaxiVehicleSim;
import com.zhcx.common.util.UUIDUtils;
import com.zhcx.common.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * Sim卡信息导入
 */
@Controller
@RequestMapping("/taxi/simExt")
public class SimImportExportController {

    private static final Logger logger = LoggerFactory.getLogger(SimImportExportController.class);

    @Autowired
    private TaxiBaseInfoSimService simService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Autowired
    private TaxiVehicleSimService taxiVehicleSimService;

    @Autowired
    private TaxiBaseInfoVehicleService taxiBaseInfoVehicleService;

    @Resource
    private UUIDUtils uuidUtils;

    @Resource(name = "redisTemplate4Json")
    private RedisTemplate<String, List<TaxiBaseInfoSimExtend>> redisTemplate;

    /**
     * @param @param  file
     * @param @return
     * @return MessageResp
     * @throws
     * @Title: importDate
     * @Description: 导入Sim数据
     */
    @ResponseBody
    @RequestMapping("/importExcelData")
    public MessageResp importExcelData(@RequestParam MultipartFile file, HttpServletRequest request, String handleType, String corpId1) {
        long start = System.currentTimeMillis();
        MessageResp resp = new MessageResp();
        Long corpId = null;
        AuthUserResp user = sessionHandler.getUser(request);
        if ("01".equals(user.getUserType())) {//是管理员
            corpId = Long.valueOf(corpId1);
        } else {
            corpId = user.getCorpId();
        }
        ImportExcel<TaxiBaseInfoSimExtend> ie = new ImportExcel(TaxiBaseInfoSimExtend.class);
        // 验证导入的表格是否符合
        resp = ExcelReader.validateImportExcel(file);
        if ("false".equals(resp.getResult())) {
            return resp;
        }
        //String xlsname = Constants.EXCLENAME_DRIVING;
        // 验证标题头
        //resp = ExcelReader.validateExcelHeader(xlsname, file);
        List<TaxiBaseInfoSimExtend> drivinglicenceExtends = new ArrayList<TaxiBaseInfoSimExtend>();
        TaxiBaseInfoSimExtend drivinglicenceExtend = new TaxiBaseInfoSimExtend();
        drivinglicenceExtend.setCorpId(corpId + "");
        List<TaxiBaseInfoSim> drivinglicences = new ArrayList<TaxiBaseInfoSim>();
        List<TaxiVehicleSim> drivinglicences2 = new ArrayList<TaxiVehicleSim>();
        if ("false".equals(resp.getResult())) {
            drivinglicenceExtend.setValidateMsg(resp.getResultDesc());
            drivinglicenceExtends.add(drivinglicenceExtend);
            resp.setResult("false");
            resp.setStatusCode("-1");
            return resp;
        } else {
            drivinglicenceExtends = (List<TaxiBaseInfoSimExtend>) ie.importExcel(file, "yyyy-MM-dd HH:mm:ss");
            int cbList_size = drivinglicenceExtends == null || drivinglicenceExtends.size() <= 0 ? 0 : drivinglicenceExtends.size();
            if (cbList_size <= 0) {
                resp.setResult("false");
                resp.setResultDesc("上传的文件内容不能为空");
                return resp;
            }

            //全量查出部分必要验证字段 sim_code
            List<TaxiBaseInfoSim> data = simService.queryAllCheckData();
            //查车

            Set<String> driverIdCertificateNumber = new HashSet<String>();
            for (int i = 0; i < cbList_size; i++) {
                drivinglicenceExtend = (TaxiBaseInfoSimExtend) drivinglicenceExtends.get(i);
                // 验证并处理excle数据
                drivinglicenceExtend = processExcelData(drivinglicenceExtend, driverIdCertificateNumber, data, corpId, user.getUserName(), handleType);
                if ("yes".equals(drivinglicenceExtend.getIfCanSave())) {
                    driverIdCertificateNumber.add(drivinglicenceExtend.getSimCode());
                    drivinglicences.add(drivinglicenceExtend.getTaxiBaseInfoSim());
                    if (StringUtils.isNotBlank(drivinglicenceExtend.getTaxiVehicleSim().getVehicleUuid())) {
                        drivinglicences2.add(drivinglicenceExtend.getTaxiVehicleSim());
                    }
                }
            }
        }
        try {
            if (drivinglicences.size() > 0) {
                if ("add".equals(handleType)) {
                    //保存到sim表
                    simService.saveBatch(drivinglicences);
                } /*else {
					for (Drivinglicence drivinglicence : drivinglicences) {
						driverService.modifyByUuid(drivinglicence);
					}
				}*/
            }
            if (drivinglicences2.size() > 0) {
                if ("add".equals(handleType)) {
                    //解绑sim卡与车辆关系
                    TaxiVehicleSim vehicleSim = new TaxiVehicleSim();
                    for (TaxiVehicleSim taxiVehicleSim : drivinglicences2) {
                        vehicleSim.setStatus("01");
                        vehicleSim.setVehicleUuid(taxiVehicleSim.getVehicleUuid());
                        //先查询出是否绑定
                        int exsit = taxiVehicleSimService.isExsit(vehicleSim);
                        if (exsit > 0) {//存在绑定关系
                            vehicleSim.setStatus("02");
                            //解除绑定关系
                            taxiVehicleSimService.unbind(vehicleSim);
                        }
                        //添加绑定
                        taxiVehicleSimService.addBind(taxiVehicleSim);
                    }
                }
            }
            String key = "drivinglicence_export" + corpId + "_" + (new Date()).getTime();
            //RedisClient.setList(key, drivinglicenceExtends, cacheTime);
            redisTemplate.opsForValue().set(key, drivinglicenceExtends, 30, TimeUnit.MINUTES);
            resp.setResultDesc("批量导入数据,成功" + drivinglicences.size() + "条,失败" + (drivinglicenceExtends.size() - drivinglicences.size()) + "条");
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


    public TaxiBaseInfoSimExtend processExcelData(TaxiBaseInfoSimExtend drivinglicenceExtend, Set<String> driverIdCertificateNumber, List<TaxiBaseInfoSim> data, Long corpId, String userName, String handleType) {
        String msg = "";
        try {
            msg += CommonUtils.checkStringParamEmptyReturn(drivinglicenceExtend.getSimCode(), "SIM卡号", 30, false);
//            msg += CommonUtils.checkStringParamEmptyReturn(drivinglicenceExtend.getGender(), "驾驶员性别",1, false);
//            msg += CommonUtils.checkStringParamEmptyReturn(drivinglicenceExtend.getIdCertificateNumber(), "身份证号码",18, false);
//            msg += CommonUtils.checkStringParamEmptyReturn(drivinglicenceExtend.getCertificateNumber(), "从业资格证号",20, false);
            //判断excel表格中可随便输入的准驾车型是否是规定的
			/*if (StringUtils.isNotBlank(drivinglicenceExtend.getQdType())) {
				String[] str = {"A1","A2","A3","B1","B2","C1","C2"};
				String qt = drivinglicenceExtend.getQdType();
				try {
					String[] qts = qt.split(",");
					for (String string : qts) {
						if(!ArrayUtils.contains(str, string)){
							msg += "驾驶员准驾车型格式不正确,";
						}
					}
				} catch (Exception e) {
					msg += "驾驶员准驾车型格式不正确,";
				}
			}*/
			/*if (StringUtils.isNotBlank(drivinglicenceExtend.getDrivingIcenseCode())&& !CommonUtils.validataIdNumPattern(drivinglicenceExtend.getDrivingIcenseCode())) {
				msg += "驾驶证号格式不正确,";
			}*/

            if (StringUtils.isNotBlank(drivinglicenceExtend.getSimCode()) && !CommonUtils.isNumeric(drivinglicenceExtend.getSimCode())) {
                msg += "SIM卡号格式不正确,";
            }
            if (StringUtils.isNotBlank(drivinglicenceExtend.getSimType()) && !CommonUtils.isNumeric(drivinglicenceExtend.getSimType())) {
                msg += "运营商格式不正确,";
            }

            if (StringUtils.isBlank(msg)) {
                if ("add".equals(handleType)) {
                    for (TaxiBaseInfoSim taxiBaseInfoSim : data) {
                        if (taxiBaseInfoSim.getSimCode().equals(drivinglicenceExtend.getSimCode())) {
                            msg += "该SIM卡号已经被其他用户录入,";
                            break;
                        }
                    }
                }
            }
            if (StringUtils.isNotBlank(msg)) {
                drivinglicenceExtend.setValidateMsg(msg);
                drivinglicenceExtend.setIfCanSave("no");
            } else {
                String time = DateUtil.getNowDateTime();
                TaxiBaseInfoSim record = new TaxiBaseInfoSim();
                BeanUtils.copyProperties(drivinglicenceExtend, record);
                record.setUuid(Integer.valueOf(uuidUtils.getLongUUID() + ""));
                record.setCorpId(corpId + "");
                record.setCreateBy(userName + "");
                record.setUpdateBy(userName + "");
                record.setCreateTime(new Date());
                record.setUpdateTime(new Date());
                drivinglicenceExtend.setTaxiBaseInfoSim(record);

                //先根据车牌查车
                PageInfo<TaxiBaseInfoVehicle> pageInfo = null;
                String plateNum = drivinglicenceExtend.getBindVehiclePlateNum();
                TaxiBaseInfoVehicle vehicle = new TaxiBaseInfoVehicle();
                vehicle.setPlateNumber(plateNum);
                vehicle.setStatus("1");
                pageInfo = taxiBaseInfoVehicleService.queryVehicleInfoByParam(vehicle);
                if (pageInfo.getSize() > 0 && pageInfo != null) {
                    vehicle = pageInfo.getList().get(0);
                }
                //构建sim卡与车辆关系实体
                TaxiVehicleSim vehicleSim = new TaxiVehicleSim();
                vehicleSim.setStatus("01");
                vehicleSim.setSimCode(String.valueOf(record.getUuid()));
                vehicleSim.setVehicleUuid(vehicle.getUuid());
                vehicleSim.setCreateBy(userName);
                vehicleSim.setCreateTime(new Date());
                vehicleSim.setUpdateTime(new Date());
                drivinglicenceExtend.setTaxiVehicleSim(vehicleSim);

                drivinglicenceExtend.setValidateMsg("成功");
                drivinglicenceExtend.setIfCanSave("yes");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drivinglicenceExtend;
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
        List<TaxiBaseInfoSimExtend> driverExtends = redisTemplate.opsForValue().get(redisKey);
        try {
            if (null != driverExtends && driverExtends.size() > 0) {
                response.setCharacterEncoding("UTF-8");
                request.setCharacterEncoding("UTF-8");
                String xlsname = "SIM卡信息";
                String filepath = this.getClass().getClassLoader().getResource("").getPath() + "exceltemplate/" + xlsname + ".xlsx";
                // 导出数据到模板
                ExportExcel<TaxiBaseInfoSimExtend> exportExcel = new ExportExcel<TaxiBaseInfoSimExtend>(TaxiBaseInfoSimExtend.class);
                exportExcel.exportBasicExcelByExcelTemplate(filepath, xlsname, driverExtends, "yyyy-MM-dd HH:mm:ss", request, response);
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


}
