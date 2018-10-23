/**
* MenuRepositoryTest.java
* @author ZHONGMC
* @description 
* @created Tue Sep 25 2018 10:37:07 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Thu Sep 27 2018 13:31:08 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.imisserver.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.ynet.imis.domain.menu.Menu;
import com.ynet.imis.imisserver.ImisApplicationTests;
import com.ynet.imis.repository.menu.MenuRepository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuRepositoryTest extends ImisApplicationTests {
    @Autowired
    private MenuRepository menuDao;


    @Test
    @Transactional
    public void whenLoadMenuTreeSuccess()throws Exception
    {
        System.out.println("=======================Menu tree ===============");        
        List<Menu> menus = menuDao.listRootMenu();
        outputObject( menus );
    }

    @Test
    @Transactional
    public void whenFindAllMenuSuccess() throws Exception
    {
        System.out.println("*************All menu list ****");
        Iterable<Menu> menus = menuDao.findAll(); // listAllMenu();
        outputObject( menus );

    }

}