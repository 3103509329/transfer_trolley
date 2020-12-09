package com.zhcx.netcar.netcarservice.controller.mapvideo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/7/18 0018 15:25
 **/
@Component
public class ThirdPartyInitialize implements CommandLineRunner {

    @Autowired
    private ThirdPartyLogin thirdPartyLogin;


    /**
     * 第三方平台（北斗定位），在项目启动后立即执行一次
     * @param strings
     * @throws Exception
     */
    @Override
    public void run(String... strings) throws Exception {
        thirdPartyLogin.run();
    }
}
