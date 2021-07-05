package com.zhss.dfs.namenode.server;

/**
 * 下发给DataNode的命令
 * @author zhonghuashishan
 *
 */
public class Command {
	
	public static final Integer REGISTER = 1;
	public static final Integer REPORT_COMPLETE_STORAGE_INFO = 2;
	public static final Integer REPLICATE = 3;
	public static final Integer REMOVE_REPLICA = 4;

	private Integer type;
	private String content;
	
	public Command() {
		
	}
	
	public Command(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Command [type=" + type + ", content=" + content + "]";
	}
	
}
