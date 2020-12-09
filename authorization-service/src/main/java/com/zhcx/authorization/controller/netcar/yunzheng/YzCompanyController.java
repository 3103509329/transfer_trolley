package com.zhcx.authorization.controller.netcar.yunzheng;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.netcarbasic.facade.netcar.CompanyService;
import com.zhcx.platformtonet.facade.YunZhengCompanyService;
import com.zhcx.platformtonet.pojo.base.YunZhengCompany;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

    @Autowired
    private YunZhengCompanyService yunZhengCompanyService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    /**
     *新增（运政）
     * @param company
     * @return
     */
    @PostMapping
    public MessageResp addCompany(@Valid @RequestBody YunZhengCompany company){
        MessageResp messageResp = new MessageResp();
        try{
            int i = yunZhengCompanyService.selectByBusiregnumber(company);
            if(i > 0){
                messageResp.setResult(Boolean.FALSE.toString());
                messageResp.setResultDesc("企业已存在");
                return messageResp;
            }
            YunZhengCompany pageInfo = yunZhengCompanyService.addCompany(company);
            messageResp.setData(pageInfo);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("新增成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("新增失败");
        }
        return messageResp;
    }

    /**
     * 查询（运政）
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
                                    @RequestParam(required = false) String orderBy){
        MessageResp messageResp = new MessageResp();
        PageInfo<YunZhengCompany> pageInfo = null;
        try{
            Long uuid = null;
            AuthUserResp user = sessionHandler.getUser(request);
            if (null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())) {
                if (user.getCorpId() != null && user.getCorpId() != 0L) {
                    uuid = user.getCorpId();
                }
            }
            pageInfo = yunZhengCompanyService.selecCompany(keyword, pageNo, pageSize, orderBy, uuid);
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


    /**
     * 修改（运政）
     * @param company
     * @return
     */
    @PutMapping
    public MessageResp updateCompany(@Valid @RequestBody YunZhengCompany company){
        MessageResp messageResp = new MessageResp();
        try{
            YunZhengCompany pageInfo = yunZhengCompanyService.updateCompany(company);
            messageResp.setData(pageInfo);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("修改成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("修改失败");
        }
        return messageResp;
    }

    /**
     * 删除（运政）
     * @param company
     * @return
     */
    @DeleteMapping
    public MessageResp deleteCompany(@RequestBody YunZhengCompany company){
        MessageResp messageResp = new MessageResp();
        try{
            int pageInfo = yunZhengCompanyService.deleteCompany(company);
            messageResp.setData(pageInfo);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("删除成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("删除失败");
        }
        return messageResp;
    }

}
