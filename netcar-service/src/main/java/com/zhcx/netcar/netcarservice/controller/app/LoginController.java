package com.zhcx.netcar.netcarservice.controller.app;


import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Maps;
import com.zhcx.auth.dubbo.AccountInfoDubboService;
import com.zhcx.auth.dubbo.AuthUserDubboService;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.auth.pojo.LoginUserInfo;
import com.zhcx.netcar.netcarservice.config.JwtTokenUtil;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author Administrator
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private AccountInfoDubboService accountInfoService;

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private AuthUserDubboService authUserService;

    @Value("${loginCount}")
    private int loginCount;

    @Value("${errorTime}")
    private int errorTime;

    @Resource(name = "redisTemplate4Json")
    private RedisTemplate<String, Integer> redisTemplate;

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/")
    public String index(@SessionAttribute("user") String account, Model model) {
        model.addAttribute("name", account);
        return "index";
    }

    @RequestMapping("/login")
    public MessageResp login() {
        MessageResp resp = new MessageResp();
        resp.setStatusCode("-99");
        resp.setResult("false");
        resp.setResultDesc("请登录!");
        return resp;
    }

    @PostMapping("/loginAppPost")
    public @ResponseBody
    MessageResp<AuthUserResp> loginPost(HttpServletRequest request,
                                        @RequestBody LoginUserInfo userInfo) {
        MessageResp resp = new MessageResp();
        HttpSession session = request.getSession();

        Integer time = redisTemplate.opsForValue().get(userInfo.getAccount());
        if (null == time) {
            time = 1;
        }
        if ((int) time >= loginCount) {
            resp.setStatusCode("-50");
            resp.setResult("false");
            resp.setResultDesc("登录错误5次，30分钟内不可登录!");
            return resp;
        }

        String rand = (String) session.getAttribute("rand");

        Map<String, String> map = new HashMap<String, String>();


        int temp = accountInfoService.verifyAccount(userInfo.getAccount(), userInfo.getPassword());
        if (temp == 1) {
            resp.setResult("true");
            resp.setResultDesc("登录成功!");
            AuthUserResp user = authUserService.queryDataByAccount(userInfo.getAccount());
            redisTemplate.delete(userInfo.getAccount());
            String jwtToken = JwtTokenUtil.generateToken(userInfo.getAccount());
            Map<String,Object> resultMap = Maps.newHashMap();
            map.put("token", jwtToken);
            map.put("user", user.getUserName());
            map.put("userId", String.valueOf(user.getUserId()));
            resp.setData(map);

        } else if (temp < 0) {//账号或密码不正确
            resp.setStatusCode("-50");
            resp.setResult("false");
            resp.setResultDesc("用户名或密码不正确！");
            redisTemplate.opsForValue().set(userInfo.getAccount(), time + 1, errorTime, TimeUnit.MINUTES);
        } else if (temp == 0) {//账号已禁用
            resp.setStatusCode("-50");
            resp.setResult("false");
            resp.setResultDesc("账号已禁用！");
        } else if (temp == 11) {//司机用户类型，个人用户类型web端不允许登录
            resp.setStatusCode("-50");
            resp.setResult("false");
            resp.setResultDesc("司机用户类型，个人用户类型web端不允许登录！");
        } else {//未知错误
            resp.setStatusCode("-50");
            resp.setResult("false");
            resp.setResultDesc("未知错误！");
        }


        return resp;
    }

    @GetMapping("/test")
    public String test() {

        return "这是一个测试";
    }


    @RequestMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public MessageResp logout(HttpSession session) {
        MessageResp resp = new MessageResp();
        // 移除session
        resp.setResult("true");
        resp.setResultDesc("登出成功!");
        return resp;
    }


}