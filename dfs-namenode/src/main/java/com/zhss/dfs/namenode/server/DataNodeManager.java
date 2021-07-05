package com.zhss.dfs.namenode.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这个组件，就是负责管理集群里的所有的datanode的
 * @author zhonghuashishan
 *
 */
public class DataNodeManager {

	/**
	 * 集群中所有的datanode
	 */
	private Map<String, DataNodeInfo> datanodes = 
			new ConcurrentHashMap<String, DataNodeInfo>();
	private FSNamesystem namesystem;
	
	public DataNodeManager() {
		new DataNodeAliveMonitor().start();
	}
	
	public void setNamesystem(FSNamesystem namesystem) {
		this.namesystem = namesystem;
	}
	
	/**
	 * datanode进行注册
	 * @param ip 
	 * @param hostname
	 */
	public Boolean register(String ip, String hostname, int nioPort) {
		if(datanodes.containsKey(ip + "-" + hostname)) {
			System.out.println("注册失败，当前已经存在这个DataNode了......");  
			return false;
		}
		
		DataNodeInfo datanode = new DataNodeInfo(ip, hostname, nioPort);  
		datanodes.put(ip + "-" + hostname, datanode);  
		System.out.println("DataNode注册：ip=" + ip + ",hostname=" + hostname + ", nioPort=" + nioPort);  
		
		return true;
	}
	
	/**
	 * datanode进行心跳
	 * @param ip
	 * @param hostname
	 * @return
	 */
	public Boolean heartbeat(String ip, String hostname) {
		DataNodeInfo datanode = datanodes.get(ip + "-" + hostname);
		
		if(datanode == null) {
			// 这个时候就需要指示DataNode重新注册以及全量上报
			System.out.println("心跳失败，需要重新注册.......");    
			return false;
		}
		
		datanode.setLatestHeartbeatTime(System.currentTimeMillis());  
		System.out.println("DataNode发送心跳：ip=" + ip + ",hostname=" + hostname);  
		return true;
	}
	
	/**
	 * 分配双副本对应的数据节点
	 * @param fileSize
	 * @return
	 */
	public List<DataNodeInfo> allocateDataNodes(long fileSize) {
		synchronized(this) {
			// 取出来所有的datanode，并且按照已经存储的数据大小来排序
			List<DataNodeInfo> datanodeList = new ArrayList<DataNodeInfo>();
			for(DataNodeInfo datanode : datanodes.values()) {
				datanodeList.add(datanode);
			}
			Collections.sort(datanodeList); 
			
			// 选择存储数据最少的头两个datanode出来
			List<DataNodeInfo> selectedDatanodes = new ArrayList<DataNodeInfo>();
			if(datanodeList.size() >= 2) {
				selectedDatanodes.add(datanodeList.get(0)); 
				selectedDatanodes.add(datanodeList.get(1)); 
				
				// 你可以做成这里是副本数量可以动态配置的，但是我这里的话呢给写死了，就是双副本
				
				// 默认认为：要上传的文件会被放到那两个datanode上去
				// 此时就应该更新那两个datanode存储数据的大小，加上上传文件的大小
				// 你只有这样做了，后面别人再次要过来上传文件的时候，就可以基于最新的存储情况来进行排序了
				datanodeList.get(0).addStoredDataSize(fileSize);
				datanodeList.get(1).addStoredDataSize(fileSize);
			}
			
			return selectedDatanodes;
		}
	}
	
	/**
	 * 分配双副本对应的数据节点
	 * @param fileSize
	 * @return
	 */
	public DataNodeInfo reallocateDataNode(long fileSize, String excludedDataNodeId) {   
		synchronized(this) {
			// 先得把排除掉的那个数据节点的存储的数据量减少文件的大小
			DataNodeInfo excludedDataNode = datanodes.get(excludedDataNodeId);
			excludedDataNode.addStoredDataSize(-fileSize);
			
			// 取出来所有的datanode，并且按照已经存储的数据大小来排序
			List<DataNodeInfo> datanodeList = new ArrayList<DataNodeInfo>();
			for(DataNodeInfo datanode : datanodes.values()) {
				if(!excludedDataNode.equals(datanode)) {
					datanodeList.add(datanode);
				}
			}
			Collections.sort(datanodeList); 
			
			// 选择存储数据最少的头两个datanode出来
			DataNodeInfo selectedDatanode = null;
			if(datanodeList.size() >= 1) {
				selectedDatanode = datanodeList.get(0); 
				datanodeList.get(0).addStoredDataSize(fileSize);
			}
			
			return selectedDatanode;
		}
	}
	
