package com.zhss.dfs.client;

/**
 * 作为文件系统的接口
 * @author zhonghuashishan
 *
 */
public interface FileSystem {

	/**
	 * 创建目录
	 * @param path 目录对应的路径
	 * @throws Exception
	 */
	void mkdir(String path) throws Exception;
	
	/**
	 * 优雅关闭
	 * @throws Exception
	 */
	void shutdown() throws Exception;
	
	/**
	 * 上传文件
	 * @param file 文件的字节数组
	 * @param filename 文件名
	 * @param fileSize 文件大小
	 * @throws Exception
	 */
	Boolean upload(FileInfo fileInfo, ResponseCallback callback) throws Exception;
	
	/**
	 * 重试上传文件
	 * @param fileInfo
	 * @param excludedHost
	 * @return
	 * @throws Exception
	 */
	Boolean retryUpload(FileInfo fileInfo, Host excludedHost) throws Exception;
	
	/**
	 * 下载文件
	 * @param filename 文件名
	 * @return 文件的字节数组
	 * @throws Exception
	 */
	byte[] download(String filename) throws Exception;
	
}
