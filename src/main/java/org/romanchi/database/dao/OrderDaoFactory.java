package org.romanchi.database.dao;

import org.romanchi.Bean;
import org.romanchi.database.dao.mysql.OrderDaoImpl;

public class OrderDaoFactory {
    @Bean
    public static OrderDao create(){ //C
        return new OrderDaoImpl(); //B
    }
}
