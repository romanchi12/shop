package org.romanchi.services;

import org.romanchi.Wired;
import org.romanchi.database.dao.*;
import org.romanchi.database.entities.*;

import java.util.Optional;

public class UserService {

    @Wired
    UserDao userDao;

    @Wired
    UserRoleDao userRoleDao;

    public Optional<User> login(String email, String password) {
        Optional<User> userOptional = userDao.findByEmailAndPassword(email,password);
        return userOptional;
    }

    public UserRole getUserRoleByName(String aDefault) {
        Optional<UserRole> userRole = userRoleDao.findByUserRoleName(aDefault);
        if(userRole.isPresent()){
            return userRole.get();
        }else{
            return null;
        }
    }

    public long saveUser(User user) {
        long userId = userDao.save(user);
        return userId;
    }
}