package com.zhcx.auth.controller.auth;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.zhcx.auth.facade.*;
import com.zhcx.auth.pojo.*;
import com.zhcx.auth.utils.UserAuthUtils;
import com.zhcx.auth.vo.ApplicationUserBaseInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.config.SessionConfig;
import com.zhcx.auth.utils.Constants;
import com.zhcx.auth.utils.MessageResp;
import com.zhcx.auth.utils.PageBeanUtil;
import com.zhcx.auth.vo.ApplicationBaseInfoVO;
import com.zhcx.auth.vo.ApplicationRoleBaseInfoVO;

import io.swagger.annotations.Api;

/**
 * @ClassName：ApplicationController
 * @Description:
 * @author：李亮
 * @date：2020/4/215:18
 */

@Controller
@RequestMapping("/application")
@Api(value = "应用及角色管理", tags = "应用及角色管理")
public class ApplicationController {

    private Logger log = LoggerFactory.getLogger(ApplicationController.class);
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ApplicationRoleService applicationRoleService;
    @Resource
    private AuthRoleService authRoleService;
    @Autowired
    private AuthUserService authUserService;

    //**************************应用****************************
    @PostMapping("/add")
    @ResponseBody
    public MessageResp addApplication(HttpServletRequest request, @RequestBody ApplicationBaseInfo applicationBaseInfo) {
        MessageResp<ApplicationBaseInfo> message = new MessageResp<ApplicationBaseInfo>();
        try {

            ApplicationBaseInfoVO application = new ApplicationBaseInfoVO();
            application.setCode(applicationBaseInfo.getCode());
            PageInfo<ApplicationBaseInfo> applicatioRresult = applicationService.selectList(application);
            if (applicatioRresult.getSize() > 0) {
                message.setResult(Boolean.FALSE.toString());
                message.setResultDesc("应用Code已存在");
                return message;
            }
            ApplicationBaseInfo result = applicationService.add(applicationBaseInfo);
            if (null == result) {
                message.setData(result);
                message.setResult(Boolean.FALSE.toString());
                message.setResultDesc("新增失败");
            } else {
                message.setData(result);
                message.setResultDesc("新增成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            message.setResult(Boolean.FALSE.toString());
            message.setResultDesc("新增异常");
            return message;
        }
        return message;
    }

    @GetMapping("/list")
    @ResponseBody
    public MessageResp select(HttpServletRequest request, @ModelAttribute ApplicationBaseInfoVO applicationBaseInfo) {
        MessageResp<List<ApplicationBaseInfo>> message = new MessageResp<List<ApplicationBaseInfo>>();
        try {

            PageInfo<ApplicationBaseInfo> data = applicationService.selectList(applicationBaseInfo);
            if (null == data) {
                message.setData(data.getList());
                message.setResult(Boolean.FALSE.toString());
                message.setResultDesc("查询失败");
            } else {
                message.setData(data.getList());
                message.setPageBean(PageBeanUtil.createPageBean(data));
                message.setResultDesc("查询成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            message.setResult(Boolean.FALSE.toString());
            message.setResultDesc("查询异常");
            return message;
        }
        return message;
    }

    @PostMapping("/update")
    @ResponseBody
    public MessageResp update(HttpServletRequest request, @RequestBody ApplicationBaseInfoVO applicationBaseInfoVO) {
        MessageResp<List<ApplicationBaseInfo>> message = new MessageResp<List<ApplicationBaseInfo>>();
        AuthUserResp authUser = UserAuthUtils.getUser();
        try {

            applicationBaseInfoVO.setModifier(authUser.getUserId());
            applicationBaseInfoVO.setModifierTime(sdf.format(new Date()));
            Integer data = applicationService.update(applicationBaseInfoVO);
            if (null == data) {
                message.setResult(Boolean.FALSE.toString());
                message.setResultDesc("修改失败");
            } else {
                message.setResultDesc("修改成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            message.setResult(Boolean.FALSE.toString());
            message.setResultDesc("修改异常");
            return message;
        }
        return message;
    }

    @PostMapping("/del")
    @ResponseBody
    public MessageResp delete(HttpServletRequest request, @RequestBody ApplicationBaseInfoVO applicationBaseInfoVO) {
        MessageResp<List<ApplicationBaseInfo>> message = new MessageResp<List<ApplicationBaseInfo>>();
        AuthUserResp authUser = UserAuthUtils.getUser();
        try {

            applicationBaseInfoVO.setStatus(0);
            applicationBaseInfoVO.setModifierTime(sdf.format(new Date()));
            Integer data = applicationService.del(applicationBaseInfoVO);
            if (null == data) {
                message.setResult(Boolean.FALSE.toString());
                message.setResultDesc("删除失败，应用已被使用");
            } else {
                message.setResultDesc("删除成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            message.setResult(Boolean.FALSE.toString());
            message.setResultDesc("删除异常");
            return message;
        }
        return message;
    }

    //**************************应用与角色关系****************************

    @PostMapping("/role/add")
    @ResponseBody
    public MessageResp roleAdd(HttpServletRequest request, @RequestBody ApplicationRoleBaseInfoVO applicationRoleBaseInfoVO) {
        MessageResp<List<ApplicationRoleBaseInfoVO>> message = new MessageResp<List<ApplicationRoleBaseInfoVO>>();
        try {
            Boolean data = applicationRoleService.add(applicationRoleBaseInfoVO);
            if (false == data) {
                message.setResult(Boolean.FALSE.toString());
                message.setResultDesc("新增失败");
            } else {
                message.setResultDesc("新增成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            message.setResult(Boolean.FALSE.toString());
            message.setResultDesc("新增异常");
            return message;
        }
        return message;
    }

    @GetMapping("/role/list")
    @ResponseBody
    public MessageResp roleSelect(HttpServletRequest request, @RequestParam Long userId) {
        MessageResp<List<ApplicationRoleBaseInfoVO>> message = new MessageResp<List<ApplicationRoleBaseInfoVO>>();
        try {
            ApplicationBaseInfoVO applicationBaseInfoVO = new ApplicationBaseInfoVO();
            applicationBaseInfoVO.setUserId(userId);
            List<ApplicationRoleBaseInfoVO> applicationBaseInfos = applicationService.selectByUser(applicationBaseInfoVO);
            if (null == applicationBaseInfos) {
                message.setResult(Boolean.FALSE.toString());
                message.setResultDesc("查询失败");
            } else {
                message.setData(applicationBaseInfos);
                message.setResultDesc("查询成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            message.setResult(Boolean.FALSE.toString());
            message.setResultDesc("查询异常");
            return message;
        }
        return message;
    }

    @GetMapping("/role/all")
    @ResponseBody
    public MessageResp roleSelectAll(HttpServletRequest request, @ModelAttribute ApplicationBaseInfoVO applicationBaseInfoVO) {
        MessageResp<List<ApplicationRoleBaseInfoVO>> message = new MessageResp<List<ApplicationRoleBaseInfoVO>>();
        try {
            AuthUserResp authUser = UserAuthUtils.getUser();

            ApplicationBaseInfoVO applicationBase = new ApplicationBaseInfoVO();
            applicationBase.setUserId(applicationBaseInfoVO.getUserId());
            List<ApplicationRoleBaseInfoVO> applicationBaseInfos = applicationService.selectApplicationAndRole(applicationBase);
            List<ApplicationRoleBaseInfoVO> resultData = new ArrayList<>();
            switch (authUser.getUserType()) {
                case "01":
                    resultData = applicationBaseInfos;
                    break;
                case "02":
                    resultData = applicationBaseInfos;
                    break;
                case "03":
                    applicationBaseInfoVO.setUserId(null);
                    applicationBaseInfoVO.setUuid(authUser.getUserId());
                    List<ApplicationRoleBaseInfoVO> applicationRoleList = applicationService.selectApplicationAndRole(applicationBaseInfoVO);
                    Map<String, List<ApplicationRoleBaseInfoVO>> applicationRoleMap = applicationRoleList.stream().collect(Collectors.groupingBy(ApplicationRoleBaseInfoVO::getApplicationCode));

                    Map<String, List<ApplicationRoleBaseInfoVO>> applicationBaseInMap = applicationBaseInfos.stream().collect(Collectors.groupingBy(ApplicationRoleBaseInfoVO::getApplicationCode));
                    List<ApplicationRoleBaseInfoVO> finalResultData = new ArrayList<>();
                    applicationRoleMap.keySet().forEach(a -> {
                        List<ApplicationRoleBaseInfoVO> data = applicationRoleMap.get(a);
                        List<ApplicationRoleBaseInfoVO> excessiveList = new ArrayList<>();
                        data.forEach(b -> {
                            //当应用下的角色为企业时，只获取单个角色数据
                            if (b.getRoleType().equals("2")) {
                                excessiveList.add(b);
                                return;
                            }
                        });
                        //当应用下的角色为监管时，获取所有角色
                        if (excessiveList.size() <= 0) {
                            excessiveList.addAll(applicationBaseInMap.get(a));
                        }
                        finalResultData.addAll(excessiveList);
                    });
                    resultData = finalResultData;
                    break;

                default:
                    break;
            }

//            //当用户类型为企业时，所获得的角色只能有企业类；修改时间20200608
            List<ApplicationRoleBaseInfoVO> result = new ArrayList<>();
            AuthUser user = authUserService.queryAuthUserByUuid(applicationBaseInfoVO.getUuid());
            if (user != null && user.getUserType().equals("03")) {
                result = resultData.stream().filter(a -> a.getRoleType().equals("2")).collect(Collectors.toList());
            } else if (user != null && user.getUserType().equals("02")) {
                result = resultData.stream().filter(a -> a.getRoleType().equals("1")).collect(Collectors.toList());
            }else {
                result = resultData;
            }
            if (null == result) {
                message.setResult(Boolean.FALSE.toString());
                message.setResultDesc("查询失败");
            } else {
                message.setData(result);
                message.setResultDesc("查询成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            message.setResult(Boolean.FALSE.toString());
            message.setResultDesc("查询异常");
            return message;
        }
        return message;
    }

    @GetMapping("/user/list")
    @ResponseBody
    public MessageResp userSelete(HttpServletRequest request, @ModelAttribute ApplicationUserBaseInfoVO applicationBaseInfoVO) {
        MessageResp<List<ApplicationBaseInfoVO>> message = new MessageResp<List<ApplicationBaseInfoVO>>();
        AuthUserResp authUser = UserAuthUtils.getUser();
        try {
            if (null == authUser || null == authUser.getUserId()) {
                message.setResult(Boolean.FALSE.toString());
                message.setResultDesc("查询失败");
            } else {
                applicationBaseInfoVO.setUserId(authUser.getUserId());
                List<ApplicationBaseInfoVO> applicationBaseInfos = applicationService.selectUserList(applicationBaseInfoVO);
                if (null == applicationBaseInfos) {
                    message.setResult(Boolean.FALSE.toString());
                    message.setResultDesc("查询失败");
                } else {
                    message.setData(applicationBaseInfos);
                    message.setResultDesc("查询成功");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            message.setResult(Boolean.FALSE.toString());
            message.setResultDesc("查询异常");
            return message;
        }
        return message;
    }

    //********************************鉴权**************
    private Boolean jurisdiction(AuthUserResp user) {
        //平台管理员放行

        if (Constants.AUTH_USER_TYPE_SYS.equals(user.getUserType())) {
            return true;
        }
        return applicationService.jurisdiction(user);
    }
}
