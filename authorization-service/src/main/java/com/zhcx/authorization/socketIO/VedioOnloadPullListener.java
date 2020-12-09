package com.zhcx.authorization.socketIO;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.google.common.base.Objects;

@Service("vedioOnloadPullListener")
public class VedioOnloadPullListener extends SocketIOResponse{

	private static final Logger logger = LoggerFactory.getLogger(VedioOnloadPullListener.class);

	public final static String NAMESPACE = "/vedio";

/*	@Resource(name = "clientCache")
	private SocketIOClientCache clientCache;*/

	private final SocketIOServer server;

	@Autowired
	public VedioOnloadPullListener(SocketIOServer server) {
		this.server = server;
	}

	@OnConnect
	public void onConnect(SocketIOClient client) {
		logger.debug("建立连接");
	}

	@OnEvent(VEDIOEVENT)
	public void onSync(SocketIOClient client, MessgeBean bean) {
		logger.debug("收到消息-from: %s to:%s\n", bean.getFrom(), bean.getTo());
		putClient(VEDIOEVENT, bean.getContent().toString(), client);
		Set<String> clients = getClient(VEDIOEVENT,bean.getContent().toString());
		//SocketIOClient ioClients = clientCache.getClient(bean.getTo());

		/*if (ioClients == null) {
			logger.debug("你发送消息的用户不在线");
			return;
		}
		socketIOResponse.sendEvent(ioClients, bean);*/
		bean.setContent("hello word！");
		bean.setFrom("socket server!");
		pushData(bean.getType(), bean, clients,NAMESPACE);
	}

	@OnDisconnect
	public void onDisconnect(SocketIOClient client) {
		if (Objects.equal(this.server.getNamespace(NAMESPACE), client.getNamespace())) {
			// 移除客户端
			removeClient(client.getSessionId().toString());
		}
		logger.debug("关闭连接");
	}
}
