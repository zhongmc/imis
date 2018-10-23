package com.ynet.imis.domain.org;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
//import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import com.ynet.imis.domain.TreeEntity;

@Entity
@Table(name = "DEPARTMENT")
public class Department extends TreeEntity<Department> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3686627916388626418L;

	// 机构状态
	public enum DepStatus {
		ACTIVE, FREEZE, DELETE
	}

	private DepStatus depStatu = DepStatus.FREEZE;

	// 组织机构编码
	@Column(length = 30)
	private String depNumber;

	@Column(length = 100)
	@Length(min = 2, max = 100, message = "机构长度为2-100")
	private String name;// 名称

	@Column(length = 200, name = "YC_DESC")
	private String desc;// 备注

	public Department() {

	}

	public Department(Long id, String name) {
		this.setId(id);
		this.setName(name);
	}

	public Department(Long id, Long pid, String name, String fullId) {
		this.id = id;
		this.parentId = pid;
		this.name = name;
		this.setFullId(fullId);
	}

	public Department(Long id, Long pid, String name, String desc, String orgNumber, String fullId) {
		super.setId(id);
		super.setParentId(pid);
		this.name = name;
		this.desc = desc;
		this.depNumber = orgNumber;
		super.setFullId(fullId);
	}

	@Override
	public String toString() {
		return String.format("Department, id:%s, name:%s", id, name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDepNumber() {
		return depNumber;
	}

	public void setDepNumber(String orgNumber) {
		this.depNumber = orgNumber;
	}

	public DepStatus getDepStatu() {
		return depStatu;
	}

	public void setDepStatu(DepStatus orgStatu) {
		this.depStatu = orgStatu;
	}

	public Department clone() {
		Department dep = new Department();

		dep.id = id;
		dep.name = name;
		dep.parentId = parentId;
		dep.depStatu = depStatu;
		dep.desc = desc;
		dep.depNumber = depNumber;
		return dep;
	}

}
