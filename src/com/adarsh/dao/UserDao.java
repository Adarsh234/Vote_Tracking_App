package com.adarsh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.adarsh.dto.UserDto;

public class UserDao {
    
    public boolean auth(UserDto userdto) {
        String sql = "SELECT userid FROM users WHERE userid=? AND password=?";
        
        Connection con = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        
        try {
            con = DB.createConnection(); 
            
            pt = con.prepareStatement(sql);
            pt.setString(1, userdto.getUserid());
            pt.setString(2, userdto.getPassword());
            
            rs = pt.executeQuery();
            
            if (rs.next()) {
                return true;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pt != null) pt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return false;
    }
}