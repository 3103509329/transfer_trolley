package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSONObject;
import com.zhcx.authorization.config.IotConfig;
import com.zhcx.authorization.controller.monitor.TtsController;
import com.zhcx.authorization.utils.BeanUtils;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.OkHttpUtil;
import com.zhcx.authorization.utils.excel.ExcelReader;
import com.zhcx.basicdata.facade.base.BaseDictionaryService;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoCompanyService;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoVehicleService;
import com.zhcx.basicdata.pojo.base.BaseDictionary;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoCompany;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoVehicle;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoVehicleImportPojo;
import com.zhcx.common.util.UUIDUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: authorization-service
 * @ClassName:TaxiInfoImportController
 * @description: 出租信息导入Controller
 * @author: ZhangKai
 * @create: 2018-12-27 16:30
 **/
@RequestMapping("/taxi/import")
@RestController
public class TaxiInfoImportController {

    private static final Logger logger = LoggerFactory.getLogger(TaxiInfoImportController.class);

    @Autowired
    private UUIDUtils uuidUtils;

    @Autowired
    private TaxiBaseInfoVehicleService  taxiBaseInfoVehicleService;

    @Autowired
    private BaseDictionaryService dictionaryService;

    @Autowired
    private IotConfig iotConfig;

    @Autowired
    private TaxiBaseInfoCompanyService taxiBaseInfoCompanyService;

    @Autowired
    private OkHttpUtil okHttpUtil;

    private List<BaseDictionary> rllxs;

    private List<BaseDictionary> cpyss;

    private List<BaseDictionary> clzc;

    private List<BaseDictionary> qcpfbz;

    private List<BaseDictionary> czqcjyqlx;

    private List<TaxiBaseInfoCompany> companies;

    private List<BaseDictionary> ejwhzt;


    /**
     * 获取deviceId
     * @param uuid
     * @return
     * @throws Exception
     */
    public String createDeviceIdFromIot(String uuid) throws Exception {
        String deviceId = null;
        String token = getIotToken();
        if(null == token || "".equals(token)){
            logger.error("登录IOT失败!");
            return deviceId;
        }
        JSONObject param = new JSONObject();
        param.put("name",uuid);
        param.put("type","default");
        String url = iotConfig.getUrl()+"/api/device";
        okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), param.toJSONString());
        Response response = okHttpUtil.doPostRequest(body, url, token);
        String  resStr = null;
        JSONObject resObj = null;
        if(response.code() == 200){
            resStr = response.body().string();
            resObj = (JSONObject) JSONObject.parse(resStr);
            JSONObject deviceInfo = (JSONObject) resObj.get("id");
            if(null == deviceInfo || deviceInfo.isEmpty()){
                logger.error("IOT创建车辆失败");
            }
            deviceId = deviceInfo.getString("id");
            if(null == deviceId || "".equals(deviceId)){
                logger.error("IOT创建车辆失败");
            }
        }
        return deviceId;
    }

    /**
     * 获取IOT token
     * @return
     */
    protected String getIotToken(){
        String token = null;
        JSONObject loginParam = new JSONObject();
        loginParam.put("username", iotConfig.getUsername());
        loginParam.put("password", iotConfig.getPassword());
        String paramStr = loginParam.toJSONString();
        okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramStr);
        Response response = okHttpUtil.doPostRequest(body, iotConfig.getUrl() + "/api/auth/login", null);
        try {
            if (response.code() == 200) {
                String tokenStr = response.body().string();
                JSONObject tokenObj = (JSONObject) JSONObject.parse(tokenStr);
                token = tokenObj.getString("token");
            } else {
                logger.error("登录IOT异常");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != response) {
                response.body().close();
                response.close();
            }
        }
        return token;
    }

    /**
     * 转换各类型的状态码
     * @param vehicle
     */
    protected void transStatusCodeFromDesc(TaxiBaseInfoVehicle vehicle){
        try{
            if(companies == null){
                companies = taxiBaseInfoCompanyService.queryCompanyList(new TaxiBaseInfoCompany()).getList();
            }
            if(rllxs == null){
                rllxs = dictionaryService.selectDictionary("RLLX");
            }
            if(cpyss == null){
                cpyss = dictionaryService.selectDictionary("CPYS");
            }

            if(clzc == null){
                clzc = dictionaryService.selectDictionary("CLZC");
            }

            if(qcpfbz == null){
                qcpfbz = dictionaryService.selectDictionary("QCPFBZ");
            }

            if(czqcjyqlx == null){
                czqcjyqlx = dictionaryService.selectDictionary("CZQCJYQLX");
            }

            if(ejwhzt == null){
                ejwhzt = dictionaryService.selectDictionary("EJWHZT");
            }

            //企业信息
            for(TaxiBaseInfoCompany company : companies){
                if(null != vehicle.getCorpName() && !"".equals(vehicle.getCorpName())){
                    if(vehicle.getCorpName().equals(company.getName())){
                        vehicle.setCorpId(company.getUuid());
                        break;
                    }
                }
            }

            //燃料类型
            for(BaseDictionary dictionary:rllxs){
                if(null != vehicle.getFuelType() && !"".equals(vehicle.getFuelType())){
                    if(vehicle.getFuelType().equals(dictionary.getItem())){
                        vehicle.setFuelTypeCode(dictionary.getItemCode());
                        break;
                    }
                }

            }

            //车牌颜色
            for(BaseDictionary dictionary:cpyss){
                if(null != vehicle.getPlateColor() && !"".equals(vehicle.getPlateColor())){
                    if(vehicle.getPlateColor().equals(dictionary.getItem())){
                        vehicle.setPlateColorCode(dictionary.getItemCode());
                        break;
                    }
                }
            }

            //车辆状况
            for(BaseDictionary dictionary:clzc){
                if(null != vehicle.getStatusCode() && !"".equals(vehicle.getStatusCode())){
                    if(vehicle.getStatusCode().equals(dictionary.getItem())){
                        vehicle.setStatusCode(dictionary.getItemCode());
                        break;
                    }
                }
            }
            //出租汽车经营权类型
            for(BaseDictionary dictionary:czqcjyqlx){
                if(null != vehicle.getRightTypeCode() && !"".equals(vehicle.getRightTypeCode())){
                    if(vehicle.getRightTypeCode().equals(dictionary.getItem())){
                        vehicle.setRightTypeCode(dictionary.getItemCode());
                        break;
                    }
                }
            }
            //排放标准
            for(BaseDictionary dictionary:qcpfbz){
                if(null != vehicle.getEmissionStandard() && !"".equals(vehicle.getEmissionStandard())){
                    if(vehicle.getEmissionStandard().equals(dictionary.getItem())){
                        vehicle.setEmissionStandardCode(dictionary.getItemCode());
                        break;
                    }
                }
            }

            //维护状态
            for(BaseDictionary dictionary:ejwhzt){
                if(null != vehicle.getCompleteMaintenanceStatus() && !"".equals(vehicle.getCompleteMaintenanceStatus())){
                    if(vehicle.getCompleteMaintenanceStatus().equals(dictionary.getItem())){
                        vehicle.setCompleteMaintenanceStatus(dictionary.getItemCode());
                        break;
                    }
                }
            }
        }catch (Exception e){
            logger.error("数据转换发生异常,{}",e);
        }

    }

}
