/**
* UserDetailService.java
* @author ZHONGMC
* @description 
* @created Thu Sep 13 2018 15:21:04 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Wed Nov 14 2018 16:16:28 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.service.security;

import com.ynet.imis.domain.menu.User;
import com.ynet.imis.repository.menu.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userDao;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        logger.info("Load user:" + s + ";");
        User user = userDao.findByUserName(s);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在！");
        }
        return user;
    }

    public User userReg(String username, String password) {
        // 如果用户名存在，返回错误
        if (userDao.findByUserName(username) != null) {
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(password);
        return userDao.save(new User(username, encode));
    }

}
