package com.zhss.dfs.client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhss.dfs.namenode.rpc.model.AllocateDataNodesRequest;
import com.zhss.dfs.namenode.rpc.model.AllocateDataNodesResponse;
import com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasRequest;
import com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse;
import com.zhss.dfs.namenode.rpc.model.CreateFileRequest;
import com.zhss.dfs.namenode.rpc.model.CreateFileResponse;
import com.zhss.dfs.namenode.rpc.model.MkdirRequest;
import com.zhss.dfs.namenode.rpc.model.MkdirResponse;
import com.zhss.dfs.namenode.rpc.model.ReallocateDataNodeRequest;
import com.zhss.dfs.namenode.rpc.model.ReallocateDataNodeResponse;
import com.zhss.dfs.namenode.rpc.model.ShutdownRequest;
import com.zhss.dfs.namenode.rpc.service.NameNodeServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;

/**
 * 文件系统客户端的实现类
 * @author zhonghuashishan
 *
 */
public class FileSystemImpl implements FileSystem {
	
	private static final String NAMENODE_HOSTNAME = "localhost";
	private static final Integer NAMENODE_PORT = 50070;
	
	private NameNodeServiceGrpc.NameNodeServiceBlockingStub namenode;
	private NioClient nioClient;
	
	public FileSystemImpl() {
		ManagedChannel channel = NettyChannelBuilder
				.forAddress(NAMENODE_HOSTNAME, NAMENODE_PORT)
				.negotiationType(NegotiationType.PLAINTEXT)
				.build();
		this.namenode = NameNodeServiceGrpc.newBlockingStub(channel);
		this.nioClient = new NioClient();
	}
	
	/**
	 * 创建目录
	 */
	@Override
	public void mkdir(String path) throws Exception {
		MkdirRequest request = MkdirRequest.newBuilder()
				.setPath(path)
				.build();
		
		MkdirResponse response = namenode.mkdir(request);
		// 网络那块知识大家应该都懂了
		// NIO和网络编程底层的一些知识大家懂了
		// 用屁股想想都知道他底层原理
		// 一定是跟NameNode指定的端口通过Socket的方式建立TCP连接
		// 然后呢按照protobuf的协议，把数据按照一定的格式进行封装，TCP包
		// 就会通过底层的以太网，传输出去到NameNode那儿去
		// 人家就会从TCP包里获取到数据，基于protobuf协议的标准提取数据，交给你的代码来处理
		
		System.out.println("创建目录的响应：" + response.getStatus());  
	}

	/**
	 * 优雅关闭
	 */
	@Override
	public void shutdown() throws Exception {
		ShutdownRequest request = ShutdownRequest.newBuilder()
				.setCode(1)
				.build();
		namenode.shutdown(request); 
	}
	
	/**
	 * 上传文件
	 * @param file 文件的字节数组
	 * @param filename 文件名
	 * @param fileSize 文件大小
	 * @throws Exception
	 */
	public Boolean upload(FileInfo fileInfo, ResponseCallback callback) throws Exception {
		// 必须先用filename发送一个RPC接口调用到master节点
		// 去尝试在文件目录树里创建一个文件
		// 此时还需要进行查重，如果这个文件已经存在，就不让你上传了
		if(!createFile(fileInfo.getFilename())) {
			return false;
		}
		
		// /image/product/iphone.jpg
		// 希望的是每个数据节点在物理存储的时候，其实就是会在DATA_DIR下面去建立
		// F:\\development\\tmp\\image\product，目录
		// 在目录里就有一个iphone.jpg
		
		// 就是找master节点去要多个数据节点的地址
		// 就是你要考虑自己上传几个副本，找对应副本数量的数据节点的地址
		// 尽可能在分配数据节点的时候，保证每个数据节点放的数据量是比较均衡的
		// 保证集群里各个机器上放的数据比较均衡
		JSONArray datanodes = allocateDataNodes(fileInfo.getFilename(), 
				fileInfo.getFileLength());
		
		// 依次把文件的副本上传到各个数据节点上去
		// 还要考虑到，如果上传的过程中，某个数据节点他上传失败
		// 此时你需要有一个容错机制的考量
		for(int i = 0; i < datanodes.size(); i++) {
			Host host = getHost(datanodes.getJSONObject(i));
			
			if(!nioClient.sendFile(fileInfo, host, callback)) {
				host = reallocateDataNode(fileInfo, host.getId());
				nioClient.sendFile(fileInfo, host, null);
			}
		}
		
		return true;
	}
	
