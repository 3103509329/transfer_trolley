package com.zhcx.authorization.controller.netcar.portal;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.platformtonet.facade.ExamBookService;
import com.zhcx.platformtonet.pojo.base.NetcarDriverExamination;
import com.zhcx.platformtonet.vo.NetcarDriverExaminationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/2/25 10:17
 **/
@RestController
@RequestMapping("/netcar/portal/driver/examination")
@Api(value = "网约车驾驶员考试报名", tags = "网约车驾驶员考试报名")
public class NetcarExamBookController {

    private Logger log = LoggerFactory.getLogger(NetcarExamBookController.class);

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Autowired
    private ExamBookService examBookService;

    @ApiOperation(value = "网约车驾驶员考试报名", notes = "")
    @PostMapping
    public MessageResp insertDriverExamBookInfo(HttpServletRequest request, @RequestBody NetcarDriverExamination param){
        MessageResp messageResp = new MessageResp();
        try{
            AuthUserResp user = sessionHandler.getUser(request);
            if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){

            }
            NetcarDriverExamination netcarDriverExamination = examBookService.insertDriverExamBookInfo(param);
            if (null == netcarDriverExamination) {
                return CommonUtils.returnErrorInfo("报名人数已满，请重新预约其他考试");
            }
            messageResp.setData(netcarDriverExamination);
            messageResp.setResultDesc("报名成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("报名异常");
        }
        return messageResp;
    }

    @ApiOperation(value = "网约车驾驶员考试列表查询", notes = "")
    @GetMapping
    public MessageResp queryDriverExamBookInfoList(HttpServletRequest request,
                                                   @RequestParam(defaultValue = "1") Integer pageNo,
                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                   @RequestParam(required = false) String orderBy){
        MessageResp messageResp = new MessageResp();
        try{
            AuthUserResp user = sessionHandler.getUser(request);
            if(null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())){
                messageResp.setResult(Boolean.FALSE.toString());
                messageResp.setResultDesc("没有权限，请联系管理员");
                return messageResp;
            }
            PageInfo<NetcarDriverExaminationVo> pageInfo = examBookService.queryDriverExamBookInfoList(pageNo,pageSize,orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询异常");
        }
        return messageResp;
    }


    @ApiOperation(value = "网约车驾驶员考试预约审核", notes = "")
    @PutMapping
    public MessageResp verifyDriverExamBookInfo(HttpServletRequest request,@RequestBody NetcarDriverExamination netcarDriverExamination ) {
        MessageResp messageResp = new MessageResp();
        try {
            AuthUserResp user = sessionHandler.getUser(request);
            if (null != user && !Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())) {
                messageResp.setResult(Boolean.FALSE.toString());
                messageResp.setResultDesc("没有权限，请联系管理员");
                messageResp.setStatusCode("-50");
                return messageResp;
            }
            int row = examBookService.verifyDriverExamBookInfo(netcarDriverExamination);
            if (row == 0) {
                messageResp.setResultDesc("审核失败");
                messageResp.setStatusCode("-50");
                return messageResp;
            }
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("审核成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("审核异常");
        }
        return messageResp;
    }

}
