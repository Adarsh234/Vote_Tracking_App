package com.adarsh.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    
    private DB(){} 

    public static Connection createConnection() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/bookingtb";
        String user = "root";
        String pass = "adarsh0103";
        
        Connection connection = DriverManager.getConnection(url, user, pass);
        
        if(connection != null){
            System.out.println("Connection Created");
        }
        return connection;
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DB.createConnection();
    }
}