package com.ynet.imis.service.menu;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.ynet.imis.domain.menu.Role;
import com.ynet.imis.domain.menu.User;
import com.ynet.imis.repository.menu.RoleRepository;
import com.ynet.imis.repository.menu.UserRepository;
import com.ynet.imis.utils.ImisUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

        User aUser = userDao.findByUserName(user.getUserName());
        if (aUser != null) {
            logger.error("Add user error, user name exist! " + ImisUtils.objectJsonStr(user));
            return null;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPass = encoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        return userDao.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userDao.save(user);
    }

    @Override
    public int updateUserRoles(Long userId, Long[] roleIds) {

        logger.info("Update user roles: " + userId);
        User user = userDao.getOne(userId);
        if (user == null)
            return 0;

        logger.info("found the user: " + user);

        List<Long> ids = new ArrayList<Long>();
        for (int i = 0; i < roleIds.length; i++)
            ids.add(roleIds[i]);
        List<Role> roles = roleDao.findAllById(ids);

        logger.info("get the roles: " + roles);

        user.setRoles(roles);
        userDao.save(user);

        return 1;

    }

    @Override
    public User getUserById(Long userId) {
        return userDao.findUserById(userId);
    }

    @Override
    public int deleteUser(Long userId) {
        userDao.deleteById(userId);
        return 1;
    }

    @Override
    public List<User> getAllUser() {
        return userDao.findAll();
    }

}