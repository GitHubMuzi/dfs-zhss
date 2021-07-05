package com.zhss.dfs.datanode.server;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 负责解析请求以及发送响应的线程
 * @author zhonghuashishan
 *
 */
public class NioProcessor extends Thread {
	
	/**
	 * 多路复用监听时的最大阻塞时间
	 */
	public static final Long POLL_BLOCK_MAX_TIME = 1000L;
	
	// 标识
	private Integer processorId;
	// 等待注册的网络连接的队列
	private ConcurrentLinkedQueue<SocketChannel> channelQueue = 
			new ConcurrentLinkedQueue<SocketChannel>();
	// 每个Processor私有的Selector多路复用器
	private Selector selector;
	// 没读取完的请求缓存在这里
	private Map<String, NetworkRequest> cachedRequests = new HashMap<String, NetworkRequest>();
	// 暂存起来的响应缓存在这里
	private Map<String, NetworkResponse> cachedResponses = new HashMap<String, NetworkResponse>();
	// 这个processor负责维护的所有客户端的SelectionKey
	private Map<String, SelectionKey> cachedKeys = new HashMap<String, SelectionKey>();
	
	public NioProcessor(Integer processorId) {
		try {
			this.processorId = processorId;
			this.selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Integer getProcessorId() {
		return processorId;
	}
	public void setProcessorId(Integer processorId) {
		this.processorId = processorId;
	}

	/**
	 * 给这个Processor线程分配一个网络连接
	 * @param channel
	 */
	public void addChannel(SocketChannel channel) {
		channelQueue.offer(channel);
		selector.wakeup();
	}
	
	/**
	 * 线程的核心主逻辑
	 */
	@Override
	public void run() {
		while(true) {
			try {
				// 注册排队等待的连接
				registerQueuedClients();
				// 处理排队中的响应
				cacheQueuedResponse();
				// 以限时阻塞的方式感知连接中的请求
				poll();
			} catch (Exception e) {
				e.printStackTrace();  
			}
		}
	}

	/**
	 * 将排队中的等待注册的连接注册到Selector上去
	 */
	private void registerQueuedClients() {
		SocketChannel channel = null;
		while((channel = channelQueue.poll()) != null) {
			try {
				channel.register(selector, SelectionKey.OP_READ);
			} catch (ClosedChannelException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 以多路复用的方式来监听各个连接的请求
	 */
	private void poll() {
		try {
			int keys = selector.select(POLL_BLOCK_MAX_TIME);
			if(keys > 0) {
				Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
				while(keyIterator.hasNext()) {
					try {
						SelectionKey key = keyIterator.next();
						keyIterator.remove();
						
						SocketChannel channel = (SocketChannel) key.channel();
						String client = channel.getRemoteAddress().toString();
						
						// 如果接受到了某个客户端的请求
						if(key.isReadable()) {
							System.out.println("准备读取请求：" + DataNodeConfig.DATA_DIR); 
							NetworkRequest request = null;
							if(cachedRequests.get(client) != null) {
								request = cachedRequests.get(client);
							} else {
								request = new NetworkRequest();
								request.setChannel(channel);
								request.setKey(key);
							}
							
							request.read();
							
							System.out.println("请求读取完毕之后：" + DataNodeConfig.DATA_DIR); 
							
							if(request.hasCompletedRead()) {
								// 此时就可以将一个请求分发到全局的请求队列里去了
								request.setProcessorId(processorId); 
								request.setClient(client);
								
								NetworkRequestQueue requestQueue = NetworkRequestQueue.get();
								requestQueue.offer(request);
								
								cachedKeys.put(client, key); // 当前这个key对应的客户端有请求进入队列了
								cachedRequests.remove(client);
								
								key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
								// 必须是得等到一个客户端的一个请求被处理完毕了以后，才会允许读取下一个请求
							} else {
								cachedRequests.put(client, request);
							}
						} else if(key.isWritable()) {
							NetworkResponse response = cachedResponses.get(client);
							channel.write(response.getBuffer());
							
							cachedResponses.remove(client);
							cachedKeys.remove(client);
							
							key.interestOps(SelectionKey.OP_READ);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 暂存排队中的响应
	 */
	private void cacheQueuedResponse() {
		NetworkResponseQueues responseQueues = NetworkResponseQueues.get();
		NetworkResponse response = null;
		
		while((response = responseQueues.poll(processorId)) != null) {
			String client = response.getClient();
			cachedResponses.put(client, response);
			cachedKeys.get(client).interestOps(SelectionKey.OP_WRITE);
		}
	}
	
}