	/**
	 * 设置一个DataNode的存储数据的大小
	 * @param ip
	 * @param hostname
	 * @param storedDataSize
	 */
	public void setStoredDataSize(String ip, String hostname, Long storedDataSize) {
		DataNodeInfo datanode = datanodes.get(ip + "-" + hostname);
		datanode.setStoredDataSize(storedDataSize); 
	}
	
	/**
	 * 获取DataNode信息
	 * @param ip
	 * @param hostname
	 * @return
	 */
	public DataNodeInfo getDatanode(String ip, String hostname) {
		return datanodes.get(ip + "-" + hostname);
	}
	
	/**
	 * 获取DataNode信息
	 * @param ip
	 * @param hostname
	 * @return
	 */
	public DataNodeInfo getDatanode(String id) {
		return datanodes.get(id);
	}
	
	/**
	 * 创建丢失副本的复制任务
	 * @param ip
	 * @param hostname
	 */
	public void createReplicateTask(DataNodeInfo deadDatanode) {
		synchronized(this) {
			List<String> files = namesystem.getFilesByDatanode(
					deadDatanode.getIp(), deadDatanode.getHostname());
			
			for(String file : files) {
				String filename = file.split("_")[0];
				Long fileLength = Long.valueOf(file.split("_")[1]);  
				// 获取这个复制任务的源头数据节点
				DataNodeInfo sourceDatanode = namesystem.getReplicateSource(filename, deadDatanode);
				// 复制任务的目标数据节点，第一，不能是已经死掉的节点 ；第二，不能是已经有这个副本的节点
				DataNodeInfo destDatanode = allocateReplicateDataNode(fileLength, sourceDatanode, deadDatanode);
				
				ReplicateTask replicateTask = new ReplicateTask(
						filename, fileLength, sourceDatanode, destDatanode);
				
				// 将复制任务放到目标数据节点的任务队列里去
				destDatanode.addReplicateTask(replicateTask); 
				System.out.println("为目标数据节点生成一个副本复制任务，" + replicateTask); 
			}
		}
	}
	
	/**
	 * 分配用来复制副本的数据节点
	 * @param fileSize
	 * @return
	 */
	public DataNodeInfo allocateReplicateDataNode(long fileSize, 
			DataNodeInfo sourceDatanode, DataNodeInfo deadDatanode) {
		synchronized(this) {
			List<DataNodeInfo> datanodeList = new ArrayList<DataNodeInfo>();
			for(DataNodeInfo datanode : datanodes.values()) {
				if(!datanode.equals(sourceDatanode) && 
						!datanode.equals(deadDatanode)) { 
					datanodeList.add(datanode);
				}
			}
			Collections.sort(datanodeList); 
			
			DataNodeInfo selectedDatanode = null;
			if(!datanodeList.isEmpty()) {
				selectedDatanode = datanodeList.get(0); 
				datanodeList.get(0).addStoredDataSize(fileSize);
			}
			
			return selectedDatanode;
		}
	}
	
	/**
	 * 为重平衡去创建副本复制的任务
	 */
	public void createRebalanceTasks() {
		synchronized(this) {
			// 计算集群节点存储数据的平均值
			long totalStoredDataSize = 0;
			for(DataNodeInfo datanode : datanodes.values()) {
				totalStoredDataSize += datanode.getStoredDataSize();
			}
			long averageStoredDataSize = totalStoredDataSize / datanodes.size();
			
			// 将集群中的节点区分为两类：迁出节点和迁入节点
			List<DataNodeInfo> sourceDatanodes = new ArrayList<DataNodeInfo>();
			List<DataNodeInfo> destDatanodes = new ArrayList<DataNodeInfo>();
			
			for(DataNodeInfo datanode : datanodes.values()) {
				if(datanode.getStoredDataSize() > averageStoredDataSize) {
					sourceDatanodes.add(datanode);
				}
				if(datanode.getStoredDataSize() < averageStoredDataSize) {
					destDatanodes.add(datanode);
				}
			}
			
			// 为迁入节点生成复制的任务，为迁出节点生成删除的任务
			// 在这里生成的删除任务统一放到24小时之后延迟调度执行，咱们可以实现一个延迟调度执行的线程
			List<RemoveReplicaTask> removeReplicaTasks = new ArrayList<RemoveReplicaTask>();
			
			for(DataNodeInfo sourceDatanode : sourceDatanodes) {
				long toRemoveDataSize = sourceDatanode.getStoredDataSize() - averageStoredDataSize;
				
				for(DataNodeInfo destDatanode : destDatanodes) {
					// 直接一次性放到一台机器就可以了
					if(destDatanode.getStoredDataSize() + toRemoveDataSize <= averageStoredDataSize) {
						createRebalanceTasks(sourceDatanode, destDatanode, 
								removeReplicaTasks, toRemoveDataSize); 
						break;
					}
					// 只能把部分数据放到这台机器上去
					else if(destDatanode.getStoredDataSize() < averageStoredDataSize) {
						long maxRemoveDataSize = averageStoredDataSize - destDatanode.getStoredDataSize();
						long removedDataSize = createRebalanceTasks(sourceDatanode, destDatanode, 
								removeReplicaTasks, maxRemoveDataSize);
						toRemoveDataSize -= removedDataSize;
					} 
				}
			}
			
			// 交给一个延迟线程去24小时之后执行删除副本的任务
			new DelayRemoveReplicaThread(removeReplicaTasks).start(); 
		} 
	}
	
