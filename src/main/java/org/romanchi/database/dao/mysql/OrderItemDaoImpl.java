package org.romanchi.database.dao.mysql;

import org.romanchi.Wired;
import org.romanchi.database.dao.OrderDao;
import org.romanchi.database.dao.OrderItemDao;
import org.romanchi.database.entities.*;

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

public class OrderItemDaoImpl implements OrderItemDao {
    private final static Logger logger = Logger.getLogger(OrderDaoImpl.class.getName());

    private final String SQL_SELECT = "SELECT "
            + ORDERITEM_PRODUCT_ID + ", "
            + ORDERITEM_ORDER_ID + ", "
            + ORDERITEM_ORDER_ITEM_QUANTITY + ", "
            + ORDER_USER_ID + ", "
            + ORDER_ORDER_STATUS + ", "
            + ORDER_SUMMARY_PRICE + ", "
            + PRODUCT_WAREHOUSEITEM_ID + ", "
            + PRODUCT_CATEGORY_ID + ", "
            + PRODUCT_PRODUCT_NAME + ", "
            + PRODUCT_PRODUCT_DESCRIPTION + ", "
            + PRODUCT_PRODUCT_PRICE + ", "
            + PRODUCT_PRODUCT_IMAGESRC + " "
            + " FROM " + ORDERITEM_TABLE
            + " LEFT JOIN " + ORDER_TABLE + " on "
            + ORDERITEM_ORDER_ID + "=" + ORDER_ORDER_ID
            + " LEFT JOIN " + PRODUCT_TABLE + " on "
            + ORDERITEM_PRODUCT_ID + "=" + PRODUCT_PRODUCT_ID;

    private final String SQL_INSERT = "INSERT INTO " + ORDERITEM_TABLE +
            "(" + ORDERITEM_PRODUCT_ID + ", " +
            ORDERITEM_ORDER_ID + ", " +
            ORDERITEM_ORDER_ITEM_QUANTITY + ") VALUES(?,?,?)";

    private final String SQL_UPDATE = "UPDATE " + ORDERITEM_TABLE + " SET " +
            ORDERITEM_ORDER_ITEM_QUANTITY + "=? WHERE " + ORDERITEM_ORDER_ID + "=? AND "
            + ORDERITEM_PRODUCT_ID + "=?";

    private final String SQL_COUNT = "SELECT COUNT(*) FROM " + ORDERITEM_TABLE;

    private final String SQL_DELETE = "DELETE FROM " + ORDERITEM_TABLE
            + " WHERE " + ORDERITEM_ORDER_ID + "=? AND "
            + ORDERITEM_PRODUCT_ID + "=?";

    @Wired
    private DataSource dataSource; //a

    public OrderItemDaoImpl() {
        logger.info(SQL_SELECT);
    }

    @Override
    public Iterable<OrderItem> findAll() {
        return findByDynamicSelect(SQL_SELECT
                + " ORDER BY "
                + ORDERITEM_ORDER_ID, null);
    }

    @Override
    public Optional<OrderItem> findByOrderIdAndProductId(long orderId, long productId) {
        return findSingleByDynamicSelect(SQL_SELECT
                        + " WHERE " + ORDERITEM_ORDER_ID + "=? AND " + ORDERITEM_PRODUCT_ID + "=? "
                        + " ORDER BY "
                        + ORDERITEM_ORDER_ID,
                new Object[]{orderId, productId});
    }

    @Override
    public boolean existsByOrderIdAndProductId(long orderId, long productId) {
        Optional<OrderItem> orderItem = findByOrderIdAndProductId(orderId, productId);
        return orderItem.isPresent();
    }

    @Override
    public Iterable<OrderItem> findAllByOrderId(long orderId) {
        return findByDynamicSelect(SQL_SELECT + " WHERE "
                + ORDERITEM_ORDER_ID + "=? ORDER BY " + ORDERITEM_ORDER_ID, new Object[]{orderId});
    }

