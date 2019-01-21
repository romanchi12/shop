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
    private
    UserDao userDao;

    @Wired
    private
    UserRoleDao userRoleDao;

    @Wired
    private
    EmailValidator emailValidator;

    @Wired
    private
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
        return userRole.orElse(null);
    }

    public long saveUser(User user) {
        return userDao.save(user);
    }
}