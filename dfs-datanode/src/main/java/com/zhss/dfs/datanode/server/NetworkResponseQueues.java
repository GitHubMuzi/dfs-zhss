package com.zhss.dfs.datanode.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 网络响应队列
 * @author zhonghuashishan
 *
 */
public class NetworkResponseQueues {

	private static volatile NetworkResponseQueues instance = null;
	
	public static NetworkResponseQueues get() {
		if(instance == null) {
			synchronized(NetworkResponseQueues.class) {
				if(instance == null) {
					instance = new NetworkResponseQueues();
				}
			}
		}
		return instance;
	}
	
	private Map<Integer, ConcurrentLinkedQueue<NetworkResponse>> responseQueues = 
			new HashMap<Integer, ConcurrentLinkedQueue<NetworkResponse>>();
	
	public void initResponseQueue(Integer processorId) {
		ConcurrentLinkedQueue<NetworkResponse> responseQueue = 
				new ConcurrentLinkedQueue<NetworkResponse>();
		responseQueues.put(processorId, responseQueue);
	}
	
	public void offer(Integer processorId, NetworkResponse response) {
		responseQueues.get(processorId).offer(response); 
	}
	
	public NetworkResponse poll(Integer processorId) {
		return responseQueues.get(processorId).poll();
	}
	
}
