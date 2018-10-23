package com.ynet.imis.imisserver.service;

import com.ynet.imis.imisserver.ImisApplicationTests;
import com.ynet.imis.service.menu.UserDetailService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;


public class UserDetailServiceTest extends ImisApplicationTests{

    @Autowired
    UserDetailService userService;

    @Test
    public void whenLoadUserByUsernameSuccess()throws Exception
    {
        System.out.println("****************User info of: admin:*******************");
        UserDetails user = userService.loadUserByUsername("admin");
        super.outputObject( user );

    }

}