package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.basicdata.domain.taxi.CreditBehaviorDo;
import com.zhcx.basicdata.facade.taxi.CreditBehaviorService;
import com.zhcx.basicdata.param.taxi.CreditBehaviorVo;
import com.zhcx.basicdata.response.taxi.CreditBehaviorResp;
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
 * @description: 信誉行为管理业务接口
 * @author: TD
 * @create: 2020-05-11 14:19
 */
@RestController
@RequestMapping("/taxi/credit/behavior")
@Api(value = "信誉行为管理管理", tags = "信誉行为管理业务接口")
public class CreditBehaviorController {

    private Logger log = LoggerFactory.getLogger(CreditBehaviorController.class);

    @Autowired
    private CreditBehaviorService creditBehaviorService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @ApiOperation(value = "查询信誉行为管理（带分页）", notes = "参数CreditBehaviorVo")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public MessageResp queryCreditBehaviorPage(HttpServletRequest request, @ModelAttribute CreditBehaviorVo param) {
        MessageResp resp = new MessageResp();
        PageInfo<CreditBehaviorResp> pageInfo = null;
        CreditBehaviorDo domodel = new CreditBehaviorDo();
        try {
            BeanUtils.copyProperties(param, domodel);
            domodel.setRecordId(-1L);
            pageInfo = creditBehaviorService.queryCreditBehaviorPage(domodel);
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setData(pageInfo.getList());
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询信誉行为成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询信誉行为异常");
        }
        return resp;
    }

    @ApiOperation(value = "查询信誉行为管理（不带分页）", notes = "参数CreditBehaviorVo")
    @RequestMapping(value = "/queryBehaviorList", method = RequestMethod.GET)
    public MessageResp queryBehaviorList(HttpServletRequest request, @ModelAttribute CreditBehaviorVo param) {
        MessageResp resp = new MessageResp();
        CreditBehaviorDo domodel = new CreditBehaviorDo();
        try {
            BeanUtils.copyProperties(param, domodel);
            domodel.setRecordId(-1L);
            List<CreditBehaviorResp> list = creditBehaviorService.queryCreditBehavior(domodel);
            resp.setData(list);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询信誉行为成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询信誉行为异常");
        }
        return resp;
    }

    @ApiOperation(value = "添加信誉行为", notes = "参数CreditBehaviorVo")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public MessageResp save(HttpServletRequest request, @RequestBody CreditBehaviorVo param) {
        MessageResp resp = new MessageResp();
        try {
            AuthUserResp authUser = sessionHandler.getUser(request);
            CreditBehaviorDo domodel = new CreditBehaviorDo();
            BeanUtils.copyProperties(param, domodel);
            domodel.setCreator(authUser.getUserId());
            domodel.setRecordId(-1L);
            domodel.setWhoModified(authUser.getUserId());
            domodel.setWhoModifiedName(authUser.getUserName());
            int count = creditBehaviorService.save(domodel);
            if (count > 0) {
                resp.setData(count);
                resp.setStatusCode("00");
                resp.setResult(Boolean.TRUE.toString());
                resp.setResultDesc("新增信誉行为成功");
                return resp;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("新增信誉行为异常");
            return resp;
        }

        return resp;
    }

    /**
     * 修改信誉行为信息
     *
     * @param request
     * @param response
     * @param param
     * @return
     */
    @ApiOperation(value = "修改信誉行为信息", notes = "")
    @ApiImplicitParam(name = "param", value = "信誉行为信息CreditBehavior", required = true, dataType = "CreditBehaviorVo")
    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    public MessageResp<Integer> modifyCreditBehavior(HttpServletRequest request, HttpServletResponse response,
                                                     @RequestBody CreditBehaviorVo param) {
        MessageResp<Integer> resp = new MessageResp<Integer>();
        AuthUserResp authUser = sessionHandler.getUser(request);
        CreditBehaviorDo domodel = new CreditBehaviorDo();
        BeanUtils.copyProperties(param, domodel);
        domodel.setWhoModified(authUser.getUserId());
        domodel.setWhoModifiedName(authUser.getUserName());
        int count = creditBehaviorService.modifyByCreditBehavior(domodel);
        if (count > 0) {
            resp.setData(count);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("设置信誉行为成功");
        } else {
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("设置信誉行为异常");
        }
        return resp;
    }


    @DeleteMapping("/delete/{uuid}")
    @ApiOperation(value = "删除信誉行为信息", notes = "uuid")
    public MessageResp delete(@PathVariable("uuid") Long uuid) {
        MessageResp resp = new MessageResp();
        try {
            if (creditBehaviorService.delete(uuid) > 0) {
                resp.setStatusCode("00");
                resp.setResult(Boolean.TRUE.toString());
                resp.setResultDesc("删除信誉行为成功");
            } else {
                resp.setResult(Boolean.FALSE.toString());
                resp.setStatusCode("-50");
                resp.setResultDesc("删除信誉行为失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("删除信誉行为异常");
        }
        return resp;
    }
}
