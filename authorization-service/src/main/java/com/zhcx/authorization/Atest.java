package com.zhcx.authorization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhcx.authorization.utils.HttpClientUtils;

public class Atest {
	public static void main(String[] args) {
		String url = "http://172.16.102.91/api/plugins/telemetry/DEVICE/9c55edd0-9baf-11e9-9c6a-91ecf8e6b751/values/timeseries?timeseries?startTs='1563292800000'&endTs='1563379140000'&limit=2000000&interval=1000&keys=K512&agg=NONE";
		Map<String, String> map = new HashMap<String, String>();
		map.put("X-Authorization", "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsdW5faGFvQDEyM2N4LmNvbSIsInNjb3BlcyI6WyJURU5BTlRfQURNSU4iXSwidXNlcklkIjoiYzhiNTA0YjAtZjM4MC0xMWU4LTgyNzItNjc2OTA2OTIxYzliIiwiZmlyc3ROYW1lIjoi5LymIiwibGFzdE5hbWUiOiLpg50iLCJlbmFibGVkIjp0cnVlLCJpc1B1YmxpYyI6ZmFsc2UsInRlbmFudElkIjoiYWQxMzgwNjAtZjM4MC0xMWU4LTgyNzItNjc2OTA2OTIxYzliIiwiY3VzdG9tZXJJZCI6IjEzODE0MDAwLTFkZDItMTFiMi04MDgwLTgwODA4MDgwODA4MCIsImlzcyI6InRoaW5nc2JvYXJkLmlvIiwiaWF0IjoxNTY0NDY3ODY5LCJleHAiOjE1NjUzNjc4Njl9.6s-axc6_Ao2Vkdo_N-54_SdT-UlPP0g1cb4DlpQ4a_CIVcZvxLbJq1MXPk_OnQycoAexoUKyA_7O0zC1UvJRUw");
		map.put("Content-Type", "application/Json; charset=UTF-8");
		String resp = null;
		try{
			resp = HttpClientUtils.httpRequest(url, "GET", map, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject rObj = JSONObject.parseObject(resp);
		//JSONArray arrys = rObj.getJSONArray("K512");
		String str = rObj.getString("K512");
		List list = JSONObject.parseArray(str);
		for (Object object : list) {
			JSONObject jt = JSONObject.parseObject(JSON.toJSONString(object));
			JSONObject gps = JSONObject.parseObject(jt.getString("value"));
			System.out.println(gps.getString("LONGITUDE"));
		}
		System.out.println("json-----------------"+resp);
	}
}
