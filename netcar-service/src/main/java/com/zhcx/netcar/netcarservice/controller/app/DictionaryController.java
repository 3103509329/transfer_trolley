package com.zhcx.netcar.netcarservice.controller.app;

import com.zhcx.netcar.facade.app.BaseDictionaryService;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.pojo.base.BaseDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-08 17:27
 * 查询字典
 */
@RestController
@RequestMapping("/netcar/app/dictionary")
public class DictionaryController {
    private Logger log = LoggerFactory.getLogger(DictionaryController.class);

    @Autowired
    private BaseDictionaryService dictionaryService;

    /**
     * 查询数据字典
     * @param keyword
     * @return
     */
    @GetMapping
    public MessageResp getDictionary(@RequestParam(value = "keyword",required = false,defaultValue = "") String keyword){
        MessageResp messageResp = new MessageResp();
        try{
            List<BaseDictionary> list = dictionaryService.selectDictionary(keyword);
            messageResp.setData(list);
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
