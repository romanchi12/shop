package org.romanchi.services;

import org.romanchi.Wired;
import org.romanchi.database.dao.TestDao;
import org.romanchi.database.dao.UserDao;
import org.romanchi.database.dao.UserRoleDao;
import org.romanchi.database.entities.User;
import org.romanchi.database.entities.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class UserService {

    @Wired
    UserDao userDao;

    @Wired
    UserRoleDao userRoleDao;


    public String getData(){
        //deleteUserRole();
        return "user service: " + userRoleDao.findById(1);
    }
    public void deleteUserRole(){
        userRoleDao.delete(userRoleDao.findById(4).get());
    }
    public long newUserRole(){
        UserRole userRole = new UserRole();
        userRole.setUserRoleName("fucked");
        return userRoleDao.save(userRole);
    }
    public long updateUserRole(){
        UserRole userRole = userRoleDao.findById(2).orElseGet(()->{
            UserRole def = new UserRole();
            def.setUserRoleId(2);
            def.setUserRoleName("Defult");
            return def;
        });
        userRole.setUserRoleName("idi nahui");
        return userRoleDao.save(userRole);
    }

    private List<UserRole> getAllUserRoles() {
        List<UserRole> userRoles = new ArrayList<>();
        userRoleDao.findAll().forEach(userRole -> {userRoles.add(userRole);});
        return userRoles;
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
    public void updateUser(){
        User user = userDao.findById(21).orElseGet(()->{
            return new User("Default","Default","Default","Default",new UserRole(1,"admin"));
        });
        user.setUserId(1);
        user.setUserEmail("fucked@gmail.com");
        userDao.save(user);
    }
    public Optional<User> findByEmailAndPassword(){
        Optional<User> optionalUser = userDao.findByEmailAndPassword("fucked@gmail.com", "frdfhtkm12");
        return optionalUser;
    }

}