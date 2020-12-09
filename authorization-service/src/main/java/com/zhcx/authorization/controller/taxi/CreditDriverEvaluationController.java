package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.basicdata.domain.taxi.CreditDriverEvaluationDo;
import com.zhcx.basicdata.facade.taxi.CreditDriverEvaluationService;
import com.zhcx.basicdata.param.taxi.CreditDriverEvaluationVo;
import com.zhcx.basicdata.response.taxi.CreditDriverEvaluationResp;
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
 * @description: 驾驶员信誉考核（年）业务接口
 * @author: TD
 * @create: 2020-05-11 15:58
 */

@RestController
@RequestMapping("/taxi/credit/driverEvaluation")
@Api(value = "驾驶员信誉考核（年）管理", tags = "驾驶员信誉考核（年）业务接口")
public class CreditDriverEvaluationController {

    private Logger log = LoggerFactory.getLogger(CreditDriverEvaluationController.class);

    @Autowired
    private CreditDriverEvaluationService creditDriverEvaluationService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;


    @ApiOperation(value = "查询驾驶员信誉（年考核）（带分页）", notes = "参数CreditDriverEvaluationVo")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public MessageResp queryDriverEvaluationPage(HttpServletRequest request, @ModelAttribute CreditDriverEvaluationVo param) {
        MessageResp resp = new MessageResp();
        PageInfo<CreditDriverEvaluationResp> pageInfo = null;
        CreditDriverEvaluationDo domodel = new CreditDriverEvaluationDo();

        AuthUserResp authUser = sessionHandler.getUser(request);
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType()) && !Constants.AUTH_USER_TYPE_ORG.equals(authUser.getUserType())) {
            if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                param.setCorpId(authUser.getCorpId());
            }
        }

        try{
            BeanUtils.copyProperties(param, domodel);
            pageInfo = creditDriverEvaluationService.queryCreditDriverEvaluationPage(domodel);
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setData(pageInfo.getList());
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询驾驶员考核成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询驾驶员考核异常");
        }
        return resp;
    }


    @ApiOperation(value = "查询近三次考核周期数据", notes = "参数driverId")
    @RequestMapping(value = "/selectByDriverId/{driverId}", method = RequestMethod.GET)
    public MessageResp selectByCorpId(@PathVariable("driverId") Long driverId) {
        MessageResp resp = new MessageResp();
        CreditDriverEvaluationDo domodel = new CreditDriverEvaluationDo();
        domodel.setCorpId(driverId);
        try{
            List<CreditDriverEvaluationResp> list = creditDriverEvaluationService.selectByCorpId(driverId);
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


    @ApiOperation(value = "查询驾驶员信誉评级分布", notes = "考核年 year")
    @RequestMapping(value = "/driverEvaluationForArae/{year}", method = RequestMethod.GET)
    public MessageResp driverEvaluationForArae(@PathVariable("year") String year) {
        MessageResp resp = new MessageResp();
        try{
            List<Map<String,Object>> list = creditDriverEvaluationService.driverEvaluationForArae(year);
            resp.setData(list);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询查询驾驶员信誉评级分布成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询查询驾驶员信誉评级分布异常");
        }
        return resp;
    }
}
