package com.zhcx.authorization.controller.netcar.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.*;
import com.zhcx.netcarbasic.facade.netcar.CompanyFareService;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoCompanyFare;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 11:21
 */
@RestController
@RequestMapping("/netcar/baseinfo/companyfare")
@Api(value = "companyfare",tags = "平台公司运价信息")
public class CompanyFareController {

    private Logger log = LoggerFactory.getLogger(CompanyFareController.class);

    @Autowired
    private CompanyFareService companyFareService;

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;
    /**
     * 平台公司运价信息查询
     * @return
     */
    @ApiOperation(value = "平台公司运价信息查询", notes = "")
    @GetMapping
    public MessageResp selectCompanyFareList(HttpServletRequest request,
                                          @RequestParam(required = false) String companyId,
                                          @RequestParam(defaultValue = "1") Integer pageNo,
                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                          @RequestParam(required = false) String orderBy){

        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoCompanyFare> pageInfo = null;
        try{
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            pageInfo = companyFareService.selectCompanyFareList(companyId,pageNo,pageSize,orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

}
