package com.zhss.dfs.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@SuppressWarnings("unused")
public class FileSystemTest {
	
	private static FileSystem filesystem = new FileSystemImpl();
	
	public static void main(String[] args) throws Exception {
//		testMkdir();
//		testShutdown();
//		testCreateFile();
		testReadFile();
	}
	
	private static void testMkdir() throws Exception {
		for(int j = 0; j < 10; j++) {
			new Thread() {
				
				public void run() {
					for(int i = 0; i < 100; i++) {
						try {
							filesystem.mkdir("/usr/warehouse/spark" + i + "_" + Thread.currentThread().getName());  
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				
			}.start();
		}
	}
	
	private static void testShutdown() throws Exception {
		filesystem.shutdown();
	}
	
	private static void testCreateFile() throws Exception {
		File iamge = new File("F:\\development\\tmp\\timg.jpg");
		long fileLength = iamge.length();
		
		ByteBuffer buffer = ByteBuffer.allocate((int)fileLength); 
		
		FileInputStream imageIn = new FileInputStream(iamge);
		FileChannel imageChannel = imageIn.getChannel();
		int hasRead = imageChannel.read(buffer);
		buffer.flip();
		
		String filename = "/image/product/iphone.jpg";
		byte[] file = buffer.array();
		
		FileInfo fileInfo = new FileInfo();
		fileInfo.setFilename(filename); 
		fileInfo.setFileLength(fileLength); 
		fileInfo.setFile(file); 
		
		filesystem.upload(fileInfo, new ResponseCallback() {
			
			@Override
			public void process(NetworkResponse response) {
				if(response.error()) {
					Host excludedHost = new Host();
					excludedHost.setHostname(response.getHostname()); 
					excludedHost.setIp(response.getIp()); 
					
					try {
						filesystem.retryUpload(fileInfo, excludedHost);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					ByteBuffer buffer = response.getBuffer();
					String responseStatus = new String(buffer.array(), 0, buffer.remaining());
					System.out.println("文件上传完毕，响应结果为：" + responseStatus);
				}
			}
			
		});
		
		imageIn.close();
		imageChannel.close();
	}
	
	private static void testReadFile() throws Exception {
		byte[] image = filesystem.download("/image/product/iphone.jpg"); 
		ByteBuffer buffer = ByteBuffer.wrap(image);
		
		FileOutputStream imageOut = new FileOutputStream("F:\\development\\tmp\\iphone.jpg");    
    	FileChannel imageChannel = imageOut.getChannel();
    	imageChannel.write(buffer);
    	
    	imageChannel.close();
    	imageOut.close();  
	}
	
}
