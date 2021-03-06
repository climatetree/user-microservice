package com.climatetree.user.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.climatetree.user.dao.RoleDao;
import com.climatetree.user.dao.UserDao;
import com.climatetree.user.enums.Constants;
import com.climatetree.user.model.User;

@Service
public class JwtUserDetailsService {

  @Autowired
  UserDao userDao;
  
  @Autowired
  RoleDao roleDao;

  public User loadUserByUsername(String username, String email) {
    User user = userDao.findByEmail(email);
    if (user == null) {
      user = this.saveUserDetails(username,email);
      userDao.save(user);
    }
    return new User(user);
  }

  public User saveUserDetails(String username,String email){
    User user = new User();
    user.setNickname(username);
    user.setEmail(email);
    //just setting advanced user for now
    user.setBlacklisted(false);
    user.setRole(roleDao.findByName(Constants.REGISTERED_USERS.name()));
    user.setLastLoginLocation(Constants.LAST_LOGIN.getStatusCode());
    user.setLastLoginTime(new Date());
    user.setRegistrationDate(new Date());
    return user;
  }
}