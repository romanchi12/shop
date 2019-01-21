package org.romanchi.database.dao.mysql;

import org.romanchi.Wired;
import org.romanchi.database.Column;
import org.romanchi.database.dao.CategoryDao;
import org.romanchi.database.entities.Category;

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
import static org.romanchi.database.Table.CATEGORY_TABLE;
import static org.romanchi.database.Table.CATEGORY_TABLE;

public class CategoryDaoImpl implements CategoryDao {
    private final static Logger logger = Logger.getLogger(CategoryDaoImpl.class.getName());

    private final String SQL_SELECT = "SELECT " +
            CATEGORY_CATEGORY_ID + ", " +
            CATEGORY_CATEGORYNAME +
            " FROM " + CATEGORY_TABLE;

    private final String SQL_INSERT = "INSERT INTO " + CATEGORY_TABLE +
            "(" + CATEGORY_CATEGORYNAME + ") VALUES(?)";

    private final String SQL_UPDATE = "UPDATE " + CATEGORY_TABLE + " SET " +
            CATEGORY_CATEGORYNAME + "=? " +
            "WHERE " + Column.CATEGORY_CATEGORY_ID + "=?";

    private final String SQL_COUNT = "SELECT COUNT(*) FROM " + CATEGORY_TABLE;

    private final String SQL_DELETE = "DELETE FROM " + CATEGORY_TABLE + " WHERE " + Column.CATEGORY_CATEGORY_ID + "=?";

    @Wired
    private DataSource dataSource; //a

    public CategoryDaoImpl() {
    }

    @Override
    public Iterable<Category> findAll() {
        return findByDynamicSelect(SQL_SELECT + " ORDER BY " + CATEGORY_CATEGORY_ID, null);
    }

    @Override
    public Iterable<Category> findTenFrom(int from) {
        return findByDynamicSelect(SQL_SELECT +" ORDER BY " + CATEGORY_CATEGORY_ID +" DESC LIMIT 10 OFFSET ?", new Object[]{from});
    }

    @Override
    public Optional<Category> findById(long сategoryId) {
        return findSingleByDynamicSelect(SQL_SELECT + " WHERE " + CATEGORY_CATEGORY_ID + "=? ORDER BY " + CATEGORY_CATEGORY_ID, new Object[]{сategoryId});
    }

    @Override
    public Optional<Category> findByCategoryName(String сategoryName) {
        return findSingleByDynamicSelect(SQL_SELECT + " WHERE " + CATEGORY_CATEGORYNAME + "=? ORDER BY " +
                CATEGORY_CATEGORY_ID, new Object[]{сategoryName});

    }

    @Override
    public long save(Category newCategory) {
        if(existsById(newCategory.getCategoryId())){
            return update(newCategory);
        }
        Connection connection = null;
        long insertedId = -1;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, newCategory.getCategoryName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                insertedId = resultSet.getLong(1);
            }
            newCategory.setCategoryId(insertedId);
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

    private long update(Category newCategory) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement(SQL_UPDATE);
            preparedStatement.setString(1, newCategory.getCategoryName());
            preparedStatement.setLong(2,newCategory.getCategoryId());
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
    public void delete(Category categoryToDelete)  {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
                statement.setLong(1, categoryToDelete.getCategoryId());
                int affectedRows = statement.executeUpdate();
                connection.commit();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
                connection.rollback();
                connection.setAutoCommit(true);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Optional<Category> findSingleByDynamicSelect(String SQL, Object[] params) {
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

    public Iterable<Category> findByDynamicSelect(String sql, Object[] params) {
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

    private Iterable<Category> fetchMultiResults(ResultSet resultSet) {
        Collection<Category> users = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Category dto = new Category();
                populateEntity(dto, resultSet);
                users.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    private Optional<Category> fetchSingleResult(ResultSet resultSet) {

        try {
            if (resultSet.next()) {
                Category entity = new Category();
                populateEntity(entity, resultSet);
                return Optional.ofNullable(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private void populateEntity(Category entity, ResultSet resultSet) {
        try {
            entity.setCategoryId(resultSet.getLong(CATEGORY_CATEGORY_ID));
            entity.setCategoryName(resultSet.getString(CATEGORY_CATEGORYNAME));
        } catch (SQLException e) {
            e.printStackTrace();
            entity = null;
        }
    }

    @Override
    public boolean existsById(long CategoryId) {
        Optional<Category> Category = findById(CategoryId);
        return Category.isPresent();
    }

    public String getTableName() {
        return CATEGORY_TABLE;
    }

}
