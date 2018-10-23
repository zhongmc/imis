package com.ynet.imis.service.menu;

import java.util.List;

import com.ynet.imis.domain.menu.User;

public interface UserService{

    public User addUser(User user);
    
    public User updateUser( User user );
    public void updateUserRoles(Long userId, Long[] roleIds );
    public User getUserById(Long userId );
    public void deleteUser(Long userId );
    public List<User> getAllUser();
}
