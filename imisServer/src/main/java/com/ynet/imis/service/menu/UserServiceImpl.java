package com.ynet.imis.service.menu;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.ynet.imis.domain.menu.Role;
import com.ynet.imis.domain.menu.User;
import com.ynet.imis.domain.menu.User.UserStatus;
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
    public User activeUser(Long id, UserStatus status) {
        User user = userDao.findUserById(id);
        if (user == null)
            return null;
        user.setStatus(status);
        userDao.save(user);
        return user;
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

    public int changePassword(User user, String password, String newPassword) {
        logger.info("Change user " + user.getUsername() + "'s password....");
        User aUser = userDao.findByUserName(user.getUsername());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(password, aUser.getPassword())) {
            return 2; // password not valid
        }
        aUser.setPassword(encoder.encode(newPassword));
        userDao.save(aUser);
        return 1;
    }

    public int resetPassword(Long id, String password) {
        User aUser = userDao.findUserById(id);
        if (aUser == null)
            return 2;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        aUser.setPassword(encoder.encode(password));
        userDao.save(aUser);
        return 1;
    }

    @Override
    public boolean verifyUser(String userName, String password) {

        logger.info("verify user:" + userName + ":" + password);
        User user = userDao.findByUserName(userName);
        if (user == null) {
            logger.info("verify failed: user not exist!");
            return false;
        }
        if (!user.isSupervisor() || user.getStatus() != UserStatus.ACTIVE) {
            logger.info("verify failed: user state: " + user.isSupervisor() + " " + user.getStatus());
            return false;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (encoder.matches(password, user.getPassword()))
            return true;

        logger.info("verify failed: password!");
        logger.info(user.getPassword());

        return false;
    }

}