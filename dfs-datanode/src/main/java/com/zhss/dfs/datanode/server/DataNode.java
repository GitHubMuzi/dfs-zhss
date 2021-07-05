package com.zhss.dfs.datanode.server;

/**
 * DataNode启动类
 * @author zhonghuashishan
 *
 */
public class DataNode {

	/**
	 * 是否还在运行
	 */
	private volatile Boolean shouldRun;
	/**
	 * 负责跟一组NameNode通信的组件
	 */
	private NameNodeRpcClient namenodeRpcClient;
	/**
	 * 心跳管理组件
	 */
	private HeartbeatManager heartbeatManager;
	/**
	 * 磁盘存储管理组件
	 */
	private StorageManager storageManager;
	/**
	 * 复制任务管理组件
	 */
	private ReplicateManager replicateManager;
	
	/**
	 * 初始化DataNode
	 */
	public DataNode() throws Exception {
		this.shouldRun = true;
		this.namenodeRpcClient = new NameNodeRpcClient();
		Boolean registerResult = this.namenodeRpcClient.register();
		
		// 如果注册成功了才会执行全量的上报
		this.storageManager = new StorageManager();
		
		if(registerResult) {
			StorageInfo storageInfo = this.storageManager.getStorageInfo();
			this.namenodeRpcClient.reportCompleteStorageInfo(storageInfo);     
		} else {
			System.out.println("不需要全量上报存储信息......");  
		}
		
		this.replicateManager = new ReplicateManager(this.namenodeRpcClient);
		
		this.heartbeatManager = new HeartbeatManager(
				this.namenodeRpcClient, this.storageManager, this.replicateManager);
		this.heartbeatManager.start();
		
		NioServer nioServer = new NioServer(namenodeRpcClient);
		nioServer.init();
		nioServer.start(); 
	}
	
	/**
	 * 运行DataNode
	 */
	private void start() {
		try {
			while(shouldRun) {
				Thread.sleep(1000);  
			}   
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(DataNodeConfig.DATA_DIR); 
		DataNode datanode = new DataNode();
		datanode.start();
	}
	
}
