package com.zhcx.authorization.controller.netcar.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.*;
import com.zhcx.netcarbasic.facade.netcar.CompanyStatService;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoCompanyStat;
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
 * @date 2018-11-24 11:03
 */
@RestController
@RequestMapping("/netcar/baseinfo/companystat")
@Api(value = "companystat", tags = "平台公司营运规模信息")
public class CompanyStatController {

    private Logger log = LoggerFactory.getLogger(CompanyStatController.class);

    @Autowired
    private CompanyStatService companyStatService;


    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;
    /**
     * 平台公司营运规模信息查询
     *
     * @return
     */
    @ApiOperation(value = "平台公司营运规模信息查询", notes = "")
    @GetMapping
    public MessageResp selectCompanyStatList(HttpServletRequest request,
                                             @RequestParam(required = false) String companyId,
                                             @RequestParam(defaultValue = "1") Integer pageNo,
                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                             @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoCompanyStat> pageInfo = null;
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            pageInfo = companyStatService.selectCompanyStatList(companyId, pageNo, pageSize, orderBy);
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
}
