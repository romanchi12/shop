package org.romanchi.services;

import org.romanchi.Wired;
import org.romanchi.database.dao.TestDao;
import org.romanchi.database.dao.UserDao;
import org.romanchi.database.entities.User;
import org.romanchi.database.entities.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {

    @Wired
    UserDao userDao;

    public String getData(){
        return "service: " + getUserById(1) + " " + getUsersAmount() + " " + existsById(1) + getUsers() + "<br>" + newUser();
    }
    public long getUsersAmount(){
        return userDao.count();
    }
    public Optional<User> getUserById(long id){
        try {
            Optional<User> user = userDao.findById(id);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean existsById(long id){
        return userDao.existsById(id);
    }
    public List<User> getUsers(){
        Iterable<User> users = userDao.findAll();
        StringBuilder builder = new StringBuilder();
        List<User> userList = new ArrayList<>();
        for(User user:users){
            userList.add(user);
        }
        return userList;
    }

    public long newUser(){
        User user = new User();
        user.setUserUserRole(new UserRole(1,"admin"));
        user.setUserName("Taras");
        user.setUserSurname("Malyarchuk");
        user.setUserPassword("frdfhtkm12");
        user.setUserEmail("tos@gmail.com");
        long insertedId = userDao.save(user);
        return insertedId;
    }

}
   /* public Optional<User> getUserById(long id){
        try {
            Optional<User> user = userDao.findById(id);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public long getUsersAmount(){
        return userDao.count();
    }
    public boolean existById( long userId){
        return userDao.existsById(userId);
    }
    public long newUser(){
        UserRole userRole = new UserRole(1,"admin");
        User newUser = new User("perdun","maluй","perdun222","haha-puk", userRole);
        return userDao.save(newUser);
    }
    public List<User> getUsers(){
        Iterable<User> usersIterable = userDao.findAll();
        List<User> users = new ArrayList<>();
        usersIterable.forEach(user -> {users.add(user);});
        return users;
    }*/



