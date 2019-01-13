package org.romanchi.database.dao;

import org.romanchi.database.entities.WarehouseItem;

import java.util.Optional;

public interface WarehouseItemDao {
    long save(WarehouseItem categoryToInsert);

    Optional<WarehouseItem> findById(long warehouseItemId);

    Iterable<WarehouseItem> findAll();

    long count();

    void delete(WarehouseItem warehouseItemToDelete);

    boolean existsById(long warehouseItemId);

}
