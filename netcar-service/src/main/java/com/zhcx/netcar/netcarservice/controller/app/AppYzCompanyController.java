package com.zhcx.netcar.netcarservice.controller.app;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.yunzheng.YunZhengCompanyService;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import com.zhcx.netcar.pojo.yuzheng.YunZhengCompany;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 企业
 * 运政数据源
 *
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/3/7 0007 16:42
 **/
@RestController
@RequestMapping("/netcar/app/company")
@Api(value = "company", tags = "企业基本信息（App）")
public class AppYzCompanyController {

    private Logger log = LoggerFactory.getLogger(AppYzCompanyController.class);

    @Autowired
    private YunZhengCompanyService yunZhengCompanyService;

    /**
     * 查询（运政）
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping
    @ApiOperation(value = "企业基本信息查询（App）", notes = "")
    public MessageResp selecCompany(@RequestParam(value = "keyword",required = false,defaultValue = "") String keyword,
                                    @RequestParam(value = "pageNo",defaultValue = "1",required = true) Integer pageNo,
                                    @RequestParam(value = "pageSize",defaultValue = "10",required = true) Integer pageSize,
                                    @RequestParam(value = "orderBy",required = false) String orderBy){
        MessageResp messageResp = new MessageResp();
        PageInfo<YunZhengCompany> pageInfo = null;
        try{
            pageInfo = yunZhengCompanyService.selecCompany(keyword, pageNo, pageSize, orderBy,null);
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
