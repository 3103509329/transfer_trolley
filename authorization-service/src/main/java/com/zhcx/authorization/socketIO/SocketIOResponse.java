package com.zhcx.authorization.socketIO;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.UUID;

public abstract class SocketIOResponse extends SocketIOClientCache {
	private static final Logger logger = LoggerFactory.getLogger(SocketIOResponse.class);

	@Autowired
	private SocketIOServer server;

	public final static String VEDIOEVENT = "onload_vedio";
	
	public final static String ALARMEVENT = "messge_alarm";
	//告警监控常量
	public final static String ALARMMONITOR = "monitoring_alarm";
	
	//告警监控推送变量
	public final static String ALARMMONITOR_PUSH = "push_monitoring_alarm";

    public void sendEvent(SocketIOClient client, MessgeBean bean) {
    	logger.debug("推送消息");
        client.sendEvent("OnMSG", bean);
    }

	/**
	 * 推送数据到客户端
	 *
	 * @param event
	 *            事件类型
	 * @param推送数据
	 * @param clientIds
	 *            推送客户端
	 */
	protected void pushData(String event, MessgeBean bean, Set<String> clientIds,String namespace) {
		if (null == clientIds || clientIds.size() == 0) {
			return;
		}
		for (String clientId : clientIds) {
			UUID uuid = UUID.fromString(clientId);
			SocketIOClient sendClient = server.getNamespace(namespace).getClient(uuid);
			if (null != sendClient) {
				sendClient.sendEvent(event, bean);
			} else {
				logger.info("无法通过clientId:" + clientId + "找到客户端");
			}
		}
	}
}
