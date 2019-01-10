package org.romanchi.database.dao;

import org.romanchi.database.entities.User;
import org.romanchi.database.entities.UserRole;

import java.util.Optional;

public interface UserRoleDao {
    long save(UserRole userRoleToInsert);

    Optional<UserRole> findById(long userRoleId);

    Iterable<UserRole> findAll();

    long count();

    void delete(UserRole userRoleToDelete);

    boolean existsById(long userRoleId);

    Optional<UserRole> findByUserRoleName(String userRoleName);
}
