/**
* Tree.java
* @author ZHONGMC
* @description 
* @created Wed Sep 12 2018 15:37:19 GMT+0800 (中国标准时间)
* @copyright YNET 
* None
* @last-modified Wed Sep 12 2018 15:37:19 GMT+0800 (中国标准时间)
*/



package com.ynet.imis.domain;

import java.util.List;


public interface Tree<T> {

		/**
	 * 获取节点名称
	 */
	String getName();
	/**
	 * 设置节点名称
	 */
	void setName(String name);
	/**
	 * 获取子节点
	 */
	List<T> getChilds();

	/**
	 * 判断节点是否是根节点
	 */
	boolean isRoot();
	
	Long getId();
	
	
	Long getParentId();
	
	void setParentId(Long id);
	
    
}
