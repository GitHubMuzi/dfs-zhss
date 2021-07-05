package com.zhss.dfs.backupnode.server;

import com.alibaba.fastjson.JSONArray;
import com.zhss.dfs.namenode.rpc.model.FetchEditsLogRequest;
import com.zhss.dfs.namenode.rpc.model.FetchEditsLogResponse;
import com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest;
import com.zhss.dfs.namenode.rpc.service.NameNodeServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;

public class NameNodeRpcClient {

	private static final String NAMENODE_HOSTNAME = "localhost";
	private static final Integer NAMENODE_PORT = 50070;
	
	private NameNodeServiceGrpc.NameNodeServiceBlockingStub namenode;
	private Boolean isNamenodeRunning = true;
	
	public NameNodeRpcClient() {
		ManagedChannel channel = NettyChannelBuilder
				.forAddress(NAMENODE_HOSTNAME, NAMENODE_PORT)
				.negotiationType(NegotiationType.PLAINTEXT)
				.build(); 
		this.namenode = NameNodeServiceGrpc.newBlockingStub(channel);
	}
	
	/**
	 * 抓取editslog数据
	 * @return
	 */
	public JSONArray fetchEditsLog(long syncedTxid) {
		FetchEditsLogRequest request = FetchEditsLogRequest.newBuilder()
				.setSyncedTxid(syncedTxid)  
				.build();
		
		FetchEditsLogResponse response = namenode.fetchEditsLog(request);
		String editsLogJson = response.getEditsLog();
		
		return JSONArray.parseArray(editsLogJson);
	}

	/**
	 * 更新checkpoint txid
	 * @param txid
	 */
	public void updateCheckpointTxid(long txid) {
		UpdateCheckpointTxidRequest request = UpdateCheckpointTxidRequest.newBuilder()
				.setTxid(txid) 
				.build();
		namenode.updateCheckpointTxid(request);
	}

	public Boolean isNamenodeRunning() {
		return isNamenodeRunning;
	}
	public void setIsNamenodeRunning(Boolean isNamenodeRunning) {
		this.isNamenodeRunning = isNamenodeRunning;
	}
	
}
