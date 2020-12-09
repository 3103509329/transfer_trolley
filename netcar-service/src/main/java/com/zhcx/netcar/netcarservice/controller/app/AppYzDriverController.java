package com.zhcx.netcar.netcarservice.controller.app;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.yunzheng.YunZhengDriverService;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import com.zhcx.netcar.pojo.yuzheng.YunZhengDriver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * 驾驶员
 * 运政数据源
 *
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/3/7 0007 17:04
 **/
@RestController
@RequestMapping("/netcar/app/driver")
@Api(value = "driver", tags = "驾驶员基本信息（App）")
public class AppYzDriverController {

    private Logger log = LoggerFactory.getLogger(AppYzCompanyController.class);

    @Autowired
    private YunZhengDriverService yunZhengDriverService;

    /**
     * 驾驶员查询（运政）
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping
    @ApiOperation(value = "驾驶员基本信息查询（App）", notes = "")
    public MessageResp selectDriver(@RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) String busiRegNumber,
                                    @RequestParam(defaultValue = "1") Integer pageNo,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(required = false) String orderBy){
        MessageResp messageResp = new MessageResp();
        try{
            PageInfo<YunZhengDriver> pageInfo = yunZhengDriverService.selectDriver(keyword, busiRegNumber, pageNo, pageSize, orderBy);
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
