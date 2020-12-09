package com.zhcx.authorization.controller.examine;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.HttpClientUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.regionmonitor.examine.TaxiBaseInfoDriverExamine;
import com.zhcx.regionmonitor.examine.TaxiBaseInfoDriverExamineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/1/14 0014 15:07
 **/
@RestController
@RequestMapping("/netcar/examine")
@Api(value = "驾驶员信息审核", tags = "")
public class ExamineDriverController {

    private Logger log = LoggerFactory.getLogger(ExamineDriverController.class);

    @Autowired
    private TaxiBaseInfoDriverExamineService taxiBaseInfoDriverExamineService;

    @Value("${examine.url}")
    private String url;

    @Value("${examine.userName}")
    private String userName;

    @Value("${examine.password}")
    private String password;

    @Value("${examine.serviceId}")
    private String serviceId;

//    @Resource(name = "redisTemplate4Json")
//    private RedisTemplate<String, String> redisTemplate;

    /**
     * 驾驶员审核信息列表查询
     *
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping("/drivers")
    @ApiOperation(value = "驾驶员审核信息列表查询", notes = "")
    public MessageResp selectDrivers(@RequestParam String keyword, String companyId,
                                     @RequestParam(defaultValue = "1") Integer pageNo,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam String orderBy) {

        MessageResp messageResp = new MessageResp();
        try {
            PageInfo<TaxiBaseInfoDriverExamine> taxiBaseInfoDriverExaminesList = taxiBaseInfoDriverExamineService.selectDrivers(keyword, companyId, orderBy, pageNo, pageSize);
            messageResp.setPageBean(PageBeanUtil.createPageBean(taxiBaseInfoDriverExaminesList));
            messageResp.setData(taxiBaseInfoDriverExaminesList.getList());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("查询异常");
        }
        return messageResp;
    }

    /**
     * 驾驶员审核信息详情查询
     *
     * @param uuid
     * @return
     */
    @GetMapping("/driver")
    @ApiOperation(value = "驾驶员审核详情查询", notes = "")
    public MessageResp selectDriver(@RequestParam Long uuid) {

        MessageResp messageResp = new MessageResp();
        try {
            TaxiBaseInfoDriverExamine taxiBaseInfoDriverExamine = taxiBaseInfoDriverExamineService.selectDriver(uuid);
            messageResp.setData(taxiBaseInfoDriverExamine);
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("查询异常");
        }
        return messageResp;
    }

    @PostMapping
    @ApiOperation(value = "驾驶员信息审核接口")
    public MessageResp postDriver(@RequestBody TaxiBaseInfoDriverExamine taxiBaseInfoDriverExamine) {
        MessageResp messageResp = new MessageResp();
        String str = "{\n" +
                "    \"userName\":\"" + userName + "\",\n" +
                "    \"password\":\"" + password + "\",\n" +
                "    \"serviceId\":\"" + serviceId + "\",\n" +
                "    \"condition\":{\n" +
                "        \"XM\":\"" + taxiBaseInfoDriverExamine.getName() + "\",\n" +
                "        \"ZJHM\":\"" + taxiBaseInfoDriverExamine.getIdCertificateNumber() + "\"\n" +
                "    }\n" +
                "}";

        try {
            System.out.println(str);
            String response = HttpClientUtils.httpPostJsonRequest(url, str);
            String data = "{\\\"isPass\\\":true}";
            String data2= "{\\\"isPass\\\":false}";
            if (response.contains(data)) {
                taxiBaseInfoDriverExamine.setAuditType(2);
                taxiBaseInfoDriverExamineService.updataDriver(taxiBaseInfoDriverExamine);
                taxiBaseInfoDriverExamine.setAuditType(2);
                taxiBaseInfoDriverExamine.setAuditTime(new Date());
                messageResp.setData(taxiBaseInfoDriverExamine);
                messageResp.setResultDesc("审核通过");
            } if (response.contains(data2)){
                taxiBaseInfoDriverExamine.setAuditType(3);
                taxiBaseInfoDriverExamineService.updataDriver(taxiBaseInfoDriverExamine);
                taxiBaseInfoDriverExamine.setAuditTime(new Date());
                messageResp.setData(taxiBaseInfoDriverExamine);
                messageResp.setStatusCode("-50");
                messageResp.setResult(Boolean.FALSE.toString());
                messageResp.setResultDesc("审核未通过");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("审核异常");
        }
        return messageResp;
    }

    @PostMapping("/driver")
    @ApiOperation(value = "驾驶员信息审核接口")
    public MessageResp addDriver(@RequestBody TaxiBaseInfoDriverExamine taxiBaseInfoDriverExamine){
        MessageResp messageResp = new MessageResp();
        try{
            taxiBaseInfoDriverExamine = taxiBaseInfoDriverExamineService.addDriver(taxiBaseInfoDriverExamine);
            messageResp.setData(taxiBaseInfoDriverExamine);
            messageResp.setResultDesc("新增成功");
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("新增异常");
        }
        return messageResp;
    }

    @PostMapping("/drivers")
    @ApiOperation(value = "驾驶员基本信息审核接口（通过Redis消息队列实现）")
    public MessageResp addRedisDriver(@RequestBody List<TaxiBaseInfoDriverExamine> taxiBaseInfoDriverExamineList){
        MessageResp messageResp = new MessageResp();
        if (taxiBaseInfoDriverExamineList.size() == 0){
            log.error("驾驶员基本信息审核数据为空");
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("新增异常");
            return messageResp;
        }
        try{

        }catch (Exception e){

        }
        return messageResp;
    }

}
