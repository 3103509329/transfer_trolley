package com.zhcx.authorization.controller.base;

import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.basicdata.facade.base.BaseDictionaryService;
import com.zhcx.basicdata.pojo.base.BaseDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/taxi/baseDictionary")
@Api(value = "dictionary", tags = "dictionary接口")
public class DictionaryController {

    private Logger log = LoggerFactory.getLogger(DictionaryController.class);

    @Resource
    private BaseDictionaryService dictionaryService;

    @GetMapping("/queryList")
    @ApiOperation(value = "数据字典，不带分页", notes = "参数为字典类型代码")
    public MessageResp selectDictionaryList(String categoryCode) {
        MessageResp result = new MessageResp();
        try {
            List<BaseDictionary> list = dictionaryService.selectDictionary(categoryCode);
            result.setData(list);
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("查询数据字典成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("查询数据字典失败");
        }
        return result;
    }

    @GetMapping("/districtList")
    @ApiOperation(value = "数据字典省市区，不带分页", notes = "参数为类型，上级id")
    public MessageResp districtList(String type, String parent){
        MessageResp result = new MessageResp();
        try {
            List list = dictionaryService.districtList(type, parent);
            result.setData(list);
            result.setResult(Boolean.TRUE.toString());
            result.setStatusCode("00");
            result.setResultDesc("处理成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("处理失败");
        }
        return result;
    }

}
