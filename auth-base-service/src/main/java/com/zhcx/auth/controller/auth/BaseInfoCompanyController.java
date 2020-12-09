package com.zhcx.auth.controller.auth;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhcx.auth.config.SessionConfig;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.auth.utils.MessageResp;
import com.zhcx.auth.utils.UserAuthUtils;
import com.zhcx.basic.dubbo.BaseInfoCompanyDubboService;
import com.zhcx.basic.model.BaseInfoCompany;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description: 企业基础信息
 * @author: qzq
 * @date 2020-04-15 14:28
 **/
@RestController
@RequestMapping("/company")
@Api(value = "BaseInfoCompany", tags = "企业基础信息")
public class BaseInfoCompanyController {

    private static final Logger log = LoggerFactory.getLogger(BaseInfoCompanyController.class);

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoCompanyDubboService baseInfoCompanyService;

//    @Autowired
//    private SessionConfig.SessionHandler sessionHandler;


    @GetMapping(value = "/list")
    @ApiOperation(value = "企业基础信息查询-获取当前用户的权限内的所有企业", notes = "")
    public MessageResp selelctByRole(HttpServletRequest request, @ModelAttribute BaseInfoCompany param) throws Exception {

        MessageResp<List<BaseInfoCompany>> message = new MessageResp<List<BaseInfoCompany>>();
        try {
             AuthUserResp authUser = UserAuthUtils.getUser();
            if (null != authUser && "01".equals(authUser.getAccountType())) {
                authUser.setUserId(null);
            }
            List<BaseInfoCompany> result = baseInfoCompanyService.selelctByRole(authUser.getUserId());
            message.setData(result);
            message.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            message.setResult(Boolean.FALSE.toString());
            message.setResultDesc("查询异常");
        }

        return message;
    }
}
