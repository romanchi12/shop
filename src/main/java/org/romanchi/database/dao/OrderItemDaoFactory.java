package org.romanchi.database.dao;

import org.romanchi.Bean;
import org.romanchi.database.dao.mysql.OrderItemDaoImpl;

public class OrderItemDaoFactory {
    @Bean
    public static OrderItemDao create(){ //C
        return new OrderItemDaoImpl(); //B
    }
}
