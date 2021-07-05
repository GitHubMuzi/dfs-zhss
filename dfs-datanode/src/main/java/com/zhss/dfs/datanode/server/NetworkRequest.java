package com.zhss.dfs.datanode.server;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * 网络请求
 * @author zhonghuashishan
 *
 */
public class NetworkRequest {
	
	public static final Integer REQUEST_SEND_FILE = 1;
	public static final Integer REQUEST_READ_FILE = 2;

	// processor标识
	private Integer processorId;
	// 该请求是哪个客户端 发送过来的
	private String client;
	// 本次网络请求对应的连接
	private SelectionKey key;
	// 本次网络请求对应的连接
	private SocketChannel channel;
	
	// 缓存中的数据
    private CachedRequest cachedRequest = new CachedRequest();
    private ByteBuffer cachedRequestTypeBuffer;
    private ByteBuffer cachedFilenameLengthBuffer;
    private ByteBuffer cachedFilenameBuffer;
    private ByteBuffer cachedFileLengthBuffer;
    private ByteBuffer cachedFileBuffer;
    
    public NetworkRequest() {
    	System.out.println("请求初始化：" + DataNodeConfig.DATA_DIR); 
    }

	public SelectionKey getKey() {
		return key;
	}
	public void setKey(SelectionKey key) {
		this.key = key;
	}
	public SocketChannel getChannel() {
		return channel;
	}
	public void setChannel(SocketChannel channel) {
		this.channel = channel;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	
	/**
	 * 从网络连接中读取与解析出来一个请求
	 * @param channel
	 */
	public void read() {
		try {
			// 假如说你这个一次读取的数据里包含了多个文件的话
			// 这个时候我们会先读取文件名，然后根据文件的大小去读取这么多的数据
		    // 需要先提取出来这次请求是什么类型：1 发送文件；2 读取文件
			
			System.out.println("开始读取请求：" + DataNodeConfig.DATA_DIR); 
			
			Integer requestType = null;
			if(cachedRequest.reqeustType != null) {
				requestType = cachedRequest.reqeustType;
			} else {
				requestType = getRequestType(channel); // 但是此时channel的position肯定也变为了4
			}
			if(requestType == null) {
				return;
			}
			System.out.println("从请求中解析出来请求类型：" + requestType); 
			
			// 拆包，就是说人家一次请求，本来是包含了：requestType + filenameLength + filename [+ imageLength + image]
			// 这次OP_READ事件，就读取到了requestType的4个字节中的2个字节，剩余的数据
			// 就被放在了下一次OP_READ事件中了
			if(REQUEST_SEND_FILE.equals(requestType)) {
				handleSendFileRequest(channel, key);
			} else if(REQUEST_READ_FILE.equals(requestType)) {
				handleReadFileRequest(channel, key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取本次请求的类型
	 * @param channel
	 * @param buffer
	 * @return
	 */
	public Integer getRequestType(SocketChannel channel) throws Exception {
		Integer requestType = null;
		
		if(cachedRequest.reqeustType != null) {
			return cachedRequest.reqeustType;
		}
		
		ByteBuffer requestTypeBuffer = null;
		if(cachedRequestTypeBuffer != null) {
			requestTypeBuffer = cachedRequestTypeBuffer;
		} else {
			requestTypeBuffer = ByteBuffer.allocate(4);
		}
		
		channel.read(requestTypeBuffer);  // 此时requestType ByteBuffer，position跟limit都是4，remaining是0
		
		if(!requestTypeBuffer.hasRemaining()) {
			// 已经读取出来了4个字节，可以提取出来requestType了
			requestTypeBuffer.rewind(); // 将position变为0，limit还是维持着4
			requestType = requestTypeBuffer.getInt();
			cachedRequest.reqeustType = requestType;
 		} else {
 			cachedRequestTypeBuffer = requestTypeBuffer;  
 		}
		
		return requestType;
	}
	
	/**
	 * 是否已经完成了一个请求的读取
	 * @return
	 */
	public Boolean hasCompletedRead() {
		return cachedRequest.hasCompletedRead;
	}
	
	/**
	 * 获取文件名同时转换为本地磁盘目录中的绝对路径
	 * @param channel
	 * @return
	 * @throws Exception
	 */
	private Filename getFilename(SocketChannel channel) throws Exception {
		Filename filename = new Filename(); 
		
		if(cachedRequest.filename != null) {
        	return cachedRequest.filename;
        } else {
        	String relativeFilename = getRelativeFilename(channel);
        	if(relativeFilename == null) {
        		return null;
        	}
        	
        	String absoluteFilename = getAbsoluteFilename(relativeFilename);
        	// /image/product/iphone.jpg
        	filename.relativeFilename = relativeFilename;
        	filename.absoluteFilename = absoluteFilename;
        	
        	cachedRequest.filename = filename;
        } 
		
		return filename;
	}
	
	/**
	 * 获取相对路径的文件名
	 * @param channel
	 * @return
	 */
	private String getRelativeFilename(SocketChannel channel) throws Exception {
		Integer filenameLength = null;
		String filename = null;
		
		// 读取文件名的大小
		if(cachedRequest.filenameLength == null) {
			ByteBuffer filenameLengthBuffer = null;
			if(cachedFilenameLengthBuffer != null) {
				filenameLengthBuffer = cachedFilenameLengthBuffer;
			} else {
				filenameLengthBuffer = ByteBuffer.allocate(4);
			}
					
			channel.read(filenameLengthBuffer); 
			
			if(!filenameLengthBuffer.hasRemaining()) { 
				filenameLengthBuffer.rewind();
				filenameLength = filenameLengthBuffer.getInt();
				cachedRequest.filenameLength = filenameLength;
	 		} else {
	 			cachedFilenameLengthBuffer = filenameLengthBuffer;
	 			return null;
	 		}
		}
		
		// 读取文件名
		ByteBuffer filenameBuffer = null;
		if(cachedFilenameBuffer != null) {
			filenameBuffer = cachedFilenameBuffer;
		} else {
			filenameBuffer = ByteBuffer.allocate(filenameLength);
		}
		
		channel.read(filenameBuffer);
		
		if(!filenameBuffer.hasRemaining()) {
			filenameBuffer.rewind();
			filename = new String(filenameBuffer.array());  
		} else {
			cachedFilenameBuffer = filenameBuffer;
		}
		
		return filename;
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
    	System.out.println(dirPath); 
    	
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
    	System.out.println(absoluteFilename);
    	
    	return absoluteFilename;
	}
	
	/**
	 * 从网络请求中获取文件大小
	 * @param channel
	 * @param buffer
	 * @return
	 * @throws Exception
	 */
	private Long getFileLength(SocketChannel channel) throws Exception {
		Long fileLength = null;
		
		if(cachedRequest.fileLength != null) {
			return cachedRequest.fileLength;
		} else {
			ByteBuffer fileLengthBuffer = null;
			if(cachedFileLengthBuffer != null) {  
				fileLengthBuffer = cachedFileLengthBuffer;
			} else {
				fileLengthBuffer = ByteBuffer.allocate(8);
			}
			
			channel.read(fileLengthBuffer);
			
			if(!fileLengthBuffer.hasRemaining()) {
				fileLengthBuffer.rewind();
				fileLength = fileLengthBuffer.getLong();
				cachedRequest.fileLength = fileLength;
			} else {
				cachedFileLengthBuffer = fileLengthBuffer;
			}
    	}
		
		return fileLength;
	}
	
	/**
	 * 发送文件
	 * @param channel
	 * @param remoteAddr
	 * @param key
	 * @throws Exception
	 */
	private void handleSendFileRequest(SocketChannel channel, SelectionKey key) throws Exception {
		// 从请求中解析文件名
        Filename filename = getFilename(channel); 
        System.out.println("从网络请求中解析出来文件名：" + filename); 
        if(filename == null) {
        	return;
        }
        // 从请求中解析文件大小
		Long fileLength = getFileLength(channel); 
		System.out.println("从网络请求中解析出来文件大小：" + fileLength); 
		if(fileLength == null) {
			return;
		}
		
		// 循环不断的从channel里读取数据，并写入磁盘文件
    	ByteBuffer fileBuffer = null;
    	if(cachedFileBuffer != null) {
    		fileBuffer = cachedFileBuffer;
    	} else {
    		fileBuffer = ByteBuffer.allocate(Integer.valueOf(
    				String.valueOf(fileLength)));
    	}
    	
    	channel.read(fileBuffer);
    	
    	if(!fileBuffer.hasRemaining()) {
    		fileBuffer.rewind();
    		cachedRequest.file = fileBuffer;
    		cachedRequest.hasCompletedRead = true;
    		System.out.println("本次文件上传请求读取完毕.......");  
    	} else {
    		cachedFileBuffer = fileBuffer;
    		System.out.println("本次文件上传出现拆包问题，缓存起来，下次继续读取.......");  
    		return;
    	}
	}
	
	/**
	 * 读取文件
	 * @param channel
	 * @param remoteAddr
	 * @param key
	 * @throws Exception
	 */
	private void handleReadFileRequest(SocketChannel channel, SelectionKey key) throws Exception {
		// 从请求中解析文件名
        // 已经是：F:\\development\\tmp1\\image\\product\\iphone.jpg
        Filename filename = getFilename(channel); 
        System.out.println("从网络请求中解析出来文件名：" + filename); 
        if(filename == null) {
        	return;
        }
        cachedRequest.hasCompletedRead = true;
	}
	
	/**
	 * 文件名
	 * @author zhonghuashishan
	 *
	 */
	class Filename {
		
		// 相对路径名
		String relativeFilename;
		// 绝对路径名
		String absoluteFilename;
		
		@Override
		public String toString() {
			return "Filename [relativeFilename=" + relativeFilename + ", absoluteFilename=" + absoluteFilename + "]";
		}
		
	}
	
	/**
	 * 缓存文件
	 * @author zhonghuashishan
	 *
	 */
	class CachedRequest {
    	
		Integer reqeustType;
    	Filename filename;
    	Integer filenameLength;
    	Long fileLength;
    	ByteBuffer file;
    	Boolean hasCompletedRead = false;
    	
    }
	
	public Integer getRequestType() {
		return cachedRequest.reqeustType;
	}
	public String getAbsoluteFilename() {
		return cachedRequest.filename.absoluteFilename;
	}
	public String getRelativeFilename() {
		return cachedRequest.filename.relativeFilename;
	}
	public ByteBuffer getFile() {
		return cachedRequest.file;
	}
	public Long getFileLength() {
		return cachedRequest.fileLength;
	}
	public Integer getProcessorId() {
		return processorId;
	}
	public void setProcessorId(Integer processorId) {
		this.processorId = processorId;
	}
	
}
