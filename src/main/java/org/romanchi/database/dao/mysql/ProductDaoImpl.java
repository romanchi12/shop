package org.romanchi.database.dao.mysql;

import org.romanchi.Wired;
import org.romanchi.database.dao.ProductDao;
import org.romanchi.database.entities.Category;
import org.romanchi.database.entities.Product;
import org.romanchi.database.entities.WarehouseItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Logger;

import static org.romanchi.database.Column.*;
import static org.romanchi.database.Table.*;

public class ProductDaoImpl implements ProductDao {
    private final static Logger logger = Logger.getLogger(ProductDaoImpl.class.getName());

    private final String SQL_SELECT = "SELECT " +
            PRODUCT_PRODUCT_ID + ", " +
            PRODUCT_WAREHOUSEITEM_ID + ", " +
            PRODUCT_CATEGORY_ID + ", " +
            PRODUCT_PRODUCT_NAME + ", " +
            PRODUCT_PRODUCT_DESCRIPTION + ", " +
            PRODUCT_PRODUCT_PRICE + ", " +
            PRODUCT_PRODUCT_IMAGESRC + ", " +
            CATEGORY_CATEGORYNAME + "," +
            WAREHOUSE_WAREHOUSE_ITEM_QUANTITY + " " +
            " FROM " + PRODUCT_TABLE +
            " LEFT JOIN " + CATEGORY_TABLE + " on " +
            PRODUCT_CATEGORY_ID + "=" + CATEGORY_CATEGORY_ID +
            " LEFT JOIN " + WAREHOUSE_TABLE + " on " +
            PRODUCT_WAREHOUSEITEM_ID + "=" + WAREHOUSE_WAREHOUSEITEM_ID;

    private final String SQL_INSERT = "INSERT INTO " + PRODUCT_TABLE +
            "(" + PRODUCT_WAREHOUSEITEM_ID + ", " +
            PRODUCT_CATEGORY_ID + ", " +
            PRODUCT_PRODUCT_NAME + ", " +
            PRODUCT_PRODUCT_DESCRIPTION + ", " +
            PRODUCT_PRODUCT_PRICE + ", " +
            PRODUCT_PRODUCT_IMAGESRC + ") VALUES(?,?,?,?,?,?)";

    private final String SQL_UPDATE = "UPDATE " + PRODUCT_TABLE + " SET " +
            PRODUCT_WAREHOUSEITEM_ID + "=?, " +
            PRODUCT_CATEGORY_ID + "=?, " +
            PRODUCT_PRODUCT_NAME + "=?, " +
            PRODUCT_PRODUCT_DESCRIPTION + "=?, " +
            PRODUCT_PRODUCT_PRICE + "=?, " +
            PRODUCT_PRODUCT_IMAGESRC + "=? WHERE " + PRODUCT_PRODUCT_ID + "=?";

    private final String SQL_COUNT = "SELECT COUNT(*) FROM " + PRODUCT_TABLE;

    private final String SQL_DELETE = "DELETE FROM " + PRODUCT_TABLE + " WHERE " + PRODUCT_PRODUCT_ID + "=?";

    @Wired
    private DataSource dataSource; //a

    public ProductDaoImpl() {
        logger.info(SQL_SELECT);
    }

    @Override
    public Iterable<Product> findAll() {
        return findByDynamicSelect(SQL_SELECT + " ORDER BY " + PRODUCT_PRODUCT_ID, null);
    }

    @Override
    public Optional<Product> findById(long productId) {
        return findSingleByDynamicSelect(SQL_SELECT + " WHERE " + PRODUCT_PRODUCT_ID + "=? ORDER BY " + PRODUCT_PRODUCT_ID, new Object[]{productId});
    }

    @Override
    public boolean existsById(long productId) {
        Optional<Product> product = findById(productId);
        return product.isPresent();
    }

    @Override
    public Iterable<Product> findAllByCategoryId(long categoryId) {
        return findByDynamicSelect(SQL_SELECT + " WHERE " + PRODUCT_CATEGORY_ID +"=? ORDER BY " + PRODUCT_PRODUCT_ID, new Object[]{categoryId});
    }

    @Override
    public Iterable<Product> findAllByWarehouseItemId(long waregouseItemId) {
        return findByDynamicSelect(SQL_SELECT + " WHERE " + PRODUCT_WAREHOUSEITEM_ID +"=? ORDER BY " + PRODUCT_PRODUCT_ID, new Object[]{waregouseItemId});
    }

    @Override
    public Iterable<Product> findAllPriceBetween(double priceLow, double priceHigh) {
        return findByDynamicSelect(SQL_SELECT + " WHERE " + PRODUCT_PRODUCT_PRICE +" BETWEEN ? AND ? ORDER BY " + PRODUCT_PRODUCT_ID, new Object[]{priceLow,priceHigh});
    }


