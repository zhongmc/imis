package com.ynet.imis.imisserver.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.ynet.imis.domain.menu.Menu;
import com.ynet.imis.domain.menu.Role;
import com.ynet.imis.domain.menu.User;
import com.ynet.imis.domain.org.Department;
import com.ynet.imis.imisserver.ImisApplicationTests;
import com.ynet.imis.service.menu.MenuService;
import com.ynet.imis.service.menu.RoleService;
import com.ynet.imis.service.menu.UserService;
import com.ynet.imis.service.org.DepartmentService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRoleTest extends ImisApplicationTests {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    DepartmentService depService;

    @Autowired
    MenuService menuService;

    public void whenUpdateUserRolesSuccess() throws Exception {

        System.out.println("..........insert new role ..............");
        Role role1 = new Role("ROLE_sys1", "ROLE_Sys1");
        Role role2 = new Role("ROLE_sys2", "ROLE_Sys2");

        role1 = roleService.addRole(role1);
        role2 = roleService.addRole(role2);
        outputObject(role1);
        outputObject(role2);

        System.out.println(".............find the root department.......");
        List<Department> departments = depService.listAllDepartmentByPid(null);

        outputObject(departments);

        Long[] ids = new Long[2];
        ids[0] = role1.getId();
        ids[1] = role2.getId();

        System.out.println("...............add new user user1 ...........");
        User user = new User();
        user.setUserName("user1");
        user.setDepartment(departments.get(0));

        User aUser = userService.addUser(user);

        aUser.addRole(role1);

        userService.updateUser(aUser);

        outputObject(aUser);

        System.out.println("...........update user role ...... ");
        userService.updateUserRoles(aUser.getId(), ids);

        User buser = userService.getUserById(aUser.getId());
        outputObject(buser);

    }

    @Test
    public void whenGetRoleMenuSuccess() throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<Menu> menus = menuService.menuTree();

        outputObject(menus);

        // map.put("menus", menus);
        // Long rid = new Long(32);
        // Role role = roleService.getRoleById( rid);
        // List<Menu> rMs = role.getMenus();
        // List<Long> selMids = new ArrayList<Long>();

        // for(Menu menu:rMs )
        // selMids.add( menu.getId());
        // map.put("mids", selMids);

        // outputObject( map );
    }
}