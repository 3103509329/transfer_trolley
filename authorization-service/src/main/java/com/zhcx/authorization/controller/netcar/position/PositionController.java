
package com.zhcx.authorization.controller.netcar.position;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.*;
import com.zhcx.netcarbasic.facade.netcar.*;
import com.zhcx.netcarbasic.params.VehiclePositionParam;
import com.zhcx.netcarbasic.pojo.netcar.*;
import com.zhcx.netcarbasic.pojo.base.BaseCity;
import com.zhcx.netcarbasic.pojo.base.BaseDistrict;
import com.zhcx.regionmonitor.response.MonitorResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/12/11 14:35
 **/

@RestController
@RequestMapping("/netcar/position")
@Api(value = "网约车定位监控", tags = "网约车定位监控")
public class PositionController {

    private static final Logger log = LoggerFactory.getLogger(PositionController.class);

    @Autowired
    private BaseDictionaryService dictionaryService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private CompanyServiceService companyService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private NetcarOnOffLineService netcarOnOffLineService;

    @Resource(name = "redisTemplate4Json")
    private RedisTemplate<String, String> redisTemplate;


    @GetMapping("/queryDistrictCompany")
    @ApiOperation(value = "网约车按地区查询车辆", notes = "")
    public MessageResp queryDistrictCompany(HttpServletRequest request) {
        MessageResp response = new MessageResp();
        BaseCity city = new BaseCity();
        List<String> companyIds = new ArrayList<>();
        List<NetcarBaseInfoCompanyService> companyList = null;
        List<NetcarBaseInfoVehicle> vehicleList = null;
        List<BaseDistrict> districtList = Lists.newArrayList();
        List<BaseDistrict> resultDis = new ArrayList<>();
        try {
            String provinceName = "湖南省";
            String address = "430900";
            //内置城市
            String cityName = Constants.DEAULT_NET_CAR_CITY_NAME;
            Long cityId = dictionaryService.getCityId(cityName);
            city.setCityName(provinceName);
            city.setUuid(cityId);
            city.setLabel(provinceName);
            AuthUserResp authUser = sessionHandler.getUser(request);
            //非管理员用户只能够查看自己企业
            if (null != authUser && !Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
                companyList = new ArrayList<>();
                Long companyId = authUser.getCorpId();
                companyIds.add(String.valueOf(companyId));
                //查询企业信息
                NetcarBaseInfoCompanyService company = companyService.selectByCompanyId(String.valueOf(companyId));
                company.setTag("company");
                company.setLabel(company.getServiceName());
                //查询企业车辆信息
                vehicleList = vehicleService.queryCompanyVehicleList(companyIds);
                vehicleList.forEach(car -> {
                    car.setLabel(car.getVehicleNo());
                    car.setTag("vehicle");
                });
                company.setVehicleList(vehicleList);
                companyList.add(company);
                BaseDistrict district  = dictionaryService.getDistrictByAddress(company.getAddress());
                district.setLabel(cityName);
                district.setChildren(companyList);
                districtList.add(district);
            } else {
                //企业和地区绑定
                Map<Long, List<NetcarBaseInfoCompanyService>> comMap = new HashMap<>();
                companyList = this.districtCompanyList(address);
                companyList.forEach(company -> {
                    company.setTag("company");
                    company.setLabel(company.getServiceName());
                    if (comMap.containsKey(cityId)) {
                        comMap.get(cityId).add(company);
                    } else {
                        List<NetcarBaseInfoCompanyService> comList = new ArrayList<>();
                        comList.add(company);
                        comMap.put(cityId, comList);
                    }
                });
                BaseDistrict district = new BaseDistrict();
                district.setLabel(cityName);
                district.setDisName(cityName);
                district.setCorpId(cityId);
                districtList.add(district);
                districtList.forEach(dis -> {
                    dis.setLabel(dis.getDisName());
                    dis.setChildren(comMap.get(dis.getCorpId()));
                });
            }
            //过滤没有企业的地区
            districtList.forEach(dis -> {
                dis.setLabel(dis.getDisName());
                if (dis.getChildren() != null) {
                    resultDis.add(dis);
                }
            });
            city.setChildren(resultDis);
            response.setData(city);
            response.setStatusCode("00");
            response.setResult(Boolean.TRUE.toString());
            response.setResultDesc("查询企业车辆信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return response;
    }


    /**
     * 按地区查询企业和车辆
     *
     * @param address
     * @param
     * @return
     * @throws Exception
     */

    private List<NetcarBaseInfoCompanyService> districtCompanyList(String address) throws Exception {
        //获取所有企业
        List<NetcarBaseInfoCompanyService> companyList = Lists.newArrayList();
        List<NetcarBaseInfoVehicle> vehicleList = vehicleService.queryVehicleListByAddress(address);
        //企业和车辆匹配
        Map<String, List<NetcarBaseInfoVehicle>> carMap = new HashMap<>();
        Set<String> corpIds = Sets.newHashSet();
        if (null != vehicleList) {
            vehicleList.forEach(vehicle -> {
                String companyId = vehicle.getCompanyId();
                vehicle.setTag("vehicle");
                vehicle.setLabel(vehicle.getVehicleNo());
                corpIds.add(companyId);
                if (carMap.containsKey(companyId)) {
                    carMap.get(companyId).add(vehicle);
                } else {
                    List<NetcarBaseInfoVehicle> vehicles = new ArrayList<>();
                    vehicles.add(vehicle);
                    carMap.put(companyId, vehicles);
                }
            });
            companyList = companyService.queryCompanyListByIds(corpIds);

            companyList.forEach(company -> {
                company.setChildren(carMap.get(company.getCompanyId()));
            });
        }
        return companyList;
    }

    @GetMapping("/queryRealTimePosition")
    @ApiOperation(value = "网约车实时位置", notes = "网约车实时位置")
    public MessageResp queryRealTimePosition(@RequestParam String vehicleNo) {
        MessageResp messageResp = new MessageResp();
        try {
            if (StringUtils.isBlank(vehicleNo)) {
                return CommonUtils.returnErrorInfo("车辆号牌不能为空");
            }
            NetcarPositionVehicle netcarPositionVehicle = positionService.queryRealTimePosition(vehicleNo);
            messageResp.setData(netcarPositionVehicle);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }

        return messageResp;
    }

    @PostMapping("/queryHistoryPosition")
    @ApiOperation(value = "网约车历史位置", notes = "网约车历史位置")
    public MessageResp queryHistoryPosition(@RequestBody VehiclePositionParam vehiclePositionParam) {

        String vehicleNo = vehiclePositionParam.getVehicleNo();
        Long ts = vehiclePositionParam.getTs();
        Integer pageNo = vehiclePositionParam.getPageNo();
        Integer pageSize = vehiclePositionParam.getPageSize();
        MessageResp messageResp = new MessageResp();
        List<NetcarPositionVehicle> list = new ArrayList<>();
        try {
            if (StringUtils.isBlank(vehicleNo)) {
                return CommonUtils.returnErrorInfo("车辆号牌不能为空");
            }
            //获取1分钟时间内的数据
//            BoundZSetOperations boundZSetOperations = redisTemplate.boundZSetOps(vehicleNo);
//            Set<String> positionSet = boundZSetOperations.range(pageNo, pageSize);
//            if (positionSet.size() == 0) {
//                int total = positionService.selectList(vehicleNo, ts);
//                positionSet = boundZSetOperations.range(pageNo, pageSize);
//            }
//            for (String positionStr : positionSet) {
//                NetcarPositionVehicle netcarPositionVehicle = JSON.parseObject(positionStr, NetcarPositionVehicle.class);
//                list.add(netcarPositionVehicle);
//            }

            list =  positionService.queryHistoryPosition(vehicleNo, ts);
            messageResp.setResultDesc("查询成功");
            messageResp.setData(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }

        return messageResp;
    }

    @PostMapping("/onOffLine")
    @ApiOperation(value = "查询网约车上下线状态", notes = "查询网约车上下线状态")
    public MessageResp queryVehicleOnOffLine(@RequestBody Map param) {
        MessageResp messageResp = new MessageResp();
        try {
            String vehicleNos = String.valueOf(param.get("vehicleNos"));
            if (StringUtils.isBlank(vehicleNos)) {
                return CommonUtils.returnErrorInfo("车辆号牌不能为空");
            }
            List<NetcarOnOffLine> list = netcarOnOffLineService.getVehicleOnOffLineList(vehicleNos);
            messageResp.setResultDesc("查询成功");
            messageResp.setData(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    @GetMapping("/driverVehicleInfo")
    @ApiOperation(value = "查询网约车驾驶员和车辆信息", notes = "查询网约车驾驶员和车辆信息")
    public MessageResp queryDriverVehicleInfo(@RequestParam String licenseId, @RequestParam String vehicleNo, @RequestParam(required = false) String companyId) {
        MessageResp messageResp = new MessageResp();
        try {
            if (StringUtils.isBlank(licenseId) || StringUtils.isBlank(vehicleNo)) {
                return CommonUtils.returnErrorInfo("驾驶证或车牌号不能为空");
            }
            Map<String, Object> resultMap = Maps.newHashMap();
            NetcarBaseInfoDriver driverInfo = driverService.selectDriverInfoByCompanyId(companyId,licenseId);
            resultMap.put("driverInfo", driverInfo);
            NetcarBaseInfoVehicle vehicleInfo = vehicleService.selectVehicleInfo(companyId,vehicleNo);
            resultMap.put("vehicleInfo", vehicleInfo);
            messageResp.setResultDesc("查询成功");
            messageResp.setResMap(resultMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }


    @GetMapping("/vehicle")
    @ApiOperation(value = "网约车定位信息", notes = "网约车定位信息")
    public MessageResp queryVehiclePosition(@RequestParam String vehicleNo,
                                            @RequestParam(required = false) Long ts,
                                            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                            @RequestParam(required = false, defaultValue = "30") Integer pageSize) {
        MessageResp messageResp = new MessageResp();
        try {
            if (StringUtils.isBlank(vehicleNo)) {
                return CommonUtils.returnErrorInfo("车辆号牌不能为空");
            }
            List list = positionService.queryHistoryPosition(vehicleNo, ts);
            long totalCount = list.size();
            List data = getListPage(pageNo, pageSize, list);
            PageBean pageBean = new PageBean();
            pageBean.setPageNo(String.valueOf(pageNo));
            pageBean.setPageSize(String.valueOf(pageSize));
            pageBean.setPageCount(String.valueOf(totalCount / pageSize + 1));
            pageBean.setPageDataCount(String.valueOf(list.size()));
            messageResp.setPageBean(pageBean);
            messageResp.setResultDesc("查询成功");
            messageResp.setData(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }

        return messageResp;
    }

    @GetMapping("/driver")
    @ApiOperation(value = "驾驶员位置信息查询", notes = "驾驶员位置信息查询")
    public MessageResp queryDriverPosition(@RequestParam String licenseId,
                                           @RequestParam(required = false) Long ts,
                                           @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                           @RequestParam(required = false, defaultValue = "30") Integer pageSize) {
        MessageResp messageResp = new MessageResp();
        try {
            if (StringUtils.isBlank(licenseId)) {
                return CommonUtils.returnErrorInfo("驾驶照号不能为空");
            }

            List list = positionService.queryDriverPosition(licenseId, ts);
            long totalCount = list.size();
            List data = getListPage(pageNo, pageSize, list);
            PageBean pageBean = new PageBean();
            pageBean.setPageNo(String.valueOf(pageNo));
            pageBean.setPageSize(String.valueOf(pageSize));
            pageBean.setPageCount(String.valueOf(totalCount / pageSize + 1));
            pageBean.setPageDataCount(String.valueOf(list.size()));

            messageResp.setPageBean(pageBean);
            messageResp.setResultDesc("查询成功");
            messageResp.setData(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }

        return messageResp;
    }


    /**
     * list分页展示
     */

    public List getListPage(int pageNo, int pageSize, List<Object> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        int totalCount = list.size();
        pageNo = pageNo - 1;
        int fromIndex = pageNo * pageSize;
        //分页不能大于总数
        if (fromIndex >= totalCount) {
//            throw new ServiceException("页数或分页大小不正确!");
            return null;
        }

        int toIndex = ((pageNo + 1) * pageSize);
        if (toIndex > totalCount) {
            toIndex = totalCount;
        }
        return list.subList(fromIndex, toIndex);
    }


    /**
     * 返回总页数
     */

    public int getPages(List<Object> obj, Integer pageSize) {
        int count = obj.size() / pageSize;
        if (obj.size() == 0) {
            return 0;
        }
        if (obj.size() <= pageSize) {
            return 1;
        } else if (count % pageSize == 0) {
            return count;
        } else {
            return count + 1;
        }

    }

}

