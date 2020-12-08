package com.zhcx.netcar.netcarservice.controller.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.base.CompanyService;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.RoleAuthenticationUtils;
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

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-25 14:04
 */
@RestController
@RequestMapping("/netcar/baseinfo/companys")
@Api(value = "companys", tags = "平台基本信息（获取公司信息）")
public class CompanyListController {

    private Logger log = LoggerFactory.getLogger(CompanyListController.class);

    @Autowired
    private CompanyService companyService;

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;

    /**
     * 平台基本信息查询
     *
     * @return
     */
    @ApiOperation(value = "平台基本信息获取(所有)", notes = "")
    @GetMapping
    public MessageResp selectCompanyIdAndName(HttpServletRequest request,
                                              @RequestParam(required = false) String companyId,
                                              @RequestParam(required = false) String keyword) {
        MessageResp messageResp = new MessageResp();
        PageInfo<CompanyNameVo> pageInfo = null;
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            pageInfo = companyService.selectCompanyIdAndName(companyId, keyword);
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
}
