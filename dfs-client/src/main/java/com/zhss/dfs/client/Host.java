package com.zhss.dfs.client;

public class Host {

	private String hostname;
	private String ip;
	private Integer nioPort;
	
	public Host() {
		
	}
	
	public Host(String hostname, Integer nioPort) {
		this.hostname = hostname;
		this.nioPort = nioPort;
	}

	public String getId() {
		return ip + "-" + hostname;
	}
	
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getNioPort() {
		return nioPort;
	}
	public void setNioPort(Integer nioPort) {
		this.nioPort = nioPort;
	}
	
}
