package org.romanchi.database.dao;

import org.romanchi.Bean;
import org.romanchi.database.dao.mysql.CategoryDaoImpl;

public class CategoryDaoFactory  {
    @Bean
    public static CategoryDao create(){ //C
        return new CategoryDaoImpl(); //B
    }
}
