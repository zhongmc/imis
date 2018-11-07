package com.ynet.imis.service.menu;

import java.util.List;

import com.ynet.imis.domain.menu.User;
import com.ynet.imis.domain.menu.User.UserStatus;

public interface UserService {

    public User addUser(User user);

    public User updateUser(User user);

    public User activeUser(Long id, UserStatus status);

    public int updateUserRoles(Long userId, Long[] roleIds);

    public User getUserById(Long userId);

    public int deleteUser(Long userId);

    public List<User> getAllUser();

    public boolean verifyUser(String userName, String password);
}
