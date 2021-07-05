package com.zhss.dfs.datanode.server;

import com.zhss.dfs.namenode.rpc.model.HeartbeatRequest;
import com.zhss.dfs.namenode.rpc.model.HeartbeatResponse;
import com.zhss.dfs.namenode.rpc.model.InformReplicaReceivedRequest;
import com.zhss.dfs.namenode.rpc.model.RegisterRequest;
import com.zhss.dfs.namenode.rpc.model.RegisterResponse;
import com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoRequest;
import com.zhss.dfs.namenode.rpc.service.NameNodeServiceGrpc;
import static com.zhss.dfs.datanode.server.DataNodeConfig.*;

import com.alibaba.fastjson.JSONArray;

import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;

/**
 * 负责跟一组NameNode中的某一个进行通信的线程组件
 * @author zhonghuashishan
 *
 */
public class NameNodeRpcClient {
	
	private NameNodeServiceGrpc.NameNodeServiceBlockingStub namenode;
	
	public NameNodeRpcClient() {
		ManagedChannel channel = NettyChannelBuilder
				.forAddress(NAMENODE_HOSTNAME, NAMENODE_PORT)
				.negotiationType(NegotiationType.PLAINTEXT)
				.build();
		this.namenode = NameNodeServiceGrpc.newBlockingStub(channel);
		System.out.println("跟NameNode的" + NAMENODE_PORT + "端口建立连接......");  
	} 
	
	/**
	 * 向自己负责通信的那个NameNode进行注册
	 */
	public Boolean register() throws Exception {
		// 发送rpc接口调用请求到NameNode去进行注册
		// 在这里进行注册的时候会提供哪些信息过去呢？
		// 比如说当前这台机器的ip地址、hostname，这两个东西假设是写在配置文件里的
		// 我们写代码的时候，主要是在本地来运行和测试，有一些ip和hostname，就直接在代码里写死了
		// 大家后面自己可以留空做一些完善，你可以加一些配置文件读取的代码
		// 通过RPC接口发送到NameNode他的注册接口上去
		RegisterRequest request = RegisterRequest.newBuilder()
				.setIp(DATANODE_IP)
				.setHostname(DATANODE_HOSTNAME)  
				.setNioPort(NIO_PORT)   
				.build();
		RegisterResponse response = namenode.register(request);
		
		System.out.println("注册时：" + DATA_DIR); 
		System.out.println("完成向NameNode的注册，响应消息为：" + response.getStatus());  
		
		if(response.getStatus() == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 发送心跳
	 * @throws Exception
	 */
	public HeartbeatResponse heartbeat() throws Exception {
		HeartbeatRequest request = HeartbeatRequest.newBuilder()
				.setIp(DATANODE_IP)
				.setHostname(DATANODE_HOSTNAME)
				.setNioPort(NIO_PORT)  
				.build();
		return namenode.heartbeat(request);
	}
	
	/**
	 * 上报全量存储信息
	 */
	public void reportCompleteStorageInfo(StorageInfo storageInfo) { 
		if(storageInfo == null) {
			System.out.println("当前没有存储任何文件，不需要全量上报....."); 
			return;
		}
		
		ReportCompleteStorageInfoRequest request = ReportCompleteStorageInfoRequest.newBuilder()
				.setIp(DATANODE_IP)
				.setHostname(DATANODE_HOSTNAME)
				.setFilenames(JSONArray.toJSONString(storageInfo.getFilenames()))
				.setStoredDataSize(storageInfo.getStoredDataSize())
				.build();
		
		namenode.reportCompleteStorageInfo(request);
		
		System.out.println("全量上报存储信息：" + storageInfo);  
	}
	
	/**
	 * 通知Master节点自己收到了一个文件的副本
	 * @param filename
	 * @throws Exception
	 */
	public void informReplicaReceived(String filename) throws Exception {
		InformReplicaReceivedRequest request = InformReplicaReceivedRequest.newBuilder()
				.setHostname(DATANODE_HOSTNAME)
				.setIp(DATANODE_IP) 
				.setFilename(filename)
				.build();
		namenode.informReplicaReceived(request);
	}
	
}
