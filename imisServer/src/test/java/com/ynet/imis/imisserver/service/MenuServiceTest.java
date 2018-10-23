/**
* MenuServiceTest.java
* @author ZHONGMC
* @description 
* @created Thu Sep 27 2018 13:43:16 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Thu Sep 27 2018 13:50:13 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.imisserver.service;

import java.util.List;

import javax.transaction.Transactional;

import com.ynet.imis.domain.menu.Menu;
import com.ynet.imis.imisserver.ImisApplicationTests;
import com.ynet.imis.service.menu.MenuService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional
public class MenuServiceTest extends ImisApplicationTests{

    @Autowired
    MenuService menuService;


    @Test
    public void whenFindAllMenuSuccess() throws Exception {
        System.out.println("********* All menu list *******");
        Iterable<Menu> menus = menuService.listAllMenu();
        outputObject( menus );

        System.out.println("************ Menu Tree **********");
        List<Menu> menuTree = menuService.menuTree();
        outputObject(menuTree);

    }
}