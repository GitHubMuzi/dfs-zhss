package com.zhss.dfs.datanode.server;

import java.io.File;

/**
 * 磁盘存储管理组件
 * @author zhonghuashishan
 *
 */
public class StorageManager {

	/**
	 * 获取存储信息
	 * @return
	 */
	public StorageInfo getStorageInfo() {
		StorageInfo storageInfo = new StorageInfo();
		
		File dataDir = new File(DataNodeConfig.DATA_DIR); 
		File[] children = dataDir.listFiles();
		if(children == null || children.length == 0) {
			return null;
		}
		
		for(File child : children) {
			scanFiles(child, storageInfo);
		}
		
		return storageInfo;
	}
	
	/**
	 * 扫描文件
	 * @param dir
	 */
	private void scanFiles(File dir, StorageInfo storageInfo) {
		if(dir.isFile()) {
			String path = dir.getPath();
			path = path.substring(DataNodeConfig.DATA_DIR.length());
			// \image\product\iphone.jpg
			path = path.replace("\\", "/"); // /image/product/iphone.jpg
			
			storageInfo.addFilename(path + "_" + dir.length());  
			storageInfo.addStoredDataSize(dir.length());  
			
			return;
		}
		
		File[] children = dir.listFiles();
		if(children == null || children.length == 0) {
			return;
		}
		
		for(File child : children) {
			scanFiles(child, storageInfo);
		}
	}
	
}
