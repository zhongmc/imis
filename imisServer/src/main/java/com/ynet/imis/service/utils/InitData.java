package com.ynet.imis.service.utils;

import java.util.ArrayList;
import java.util.List;

import com.ynet.imis.domain.budget.CostGroup;
import com.ynet.imis.domain.menu.Menu;
import com.ynet.imis.domain.menu.Role;
import com.ynet.imis.domain.menu.User;
import com.ynet.imis.domain.org.Department;

public class InitData {

    private List<User> users = new ArrayList<User>();
    private List<Department> departments = new ArrayList<Department>();
    private List<Menu> menus = new ArrayList<Menu>();
    private List<Role> roles = new ArrayList<Role>();
    private List<CostGroup> costDefs = new ArrayList<CostGroup>();

    public List<User> getUsers() {
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Department> getDepartments() {
        return this.departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Menu> getMenus() {
        return this.menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public List<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<CostGroup> getCostDefs() {
        return this.costDefs;
    }

    public void setCostDefs(List<CostGroup> costDefs) {
        this.costDefs = costDefs;
    }

}