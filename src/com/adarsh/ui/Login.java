package com.adarsh.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.adarsh.dao.UserDao;
import com.adarsh.dto.UserDto;

public class Login extends JFrame {
    JTextField userid;
    JPasswordField passid;

    Color bgDark = new Color(44, 62, 80); 
    Color accentColor = new Color(230, 126, 34); 
    Color textWhite = Color.WHITE; 

    public Login() {
        setSize(900, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(bgDark);
        getContentPane().setLayout(null);

        JLabel title = new JLabel("VOTING SYSTEM", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(textWhite);
        title.setBounds(0, 80, 900, 50); // Full width to center text
        add(title);
        
        JLabel subTitle = new JLabel("Admin Login", SwingConstants.CENTER);
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subTitle.setForeground(new Color(189, 195, 199)); // Light Grey
        subTitle.setBounds(0, 130, 900, 30);
        add(subTitle);

        JLabel userlb = new JLabel("USERNAME");
        userlb.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userlb.setForeground(textWhite);
        userlb.setBounds(250, 220, 100, 30);
        add(userlb);
        
        userid = new JTextField();
        userid.setBounds(250, 250, 400, 45); // Wider and taller
        userid.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userid.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding inside
        ));
        add(userid);

        JLabel passlb = new JLabel("PASSWORD");
        passlb.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passlb.setForeground(textWhite);
        passlb.setBounds(250, 310, 100, 30);
        add(passlb);
        
        passid = new JPasswordField();
        passid.setBounds(250, 340, 400, 45);
        passid.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passid.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        add(passid);

        JButton loginBt = new JButton("LOGIN");
        loginBt.setBounds(250, 430, 400, 50);
        loginBt.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginBt.setBackground(accentColor);
        loginBt.setForeground(Color.WHITE);
        loginBt.setFocusPainted(false); // Remove dotted line on click
        loginBt.setBorder(BorderFactory.createEmptyBorder()); // Flat look
        loginBt.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand pointer on hover
        
        loginBt.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginBt.setBackground(new Color(211, 84, 0)); // Darker orange on hover
            }
            public void mouseExited(MouseEvent e) {
                loginBt.setBackground(accentColor); // Back to normal
            }
        });
        
        add(loginBt);

        JLabel footer = new JLabel("© 2026 JAVA Voting Projects", SwingConstants.CENTER);
        footer.setForeground(Color.GRAY);
        footer.setBounds(0, 620, 900, 30);
        add(footer);

        loginBt.addActionListener(e -> doLogin());
    }

    void doLogin() {
        String uid = userid.getText();
        String password = new String(passid.getPassword());

        UserDto dto = new UserDto();
        dto.setUserid(uid);
        dto.setPassword(password);

        UserDao dao = new UserDao();
        
        try {
            if (dao.auth(dto)) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                this.dispose(); 
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid UserID or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "System Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Login().setVisible(true);
    }
}