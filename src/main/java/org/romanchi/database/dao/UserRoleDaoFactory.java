package org.romanchi.database.dao;

import org.romanchi.Bean;
import org.romanchi.database.dao.mysql.UserRoleDaoImpl;
import org.romanchi.database.entities.UserRole;

public class UserRoleDaoFactory {
    @Bean
    public static UserRoleDao create(){ //C
        return new UserRoleDaoImpl(); //B
    }
}
