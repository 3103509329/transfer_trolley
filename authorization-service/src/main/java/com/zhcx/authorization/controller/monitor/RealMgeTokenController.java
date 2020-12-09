package com.zhcx.authorization.controller.monitor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zhcx.authorization.common.IotOperationCommon;
import com.zhcx.authorization.config.IotConfig;
import com.zhcx.authorization.utils.OkHttpUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import okhttp3.MediaType;
import okhttp3.Response;

@RestController
@RequestMapping("/real")
@Api(value = "IOT鉴权获取token", tags = "")
public class RealMgeTokenController{

	private static final Logger logger = LoggerFactory
			.getLogger(RealMgeTokenController.class);
	/*@Autowired
	private IotConfig iotConfig;*/
	
	@Autowired
	private IotOperationCommon iotOperationCommon;
/*	
	@Autowired
    private OkHttpUtil okHttpUtil;*/

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "获取token", notes = "")
	@RequestMapping(value = "/author", method = RequestMethod.GET)
	public String timeseries(HttpServletRequest request) {
		// 鉴权
		String token = iotOperationCommon.doLoginIot();
        return token;
	}

}
