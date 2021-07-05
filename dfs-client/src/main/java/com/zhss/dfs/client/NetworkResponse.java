package com.zhss.dfs.client;

import java.nio.ByteBuffer;

public class NetworkResponse {
	
	public static final String RESPONSE_SUCCESS = "SUCCESS";

	private String requestId;
	private String hostname;
	private String ip;
	private ByteBuffer lengthBuffer;
	private ByteBuffer buffer;
	private Boolean error;
	private Boolean finished;

	public Boolean finished() {
		return finished;
	}
	public void setFinished(Boolean finished) {
		this.finished = finished;
	}
	public ByteBuffer getLengthBuffer() {
		return lengthBuffer;
	}
	public void setLengthBuffer(ByteBuffer lengthBuffer) {
		this.lengthBuffer = lengthBuffer;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public ByteBuffer getBuffer() {
		return buffer;
	}
	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}
	public Boolean error() {
		return error;
	}
	public void setError(Boolean error) {
		this.error = error;
	}
	
}
