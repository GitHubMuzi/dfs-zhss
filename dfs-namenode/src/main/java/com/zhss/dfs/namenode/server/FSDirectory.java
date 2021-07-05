package com.zhss.dfs.namenode.server;

import java.util.LinkedList;
import java.util.List;

/**
 * 负责管理内存中的文件目录树的核心组件
 * @author zhonghuashishan
 *
 */
public class FSDirectory {
	
	/**
	 * 内存中的文件目录树
	 */
	private INode dirTree; 
	// 他就是一个父子层级关系的数据结构，文件目录树
	// 创建目录，删除目录，重命名目录，创建文件，删除文件，重命名文件
	// 诸如此类的一些操作，都是在维护内存里的文件目录树，其实本质都是对这个内存的数据结构进行更新
	
	// 先创建了一个目录层级结果：/usr/warehosue/hive
	// 如果此时来创建另外一个目录：/usr/warehouse/spark
	
	public FSDirectory() {
		this.dirTree = new INode("/");  // 默认刚开始就是空的节点
	}
	
	/**
	 * 创建目录
	 * @param path 目录路径
	 */
	public void mkdir(String path) {
		// path = /usr/warehouse/hive
		// 你应该先判断一下，“/”根目录下有没有一个“usr”目录的存在
		// 如果说有的话，那么再判断一下，“/usr”目录下，有没有一个“/warehouse”目录的存在
		// 如果说没有，那么就得先创建一个“/warehosue”对应的目录，挂在“/usr”目录下
		// 接着再对“/hive”这个目录创建一个节点挂载上去
	
		synchronized(dirTree) { // 内存数据结构，更新的时候必须得加锁的
			String[] pathes = path.split("/");
			INode parent = dirTree;
			
			for(String splitedPath : pathes) { // ["","usr","warehosue","spark"]  
				if(splitedPath.trim().equals("")) {
					continue;
				}
				
				INode dir = findDirectory(parent, splitedPath); // parent="/usr"
				
				if(dir != null) {
					parent = dir;
					continue;
				}
				
				INode child = new INode(splitedPath); // "/usr"
				parent.addChild(child);  
				parent = child;
			}
		}
		
//		printDirTree(dirTree, "");  
	}
	
	/**
	 * 创建文件
	 * @param filename 文件名
	 * @return
	 */
	public Boolean create(String filename) {
		// /image/product/img001.jpg
		// 其实完全可以把前面的路径部分截取出来，去找对应的目录
		synchronized(dirTree) {
			String[] splitedFilename = filename.split("/"); 
			String realFilename = splitedFilename[splitedFilename.length - 1];
			
			INode parent = dirTree;
			
			for(int i = 0; i < splitedFilename.length - 1; i++) { 
				if(i == 0) {
					continue;
				}
				
				INode dir = findDirectory(parent, splitedFilename[i]);  
				
				if(dir != null) {
					parent = dir;
					continue;
				}
				
				INode child = new INode(splitedFilename[i]);  
				parent.addChild(child);  
				parent = child;
			}
			
			// 此时就已经获取到了文件的上一级目录
			// 可以查找一下当前这个目录下面是否有对应的文件了
			if(existFile(parent, realFilename)) {
				return false;
			}
			
			// 真正的在目录里创建一个文件出来
			INode file = new INode(realFilename);
			parent.addChild(file);
			
			System.out.println(dirTree); 
			
			return true;
		}
	}
	
	/**
	 * 目录下是否存在这个文件
	 * @param dir
	 * @param filename
	 * @return
	 */
	private Boolean existFile(INode dir, String filename) {
		if(dir.getChildren() != null && dir.getChildren().size() > 0) {
			for(INode child : dir.getChildren()) {
				if(child.getPath().equals(filename)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	private void printDirTree(INode dirTree, String blank) {
		if(dirTree.getChildren().size() == 0) {
			return;
		}
		for(INode dir : dirTree.getChildren()) {
			System.out.println(blank + ((INode) dir).getPath());    
			printDirTree((INode) dir, blank + " ");  
		}
	}
	
	/**
	 * 查找子目录
	 * @param dir
	 * @param path
	 * @return
	 */
	private INode findDirectory(INode dir, String path) {
		if(dir.getChildren().size() == 0) {
			return null;
		}
		
		for(INode child : dir.getChildren()) {
			if(child instanceof INode) {
				INode childDir = (INode) child;
				if((childDir.getPath().equals(path))) {
					return childDir;
				} 
			}
		}
		
		return null;
	}
	
	
	public INode getDirTree() {
		return dirTree;
	}
	public void setDirTree(INode dirTree) {
		this.dirTree = dirTree;
	}
	
	/**
	 * 代表文件目录树中的一个目录
	 * @author zhonghuashishan
	 *
	 */
	public static class INode {
		
		private String path;
		private List<INode> children;
		
		public INode() {
			
		}
		
		public INode(String path) {
			this.path = path;
			this.children = new LinkedList<INode>();
		}
		
		public void addChild(INode inode) {
			this.children.add(inode);
		}
		
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public List<INode> getChildren() {
			return children;
		}
		public void setChildren(List<INode> children) {
			this.children = children;
		}

		@Override
		public String toString() {
			return "INode [path=" + path + ", children=" + children + "]";
		}
		
	}
	
}
