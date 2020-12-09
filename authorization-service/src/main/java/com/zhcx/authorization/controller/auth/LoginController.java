package com.zhcx.authorization.controller.auth;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zhcx.authorization.config.SystemControllerLog;
import com.zhcx.platformtonet.facade.YunZhengCompanyService;
import com.zhcx.platformtonet.pojo.base.YunZhengCompany;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.zhcx.auth.facade.AccountInfoService;
import com.zhcx.auth.facade.AuthUserService;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig.SessionHandler;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.RSAUtils;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoCompanyService;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/auth")
public class LoginController {
	
	 @Autowired
	 private SessionHandler sessionHandler;
	
	 @Resource
	 private AuthUserService authUserService;
	 
	 @Resource
	 private AccountInfoService accountInfoService;
	 
	 @Resource
	 private TaxiBaseInfoCompanyService taxiBaseInfoCompanyService;

	 @Autowired
     private YunZhengCompanyService yunZhengCompany;

	 @Value("${loginCount}")
	 private int loginCount;
	 
	 @Value("${errorTime}")
	 private int errorTime;
	 
	 @Value("${isCaptcha}")
	 private String isCaptcha;
	 
	 @Resource(name="redisTemplate4Json")
	 private RedisTemplate<String, Integer> redisTemplate;
	 /*@Resource
	 private AuthMenuService authMenuService;*/
	 
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

