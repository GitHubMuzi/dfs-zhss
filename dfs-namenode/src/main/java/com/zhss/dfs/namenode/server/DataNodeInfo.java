package com.zhss.dfs.namenode.server;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 用来描述datanode的信息
 * @author zhonghuashishan
 *
 */
public class DataNodeInfo implements Comparable<DataNodeInfo> {  

	/**
	 * ip地址
	 */
	private String ip;
	/**
	 * 机器名字
	 */
	private String hostname;
	/**
	 * NIO端口
	 */
	private int nioPort;
	/**
	 * 最近一次心跳的时间
	 */
	private long latestHeartbeatTime;
	/**
	 * 已经存储数据的大小
	 */
	private long storedDataSize;
	/**
	 * 副本复制任务队列
	 */
	private ConcurrentLinkedQueue<ReplicateTask> replicateTaskQueue = 
			new ConcurrentLinkedQueue<ReplicateTask>();
	/**
	 * 删除副本任务
	 */
	private ConcurrentLinkedQueue<RemoveReplicaTask> removeReplicaTaskQueue = 
			new ConcurrentLinkedQueue<RemoveReplicaTask>();
  	
	public DataNodeInfo(String ip, String hostname, int nioPort) {
		this.ip = ip;
		this.hostname = hostname;
		this.nioPort = nioPort;
		this.latestHeartbeatTime = System.currentTimeMillis();
		this.storedDataSize = 0L;
	}
	
	public void addReplicateTask(ReplicateTask replicateTask) {
		replicateTaskQueue.offer(replicateTask);
	}
	
	public ReplicateTask pollReplicateTask() {
		if(!replicateTaskQueue.isEmpty()) {
			return replicateTaskQueue.poll();
		}
		return null;
	}
	
	public void addRemoveReplicaTask(RemoveReplicaTask removeReplicaTask) {
		removeReplicaTaskQueue.offer(removeReplicaTask);
	}
	
	public RemoveReplicaTask pollRemoveReplicaTask() {
		if(!removeReplicaTaskQueue.isEmpty()) {
			return removeReplicaTaskQueue.poll();
		}
		return null;
	}
	
	public String getId() {
		return ip + "-" + hostname;
	} 
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public long getLatestHeartbeatTime() {
		return latestHeartbeatTime;
	}
	public void setLatestHeartbeatTime(long latestHeartbeatTime) {
		this.latestHeartbeatTime = latestHeartbeatTime;
	}
	public long getStoredDataSize() {
		return storedDataSize;
	}
	public void setStoredDataSize(long storedDataSize) {
		this.storedDataSize = storedDataSize;
	}
	
	public void addStoredDataSize(long storedDataSize) {
		this.storedDataSize += storedDataSize;
	}
	public int getNioPort() {
		return nioPort;
	}
	public void setNioPort(int nioPort) {
		this.nioPort = nioPort;
	}

	@Override
	public int compareTo(DataNodeInfo o) {
		if(this.storedDataSize - o.getStoredDataSize() > 0) {
			return 1;
		} else if(this.storedDataSize - o.getStoredDataSize() < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "DataNodeInfo [ip=" + ip + ", hostname=" + hostname + ", latestHeartbeatTime=" + latestHeartbeatTime
				+ ", storedDataSize=" + storedDataSize + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hostname == null) ? 0 : hostname.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DataNodeInfo other = (DataNodeInfo) obj;
		if (hostname == null) {
			if (other.hostname != null) {
				return false;
			}
		} else if (!hostname.equals(other.hostname)) {
			return false;
		}
		if (ip == null) {
			if (other.ip != null) {
				return false;
			}
		} else if (!ip.equals(other.ip)) {
			return false;
		}
		return true;
	}
	
}
