package com.zhss.dfs.backupnode.server;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 从namenode同步editslog的组件
 * @author zhonghuashishan
 *
 */
public class EditsLogFetcher extends Thread {

	public static final Integer BACKUP_NODE_FETCH_SIZE = 10;
	
	private BackupNode backupNode;
	private NameNodeRpcClient namenode;
	private FSNamesystem namesystem;
	
	public EditsLogFetcher(
			BackupNode backupNode, 
			FSNamesystem namesystem, 
			NameNodeRpcClient namenode) {
		this.backupNode = backupNode;
		this.namenode = namenode;
		this.namesystem = namesystem;
	}
	
	@Override
	public void run() {
		System.out.println("Editslog抓取线程已经启动......");    
		
		while(backupNode.isRunning()) {  
			try {
				if(!namesystem.isFinishedRecover()) {
					System.out.println("当前还没完成元数据恢复，不进行editlog同步......");  
					Thread.sleep(1000); 
					continue;
				}
				
				long syncedTxid = namesystem.getSyncedTxid();
				JSONArray editsLogs = namenode.fetchEditsLog(syncedTxid);  
				
				if(editsLogs.size() == 0) {
					Thread.sleep(1000); 
					continue;
				}
				
				if(editsLogs.size() < BACKUP_NODE_FETCH_SIZE) {
					Thread.sleep(1000); 
				}
				
				for(int i = 0; i < editsLogs.size(); i++) {
					JSONObject editsLog = editsLogs.getJSONObject(i);
					System.out.println("拉取到一条editslog：" + editsLog.toJSONString());  
					String op = editsLog.getString("OP");
					
					if(op.equals("MKDIR")) {
						String path = editsLog.getString("PATH"); 
						try {
							namesystem.mkdir(editsLog.getLongValue("txid"), path);
						} catch (Exception e) {  
							e.printStackTrace();
						}
					} else if(op.equals("CREATE")) {
						String filename = editsLog.getString("PATH"); 
						try {
							namesystem.create(editsLog.getLongValue("txid"), filename);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				namenode.setIsNamenodeRunning(true); 
			} catch (Exception e) {
				namenode.setIsNamenodeRunning(false);  
			}
		}
	}
	
}
