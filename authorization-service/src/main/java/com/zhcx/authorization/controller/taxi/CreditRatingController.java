package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.basicdata.domain.taxi.CreditRatingDo;
import com.zhcx.basicdata.facade.taxi.CreditRatingService;
import com.zhcx.basicdata.param.taxi.CreditRatingVo;
import com.zhcx.basicdata.response.taxi.CreditRatingResp;
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
 * @description: 信誉评级业务接口
 * @author: TD
 * @create: 2020-05-11 14:19
 */
@RestController
@RequestMapping("/taxi/credit/rating")
@Api(value = "信誉评级管理", tags = "信誉评级业务接口")
public class CreditRatingController {

    private Logger log = LoggerFactory.getLogger(CreditRatingController.class);

    @Autowired
    private CreditRatingService creditRatingService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @ApiOperation(value = "查询信誉评级（带分页）", notes = "参数CreditRatingVo")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public MessageResp queryCreditRatingPage(HttpServletRequest request, @ModelAttribute CreditRatingVo param) {
        MessageResp resp = new MessageResp();
        PageInfo<CreditRatingResp> pageInfo = null;
        CreditRatingDo domodel = new CreditRatingDo();
        try {
            BeanUtils.copyProperties(param, domodel);
            pageInfo = creditRatingService.queryCreditRatingPage(domodel);
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setData(pageInfo.getList());
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询信誉评级成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询信誉评级异常");
        }
        return resp;
    }

    @ApiOperation(value = "查询信誉评级（不带分页）", notes = "参数CreditRatingVo")
    @RequestMapping(value = "/queryRatingList", method = RequestMethod.GET)
    public MessageResp queryRatingList(HttpServletRequest request, @ModelAttribute CreditRatingVo param) {
        MessageResp resp = new MessageResp();
        CreditRatingDo domodel = new CreditRatingDo();
        try {
            BeanUtils.copyProperties(param, domodel);
            List<CreditRatingResp> list = creditRatingService.queryCreditRating(domodel);
            resp.setData(list);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询信誉评级成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询信誉评级异常");
        }
        return resp;
    }

    @ApiOperation(value = "添加信誉评级", notes = "参数CreditRatingVo")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public MessageResp save(HttpServletRequest request, @RequestBody CreditRatingVo param) {
        MessageResp resp = new MessageResp();
        AuthUserResp authUser = sessionHandler.getUser(request);
        CreditRatingDo domodel = new CreditRatingDo();
        domodel.setGradeName(param.getGradeName().trim());
        domodel.setObjectType(param.getObjectType());
        List<CreditRatingResp> list = creditRatingService.queryCreditRating(domodel);
        if (null != list && list.size() > 0) {
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("信誉评级已存在");
            return resp;
        }
        BeanUtils.copyProperties(param, domodel);
        domodel.setGradeName(param.getGradeName().trim());
        domodel.setCreator(authUser.getUserId());
        int count = creditRatingService.save(domodel);
        if (count > 0) {
            resp.setData(count);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("新增信誉评级成功");
        } else {
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("新增信誉评级异常");
        }
        return resp;
    }

    /**
     * 修改信誉评级信息
     *
     * @param request
     * @param response
     * @param param
     * @return
     */
    @ApiOperation(value = "修改信誉评级信息", notes = "")
    @ApiImplicitParam(name = "param", value = "信誉评级信息CreditRating", required = true, dataType = "CreditRatingVo")
    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    public MessageResp<Integer> modifyCreditRating(HttpServletRequest request, HttpServletResponse response,
                                                   @RequestBody CreditRatingVo param) {
        MessageResp<Integer> resp = new MessageResp<Integer>();
        AuthUserResp authUser = sessionHandler.getUser(request);
        CreditRatingDo domodel = new CreditRatingDo();
        domodel.setGradeName(param.getGradeName().trim());
        domodel.setObjectType(param.getObjectType());
        List<CreditRatingResp> list = creditRatingService.queryCreditRating(domodel);
        if (null != list && list.size() > 0) {
            if (list.get(0).getUuid() == param.getUuid()) {
                resp.setResult(Boolean.FALSE.toString());
                resp.setStatusCode("-50");
                resp.setResultDesc("信誉评级已存在");
                return resp;
            }
        }

        BeanUtils.copyProperties(param, domodel);
        domodel.setWhoModified(authUser.getUserId());
        int count = creditRatingService.modifyByCreditRating(domodel);
        if (count > 0) {
            resp.setData(count);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("设置信誉评级成功");
        } else {
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("设置信誉评级异常");
        }
        return resp;
    }

    @DeleteMapping("/delete/{uuid}")
    @ApiOperation(value = "删除信誉评级信息", notes = "uuid")
    public MessageResp delete(@PathVariable("uuid") Long uuid) {
        MessageResp resp = new MessageResp();
        try {
            if (creditRatingService.delete(uuid) > 0) {
                resp.setStatusCode("00");
                resp.setResult(Boolean.TRUE.toString());
                resp.setResultDesc("删除信誉评级成功");
            } else {
                resp.setResult(Boolean.FALSE.toString());
                resp.setStatusCode("-50");
                resp.setResultDesc("删除信誉评级失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("删除信誉评级异常");
        }
        return resp;
    }


}
