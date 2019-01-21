package org.romanchi.database.dao;

import org.romanchi.database.entities.Product;

import java.util.Optional;

public interface ProductDao {
    long save(Product productToInsert);

    Optional<Product> findById(long productId);

    Iterable<Product> findAll();

    long count();

    void delete(Product productToDelete);

    boolean existsById(long productId);

    Iterable<Product> findAllByCategoryId(long categoryId);

    Iterable<Product> findAllByWarehouseItemId(long waregouseItemId);

    Iterable<Product> findAllPriceBetween(double priceLow, double priceHigh);

    Iterable<Product> findTenFrom(int from, String sort);

    Iterable<Product> findTenByCategoryIdFrom(int from, long categoryId, String sort);

    Iterable<Product> findAllProductNameContains(String query);
}
