/**
* Menu.java
* @author ZHONGMC
* @description 
* @created Wed Sep 12 2018 15:18:00 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Tue Oct 09 2018 14:25:06 GMT+0800 (中国标准时间)
*/



package com.ynet.imis.domain.menu;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.ynet.imis.domain.TreeEntity;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import java.util.List;

@Entity
@Table(name="MENU")
public class Menu extends TreeEntity<Menu> {

	private static final long serialVersionUID = 2923001923097575286L;

    @Column(length = 128)
    private String url;

	@Column(length = 128)
    private String path;

	@Column(length = 64)
    private String component;

	@Column(length = 64)
    private String name;

    private String iconCls;

    private boolean hidden = false;

    public Menu()
    {

    }
    
    public Menu(Long id, Long parentId, String name, String path, String component, boolean hidden)
    {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.path = path;
        this.component = component;
        this.hidden = hidden;
    }


    public Menu cloneWithoutChilds()
    {
        Menu menu = new Menu();
        menu.id = this.id;
        menu.parentId = parentId;
        menu.name = name;
        menu.url = url;
        menu.path = path;
        menu.roles = roles;
        menu.component = component;
        menu.hidden = hidden;

        return menu;

    }

    public boolean getHidden() {
        return this.hidden;
    }

    public void isHidden(boolean hidden) {
        this.hidden = hidden;
    }


	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "ROLE_MENU", //
	joinColumns = { @JoinColumn(name = "MENU_ID", referencedColumnName = "ID") }, //
	inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") })
	private List<Role> roles = new java.util.ArrayList<Role>();// 可访问的角色列表
    
   
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
