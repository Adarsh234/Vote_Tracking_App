package com.adarsh.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.adarsh.dao.VoteDao;

public class Dashboard extends JFrame {

    JTextArea resultArea;
    VoteDao voteDao;

    // --- THEME COLORS ---
    Color bgDark = new Color(44, 62, 80);       // Dark Blue Background
    Color bgLighter = new Color(52, 73, 94);    // Slightly Lighter (for panels)
    Color accentColor = new Color(230, 126, 34); // Orange
    Color textWhite = Color.WHITE;

    public Dashboard() {
        voteDao = new VoteDao();

        // Window Setup
        setSize(900, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(bgDark);
        getContentPane().setLayout(null);

        // --- HEADER ---
        JLabel title = new JLabel("ELECTION DASHBOARD", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(accentColor);
        title.setBounds(0, 30, 900, 40);
        add(title);

        // ============================
        // LEFT SIDE: VOTING PANEL
        // ============================
        
        // Panel Background (Visual only)
        JLabel leftPanel = new JLabel();
        leftPanel.setOpaque(true);
        leftPanel.setBackground(bgLighter);
        leftPanel.setBounds(50, 120, 350, 400);
        leftPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        
        JLabel voteLabel = new JLabel("CAST YOUR VOTE");
        voteLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        voteLabel.setForeground(textWhite);
        voteLabel.setBounds(80, 140, 300, 30);
        add(voteLabel);

        // Radio Buttons
        JRadioButton rb1 = createStyledRadio("Party A", 190);
        JRadioButton rb2 = createStyledRadio("Party B", 240);
        JRadioButton rb3 = createStyledRadio("Party C", 290);

        ButtonGroup group = new ButtonGroup();
        group.add(rb1); group.add(rb2); group.add(rb3);
        
        add(rb1); add(rb2); add(rb3);

        // Submit Button
        JButton voteBtn = createStyledButton("SUBMIT VOTE", accentColor);
        voteBtn.setBounds(80, 360, 290, 50);
        add(voteBtn);
        
        // Fix z-order
        add(leftPanel);
        getContentPane().setComponentZOrder(leftPanel, getContentPane().getComponentCount()-1);


        // ============================
        // RIGHT SIDE: RESULTS PANEL
        // ============================
        
        JLabel resultLabel = new JLabel("LIVE STANDINGS");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        resultLabel.setForeground(textWhite);
        resultLabel.setBounds(500, 140, 300, 30);
        add(resultLabel);

        resultArea = new JTextArea();
        resultArea.setBounds(500, 190, 350, 330);
        resultArea.setBackground(new Color(30, 30, 30)); // Very dark grey
        resultArea.setForeground(new Color(0, 255, 0));  // Hacker Green text
        resultArea.setFont(new Font("Consolas", Font.BOLD, 16));
        resultArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        resultArea.setEditable(false);
        updateResults(); 
        add(resultArea);

        // --- BOTTOM BUTTONS ---
        
        // RESET BUTTON (New Feature)
        JButton resetBtn = createStyledButton("RESET ELECTION", new Color(142, 68, 173)); // Purple
        resetBtn.setBounds(50, 600, 180, 40); // Bottom Left
        resetBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(resetBtn);

        // LOGOUT BUTTON
        JButton logoutBtn = createStyledButton("LOGOUT", new Color(192, 57, 43)); // Red
        logoutBtn.setBounds(750, 600, 100, 40); // Bottom Right
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(logoutBtn);

        // --- EVENTS ---
        voteBtn.addActionListener(e -> {
            String selected = null;
            if (rb1.isSelected()) selected = "Party A";
            else if (rb2.isSelected()) selected = "Party B";
            else if (rb3.isSelected()) selected = "Party C";

            if (selected != null) {
                boolean success = voteDao.castVote(selected);
                if(success) {
                    JOptionPane.showMessageDialog(this, "Vote Successfully Cast!");
                    updateResults(); 
                    group.clearSelection();
                } else {
                    JOptionPane.showMessageDialog(this, "Error Saving Vote.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a party to vote.");
            }
        });

        // Event for Reset Button
        resetBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to RESET all votes to zero?", 
                "Admin Warning", 
                JOptionPane.YES_NO_OPTION);
                
            if(confirm == JOptionPane.YES_OPTION) {
                // Assuming you added the resetVotes() method to VoteDao as discussed
                voteDao.resetVotes(); 
                updateResults();
                JOptionPane.showMessageDialog(this, "Election has been reset!");
            }
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });
    }

    // --- HELPER METHODS FOR STYLING ---

    private JRadioButton createStyledRadio(String text, int y) {
        JRadioButton rb = new JRadioButton(text);
        rb.setBounds(80, y, 200, 30);
        rb.setBackground(bgLighter); // Match panel
        rb.setForeground(textWhite);
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rb.setFocusPainted(false);
        return rb;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover Effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private void updateResults() {
        Map<String, Integer> results = voteDao.getResults();
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------\n");
        sb.append(" PARTY          VOTES       \n");
        sb.append("----------------------------\n\n");
        
        sb.append(String.format(" %-15s %03d \n\n", "Party A", results.getOrDefault("Party A", 0)));
        sb.append(String.format(" %-15s %03d \n\n", "Party B", results.getOrDefault("Party B", 0)));
        sb.append(String.format(" %-15s %03d \n\n", "Party C", results.getOrDefault("Party C", 0)));
        
        sb.append("----------------------------");
        resultArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        new Dashboard().setVisible(true);
    }
}