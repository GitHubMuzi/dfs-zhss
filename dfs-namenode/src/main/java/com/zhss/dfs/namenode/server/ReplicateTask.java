package com.zhss.dfs.namenode.server;

/**
 * 副本复制任务
 * @author zhonghuashishan
 *
 */
public class ReplicateTask {

	private String filename;
	private Long fileLength;
	private DataNodeInfo sourceDatanode;
	private DataNodeInfo destDatanode;
	
	public ReplicateTask(String filename, Long fileLength, DataNodeInfo sourceDatanode, DataNodeInfo destDatanode) {
		this.filename = filename;
		this.fileLength = fileLength;
		this.sourceDatanode = sourceDatanode;
		this.destDatanode = destDatanode;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Long getFileLength() {
		return fileLength;
	}

	public void setFileLength(Long fileLength) {
		this.fileLength = fileLength;
	}

	public DataNodeInfo getSourceDatanode() {
		return sourceDatanode;
	}

	public void setSourceDatanode(DataNodeInfo sourceDatanode) {
		this.sourceDatanode = sourceDatanode;
	}

	public DataNodeInfo getDestDatanode() {
		return destDatanode;
	}

	public void setDestDatanode(DataNodeInfo destDatanode) {
		this.destDatanode = destDatanode;
	}

	@Override
	public String toString() {
		return "ReplicateTask [filename=" + filename + ", fileLength=" + fileLength + ", sourceDatanode="
				+ sourceDatanode + ", destDatanode=" + destDatanode + "]";
	}
	
}
