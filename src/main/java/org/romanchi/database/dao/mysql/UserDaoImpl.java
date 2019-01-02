package org.romanchi.database.dao.mysql;

import static org.romanchi.database.Column.*;
import static org.romanchi.database.Table.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import javax.sql.DataSource;

import org.romanchi.Wired;
import org.romanchi.database.Column;
import org.romanchi.database.dao.UserDao;
import org.romanchi.database.entities.User;
import org.romanchi.database.entities.UserRole;


/**
 *
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
            USERROLE_USERROLE_ID + ", " +
            USERROLE_USERROLENAME + " " +
            " FROM " + USER_TABLE +
            " LEFT JOIN " + USERROLE_TABLE + " on " +
            USER_USERUSERROLE_ID + "=" + USERROLE_USERROLE_ID;

    private final String SQL_INSERT = "INSERT INTO " + USER_TABLE +
            "(" + USER_USERNAME + ", " +
            USER_USERSURNAME + ", " +
            USER_USEREMAIL + ", " +
            USER_USERPASSWORD + ", "+
            USER_USERUSERROLE_ID + ") VALUES(?,?,?,?,?)";

    private final String SQL_UPDATE = "UPDATE " + getTableName() +" SET " +
            USER_USERNAME + "=?, " +
            USER_USERSURNAME + "=?, " +
            USER_USEREMAIL + "=?, " +
            USER_USERPASSWORD + "=?, " +
            USER_USERUSERROLE_ID + "=? WHERE " + Column.USER_USER_ID + "=?";

    private final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE " + Column.USER_USER_ID + "=?";

    @Wired
    private DataSource dataSource; //a


    public UserDaoImpl() {}
    
    @Override
    public User[] findAll() throws Exception {
        return findByDynamicSelect(SQL_SELECT + " ORDER BY UserId", null);
    }
    
    @Override
    public User findWhereUserIdEquals(long userId) throws Exception {
        logger.info(String.valueOf(userId));
        return findSingleByDynamicSelect(SQL_SELECT + " WHERE " + USER_USER_ID + "=? ORDER BY " + USER_USER_ID, new Object[]{userId});
    }






    

   
    
    //"INSERT INTO User(UserRoleId, TeamId, UserLogin, UserPassword, IsCaptain) VALUES(?,?,?,?,?)";
    @Override
    public long insert(User newUser) throws Exception{
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = null;
        long insertedId = -1;
        try{
            preparedStatement = connection.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, newUser.getUserName());
            preparedStatement.setString(2, newUser.getUserSurname());
            preparedStatement.setString(3, newUser.getUserEmail());
            preparedStatement.setString(4, newUser.getUserPassword());
            preparedStatement.setLong(5, newUser.getUserUserRole().getUserRoleId());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while(resultSet.next()){
                insertedId = resultSet.getLong(1);
            }
            newUser.setUserId(insertedId);
            connection.commit();
            return insertedId;
        }catch(SQLException exception){
            exception.printStackTrace();
            if(connection != null){
                try{
                    connection.rollback();
                }catch(SQLException exception2){
                   exception2.printStackTrace();
                }
            }
            return insertedId;
        }finally{
            if(connection != null){
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }
    
    //"UPDATE User SET UserRoleId=?, TeamId=?, UserLogin=?, UserPassword=?, IsCaptain=? WHERE UserId = ?";
    @Override
    public int update(User newUser) throws Exception{
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(SQL_UPDATE);
            preparedStatement.setString(1, newUser.getUserName());
            preparedStatement.setString(2, newUser.getUserSurname());
            preparedStatement.setString(3, newUser.getUserEmail());
            preparedStatement.setString(4, newUser.getUserPassword());
            preparedStatement.setLong(5, newUser.getUserUserRole().getUserRoleId());
            int affectedRows = preparedStatement.executeUpdate();
            connection.commit();
            return affectedRows;
        }catch(SQLException exception){
            exception.printStackTrace();
            if(connection != null){
                try{
                    connection.rollback();
                }catch(SQLException sqlException){
                    sqlException.printStackTrace();
                }
            }
            return -1;
        }finally{
            if(connection != null){
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }
    
    //"DELETE FROM User WHERE User.UserId=?";
    @Override
    public int delete(User userToDelete) throws Exception {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(SQL_DELETE);
            preparedStatement.setLong(1, userToDelete.getUserId());
            int affectedRows = preparedStatement.executeUpdate();
            connection.commit();
            return affectedRows;
        }catch(Exception exception){
            try{
                if(connection != null){
                    connection.rollback();
                }
            }catch(Exception exception2){
                exception2.printStackTrace();
            }
            return -1;
        }finally{
            if(connection != null){
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    public User findSingleByDynamicSelect(String SQL, Object[] params) throws Exception{
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(SQL);
            if(params != null){
                for(int i = 1; i <= params.length; i++){
                    preparedStatement.setObject(i,params[i-1]);
                }
            }
            resultSet = preparedStatement.executeQuery();
            return fetchSingleResult(resultSet);
        }catch(Exception exception){
            throw new Exception(exception);
        }finally{
            if(connection != null){
                connection.close();
            }
        }
    }
    public User[] findByDynamicSelect(String sql, Object[] params) throws Exception{
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(sql);
            if(params != null){
                for(int i = 1; i <= params.length; i++){
                    preparedStatement.setObject(i, params[i-1]);
                }
            }
            resultSet = preparedStatement.executeQuery();
           
            return fetchMultiResults(resultSet);
        }catch(Exception exception){
            //logger.error(ex, ex);
            exception.printStackTrace();
            throw new Exception(exception);
        }finally{
            if(connection != null){
                connection.close();
            }
        }   
    }
    
    private User[] fetchMultiResults(ResultSet resultSet) throws Exception {
        Collection<User> users = new ArrayList<>();
        while(resultSet.next()){
            User dto = new User();
            populateDto(dto, resultSet);
            users.add(dto);
        }
        
        User[] usersArray = new User[users.size()];
        users.toArray(usersArray);
        return usersArray;
    }
    private User fetchSingleResult(ResultSet resultSet) throws Exception{
        if(resultSet.next()){
            User dto = new User();
            populateDto(dto, resultSet);
            return dto;
        }
        return null;
    }

    private void populateDto(User dto, ResultSet resultSet) throws Exception{
        dto.setUserId(resultSet.getLong(USER_USER_ID));
        dto.setUserName(resultSet.getString(USER_USERNAME));
        dto.setUserSurname(resultSet.getString(USER_USERSURNAME));
        dto.setUserEmail(resultSet.getString(USER_USEREMAIL));
        dto.setUserPassword(resultSet.getString(USER_USERPASSWORD));
            UserRole userRole = new UserRole();
            userRole.setUserRoleId(resultSet.getLong(USER_USERUSERROLE_ID));
            userRole.setUserRoleName(resultSet.getString(USERROLE_USERROLENAME));
        dto.setUserUserRole(userRole);
    }
    
    public String getTableName(){
        return "User";
    }

    

}
