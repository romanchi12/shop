package org.romanchi.database.dao;

import org.romanchi.Bean;
import org.romanchi.database.dao.mysql.ProductDaoImpl;

public class ProductDaoFactory {
    @Bean
    public static ProductDao create(){ //C
        return new ProductDaoImpl(); //B
    }
}
