package com.adarsh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList; // Added
import java.util.HashMap;
import java.util.List;      // Added
import java.util.Map;

public class VoteDao {

    // 1. Cast a vote for a specific candidate
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

    // 2. Get results for the Live Dashboard (Name + Vote Count)
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

    // 3. Reset votes only (keep candidates same)
    public void resetVotes() {
        String sql = "UPDATE candidates SET votes = 0";
        try {
            Connection con = DB.createConnection();
            PreparedStatement pt = con.prepareStatement(sql);
            pt.executeUpdate();
            
            // Also reset user status so they can vote again
            con.createStatement().executeUpdate("UPDATE users SET has_voted = 0");
            
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- NEW METHODS FOR DYNAMIC CANDIDATES ---

    // 4. Get just the list of names (For generating Radio Buttons)
    public List<String> getAllCandidates() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT name FROM candidates";
        
        try {
            Connection con = DB.createConnection();
            ResultSet rs = con.createStatement().executeQuery(sql);
            
            while(rs.next()) {
                list.add(rs.getString("name"));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 5. Admin: Wipe old election and start fresh with NEW candidates
    public void setupNewElection(String[] candidateNames) {
        try {
            Connection con = DB.createConnection();
            
            // A. Clear existing candidates
            con.createStatement().executeUpdate("DELETE FROM candidates");
            
            // B. Reset all users so they can vote in the new election
            con.createStatement().executeUpdate("UPDATE users SET has_voted = 0"); 
            
            // C. Insert the new list of candidates
            String sql = "INSERT INTO candidates (name, votes) VALUES (?, 0)";
            PreparedStatement pt = con.prepareStatement(sql);
            
            for(String name : candidateNames) {
                if(!name.trim().isEmpty()) { // Avoid empty names
                    pt.setString(1, name.trim());
                    pt.executeUpdate();
                }
            }
            
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}