package com.zhcx.authorization.controller.taxi;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zhcx.authorization.config.SessionConfig.SessionHandler;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.basicdata.facade.taxi.TaxiPhoneBookService;
import com.zhcx.basicdata.pojo.taxi.TaxiPhoneBook;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 电话本
 * @author tangding
 *
 */
@RestController
@RequestMapping("/taxi/phone")
@Api(value = "phonebook", tags = "出租车电话本接口")
public class TaxiPhoneBookController {


    private static final Logger logger = LoggerFactory.getLogger(TaxiPhoneBookController.class);


    @Autowired
    private TaxiPhoneBookService taxiPhoneBookService;

    @Autowired
    private SessionHandler sessionHandler;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "查询出租车辆信息", notes = "参数为出租车辆信息对象")
    public MessageResp<List<TaxiPhoneBook>> queryTaxiBaseInfoVehicle(HttpServletRequest request,@ModelAttribute TaxiPhoneBook param) {
        MessageResp<List<TaxiPhoneBook>> result = new MessageResp<List<TaxiPhoneBook>>();
        /*AuthUserResp authUser = sessionHandler.getUser(request);
        //非管理员用户只能够查看自己企业设置的电话本
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
            if(authUser.getCorpId() != null && authUser.getCorpId() != 0L){
                param.setCorpId(String.valueOf(authUser.getCorpId()));
            }
        }*/
        try {
        	List<TaxiPhoneBook> list = taxiPhoneBookService.queryByTaxiPhoneBook(param);
            result.setData(list);
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("查询失败");
        }
        return result;
    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "新增电话本信息", notes = "参数为电话本信息对象")
    public MessageResp addPhone(HttpServletRequest request, @RequestBody TaxiPhoneBook record) {
        MessageResp result = new MessageResp();
        int addResult = 0;
        Map<String,Object> checkRes = null;
        try {
            addResult = taxiPhoneBookService.add(record);
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("添加失败");
        }
        return result;
    }


    @RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
    @ApiOperation(value = "修改电话本信息", notes = "参数为电话本信息对象")
    public MessageResp modifyPhone(HttpServletRequest request, @RequestBody TaxiPhoneBook record) {
        MessageResp result = new MessageResp();
        int updResult = 0;
        try {
            updResult =taxiPhoneBookService.modify(record);
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("修改失败");
        }
        return result;
    }

	@ResponseBody
	@RequestMapping(value = "{uuid}", method = RequestMethod.DELETE)
	public MessageResp delete(@PathVariable Long uuid) {
        MessageResp result = new MessageResp();
        int delResult = 0;
        try {
            delResult = taxiPhoneBookService.deleteByPrimaryKey(uuid);
            result.setResult(Boolean.TRUE.toString());
            result.setResultDesc("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(Boolean.FALSE.toString());
            result.setStatusCode("-50");
            result.setResultDesc("删除失败");
        }
        return result;
    }


}
