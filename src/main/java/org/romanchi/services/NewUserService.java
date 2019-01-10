package org.romanchi.services;

import org.romanchi.Wired;
import org.romanchi.database.dao.TestDao;
import org.romanchi.database.dao.UserDao;

public class NewUserService {

    @Wired
    UserDao userDao;

    public String getData(){
        return "service: " + userDao.count();
    }
}
