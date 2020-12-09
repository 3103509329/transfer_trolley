package com.zhcx.netcar.netcarservice.controller.yz;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zhcx.basic.dubbo.BaseInfoEmplDubboService;
import com.zhcx.basic.model.BaseInfoEmpl;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * 驾驶员
 * 运政数据源
 *
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/3/7 0007 17:04
 **/
@RestController
@RequestMapping("/netcar/yz/driver")
public class YzDriverController {

    private Logger log = LoggerFactory.getLogger(YzCompanyController.class);

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private BaseInfoEmplDubboService baseInfoEmplService;

    /**
     * 驾驶员查询（运政）
     *
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping
    public MessageResp selectDriver(@RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) Long corpId,
                                    @RequestParam(required = false) String busiRegNumber,
                                    @RequestParam(defaultValue = "1") Integer pageNo,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(required = false) String orderBy,
                                    @RequestParam(required = false) Integer dataType) {
        MessageResp messageResp = new MessageResp();
        PageInfo<BaseInfoEmpl> pageInfo = null;
        try {
            Integer source = null;
            if (dataType != null && !dataType.equals("")) {
                source = dataType;
            }
            orderBy ="time_created_desc";
            pageInfo = baseInfoEmplService.select(corpId,busiRegNumber, keyword, "driverName", source, pageNo, pageSize, orderBy);
            messageResp.setData(pageInfo.getList());
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }
}


