package com.car.authorization.controller.demo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.car.authorization.utils.CommonUtils;
import com.car.authorization.utils.MessageResp;
import com.car.authorization.utils.PageBeanUtil;
import com.car.carservice.dubbo.DemoDubboService;
import com.car.carservice.params.Demo;
import com.car.carservice.pojo.BaseDictionary;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/demo")
public class DemoController {
    private Logger log = LoggerFactory.getLogger(DemoController.class);

    @Reference(version = "${car.business.dubbo.version}", check = false)
    private DemoDubboService demoDubboService;



    @GetMapping("/demo1")
    @ApiOperation(value = "基本信息获取", notes = "")
    public MessageResp select(HttpServletRequest request, @ModelAttribute BaseDictionary baseDictionary) {
        MessageResp messageResp = new MessageResp();
        PageInfo<BaseDictionary> pageInfo = null;
        try {
            pageInfo = demoDubboService.select(baseDictionary);
            messageResp.setData(pageInfo.getList());
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }
}
