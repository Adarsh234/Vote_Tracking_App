package com.adarsh.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image; // Import java.awt.Image

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class SplaceScreen extends JWindow {
    
    JProgressBar progressBar;
    
    // --- THEME COLORS ---
    Color bgDark = new Color(44, 62, 80);       // Dark Blue Background
    Color accentColor = new Color(230, 126, 34); // Orange Accent

    SplaceScreen() {
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(bgDark);
        setContentPane(contentPanel);

        ImageIcon originalIcon = new ImageIcon("images/vote_logo.png");
        
        int targetWidth = 400;
        int targetHeight = 300;

        Image scaledImage = originalIcon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        
        ImageIcon finalIcon = new ImageIcon(scaledImage);
        
        JLabel lb = new JLabel(finalIcon);
        
        lb.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(lb, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(bgDark);
        bottomPanel.setBorder(new EmptyBorder(0, 0, 50, 0)); // Add padding at bottom so it's not stuck to edge

        JLabel loadingText = new JLabel("Loading System Modules...", SwingConstants.CENTER);
        loadingText.setForeground(Color.WHITE);
        loadingText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loadingText.setBorder(new EmptyBorder(0, 0, 10, 0)); // Space between text and bar
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(accentColor);
        progressBar.setBackground(new Color(236, 240, 241));
        progressBar.setBorderPainted(false);
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        progressBar.setUI(new BasicProgressBarUI() {
            @Override
            protected Color getSelectionBackground() { return Color.BLACK; }
            @Override
            protected Color getSelectionForeground() { return Color.BLACK; }
        });

        JPanel barContainer = new JPanel(new BorderLayout());
        barContainer.setBackground(bgDark);
        barContainer.setBorder(new EmptyBorder(0, 100, 0, 100)); // 100px padding on Left/Right
        barContainer.add(progressBar, BorderLayout.CENTER);

        bottomPanel.add(loadingText, BorderLayout.NORTH);
        bottomPanel.add(barContainer, BorderLayout.SOUTH);

        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        loadProgress();
    }

    Timer timer;
    void loadProgress() {
        timer = new Timer(30, e -> {
            int val = progressBar.getValue();
            if (val < 100) {
                progressBar.setValue(val + 1);
                progressBar.setString(val + "%");
                
            } else {
                timer.stop();
                dispose();
                Login login = new Login();
                login.setVisible(true);
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        SplaceScreen splaceScreen = new SplaceScreen();
        splaceScreen.setVisible(true);
    }
}