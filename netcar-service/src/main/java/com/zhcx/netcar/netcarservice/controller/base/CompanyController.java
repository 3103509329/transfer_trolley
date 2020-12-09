package com.zhcx.netcar.netcarservice.controller.base;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zhcx.basic.dubbo.BaseInfoCompanyDubboService;
import com.zhcx.basic.facade.BaseInfoCompanyService;
import com.zhcx.basic.model.BaseInfoCompany;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import com.zhcx.netcar.vo.CompanyNameVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 10:58
 */
@RestController
@RequestMapping("/netcar/baseinfo/company")
@Api(value = "company", tags = "平台基本信息")
public class CompanyController {
    private Logger log = LoggerFactory.getLogger(CompanyController.class);

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoCompanyDubboService baseInfoCompanyService;

    @ApiOperation(value = "平台基本信息获取", notes = "")
    @GetMapping
    public MessageResp select(HttpServletRequest request,
                              @RequestParam(required = false) String companyId,
                              @RequestParam(required = false) String keyword,
                              @RequestParam(required = false) String type,
                              @RequestParam(required = false) Integer source,
                              @RequestParam(defaultValue = "1") Integer pageNo,
                              @RequestParam(defaultValue = "30") Integer pageSize,
                              @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        PageInfo<BaseInfoCompany> pageInfo = null;
        try {
            source = null;
            pageInfo = baseInfoCompanyService.select(companyId, keyword, type, source, pageNo, pageSize, orderBy);
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
