package com.adarsh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.adarsh.dto.UserDto;

public class UserDao {
    
    // Returns "admin", "user", or null (if login fails)
    public String checkLogin(UserDto userdto) {
        String sql = "SELECT role FROM users WHERE userid=? AND password=?";
        try {
            Connection con = DB.createConnection();
            PreparedStatement pt = con.prepareStatement(sql);
            pt.setString(1, userdto.getUserid());
            pt.setString(2, userdto.getPassword());
            ResultSet rs = pt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // Check if user has already voted
    public boolean hasUserVoted(String userid) {
        String sql = "SELECT has_voted FROM users WHERE userid=?";
        try {
            Connection con = DB.createConnection();
            PreparedStatement pt = con.prepareStatement(sql);
            pt.setString(1, userid);
            ResultSet rs = pt.executeQuery();
            if(rs.next()) {
                return rs.getInt("has_voted") == 1;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // Mark user as voted
    public void markUserAsVoted(String userid) {
        try {
            Connection con = DB.createConnection();
            con.prepareStatement("UPDATE users SET has_voted = 1 WHERE userid = '" + userid + "'").executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}