package com.zhcx.netcar.netcarservice.controller.app;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.netcar.facade.app.NewsService;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import com.zhcx.netcar.pojo.app.NetcarNews;
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
 * @date 2019/2/24 19:26
 **/
@RestController
@RequestMapping("/netcar/app/news")
@Api(value = "news", tags = "新闻资讯信息")
public class NetcarNewsController {

    private Logger log = LoggerFactory.getLogger(NetcarNewsController.class);

    @Autowired
    private NewsService newsService;

    /**
     * 新闻资讯信息列表查询
     * @return
     */
    @ApiOperation(value = "新闻资讯信息列表查询", notes = "")
    @GetMapping("/{type}")
    public MessageResp selectNewsInfoListByType(HttpServletRequest request,
                                                @PathVariable Integer type,
                                                @RequestParam(defaultValue = "1") Integer pageNo,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(required = false) String orderBy,
                                                @RequestParam(required = false) String keyword){

        MessageResp messageResp = new MessageResp();
        try{
            PageInfo<NetcarNews> pageInfo = newsService.selectNewsInfoListByType(type,pageNo,pageSize,orderBy,keyword);
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
     * 新闻资讯信息新增
     * @return
     */
    @ApiOperation(value = "新闻资讯信息新增", notes = "")
    @PostMapping
    public MessageResp insertNewsInfo(HttpServletRequest request,@RequestBody NetcarNews param){

        MessageResp messageResp = new MessageResp();
        try{
            NetcarNews netcarNews = newsService.insertNewsInfo(param);
            messageResp.setData(netcarNews);
            messageResp.setResultDesc("新增成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("新增异常");
        }
        return messageResp;
    }

    /**
     * 新闻资讯信息删除
     * @return
     */
    @ApiOperation(value = "新闻资讯信息删除", notes = "")
    @DeleteMapping("/{uuid}")
    public MessageResp deleteNewsInfo(HttpServletRequest request,@PathVariable Long uuid){

        MessageResp messageResp = new MessageResp();
        try{
            int row = newsService.deleteNewsInfo(uuid);
            if (row == 0) {
                return CommonUtils.returnErrorInfo("删除失败");
            }
            messageResp.setResultDesc("删除成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("删除异常");
        }
        return messageResp;
    }


    /**
     * 新闻资讯信息修改
     * @return
     */
    @ApiOperation(value = "新闻资讯信息修改", notes = "")
    @PutMapping
    public MessageResp updateNewsInfo(HttpServletRequest request,@RequestBody NetcarNews param){

        MessageResp messageResp = new MessageResp();
        try{
            int row = newsService.updateNewsInfo(param);
            if (row == 0) {
                return CommonUtils.returnErrorInfo("修改失败");
            }
            messageResp.setResultDesc("修改成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("修改异常");
        }
        return messageResp;
    }
}
