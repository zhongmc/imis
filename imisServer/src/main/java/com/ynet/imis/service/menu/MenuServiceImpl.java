/**
* MenuServiceImpl.java
* @author ZHONGMC
* @description 
* @created Thu Sep 13 2018 16:09:07 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Tue Oct 09 2018 14:03:58 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.service.menu;

import com.ynet.imis.domain.menu.Menu;
import com.ynet.imis.domain.menu.Role;
import com.ynet.imis.repository.menu.MenuRepository;
import com.ynet.imis.repository.menu.RoleRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    MenuRepository menuDao;

    @Autowired
    RoleRepository roleDao;

	private Logger logger = LoggerFactory.getLogger(getClass());


    public List<Menu> getMenusByUserId(Long id) {
        return null;
        //return menuMapper.getMenusByHrId(HrUtils.getCurrentHr().getId());
    }

    public List<Menu> menuTree() {

        List<Menu> rootMenu = menuDao.listRootMenu();

        return rootMenu;
       
        // if( rootMenu == null )
        //     return null;

        //     //假定只有两层menu
        // for( Menu menu : rootMenu)
        // {
        //     List<Menu> childs = menuDao.findChild(menu.getId());
        //     menu.setChilds(childs);
        // }

        // return rootMenu;

    }

        //所有菜单列表

    public Iterable<Menu> listAllMenu(){
        Iterable<Menu> menus = menuDao.findAll();
        List<Menu>retMenus = new ArrayList<Menu>();
        for(Menu menu:menus){
            retMenus.add( menu.cloneWithoutChilds() );
        }
        return retMenus; // listAllMenu();
    }


    public List<Long> getMenusByRid(Long rid) {
        return null;
        //return menuDao.getMenusByRid(rid);
    }

    @Override
    public List<Menu> myMenuTree() {
        return  menuDao.listRootMenu();
    }

    @Override
    public void updateMenuRoles(Long menuId, Long[] roleIds )
    {
        logger.info("Update Menu roles: " + menuId  + "with: " + roleIds );
        Menu menu = menuDao.findById(menuId).get();

        logger.info("found the Menu: " + menu );

        List<Long>ids = new ArrayList<Long>();
        for(int i=0; i<roleIds.length; i++ )
             ids.add(roleIds[i]);
        List<Role> roles = roleDao.findAllById(ids);
        
        logger.info("get the roles: " + roles );

        menu.setRoles( roles );
        menuDao.save( menu );


    }
}
