package com.zhcx.netcar.netcarservice.controller.app;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.app.TrafficAssessmentService;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import com.zhcx.netcar.pojo.app.NetcarAppTrafficAssessment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/5/18 15:21
 * 交通考核
 **/

@RestController
@RequestMapping("/netcar/app/assessment")
public class TrafficAssessmentController {

    @Autowired
    private TrafficAssessmentService trafficAssessmentService;

    @GetMapping
    public MessageResp queryTrafficAssessmentList(HttpServletRequest request,
                                                  @RequestParam(required = false) String branum,
                                                  @RequestParam(defaultValue = "1") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(required = false) String orderBy,
                                                  @RequestParam(required = false) String keyword) {

        MessageResp messageResp = new MessageResp();
        try {
            PageInfo<NetcarAppTrafficAssessment> pageInfo = trafficAssessmentService.queryTrafficAssessmentList(pageNo, pageSize, orderBy, keyword, branum);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;

    }

    @PostMapping
    public MessageResp insertTrafficAssessment(HttpServletRequest request,
                                                  @RequestBody NetcarAppTrafficAssessment param) {

        MessageResp messageResp = new MessageResp();
        try {
           int i = trafficAssessmentService.insertTrafficAssessment(param);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("保存失败");
        }
        return messageResp;

    }

}
