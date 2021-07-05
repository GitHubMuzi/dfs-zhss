package com.zhss.dfs.datanode.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.zhss.dfs.datanode.server.DataNodeConfig.*;

/**
 * 数据节点的NIOServer
 * @author zhonghuashishan
 *
 */
public class NioServer extends Thread {
	
	public static final Integer PROCESSOR_THREAD_NUM = 10;
	public static final Integer IO_THREAD_NUM = 10;

	// NIO的selector，负责多路复用监听多个连接的请求
    private Selector selector;  
    // 负责解析请求和发送响应的Processor线程
    private List<NioProcessor> processors = new ArrayList<NioProcessor>();
    // 与NameNode进行通信的客户端
    private NameNodeRpcClient namenode; 
    
    /**
     * NIOServer的初始化，监听端口、队列初始化、线程初始化
     */
    public NioServer(NameNodeRpcClient namenodeRpcClient){  
        this.namenode = namenodeRpcClient;
    }  
    
    public void init() {
    	// 这块代码，就是让NioServer去监听指定的端口号
        try {  
        	// 需要用一个Selector多路复用监听多个连接的事件
        	// 同步非阻塞的效果，也可以实现单个线程支持N个连接的高并发的架构
        	selector = Selector.open();
        	
        	ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();  
        	serverSocketChannel.configureBlocking(false);  // 必须将Channel给设置为非阻塞的
        	// 因为只有这样在底层Selector在多路复用监听的时候，才不会阻塞在某个Channel上
        	serverSocketChannel.socket().bind(new InetSocketAddress(NIO_PORT), 100);   
        	// 其实就是将ServerSocketChannel注册到Selector上去，关注的事件，就是OP_ACCEPT
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); 
            
            System.out.println("NIOServer已经启动，开始监听端口：" + NIO_PORT); 
            
            // 启动固定数量的Processor线程
            NetworkResponseQueues responseQueues = NetworkResponseQueues.get();
            
            for(int i = 0; i < PROCESSOR_THREAD_NUM; i++) {
            	NioProcessor processor = new NioProcessor(i); 
            	processors.add(processor);
            	processor.start();
            	
            	responseQueues.initResponseQueue(i); 
            }
            
            // 启动固定数量的Processor线程
            for(int i = 0; i < IO_THREAD_NUM; i++) {
            	new IOThread(namenode).start();
            }
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }
    
	public void run() { 
		/**
		 * 无限循环，等待IO多路复用方式监听请求
		 */
        while(true){  
            try{  
                selector.select();  // 同步非阻塞             
                
                // 执行到这里，说明有人发送连接请求过来
                Iterator<SelectionKey> keysIterator = selector.selectedKeys().iterator();  
                   
                while(keysIterator.hasNext()){  
                    SelectionKey key = (SelectionKey) keysIterator.next();  
                    keysIterator.remove();
                    
                    if(key.isAcceptable()){  
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();  
                        // 跟每个客户端建立连接
                        SocketChannel channel = serverSocketChannel.accept(); 
                        if(channel != null) {
                            channel.configureBlocking(false);  
                            // 如果一旦跟某个客户端建立了连接之后，就需要将这个客户端
                            // 均匀的分发给后续的Processor线程
                            Integer processorIndex = new Random().nextInt(PROCESSOR_THREAD_NUM);
                            NioProcessor processor = processors.get(processorIndex);
                            processor.addChannel(channel); 
                        }
                    }
                }  
            }  
            catch(Throwable t){  
                t.printStackTrace();  
            }                            
        }                
    }  
	
}
