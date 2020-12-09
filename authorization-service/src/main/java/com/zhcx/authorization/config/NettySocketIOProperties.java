package com.zhcx.authorization.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="zhcx.ws.server")
public class NettySocketIOProperties {

	private String host = "localhost";

	private int port = 9100;

	private int threadsNum = 4;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getThreadsNum() {
		return threadsNum;
	}

	public void setThreadsNum(int threadsNum) {
		this.threadsNum = threadsNum;
	}

}
