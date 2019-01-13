package org.romanchi.database.dao;

import org.romanchi.Bean;
import org.romanchi.database.dao.mysql.WarehouseItemDaoImpl;

public class WarehouseItemDaoFactory {
    @Bean
    public static WarehouseItemDao create(){ //C
        return new WarehouseItemDaoImpl(); //B
    }
}
