package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.basicdata.domain.taxi.CreditWarningDo;
import com.zhcx.basicdata.facade.taxi.CreditWarningService;
import com.zhcx.basicdata.param.taxi.CreditWarningVo;
import com.zhcx.basicdata.response.taxi.CreditWarningResp;
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
 * @description: 信誉预警业务接口
 * @author: TD
 * @create: 2020-05-11 16:10
 */

@RestController
@RequestMapping("/taxi/credit/warning")
@Api(value = "信誉预警管理", tags = "信誉预警业务接口")
public class CreditWarningController {

    private Logger log = LoggerFactory.getLogger(CreditWarningController.class);

    @Autowired
    private CreditWarningService creditWarningService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;


    @ApiOperation(value = "查询信用预警弹窗列表")
    @GetMapping("/findList")
    public MessageResp findList(@ModelAttribute CreditWarningVo param) {
        MessageResp resp = new MessageResp();
        try {
            CreditWarningDo domodel = new CreditWarningDo();
            BeanUtils.copyProperties(param, domodel);
            List<CreditWarningResp> list = creditWarningService.findList(domodel);
            resp.setData(list);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询信用预警弹窗列表成功");
            return resp;
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询信用预警弹窗列表异常");
            return resp;
        }
    }

    @ApiOperation(value = "查询信誉预警（带分页）", notes = "参数CreditWarningVo")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public MessageResp queryCreditWarningPage(HttpServletRequest request, @ModelAttribute CreditWarningVo param) {
        MessageResp resp = new MessageResp();
        PageInfo<CreditWarningResp> pageInfo = null;
        CreditWarningDo domodel = new CreditWarningDo();
        try{
            BeanUtils.copyProperties(param, domodel);
            pageInfo = creditWarningService.queryCreditWarningPage(domodel);
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setData(pageInfo.getList());
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询信誉预警成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询信誉预警异常");
        }
        return resp;
    }

    /**
     * 修改信誉预警信息
     *
     * @param request
     * @param response
     * @param param
     * @return
     */
    @ApiOperation(value = "修改信誉预警信息", notes = "")
    @ApiImplicitParam(name = "param", value = "信誉预警信息CreditWarning", required = true, dataType = "CreditWarningVo")
    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    public MessageResp<Integer> modifyCreditWarning(HttpServletRequest request, HttpServletResponse response,
                                                   @RequestBody CreditWarningVo param) {
        MessageResp<Integer> resp = new MessageResp<Integer>();
        AuthUserResp authUser = sessionHandler.getUser(request);
        CreditWarningDo domodel = new CreditWarningDo();
        BeanUtils.copyProperties(param, domodel);
        domodel.setDisposeId(authUser.getUserId());
        domodel.setDisposeName(authUser.getUserName());
        int count  = creditWarningService.modifyByCreditWarning(domodel);
        if (count>0){
            resp.setData(count);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("设置信誉预警成功");
        }else{
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("设置信誉预警异常");
        }
        return resp;
    }
    
}
