/**
* TreeEntity.java
* @author ZHONGMC
* @description 
* @created Wed Sep 12 2018 15:37:32 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Tue Dec 04 2018 16:17:38 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class TreeEntity<T extends Tree<T>> extends AbstractEntity implements Tree<T> {

	private static final long serialVersionUID = -8382756331611251090L;

	/**
	 * 父节点
	 */
	// @JsonIgnore
	// @ManyToOne
	// @JoinColumn(name="PARENT_ID", insertable = false, updatable = false)
	// private T parent;

	/**
	 * 所有子节点
	 */
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	private List<T> childs = new ArrayList<T>();

	/**
	 * 树节点的ID链
	 */
	private String fullId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycnet.mirage.domain.Tree#isRoot()
	 */
	@Transient
	public boolean isRoot() {
		return (null == parentId);
	}

	/**
	 * @return the parent
	 */
	// public T getParent() {
	// return parent;
	// }

	/**
	 * @param parent the parent to set
	 */
	// public void setParent(T parent) {
	// this.parent = parent;
	// }

	@Column(name = "PARENT_ID")
	protected Long parentId;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long pid) {
		this.parentId = pid;
	}

	/**
	 * 添加子节点
	 * 
	 * @param child
	 */
	public void addChild(T child) {
		childs.add(child);
		child.setParentId(id);
		// child.setParent((T) this);
	}

	/**
	 * @return the childs
	 */
	public List<T> getChilds() {
		return childs;
	}

	/**
	 * @param childs the childs to set
	 */
	public void setChilds(List<T> childs) {
		this.childs = childs;
	}

	public String getFullId() {
		return fullId;
	}

	public void setFullId(String fullId) {
		this.fullId = fullId;
	}

}
