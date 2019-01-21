package org.romanchi.database.dao;

import org.romanchi.database.entities.Order;

import java.util.Optional;

public interface OrderDao {
    long save(Order orderToInsert);

    Optional<Order> findById(long orderId);

    Iterable<Order> findAll();

    long count();

    void delete(Order orderToDelete);

    boolean existsById(long orderId);

    Iterable<Order> findAllByUserId(long userId);
}
