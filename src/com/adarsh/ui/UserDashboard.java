package com.adarsh.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
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
    
    // Logic Components
    UserDao userDao;
    VoteDao voteDao;
    ButtonGroup group;       // Holds the dynamic buttons
    JPanel dynamicVotePanel; // Visual container for buttons
    
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
        setSize(900, 650);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(bgDark);
        setLayout(null);

        // --- HEADER ---
        JLabel header = new JLabel("ACTIVE POLL", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 32));
        header.setForeground(accentColor);
        header.setBounds(0, 20, 900, 30);
        add(header);

        // Dynamic Question from Database
        questionLabel = new JLabel("Loading Question...", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        questionLabel.setForeground(Color.LIGHT_GRAY);
        questionLabel.setBounds(0, 55, 900, 30);
        add(questionLabel);
        loadQuestion(); 

        // ============================
        // LEFT SIDE: VOTING AREA (DYNAMIC)
        // ============================
        
        // 1. Visual Background Box
        JPanel containerPanel = new JPanel(null);
        containerPanel.setBackground(bgPanel);
        containerPanel.setBounds(50, 100, 380, 400);
        containerPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(containerPanel);

        JLabel lblVote = new JLabel("Select Candidate");
        lblVote.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblVote.setForeground(textWhite);
        lblVote.setBounds(20, 20, 200, 30);
        containerPanel.add(lblVote);

        // 2. Dynamic List Container (Scrollable)
        dynamicVotePanel = new JPanel();
        dynamicVotePanel.setLayout(new GridLayout(0, 1, 0, 10)); // 1 Col, Auto Rows, 10px Gap
        dynamicVotePanel.setBackground(bgPanel);
        
        group = new ButtonGroup();
        List<String> candidates = voteDao.getAllCandidates();
        
        // Loop through DB names and create buttons
        for(String name : candidates) {
            JRadioButton rb = createStyledRadio(name);
            rb.setActionCommand(name); // IMPORTANT: Stores the name to retrieve later
            group.add(rb);
            dynamicVotePanel.add(rb);
        }

        // 3. Scroll Pane (Holds the dynamic panel)
        JScrollPane scroll = new JScrollPane(dynamicVotePanel);
        scroll.setBounds(20, 60, 340, 200);
        scroll.setBorder(null); // No border
        scroll.getViewport().setBackground(bgPanel); // Match background
        containerPanel.add(scroll);

        // 4. Vote Button
        voteBtn = new JButton("CONFIRM VOTE");
        voteBtn.setBounds(40, 300, 300, 50);
        voteBtn.setBackground(accentColor);
        voteBtn.setForeground(Color.WHITE);
        voteBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        voteBtn.setFocusPainted(false);
        containerPanel.add(voteBtn);


        // ============================
        // RIGHT SIDE: RESULT AREA
        // ============================
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(null);
        resultPanel.setBackground(new Color(30, 30, 30)); // Black/Grey
        resultPanel.setBounds(470, 100, 380, 400);
        resultPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN)); 
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
            // Get selected button model
            if (group.getSelection() != null) {
                String selected = group.getSelection().getActionCommand();
                
                boolean success = voteDao.castVote(selected);
                
                if(success) {
                    userDao.markUserAsVoted(currentUserId);
                    JOptionPane.showMessageDialog(this, "Vote Submitted Successfully for " + selected + "!");
                    disableVoting("Vote Cast");
                    updateResults(); 
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
        
        // Dynamic Loop for Results
        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            sb.append(String.format(" %-14s [ %03d ] \n\n", entry.getKey(), entry.getValue()));
        }
        
        sb.append("----------------------------");
        resultArea.setText(sb.toString());
    }

    private void disableVoting(String message) {
        voteBtn.setEnabled(false);
        voteBtn.setText(message);
        voteBtn.setBackground(Color.GRAY);
        
        // Also disable radio buttons
        Enumeration<AbstractButton> buttons = group.getElements();
        while(buttons.hasMoreElements()) {
            buttons.nextElement().setEnabled(false);
        }
    }

    private JRadioButton createStyledRadio(String text) {
        JRadioButton rb = new JRadioButton(text);
        // Note: No setBounds needed because we are using GridLayout now!
        rb.setBackground(bgPanel);
        rb.setForeground(textWhite);
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rb.setFocusPainted(false);
        return rb;
    }
}