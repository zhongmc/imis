package com.ynet.imis.controller;

import java.util.List;

import com.ynet.imis.domain.menu.User;
import com.ynet.imis.domain.menu.User.UserStatus;
import com.ynet.imis.service.menu.UserService;
import com.ynet.imis.utils.ImisUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/id/{userId}", method = RequestMethod.GET)
    public User getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @RequestMapping(value = "/id/{userId}", method = RequestMethod.DELETE)
    public ResponseBean deleteHr(@PathVariable Long userId) {
        if (userService.deleteUser(userId) == 1) {
            return new ResponseBean("success", "删除成功!");
        }
        return new ResponseBean("error", "删除失败!");
    }

    @RequestMapping(value = "/active", method = RequestMethod.PUT)
    public ResponseBean updateUser(Long id, UserStatus status) {

        logger.info("active/deactive User:" + id + " to " + status);

        User aUser = userService.activeUser(id, status);
        if (aUser != null) {
            return new ResponseBean("success", "更新成功!");
        }
        return new ResponseBean("error", "更新失败!");
    }

    @RequestMapping(value = "/roles", method = RequestMethod.PUT)
    public ResponseBean updateUserRoles(Long userId, Long[] rids) {
        logger.info("update user " + userId + " 's roles: '" + ImisUtils.objectJsonStr(rids));
        if (userService.updateUserRoles(userId, rids) == 1) {
            return new ResponseBean("success", "更新成功!");
        }
        return new ResponseBean("error", "更新失败!");
    }

    @RequestMapping(value = "/resetpassword", method = RequestMethod.POST) // reset user's password
    public ResponseBean resetUserPassword(long id, String username, String password, String rePassword) {
        logger.info("reset user " + username + "'s password to: " + password);
        int ret = userService.resetPassword(id, password); // , newPassword)
        if (ret == 1) {
            return new ResponseBean("success", "重置密码成功！");
        } else
            return new ResponseBean("success", "重置密码失败！" + ret);

    }

    @RequestMapping("/search/{keywords}")
    public List<User> getUsersByKeywords(@PathVariable(required = false) String keywords) {
        List<User> hrs = userService.getAllUser();
        return hrs;
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public ResponseBean addUser(User user) {
        logger.info("add user: " + ImisUtils.objectJsonStr(user));
        user.setId(null);
        User aUser = userService.addUser(user);
        if (aUser != null) {
            return new ResponseBean("success", "注册成功!", aUser);
        } else {
            return new ResponseBean("error", "注册失败!");

        }
    }

}