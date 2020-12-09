package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.facade.AuthUserRoleService;
import com.zhcx.auth.facade.AuthUserService;
import com.zhcx.auth.pojo.AuthUser;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.auth.pojo.AuthUserRole;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.*;
import com.zhcx.basicdata.facade.base.BaseDictionaryService;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoCompanyService;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoVehicleService;
import com.zhcx.basicdata.facade.taxi.TaxiOperationLogService;
import com.zhcx.basicdata.pojo.base.BaseCity;
import com.zhcx.basicdata.pojo.base.BaseDistrict;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoCompany;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoVehicle;
import com.zhcx.basicdata.pojo.taxi.TaxiOperationLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/taxi/company")
@Api(value = "company", tags = "出租车企业company接口")
public class TaxiCompanyController {

    private Logger log = LoggerFactory.getLogger(TaxiCompanyController.class);

    @Autowired
    private TaxiBaseInfoCompanyService companyService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Autowired
    private AuthUserService userService;

    @Autowired
    private AuthUserRoleService userRoleService;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private TaxiBaseInfoVehicleService vehicleService;

    @Autowired
    private BaseDictionaryService dictionaryService;

    @Autowired
    private TaxiOperationLogService logService;

    @Value("${zhcx.sync.streamax.url}")
    private String syncUrl;

    @Value("${zhcx.sync.streamax.userId}")
    private String userId;

    @Value("${zhcx.sync.streamax.userKey}")
    private String userKey;

    String oldName = null;

    @GetMapping("/queryCompanyList")
    @ApiOperation(value = "查询企业信息列表", notes = "参数Company")
    public MessageResp queryCompanyList(HttpServletRequest request, @ModelAttribute TaxiBaseInfoCompany param) {
        MessageResp response = new MessageResp();
        PageInfo<TaxiBaseInfoCompany> pageInfo = null;
        try {
            AuthUserResp authUser = sessionHandler.getUser(request);
            //非管理员用户只能够查看自己企业
            if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
                response.setStatusCode("-50");
                response.setResult(Boolean.FALSE.toString());
                response.setResultDesc("您不是系统管理员，没有权限");
                return response;
            }
            pageInfo = companyService.queryCompanyList(param);
            response.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            response.setData(pageInfo.getList());
            response.setStatusCode("00");
            response.setResult(Boolean.TRUE.toString());
            response.setResultDesc("查询企业信息列表成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setResult(Boolean.FALSE.toString());
            response.setStatusCode("-50");
            response.setResultDesc("查询企业信息列表异常");
        }
        return response;
    }

