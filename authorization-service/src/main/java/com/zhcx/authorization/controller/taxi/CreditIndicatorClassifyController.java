package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.basicdata.domain.taxi.CreditIndicatorClassifyDo;
import com.zhcx.basicdata.facade.taxi.CreditIndicatorClassifyService;
import com.zhcx.basicdata.param.taxi.CreditIndicatorClassifyVo;
import com.zhcx.basicdata.response.taxi.CreditIndicatorClassifyResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: public-transport
 * @description: 指标类业务接口
 * @author: TD
 * @create: 2020-05-11 11:38
 */
@RestController
@RequestMapping("/taxi/credit/classify")
@Api(value = "指标类管理", tags = "指标类业务接口")
public class CreditIndicatorClassifyController {

    private Logger log = LoggerFactory.getLogger(CreditIndicatorClassifyController.class);

    @Autowired
    private CreditIndicatorClassifyService creditIndicatorClassifyService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @ApiOperation(value = "查询指标类（带分页）", notes = "参数CreditIndicatorClassifyVo")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public MessageResp queryIndicatorClassifyPage(HttpServletRequest request, @ModelAttribute CreditIndicatorClassifyVo param) {
        MessageResp resp = new MessageResp();
        PageInfo<CreditIndicatorClassifyResp> pageInfo = null;
        CreditIndicatorClassifyDo domodel = new CreditIndicatorClassifyDo();
        try{
            BeanUtils.copyProperties(param, domodel);
            pageInfo = creditIndicatorClassifyService.queryCreditIndicatorClassifyPage(domodel);
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setData(pageInfo.getList());
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询指标类成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询指标类异常");
        }
        return resp;
    }

    @ApiOperation(value = "查询指标类（不带分页）", notes = "参数CreditIndicatorClassifyVo")
    @RequestMapping(value = "/queryIndicatorClassifyList", method = RequestMethod.GET)
    public MessageResp queryIndicatorClassifyList(HttpServletRequest request, @ModelAttribute CreditIndicatorClassifyVo param) {
        MessageResp resp = new MessageResp();
        CreditIndicatorClassifyDo domodel = new CreditIndicatorClassifyDo();
        try{
            BeanUtils.copyProperties(param, domodel);
            List<CreditIndicatorClassifyResp> list = creditIndicatorClassifyService.queryCreditIndicatorClassify(domodel);
            resp.setData(list);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询指标类成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询指标类异常");
        }
        return resp;
    }

    /**
     * 修改指标类信息
     *
     * @param request
     * @param response
     * @param param
     * @return
     */
    @ApiOperation(value = "修改指标类信息", notes = "")
    @ApiImplicitParam(name = "param", value = "指标类信息CreditIndicatorClassify", required = true, dataType = "CreditIndicatorClassifyVo")
    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    public MessageResp<Integer> modifyCreditIndicatorClassify(HttpServletRequest request, HttpServletResponse response,
                                                          @RequestBody CreditIndicatorClassifyVo param) {
        MessageResp<Integer> resp = new MessageResp<Integer>();
        AuthUserResp authUser = sessionHandler.getUser(request);
        CreditIndicatorClassifyDo domodel = new CreditIndicatorClassifyDo();
        BeanUtils.copyProperties(param, domodel);
        domodel.setWhoModified(authUser.getUserId());
        int count  = creditIndicatorClassifyService.modifyByCreditIndicatorClassify(domodel);
        if (count>0){
            resp.setData(count);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("设置指标类成功");
        }else{
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("设置指标类异常");
        }
        return resp;
    }
}
