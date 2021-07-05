package com.zhss.dfs.backupnode.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 负责上传fsimage到NameNode线程
 * @author zhonghuashishan
 *
 */
public class FSImageUploader extends Thread {

	private FSImage fsimage;
	
	public FSImageUploader(FSImage fsimage) {
		this.fsimage = fsimage;
	}
	
	@Override
	public void run() {
		SocketChannel channel = null;  
		Selector selector = null;  
		try {  
			channel = SocketChannel.open();  
			channel.configureBlocking(false);  
			channel.connect(new InetSocketAddress("localhost", 9000)); 
			
			selector = Selector.open();  
			channel.register(selector, SelectionKey.OP_CONNECT); 
			
			boolean uploading = true;
			
			while(uploading){  
				selector.select();   
				
				Iterator<SelectionKey> keysIterator = selector.selectedKeys().iterator();  
				while(keysIterator.hasNext()){  
					SelectionKey key = (SelectionKey) keysIterator.next();  
					keysIterator.remove();  
					   
					if(key.isConnectable()){  
						channel = (SocketChannel) key.channel(); 
						
						if(channel.isConnectionPending()){  
							channel.finishConnect();
							ByteBuffer buffer = ByteBuffer.wrap(fsimage.getFsimageJson().getBytes());
							System.out.println("准备上传fsimage文件数据，大小为：" + buffer.capacity());  
							channel.write(buffer);  
						}   
						
						channel.register(selector, SelectionKey.OP_READ);
					}  
					else if(key.isReadable()){  
						ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);           
						channel = (SocketChannel) key.channel();
						int count = channel.read(buffer);  
						
						if(count > 0) {
							System.out.println("上传fsimage文件成功，响应消息为：" + 
									new String(buffer.array(), 0, count));
							channel.close();
							uploading = false;
						}
					}
				}  
			}                            
		} catch (Exception e) {  
			e.printStackTrace();  
		} finally{  
			if(channel != null){  
				try {  
					channel.close();  
				} catch (IOException e) {                        
					e.printStackTrace();  
				}                    
			}  
			   
			if(selector != null){  
				try {  
					selector.close();  
				} catch (IOException e) {  
					e.printStackTrace();  
				}  
			}  
		}  
	}
	
}
