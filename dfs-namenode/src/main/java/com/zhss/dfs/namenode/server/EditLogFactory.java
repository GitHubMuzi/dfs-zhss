package com.zhss.dfs.namenode.server;

/**
 * 生成editlog内容的工厂类
 * @author zhonghuashishan
 *
 */
public class EditLogFactory {

	public static String mkdir(String path) {
		return "{'OP':'MKDIR','PATH':'" + path + "'}";
	}
	
	public static String create(String path) {
		return "{'OP':'CREATE','PATH':'" + path + "'}";
	}
	
}