    @Override
    public void save(OrderItem newOrderItem) {
        logger.info("exist: " + existsByOrderIdAndProductId(newOrderItem.getOrder().getOrderId(),
                newOrderItem.getProduct().getProductId()));
        if(existsByOrderIdAndProductId(newOrderItem.getOrder().getOrderId(),
                newOrderItem.getProduct().getProductId())){
            update(newOrderItem);
            return;
        }
        Connection connection = null;
        long insertedId = -1;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement(SQL_INSERT);
            preparedStatement.setLong(1, newOrderItem.getProduct().getProductId());
            preparedStatement.setLong(2, newOrderItem.getOrder().getOrderId());
            preparedStatement.setDouble(3, newOrderItem.getOrderItemQuantity());

            preparedStatement.executeUpdate();

            connection.commit();
        } catch (SQLException exception) {
            exception.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException exception2) {
                    exception2.printStackTrace();
                }
            }
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

    private void update(OrderItem newOrderItem) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement(SQL_UPDATE);
            preparedStatement.setDouble(1, newOrderItem.getOrderItemQuantity());
            preparedStatement.setLong(2, newOrderItem.getOrder().getOrderId());
            preparedStatement.setLong(3, newOrderItem.getProduct().getProductId());

            int affectedRows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException exception) {
            exception.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
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
    public void delete(OrderItem orderItemToDelete)  {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
                statement.setLong(1, orderItemToDelete.getOrder().getOrderId());
                statement.setLong(2, orderItemToDelete.getProduct().getProductId());
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

    public Optional<OrderItem> findSingleByDynamicSelect(String SQL, Object[] params) {
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

    public Iterable<OrderItem> findByDynamicSelect(String sql, Object[] params) {
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

    private Iterable<OrderItem> fetchMultiResults(ResultSet resultSet) {
        Collection<OrderItem> OrderItems = new ArrayList<>();
        try {
            while (resultSet.next()) {
                OrderItem entity = new OrderItem();
                populateEntity(entity, resultSet);
                OrderItems.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return OrderItems;
    }

    private Optional<OrderItem> fetchSingleResult(ResultSet resultSet) {

        try {
            if (resultSet.next()) {
                OrderItem entity = new OrderItem();
                populateEntity(entity, resultSet);
                return Optional.ofNullable(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private void populateEntity(OrderItem entity, ResultSet resultSet) {
        try {
                Product product = new Product();
                    WarehouseItem warehouseItem = new WarehouseItem();
                    warehouseItem.setWarehouseItemId(resultSet.getLong(PRODUCT_WAREHOUSEITEM_ID));
                product.setWarehouseItem(warehouseItem);
                    Category category = new Category();
                    category.setCategoryId(resultSet.getLong(PRODUCT_CATEGORY_ID));
                product.setCategory(category);
                product.setProductName(resultSet.getString(PRODUCT_PRODUCT_NAME));
                product.setProductDescription(resultSet.getString(PRODUCT_PRODUCT_DESCRIPTION));
                product.setProductPrice(resultSet.getDouble(PRODUCT_PRODUCT_PRICE));
                product.setProductImgSrc(resultSet.getString(PRODUCT_PRODUCT_IMAGESRC));
                product.setProductId(resultSet.getLong(ORDERITEM_PRODUCT_ID));
            entity.setProduct(product);
                Order order = new Order();
                order.setOrderId(resultSet.getLong(ORDERITEM_ORDER_ID));
                    User user = new User();
                    user.setUserId(resultSet.getLong(ORDER_USER_ID));
                order.setUser(user);
                order.setOrderStatus(resultSet.getInt(ORDER_ORDER_STATUS));
                order.setSummaryPrice(resultSet.getDouble(ORDER_SUMMARY_PRICE));
            entity.setOrder(order);
            entity.setOrderItemQuantity(resultSet.getDouble(ORDERITEM_ORDER_ITEM_QUANTITY));
        } catch (SQLException e) {
            e.printStackTrace();
            entity = null;
        }
    }

    public String getTableName() {
        return ORDERITEM_TABLE;
    }


}
