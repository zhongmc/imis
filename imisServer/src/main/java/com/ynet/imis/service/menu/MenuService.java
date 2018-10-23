/**
* MenuService.java
* @author ZHONGMC
* @description 
* @created Sat Sep 22 2018 09:37:26 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Mon Oct 08 2018 10:57:18 GMT+0800 (中国标准时间)
*/



package com.ynet.imis.service.menu;
import com.ynet.imis.domain.menu.Menu;

import java.util.List;


public interface MenuService {

    //树形组织的菜单
    public List<Menu> menuTree();
    //所有菜单列表
    public Iterable<Menu> listAllMenu();

    public List<Menu> myMenuTree();
    public List<Menu> getMenusByUserId(Long id);
    public List<Long> getMenusByRid(Long rid);

    public void updateMenuRoles(Long menuId, Long[] roleIds );    
}
