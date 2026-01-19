package com.adarsh.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.adarsh.dao.DB;

public class AdminDashboard extends JFrame {

    JTextField questionField;

    // --- THEME COLORS ---
    Color bgDark = new Color(44, 62, 80);       // Main Background
    Color bgPanel = new Color(52, 73, 94);      // Lighter Panel Background
    Color accentColor = new Color(230, 126, 34); // Orange
    Color dangerColor = new Color(192, 57, 43);  // Red
    Color textWhite = Color.WHITE;

    public AdminDashboard() {
        // Window Setup
        setSize(900, 650); // Consistent size with other screens
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Admin Control Panel");
        getContentPane().setBackground(bgDark);
        setLayout(null);

        // --- HEADER ---
        JLabel title = new JLabel("ADMINISTRATOR PANEL", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(accentColor);
        title.setBounds(0, 30, 900, 40);
        add(title);

        JLabel subtitle = new JLabel("Manage Election Settings & System Controls", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setBounds(0, 70, 900, 20);
        add(subtitle);

        // ============================
        // SECTION 1: POLL SETTINGS (Card)
        // ============================
        JPanel pollPanel = createCardPanel(100, 130, 700, 180);
        add(pollPanel);

        JLabel lblPoll = new JLabel("SET ACTIVE POLL QUESTION");
        lblPoll.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPoll.setForeground(new Color(52, 152, 219)); // Light Blue title
        lblPoll.setBounds(30, 20, 300, 20);
        pollPanel.add(lblPoll);

        // Styled Text Field
        questionField = new JTextField();
        questionField.setBounds(30, 60, 450, 45);
        questionField.setBackground(bgDark); // Dark input background
        questionField.setForeground(textWhite);
        questionField.setCaretColor(textWhite);
        questionField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        questionField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding inside input
        ));
        pollPanel.add(questionField);

        // Update Button
        JButton updateBtn = createStyledButton("UPDATE QUESTION", accentColor);
        updateBtn.setBounds(500, 60, 170, 45);
        pollPanel.add(updateBtn);
        
        JLabel lblHint = new JLabel("This question will be visible to all users immediately.");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblHint.setForeground(Color.GRAY);
        lblHint.setBounds(30, 115, 400, 20);
        pollPanel.add(lblHint);


        // ============================
        // SECTION 2: DANGER ZONE (Card)
        // ============================
        JPanel dangerPanel = createCardPanel(100, 350, 700, 120);
        dangerPanel.setBorder(BorderFactory.createLineBorder(dangerColor)); // Red Border
        add(dangerPanel);

        JLabel lblDanger = new JLabel("DANGER ZONE");
        lblDanger.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDanger.setForeground(dangerColor);
        lblDanger.setBounds(30, 20, 300, 20);
        dangerPanel.add(lblDanger);

        JLabel lblWarning = new JLabel("Resetting the election will delete ALL votes and allow users to vote again.");
        lblWarning.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblWarning.setForeground(Color.LIGHT_GRAY);
        lblWarning.setBounds(30, 55, 450, 20);
        dangerPanel.add(lblWarning);

        // Reset Button
        JButton resetBtn = createStyledButton("RESET ELECTION", dangerColor);
        resetBtn.setBounds(500, 40, 170, 45);
        dangerPanel.add(resetBtn);


        // --- LOGOUT BUTTON ---
        JButton logoutBtn = createStyledButton("LOGOUT", Color.GRAY);
        logoutBtn.setBounds(760, 510, 100, 35);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(logoutBtn);


        // ============================
        // EVENTS
        // ============================
        
        // 1. Set Poll Question
        updateBtn.addActionListener(e -> {
            String question = questionField.getText();
            if(question.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Question cannot be empty!");
                return;
            }
            try {
                Connection con = DB.createConnection();
                PreparedStatement pt = con.prepareStatement("UPDATE poll_settings SET question=? WHERE id=1");
                pt.setString(1, question);
                pt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Poll Question Updated Successfully!");
            } catch(Exception ex) { 
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error");
            }
        });

        // 2. Reset Election logic
        resetBtn.addActionListener(e -> {
            int check = JOptionPane.showConfirmDialog(this, 
                "⚠️ CRITICAL WARNING ⚠️\n\nThis will PERMANENTLY DELETE all votes.\nAll users will be allowed to vote again.\n\nAre you sure?", 
                "Confirm System Reset", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if(check == JOptionPane.YES_OPTION) {
                try {
                    Connection con = DB.createConnection();
                    // Clear Vote Counts
                    con.prepareStatement("UPDATE candidates SET votes = 0").executeUpdate();
                    // Reset User Vote Status
                    con.prepareStatement("UPDATE users SET has_voted = 0").executeUpdate();
                    
                    JOptionPane.showMessageDialog(this, "System Reset Complete. Election Restarted.");
                } catch(Exception ex) { ex.printStackTrace(); }
            }
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });
    }

    // --- HELPER METHODS FOR STYLING ---

    private JPanel createCardPanel(int x, int y, int w, int h) {
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(bgPanel);
        p.setBounds(x, y, w, h);
        // Subtle drop shadow effect using border (hacky but works for simple Swing)
        p.setBorder(BorderFactory.createLineBorder(new Color(60, 80, 100))); 
        return p;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
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

    public static void main(String[] args) {
        new AdminDashboard().setVisible(true);
    }
}