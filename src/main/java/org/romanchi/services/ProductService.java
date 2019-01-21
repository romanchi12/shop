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
    private
    ProductDao productDao;

    @Wired
    private
    WarehouseItemDao warehouseItemDao;

    @Wired
    private
    CategoryDao categoryDao;

    @Wired
    private
    Logger logger;


    public Product getProductById(long productId) {
        Optional<Product> product = productDao.findById(productId);
        return product.orElse(null);
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
            productDao.delete(product);
            warehouseItemDao.delete(product.getWarehouseItem());
        });
    }

    public List<ProductDTO> getProductDTOs(Integer page, String sort) {
        int from = 0;
        if(page!=null){
            from = page*10;
        }
        Iterable<Product> productIterable = productDao.findTenFrom(from, sort);

        return processProductIterable(productIterable);
    }
    public List<ProductDTO> getProductDTOs(Integer page, Long categoryId, String sort) {
        int from = 0;
        if(page!=null){
            from = page*10;
        }
        Iterable<Product> productIterable = productDao.findTenByCategoryIdFrom(from, categoryId, sort);

        return processProductIterable(productIterable);
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
        return categoryOptional.orElse(null);
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
        categoryIterable.forEach(categoryList::add);
        return categoryList;
    }
    public List<Category> getAllCategories() {
        Iterable<Category> categoryIterable = categoryDao.findAll();
        List<Category> categories = new ArrayList<>();

        categoryIterable.forEach(categories::add);

        return categories;
    }

    public long saveWarehouseItem(WarehouseItem warehouseItem){
        return warehouseItemDao.save(warehouseItem);
    }

    public List<Product> search(String query) {
        Iterable<Product> productIterable = productDao.findAllProductNameContains(query);
        List<Product> productList = new ArrayList<>();
        productIterable.forEach(productList::add);
        return productList;
    }
}
