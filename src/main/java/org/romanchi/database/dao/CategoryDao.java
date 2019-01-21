package org.romanchi.database.dao;

import org.romanchi.database.entities.Category;
import org.romanchi.database.entities.UserRole;

import java.util.Optional;

public interface CategoryDao {
    long save(Category categoryToInsert);

    Optional<Category> findById(long categoryId);

    Iterable<Category> findAll();

    Iterable<Category> findTenFrom(int from);

    long count();

    void delete(Category categoryToDelete);

    boolean existsById(long categoryId);

    Optional<Category> findByCategoryName(String categoryName);
}
