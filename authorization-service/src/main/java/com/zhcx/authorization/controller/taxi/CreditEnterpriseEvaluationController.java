package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.basicdata.domain.taxi.CreditEnterpriseEvaluationDo;
import com.zhcx.basicdata.facade.taxi.CreditEnterpriseEvaluationService;
import com.zhcx.basicdata.param.taxi.CreditEnterpriseEvaluationVo;
import com.zhcx.basicdata.response.taxi.CreditEnterpriseEvaluationResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @program: public-transport
 * @description: 企业信誉考核（年）业务接口
 * @author: TD
 * @create: 2020-05-11 16:03
 */

@RestController
@RequestMapping("/taxi/credit/enterpriseEvaluation")
@Api(value = "企业信誉考核（年）管理", tags = "企业信誉考核（年）业务接口")
public class CreditEnterpriseEvaluationController {

    private Logger log = LoggerFactory.getLogger(CreditEnterpriseEvaluationController.class);

    @Autowired
    private CreditEnterpriseEvaluationService creditEnterpriseEvaluationService;
    
    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @ApiOperation(value = "查询企业信誉（年考核）（带分页）", notes = "参数CreditEnterpriseEvaluationVo")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public MessageResp queryEnterpriseEvaluationPage(HttpServletRequest request, @ModelAttribute CreditEnterpriseEvaluationVo param) {
        MessageResp resp = new MessageResp();
        PageInfo<CreditEnterpriseEvaluationResp> pageInfo = null;
        CreditEnterpriseEvaluationDo domodel = new CreditEnterpriseEvaluationDo();

        AuthUserResp authUser = sessionHandler.getUser(request);
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType()) && !Constants.AUTH_USER_TYPE_ORG.equals(authUser.getUserType())) {
            if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                param.setCorpId(authUser.getCorpId());
            }
        }

        try{
            BeanUtils.copyProperties(param, domodel);
            pageInfo = creditEnterpriseEvaluationService.queryCreditEnterpriseEvaluationPage(domodel);
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setData(pageInfo.getList());
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询企业考核成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询企业考核异常");
        }
        return resp;
    }

    @ApiOperation(value = "查询近三次考核周期数据", notes = "参数corpId")
    @RequestMapping(value = "/selectByCorpId/{corpId}", method = RequestMethod.GET)
    public MessageResp selectByCorpId(@PathVariable("corpId") Long corpId) {
        MessageResp resp = new MessageResp();
        CreditEnterpriseEvaluationDo domodel = new CreditEnterpriseEvaluationDo();
        domodel.setCorpId(corpId);
        try{
            List<CreditEnterpriseEvaluationResp> list = creditEnterpriseEvaluationService.selectByCorpId(corpId);
            resp.setData(list);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询异常");
        }
        return resp;
    }


    @ApiOperation(value = "查询企业信誉评级分布", notes = "考核年 year")
    @RequestMapping(value = "/enterpriseEvaluationForArae/{year}", method = RequestMethod.GET)
    public MessageResp enterpriseEvaluationForArae(@PathVariable("year") String year) {
        MessageResp resp = new MessageResp();
        try{
            List<Map<String,Object>> list = creditEnterpriseEvaluationService.enterpriseEvaluationForArae(year);
            resp.setData(list);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询查询企业信誉评级分布成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询查询企业信誉评级分布异常");
        }
        return resp;
    }


}
