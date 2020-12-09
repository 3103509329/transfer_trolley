package com.zhcx.authorization.controller.monitor;

import com.zhcx.authorization.config.IotConfig;
import com.zhcx.authorization.utils.DateUtil;
import com.zhcx.authorization.utils.HttpClientUtils;
import com.zhcx.authorization.utils.JsonParseUtils;
import com.zhcx.authorization.utils.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/history")
@Api(value = "车辆轨迹", tags = "")
public class HistoryController{

	private static final Logger logger = LoggerFactory
			.getLogger(HistoryController.class);
	@Autowired
	private IotConfig iotConfig;
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "获取车辆轨迹", notes = "")
	@RequestMapping(value = "/timeseries", method = RequestMethod.GET)
	public String timeseries(@RequestParam String date, @RequestParam String deviceId) {
		Date changeDate=null;
		try{
			changeDate= DateUtil.getYMDFormat(date);
		}catch(Exception e){
			logger.info("日期转换失败"+e.getMessage());
		}
		User user = new User();
		user.setUsername(iotConfig.getUsername());
		user.setPassword(iotConfig.getPassword());
		Map<String, Object> mapJson = null;
		long startTs = DateUtil.getTimesMorning(changeDate).getTime();
		long endTs = DateUtil.getTimesnight(changeDate).getTime();
		try {
			Map<String, String> headerParams = new HashMap<String, String>();
			headerParams.put("Content-Type", "application/Json; charset=UTF-8");
			String json = HttpClientUtils.httpRequest(
					iotConfig.getUrl()+"/api/auth/login", "POST",
					headerParams, JsonParseUtils.bean2Json(user));
			mapJson = JsonParseUtils.json2Bean(json, Map.class);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		String token = (String) mapJson.get("token");
		if (token == null) {
			return null;
		}
		String url = iotConfig.getUrl()+"/api/plugins/telemetry/DEVICE/"
				+ deviceId + "/values/" + "timeseries?" + "startTs=" + startTs
				+ "&endTs=" + endTs + "&keys=" + iotConfig.getKeys() + "&agg=" + iotConfig.getAgg()
				+ "&interval=" + iotConfig.getInterval() + "&limit=" + iotConfig.getLimit();
		Map<String, String> map = new HashMap<String, String>();
		map.put("X-Authorization", "Bearer " + token);
		map.put("Content-Type", "application/Json; charset=UTF-8");
		String resp = null;
		try{
			resp = HttpClientUtils.httpRequest(url, "GET", map, null);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return resp;
	}

	@RequestMapping(value = "/vehicleTrajectory", method = RequestMethod.GET)
	public String vehicleTrajectory (@RequestParam String startDate,@RequestParam String endDate, @RequestParam String deviceId) {
		Date changeDate1=null;
		Date changeDate2=null;

		try{
			changeDate1= DateUtil.getYMDHMSFormat(startDate);
			changeDate2= DateUtil.getYMDHMSFormat(endDate);
		}catch(Exception e){
			logger.info("日期转换失败"+e.getMessage());
		}
		User user = new User();
		user.setUsername(iotConfig.getUsername());
		user.setPassword(iotConfig.getPassword());
		Map<String, Object> mapJson = null;
		long startTs = changeDate1.getTime();
		long endTs = changeDate2.getTime();
		try {
			Map<String, String> headerParams = new HashMap<String, String>();
			headerParams.put("Content-Type", "application/Json; charset=UTF-8");
			String json = HttpClientUtils.httpRequest(
					iotConfig.getUrl()+"/api/auth/login", "POST",
					headerParams, JsonParseUtils.bean2Json(user));
			mapJson = JsonParseUtils.json2Bean(json, Map.class);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		String token = (String) mapJson.get("token");
		if (token == null) {
			return null;
		}
		String url = iotConfig.getUrl()+"/api/plugins/telemetry/DEVICE/"
				+ deviceId + "/values/" + "timeseries?" + "startTs=" + startTs
				+ "&endTs=" + endTs + "&keys=" + iotConfig.getKeys() + "&agg=" + iotConfig.getAgg()
				+ "&interval=" + iotConfig.getInterval() + "&limit=" + iotConfig.getLimit();
		Map<String, String> map = new HashMap<String, String>();
		map.put("X-Authorization", "Bearer " + token);
		map.put("Content-Type", "application/Json; charset=UTF-8");
		String resp = null;
		try{
			resp = HttpClientUtils.httpRequest(url, "GET", map, null);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return resp;
	}

}
