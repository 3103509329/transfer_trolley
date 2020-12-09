package com.zhcx.authorization.controller.netcar.portal;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.platformtonet.facade.ExaminationService;
import com.zhcx.platformtonet.pojo.base.NetcarExamination;
import com.zhcx.platformtonet.vo.NetcarExaminationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/2/25 10:39
 **/
@RestController
@RequestMapping("/netcar/portal/examinations")
@Api(value = "网约车考试信息管理", tags = "网约车考试信息管理")
public class ExaminationController {

    private static Logger logger = LoggerFactory.getLogger(ExaminationController.class);

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Autowired
    private ExaminationService examinationService;

    /**
     * 门户驾驶员预约页面
     * @param request
     * @param examAddress
     * @return
     */
    @ApiOperation(value = "根据考试地点网约车考试信息查询", notes = "")
    @GetMapping("/address")
    public MessageResp queryExamTimeByAddress(HttpServletRequest request, @RequestParam(required = false) String examAddress){
        MessageResp messageResp = new MessageResp();

        try {
            List<NetcarExaminationVo> list = examinationService.queryExamTimeByAddress(examAddress);
            messageResp.setResultDesc("查询成功");
            messageResp.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return CommonUtils.returnErrorInfo("查询异常");
        }
        return messageResp;
    }


    @ApiOperation(value = "管理端网约车考试信息列表查询", notes = "")
    @GetMapping
    public MessageResp queryExaminationList(HttpServletRequest request,
                                            @RequestParam(defaultValue = "1") Integer pageNo,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @RequestParam(required = false) String orderBy){
        MessageResp messageResp = new MessageResp();

        AuthUserResp user = sessionHandler.getUser(request);
        if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("没有权限，请联系管理员");
            return messageResp;
        }
        try {
            PageInfo<NetcarExamination> pageInfo = examinationService.queryExaminationList(pageNo, pageSize, orderBy);
            messageResp.setResultDesc("查询成功");
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return CommonUtils.returnErrorInfo("查询异常");
        }
        return messageResp;
    }

    @ApiOperation(value = "管理端网约车考试信息新增", notes = "")
    @PostMapping
    public MessageResp insertExaminationInfo(HttpServletRequest request, @RequestBody NetcarExamination param){
        MessageResp messageResp = new MessageResp();

        AuthUserResp user = sessionHandler.getUser(request);
        if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("没有权限，请联系管理员");
            return messageResp;
        }
        try {
            NetcarExamination netcarExamination = examinationService.insertExaminationInfo(param);
            messageResp.setResultDesc("新增成功");
            messageResp.setData(netcarExamination);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return CommonUtils.returnErrorInfo("新增异常");
        }
        return messageResp;
    }


    @ApiOperation(value = "管理端网约车考试信息删除", notes = "")
    @DeleteMapping("/{uuid}")
    public MessageResp deleteExaminationInfo(HttpServletRequest request, @PathVariable Long uuid){
        MessageResp messageResp = new MessageResp();

        AuthUserResp user = sessionHandler.getUser(request);
        if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("没有权限，请联系管理员");
            return messageResp;
        }
        try {
            int row = examinationService.deleteExaminationInfo(uuid);
            if(row == 0){
                return CommonUtils.returnErrorInfo("删除失败");
            }
            messageResp.setResultDesc("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return CommonUtils.returnErrorInfo("删除异常");
        }
        return messageResp;
    }

    @ApiOperation(value = "管理端网约车考试信息修改", notes = "")
    @PutMapping
    public MessageResp updateExaminationInfo(HttpServletRequest request, @RequestBody NetcarExamination param){
        MessageResp messageResp = new MessageResp();

        AuthUserResp user = sessionHandler.getUser(request);
        if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("没有权限，请联系管理员");
            return messageResp;
        }
        try {
            int result = examinationService.updateExaminationInfo(param);
            messageResp.setResultDesc("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return CommonUtils.returnErrorInfo("修改异常");
        }
        return messageResp;
    }
}
