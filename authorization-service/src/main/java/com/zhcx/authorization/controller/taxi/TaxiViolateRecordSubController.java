package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig.SessionHandler;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.basicdata.facade.taxi.TaxiViolateRecordSubService;
import com.zhcx.basicdata.pojo.taxi.TaxiViolateRecordSub;
import com.zhcx.common.util.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @description: 违规行为附加信息 controller
 * @author: qiuziqiang
 * @date 2019-05-15 10:48
 **/

@RestController
@RequestMapping("/taxi/violateRecordSub")
@Api(value = "violateRecordSub", tags = "违规行为附加信息接口")
public class TaxiViolateRecordSubController {

    private Logger log = LoggerFactory.getLogger(TaxiViolateRecordController.class);

    @Autowired
    private TaxiViolateRecordSubService taxiViolateRecordSubService;

    @Resource
    private UUIDUtils uuidUtils;

    @Autowired
    private SessionHandler sessionHandler;


    @GetMapping("")
    @ApiOperation(value = "查询违规行为附加信息", notes = "参数为违规行为附加信息对象")
    public MessageResp getRecord(HttpServletRequest request, @ModelAttribute TaxiViolateRecordSub param) {
        MessageResp result = new MessageResp();
        PageInfo<TaxiViolateRecordSub> pageInfo = null;
        try {
            pageInfo = taxiViolateRecordSubService.selectByParam(param);
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

    @PostMapping("/addViolateRecordSub")
    @ApiOperation(value = "添加违规行为附加信息", notes = "参数为违规行为附加信息对象")
    public MessageResp insertTaxiViolateRecordSub(HttpServletRequest request, @RequestBody TaxiViolateRecordSub record) {
        MessageResp result = new MessageResp();

        AuthUserResp user = sessionHandler.getUser(request);
        if (user != null) {
            record.setCreateBy(user.getUserId());
        }
        record.setUuid(uuidUtils.getLongUUID());
        try {
            int res = taxiViolateRecordSubService.addRecord(record);
            if (res < 1) {
                return CommonUtils.returnErrorInfo("添加失败");
            }
            result.setResultDesc("新增成功");
            result.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            log.info("添加违规行为附加信息异常:" + e.getMessage());
            return CommonUtils.returnErrorInfo("添加失败");
        }
        return result;
    }

    @PostMapping("/updateViolateRecordSub")
    @ApiOperation(value = "修改违规行为附加信息", notes = "参数为违规行为附加信息对象")
    public MessageResp updateTaxiViolateRecordSub(HttpServletRequest request, @RequestBody TaxiViolateRecordSub record) {
        MessageResp result = new MessageResp();
        try {
            int res = taxiViolateRecordSubService.updateRecord(record);
            result.setResultDesc("修改成功");
            result.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            log.info("修改违规行为附加信息异常:" + e.getMessage());
            return CommonUtils.returnErrorInfo("修改失败");
        }
        return result;
    }

    @DeleteMapping("/deleteViolateRecordSub/{uuid}")
    @ApiOperation(value = "删除违规行为附加信息", notes = "参数UUID")
    public MessageResp deleteTaxiViolateRecordSub(@PathVariable(value = "uuid") String uuid) {
        MessageResp result = new MessageResp();
        try {
            int res = taxiViolateRecordSubService.deleRecord(Long.parseLong(uuid));
            result.setResultDesc("删除成功");
            result.setResult(Boolean.TRUE.toString());

        } catch (NumberFormatException e) {
            log.info("删除违规行为附加信息异常:" + e.getMessage());
            return CommonUtils.returnErrorInfo("删除失败");
        }
        return result;
    }

}
