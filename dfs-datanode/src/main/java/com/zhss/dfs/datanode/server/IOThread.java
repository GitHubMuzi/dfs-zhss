package com.zhss.dfs.datanode.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 负责执行磁盘IO的线程
 * @author zhonghuashishan
 *
 */
public class IOThread extends Thread {

	public static final Integer REQUEST_SEND_FILE = 1;
	public static final Integer REQUEST_READ_FILE = 2;
	
	private NetworkRequestQueue requestQueue = NetworkRequestQueue.get();
	private NameNodeRpcClient namenode;
	
	public IOThread(NameNodeRpcClient namenode) {
		this.namenode = namenode;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				NetworkRequest request = requestQueue.poll();
				if(request == null) {
					Thread.sleep(100);
					continue;
				}
				
				Integer requestType = request.getRequestType();
				
				if(requestType.equals(REQUEST_SEND_FILE)) {
					// 对于上传文件，将文件写入本地磁盘即可
					writeFileToLocalDisk(request);
				} else if(requestType.equals(REQUEST_READ_FILE)) {
					readFileFromLocalDisk(request);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void readFileFromLocalDisk(NetworkRequest request) throws Exception {
		FileInputStream localFileIn = null;
		FileChannel localFileChannel = null;
		
		try {
			File file = new File(request.getAbsoluteFilename());
	 		Long fileLength = file.length();
	 		
	 		String absoluteFilename = getAbsoluteFilename(request.getRelativeFilename()); 
    		System.out.println("准备读取的文件路径：" + absoluteFilename); 
	 		
	 		localFileIn = new FileInputStream(absoluteFilename);    
	     	localFileChannel = localFileIn.getChannel();
	 		
	 		// 循环不断的从channel里读取数据，并写入磁盘文件
	     	ByteBuffer buffer = ByteBuffer.allocate(
	     			8 + Integer.valueOf(String.valueOf(fileLength)));   
	     	buffer.putLong(fileLength);
	     	int hasReadImageLength = localFileChannel.read(buffer);
	     	System.out.println("从本次磁盘文件中读取了" + hasReadImageLength + " bytes的数据"); 
	     	 
	     	buffer.rewind();
	     	
	     	// 封装响应
	    	NetworkResponse response = new NetworkResponse();
	    	response.setClient(request.getClient());
	    	response.setBuffer(buffer); 
	    	
	    	NetworkResponseQueues responseQueues = NetworkResponseQueues.get();
	    	responseQueues.offer(request.getProcessorId(), response);  
		} finally {
			if(localFileChannel != null) {
				localFileChannel.close();
			}
			if(localFileIn != null) {
				localFileIn.close();
			}
		}
	}

	private void writeFileToLocalDisk(NetworkRequest request) throws Exception {
		// 构建针对本地文件的输出流
		FileOutputStream localFileOut = null;
    	FileChannel localFileChannel = null;
    	
    	try {
    		System.out.println("准备写文件：" + DataNodeConfig.DATA_DIR); 
    		
    		String absoluteFilename = getAbsoluteFilename(request.getRelativeFilename()); 
    		System.out.println("准备写入的文件路径：" + absoluteFilename); 
    		
    		localFileOut = new FileOutputStream(absoluteFilename);    
    		localFileChannel = localFileOut.getChannel();
    		localFileChannel.position(localFileChannel.size());
        	
			int written = localFileChannel.write(request.getFile());
			System.out.println("本次文件上传完毕，将" + written + " bytes的数据写入本地磁盘文件.......");  
			
	    	// 增量上报Master节点自己接收到了一个文件的副本
	    	// /image/product/iphone.jpg
	    	namenode.informReplicaReceived(request.getRelativeFilename() + "_" + request.getFileLength());  
	    	System.out.println("增量上报收到的文件副本给NameNode节点......"); 
	    	
	    	// 封装响应
	    	NetworkResponse response = new NetworkResponse();
	    	response.setClient(request.getClient()); 
	    	response.setBuffer(ByteBuffer.wrap("SUCCESS".getBytes())); 
	    	
	    	NetworkResponseQueues responseQueues = NetworkResponseQueues.get();
	    	responseQueues.offer(request.getProcessorId(), response);  
		} finally {
			localFileChannel.close();
            localFileOut.close();
		}
	}
	
	/**
	 * 获取文件在本地磁盘上的绝对路径名
	 * @param relativeFilename
	 * @return
	 * @throws Exception
	 */
	private String getAbsoluteFilename(String relativeFilename) throws Exception {
		String[] relativeFilenameSplited = relativeFilename.split("/"); 
    	
    	String dirPath = DataNodeConfig.DATA_DIR;
    	
    	for(int i = 0; i < relativeFilenameSplited.length - 1; i++) {
    		if(i == 0) {
    			continue;
    		}
    		dirPath += "\\" + relativeFilenameSplited[i];
    	}
    	
    	File dir = new File(dirPath);
    	if(!dir.exists()) {
    		dir.mkdirs();
    	}
    	
    	String absoluteFilename = dirPath + "\\" + 
    			relativeFilenameSplited[relativeFilenameSplited.length - 1];
    	
    	return absoluteFilename;
	}
	
}
