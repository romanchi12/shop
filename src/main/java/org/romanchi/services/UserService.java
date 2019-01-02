package org.romanchi.services;

import org.romanchi.Wired;
import org.romanchi.database.dao.UserDao;
import org.romanchi.database.entities.User;

public class UserService {

    @Wired
    UserDao userDao;

    public User getUserById(long id){
        try {
            User user = userDao.findWhereUserIdEquals(0);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
