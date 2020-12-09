package com.zhcx.authorization.controller.netcar.yunzheng;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.platformtonet.facade.YunZhengDriverService;
import com.zhcx.platformtonet.pojo.base.YunZhengDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
@RequestMapping("/netcar/yz/driver")
public class YzDriverController {

    private Logger log = LoggerFactory.getLogger(YzCompanyController.class);

    @Autowired
    private YunZhengDriverService yunZhengDriverService;

    /**
     * 驾驶员新增（运政）
     * @param yunZhengDriver
     * @return
     */
    @PostMapping
    public MessageResp addDriver(@Valid @RequestBody YunZhengDriver yunZhengDriver){
        MessageResp messageResp = new MessageResp();
        try{
            int i = yunZhengDriverService.selectByLicenseId(yunZhengDriver.getCardno());
            if (i > 0){
                messageResp.setResult(Boolean.TRUE.toString());
                messageResp.setResultDesc("驾驶员信息已存在");
                return messageResp;
            }
            YunZhengDriver pageInfo = yunZhengDriverService.addDriver(yunZhengDriver);
            messageResp.setData(pageInfo);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("新增成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("新增失败");
        }
        return messageResp;
    }

    /**
     * 驾驶员查询（运政）
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping
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

    /**
     * 驾驶员修改（运政）
     * @param yunZhengDriver
     * @return
     */
    @PutMapping
    public MessageResp updateDriver(@Valid @RequestBody YunZhengDriver yunZhengDriver){
        MessageResp messageResp = new MessageResp();
        try{
            YunZhengDriver pageInfo = yunZhengDriverService.updateDriver(yunZhengDriver);
            messageResp.setData(pageInfo);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("修改成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("修改失败");
        }
        return messageResp;
    }

    /**
     * 驾驶员删除（运政）
     * @return
     */
    @DeleteMapping
    public MessageResp deleteDriver(@RequestBody YunZhengDriver yunZhengDriver){
        MessageResp messageResp = new MessageResp();
        try{
            int pageInfo = yunZhengDriverService.deleteDriver(yunZhengDriver.getCardno(), yunZhengDriver.getTime());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("删除成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("删除失败");
        }
        return messageResp;
    }

}
