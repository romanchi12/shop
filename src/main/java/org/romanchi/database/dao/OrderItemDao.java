package org.romanchi.database.dao;

import org.romanchi.database.entities.Order;
import org.romanchi.database.entities.OrderItem;

import java.util.Optional;

public interface OrderItemDao {
    void save(OrderItem orderItemToInsert);

    Optional<OrderItem> findByOrderIdAndProductId(long orderId, long productId);

    Iterable<OrderItem> findAll();

    long count();

    void delete(OrderItem orderItemToDelete);

    boolean existsByOrderIdAndProductId(long orderId, long productId);

    Iterable<OrderItem> findAllByOrderId(long orderId);

}
