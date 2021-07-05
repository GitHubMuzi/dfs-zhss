package com.zhss.dfs.backupnode.server;

public class FSImage {

	private long maxTxid;
	private String fsimageJson;
	
	public FSImage(long maxTxid, String fsimageJson) {
		this.maxTxid = maxTxid;
		this.fsimageJson = fsimageJson;
	}

	public long getMaxTxid() {
		return maxTxid;
	}
	public void setMaxTxid(long maxTxid) {
		this.maxTxid = maxTxid;
	}
	public String getFsimageJson() {
		return fsimageJson;
	}
	public void setFsimageJson(String fsimageJson) {
		this.fsimageJson = fsimageJson;
	}
	
}
