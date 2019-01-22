package org.romanchi.database.dao.mysql;

import org.romanchi.Wired;
import org.romanchi.database.Column;
import org.romanchi.database.dao.WarehouseItemDao;
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

import static org.romanchi.database.Column.WAREHOUSE_WAREHOUSE_ITEM_QUANTITY;
import static org.romanchi.database.Column.WAREHOUSE_WAREHOUSEITEM_ID;
import static org.romanchi.database.Table.WAREHOUSE_TABLE;

public class WarehouseItemDaoImpl implements WarehouseItemDao {
    private final static Logger logger = Logger.getLogger(WarehouseItemDaoImpl.class.getName());

    private final String SQL_SELECT = "SELECT " +
            WAREHOUSE_WAREHOUSEITEM_ID + ", " +
            WAREHOUSE_WAREHOUSE_ITEM_QUANTITY +
            " FROM " + WAREHOUSE_TABLE;

    private final String SQL_INSERT = "INSERT INTO " + WAREHOUSE_TABLE +
            "(" + WAREHOUSE_WAREHOUSE_ITEM_QUANTITY + ") VALUES(?)";

    private final String SQL_UPDATE = "UPDATE " + WAREHOUSE_TABLE + " SET " +
            WAREHOUSE_WAREHOUSE_ITEM_QUANTITY + "=? " +
            "WHERE " + Column.WAREHOUSE_WAREHOUSEITEM_ID + "=?";

    private final String SQL_COUNT = "SELECT COUNT(*) FROM " + WAREHOUSE_TABLE;

    private final String SQL_DELETE = "DELETE FROM " + WAREHOUSE_TABLE + " WHERE " + Column.WAREHOUSE_WAREHOUSEITEM_ID + "=?";

    @Wired
    private DataSource dataSource; //a

    public WarehouseItemDaoImpl() {
    }

    @Override
    public Iterable<WarehouseItem> findAll() {
        return findByDynamicSelect(SQL_SELECT + " ORDER BY " + WAREHOUSE_WAREHOUSEITEM_ID, null);
    }

    @Override
    public Optional<WarehouseItem> findById(long сategoryId) {
        return findSingleByDynamicSelect(SQL_SELECT + " WHERE " + WAREHOUSE_WAREHOUSEITEM_ID + "=? ORDER BY " + WAREHOUSE_WAREHOUSEITEM_ID, new Object[]{сategoryId});
    }

    @Override
    public long save(WarehouseItem newWarehouseItem) {
        if(existsById(newWarehouseItem.getWarehouseItemId())){
            return update(newWarehouseItem);
        }
        Connection connection = null;
        long insertedId = -1;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setDouble(1, newWarehouseItem.getWarehouseItemQuantity());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                insertedId = resultSet.getLong(1);
            }
            newWarehouseItem.setWarehouseItemId(insertedId);
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

    private long update(WarehouseItem newWarehouseItem) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement(SQL_UPDATE);
            preparedStatement.setDouble(1, newWarehouseItem.getWarehouseItemQuantity());
            preparedStatement.setLong(2, newWarehouseItem.getWarehouseItemId());
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
    public void delete(WarehouseItem warehouseItemToDelete)  {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
                statement.setLong(1, warehouseItemToDelete.getWarehouseItemId());
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

    public Optional<WarehouseItem> findSingleByDynamicSelect(String SQL, Object[] params) {
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

    public Iterable<WarehouseItem> findByDynamicSelect(String sql, Object[] params) {
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

    private Iterable<WarehouseItem> fetchMultiResults(ResultSet resultSet) {
        Collection<WarehouseItem> users = new ArrayList<>();
        try {
            while (resultSet.next()) {
                WarehouseItem dto = new WarehouseItem();
                populateEntity(dto, resultSet);
                users.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    private Optional<WarehouseItem> fetchSingleResult(ResultSet resultSet) {

        try {
            if (resultSet.next()) {
                WarehouseItem entity = new WarehouseItem();
                populateEntity(entity, resultSet);
                return Optional.ofNullable(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private void populateEntity(WarehouseItem entity, ResultSet resultSet) {
        try {
            entity.setWarehouseItemId(resultSet.getLong(WAREHOUSE_WAREHOUSEITEM_ID));
            entity.setWarehouseItemQuantity(resultSet.getInt(WAREHOUSE_WAREHOUSE_ITEM_QUANTITY));
        } catch (SQLException e) {
            e.printStackTrace();
            entity = null;
        }
    }

    @Override
    public boolean existsById(long WarehouseId) {
        Optional<WarehouseItem> Warehouse = findById(WarehouseId);
        return Warehouse.isPresent();
    }

    public String getTableName() {
        return WAREHOUSE_TABLE;
    }
}
