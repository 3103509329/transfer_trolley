package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.basicdata.facade.taxi.TaxiTextScheduleService;
import com.zhcx.basicdata.pojo.taxi.TaxiTextSchedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/taxi/textSchedule")
@Api(value = "textSchedule" , tags = "出租车调度文本")
public class TaxiTextScheduleController {

    private Logger log = LoggerFactory.getLogger(TaxiTextScheduleController.class);

    @Resource
    private TaxiTextScheduleService scheduleService;

    @Autowired
    SessionConfig.SessionHandler sessionHandler;

    @GetMapping("/queryList")
    @ApiOperation(value = "查询调度文本信息列表", notes = "参数TaxiTextSchedule")
    public MessageResp queryTextScheduleList(@ModelAttribute TaxiTextSchedule param){
        MessageResp resp = new MessageResp();
        PageInfo<TaxiTextSchedule> pageInfo = null;
        try {
            pageInfo = scheduleService.queryTextScheduleList(param);
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setData(pageInfo.getList());
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询调度文本列表成功");
        }catch (Exception e){
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }
        return resp;
    }

    @GetMapping("/queryTextScheduleInfo")
    @ApiOperation(value = "查询调度文本信息", notes = "uuid")
    public MessageResp queryTextScheduleInfo(@RequestParam String uuid){
        MessageResp resp = new MessageResp();
        TaxiTextSchedule schedule = null;
        try {
            schedule = scheduleService.queryTextScheduleInfo(uuid);
            resp.setData(schedule);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询调度文本成功");
        }catch (Exception e){
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("查询异常");
        }
        return resp;
    }

    @PostMapping("/insertTextSchedule")
    @ApiOperation(value = "添加调度文本信息", notes = "参数TaxiTextSchedule")
    public MessageResp insertTextSchedule(HttpServletRequest request, @RequestBody TaxiTextSchedule schedule){
        MessageResp resp = new MessageResp();
        try {
            //调度文本唯一性校验
            TaxiTextSchedule param = new TaxiTextSchedule();
            param.setText(schedule.getText());
            PageInfo<TaxiTextSchedule> pageInfo = scheduleService.queryTextScheduleList(param);
            List<TaxiTextSchedule> scheduleList = pageInfo.getList();
            if(scheduleList != null && scheduleList.size() > 0){
                resp.setStatusCode("00");
                resp.setResult(Boolean.FALSE.toString());
                resp.setResultDesc("调度文本已存在");
                return resp;
            }
            AuthUserResp authUser = sessionHandler.getUser(request);
            if(authUser != null){
                schedule.setCreateBy(authUser.getUserName());
            }
            scheduleService.insertTextSchedule(schedule);
            resp.setData(schedule);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("新增调度文本成功");
        }catch (Exception e){
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("新增异常");
        }
        return resp;
    }

    @PutMapping("/updateTextSchedule")
    @ApiOperation(value = "更新调度文本信息", notes = "参数TaxiTextSchedule")
    public MessageResp updateTextSchedule(HttpServletRequest request, @RequestBody TaxiTextSchedule schedule){
        MessageResp resp = new MessageResp();
        try {
            //调度文本唯一性校验
            TaxiTextSchedule param = new TaxiTextSchedule();
            param.setText(schedule.getText());
            PageInfo<TaxiTextSchedule> pageInfo = scheduleService.queryTextScheduleList(param);
            List<TaxiTextSchedule> scheduleList = pageInfo.getList();
            if(scheduleList != null && scheduleList.size() > 0){
                TaxiTextSchedule dbSchedule = scheduleList.get(0);
                if(StringUtils.equals(dbSchedule.getText(), schedule.getText()) && !StringUtils.equals(dbSchedule.getUuid(), schedule.getUuid())){
                    resp.setStatusCode("00");
                    resp.setResult(Boolean.TRUE.toString());
                    resp.setResultDesc("调度文本已存在");
                    return resp;
                }
                resp.setStatusCode("00");
                resp.setResult(Boolean.TRUE.toString());
                resp.setResultDesc("调度文本已存在");
                return resp;
            }
            AuthUserResp authUser = sessionHandler.getUser(request);
            if(authUser != null){
                schedule.setUpdateBy(authUser.getUserName());
            }
            scheduleService.updateTextSchedule(schedule);
            resp.setData(schedule);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("更新调度文本成功");
        }catch (Exception e){
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("更新异常");
        }
        return resp;
    }

    @DeleteMapping("/deleteTextSchedule")
    @ApiOperation(value = "删除调度文本信息", notes = "uuid")
    public MessageResp deleteTextSchedule(@RequestParam String uuid){
        MessageResp resp = new MessageResp();
        try {
            scheduleService.deleteTextSchedule(uuid);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("删除调度文本成功");
        }catch (Exception e){
            log.error(e.getMessage());
            resp = CommonUtils.returnErrorInfo("删除异常");
        }
        return resp;
    }

}
