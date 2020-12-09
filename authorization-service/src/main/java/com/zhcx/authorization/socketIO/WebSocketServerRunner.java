package com.zhcx.authorization.socketIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOServer;

@Component
public class WebSocketServerRunner implements CommandLineRunner {


	private final SocketIOServer server;

	@Autowired
	private VedioOnloadPullListener vedioOnloadPullListener;
	
	@Autowired
	private AlarmPullListener alarmPullListener;

	@Autowired
	public WebSocketServerRunner(SocketIOServer server) {
		this.server = server;
	}

	public void run(String... args) throws Exception {

        server.addNamespace(VedioOnloadPullListener.NAMESPACE).addListeners(vedioOnloadPullListener);
        server.addNamespace(AlarmPullListener.NAMESPACE).addListeners(alarmPullListener);
		server.start();
	}
}