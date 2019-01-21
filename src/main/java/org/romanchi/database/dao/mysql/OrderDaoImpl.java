package org.romanchi.database.dao.mysql;

import org.romanchi.Wired;
import org.romanchi.database.Column;
import org.romanchi.database.dao.OrderDao;
import org.romanchi.database.entities.Order;
import org.romanchi.database.entities.User;
import org.romanchi.database.entities.UserRole;

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
import static org.romanchi.database.Column.USERROLE_USERROLENAME;
import static org.romanchi.database.Column.USER_USERUSERROLE_ID;
import static org.romanchi.database.Table.ORDER_TABLE;
import static org.romanchi.database.Table.USERROLE_TABLE;
import static org.romanchi.database.Table.USER_TABLE;

public class OrderDaoImpl implements OrderDao {
    private final static Logger logger = Logger.getLogger(OrderDaoImpl.class.getName());

    private final String SQL_SELECT = "SELECT "
            + ORDER_ORDER_ID + ", "
            + ORDER_USER_ID + ", "
            + ORDER_ORDER_STATUS + ", "
            + ORDER_SUMMARY_PRICE + ", "
            + USER_USER_ID + ", "
            + USER_USERNAME + ", "
            + USER_USERSURNAME + ", "
            + USER_USEREMAIL + ", "
            + USER_USERPASSWORD + ", "
            + USER_USERUSERROLE_ID + " "
            + " FROM " + ORDER_TABLE
            + " LEFT JOIN " + USER_TABLE + " on "
            + ORDER_USER_ID + "=" + USER_USER_ID;

    private final String SQL_INSERT = "INSERT INTO " + ORDER_TABLE +
            "(" + ORDER_USER_ID + ", " +
            ORDER_ORDER_STATUS + ", " +
            ORDER_SUMMARY_PRICE + ") VALUES(?,?,?)";

    private final String SQL_UPDATE = "UPDATE " + ORDER_TABLE + " SET " +
            ORDER_USER_ID + "=?, " +
            ORDER_ORDER_STATUS + "=?, " +
            ORDER_SUMMARY_PRICE + "=? WHERE " + ORDER_ORDER_ID + "=?";

    private final String SQL_COUNT = "SELECT COUNT(*) FROM " + ORDER_TABLE;

    private final String SQL_DELETE = "DELETE FROM " + ORDER_TABLE + " WHERE " + ORDER_ORDER_ID + "=?";

    @Wired
    private DataSource dataSource; //a

    public OrderDaoImpl() {
    }

    @Override
    public Iterable<Order> findAll() {
        return findByDynamicSelect(SQL_SELECT
                + " ORDER BY "
                + ORDER_ORDER_ID, null);
    }

    @Override
    public Optional<Order> findById(long OrderId) {
        return findSingleByDynamicSelect(SQL_SELECT
                + " WHERE "
                + ORDER_ORDER_ID
                + "=? ORDER BY "
                + ORDER_ORDER_ID,
                new Object[]{OrderId});
    }

    @Override
    public Iterable<Order> findAllByUserId(long userId) {
        return  findByDynamicSelect(SQL_SELECT
                + " WHERE "
                + ORDER_USER_ID
                + "=? ORDER BY "
                + ORDER_ORDER_ID
                + " DESC",
                new Object[]{userId});
    }

    @Override
    public long save(Order newOrder) {
        if(existsById(newOrder.getOrderId())){
            return update(newOrder);
        }
        Connection connection = null;
        long insertedId = -1;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, newOrder.getUser().getUserId());
            preparedStatement.setInt(2, newOrder.getOrderStatus());
            preparedStatement.setDouble(3, newOrder.getSummaryPrice());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                insertedId = resultSet.getLong(1);
            }
            newOrder.setOrderId(insertedId);
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

    private long update(Order newOrder) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement(SQL_UPDATE);
            preparedStatement.setLong(1, newOrder.getUser().getUserId());
            preparedStatement.setInt(2, newOrder.getOrderStatus());
            preparedStatement.setDouble(3, newOrder.getSummaryPrice());
            preparedStatement.setLong(4, newOrder.getOrderId());

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
    public void delete(Order orderToDelete)  {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
                statement.setLong(1, orderToDelete.getOrderId());
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


    public Optional<Order> findSingleByDynamicSelect(String SQL, Object[] params) {
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

    public Iterable<Order> findByDynamicSelect(String sql, Object[] params) {
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
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }

    private Iterable<Order> fetchMultiResults(ResultSet resultSet) {
        Collection<Order> Orders = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Order entity = new Order();
                populateEntity(entity, resultSet);
                Orders.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Orders;
    }

    private Optional<Order> fetchSingleResult(ResultSet resultSet) {

        try {
            if (resultSet.next()) {
                Order entity = new Order();
                populateEntity(entity, resultSet);
                return Optional.ofNullable(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private void populateEntity(Order entity, ResultSet resultSet) {
        try {
            entity.setOrderId(resultSet.getLong(ORDER_ORDER_ID));
            User user = new User();
                user.setUserId(resultSet.getLong(ORDER_USER_ID));
                user.setUserName(resultSet.getString(USER_USERNAME));
                user.setUserSurname(resultSet.getString(USER_USERSURNAME));
                user.setUserEmail(resultSet.getString(USER_USEREMAIL));
                user.setUserPassword(resultSet.getString(USER_USERPASSWORD));
                UserRole userRole = new UserRole();
                    userRole.setUserRoleId(resultSet.getLong(USER_USERUSERROLE_ID));
                user.setUserUserRole(userRole);
            entity.setUser(user);

            entity.setOrderStatus(resultSet.getInt(ORDER_ORDER_STATUS));
            entity.setSummaryPrice(resultSet.getDouble(ORDER_SUMMARY_PRICE));
        } catch (SQLException e) {
            e.printStackTrace();
            entity = null;
        }
    }

    @Override
    public boolean existsById(long orderId) {
        Optional<Order> order = findById(orderId);
        return order.isPresent();
    }


    public String getTableName() {
        return ORDER_TABLE;
    }


}
