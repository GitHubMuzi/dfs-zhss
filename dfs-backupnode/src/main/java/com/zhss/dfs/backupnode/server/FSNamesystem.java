package com.zhss.dfs.backupnode.server;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zhss.dfs.backupnode.server.FSDirectory.INode;

/**
 * 负责管理元数据的核心组件
 * @author zhonghuashishan
 *
 */
public class FSNamesystem {

	private FSDirectory directory;
	private long checkpointTime;
	private long syncedTxid;
	private String checkpointFile = "";
	private volatile boolean finishedRecover = false;
	
	public FSNamesystem() {
		directory = new FSDirectory();
		recoverNamespace();
	}
	
	/**
	 * 创建目录
	 * @param path 目录路径
	 * @return 是否成功
	 */
	public Boolean mkdir(long txid, String path) throws Exception {
		directory.mkdir(txid, path); // 第一步就是基于FSDirectory这个组件来真正去管理文件目录树
		return true;
	}
	
	/**
	 * 创建文件
	 * @param filename 文件名，包含所在的绝对路径，/products/img001.jpg
	 * @return
	 * @throws Exception
	 */
	public Boolean create(long txid, String filename) throws Exception {
		if(!directory.create(txid, filename)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 获取文件目录树的json
	 * @return
	 * @throws Exception
	 */
	public FSImage getFSImage() throws Exception {
		return directory.getFSImage();
	}
	
	/**
	 * 获取当前同步到的最大的txid
	 * @return
	 */
	public long getSyncedTxid() {
		return directory.getFSImage().getMaxTxid();
	}
	
	/**
	 * 恢复元数据
	 */
	public void recoverNamespace() {
		try {
			loadCheckpointInfo();
			loadFSImage();
			finishedRecover = true;
		} catch (Exception e) {
			e.printStackTrace();   
		}
	}
	
	/**
	 * 加载checkpoint txid
	 * @return
	 * @throws Exception
	 */
	private void loadCheckpointInfo() throws Exception {
		FileInputStream in = null;
		FileChannel channel = null;
		try {
			String path = "F:\\development\\backupnode\\checkpoint-info.meta";
			
			File file = new File(path);
			if(!file.exists()) {
				System.out.println("checkpoint info文件不存在，不进行恢复.......");  
				return;
			}
			
			in = new FileInputStream(path);
			channel = in.getChannel();
			
			ByteBuffer buffer = ByteBuffer.allocate(1024); // 这个参数是可以动态调节的
			// 每次你接受到一个fsimage文件的时候记录一下他的大小，持久化到磁盘上去
			// 每次重启就分配对应空间的大小就可以了
			int count = channel.read(buffer);
			
			buffer.flip();
			
			String checkpointInfo = new String(buffer.array(), 0, count);
			long checkpointTime = Long.valueOf(checkpointInfo.split("_")[0]); 
			long syncedTxid = Long.valueOf(checkpointInfo.split("_")[1]);  
			String fsimageFile = checkpointInfo.split("_")[2];
			
			System.out.println("恢复checkpoint time：" + checkpointTime + ", synced txid: " + syncedTxid + ", fsimage file: " + fsimageFile);    
			
			this.checkpointTime = checkpointTime;
			this.syncedTxid = syncedTxid;
			this.checkpointFile = fsimageFile;
			directory.setMaxTxid(syncedTxid);  
		} finally {
			if(in != null) {
				in.close();
			}
			if(channel != null) {
				channel.close();   
			}
		}
	}
	
	/**
	 * 加载fsimage文件到内存里来进行恢复
	 */
	private void loadFSImage() throws Exception {
		FileInputStream in = null;
		FileChannel channel = null;
		try {
			String path = "F:\\development\\backupnode\\fsimage-" + syncedTxid + ".meta";
			File file = new File(path);
			if(!file.exists()) {
				System.out.println("fsimage文件当前不存在，不进行恢复.......");  
				return;
			}
			
			in = new FileInputStream(path); 
			channel = in.getChannel();
			
			ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024); // 这个参数是可以动态调节的
			// 每次你接受到一个fsimage文件的时候记录一下他的大小，持久化到磁盘上去
			// 每次重启就分配对应空间的大小就可以了
			int count = channel.read(buffer);
			
			buffer.flip();
			String fsimageJson = new String(buffer.array(), 0, count);
			System.out.println("恢复fsimage文件中的数据：" + fsimageJson);  
			
			INode dirTree = JSONObject.parseObject(fsimageJson, new TypeReference<INode>(){});  
			System.out.println(dirTree);  
			directory.setDirTree(dirTree); 
		} finally {
			if(in != null) {
				in.close();
			}
			if(channel != null) {
				channel.close();   
			}
		}
	}

	public long getCheckpointTime() {
		return checkpointTime;
	}

	public void setCheckpointTime(long checkpointTime) {
		this.checkpointTime = checkpointTime;
	}

	public void setSyncedTxid(long syncedTxid) {
		this.syncedTxid = syncedTxid;
	}

	public boolean isFinishedRecover() {
		return finishedRecover;
	}

	public void setFinishedRecover(boolean finishedRecover) {
		this.finishedRecover = finishedRecover;
	}

	public String getCheckpointFile() {
		return checkpointFile;
	}

	public void setCheckpointFile(String checkpointFile) {
		this.checkpointFile = checkpointFile;
	}
	
}
