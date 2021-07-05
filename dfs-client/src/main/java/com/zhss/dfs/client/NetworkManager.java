package com.zhss.dfs.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 网络连接池子
 * @author zhonghuashishan
 *
 */
public class NetworkManager {
	
	// 正在连接中
	public static final Integer CONNECTING = 1;
	// 已经建立连接
	public static final Integer CONNECTED = 2;
	// 断开连接
	public static final Integer DISCONNECTED = 3;
	// 响应状态：成功
	public static final Integer RESPONSE_SUCCESS = 1;
	// 响应状态：失败
	public static final Integer RESPONSE_FAILURE = 2;
	// 网络poll操作的超时时间
	public static final Long POLL_TIMEOUT = 500L; 
	// 请求超时检测间隔
	public static final long REQUEST_TIMEOUT_CHECK_INTERVAL = 1000;
	// 请求超时时长
	public static final long REQUEST_TIMEOUT = 30 * 1000;
	
	// 多路复用Selector
	private Selector selector;
	// 所有的连接
	private Map<String, SelectionKey> connections;
	// 每个数据节点的连接状态
	private Map<String, Integer> connectState;
	// 等待建立连接的机器
	private ConcurrentLinkedQueue<Host> waitingConnectHosts;
	// 排队等待发送的网络请求
	private Map<String, ConcurrentLinkedQueue<NetworkRequest>> waitingRequests;
	// 马上准备要发送的网络请求
	private Map<String, NetworkRequest> toSendRequests;
	// 已经完成请求的响应
	private Map<String, NetworkResponse> finishedResponses;
	// 还没读取完毕的响应
	private Map<String, NetworkResponse> unfinishedResponses;
	
