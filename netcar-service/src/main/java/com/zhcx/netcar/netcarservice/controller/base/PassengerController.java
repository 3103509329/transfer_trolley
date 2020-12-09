package com.zhcx.netcar.netcarservice.controller.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoPassenger;
import com.zhcx.netcar.facade.base.PassengerService;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import com.zhcx.netcar.netcarservice.utils.RoleAuthenticationUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/7 10:30
 **/
@RestController
@RequestMapping("/netcar/passengers")
@Api(value = "乘客基本信息",tags = "乘客基本信息")
public class PassengerController {

    private Logger log = LoggerFactory.getLogger(PassengerController.class);

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private RoleAuthenticationUtils roleAuthenticationUtils;
    /**
     * 乘客基本信息查询
     * @return
     */
    @ApiOperation(value = "乘客基本信息查询", notes = "乘客基本信息查询")
    @GetMapping
    public MessageResp queryPassengerList(HttpServletRequest request,
                                          @RequestParam(required = false) String companyId,
                                          @RequestParam(required = false) String passengerPhone,
                                          @RequestParam(defaultValue = "1") Integer pageNo,
                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                          @RequestParam(required = false) String orderBy){
        MessageResp messageResp = new MessageResp();
        try{
            companyId = roleAuthenticationUtils.getCompanyId(request, companyId);

            PageInfo<NetcarBaseInfoPassenger> pageInfo = passengerService.queryPassengerList(companyId, passengerPhone, pageNo, pageSize, orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

}
