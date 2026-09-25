package com.group11.courseregistration.gui;

import com.group11.courseregistration.model.Student;
import com.group11.courseregistration.service.StudentService;
import com.group11.courseregistration.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    
    private StudentService studentService = new StudentService();

    public LoginFrame() {
        setTitle("College Course Registration - Login");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());

        // Left Panel (45%)
        JPanel leftPanel = createLeftPanel();
        leftPanel.setPreferredSize(new Dimension((int)(1100 * 0.45), 700));
        add(leftPanel, BorderLayout.WEST);

        // Right Panel (55%)
        JPanel rightPanel = createRightPanel();
        add(rightPanel, BorderLayout.CENTER);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIUtils.COLOR_DARK_NAVY);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel iconLabel = new JLabel("🎓");
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 64));
        iconLabel.setForeground(UIUtils.COLOR_WHITE);
        panel.add(iconLabel, gbc);
        
        JLabel title1 = new JLabel("COLLEGE COURSE");
        title1.setFont(new Font("SansSerif", Font.BOLD, 24));
        title1.setForeground(UIUtils.COLOR_WHITE);
        panel.add(title1, gbc);
        
        JLabel title2 = new JLabel("REGISTRATION");
        title2.setFont(new Font("SansSerif", Font.BOLD, 24));
        title2.setForeground(UIUtils.COLOR_WHITE);
        panel.add(title2, gbc);
        
        gbc.insets = new Insets(30, 10, 30, 10);
        JLabel groupLabel = new JLabel("MANAGEMENT SYSTEM - GROUP 11");
        groupLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        groupLabel.setForeground(UIUtils.COLOR_LIGHT_BLUE);
        panel.add(groupLabel, gbc);
        
        JLabel subtitle1 = new JLabel("Manage your courses.");
        subtitle1.setFont(new Font("SansSerif", Font.ITALIC, 16));
        subtitle1.setForeground(UIUtils.COLOR_SECONDARY_TEXT);
        panel.add(subtitle1, gbc);
        
        JLabel subtitle2 = new JLabel("Plan your semester.");
        subtitle2.setFont(new Font("SansSerif", Font.ITALIC, 16));
        subtitle2.setForeground(UIUtils.COLOR_SECONDARY_TEXT);
        panel.add(subtitle2, gbc);
        
        JLabel subtitle3 = new JLabel("Build your future.");
        subtitle3.setFont(new Font("SansSerif", Font.ITALIC, 16));
        subtitle3.setForeground(UIUtils.COLOR_SECONDARY_TEXT);
        panel.add(subtitle3, gbc);
        
        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIUtils.COLOR_BACKGROUND);
        
        JPanel card = UIUtils.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(400, 450));
        
        // Add elements to card
        JLabel welcomeLabel = UIUtils.createTitleLabel("Welcome Back");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(welcomeLabel);
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel subtitle = UIUtils.createSubtitleLabel("Sign in to continue to your portal.");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitle);
        
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Form fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(UIUtils.COLOR_WHITE);
        
        JLabel userLabel = new JLabel("Username / Student ID");
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(userLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JTextField userField = UIUtils.createStyledTextField();
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        formPanel.add(userField);
        
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel passLabel = new JLabel("Password");
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JPasswordField passField = UIUtils.createStyledPasswordField();
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        formPanel.add(passField);
        
        card.add(formPanel);
        
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JButton loginButton = UIUtils.createPrimaryButton("Sign In →");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            
            if (username.equals("admin") && password.equals("admin123")) {
                AdminDashboard adminDashboard = new AdminDashboard();
                adminDashboard.setVisible(true);
                this.dispose();
            } else {
                Student student = studentService.login(username, password);
                if (student != null) {
                    StudentDashboard studentDashboard = new StudentDashboard(student);
                    studentDashboard.setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        card.add(loginButton);
        getRootPane().setDefaultButton(loginButton);
        
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Demo accounts text
        JLabel demoText = new JLabel("<html><center><b>Demo Accounts</b><br>Student: student / student123<br>Admin: admin / admin123</center></html>");
        demoText.setAlignmentX(Component.CENTER_ALIGNMENT);
        demoText.setForeground(UIUtils.COLOR_SECONDARY_TEXT);
        demoText.setFont(UIUtils.FONT_SMALL);
        card.add(demoText);
        
        panel.add(card);
        
        return panel;
    }
}
