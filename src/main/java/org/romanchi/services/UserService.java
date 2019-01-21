package org.romanchi.services;

import org.romanchi.Wired;
import org.romanchi.database.dao.*;
import org.romanchi.database.entities.*;
import org.romanchi.validators.EmailValidator;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class UserService {

    @Wired
    UserDao userDao;

    @Wired
    UserRoleDao userRoleDao;

    @Wired
    EmailValidator emailValidator;

    @Wired
    Logger logger;

    public Optional<User> login(String email, String password) {
        if(!emailValidator.validate(email)){
            return Optional.empty();
        }
        return userDao.findByEmailAndPassword(email,password);
    }

    public Optional<User> register(User user){
        if(!emailValidator.validate(user.getUserEmail())){
            return Optional.empty();
        }

        Optional<User> userOptional = userDao.
                findByEmailAndPassword(user.getUserEmail(),user.getUserPassword());
        if(userOptional.isPresent()){
            return Optional.empty();
        }
        long userId = saveUser(user);
        logger.info(String.valueOf(userId));
        user.setUserId(userId);
        return Optional.of(user);
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
        return userDao.save(user);
    }
}