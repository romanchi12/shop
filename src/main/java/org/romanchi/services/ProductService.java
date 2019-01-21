package org.romanchi.services;

import org.romanchi.Wired;
import org.romanchi.database.dao.CategoryDao;
import org.romanchi.database.dao.ProductDao;
import org.romanchi.database.dao.WarehouseItemDao;
import org.romanchi.database.dto.ProductDTO;
import org.romanchi.database.entities.Category;
import org.romanchi.database.entities.Product;
import org.romanchi.database.entities.WarehouseItem;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class ProductService {
    @Wired
    ProductDao productDao;

    @Wired
    WarehouseItemDao warehouseItemDao;

    @Wired
    CategoryDao categoryDao;

    @Wired
    Logger logger;


    public Product getProductById(long productId) {
        Optional<Product> product = productDao.findById(productId);
        if(product.isPresent()){
            return product.get();
        }
        return null;
    }
    public long saveProduct(Product product) {
        return productDao.save(product);
    }
    public void deleteProduct(Product product) {
        Optional<Product> productFromDBOptional = productDao.findById(product.getProductId());
        if(productFromDBOptional.isPresent()){
            Product productFromDB = productFromDBOptional.get();
            WarehouseItem warehouseItem = productFromDB.getWarehouseItem();
            productDao.delete(productFromDB);
            warehouseItemDao.delete(warehouseItem);
        }
    }
    public void deleteAllProductsByCategory(Category category) {
        Iterable<Product> products = productDao.findAllByCategoryId(category.getCategoryId());
        products.forEach(product -> {
            logger.info(product.toString());
            productDao.delete(product);
            warehouseItemDao.delete(product.getWarehouseItem());
        });
    }

    public List<ProductDTO> getProductDTOs(Integer page, String sort) {
        int from = 0;
        if(page==null){
            from = 0;
        }else{
            from = page*10;
        }
        Iterable<Product> productIterable = productDao.findTenFrom(from, sort);
        List<ProductDTO> productList = processProductIterable(productIterable);

        return productList;
    }
    public List<ProductDTO> getProductDTOs(Integer page, Long categoryId, String sort) {
        int from = 0;
        if(page==null){
            from = 0;
        }else{
            from = page*10;
        }
        Iterable<Product> productIterable = productDao.findTenByCategoryIdFrom(from, categoryId, sort);

        List<ProductDTO> productList = processProductIterable(productIterable);

        return productList;
    }
    private List<ProductDTO> processProductIterable(Iterable<Product> productIterable){
        List<ProductDTO> productList = new ArrayList<>();
        productIterable.forEach(product -> {
            logger.info(product.toString());
            WarehouseItem warehouseItem = warehouseItemDao.findById(product.getWarehouseItem().getWarehouseItemId()).orElseGet(()->{
                WarehouseItem warehouseItem1 = new WarehouseItem();
                warehouseItem1.setWarehouseItemId(product.getWarehouseItem().getWarehouseItemId());
                return warehouseItem1;
            });
            Category category = categoryDao.findById(product.getCategory().getCategoryId()).
                    orElseGet(()->{
                        Category category1 = new Category();
                        category1.setCategoryId(product.getCategory().getCategoryId());
                        return category1;
                    });
            ProductDTO productDTO = new ProductDTO(product.getProductId(),
                    warehouseItem.getWarehouseItemId(),
                    warehouseItem.getWarehouseItemQuantity(),
                    category.getCategoryId(),
                    category.getCategoryName(),
                    product.getProductName(),
                    product.getProductDescription(),
                    product.getProductPrice(),
                    product.getProductImgSrc()
            );
            productList.add(productDTO);
        });
        return productList;
    }

    public Category getCategoryById(Integer categoryId) {
        Optional<Category> categoryOptional = categoryDao.findById(categoryId);
        if(categoryOptional.isPresent()){
            return categoryOptional.get();
        }else{
            return null;
        }
    }
    public long saveCategory(Category category) {
        return categoryDao.save(category);
    }
    public void deleteCategory(Category category) {
        categoryDao.delete(category);
    }
    public List<Category> getCategories(Integer page){
        int from = 0;
        if(page==null){
            from = 0;
        }else{
            from = page*10;
        }
        Iterable<Category> categoryIterable = categoryDao.findTenFrom(from);
        List<Category> categoryList = new ArrayList<>();
        categoryIterable.forEach(category -> {
            categoryList.add(category);
        });
        return categoryList;
    }
    public List<Category> getAllCategories() {
        Iterable<Category> categoryIterable = categoryDao.findAll();
        List<Category> categories = new ArrayList<>();

        categoryIterable.forEach(category -> {
            categories.add(category);
        });

        return categories;
    }

    public long saveWarehouseItem(WarehouseItem warehouseItem){
        long id = warehouseItemDao.save(warehouseItem);
        return id;
    }

    public List<Product> search(String query) {
        Iterable<Product> productIterable = productDao.findAllProductNameContains(query);
        List<Product> productList = new ArrayList<>();
        productIterable.forEach(product -> {
            productList.add(product);
        });
        return productList;
    }
}