	public NetworkManager() {
		try {
			this.selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.connections = new ConcurrentHashMap<String, SelectionKey>();
		this.connectState = new ConcurrentHashMap<String, Integer>();
		this.waitingConnectHosts = new ConcurrentLinkedQueue<Host>();
		this.waitingRequests = new ConcurrentHashMap<String, ConcurrentLinkedQueue<NetworkRequest>>();
		this.toSendRequests = new ConcurrentHashMap<String, NetworkRequest>();
		this.finishedResponses = new ConcurrentHashMap<String, NetworkResponse>();
		this.unfinishedResponses = new ConcurrentHashMap<String, NetworkResponse>();
		
		new NetworkPollThread().start();
		new RequestTimeoutCheckThread().start();
	}
	
	/**
	 * 尝试连接到数据节点的端口上去
	 * @param hostname
	 * @param nioPort
	 * @return
	 */
	public Boolean maybeConnect(String hostname, Integer nioPort) {
		synchronized(this) {
			if(!connectState.containsKey(hostname) || 
					connectState.get(hostname).equals(DISCONNECTED)) {
				connectState.put(hostname, CONNECTING);  
				waitingConnectHosts.offer(new Host(hostname, nioPort)); 
			}
			
			while(connectState.get(hostname).equals(CONNECTING)) {
				try {
					wait(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(connectState.get(hostname).equals(DISCONNECTED)) {
				return false;
			}
			
			return true;
		}
	}
	
	/**
	 * 发送网络请求
	 * @param request
	 */
	public void sendRequest(NetworkRequest request) {
		ConcurrentLinkedQueue<NetworkRequest> requestQueue = 
				waitingRequests.get(request.getHostname());
		requestQueue.offer(request);
	}
	
	/**
	 * 等待指定请求的响应 
	 * @param requestId
	 * @return
	 */
	public NetworkResponse waitResponse(String requestId) throws Exception {
		NetworkResponse response = null;
		
		while((response = finishedResponses.get(requestId)) == null) {
			Thread.sleep(100); 
		}
		
		toSendRequests.remove(response.getHostname());
		finishedResponses.remove(requestId);
		
		return response;
	}
	
	// 网络连接的核心线程
	class NetworkPollThread extends Thread {
		
		@Override
		public void run() {
			while(true) {
				tryConnect();
				prepareRequests();
				poll();
			}
		}
		
		/**
		 * 尝试把排队中的机器发起连接的请求
		 */
		private void tryConnect() {
			Host host = null;
			SocketChannel channel = null;
			
			while((host = waitingConnectHosts.poll()) != null) {
				try {
					channel = SocketChannel.open();  
					channel.configureBlocking(false);  
					channel.connect(new InetSocketAddress(host.getHostname(), host.getNioPort())); 
					channel.register(selector, SelectionKey.OP_CONNECT);  
				} catch (Exception e) {
					e.printStackTrace();  
					connectState.put(host.getHostname(), DISCONNECTED);
				}
			}
		}
		
		/**
		 * 准备好要发送的请求
		 */
		private void prepareRequests() {
			for(String hostname : waitingRequests.keySet()) {
				// 看一下这台机器当前是否还没有请求马上就要发送出去了 
				ConcurrentLinkedQueue<NetworkRequest> requestQueue = 
						waitingRequests.get(hostname);
				if(!requestQueue.isEmpty() && !toSendRequests.containsKey(hostname)) {
					// 对这台机器获取一个派对的请求出来
					NetworkRequest request = requestQueue.poll();
					// 将这个请求暂存起来，接下来 就可以等待发送出去
					toSendRequests.put(hostname, request);
					
					// 让这台机器对应的连接关注的事件为OP_WRITE
					SelectionKey key = connections.get(hostname);
					key.interestOps(SelectionKey.OP_WRITE);
				}
			}
		}
		
		/**
		 * 尝试完成网络连接、请求发送、响应读取
		 */
		private void poll() {
			SocketChannel channel = null;
			
			try {
				int selectedKeys = selector.select(500);   
				if(selectedKeys <= 0) {
					return;
				}
				
				Iterator<SelectionKey> keysIterator = selector.selectedKeys().iterator();  
				while(keysIterator.hasNext()){  
					SelectionKey key = (SelectionKey) keysIterator.next();  
					keysIterator.remove();  
					
					channel = (SocketChannel) key.channel();
					
					// 如果是网络连接操作
					if(key.isConnectable()){  
						finishConnect(key, channel);  
					} else if(key.isWritable()) {
						sendRequest(key, channel);
					} else if(key.isReadable()) {
						readResponse(key, channel);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				
				if(channel != null) {
					try {
						channel.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}

		/**
		 * 完成跟机器的连接
		 * @param key
		 */
		private void finishConnect(SelectionKey key, SocketChannel channel) {
			InetSocketAddress remoteAddress = null;
			
			try {
				remoteAddress = (InetSocketAddress)channel.getRemoteAddress();
				
				if(channel.isConnectionPending()){  
					while(!channel.finishConnect()) {
						Thread.sleep(100); 
					}
				}   
				
				System.out.println("完成与服务器【" + remoteAddress.getHostName() + "】的连接的建立......"); 
				
				waitingRequests.put(remoteAddress.getHostName(), 
						new ConcurrentLinkedQueue<NetworkRequest>());
				connections.put(remoteAddress.getHostName(), key);
				connectState.put(remoteAddress.getHostName(), CONNECTED);
			} catch (Exception e) {
				e.printStackTrace();
				
				if(remoteAddress != null) {
					connectState.put(remoteAddress.getHostName(), DISCONNECTED);
				}
			}
		}

		/**
		 * 发送请求
		 * @param key
		 * @param channel
		 */
		private void sendRequest(SelectionKey key, SocketChannel channel) {
			InetSocketAddress remoteAddress = null;
			
			try {
				remoteAddress = (InetSocketAddress)channel.getRemoteAddress();
				String hostname = remoteAddress.getHostName();
				
				// 获取要发送到这台机器的请求的数据
				NetworkRequest request = toSendRequests.get(hostname);
				ByteBuffer buffer = request.getBuffer();
			
				// 将请求发送到对方机器上去
				channel.write(buffer);
				while(buffer.hasRemaining()) {
					channel.write(buffer);
				}
				
				System.out.println("本次向【" + hostname + "】机器的请求发送完毕......");  
				
				request.setSendTime(System.currentTimeMillis()); 
				
				key.interestOps(SelectionKey.OP_READ);
			} catch (Exception e) {
				e.printStackTrace();
				
				key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);

				if(remoteAddress != null) {
					String hostname = remoteAddress.getHostName();
					
					NetworkRequest request = toSendRequests.get(hostname);
					
					NetworkResponse response = new NetworkResponse();
					response.setHostname(hostname); 
					response.setRequestId(request.getId());  
					response.setIp(request.getIp()); 
					response.setError(true); 
					response.setFinished(true); 
					
					if(request.needResponse()) {
						finishedResponses.put(request.getId(), response);
					} else {
						if(request.getCallback() != null) {
							request.getCallback().process(response);  
						}
						toSendRequests.remove(hostname);	
					}
				}
			}
		}
		
		/**
		 * 读取响应信息
		 * @param key
		 * @param channel
		 */
		private void readResponse(SelectionKey key, SocketChannel channel) throws Exception {
			InetSocketAddress remoteAddress = (InetSocketAddress)channel.getRemoteAddress();
			String hostname = remoteAddress.getHostName();
			
			NetworkRequest request = toSendRequests.get(hostname);
			NetworkResponse response = null;
		
			if(request.getRequestType().equals(NetworkRequest.REQUEST_SEND_FILE)) {
				response = getSendFileResponse(request.getId(), hostname, channel); 
			} else if(request.getRequestType().equals(NetworkRequest.REQUEST_READ_FILE)) {
				response = getReadFileResponse(request.getId(), hostname, channel);
			}
			
			if(!response.finished()) {
				return;
			}
			
			key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
			
			if(request.needResponse()) {
				finishedResponses.put(request.getId(), response);
			} else {
				if(request.getCallback() != null) {
					request.getCallback().process(response);  
				}
				toSendRequests.remove(hostname);
			}
		}
			
		/**
		 * 读取下载的文件响应
		 * @param id
		 * @param hostname
		 * @param channel
		 * @return
		 */
		private NetworkResponse getReadFileResponse(String requestId, 
				String hostname, SocketChannel channel) throws Exception {
			NetworkResponse response = null;
			
			if(!unfinishedResponses.containsKey(hostname)) {
				response = new NetworkResponse();
				response.setRequestId(requestId); 
				response.setHostname(hostname); 
				response.setError(false); 
				response.setFinished(false); 
			} else {
				response = unfinishedResponses.get(hostname);
			}
			
			Long fileLength = null;
			
			if(response.getBuffer() == null) { 
				ByteBuffer lengthBuffer = null;
				
				if(response.getLengthBuffer() == null) {
					lengthBuffer = ByteBuffer.allocate(NetworkRequest.FILE_LENGTH);
					response.setLengthBuffer(lengthBuffer);   
				} else {
					lengthBuffer = response.getLengthBuffer();
				}
				
				channel.read(lengthBuffer); 
				
				if(!lengthBuffer.hasRemaining()) {
					lengthBuffer.rewind();
					fileLength = lengthBuffer.getLong();
				} else {
					unfinishedResponses.put(hostname, response);
				}
			}
			
			if(fileLength != null || response.getBuffer() != null) {
				ByteBuffer buffer = null;
				
				if(response.getBuffer() == null) {
					buffer = ByteBuffer.allocate(Integer.valueOf(
							String.valueOf(fileLength)));  
					response.setBuffer(buffer);
				} else {
					buffer = response.getBuffer();
				}
				
				channel.read(buffer);
				
				if(!buffer.hasRemaining()) {
					buffer.rewind();
					response.setFinished(true); 
					unfinishedResponses.remove(hostname);
				} else {
					unfinishedResponses.put(hostname, response);
				}
			} 
			
			return response;
		}

		/**
		 * 读取上传文件的响应
		 * @param channel
		 * @return
		 * @throws Exception
		 */
		private NetworkResponse getSendFileResponse(String requestId, 
				String hostname, SocketChannel channel) throws Exception {
			ByteBuffer buffer = ByteBuffer.allocate(1024);                         
			channel.read(buffer); 
			buffer.flip();
			
			NetworkResponse response = new NetworkResponse();
			response.setRequestId(requestId); 
			response.setHostname(hostname); 
			response.setBuffer(buffer); 
			response.setError(false); 
			response.setFinished(true); 
			
			return response;
		}
		
	}
	
	/**
	 * 超时检测线程
	 * @author zhonghuashishan
	 *
	 */
	class RequestTimeoutCheckThread extends Thread {
		
		@Override
		public void run() {
			while(true) {
				try {
					long now = System.currentTimeMillis();
					
					for(NetworkRequest request : toSendRequests.values()) {
						if(now - request.getSendTime() > REQUEST_TIMEOUT) {
							String hostname = request.getHostname();
							
							NetworkResponse response = new NetworkResponse();
							response.setHostname(hostname); 
							response.setIp(request.getIp()); 
							response.setRequestId(request.getId());  
							response.setError(true); 
							response.setFinished(true); 
							
							if(request.needResponse()) {
								finishedResponses.put(request.getId(), response);
							} else {
								if(request.getCallback() != null) {
									request.getCallback().process(response); 
								}
								toSendRequests.remove(hostname);
							}
						}
					}
					
					Thread.sleep(REQUEST_TIMEOUT_CHECK_INTERVAL);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
