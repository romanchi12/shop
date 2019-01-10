/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.romanchi.database.dao;

import org.romanchi.database.entities.User;

import java.util.Optional;

/**
 * @author Роман
 */
public interface UserDao {
    long save(User userToInsert);

    Optional<User> findById(long userId);

    Iterable<User> findAll();

    long update(User userToUpdate);    // todo ????? is it needed?

    long count();

    void delete(User userToDelete);

    boolean existsById(long userId);

    Optional<User> findByEmailAndPassword(String userEmail, String userPassword);
}