    @SystemControllerLog(description = "登录系统",actionType = "1")
    @ApiOperation(value = "用户登录接口", notes = "")
	@RequestMapping(value = "/loginPost", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody MessageResp<AuthUserResp> loginPost(HttpServletRequest request,@RequestParam String account,@RequestParam String password,String captcha) {
        MessageResp<AuthUserResp> resp = new MessageResp<AuthUserResp>();
        HttpSession session = request.getSession();
        
        Integer time = redisTemplate.opsForValue().get(account);
        if(null == time){
        	time = 1;
        }
        if((int)time>= loginCount){
        	resp.setStatusCode("-50");
        	resp.setResult("false");
			resp.setResultDesc("登录错误5次，30分钟内不可登录!");
			return resp;
        }

        String rand = (String)session.getAttribute("rand");
        if(null == captcha ||"".equals(captcha)){
        	resp.setStatusCode("-50");
        	resp.setResult("false");
			resp.setResultDesc("请输入验证码!");
			return resp;
        }
        if(isCaptcha.equals("true")){//验证码生效开关
        	if(!captcha.equals(rand)){
            	resp.setStatusCode("-50");
            	resp.setResult("false");
    			resp.setResultDesc("验证码不正确!");
    			return resp;
    		}
        }
		Map<String, String> map = new HashMap<String, String>();
		
		String de_result;
		try {
			de_result = RSAUtils.decryptreverse(CommonUtils.hex2Byte(password));//RSAUtil.decryptreverse(en_result);
			password = de_result;
		} catch (Exception e) {
			logger.error("LoginController--loginPost--前端密码解密异常！账号："+account+"--密码："+password, e);
			resp.setStatusCode("-50");
			resp.setResult("false");
			resp.setResultDesc("用户名或密码不正确！");
			return resp;
		}
	
		int temp = accountInfoService.verifyAccount(account,password);
		if(temp == 1){
			resp.setResult("true");
			resp.setResultDesc("登录成功!");
			AuthUserResp user= authUserService.queryDataByAccount(account);
			if(user.getCorpId()!=null){
			    if (!StringUtils.equals(user.getSource(), "netcar")){
                    Map<String,Object> corpMap = taxiBaseInfoCompanyService.queryCityAndDisByCompany(user.getCorpId().toString());
                    user.setCorpName(corpMap.get("corp_name").toString());
                }else {
                    YunZhengCompany corp = yunZhengCompany.selectByCompanyId(user.getCorpId());
                    user.setCorpName(corp.getClitname());
                }
			}
			redisTemplate.delete(account);
			sessionHandler.setUser(session.getId(), user);
			resp.setData(user);
		}else if(temp<0){//账号或密码不正确
			resp.setStatusCode("-50");
			resp.setResult("false");
			resp.setResultDesc("用户名或密码不正确！");
			redisTemplate.opsForValue().set(account, time+1, errorTime, TimeUnit.MINUTES);
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
		
		//登录日志待补充
		
		
        return resp;
    }


	@SystemControllerLog(description = "退出系统",actionType = "1")
    @ApiOperation(value = "用户登出接口", notes = "")
    @RequestMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public MessageResp logout(HttpSession session) {
    	 MessageResp resp = new MessageResp();
        // 移除session
    	sessionHandler.removeSession(session.getId());
    	resp.setResult("true");
		resp.setResultDesc("登出成功!");
        return resp;
    }
    
    @RequestMapping(value = "/captcha")
    public void captcha(HttpServletRequest request,HttpServletResponse response){
    	HttpSession session = request.getSession(); 
    	/**
    	 * 定义验证码类型
    	 * @see 1--纯数字,2--纯汉字
    	 * @see 这里也支持数字和英文字母组合,但考虑到不好辨认,故注释了这部分代码,详见69行
    	 */
    	int captchaType = 1;
    	
    	//设置页面不缓存
    	response.setHeader("Pragma", "No-cache");
    	response.setHeader("Cache-Control", "no-cache");
    	response.setDateHeader("Expires", 0);

    	//创建随机类实例
    	Random random = new Random();
    	//定义图片尺寸
    	int width=60*captchaType, height=(captchaType==1)?20:30;
    	//创建内存图像
    	BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    	//获取图形上下文
    	Graphics g = image.getGraphics();
    	//设定背景色
    	g.setColor(this.getRandColor(random, 200, 250));
    	//设定图形的矩形坐标及尺寸
    	g.fillRect(0, 0, width, height);

    	String sRand = "";
    	if(captchaType == 1){
    		//图片背景随机产生50条干扰线作为噪点
    		g.setColor(this.getRandColor(random, 90, 200));
    		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
    		for(int i=0; i<50; i++){
    			int x11 = random.nextInt(width);
    			int y11 = random.nextInt(height);
    			int x22 = random.nextInt(width);
    			int y22 = random.nextInt(height);
    			g.drawLine(x11, y11, x11+x22, y11+y22);
    		}
    		//取随机产生的4个数字作为验证码
    		//String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    		//String str = "abcdefghkmnpqrstwxyABCDEFGHJKLMNPRSTWXYZ123456789";
    		String str = "abcdefghkmnpqrstwxy123456789";
    		for(int i=0; i<4; i++){
    			//String rand = String.valueOf(str.charAt(random.nextInt(62)));
    			//String rand = String.valueOf(str.charAt(random.nextInt(49)));
    			String rand = String.valueOf(str.charAt(random.nextInt(28)));
    			//String rand = String.valueOf(random.nextInt(10));
    			sRand += rand;
    			g.setColor(this.getRandColor(random, 10, 150));
    			//将此数字画到图片上
    			g.drawString(rand, 13*i+6, 16);
    		}
    	}else{
    		//设定备选汉字
    		String base = "\u7684\u4e00\u4e86\u662f\u6211\u4e0d\u5728\u4eba\u4eec\u6709\u6765\u4ed6\u8fd9\u4e0a\u7740" +
    					  "\u4e2a\u5730\u5230\u5927\u91cc\u8bf4\u5c31\u53bb\u5b50\u5f97\u4e5f\u548c\u90a3\u8981\u4e0b" +
    					  "\u770b\u5929\u65f6\u8fc7\u51fa\u5c0f\u4e48\u8d77\u4f60\u90fd\u628a\u597d\u8fd8\u591a\u6ca1" +
    					  "\u4e3a\u53c8\u53ef\u5bb6\u5b66\u53ea\u4ee5\u4e3b\u4f1a\u6837\u5e74\u60f3\u751f\u540c\u8001" +
    					  "\u4e2d\u5341\u4ece\u81ea\u9762\u524d\u5934\u9053\u5b83\u540e\u7136\u8d70\u5f88\u50cf\u89c1" +
    					  "\u4e24\u7528\u5979\u56fd\u52a8\u8fdb\u6210\u56de\u4ec0\u8fb9\u4f5c\u5bf9\u5f00\u800c\u5df1" +
    					  "\u4e9b\u73b0\u5c71\u6c11\u5019\u7ecf\u53d1\u5de5\u5411\u4e8b\u547d\u7ed9\u957f\u6c34\u51e0" +
    					  "\u4e49\u4e09\u58f0\u4e8e\u9ad8\u624b\u77e5\u7406\u773c\u5fd7\u70b9\u5fc3\u6218\u4e8c\u95ee" +
    					  "\u4f46\u8eab\u65b9\u5b9e\u5403\u505a\u53eb\u5f53\u4f4f\u542c\u9769\u6253\u5462\u771f\u5168" +
    					  "\u624d\u56db\u5df2\u6240\u654c\u4e4b\u6700\u5149\u4ea7\u60c5\u8def\u5206\u603b\u6761\u767d" +
    					  "\u8bdd\u4e1c\u5e2d\u6b21\u4eb2\u5982\u88ab\u82b1\u53e3\u653e\u513f\u5e38\u6c14\u4e94\u7b2c" +
    					  "\u4f7f\u5199\u519b\u5427\u6587\u8fd0\u518d\u679c\u600e\u5b9a\u8bb8\u5feb\u660e\u884c\u56e0" +
    					  "\u522b\u98de\u5916\u6811\u7269\u6d3b\u90e8\u95e8\u65e0\u5f80\u8239\u671b\u65b0\u5e26\u961f" +
    					  "\u5148\u529b\u5b8c\u5374\u7ad9\u4ee3\u5458\u673a\u66f4\u4e5d\u60a8\u6bcf\u98ce\u7ea7\u8ddf" +
    					  "\u7b11\u554a\u5b69\u4e07\u5c11\u76f4\u610f\u591c\u6bd4\u9636\u8fde\u8f66\u91cd\u4fbf\u6597" +
    					  "\u9a6c\u54ea\u5316\u592a\u6307\u53d8\u793e\u4f3c\u58eb\u8005\u5e72\u77f3\u6ee1\u65e5\u51b3" +
    					  "\u767e\u539f\u62ff\u7fa4\u7a76\u5404\u516d\u672c\u601d\u89e3\u7acb\u6cb3\u6751\u516b\u96be" +
    					  "\u65e9\u8bba\u5417\u6839\u5171\u8ba9\u76f8\u7814\u4eca\u5176\u4e66\u5750\u63a5\u5e94\u5173" +
    					  "\u4fe1\u89c9\u6b65\u53cd\u5904\u8bb0\u5c06\u5343\u627e\u4e89\u9886\u6216\u5e08\u7ed3\u5757" +
    					  "\u8dd1\u8c01\u8349\u8d8a\u5b57\u52a0\u811a\u7d27\u7231\u7b49\u4e60\u9635\u6015\u6708\u9752" +
    					  "\u534a\u706b\u6cd5\u9898\u5efa\u8d76\u4f4d\u5531\u6d77\u4e03\u5973\u4efb\u4ef6\u611f\u51c6" +
    					  "\u5f20\u56e2\u5c4b\u79bb\u8272\u8138\u7247\u79d1\u5012\u775b\u5229\u4e16\u521a\u4e14\u7531" +
    					  "\u9001\u5207\u661f\u5bfc\u665a\u8868\u591f\u6574\u8ba4\u54cd\u96ea\u6d41\u672a\u573a\u8be5" +
    					  "\u5e76\u5e95\u6df1\u523b\u5e73\u4f1f\u5fd9\u63d0\u786e\u8fd1\u4eae\u8f7b\u8bb2\u519c\u53e4" +
    					  "\u9ed1\u544a\u754c\u62c9\u540d\u5440\u571f\u6e05\u9633\u7167\u529e\u53f2\u6539\u5386\u8f6c" +
    					  "\u753b\u9020\u5634\u6b64\u6cbb\u5317\u5fc5\u670d\u96e8\u7a7f\u5185\u8bc6\u9a8c\u4f20\u4e1a" +
    					  "\u83dc\u722c\u7761\u5174\u5f62\u91cf\u54b1\u89c2\u82e6\u4f53\u4f17\u901a\u51b2\u5408\u7834" +
    					  "\u53cb\u5ea6\u672f\u996d\u516c\u65c1\u623f\u6781\u5357\u67aa\u8bfb\u6c99\u5c81\u7ebf\u91ce" +
    					  "\u575a\u7a7a\u6536\u7b97\u81f3\u653f\u57ce\u52b3\u843d\u94b1\u7279\u56f4\u5f1f\u80dc\u6559" +
    					  "\u70ed\u5c55\u5305\u6b4c\u7c7b\u6e10\u5f3a\u6570\u4e61\u547c\u6027\u97f3\u7b54\u54e5\u9645" +
    					  "\u65e7\u795e\u5ea7\u7ae0\u5e2e\u5566\u53d7\u7cfb\u4ee4\u8df3\u975e\u4f55\u725b\u53d6\u5165" +
    					  "\u5cb8\u6562\u6389\u5ffd\u79cd\u88c5\u9876\u6025\u6797\u505c\u606f\u53e5\u533a\u8863\u822c" +
    					  "\u62a5\u53f6\u538b\u6162\u53d4\u80cc\u7ec6";
    		//图片背景增加噪点
    		g.setColor(this.getRandColor(random, 160, 200));
    		g.setFont(new Font("Times New Roman", Font.PLAIN, 14)); 
    		for(int i=0; i<6; i++){
    			g.drawString("*********************************************", 0, 5*(i+2));
    		}
    		//设定验证码汉字的备选字体{"宋体", "新宋体", "黑体", "楷体", "隶书"}
    		String[] fontTypes = {"\u5b8b\u4f53", "\u65b0\u5b8b\u4f53", "\u9ed1\u4f53", "\u6977\u4f53", "\u96b6\u4e66"};
    		//取随机产生的4个汉字作为验证码
    		for(int i=0; i<4; i++){
    			int start = random.nextInt(base.length());
    			String rand = base.substring(start, start+1);
    			sRand += rand;
    			g.setColor(this.getRandColor(random, 10, 150));
    			g.setFont(new Font(fontTypes[random.nextInt(fontTypes.length)], Font.BOLD, 18+random.nextInt(4)));
    			//将此汉字画到图片上
    			g.drawString(rand, 24*i+10+random.nextInt(8), 24);
    		}
    	}

    	//将验证码存入SESSION
    	session.setAttribute("rand", sRand);
    	//图像生效
    	g.dispose();
    	
    	final Graphics2D g2d = image.createGraphics();//获得一个图形类
    	g2d.drawOval(100, 100, 100, 100);//绘制图形
    	OutputStream out = null;
    	try {
    		out = response.getOutputStream();//打开输出流
			ImageIO.write(image, "JPEG", out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
     /**
	 * 生成给定范围内的随机颜色
	 */
    public Color getRandColor(Random random,int fc, int bc){
		if(fc>255) fc = 255;
		if(bc>255) bc = 255;
		int r = fc + random.nextInt(bc-fc);
		int g = fc + random.nextInt(bc-fc);
		int b = fc + random.nextInt(bc-fc);
		return new Color(r, g, b);
	}

}