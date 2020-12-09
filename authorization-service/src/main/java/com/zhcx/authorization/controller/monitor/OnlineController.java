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
import com.zhcx.network.pojo.OnLineMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/online")
@Api(value = "车辆在离线", tags = "车辆在离线状态")
public class OnlineController {
	
    @Autowired
    private TaxiBaseInfoVehicleService vehicleService;
    
    @Autowired
    private OnLineService onLineService;
    
    @Autowired
    private GpsService gpsService;
	
	@ApiOperation(value = "获取单个企业车辆在离线信息", notes = "")
	@RequestMapping(value = "/corp/{id}/vehicles/location", method = RequestMethod.GET)
	public MessageResp<List<Map<String, Object>>> queryOnLineVehLocation(@PathVariable("id") Long id) {
		MessageResp<List<Map<String, Object>>> resp = new MessageResp<List<Map<String, Object>>>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List corps = new ArrayList<String>();
		corps.add(id.toString());
		if(null == id){
			resp.setStatusCode("-50");
        	resp.setResult("false");
			resp.setResultDesc("请选择企业!");
			return resp;
		}
		try {
			List<TaxiBaseInfoVehicle> ves = vehicleService.queryCompanyVehicleList(corps);
			for (TaxiBaseInfoVehicle taxiBaseInfoVehicle : ves) {
				OnLineMessage onLineMessage = onLineService.getOnLineMessage(taxiBaseInfoVehicle.getDeviceId());
				Map map = new HashMap<String, Object>();
				map.put(taxiBaseInfoVehicle.getDeviceId(), onLineMessage);
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	

}
*/