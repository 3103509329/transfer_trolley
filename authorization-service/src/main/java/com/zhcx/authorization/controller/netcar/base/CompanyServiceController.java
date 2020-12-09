package com.zhcx.authorization.controller.netcar.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.*;
import com.zhcx.netcarbasic.facade.netcar.CompanyServiceService;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoCompany;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 11:11
 */
@RestController
@RequestMapping("/netcar/baseinfo/companyservice")
@Api(value = "companyservice", tags = "平台公司服务机构")
public class CompanyServiceController {


    private Logger log = LoggerFactory.getLogger(CompanyServiceController.class);

    @Autowired
    private CompanyServiceService companyServiceService;

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;

    /**
     * 平台公司服务机构查询
     *
     * @return
     */
    @ApiOperation(value = "平台公司服务机构查询", notes = "")
    @GetMapping
    public MessageResp selectCompanyServiceList(HttpServletRequest request,
                                                @RequestParam(required = false) String companyId,
                                                @RequestParam(defaultValue = "1") Integer pageNo,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(required = false) String orderBy) {

        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoCompanyService> pageInfo = null;
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            pageInfo = companyServiceService.selectCompanyServiceList(companyId, pageNo, pageSize, orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }


    /**
     * 平台公司服务机构查询（不合规）
     *
     * @return
     */
    @ApiOperation(value = "平台公司服务机构查询（不合规）", notes = "")
    @GetMapping("/illegal")
    public MessageResp selectIllegalCompanyServiceList(HttpServletRequest request,
                                                @RequestParam(required = false) String companyId,
                                                @RequestParam(defaultValue = "1") Integer pageNo,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(required = false) String orderBy) {

        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoCompanyService> pageInfo = null;
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            pageInfo = companyServiceService.selectIllegalCompanyServiceList(companyId, pageNo, pageSize, orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    /**
     * 平台基本信息查询
     *
     * @return
     */
    @ApiOperation(value = "平台基本信息获取", notes = "")
    @GetMapping("/name")
    public MessageResp selectCompanyIdAndName(HttpServletRequest request,
                                              @RequestParam(required = false) String companyId,
                                              @RequestParam(required = false) String keyword) {
        MessageResp messageResp = new MessageResp();
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            List list = companyServiceService.selectCompanyIdAndName(companyId, keyword);
            messageResp.setData(list);
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

