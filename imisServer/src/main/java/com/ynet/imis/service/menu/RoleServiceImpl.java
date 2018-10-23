package com.ynet.imis.service.menu;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.ynet.imis.domain.menu.Menu;
import com.ynet.imis.domain.menu.Role;
import com.ynet.imis.repository.menu.MenuRepository;
import com.ynet.imis.repository.menu.RoleRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleDao;

    @Autowired
    private MenuRepository menuDao;

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public Role getRoleById(Long id) {
        return roleDao.getOne(id);
    }

    @Override
    public int updateRole(Role role) {

        roleDao.save(role);
        return 1;
    }

    @Override
    public Role addRole(Role role) {
        return roleDao.save(role);
    }

    @Override
    public int deleteRoleById(Long id) {
        roleDao.deleteById(id);
        return 1; // ????????
    }

	@Override
	public List<Role> getAllRoles() {
		return roleDao.findAll();
	}

    @Override
    public int updateRoleMenus(Long rid, Long[]mids)
    {

        logger.info("Update Role menus: " + rid  + "with: " + mids );
        Role role = roleDao.findById( rid ).get();
        if( role == null )
            return 0;

        logger.info("found the Role: " + role );

        List<Long>ids = new ArrayList<Long>();
        for(int i=0; i<mids.length; i++ )
             ids.add(mids[i]);

        Iterable<Menu> ms = menuDao.findAllById(ids);

        List<Menu> menus = new ArrayList<Menu>();
        
        for(Menu m : ms )
            menus.add(m);

        logger.info("get the menus: " + menus );
        role.setMenus( menus);
        roleDao.save( role );

        return 1;
    }

}