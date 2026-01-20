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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.adarsh.dao.DB;
import com.adarsh.dao.VoteDao;

public class AdminDashboard extends JFrame {

    JTextField questionField;
    JTextArea candidateInput; // Input for dynamic candidates

    // --- THEME COLORS ---
    Color bgDark = new Color(44, 62, 80);       // Main Background
    Color bgPanel = new Color(52, 73, 94);      // Lighter Panel Background
    Color accentColor = new Color(230, 126, 34); // Orange
    Color dangerColor = new Color(192, 57, 43);  // Red
    Color textWhite = Color.WHITE;

    public AdminDashboard() {
        // Window Setup
        setSize(900, 750); // Increased height to fit new section
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
        // SECTION 1: POLL SETTINGS (Question)
        // ============================
        JPanel pollPanel = createCardPanel(100, 120, 700, 150);
        add(pollPanel);

        JLabel lblPoll = new JLabel("SET ACTIVE POLL QUESTION");
        lblPoll.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPoll.setForeground(new Color(52, 152, 219)); // Light Blue
        lblPoll.setBounds(30, 20, 300, 20);
        pollPanel.add(lblPoll);

        questionField = new JTextField();
        questionField.setBounds(30, 55, 450, 45);
        questionField.setBackground(bgDark);
        questionField.setForeground(textWhite);
        questionField.setCaretColor(textWhite);
        questionField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        questionField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        pollPanel.add(questionField);

        JButton updateBtn = createStyledButton("UPDATE QUESTION", accentColor);
        updateBtn.setBounds(500, 55, 170, 45);
        pollPanel.add(updateBtn);
        
        JLabel lblHint = new JLabel("Visible to all users immediately.");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblHint.setForeground(Color.GRAY);
        lblHint.setBounds(30, 110, 400, 20);
        pollPanel.add(lblHint);


        // ============================
        // SECTION 2: MANAGE CANDIDATES (NEW!)
        // ============================
        JPanel candidatePanel = createCardPanel(100, 290, 700, 180);
        add(candidatePanel);

        JLabel lblCand = new JLabel("MANAGE CANDIDATES");
        lblCand.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblCand.setForeground(new Color(46, 204, 113)); // Green title
        lblCand.setBounds(30, 20, 300, 20);
        candidatePanel.add(lblCand);

        JLabel lblNote = new JLabel("Enter names separated by commas (e.g. Iron Man, Hulk, Thor)");
        lblNote.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNote.setForeground(Color.LIGHT_GRAY);
        lblNote.setBounds(30, 45, 400, 20);
        candidatePanel.add(lblNote);

        candidateInput = new JTextArea();
        candidateInput.setBackground(bgDark);
        candidateInput.setForeground(textWhite);
        candidateInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        candidateInput.setCaretColor(textWhite);
        
        JScrollPane scroll = new JScrollPane(candidateInput);
        scroll.setBounds(30, 70, 450, 80);
        scroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        candidatePanel.add(scroll);

        JButton startElectionBtn = createStyledButton("START ELECTION", accentColor);
        startElectionBtn.setBounds(500, 70, 170, 45);
        candidatePanel.add(startElectionBtn);


        // ============================
        // SECTION 3: DANGER ZONE (Reset)
        // ============================
        JPanel dangerPanel = createCardPanel(100, 490, 700, 120);
        dangerPanel.setBorder(BorderFactory.createLineBorder(dangerColor)); 
        add(dangerPanel);

        JLabel lblDanger = new JLabel("DANGER ZONE");
        lblDanger.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDanger.setForeground(dangerColor);
        lblDanger.setBounds(30, 20, 300, 20);
        dangerPanel.add(lblDanger);

        JLabel lblWarning = new JLabel("This will reset all votes to zero but keep the current candidates.");
        lblWarning.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblWarning.setForeground(Color.LIGHT_GRAY);
        lblWarning.setBounds(30, 55, 450, 20);
        dangerPanel.add(lblWarning);

        JButton resetBtn = createStyledButton("RESET VOTES ONLY", dangerColor);
        resetBtn.setBounds(500, 40, 170, 45);
        dangerPanel.add(resetBtn);


        // --- LOGOUT BUTTON ---
        JButton logoutBtn = createStyledButton("LOGOUT", Color.GRAY);
        logoutBtn.setBounds(760, 640, 100, 35);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(logoutBtn);


        // ============================
        // EVENTS
        // ============================
        
        // 1. Update Question
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

        // 2. Start New Election (Dynamic Candidates)
        startElectionBtn.addActionListener(e -> {
            String rawText = candidateInput.getText();
            if(rawText.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter at least one candidate name!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                "⚠️ STARTING A NEW ELECTION ⚠️\n\n1. This will DELETE old candidates.\n2. This will DELETE all existing votes.\n3. All users can vote again.\n\nProceed?", 
                "Confirm New Election", JOptionPane.YES_NO_OPTION);
                
            if(confirm == JOptionPane.YES_OPTION) {
                String[] names = rawText.split(",");
                VoteDao dao = new VoteDao();
                dao.setupNewElection(names); // Calls the method we added to VoteDao
                JOptionPane.showMessageDialog(this, "New Election Started with " + names.length + " candidates!");
            }
        });

        // 3. Reset Votes Only (Danger Zone)
        resetBtn.addActionListener(e -> {
            int check = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to RESET votes to zero?\n(Candidates will stay the same)", 
                "Confirm Reset", JOptionPane.YES_NO_OPTION);
                
            if(check == JOptionPane.YES_OPTION) {
                new VoteDao().resetVotes();
                JOptionPane.showMessageDialog(this, "Votes have been reset to zero.");
            }
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });
    }

    // --- HELPER METHODS ---

    private JPanel createCardPanel(int x, int y, int w, int h) {
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(bgPanel);
        p.setBounds(x, y, w, h);
        p.setBorder(BorderFactory.createLineBorder(new Color(60, 80, 100))); 
        return p;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Slightly smaller font to fit text
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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