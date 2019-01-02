/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.romanchi.database.dao;

import org.romanchi.database.entities.User;

/**
 *
 * @author Роман
 */
public interface UserDao {
    public User[] findAll() throws Exception;
    public User findWhereUserIdEquals(long userId) throws Exception;
    public int update(User newUser) throws Exception;
    public long insert(User newUser) throws Exception;
    public int delete(User userToDelete) throws Exception;
}
