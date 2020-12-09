package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.basicdata.facade.taxi.TaxiAlarmBlackListService;
import com.zhcx.basicdata.pojo.taxi.TaxiAlarmBlackList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description: 黑名单 Controller
 * @author: qiuziqiang
 * @date 2019-06-27 14:06
 **/
@RestController
@RequestMapping("/taxi/alarmBlackList")
@Api(value = "alarmBlackList", tags = "出租车警情黑名单接口")
public class TaxiAlarmBlackListController {

    private Logger log = LoggerFactory.getLogger(TaxiAlarmBlackListController.class);

    @Autowired
    private TaxiAlarmBlackListService taxiAlarmBlackListService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @GetMapping("")
    @ApiOperation(value = "查询黑名单列表", notes = "参数为黑名单对象")
    public MessageResp getRecord(HttpServletRequest request, @ModelAttribute TaxiAlarmBlackList param) {
        MessageResp result = new MessageResp();
        PageInfo<TaxiAlarmBlackList> pageInfo = null;
        try {
            pageInfo = taxiAlarmBlackListService.queryPageAlarmBlackList(param);
            result.setData(pageInfo.getList());
            result.setResultDesc("查询成功");
            result.setResult(Boolean.TRUE.toString());
            result.setPageBean(PageBeanUtil.createPageBean(pageInfo));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询失败,{}", e.getMessage());
            return CommonUtils.returnErrorInfo(null);
        }
        return result;
    }

    @PostMapping("/addVehBlackList")
    @ApiOperation(value = "添加黑名单信息", notes = "参数为黑名单对象")
    public MessageResp addVehBlackList(HttpServletRequest request, @RequestBody TaxiAlarmBlackList record) {
        MessageResp result = new MessageResp();

        AuthUserResp user = sessionHandler.getUser(request);
        if (user != null) {
            record.setCreateBy(user.getUserId());
        }
        try {
            List<TaxiAlarmBlackList> blackLists = taxiAlarmBlackListService.queryAlarmBlackList(record);
            if (blackLists != null && blackLists.size() > 0) {
                return CommonUtils.returnErrorInfo("添加失败,黑名单中已存在此车辆");
            }
            int res = taxiAlarmBlackListService.insertAlarmBlackList(record);
            if (res < 1) {
                return CommonUtils.returnErrorInfo("添加失败,系统中不存在此车辆");
            }
            result.setResultDesc("新增成功");
            result.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            log.info("添加车辆到黑名单异常:" + e.getMessage());
            return CommonUtils.returnErrorInfo("添加失败");
        }
        return result;
    }

    @DeleteMapping("/deleteVehBlackList")
    @ApiOperation(value = "删除黑名单信息", notes = "参数UUID")
    public MessageResp deleteVehBlackList(Long uuid) {
        MessageResp result = new MessageResp();
        try {
            int res = taxiAlarmBlackListService.deleteByUuid(uuid);
            if (res > 0) {
                result.setResultDesc("删除成功");
                result.setResult(Boolean.TRUE.toString());
            }
        } catch (Exception e) {
            log.info("删除黑名单车辆异常:" + e.getMessage());
            return CommonUtils.returnErrorInfo("删除失败");
        }
        return result;
    }

}
