package com.zhcx.authorization.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class IotConfig {
	@Value("${iot.username}")
	private String username;
	@Value("${iot.password}")
	private String password;
	@Value("${iot.keys}")
	private String keys;
	@Value("${iot.agg}")
	private String agg;
	@Value("${iot.interval}")
	private String interval;
	@Value("${iot.limit}")
	private String limit;
	@Value("${iot.url}")
	private String url;
	@Value("${iot.tts.url}")
	private String ttsUrl;
	@Value("${iot.ins.url}")
	private String insUrl;
}
