package com.zhcx.netcar.netcarservice.controller.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.base.ShareCompanyService;
import com.zhcx.netcar.facade.base.ShareService;
import com.zhcx.netcar.netcarservice.config.SessionConfig;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import com.zhcx.netcar.params.QueryParam;
import com.zhcx.netcar.pojo.base.*;
import com.zhcx.netcar.vo.CompanyNameVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2018/11/29 0029 14:15
 **/
@RestController
@RequestMapping("/netcar/share")
@Api(value = "私人小客车合乘订单信息", tags = "私人小客车合乘订单信息接口")
public class ShareController {

    private Logger log = LoggerFactory.getLogger(ShareController.class);

    @Autowired
    private ShareService shareService;

    @Autowired
    private ShareCompanyService shareCompanyService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @ApiOperation(value = "查询私人小客车合乘服务平台基本信息", notes = "查询私人小客车合乘服务平台基本信息")
    @GetMapping("/company")
    public MessageResp queryShareCompanyList(HttpServletRequest request,
                                             @RequestParam(required = false) String companyId,
                                             @RequestParam(required = false) String identifier,
                                             @RequestParam(defaultValue = "1") Integer pageNo,
                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                             @RequestParam(required = false) String orderBy){
        MessageResp messageResp = new MessageResp();
        try {
//            AuthUserResp user = sessionHandler.getUser(request);
//            if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
//                if(null != user.getCorpId() && user.getCorpId() != 0L){
//                    companyId = String.valueOf(user.getCorpId());
//                }
//            }
            PageInfo<NetcarShareCompany> pageInfo = shareCompanyService.queryShareCompanyList(companyId, identifier, pageNo, pageSize, orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    @ApiOperation(value = "查询私人小客车公司名称", notes = "查询私人小客车公司名称")
    @GetMapping("/companyNames")
    public MessageResp queryShareCompanyNameList(HttpServletRequest request){
        MessageResp messageResp = new MessageResp();
        try {
            String companyId = null;
//            AuthUserResp user = sessionHandler.getUser(request);
//            if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
//                if(null != user.getCorpId() && user.getCorpId() != 0L){
//                    companyId = String.valueOf(user.getCorpId());
//                }
//            }
            List<CompanyNameVo> list = shareCompanyService.queryShareCompanyNameList(companyId);
            messageResp.setData(list);
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    @ApiOperation(value = "查询私人小客车驾驶员行程发布信息", notes = "查询私人小客车驾驶员行程发布信息")
    @GetMapping("/route")
    public MessageResp queryShareRouteList(HttpServletRequest request, @ModelAttribute QueryParam queryParam){
        MessageResp messageResp = new MessageResp();
        try {
//            AuthUserResp user = sessionHandler.getUser(request);
//            if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
//                if(null != user.getCorpId() && user.getCorpId() != 0L){
//                    queryParam.setCompanyId(String.valueOf(user.getCorpId()));
//                }
//            }
            PageInfo<NetcarShareRoute> pageInfo = shareService.queryShareRouteList(queryParam);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    @ApiOperation(value = "私人小客车合乘订单信息查询", notes = "私人小客车合乘订单信息查询接口")
    @GetMapping("/order")
    public MessageResp queryShareOrderList(HttpServletRequest request,
                                           @ModelAttribute QueryParam queryParam){
        MessageResp messageResp = new MessageResp();
        try {
//            AuthUserResp user = sessionHandler.getUser(request);
//            if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
//                if(null != user.getCorpId() && user.getCorpId() != 0L){
//                    queryParam.setCompanyId(String.valueOf(user.getCorpId()));
//                }
//            }
            PageInfo<NetcarShareOrder> pageInfo = shareService.queryShareOrderList(queryParam);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    @ApiOperation(value = "私人小客车合乘支付信息查询", notes = "私人小客车合乘支付信息查询")
    @GetMapping("/pay")
    public MessageResp querySharePayList(HttpServletRequest request,
                                         @ModelAttribute QueryParam queryParam){
        MessageResp messageResp = new MessageResp();
        try {
//            AuthUserResp user = sessionHandler.getUser(request);
//            if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
//                if(null != user.getCorpId() && user.getCorpId() != 0L){
//                    queryParam.setCompanyId(String.valueOf(user.getCorpId()));
//                }
//            }
            PageInfo<NetcarSharePay> pageInfo = shareService.querySharePayList(queryParam);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    @ApiOperation(value = "私人小客车合乘信息详情列表查询", notes = "私人小客车合乘信息详情列表查询")
    @GetMapping("/detail")
    public MessageResp queryShareInfoList(HttpServletRequest request,
                                          @ModelAttribute QueryParam queryParam){
        MessageResp messageResp = new MessageResp();
        try {
//            AuthUserResp user = sessionHandler.getUser(request);
//            if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
//                if(null != user.getCorpId() && user.getCorpId() != 0L){
//                    queryParam.setCompanyId(String.valueOf(user.getCorpId()));
//                }
//            }
            PageInfo<NetcarShareInfo> pageInfo = shareService.queryShareInfoList(queryParam);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }
}
