package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.basicdata.domain.taxi.CreditIndicatorItemDo;
import com.zhcx.basicdata.facade.taxi.CreditIndicatorItemService;
import com.zhcx.basicdata.param.taxi.CreditIndicatorItemVo;
import com.zhcx.basicdata.response.taxi.CreditIndicatorItemResp;
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
 * @description: 指标项业务接口
 * @author: TD
 * @create: 2020-05-09 16:35
 */
@RestController
@RequestMapping("/taxi/credit/item")
@Api(value = "指标项管理", tags = "指标项业务接口")
public class CreditIndicatorItemController {

    private Logger log = LoggerFactory.getLogger(CreditIndicatorItemController.class);

    @Autowired
    private CreditIndicatorItemService creditIndicatorItemService;

    @ApiOperation(value = "查询指标项（带分页）", notes = "参数CreditIndicatorItemVo")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public MessageResp queryIndicatorItemPage(HttpServletRequest request, @ModelAttribute CreditIndicatorItemVo param) {
        MessageResp resp = new MessageResp();
        PageInfo<CreditIndicatorItemResp> pageInfo = null;
        CreditIndicatorItemDo domodel = new CreditIndicatorItemDo();
       try{
            BeanUtils.copyProperties(param, domodel);
            pageInfo = creditIndicatorItemService.queryCreditIndicatorItemPage(domodel);
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setData(pageInfo.getList());
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询指标项成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询指标项异常");
        }
        return resp;
    }

    @ApiOperation(value = "查询指标项（不带分页）", notes = "参数CreditIndicatorItemVo")
    @RequestMapping(value = "/queryIndicatorItemList", method = RequestMethod.GET)
    public MessageResp queryIndicatorItemList(HttpServletRequest request, @ModelAttribute CreditIndicatorItemVo param) {
        MessageResp resp = new MessageResp();
        CreditIndicatorItemDo domodel = new CreditIndicatorItemDo();
        try{
            BeanUtils.copyProperties(param, domodel);
            List<CreditIndicatorItemResp> list = creditIndicatorItemService.queryCreditIndicatorItem(domodel);
            resp.setData(list);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询指标项成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询指标项异常");
        }
        return resp;
    }

    /**
     * 修改指标项信息
     *
     * @param request
     * @param response
     * @param param
     * @return
     */
    @ApiOperation(value = "修改指标项信息", notes = "")
    @ApiImplicitParam(name = "param", value = "指标项信息CreditIndicatorItem", required = true, dataType = "CreditIndicatorItemVo")
    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    public MessageResp<Integer> modifyCreditIndicatorItem(HttpServletRequest request, HttpServletResponse response,
                                                  @RequestBody CreditIndicatorItemVo param) {
        MessageResp<Integer> resp = new MessageResp<Integer>();
        //AuthUserResp authUser = UserAuthUtils.getUser();
        CreditIndicatorItemDo domodel = new CreditIndicatorItemDo();
        BeanUtils.copyProperties(param, domodel);
        //domodel.setWhoModified(authUser.getUserId());
        int count  = creditIndicatorItemService.modifyByCreditIndicatorItem(domodel);
        if (count>0){
            resp.setData(count);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("设置指标项成功");
        }else{
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("设置指标项异常");
        }
        return resp;
    }

}
