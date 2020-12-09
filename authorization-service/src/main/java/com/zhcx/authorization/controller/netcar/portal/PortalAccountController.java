package com.zhcx.authorization.controller.netcar.portal;

import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.platformtonet.facade.PortalAccountService;
import com.zhcx.platformtonet.pojo.base.NetcarPortalAccount;
import com.zhcx.platformtonet.vo.LoginParam;
import com.zhcx.platformtonet.vo.NetcatAccountVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/2/26 19:37
 **/
@RestController
@RequestMapping("/netcar/portal")
@Api(value = "门户网站企业信息管理", tags = "门户网站企业信息管理")
public class PortalAccountController {

    private static Logger logger = LoggerFactory.getLogger(PortalAccountController.class);

    @Value("${loginCount}")
    private int loginCount;

    @Value("${errorTime}")
    private int errorTime;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;


    @Resource(name="redisTemplate4Json")
    private RedisTemplate<String, Integer> redisTemplate;

    @Autowired
    private PortalAccountService portalAccountService;

    /**
     * 注册完成后，开放登陆门户网站
     * @param param
     * @return
     */
    @PostMapping("/register")
    @ApiOperation(notes = "门户网站企业注册",value = "门户网站企业注册")
    public MessageResp register(@RequestBody NetcatAccountVo param){
        MessageResp messageResp = new MessageResp();
        try {
            if(StringUtils.isBlank(param.getUsername()) || StringUtils.isBlank(param.getPassword())){
                return CommonUtils.returnErrorInfo("用户名和密码不能为空");
            }
            if(StringUtils.isBlank(param.getPhone()) ){
                return CommonUtils.returnErrorInfo("联系电话不能为空");
            }
            if (StringUtils.equals("1", param.getAccountType()) && StringUtils.isBlank(param.getCompanyName())) {
                return CommonUtils.returnErrorInfo("公司名称不能为空");
            }
            int flag = portalAccountService.checkAccount(param.getUsername());
            if (flag > 0) {
                return CommonUtils.returnErrorInfo("用户名已存在");
            }

            int row = portalAccountService.insertPortalAccount(param);
            if (row == 0) {
                return CommonUtils.returnErrorInfo("注册失败");
            }
            messageResp.setResultDesc("注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return CommonUtils.returnErrorInfo("注册异常");
        }
        return messageResp;
    }


    @PostMapping("/login")
    @ApiOperation(notes = "门户网站登陆",value = "企业账号登陆门户网站")
    public MessageResp login(HttpServletRequest request, @RequestBody LoginParam loginParam){
        MessageResp<AuthUserResp> resp = new MessageResp<AuthUserResp>();
        HttpSession session = request.getSession();

        String password = loginParam.getPassword();
        String account = loginParam.getAccount();
        if(org.apache.commons.lang.StringUtils.isBlank(password) || org.apache.commons.lang.StringUtils.isBlank(account)){
            return CommonUtils.returnErrorInfo("账号或密码不能为空");
        }
        String user_key = "netcar_portal" + account;
        Integer time = redisTemplate.opsForValue().get(user_key);
        if(null == time){
            time = 1;
        }
//        if((int)time>= loginCount){
//        	resp.setStatusCode("-50");
//        	resp.setResult("false");
//			resp.setResultDesc("登录错误5次，30分钟内不可登录!");
//			return resp;
//        }
//
//        String rand = (String)session.getAttribute("rand");
//        if(null == captcha ||"".equals(captcha)){
//        	resp.setStatusCode("-50");
//        	resp.setResult("false");
//			resp.setResultDesc("请输入验证码!");
//			return resp;
//        }
//       if(!captcha.equals(rand)){
//        	resp.setStatusCode("-50");
//        	resp.setResult("false");
//			resp.setResultDesc("验证码不正确!");
//			return resp;
//		}
        Map<String, String> map = new HashMap<String, String>();

        try {
//            password = RSAUtils.decryptreverse(CommonUtils.hex2Byte(password));

            int temp = portalAccountService.verifyAccount(account, password);
            if(temp == 1){
                resp.setResult("true");
                resp.setResultDesc("登录成功!");
                redisTemplate.delete(user_key);

                NetcarPortalAccount netcarPortalAccount = portalAccountService.queryDataByAccount(account);

                /**
                 * 将门户网站账号信息转换为平台用户信息
                 */
                AuthUserResp user = assembleUserAccountInfo(netcarPortalAccount);
                sessionHandler.setUser(session.getId(), user);
                resp.setData(user);

            }else if(temp<0){
                resp.setStatusCode("-50");
                resp.setResult("false");
                resp.setResultDesc("用户名或密码不正确！");
                redisTemplate.opsForValue().set(user_key, time + 1, errorTime, TimeUnit.MINUTES);
            }else if(temp == 0){//账号已禁用
                resp.setStatusCode("-50");
                resp.setResult("false");
                resp.setResultDesc("账号已禁用！");
            }else if(temp ==11){//司机用户类型，个人用户类型web端不允许登录
                resp.setStatusCode("-50");
                resp.setResult("false");
                resp.setResultDesc("司机用户类型，个人用户类型web端不允许登录！");
            }else{//未知错误
                resp.setStatusCode("-50");
                resp.setResult("false");
                resp.setResultDesc("未知错误！");
            }

        } catch (Exception e) {
            logger.error("LoginController--loginPost--前端密码解密异常！账号：" + account + "--密码：" + password, e);
            resp.setStatusCode("-50");
            resp.setResult("false");
            resp.setResultDesc("用户名或密码不正确！");
            return resp;
        }
        return resp;
    }

    private AuthUserResp assembleUserAccountInfo(NetcarPortalAccount netcarPortalAccount) {
        AuthUserResp authUserResp = new AuthUserResp();
        authUserResp.setAccountName(netcarPortalAccount.getAccountName());
        authUserResp.setAccountStatus(netcarPortalAccount.getAccountStatus());
        authUserResp.setCorpId(netcarPortalAccount.getCorpId());
        authUserResp.setAccountType(netcarPortalAccount.getAccountType());
        authUserResp.setMobilePhone(netcarPortalAccount.getPhone());
        authUserResp.setUserId(netcarPortalAccount.getUserId());
        authUserResp.setUserName(netcarPortalAccount.getUserName());
        authUserResp.setUserStatus(netcarPortalAccount.getUserStatus());
        authUserResp.setUserType(netcarPortalAccount.getUserType());

        return authUserResp;
    }


}
