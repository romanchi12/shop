package org.romanchi.services;

import org.romanchi.Wired;
import org.romanchi.database.dao.TestDao;

public class TestService {

    @Wired
    TestDao testDao;

    public String getData(){
        return "service: " + testDao.getData();
    }
}
