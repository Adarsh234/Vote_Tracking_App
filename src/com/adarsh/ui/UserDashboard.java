package com.adarsh.ui;

import java.awt.Color;
// import java.awt.Cursor;
import java.awt.Font;
// import java.awt.event.MouseAdapter;
// import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.adarsh.dao.DB;
import com.adarsh.dao.UserDao;
import com.adarsh.dao.VoteDao;

public class UserDashboard extends JFrame {
    
    String currentUserId;
    JLabel questionLabel;
    JButton voteBtn;
    JTextArea resultArea;
    
    UserDao userDao;
    VoteDao voteDao;
    
    // --- THEME COLORS ---
    Color bgDark = new Color(44, 62, 80);       // Dark Blue
    Color bgPanel = new Color(52, 73, 94);      // Lighter Blue
    Color accentColor = new Color(230, 126, 34); // Orange
    Color textWhite = Color.WHITE;

    public UserDashboard(String userid) {
        this.currentUserId = userid;
        userDao = new UserDao();
        voteDao = new VoteDao();

        // Window Setup
        setTitle("User Voting Panel");
        setSize(900, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(bgDark);
        setLayout(null);

        // --- HEADER ---
        JLabel header = new JLabel("ACTIVE POLL", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(accentColor);
        header.setBounds(0, 20, 900, 30);
        add(header);

        // Dynamic Question from Database
        questionLabel = new JLabel("Loading Question...", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        questionLabel.setForeground(Color.LIGHT_GRAY);
        questionLabel.setBounds(0, 55, 900, 20);
        add(questionLabel);
        loadQuestion(); 

        // ============================
        // LEFT SIDE: VOTING AREA
        // ============================
        JPanel votePanel = new JPanel();
        votePanel.setLayout(null);
        votePanel.setBackground(bgPanel);
        votePanel.setBounds(50, 100, 380, 400);
        votePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(votePanel);

        JLabel lblVote = new JLabel("Select Candidate");
        lblVote.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblVote.setForeground(textWhite);
        lblVote.setBounds(20, 20, 200, 30);
        votePanel.add(lblVote);

        JRadioButton rb1 = createStyledRadio("Party A", 80);
        JRadioButton rb2 = createStyledRadio("Party B", 130);
        JRadioButton rb3 = createStyledRadio("Party C", 180);

        ButtonGroup group = new ButtonGroup();
        group.add(rb1); group.add(rb2); group.add(rb3);
        
        votePanel.add(rb1); votePanel.add(rb2); votePanel.add(rb3);

        voteBtn = new JButton("CONFIRM VOTE");
        voteBtn.setBounds(40, 280, 300, 50);
        voteBtn.setBackground(accentColor);
        voteBtn.setForeground(Color.WHITE);
        voteBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        voteBtn.setFocusPainted(false);
        votePanel.add(voteBtn);


        // ============================
        // RIGHT SIDE: RESULT AREA
        // ============================
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(null);
        resultPanel.setBackground(new Color(30, 30, 30)); // Black/Grey
        resultPanel.setBounds(470, 100, 380, 400);
        resultPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN)); // Matrix style border
        add(resultPanel);

        JLabel lblResult = new JLabel("Live Results");
        lblResult.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblResult.setForeground(Color.GREEN);
        lblResult.setBounds(20, 20, 200, 30);
        resultPanel.add(lblResult);

        resultArea = new JTextArea();
        resultArea.setBounds(20, 70, 340, 300);
        resultArea.setBackground(new Color(30, 30, 30));
        resultArea.setForeground(Color.GREEN);
        resultArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        resultArea.setEditable(false);
        resultPanel.add(resultArea);
        
        // Initial Load of Results
        updateResults();

        // --- FOOTER / LOGOUT ---
        JButton logoutBtn = new JButton("LOGOUT");
        logoutBtn.setBounds(750, 520, 100, 30);
        logoutBtn.setBackground(new Color(192, 57, 43)); // Red
        logoutBtn.setForeground(Color.WHITE);
        add(logoutBtn);


        // --- LOGIC: Check if User Voted ---
        if(userDao.hasUserVoted(currentUserId)) {
            disableVoting("Vote Already Cast");
        }

        // --- EVENTS ---
        voteBtn.addActionListener(e -> {
            String selected = null;
            if(rb1.isSelected()) selected = "Party A";
            if(rb2.isSelected()) selected = "Party B";
            if(rb3.isSelected()) selected = "Party C";

            if(selected != null) {
                // 1. Cast Vote
                boolean success = voteDao.castVote(selected);
                
                if(success) {
                    // 2. Mark User as Voted
                    userDao.markUserAsVoted(currentUserId);
                    
                    JOptionPane.showMessageDialog(this, "Vote Submitted Successfully!");
                    
                    // 3. Disable Buttons & Update Results
                    disableVoting("Vote Cast");
                    updateResults(); // Refresh the right screen immediately
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a candidate first.");
            }
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });
    }

    // --- HELPER METHODS ---

    private void loadQuestion() {
        try {
            ResultSet rs = DB.createConnection().createStatement().executeQuery("SELECT question FROM poll_settings WHERE id=1");
            if(rs.next()) questionLabel.setText(rs.getString("question"));
        } catch(Exception e) { e.printStackTrace(); }
    }

    private void updateResults() {
        Map<String, Integer> results = voteDao.getResults();
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------\n");
        sb.append(" CANDIDATE      VOTES       \n");
        sb.append("----------------------------\n\n");
        
        sb.append(String.format(" Party A        [ %03d ] \n\n", results.getOrDefault("Party A", 0)));
        sb.append(String.format(" Party B        [ %03d ] \n\n", results.getOrDefault("Party B", 0)));
        sb.append(String.format(" Party C        [ %03d ] \n\n", results.getOrDefault("Party C", 0)));
        
        sb.append("----------------------------");
        resultArea.setText(sb.toString());
    }

    private void disableVoting(String message) {
        voteBtn.setEnabled(false);
        voteBtn.setText(message);
        voteBtn.setBackground(Color.GRAY);
    }

    private JRadioButton createStyledRadio(String text, int y) {
        JRadioButton rb = new JRadioButton(text);
        rb.setBounds(40, y, 200, 30);
        rb.setBackground(bgPanel);
        rb.setForeground(textWhite);
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rb.setFocusPainted(false);
        return rb;
    }
}