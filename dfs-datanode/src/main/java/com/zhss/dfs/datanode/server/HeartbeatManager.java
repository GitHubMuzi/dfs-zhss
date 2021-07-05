package com.zhss.dfs.datanode.server;

import java.io.File;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhss.dfs.namenode.rpc.model.HeartbeatResponse;

/**
 * 心跳管理组件
 * @author zhonghuashishan
 *
 */
public class HeartbeatManager {
	
	public static final Integer SUCCESS = 1;
	public static final Integer FAILURE = 2;
	public static final Integer COMMAND_REGISTER = 1;
	public static final Integer COMMAND_REPORT_COMPLETE_STORAGE_INFO = 2;
	public static final Integer COMMAND_REPLICATE = 3;
	public static final Integer COMMAND_REMOVE_REPLICA = 4;

	private NameNodeRpcClient namenodeRpcClient;
	private StorageManager storageManager;
	private ReplicateManager replicateManager;
	
	public HeartbeatManager(NameNodeRpcClient namenodeRpcClient, 
			StorageManager storageManager,
			ReplicateManager replicateManager) {
		this.namenodeRpcClient = namenodeRpcClient;
		this.storageManager = storageManager;
		this.replicateManager = replicateManager;
	}
	
	public void start() {
		new HeartbeatThread().start();
	}
	
	/**
	 * 负责心跳的线程
	 * @author zhonghuashishan
	 *
	 */
	class HeartbeatThread extends Thread {
		
		@Override
		public void run() {
			while(true) {
				try {
					// 通过RPC接口发送到NameNode他的注册接口上去
					HeartbeatResponse response = namenodeRpcClient.heartbeat();
					
					if(SUCCESS.equals(response.getStatus())) { 
						JSONArray commands = JSONArray.parseArray(response.getCommands());
						
						if(commands.size() > 0) {
							for(int i = 0; i < commands.size(); i++) {
								JSONObject command = commands.getJSONObject(i);
								Integer type = command.getInteger("type"); 
								JSONObject task = command.getJSONObject("content"); 
								
								if(type.equals(COMMAND_REPLICATE)) {
									replicateManager.addReplicateTask(task);
									System.out.println("接收副本复制任务，" + command); 
								} else if(type.equals(COMMAND_REMOVE_REPLICA)) {
									// 删除副本
									String filename = task.getString("filename"); 
									String absoluteFilename = FileUtils.getAbsoluteFilename(filename);
									File file = new File(absoluteFilename);
									if(file.exists()) {
										file.delete();
									}
								}
							}
						}
					}
					
					// 如果心跳失败了
					if(FAILURE.equals(response.getStatus())) { 
 						JSONArray commands = JSONArray.parseArray(response.getCommands());
						
						for(int i = 0; i < commands.size(); i++) {
							JSONObject command = commands.getJSONObject(i);
							Integer type = command.getInteger("type"); 
							
							// 如果是注册的命令
							if(type.equals(COMMAND_REGISTER)) {
								namenodeRpcClient.register();
							} 
							// 如果是全量上报的命令
							else if(type.equals(COMMAND_REPORT_COMPLETE_STORAGE_INFO)) {
								StorageInfo storageInfo = storageManager.getStorageInfo();
								namenodeRpcClient.reportCompleteStorageInfo(storageInfo);  
							}
						}
					}
				} catch (Exception e) { 
					System.out.println("当前NameNode不可用，心跳失败.......");  
				}
				
				try {
					Thread.sleep(30 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} // 每隔30秒发送一次心跳到NameNode上去  
			}
		}
		
	}
	
}
