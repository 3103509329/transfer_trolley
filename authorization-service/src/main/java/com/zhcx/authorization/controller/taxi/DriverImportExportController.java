package com.zhcx.authorization.controller.taxi;

import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.BeanUtils;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.excel.ExcelReader;
import com.zhcx.authorization.utils.excel.ExportExcel;
import com.zhcx.authorization.utils.excel.ImportExcel;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoDriverService;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoDriver;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoDriverExtend;
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
 * @ClassName：驾驶员导入
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author：tangding
 * @date：2019年3月4日
 */
@Controller
@RequestMapping("/taxi/driverExt")
public class DriverImportExportController {

    private static final Logger logger = LoggerFactory.getLogger(DriverImportExportController.class);

    @Autowired
    private TaxiBaseInfoDriverService driverService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Resource
    private UUIDUtils uuidUtils;

    @Resource(name = "redisTemplate4Json")
    private RedisTemplate<String, List<TaxiBaseInfoDriverExtend>> redisTemplate;

    /**
     * @param @param  file
     * @param @return
     * @return MessageResp
     * @throws
     * @Title: importDate
     * @Description: 导入驾驶员数据
     */
    @ResponseBody
    @RequestMapping("/importExcelData")
    public MessageResp importExcelData(@RequestParam MultipartFile file, HttpServletRequest request, String handleType, Long corpId1) {
        long start = System.currentTimeMillis();
        MessageResp resp = new MessageResp();

        AuthUserResp user = sessionHandler.getUser(request);

        if (!Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())) {
            corpId1 = user.getCorpId();
        }
        ImportExcel<TaxiBaseInfoDriverExtend> ie = new ImportExcel(TaxiBaseInfoDriverExtend.class);
        // 验证导入的表格是否符合
        resp = ExcelReader.validateImportExcel(file);
        if ("false".equals(resp.getResult())) {
            return resp;
        }
        //String xlsname = Constants.EXCLENAME_DRIVING;
        // 验证标题头
        //resp = ExcelReader.validateExcelHeader(xlsname, file);
        List<TaxiBaseInfoDriverExtend> drivinglicenceExtends = new ArrayList<TaxiBaseInfoDriverExtend>();
        TaxiBaseInfoDriverExtend drivinglicenceExtend = new TaxiBaseInfoDriverExtend();
        drivinglicenceExtend.setCorpId(corpId1 + "");
        List<TaxiBaseInfoDriver> drivinglicences = new ArrayList<TaxiBaseInfoDriver>();
        if ("false".equals(resp.getResult())) {
            drivinglicenceExtend.setValidateMsg(resp.getResultDesc());
            drivinglicenceExtends.add(drivinglicenceExtend);
            resp.setResult("false");
            resp.setStatusCode("-1");
            return resp;
        } else {
            drivinglicenceExtends = (List<TaxiBaseInfoDriverExtend>) ie.importExcel(file, "yyyy-MM-dd HH:mm:ss");
            int cbList_size = drivinglicenceExtends == null || drivinglicenceExtends.size() <= 0 ? 0 : drivinglicenceExtends.size();
            if (cbList_size <= 0) {
                resp.setResult("false");
                resp.setResultDesc("上传的文件内容不能为空");
                return resp;
            }

            //全量查出部分必要验证字段
            List<TaxiBaseInfoDriver> data = driverService.queryAllCheckData();
            Set<String> driverIdCertificateNumber = new HashSet<String>();
            for (int i = 0; i < cbList_size; i++) {
                drivinglicenceExtend = (TaxiBaseInfoDriverExtend) drivinglicenceExtends.get(i);
                // 验证并处理excle数据
                drivinglicenceExtend = processExcelData(drivinglicenceExtend, driverIdCertificateNumber, data, corpId1, user.getUserId(), handleType);
                if ("yes".equals(drivinglicenceExtend.getIfCanSave())) {
                    driverIdCertificateNumber.add(drivinglicenceExtend.getIdCertificateNumber());
                    drivinglicences.add(drivinglicenceExtend.getTaxiBaseInfoDriver());
                }
            }
        }
        try {
            if (drivinglicences.size() > 0) {
                if ("add".equals(handleType)) {
                    driverService.saveBatch(drivinglicences);
                } /*else {
					for (Drivinglicence drivinglicence : drivinglicences) {
						driverService.modifyByUuid(drivinglicence);
					}
				}*/
            }
            String key = "drivinglicence_export" + corpId1 + "_" + (new Date()).getTime();
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


    public TaxiBaseInfoDriverExtend processExcelData(TaxiBaseInfoDriverExtend drivinglicenceExtend, Set<String> driverIdCertificateNumber, List<TaxiBaseInfoDriver> data, Long corpId, Long userUuid, String handleType) {
        String msg = "";
        try {
            msg += CommonUtils.checkStringParamEmptyReturn(drivinglicenceExtend.getName(), "驾驶员姓名", 30, false);
            msg += CommonUtils.checkStringParamEmptyReturn(drivinglicenceExtend.getGender(), "驾驶员性别", 1, false);
            msg += CommonUtils.checkStringParamEmptyReturn(drivinglicenceExtend.getIdCertificateNumber(), "身份证号码", 18, false);
            msg += CommonUtils.checkStringParamEmptyReturn(drivinglicenceExtend.getCertificateNumber(), "从业资格证号", 20, false);
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

            if (StringUtils.isNotBlank(drivinglicenceExtend.getGender()) && !CommonUtils.validataSex(drivinglicenceExtend.getGender())) {
                msg += "驾驶员性别格式不正确,";
            }

            if (StringUtils.isNotBlank(drivinglicenceExtend.getEvaluationLevel()) && !CommonUtils.validataLeve(drivinglicenceExtend.getEvaluationLevel())) {
                msg += "驾驶员从业资格等级格式不正确,";
            }

            if (StringUtils.isNotBlank(drivinglicenceExtend.getIdCertificateNumber()) && !CommonUtils.validataIdNumPattern(drivinglicenceExtend.getIdCertificateNumber())) {
                msg += "驾驶员身份证格式不正确,";
            }
            if (!CommonUtils.isMobileNO(drivinglicenceExtend.getPhoneNumber())) {
                msg += "驾驶员电话格式不正确,";
            }
			/*if (drverCodeAndUuid.contains(drivinglicenceExtend.getDrivingIcenseCode())) {
				msg += "文档中驾驶证号重复,";
			}*/
			/*if (drverCodeAndUuid.contains(drivinglicenceExtend.getPhone())) {
				msg += "文档中手机号重复,";
			}*/
            if (driverIdCertificateNumber.contains(drivinglicenceExtend.getIdCertificateNumber())) {
                msg += "文档中身份证号重复,";
            }
            if (StringUtils.isBlank(msg)) {
                if ("add".equals(handleType)) {
                    for (TaxiBaseInfoDriver taxiBaseInfoDriver : data) {
                        if (taxiBaseInfoDriver.getIdCertificateNumber().equals(drivinglicenceExtend.getIdCertificateNumber())) {
                            msg += "该驾驶身份证号已经被其他用户录入,";
                            break;
                        }
                        if (taxiBaseInfoDriver.getCertificateNumber().equals(drivinglicenceExtend.getCertificateNumber())) {
                            msg += "该驾驶从业资格证号已经被其他用户录入,";
                            break;
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
            if (StringUtils.isNotBlank(msg)) {
                drivinglicenceExtend.setValidateMsg(msg);
                drivinglicenceExtend.setIfCanSave("no");
            } else {
                String time = DateUtil.getNowDateTime();
                TaxiBaseInfoDriver record = new TaxiBaseInfoDriver();
                BeanUtils.copyProperties(drivinglicenceExtend, record);
                record.setUuid(uuidUtils.getLongUUID() + "");
                record.setCorpId(corpId + "");
                record.setCreateBy(userUuid + "");
                record.setUpdateBy(userUuid + "");
                record.setCreateTime(time);
                record.setUpdateTime(time);
                record.setStatus(Constants.STATUS_YES);
                record.setBindStatus(Constants.DRIVER_BINDSTATUS_NO);
                drivinglicenceExtend.setTaxiBaseInfoDriver(record);
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
        List<TaxiBaseInfoDriverExtend> driverExtends = redisTemplate.opsForValue().get(redisKey);
        try {
            if (null != driverExtends && driverExtends.size() > 0) {
                response.setCharacterEncoding("UTF-8");
                request.setCharacterEncoding("UTF-8");
                String xlsname = Constants.EXCLENAME_DRIVER;
                String filepath = this.getClass().getClassLoader().getResource("").getPath() + "exceltemplate/" + xlsname + ".xlsx";
                // 导出数据到模板
                ExportExcel<TaxiBaseInfoDriverExtend> exportExcel = new ExportExcel<TaxiBaseInfoDriverExtend>(TaxiBaseInfoDriverExtend.class);
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
