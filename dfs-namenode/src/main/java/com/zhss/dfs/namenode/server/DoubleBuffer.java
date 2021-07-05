package com.zhss.dfs.namenode.server;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 内存双缓冲
 * @author zhonghuashishan
 *
 */
public class DoubleBuffer {
	
	/**
	 * 单块editslog缓冲区的最大大小：默认是512kb
	 */
	public static final Integer EDIT_LOG_BUFFER_LIMIT = 25 * 1024;
	
	/**
	 * 是专门用来承载线程写入edits log
	 */
	private EditLogBuffer currentBuffer = new EditLogBuffer();
	/**
	 * 专门用来将数据同步到磁盘中去的一块缓冲
	 */
	private EditLogBuffer syncBuffer = new EditLogBuffer();
	/**
	 * 当前这块缓冲区写入的最大的一个txid
	 */
	private long startTxid = 1L;
	/**
	 * 已经输入磁盘中的txid范围
	 */
	private List<String> flushedTxids = new CopyOnWriteArrayList<String>();
	
	/**
	 * 将edits log写到内存缓冲里去
	 * @param log
	 * @throws IOException 
	 */
	public void write(EditLog log) throws IOException {
		currentBuffer.write(log);   
	}
	
	/**
	 * 判断一下当前的缓冲区是否写满了需要刷到磁盘上去
	 * @return
	 */
	public boolean shouldSyncToDisk() {
		if(currentBuffer.size() >= EDIT_LOG_BUFFER_LIMIT) {  
			return true;
		}
		return false;
	}

	/**
	 * 交换两块缓冲区，为了同步内存数据到磁盘做准备
	 */
	public void setReadyToSync() {
		EditLogBuffer tmp = currentBuffer;
		currentBuffer = syncBuffer;
		syncBuffer = tmp;
	}
	
	/**
	 * 将syncBuffer缓冲区中的数据刷入磁盘中
	 * @throws IOException 
	 */
	public void flush() throws IOException {
		syncBuffer.flush();
		syncBuffer.clear();   
	}
	
	/**
	 * 获取已经刷入磁盘的editslog数据
	 * @return
	 */
	public List<String> getFlushedTxids() {
		return flushedTxids;
	}
	
	/**
	 * 获取当前缓冲区里的数据
	 * @return
	 */
	public String[] getBufferedEditsLog() {
		if(currentBuffer.size() == 0) {
			return null; 
		}
		String editsLogRawData = new String(currentBuffer.getBufferData());
		return editsLogRawData.split("\n");  
	}
	
	/**
	 * editslog缓冲区
	 * @author zhonghuashishan
	 *
	 */
	class EditLogBuffer {
		
		/**
		 * 针对内存缓冲区的字节数组输出流
		 */
		ByteArrayOutputStream buffer; 
		/**
		 * 上一次flush到磁盘的时候他的最大的txid是多少
		 */
		long endTxid = 0L;
		
		public EditLogBuffer() {
			this.buffer = new ByteArrayOutputStream(EDIT_LOG_BUFFER_LIMIT * 2);
		}
		
		/**
		 * 将editslog日志写入缓冲区
		 * @param log
		 * @throws IOException    
		 */
		public void write(EditLog log) throws IOException {
			endTxid = log.getTxid();
			buffer.write(log.getContent().getBytes());   
			buffer.write("\n".getBytes());  
			System.out.println("写入一条editslog：" + log.getContent() 
					+ "，当前缓冲区的大小是：" + size());  
		}
		
		/**
		 * 获取当前缓冲区已经写入数据的字节数量
		 * @return
		 */
		public Integer size() {
			return buffer.size();
		}
		
		/**
		 * 将sync buffer中的数据刷入磁盘中
		 * @throws IOException 
		 */
		public void flush() throws IOException {
			byte[] data = buffer.toByteArray();
			ByteBuffer dataBuffer = ByteBuffer.wrap(data);
			
			String editsLogFilePath = "F:\\development\\editslog\\edits-" 
					+ startTxid + "-" + endTxid + ".log";
			flushedTxids.add(startTxid + "_" + endTxid);
			 
			RandomAccessFile file = null;
			FileOutputStream out = null;
			FileChannel editsLogFileChannel = null;
			
			try {
				file = new RandomAccessFile(editsLogFilePath, "rw"); // 读写模式，数据写入缓冲区中
				out = new FileOutputStream(file.getFD()); 
				editsLogFileChannel = out.getChannel();
				
				editsLogFileChannel.write(dataBuffer);  
				editsLogFileChannel.force(false);  // 强制把数据刷入磁盘上
			} finally {
				if(out != null) {
					out.close();
				}
				if(file != null) {
					file.close();  
				}
				if(editsLogFileChannel != null) {
					editsLogFileChannel.close(); 
				}
			}
			
			startTxid = endTxid + 1;
		}
		
		/**
		 * 清空掉内存缓冲里面的数据
		 */
		public void clear() {
			buffer.reset();  
		}
		
		/**
		 * 获取内存缓冲区当前的数据
		 * @return
		 */
		public byte[] getBufferData() {
			return buffer.toByteArray();
		}
		
	}
	
}
	