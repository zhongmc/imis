
package com.ynet.imis.domain.menu;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ynet.imis.domain.AbstractEntity;

@Entity
public class Role extends AbstractEntity {


	private static final long serialVersionUID = -6370326924479691040L;

	@Column(length = 64)
    private String name;

    @Column(length = 64)
    private String nameZh;

	public String getNameZh() {
		return this.nameZh;
	}

	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}
        
	@Column(name = "YC_DESC", length = 128)
	private String desc;// 描述


	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "ROLE_MENU", //
	joinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") }, //
	inverseJoinColumns = { @JoinColumn(name = "MENU_ID", referencedColumnName = "ID") })
	private List<Menu> menus = new ArrayList<Menu>();// 可访问的角色列表

	public List<Menu> getMenus() {
		return this.menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public void addMenu(Menu menu)
	{
		menus.add(menu);
	}

	public void removeMenu(Menu menu)
	{
		menus.remove( menu );
	}

	public Role() {
	}

	public Role(String name, String nameZh)
	{
		this.name = name;
		this.nameZh = nameZh;
	}

	public Role(Long id, String name, String nameZh)
	{
		this.id = id;
		this.name = name;
		this.nameZh = nameZh;
	}

	public String getName() {
		return name;
	}

	public void setName(String roleName) {
		this.name = roleName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String roleDesc) {
		this.desc = roleDesc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Role))
			return false;
		else {
			return getId().equals(((Role) obj).getId());
		}
	}

	@Override
	public String toString() {
		return "[Role] " + getId() + ":" + name;
	}

}