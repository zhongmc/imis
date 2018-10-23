package com.ynet.imis.service.menu;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.ynet.imis.domain.menu.Role;
import com.ynet.imis.domain.menu.User;
import com.ynet.imis.repository.menu.RoleRepository;
import com.ynet.imis.repository.menu.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userDao;


    @Autowired
    private RoleRepository roleDao;

	private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public User addUser(User user) {
        return userDao.save( user );
    }

    @Override
    public User updateUser(User user) {
        return userDao.save( user );
    }

    @Override
    public void updateUserRoles(Long userId, Long[] roleIds) {
        
        logger.info("Update user roles: " + userId );
        User user = userDao.getOne(userId );

        logger.info("found the user: " + user );

        List<Long>ids = new ArrayList<Long>();
        for(int i=0; i<roleIds.length; i++ )
             ids.add(roleIds[i]);
        List<Role> roles = roleDao.findAllById(ids);
        
        logger.info("get the roles: " + roles );

        user.setRoles( roles );
        userDao.save( user );

    }

    @Override
    public User getUserById(Long userId) {
        return userDao.findUserById( userId );
    }

    @Override
    public void deleteUser(Long userId) {
        userDao.deleteById( userId );
    }

	@Override
	public List<User> getAllUser() {
		return userDao.findAll();
	}


}