package com.zhcx.netcar.netcarservice.controller.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.netcarservice.utils.RoleAuthenticationUtils;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyPermit;
import com.zhcx.netcar.facade.base.CompanyPermitService;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
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
 * @date 2018-11-24 11:14
 */
@RestController
@RequestMapping("/netcar/baseinfo/companypermit")
@Api(value = "companypermit", tags = "网约车企业经营许可证接口")
public class CompanyPermitController {

    private Logger log = LoggerFactory.getLogger(CompanyPermitController.class);

    @Autowired
    private CompanyPermitService companyPermitService;

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;

    /**
     * 公司经营许可信息查询
     *
     * @return
     */
    @ApiOperation(value = "公司经营许可信息查询", notes = "")
    @GetMapping
    public MessageResp getCompanyPermitList(HttpServletRequest request,
                                            @RequestParam(required = false) String companyId,
                                            @RequestParam(defaultValue = "1") Integer pageNo,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @RequestParam(required = false) String orderBy) {
        MessageResp messageResp = new MessageResp();
        PageInfo<NetcarBaseInfoCompanyPermit> pageInfo = null;
        try {
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);
            pageInfo = companyPermitService.getCompanyPermitList(companyId, pageNo, pageSize, orderBy);
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
