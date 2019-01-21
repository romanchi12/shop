package org.romanchi.database.dao.mysql;

import org.romanchi.Wired;
import org.romanchi.database.Column;
import org.romanchi.database.dao.UserRoleDao;
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
import static org.romanchi.database.Table.USERROLE_TABLE;
import static org.romanchi.database.Table.USER_TABLE;

public class UserRoleDaoImpl implements UserRoleDao {
    private final static Logger logger = Logger.getLogger(UserRoleDaoImpl.class.getName());

    private final String SQL_SELECT = "SELECT " +
            USERROLE_USERROLE_ID + ", " +
            USERROLE_USERROLENAME +
            " FROM " + USERROLE_TABLE;

    private final String SQL_INSERT = "INSERT INTO " + USERROLE_TABLE +
            "(" + USERROLE_USERROLENAME + ") VALUES(?)";

    private final String SQL_UPDATE = "UPDATE " + USERROLE_TABLE + " SET " +
            USERROLE_USERROLENAME + "=? " +
            "WHERE " + Column.USERROLE_USERROLE_ID + "=?";

    private final String SQL_COUNT = "SELECT COUNT(*) FROM " + USERROLE_TABLE;

    private final String SQL_DELETE = "DELETE FROM " + USERROLE_TABLE + " WHERE " + Column.USERROLE_USERROLE_ID + "=?";

    @Wired
    private DataSource dataSource; //a

    public UserRoleDaoImpl() {
    }

    @Override
    public Iterable<UserRole> findAll() {
        return findByDynamicSelect(SQL_SELECT + " ORDER BY " + USERROLE_USERROLE_ID, null);
    }

    @Override
    public Optional<UserRole> findById(long userRoleId) {
        return findSingleByDynamicSelect(SQL_SELECT + " WHERE " + USERROLE_USERROLE_ID + "=? ORDER BY " + USERROLE_USERROLE_ID, new Object[]{userRoleId});
    }

    @Override
    public Optional<UserRole> findByUserRoleName(String userRoleName) {
        return findSingleByDynamicSelect(SQL_SELECT + " WHERE " + USERROLE_USERROLENAME + "=? ORDER BY " +
                USERROLE_USERROLE_ID, new Object[]{userRoleName});

    }

    @Override
    public long save(UserRole newUserRole) {
        if(existsById(newUserRole.getUserRoleId())){
            return update(newUserRole);
        }
        Connection connection = null;
        long insertedId = -1;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, newUserRole.getUserRoleName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                insertedId = resultSet.getLong(1);
            }
            newUserRole.setUserRoleId(insertedId);
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

    private long update(UserRole newUserRole) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement(SQL_UPDATE);
            preparedStatement.setString(1, newUserRole.getUserRoleName());
            preparedStatement.setLong(2,newUserRole.getUserRoleId());
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
    public void delete(UserRole userRoleToDelete)  {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
                statement.setLong(1, userRoleToDelete.getUserRoleId());
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

    public Optional<UserRole> findSingleByDynamicSelect(String SQL, Object[] params) {
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

    public Iterable<UserRole> findByDynamicSelect(String sql, Object[] params) {
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

    private Iterable<UserRole> fetchMultiResults(ResultSet resultSet) {
        Collection<UserRole> users = new ArrayList<>();
        try {
            while (resultSet.next()) {
                UserRole dto = new UserRole();
                populateEntity(dto, resultSet);
                users.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    private Optional<UserRole> fetchSingleResult(ResultSet resultSet) {

        try {
            if (resultSet.next()) {
                UserRole entity = new UserRole();
                populateEntity(entity, resultSet);
                return Optional.ofNullable(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private void populateEntity(UserRole entity, ResultSet resultSet) {
        try {
            entity.setUserRoleId(resultSet.getLong(USERROLE_USERROLE_ID));
            entity.setUserRoleName(resultSet.getString(USERROLE_USERROLENAME));
        } catch (SQLException e) {
            e.printStackTrace();
            entity = null;
        }
    }

    @Override
    public boolean existsById(long userRoleId) {
        Optional<UserRole> userRole = findById(userRoleId);
        return userRole.isPresent();
    }

    public String getTableName() {
        return USERROLE_TABLE;
    }

}
