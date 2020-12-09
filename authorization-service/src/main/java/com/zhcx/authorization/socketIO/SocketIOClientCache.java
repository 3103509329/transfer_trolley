package com.zhcx.authorization.socketIO;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SocketIOClientCache {
    //String：EventType类型
	protected Map<String,SocketIOClient> clients=new ConcurrentHashMap<String,SocketIOClient>();

    protected static Map<String, Map<String, Set<String>>> socketClients = Maps.newConcurrentMap();

    //用户发送消息添加
    public void addClient(SocketIOClient client,MessgeBean msgBean){
        clients.put(msgBean.getFrom(),client);
    }

	/**
	 * 缓存要拉取数据的客户端
	 *
	 * @param event
	 * @param dataId
	 * @param clientId
	 */
	protected void putClient(String event, String dataId, SocketIOClient client) {
		String clientId = client.getSessionId().toString();
		Map<String, Set<String>> cache = socketClients.get(event);
		// 如果没有具体数据
		if (null == dataId || dataId.length() == 0) {
			cache = Maps.newConcurrentMap();
		} else {
			// 如果事件对应的数据没有,就创建一个map存储数据
			if (null == cache || cache.size() == 0) {
				cache = Maps.newConcurrentMap();
				// 给每个数据id存放需要推送的客户端id集合
					Set<String> clientIds = Sets.newConcurrentHashSet();
					clientIds.add(clientId);
					cache.put(dataId, clientIds);
			} else {
				// 如果事件存在数据,就添加新的数据
					Set<String> clientIds = null;
					// 数据id存在,就把新的客户端id放入集合
					if (cache.containsKey(dataId)) {
						clientIds = cache.get(dataId);
						clientIds.add(clientId);
					} else {
						// 数据id不存在,就创建新的集合并放入客户端id
						clientIds = Sets.newConcurrentHashSet();
						clientIds.add(clientId);
						cache.put(dataId, clientIds);
					}
			}
		}
		socketClients.put(event, cache);
		clients.put(clientId, client);
	}

    //用户退出时删除客户端
   /* public void remove(MessgeBean msgBean) {
        clients.remove(msgBean.getFrom());
    }*/

	protected void removeClient(String clientId) {
		Set<Entry<String, Map<String, Set<String>>>> set = socketClients.entrySet();
		for (Entry<String, Map<String, Set<String>>> entry : set) {
			Map<String, Set<String>> cache = entry.getValue();
			Set<Entry<String, Set<String>>> set2 = cache.entrySet();
			for (Entry<String, Set<String>> entry2 : set2) {
				Set<String> clientIds = entry2.getValue();
				for (Iterator<String> iterator = clientIds.iterator(); iterator.hasNext();) {
					String id = iterator.next();
					if (clientId.equals(id)) {
						iterator.remove();
						break;
					}
				}
			}
		}

	}

    //获取所有
    /*public  SocketIOClient getClient(String to) {
        return clients.get(to);
    }*/

	/**
	 * 获取需要推送数据的客户端
	 *
	 * @param event
	 * @param dataId
	 * @return
	 */
	protected Set<String> getClient(String event, String dataId) {
		Set<String> clientIds = new HashSet<>();
		Map<String, Set<String>> cache = socketClients.get(event);
		if (cache != null) {
			Set<String> mapClientIds = cache.get(dataId);
			if(null != mapClientIds) {
				for (String clientId : mapClientIds) {
					clientIds.add(clientId);
				}
			}
		}
		return clientIds;
	}
}