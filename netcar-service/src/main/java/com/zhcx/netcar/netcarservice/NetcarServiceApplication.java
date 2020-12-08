package com.zhcx.netcar.netcarservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;

@ImportResource(locations={"classpath:Elastic-jobs.xml"})
@SpringBootApplication
public class NetcarServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetcarServiceApplication.class, args);
    }

}
