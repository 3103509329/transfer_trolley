/*package com.zhcx.authorization.controller.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoVehicleService;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoVehicle;
import com.zhcx.network.facade.GpsService;
import com.zhcx.network.facade.OnLineService;
import com.zhcx.network.pojo.GpsMessage;
import com.zhcx.network.pojo.OnLineMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/gps")
@Api(value = "车辆轨迹", tags = "车辆轨迹信息")
public class GpsController {
	
    @Autowired
    private GpsService gpsService;
	
	@ApiOperation(value = "获取车辆轨迹信息", notes = "")
	@RequestMapping(value = "/deviceId/{id}/vehicles/location", method = RequestMethod.GET)
	public MessageResp<Map<String, Object>> queryOnLineVehLocation(@PathVariable("id") String id) {
		MessageResp<Map<String, Object>> resp = new MessageResp<Map<String, Object>>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List corps = new ArrayList<String>();
		corps.add(id.toString());
		if(null == id){
			resp.setStatusCode("-50");
        	resp.setResult("false");
			resp.setResultDesc("请选择车辆!");
			return resp;
		}
		try {
			Map map = new HashMap<String, Object>();
			GpsMessage gpsMessage = gpsService.findGps(id);
			map.put(id, gpsMessage);
			resp.setData(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	

}
*/