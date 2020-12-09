package com.zhcx.netcar.netcarservice.controller.yz;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zhcx.basic.dubbo.BaseInfoCompanyDubboService;
import com.zhcx.basic.model.BaseInfoCompany;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 企业
 * 运政数据源
 *
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/3/7 0007 16:42
 **/
@RestController
@RequestMapping("/netcar/yz/company")
public class YzCompanyController {

    private Logger log = LoggerFactory.getLogger(YzCompanyController.class);

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoCompanyDubboService baseInfoCompanyService;

    /**
     * 查询（运政）
     *
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping
    public MessageResp selectCompany(HttpServletRequest request,
                                     @RequestParam String keyword,
                                     @RequestParam(defaultValue = "1") Integer pageNo,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false) String orderBy,
                                     @RequestParam(required = false) Integer dataType) {
        MessageResp messageResp = new MessageResp();
        PageInfo<BaseInfoCompany> pageInfo = null;
        try {
            Integer source = null;
            if (dataType != null && !dataType.equals("")) {
                source = dataType;
            }
            orderBy = "time_created_desc";
            pageInfo = baseInfoCompanyService.select(null, keyword, null, source, pageNo, pageSize, orderBy);
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
