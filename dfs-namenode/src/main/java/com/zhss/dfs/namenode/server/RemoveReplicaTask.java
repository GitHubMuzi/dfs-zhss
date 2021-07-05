package com.zhss.dfs.namenode.server;

/**
 * 删除副本任务
 * @author zhonghuashishan
 *
 */
public class RemoveReplicaTask {

	private String filename;
	private DataNodeInfo datanode;
	
	public RemoveReplicaTask(String filename, DataNodeInfo datanode) {
		this.filename = filename;
		this.datanode = datanode;
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public DataNodeInfo getDatanode() {
		return datanode;
	}
	public void setDatanode(DataNodeInfo datanode) {
		this.datanode = datanode;
	}
	
}