	/**
	 * 获取数据节点对应的机器
	 * @param datanode
	 * @return
	 */
	private Host getHost(JSONObject datanode) {
		Host host = new Host();
		host.setHostname(datanode.getString("hostname")); 
		host.setIp(datanode.getString("ip")); 
		host.setNioPort(datanode.getInteger("nioPort"));
		return host;
	}
	
	/**
	 * 发送请求到master节点创建文件
	 * @param filename
	 * @return
	 */
	private Boolean createFile(String filename) {
		CreateFileRequest request = CreateFileRequest.newBuilder()
				.setFilename(filename)
				.build();
		CreateFileResponse response = namenode.create(request);
		
		if(response.getStatus() == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 分配双副本对应的数据节点
	 * @param filename
	 * @param fileSize
	 * @return
	 */
	public JSONArray allocateDataNodes(String filename, long fileSize) {
		AllocateDataNodesRequest request = AllocateDataNodesRequest.newBuilder()
				.setFilename(filename)
				.setFileSize(fileSize)
				.build();
		AllocateDataNodesResponse response = namenode.allocateDataNodes(request);
		return JSONArray.parseArray(response.getDatanodes()); 
	}
	
	/**
	 * 分配双副本对应的数据节点
	 * @param filename
	 * @param fileSize
	 * @return
	 */
	public Host reallocateDataNode(FileInfo fileInfo, String excludedHostId) {
		ReallocateDataNodeRequest request = ReallocateDataNodeRequest.newBuilder()
				.setFilename(fileInfo.getFilename())
				.setFileSize(fileInfo.getFileLength())
				.setExcludedDataNodeId(excludedHostId) 
				.build();
		ReallocateDataNodeResponse response = namenode.reallocateDataNode(request);
		return getHost(JSONObject.parseObject(response.getDatanode()));   
	}
	
	/**
	 * 下载文件
	 * @param filename 文件名
	 * @return 文件的字节数组
	 * @throws Exception
	 */
	@Override
	public byte[] download(String filename) throws Exception {
		Host datanode = chooseDataNodeFromReplicas(filename, "");
		
		byte[] file = null;
		
		try {
			file = nioClient.readFile(datanode, filename, true);
		} catch (Exception e) {
			datanode = chooseDataNodeFromReplicas(filename, datanode.getId());
			try {
				file = nioClient.readFile(datanode, filename, false);
			} catch (Exception e2) {
				throw e2;
			}
		}
		
		return file;
	}
	
	/**
	 * 获取文件的某个副本所在的机器
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	private Host chooseDataNodeFromReplicas(String filename, 
			String excludedDataNodeId) throws Exception {
		ChooseDataNodeFromReplicasRequest request = ChooseDataNodeFromReplicasRequest.newBuilder()
				.setFilename(filename)
				.setExcludedDataNodeId(excludedDataNodeId)  
				.build();
		ChooseDataNodeFromReplicasResponse response = namenode.chooseDataNodeFromReplicas(request);
		return getHost(JSONObject.parseObject(response.getDatanode()));
	}

	/**
	 * 重试上传文件
	 */
	@Override
	public Boolean retryUpload(FileInfo fileInfo, Host excludedHost) throws Exception {
		Host host = reallocateDataNode(fileInfo, excludedHost.getId());
		nioClient.sendFile(fileInfo, host, null);
		return true;
	}
	
}