    /*SQL_INSERT = "INSERT INTO " + PRODUCT_TABLE +
            "(" + PRODUCT_WAREHOUSEITEM_ID + ", " +
    PRODUCT_CATEGORY_ID + ", " +
    PRODUCT_PRODUCT_NAME + ", " +
    PRODUCT_PRODUCT_DESCRIPTION + ", " +
    PRODUCT_PRODUCT_PRICE + ", " +
    PRODUCT_PRODUCT_IMAGESRC + ") VALUES(?,?,?,?,?,?)";*/
    @Override
    public long save(Product newProduct) {
        if(existsById(newProduct.getProductId())){
            return update(newProduct);
        }
        Connection connection = null;
        long insertedId = -1;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, newProduct.getWarehouseItem().getWarehouseItemId());
            preparedStatement.setLong(2, newProduct.getCategory().getCategoryId());
            preparedStatement.setString(3, newProduct.getProductName());
            preparedStatement.setString(4, newProduct.getProductDescription());
            preparedStatement.setDouble(5, newProduct.getProductPrice());
            preparedStatement.setString(6, newProduct.getProductImgSrc());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                insertedId = resultSet.getLong(1);
            }
            newProduct.setProductId(insertedId);
            connection.commit();
            return insertedId;
        } catch (SQLException exception) {
            exception.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException exception2) {
                    exception2.printStackTrace();
                }
            }
            return insertedId;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /*SQL_UPDATE = "UPDATE " + PRODUCT_TABLE + " SET " +
    PRODUCT_WAREHOUSEITEM_ID + "=?, " +
    PRODUCT_CATEGORY_ID + "=?, " +
    PRODUCT_PRODUCT_NAME + "=?, " +
    PRODUCT_PRODUCT_DESCRIPTION + "=?, " +
    PRODUCT_PRODUCT_PRICE + "=?, " +
    PRODUCT_PRODUCT_IMAGESRC + "=? WHERE " + PRODUCT_PRODUCT_ID + "=?";*/
    private long update(Product newProduct) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE);
            preparedStatement.setLong(1, newProduct.getWarehouseItem().getWarehouseItemId());
            preparedStatement.setLong(2, newProduct.getCategory().getCategoryId());
            preparedStatement.setString(3, newProduct.getProductName());
            preparedStatement.setString(4, newProduct.getProductDescription());
            preparedStatement.setDouble(5, newProduct.getProductPrice());
            preparedStatement.setString(6, newProduct.getProductImgSrc());
            preparedStatement.setLong(7,newProduct.getProductId());
            int affectedRows = preparedStatement.executeUpdate();
            connection.commit();
            return affectedRows;
        } catch (SQLException exception) {
            exception.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
            return -1;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public long count() {
        Connection connection = null;
        try{
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_COUNT);
            ResultSet set = statement.executeQuery();
            set.next();
            return set.getLong(1);
        }catch (SQLException e){
            e.printStackTrace();
            return -1;
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void delete(Product productToDelete)  {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
                statement.setLong(1, productToDelete.getProductId());
                int affectedRows = statement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
                connection.rollback();
                connection.setAutoCommit(true);
            }
            connection.commit();
            connection.setAutoCommit(true);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public Optional<Product> findSingleByDynamicSelect(String SQL, Object[] params) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection connection = dataSource.getConnection()) {
            preparedStatement = connection.prepareStatement(SQL);
            if (params != null) {
                for (int i = 1; i <= params.length; i++) {
                    preparedStatement.setObject(i, params[i - 1]);
                }
            }
            resultSet = preparedStatement.executeQuery();
            return fetchSingleResult(resultSet);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return Optional.empty();
        }
    }

    public Iterable<Product> findByDynamicSelect(String sql, Object[] params) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection connection = dataSource.getConnection()) {
            preparedStatement = connection.prepareStatement(sql);
            if (params != null) {
                for (int i = 1; i <= params.length; i++) {
                    preparedStatement.setObject(i, params[i - 1]);
                }
            }
            resultSet = preparedStatement.executeQuery();

            return fetchMultiResults(resultSet);
        } catch (SQLException exception) {
            //logger.error(ex, ex);
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }

    private Iterable<Product> fetchMultiResults(ResultSet resultSet) {
        Collection<Product> products = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Product entity = new Product();
                populateEntity(entity, resultSet);
                products.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    private Optional<Product> fetchSingleResult(ResultSet resultSet) {

        try {
            if (resultSet.next()) {
                Product entity = new Product();
                populateEntity(entity, resultSet);
                return Optional.ofNullable(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private void populateEntity(Product entity, ResultSet resultSet) {
        try {
            entity.setProductId(resultSet.getLong(PRODUCT_PRODUCT_ID));
            WarehouseItem warehouseItem = new WarehouseItem();
                warehouseItem.setWarehouseItemId(resultSet.getLong(PRODUCT_WAREHOUSEITEM_ID));
                warehouseItem.setWarehouseItemQuantity(resultSet.getDouble(WAREHOUSE_WAREHOUSE_ITEM_QUANTITY));
            entity.setWarehouseItem(warehouseItem);
            Category category = new Category();
                category.setCategoryId(resultSet.getLong(PRODUCT_CATEGORY_ID));
                category.setCategoryName(resultSet.getString(CATEGORY_CATEGORYNAME));
            entity.setCategory(category);

            entity.setProductName(resultSet.getString(PRODUCT_PRODUCT_NAME));
            entity.setProductDescription(resultSet.getString(PRODUCT_PRODUCT_DESCRIPTION));
            entity.setProductPrice(resultSet.getDouble(PRODUCT_PRODUCT_PRICE));
            entity.setProductImgSrc(resultSet.getString(PRODUCT_PRODUCT_IMAGESRC));
        } catch (SQLException e) {
            e.printStackTrace();
            entity = null;
        }
    }




    public String getTableName() {
        return "Product";
    }


}
