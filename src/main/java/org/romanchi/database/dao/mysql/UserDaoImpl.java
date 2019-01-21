package org.romanchi.database.dao.mysql;

import static org.romanchi.database.Column.*;
import static org.romanchi.database.Table.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javax.sql.DataSource;

import org.romanchi.Wired;
import org.romanchi.database.Column;
import org.romanchi.database.dao.UserDao;
import org.romanchi.database.entities.User;
import org.romanchi.database.entities.UserRole;


/**
 * @author Роман
 */
public class UserDaoImpl implements UserDao {
    private final static Logger logger = Logger.getLogger(UserDaoImpl.class.getName());

    private final String SQL_SELECT = "SELECT " +
            USER_USER_ID + ", " +
            USER_USERNAME + ", " +
            USER_USERSURNAME + ", " +
            USER_USEREMAIL + ", " +
            USER_USERPASSWORD + ", " +
            USER_USERUSERROLE_ID + ", " +
            USER_USERADDRESS + ", " +
            USER_USERLANGUAGE + ", " +
            USERROLE_USERROLE_ID + ", " +
            USERROLE_USERROLENAME + " " +
            " FROM " + USER_TABLE +
            " LEFT JOIN " + USERROLE_TABLE + " on " +
            USER_USERUSERROLE_ID + "=" + USERROLE_USERROLE_ID;

    private final String SQL_INSERT = "INSERT INTO " + USER_TABLE +
            "(" + USER_USERNAME + ", " +
            USER_USERSURNAME + ", " +
            USER_USEREMAIL + ", " +
            USER_USERPASSWORD + ", " +
            USER_USERUSERROLE_ID + ", " +
            USER_USERADDRESS + ", " +
            USER_USERLANGUAGE + ") VALUES(?,?,?,?,?,?,?)";

    private final String SQL_UPDATE = "UPDATE " + USER_TABLE + " SET " +
            USER_USERNAME + "=?, " +
            USER_USERSURNAME + "=?, " +
            USER_USEREMAIL + "=?, " +
            USER_USERPASSWORD + "=?, " +
            USER_USERUSERROLE_ID + "=?, " +
            USER_USERADDRESS + "=?, " +
            USER_USERLANGUAGE + "=? WHERE " + USER_USER_ID + "=?";

    private final String SQL_COUNT = "SELECT COUNT(*) FROM " + USER_TABLE;

    private final String SQL_DELETE = "DELETE FROM " + USER_TABLE + " WHERE " + Column.USER_USER_ID + "=?";

    @Wired
    private DataSource dataSource; //a

    public UserDaoImpl() {
    }

    @Override
    public Iterable<User> findAll() {
        return findByDynamicSelect(SQL_SELECT + " ORDER BY " + USER_USER_ID, null);
    }

    @Override
    public Optional<User> findById(long userId) {
        logger.info(String.valueOf(userId));
        return findSingleByDynamicSelect(SQL_SELECT + " WHERE " + USER_USER_ID + "=? ORDER BY " + USER_USER_ID, new Object[]{userId});
    }

    @Override
    public Optional<User> findByEmailAndPassword(String userEmail, String userPassword) {
        return findSingleByDynamicSelect(SQL_SELECT + " WHERE " + USER_USEREMAIL + "=? AND " + USER_USERPASSWORD + "=? ORDER BY " + USER_USER_ID, new Object[]{userEmail,userPassword});
    }

    //"INSERT INTO User(UserRoleId, TeamId, UserLogin, UserPassword, IsCaptain) VALUES(?,?,?,?,?)";
    @Override
    public long save(User newUser) {
        if(existsById(newUser.getUserId())){
            return update(newUser);
        }
        Connection connection = null;
        long insertedId = -1;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, newUser.getUserName());
            preparedStatement.setString(2, newUser.getUserSurname());
            preparedStatement.setString(3, newUser.getUserEmail());
            preparedStatement.setString(4, newUser.getUserPassword());
            preparedStatement.setLong(5, newUser.getUserUserRole().getUserRoleId());
            preparedStatement.setString(6, newUser.getUserAddress());
            preparedStatement.setString(7, newUser.getUserLanguage());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                insertedId = resultSet.getLong(1);
            }
            newUser.setUserId(insertedId);
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

    //"UPDATE User SET UserRoleId=?, TeamId=?, UserLogin=?, UserPassword=?, IsCaptain=? WHERE UserId = ?";
    private long update(User newUser) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement(SQL_UPDATE);
            preparedStatement.setString(1, newUser.getUserName());
            preparedStatement.setString(2, newUser.getUserSurname());
            preparedStatement.setString(3, newUser.getUserEmail());
            preparedStatement.setString(4, newUser.getUserPassword());
            preparedStatement.setLong(5, newUser.getUserUserRole().getUserRoleId());
            preparedStatement.setString(6,newUser.getUserAddress());
            preparedStatement.setString(7,newUser.getUserLanguage());
            preparedStatement.setLong(8,newUser.getUserId());
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

    //"DELETE FROM User WHERE User.UserId=?";
    @Override
    public void delete(User userToDelete)  {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
                statement.setLong(1, userToDelete.getUserId());
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


    public Optional<User> findSingleByDynamicSelect(String SQL, Object[] params) {
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

    public Iterable<User> findByDynamicSelect(String sql, Object[] params) {
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

    private Iterable<User> fetchMultiResults(ResultSet resultSet) {
        Collection<User> users = new ArrayList<>();
        try {
            while (resultSet.next()) {
                User entity = new User();
                populateEntity(entity, resultSet);
                users.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    private Optional<User> fetchSingleResult(ResultSet resultSet) {

        try {
            if (resultSet.next()) {
                User entity = new User();
                populateEntity(entity, resultSet);
                return Optional.ofNullable(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private void populateEntity(User entity, ResultSet resultSet) {
        try {
            entity.setUserId(resultSet.getLong(USER_USER_ID));
            entity.setUserName(resultSet.getString(USER_USERNAME));
            entity.setUserSurname(resultSet.getString(USER_USERSURNAME));
            entity.setUserEmail(resultSet.getString(USER_USEREMAIL));
            entity.setUserPassword(resultSet.getString(USER_USERPASSWORD));
            entity.setUserAddress(resultSet.getString(USER_USERADDRESS));
            entity.setUserLanguage(resultSet.getString(USER_USERLANGUAGE));
            UserRole userRole = new UserRole();
            userRole.setUserRoleId(resultSet.getLong(USER_USERUSERROLE_ID));
            userRole.setUserRoleName(resultSet.getString(USERROLE_USERROLENAME));
            entity.setUserUserRole(userRole);
        } catch (SQLException e) {
            e.printStackTrace();
            entity = null;
        }
    }

    @Override
    public boolean existsById(long userId) {
        Optional<User> user = findById(userId);
        return user.isPresent();
    }



    public String getTableName() {
        return "User";
    }


}