	private long createRebalanceTasks(DataNodeInfo sourceDatanode, DataNodeInfo destDatanode, 
			List<RemoveReplicaTask> removeReplicaTasks, long maxRemoveDataSize) {
		List<String> files = namesystem.getFilesByDatanode(sourceDatanode.getIp(), 
				sourceDatanode.getHostname());
		
		// 遍历文件，不停的为每个文件生成一个复制的任务，直到准备迁移的文件的大小
		// 超过了待迁移总数据量的大小为止
		long removedDataSize = 0;
		
		for(String file : files) {
			String filename = file.split("_")[0];
			long fileLength = Long.valueOf(file.split("_")[1]);  
			
			if(removedDataSize + fileLength >= maxRemoveDataSize) {
				break;
			}
			
			// 为这个文件生成复制任务
			ReplicateTask replicateTask = new ReplicateTask(
					filename, fileLength, sourceDatanode, destDatanode);
			destDatanode.addReplicateTask(replicateTask); 
			destDatanode.addStoredDataSize(fileLength); 
			
			// 为这个文件生成删除任务
			sourceDatanode.addStoredDataSize(-fileLength); 
			namesystem.removeReplicaFromDataNode(sourceDatanode.getId(), file); 
			RemoveReplicaTask removeReplicaTask = new RemoveReplicaTask(
					filename, sourceDatanode);
			removeReplicaTasks.add(removeReplicaTask);  
			
			removedDataSize += fileLength;
		}
		
		return removedDataSize;
	}
	
	/**
	 * 延迟删除副本的线程
	 * @author zhonghuashishan
	 *
	 */
	class DelayRemoveReplicaThread extends Thread {
		
		private List<RemoveReplicaTask> removeReplicaTasks;
		
		public DelayRemoveReplicaThread(List<RemoveReplicaTask> removeReplicaTasks) {
			this.removeReplicaTasks = removeReplicaTasks;
		}
		
		@Override
		public void run() {
			long start = System.currentTimeMillis();
			
			while(true) {
				try {
					long now = System.currentTimeMillis();
					
					if(now - start > 24 * 60 * 60 * 1000) {
						for(RemoveReplicaTask removeReplicaTask : removeReplicaTasks) {
							removeReplicaTask.getDatanode().addRemoveReplicaTask(removeReplicaTask);  
						}
						break;
					}
					
					Thread.sleep(60 * 1000); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * datanode是否存活的监控线程
	 * @author zhonghuashishan
	 *
	 */
	class DataNodeAliveMonitor extends Thread {
		
		@Override
		public void run() {
			try {
				while(true) {
					List<DataNodeInfo> toRemoveDatanodes = new ArrayList<DataNodeInfo>();
					
					Iterator<DataNodeInfo> datanodesIterator = datanodes.values().iterator();
					DataNodeInfo datanode = null;
					while(datanodesIterator.hasNext()) {
						datanode = datanodesIterator.next();
						if(System.currentTimeMillis() - datanode.getLatestHeartbeatTime() > 1 * 60 * 1000) {
							toRemoveDatanodes.add(datanode);
						}
					}
					
					if(!toRemoveDatanodes.isEmpty()) {
						for(DataNodeInfo toRemoveDatanode : toRemoveDatanodes) {
							System.out.println("数据节点【" + toRemoveDatanode + "】宕机，需要 进行副本复制......");
							// 还有一个地方是在哪儿呢？就是这个叫做，我们还维护了一块是数据节点和副本的关系
							createReplicateTask(toRemoveDatanode);  
							// 来实现DataNode宕机之后的一些处理
							datanodes.remove(toRemoveDatanode.getId());
							System.out.println("从内存数据结构中删除掉这个数据节点，" + datanodes);
							// 删除掉这个数据结构
							namesystem.removeDeadDatanode(toRemoveDatanode);  
						}
					}
					
					Thread.sleep(30 * 1000); 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
