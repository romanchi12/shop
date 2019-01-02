package org.romanchi.database.dao;

import org.romanchi.Wired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDao {

    @Wired
    DataSource dataSource;

    public String getData(){
        try {
            Connection connection = dataSource.getConnection();
            Statement stmt=connection.createStatement();
            ResultSet rs=stmt.executeQuery("select * from User LIMIT 10");
            StringBuilder stringBuilder = new StringBuilder();

            while(rs.next()){
            stringBuilder.append(rs.getObject(1)).append(rs.getObject(2)).append(rs.getObject(3)).append(rs.getObject(4)).append(rs.getObject(5)).append("<br>");
            }
            return stringBuilder.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        }
    }
}
