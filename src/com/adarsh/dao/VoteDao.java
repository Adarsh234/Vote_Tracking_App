package com.adarsh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class VoteDao {

    public boolean castVote(String candidateName) {
        String sql = "UPDATE candidates SET votes = votes + 1 WHERE name = ?";
        
        try {
            Connection con = DB.createConnection();
            PreparedStatement pt = con.prepareStatement(sql);
            pt.setString(1, candidateName);
            
            int rowsUpdated = pt.executeUpdate();
            
            con.close();
            return rowsUpdated > 0; 
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Integer> getResults() {
        Map<String, Integer> results = new HashMap<>();
        String sql = "SELECT name, votes FROM candidates";
        
        try {
            Connection con = DB.createConnection();
            PreparedStatement pt = con.prepareStatement(sql);
            ResultSet rs = pt.executeQuery();
            
            while(rs.next()) {
                results.put(rs.getString("name"), rs.getInt("votes"));
            }
            
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public void resetVotes() {
        String sql = "UPDATE candidates SET votes = 0";
        try {
            Connection con = DB.createConnection();
            PreparedStatement pt = con.prepareStatement(sql);
            pt.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}