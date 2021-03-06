package org.romanchi.services;

import org.romanchi.Wired;
import org.romanchi.database.dao.CategoryDao;
import org.romanchi.database.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    @Wired
    private
    CategoryDao categoryDao;

    public List<Category> getAllCategories(){
        Iterable<Category> categoryIterable = categoryDao.findAll();
        List<Category> categoryList = new ArrayList<>();
        categoryIterable.forEach(categoryList::add);
        return categoryList;
    }
}
