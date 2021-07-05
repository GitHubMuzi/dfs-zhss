package com.zhss.dfs.namenode.server;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 负责管理edits log日志的核心组件
 * @author zhonghuashishan
 *
 */
public class FSEditlog {
	
	/**
	 * editlog日志文件清理的时间间隔
	 */
	private static final Long EDIT_LOG_CLEAN_INTERVAL = 30 * 1000L;

	/**
	 * 元数据管理组件
	 */
	private FSNamesystem namesystem;
	/**
	 * 当前递增到的txid的序号
	 */
	private long txidSeq = 0L;
	/**
	 * 内存双缓冲区
	 */
	private DoubleBuffer doubleBuffer = new DoubleBuffer();
	/**
	 * 当前是否在将内存缓冲刷入磁盘中
	 */
	private volatile Boolean isSyncRunning = false;
	/**
	 * 在同步到磁盘中的最大的一个txid
	 */
	private volatile Long syncTxid = 0L;
	/**
	 * 是否正在调度一次刷盘的操作
	 */
	private volatile Boolean isSchedulingSync = false;
	/**
	 * 每个线程自己本地的txid副本
	 */
	private ThreadLocal<Long> localTxid = new ThreadLocal<Long>();
	
	public FSEditlog(FSNamesystem namesystem) {
		this.namesystem = namesystem;
		
		EditLogCleaner editlogCleaner = new EditLogCleaner();
		editlogCleaner.start();   
	}
	
	/**
	 * 记录edits log日志
	 * @param log
	 */
	public void logEdit(String content) {
		// 这里必须得直接加锁
		synchronized(this) {
			// 刚进来就直接检查一下是否有人正在调度一次刷盘的操作
			waitSchedulingSync();
			
			// 获取全局唯一递增的txid，代表了edits log的序号
			txidSeq++;
			long txid = txidSeq;
			localTxid.set(txid); // 放到ThreadLocal里去，相当于就是维护了一份本地线程的副本
			
			// 构造一条edits log对象
			EditLog log = new EditLog(txid, content); 
			
			// 将edits log写入内存缓冲中，不是直接刷入磁盘文件
			try {
				doubleBuffer.write(log);  
			} catch (Exception e) {
				e.printStackTrace();  
			}
			
			// 每次写完一条editslog之后，就应该检查一下当前这个缓冲区是否满了
			if(!doubleBuffer.shouldSyncToDisk()) {
				return;
			}
			
			// 如果代码进行到这里，就说明需要刷磁盘
			isSchedulingSync = true;
		}
		
		// 释放掉锁
		
		logSync();
	}
	
	/**
	 * 等待正在调度的刷磁盘的操作
	 */
	private void waitSchedulingSync() {
		try {
			while(isSchedulingSync) {
				wait(100); 
				// 此时就释放锁，等待一秒再次尝试获取锁，去判断
				// isSchedulingSync是否为false，就可以脱离出while循环
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将内存缓冲中的数据刷入磁盘文件中
	 * 在这里尝试允许某一个线程一次性将内存缓冲中的数据刷入磁盘文件中
	 * 相当于实现一个批量将内存缓冲数据刷磁盘的过程
	 */
	private void logSync() {
		// 再次尝试加锁
		synchronized(this) {
			long txid = localTxid.get(); // 获取到本地线程的副本
			
			// 如果说当前正好有人在刷内存缓冲到磁盘中去
			if(isSyncRunning) {
				// 那么此时这里应该有一些逻辑判断
				
				// 假如说某个线程已经把txid = 1,2,3,4,5的edits log都从syncBuffer刷入磁盘了
				// 或者说此时正在刷入磁盘中
				// 此时syncMaxTxid = 5，代表的是正在输入磁盘的最大txid
				// 那么这个时候来一个线程，他对应的txid = 3，此时他是可以直接返回了
				// 就代表说肯定是他对应的edits log已经被别的线程在刷入磁盘了
				// 这个时候txid = 3的线程就不需要等待了
				if(txid <= syncTxid) { // 如果之前已经有人在同步syncTxid=30的数据了
					// 进来一个线程结果他的txid=25，此时肯定是不对了，直接返回就可以了
					return;
				}
				
				// 比如说当前线程的txid=80，就说明他需要去刷数据，但是需要等待别人先刷完
				try {
					while(isSyncRunning) {
						wait(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();  
				}
			}
			
			// 交换两块缓冲区
			doubleBuffer.setReadyToSync();
			// 然后可以保存一下当前要同步到磁盘中去的最大的txid
			// 此时editLogBuffer中的syncBuffer这块区域，交换完以后这里可能有多条数据
			// 而且他里面的edits log的txid一定是从小到大的
			// 此时要同步的txid = 6,7,8,9,10,11,12
			// syncMaxTxid = 12
			syncTxid = txid;
			// 写入的最大的一个txid
			// syncTxid代表的就是说，把txid=30为止的之前所有的editslog都会刷入磁盘中了
			// editslog的txid都是依次递增的，就是当前他的这个txid
			// 设置当前正在同步到磁盘的标志位
			isSchedulingSync = false;
			notifyAll(); // 唤醒那些还卡在while循环那儿的线程
			isSyncRunning = true;
		}
		
		// 开始同步内存缓冲的数据到磁盘文件里去
		// 这个过程其实是比较慢，基本上肯定是毫秒级了，弄不好就要几十毫秒
		try {
			doubleBuffer.flush();  
		} catch (Exception e) {
			e.printStackTrace();  
		}
		
		synchronized(this) {
			// 同步完了磁盘之后，就会将标志位复位，再释放锁
			isSyncRunning = false;
			// 唤醒可能正在等待他同步完磁盘的线程
			notifyAll();
		}
	}
	
	/**
	 * 强制把内存缓冲里的数据刷入磁盘中
	 */
	public void flush() {
		try {
			doubleBuffer.setReadyToSync(); 
			doubleBuffer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取已经刷入磁盘的editslog数据
	 * @return
	 */
	public List<String> getFlushedTxids() {
		return doubleBuffer.getFlushedTxids();  
	}
	
	/**
	 * 获取当前缓冲区里的数据
	 * @return
	 */
	public String[] getBufferedEditsLog() {
		synchronized(this) { // 这边此时只要获取到了锁，那么就意味着
			// 肯定没有人当前在修改这个内存数据了
			// 此时拉取就肯定可以获取到当前完整的内存缓冲里的数据
			return doubleBuffer.getBufferedEditsLog();
		}
	}
	
	/**
	 * 自动清理editlog文件
	 * @author zhonghuashishan
	 *
	 */
	class EditLogCleaner extends Thread {
		
		@Override
		public void run() {
			System.out.println("editlog日志文件后台清理线程启动......");  
			
			while(true) {
				try {
					Thread.sleep(EDIT_LOG_CLEAN_INTERVAL);
					
					List<String> flushedTxids = getFlushedTxids();
					if(flushedTxids != null && flushedTxids.size() > 0) {
						long checkpointTxid = namesystem.getCheckpointTxid();
						
						for(String flushedTxid : flushedTxids) {
							long startTxid = Long.valueOf(flushedTxid.split("_")[0]);
							long endTxid = Long.valueOf(flushedTxid.split("_")[1]);  
							
							if(checkpointTxid >= endTxid) {
								// 此时就要删除这个文件
								File file = new File("F:\\\\development\\\\editslog\\\\edits-"
										+ startTxid + "-" + endTxid + ".log");
								if(file.exists()) {
									file.delete();  
									System.out.println("发现editlog日志文件不需要，进行删除：" + file.getPath());  
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();  
				}
			}
		}
		
	}
	
}
	
