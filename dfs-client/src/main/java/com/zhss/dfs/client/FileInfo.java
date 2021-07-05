package com.zhss.dfs.client;

/**
 * 文件信息
 * @author zhonghuashishan
 *
 */
public class FileInfo {

	private String filename;
	private long fileLength;
	private byte[] file;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getFileLength() {
		return fileLength;
	}
	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	
}