    @GetMapping("/queryCompanyInfo")
    @ApiOperation(value = "查询企业信息", notes = "参数uuid")
    public MessageResp queryCompanyInfo(HttpServletRequest request, @RequestParam String uuid) {
        MessageResp response = new MessageResp();
        try {
            AuthUserResp authUser = sessionHandler.getUser(request);
            //非管理员用户只能够查看自己企业
            if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())
                    && !StringUtils.equals(uuid, String.valueOf(authUser.getCorpId()))) {
                response.setStatusCode("-50");
                response.setResult(Boolean.FALSE.toString());
                response.setResultDesc("您不是系统管理员，不能查看其它企业信息");
                return response;
            }
            TaxiBaseInfoCompany company = companyService.selectCompanyInfo(uuid);
            response.setData(company);
            response.setStatusCode("00");
            response.setResult(Boolean.TRUE.toString());
            response.setResultDesc("查询企业信息成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setResult(Boolean.FALSE.toString());
            response.setStatusCode("-50");
            response.setResultDesc("查询车辆信息异常");
        }
        return response;
    }

    @PostMapping("/insertCompany")
    @ApiOperation(value = "添加企业信息", notes = "参数company，系统管理员权限")
    public MessageResp insertCompany(HttpServletRequest request, @RequestBody TaxiBaseInfoCompany company) {
        MessageResp response = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_C);
        try {
            AuthUserResp authUser = sessionHandler.getUser(request);
            //非管理员用户只能够查看自己企业
            if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
                response.setStatusCode("-50");
                response.setResult(Boolean.FALSE.toString());
                response.setResultDesc("您不是系统管理员，没有添加企业权限");
                return response;
            }
            AuthUserResp resp = authUserService.verificationUserName(company.getContactTelephoneNumber());
            if (null != resp) {
                response.setResult(Boolean.FALSE.toString());
                response.setStatusCode("-50");
                response.setResultDesc("企业联系人手机号码已经存在");
                return response;
            }
            //企业名称唯一性校验
            TaxiBaseInfoCompany param = new TaxiBaseInfoCompany();
            param.setName(company.getName());
            PageInfo<TaxiBaseInfoCompany> pageInfo = companyService.queryCompanyList(param);
            List<TaxiBaseInfoCompany> companyList = pageInfo.getList();
            if (companyList != null && companyList.size() > 0) {
                response.setStatusCode("-50");
                response.setResult(Boolean.FALSE.toString());
                response.setResultDesc("企业" + company.getName() + "已经存在");
                return response;
            }
            param.setName(null);
            param.setOrganizationName(company.getOrganizationName());
            pageInfo = companyService.queryCompanyList(param);
            companyList = pageInfo.getList();
            if (companyList != null && companyList.size() > 0) {
                response.setStatusCode("-50");
                response.setResult(Boolean.FALSE.toString());
                response.setResultDesc("企业" + company.getName() + ",组织机构代码已经存在");
                return response;
            }
            param.setOrganizationName(null);
            param.setOperationPermitNumber(company.getOperationPermitNumber());
            pageInfo = companyService.queryCompanyList(param);
            companyList = pageInfo.getList();
            if (companyList != null && companyList.size() > 0) {
                response.setStatusCode("-50");
                response.setResult(Boolean.FALSE.toString());
                response.setResultDesc("企业" + company.getName() + ",经营许可证号已经存在");
                return response;
            }
            company.setCreateBy(authUser.getUserName());
            logInfo.setOperatorName(authUser.getUserName());//添加到操作日志
            //添加企业
            Long corpId = companyService.insertCompany(company);
            //添加企业管理员账号
            if (StringUtils.isNotBlank(company.getContactTelephoneNumber())) {
                AuthUser companyUser = new AuthUser();
                companyUser.setCorpId(corpId);
                companyUser.setUserStatus(Constants.AUTH_USER_STATUS);
                companyUser.setUserType(Constants.AUTH_USER_TYPE_SUPPLIER);
                companyUser.setUserName(company.getContactTelephoneNumber());
                companyUser.setMobilePhone(company.getContactTelephoneNumber());
                companyUser.setSource(Constants.DEAULT_TAXI_SOURCE);
                companyUser.setPassword(Constants.AUTH_USER_DEFAULT_PASSWORD);
                Long userId = userService.insertAuthUser(companyUser);
                AuthUserRole AuthUserRole = new AuthUserRole();
                AuthUserRole.setRoleId(3L);
                AuthUserRole.setUserId(userId);
                userRoleService.saveUserRole(AuthUserRole, authUser.getUserId());
            }
            response.setData(company);
            response.setStatusCode("00");
            response.setResult(Boolean.TRUE.toString());
            response.setResultDesc("添加企业信息成功");
            //同步企业信息到锐明
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synDataCompany(company, null, logInfo);
                    }
                }).start();
            } catch (Exception e) {
                log.info("调用同步企业信息的方法异常" + e.getMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setResult(Boolean.FALSE.toString());
            response.setStatusCode("-50");
            response.setResultDesc("添加企业信息异常");
        }
        return response;
    }


    @PutMapping("/updateCompany")
    @ApiOperation(value = "更新企业信息", notes = "参数company")
    public MessageResp updateCompany(HttpServletRequest request, @RequestBody TaxiBaseInfoCompany company) {
        MessageResp response = new MessageResp();
        TaxiOperationLog logInfo = new TaxiOperationLog();
        logInfo.setLogType(Constants.LOG_TYPE_U);
        try {
            AuthUserResp authUser = sessionHandler.getUser(request);
            //非管理员用户只能够查看自己企业
            if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())
                    && !StringUtils.equals(company.getUuid(), String.valueOf(authUser.getCorpId()))) {
                response.setStatusCode("-50");
                response.setResult(Boolean.FALSE.toString());
                response.setResultDesc("您不是系统管理员，不能更新其它企业信息");
                return response;
            }

            //由于需要同步数据至锐明处，对于企业名称修改需要将修改前的数据也同步过去，故多一步查询操作
            TaxiBaseInfoCompany oldCompany = companyService.selectCompanyInfo(company.getUuid());

            if (null != oldCompany && !oldCompany.getName().equals(company.getName())) {
                oldName = oldCompany.getName();
            }


            //企业名称唯一性校验
            TaxiBaseInfoCompany dbCompany = null;
            TaxiBaseInfoCompany param = new TaxiBaseInfoCompany();
            param.setName(company.getName());
            PageInfo<TaxiBaseInfoCompany> pageInfo = companyService.queryCompanyList(param);
            List<TaxiBaseInfoCompany> companyList = pageInfo.getList();
            if (companyList != null && companyList.size() > 0) {
                dbCompany = companyList.get(0);
                if (StringUtils.equals(dbCompany.getName(), company.getName())
                        && !StringUtils.equals(dbCompany.getUuid(), company.getUuid())) {
                    response.setStatusCode("-50");
                    response.setResult(Boolean.FALSE.toString());
                    response.setResultDesc("企业" + company.getName() + "已经存在");
                    return response;
                }
            }
            param.setName(null);
            param.setOrganizationName(company.getOrganizationName());
            pageInfo = companyService.queryCompanyList(param);
            companyList = pageInfo.getList();
            if (companyList != null && companyList.size() > 0) {
                dbCompany = companyList.get(0);
                if (StringUtils.equals(dbCompany.getOrganizationName(), company.getOrganizationName())
                        && !StringUtils.equals(dbCompany.getUuid(), company.getUuid())) {
                    response.setStatusCode("-50");
                    response.setResult(Boolean.FALSE.toString());
                    response.setResultDesc("企业" + company.getName() + ",组织机构代码已经存在");
                    return response;
                }
            }
            param.setOrganizationName(null);
            param.setOperationPermitNumber(company.getOperationPermitNumber());
            pageInfo = companyService.queryCompanyList(param);
            companyList = pageInfo.getList();
            if (companyList != null && companyList.size() > 0) {
                dbCompany = companyList.get(0);
                if (StringUtils.equals(dbCompany.getOperationPermitNumber(), company.getOperationPermitNumber())
                        && !StringUtils.equals(dbCompany.getUuid(), company.getUuid())) {
                    response.setStatusCode("-50");
                    response.setResult(Boolean.FALSE.toString());
                    response.setResultDesc("企业" + company.getName() + ",经营许可证号已经存在");
                    return response;
                }
            }
            company.setUpdateBy(authUser.getUserName());
            companyService.updateCompany(company);
            response.setData(company);
            response.setStatusCode("00");
            response.setResult(Boolean.TRUE.toString());
            response.setResultDesc("更新企业信息成功");
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synDataCompany(company, oldName, logInfo);
                    }
                }).start();
            } catch (Exception e) {
                log.info("调用同步企业信息的方法异常" + e.getMessage());
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            response.setResult(Boolean.FALSE.toString());
            response.setStatusCode("-50");
            response.setResultDesc("更新企业信息异常");
        }
        return response;
    }

    @DeleteMapping("/deleteCompany")
    @ApiOperation(value = "删除企业信息", notes = "参数company，系统管理员权限")
    public MessageResp deleteCompany(HttpServletRequest request, @RequestParam String uuid) {
        MessageResp response = new MessageResp();
        try {
            AuthUserResp authUser = sessionHandler.getUser(request);
            //非管理员用户只能够查看自己企业
            if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
                response.setStatusCode("-50");
                response.setResult(Boolean.FALSE.toString());
                response.setResultDesc("您不是系统管理员，没有删除企业权限");
                return response;
            }
            int count = companyService.queryBindVehicleCount(uuid);
            if (count > 0) {
                response.setResult(Boolean.FALSE.toString());
                response.setStatusCode("-50");
                response.setResultDesc("该企业已关联车辆和驾驶员，请勿删除");
                return response;
            }
            companyService.deleteCompany(uuid);
            response.setStatusCode("00");
            response.setResult(Boolean.TRUE.toString());
            response.setResultDesc("删除企业信息成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setResult(Boolean.FALSE.toString());
            response.setStatusCode("-50");
            response.setResultDesc("删除企业信息异常");
        }
        return response;
    }

    @GetMapping("/queryDistrictCompany")
    @ApiOperation(value = "按地区查询企业车辆", tags = "参数keyword， 按地区查询企业")
    public MessageResp queryDistrictCompany(HttpServletRequest request) {
        MessageResp response = new MessageResp();
        List<BaseDistrict> resultDis = new ArrayList<>();
        List<String> corpIds = new ArrayList<>();
        BaseCity city = new BaseCity();
        List<BaseDistrict> districtList = null;
        List<TaxiBaseInfoCompany> companyList = null;
        List<TaxiBaseInfoVehicle> vehicleList = null;
        try {
            //内置城市
            String cityName = Constants.DEAULT_CITY_NAME;
            Long cityId = dictionaryService.getCityId(cityName);
            city.setUuid(cityId);
            city.setCityName(cityName);
            city.setLabel(cityName);
            AuthUserResp authUser = sessionHandler.getUser(request);
            //非管理员用户只能够查看自己企业
            if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
                companyList = new ArrayList<>();
                districtList = new ArrayList<>();
                BaseDistrict district = null;
                Long corpId = authUser.getCorpId();
                corpIds.add(String.valueOf(corpId));
                //查询企业信息
                TaxiBaseInfoCompany company = companyService.selectCompanyInfo(String.valueOf(corpId));
                company.setTag("company");
                company.setLabel(company.getName());
                //查询企业所在区域
                if (StringUtils.isNotBlank(company.getDistrict())) {
                    district = dictionaryService.getDistrictById(company.getDistrict());
                    district.setLabel(district.getDisName());
                } else {
                    district = new BaseDistrict();
                }
                //查询企业车辆信息
                vehicleList = vehicleService.queryCompanyVehicleList(corpIds);
                vehicleList.forEach(car -> {
                    car.setLabel(car.getPlateNumber());
                });
                company.setChildren(vehicleList);
                companyList.add(company);
                district.setChildren(companyList);
                districtList.add(district);
            } else {
                districtList = dictionaryService.districtList("D", String.valueOf(cityId));
                companyList = this.districtCompanyList(cityName, null, corpIds);
                //企业和地区绑定
                Map<String, List<TaxiBaseInfoCompany>> comMap = new HashMap<>();
                companyList.forEach(company -> {
                    String districtId = company.getDistrict();
                    if (comMap.containsKey(districtId)) {
                        comMap.get(districtId).add(company);
                    } else {
                        List<TaxiBaseInfoCompany> comList = new ArrayList<>();
                        comList.add(company);
                        comMap.put(districtId, comList);
                    }
                });
                districtList.forEach(dis -> {
                    dis.setChildren(comMap.get(String.valueOf(dis.getUuid())));
                });
            }
            //过滤没有企业的地区
            districtList.forEach(dis -> {
                if (dis.getChildren() != null) {
                    resultDis.add(dis);
                }
            });
            city.setChildren(resultDis);
            response.setResult(Boolean.TRUE.toString());
            response.setData(city);
            response.setStatusCode("00");
            response.setResultDesc("查询地区企业车辆信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            response = CommonUtils.returnErrorInfo("查询地区企业车辆信息异常");
        }
        return response;
    }

    /**
     * 按地区查询企业和车辆
     *
     * @param cityName
     * @param keyWord
     * @param corpIds
     * @return
     * @throws Exception
     */
    private List<TaxiBaseInfoCompany> districtCompanyList(String cityName, String keyWord, List<String> corpIds) throws Exception {
        //获取所有企业
        List<TaxiBaseInfoCompany> companyList = companyService.queryDistrictCompany(cityName, keyWord);
        //获取所有企业车辆
        companyList.forEach(company -> corpIds.add(company.getUuid()));
        if (corpIds.size() > 0) {
            List<TaxiBaseInfoVehicle> vehicleList = vehicleService.queryCompanyVehicleList(corpIds);
            //企业和车辆匹配
            Map<String, List<TaxiBaseInfoVehicle>> carMap = new HashMap<>();
            vehicleList.forEach(vehicle -> {
                String corpId = vehicle.getCorpId();
                if (carMap.containsKey(corpId)) {
                    carMap.get(corpId).add(vehicle);
                } else {
                    List<TaxiBaseInfoVehicle> vehicles = new ArrayList<>();
                    vehicles.add(vehicle);
                    carMap.put(corpId, vehicles);
                }
            });
            companyList.forEach(company -> {
                company.setChildren(carMap.get(company.getUuid()));
            });
        }
        return companyList;
    }

    //同步企业信息到锐明
    private String synDataCompany(TaxiBaseInfoCompany company, String oldKey, TaxiOperationLog logInfo) {
        Map<String, String> header = new HashMap<>();
        header.put("userId", userId);
        header.put("userKey", userKey);
        header.put("Content-Type", "application/Json");
        String dataStr = null;
        String result = null;
        StringBuilder res = new StringBuilder();
        Map<String, Object> syncDataCompany = new HashMap<>();
        syncDataCompany.put("oldName", "");
        if (null != oldKey && !"".equals(oldKey)) {
            syncDataCompany.put("oldName", oldKey);
        }
        syncDataCompany.put("name", company.getName());
        syncDataCompany.put("fullName", company.getName());
        dataStr = "[" + JSONObject.toJSONString(syncDataCompany) + "]";
        logInfo.setOperation(dataStr);//记录操作内容
        logInfo.setLoginIp(syncUrl + "synCompany");
        result = HttpClientUtils.httpRequest(syncUrl + "synCompany", "POST", header, dataStr);
        if (!isSuccess(result)) {
            log.info("同步企业信息到锐明异常" + dataStr);
            logInfo.setResult(Constants.RESULT_TYPE_FALSE);
            logService.insertInfo(logInfo);//添加到操作日志表
            return res.append("同步企业信息到锐明异常").toString();
        }
        log.info("同步企业信息到锐明成功" + dataStr);
        res.append("同步企业信息到锐明成功");
        logInfo.setResult(Constants.RESULT_TYPE_TRUE);
        logService.insertInfo(logInfo);//添加到操作日志表
        return res.toString();
    }

    protected boolean isSuccess(String result) {
        boolean flag = false;
        if (null == result || "".equals(result)) {
            return flag;
        }
        JSONObject obj = JSONObject.parseObject(result);
        String success = obj.getString("success");
        if (success.equals("1")) {
            flag = true;
        } else {
            log.error(obj.getString("msg"));
        }
        return flag;
    }
}
