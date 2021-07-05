package com.zhss.dfs.datanode.server;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 网络请求队列
 * @author zhonghuashishan
 *
 */
public class NetworkRequestQueue {

	private static volatile NetworkRequestQueue instance = null;
	
	public static NetworkRequestQueue get() {
		if(instance == null) {
			synchronized(NetworkRequestQueue.class) {
				if(instance == null) {
					instance = new NetworkRequestQueue();
				}
			}
		}
		return instance;
	}
	
	// 一个全局的请求队列
	private ConcurrentLinkedQueue<NetworkRequest> requestQueue = 
			new ConcurrentLinkedQueue<NetworkRequest>();
	
	public void offer(NetworkRequest request) {
		requestQueue.offer(request);
	}
	
	public NetworkRequest poll() {
		return requestQueue.poll();
	}
	
}
