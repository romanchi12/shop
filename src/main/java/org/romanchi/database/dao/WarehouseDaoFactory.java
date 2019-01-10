package org.romanchi.database.dao;

import org.romanchi.Bean;
import org.romanchi.database.dao.mysql.WarehouseDaoImpl;

public class WarehouseDaoFactory {
    @Bean
    public static WarehouseDao create(){ //C
        return new WarehouseDaoImpl(); //B
    }
}
