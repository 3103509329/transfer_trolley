package com.zhcx.authorization.controller.demo;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zhcx.authorization.config.SessionConfig.SessionHandler;
import com.zhcx.basicdata.facade.demo.DemoService;
import com.zhcx.basicdata.pojo.demo.Demo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/demo")
@Api(value = "demo", tags = "demo接口")
public class DemoContrllor {

	Logger logger = LoggerFactory.getLogger(DemoContrllor.class);
	
	@Resource
	private DemoService demoService;
	
	@ApiOperation(value = "demo测试方法", notes = "")
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String getOrgFileDataStatistics(HttpServletRequest request,String id){
		Demo demo = new Demo();
		demo.setUuid(id);
		String result = demoService.query(demo);
		logger.debug(result);
		return result;
	}
	

	
}
