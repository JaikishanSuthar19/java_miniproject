package com.group11.courseregistration.gui;

import com.group11.courseregistration.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class AdminDashboard extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JLabel headerTitle;

    // Sub-panels
    private AdminHomePanel homePanel;
    private AdminCoursePanel coursePanel;
    private AdminStudentPanel studentPanel;
    private AdminFacultyPanel facultyPanel;
    private AdminRegistrationPanel registrationPanel;

    // Navigation button tracking for active highlight
    private Map<String, JLabel> navButtons = new HashMap<>();

    public AdminDashboard() {
        setTitle("College Course Registration - Admin Portal");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());

        // Sidebar
        add(createSidebar(), BorderLayout.WEST);

        // Right side
        JPanel rightSide = new JPanel(new BorderLayout());
        rightSide.add(createHeader(), BorderLayout.NORTH);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        homePanel = new AdminHomePanel(this);
        coursePanel = new AdminCoursePanel();
        studentPanel = new AdminStudentPanel();
        facultyPanel = new AdminFacultyPanel();
        registrationPanel = new AdminRegistrationPanel();

        cardPanel.add(homePanel, "Dashboard");
        cardPanel.add(coursePanel, "Manage Courses");
        cardPanel.add(studentPanel, "Students");
        cardPanel.add(facultyPanel, "Faculty");
        cardPanel.add(registrationPanel, "Registrations");
        
        rightSide.add(cardPanel, BorderLayout.CENTER);
        
        add(rightSide, BorderLayout.CENTER);

        // Highlight initial tab
        updateNavSelection("Dashboard");
    }
    
    public void switchTab(String name) {
        cardLayout.show(cardPanel, name);
        headerTitle.setText(name);
        updateNavSelection(name);

        // Auto-refresh target tab
        if ("Dashboard".equals(name)) {
            homePanel.refreshData();
        } else if ("Manage Courses".equals(name)) {
            coursePanel.loadData();
        } else if ("Students".equals(name)) {
            studentPanel.loadData("");
        } else if ("Faculty".equals(name)) {
            facultyPanel.loadData("");
        } else if ("Registrations".equals(name)) {
            registrationPanel.loadData();
        }
    }

    private void updateNavSelection(String activeTab) {
        for (Map.Entry<String, JLabel> entry : navButtons.entrySet()) {
            if (entry.getKey().equals(activeTab)) {
                entry.getValue().setBackground(UIUtils.COLOR_PRIMARY_BLUE);
            } else {
                entry.getValue().setBackground(UIUtils.COLOR_DARK_NAVY);
            }
        }
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIUtils.COLOR_DARK_NAVY);
        sidebar.setPreferredSize(new Dimension(240, 750));
        
        // Logo area
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        logoPanel.setBackground(UIUtils.COLOR_DARK_NAVY);
        logoPanel.setMaximumSize(new Dimension(240, 80));
        
        JLabel logo = new JLabel("🎓");
        logo.setFont(new Font("SansSerif", Font.PLAIN, 28));
        logo.setForeground(UIUtils.COLOR_WHITE);
        logoPanel.add(logo);
        
        JLabel logoText = new JLabel("<html><b style='color:white; font-size:14px;'>Admin Portal</b><br><span style='color:#EFF6FF; font-size:12px;'>Group 11</span></html>");
        logoPanel.add(logoText);
        
        sidebar.add(logoPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        
        sidebar.add(createNavButton("🏠 Dashboard", "Dashboard"));
        sidebar.add(createNavButton("📚 Manage Courses", "Manage Courses"));
        sidebar.add(createNavButton("👥 Students", "Students"));
        sidebar.add(createNavButton("👨‍🏫 Faculty", "Faculty"));
        sidebar.add(createNavButton("📋 Registrations", "Registrations"));
        
        sidebar.add(Box.createVerticalGlue());
        
        sidebar.add(createNavButton("⚙ Settings", "Settings"));
        
        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setBackground(UIUtils.COLOR_DARK_NAVY);
        logoutPanel.setMaximumSize(new Dimension(240, 50));
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel logoutLabel = new JLabel("🚪 Logout");
        logoutLabel.setForeground(UIUtils.COLOR_WHITE);
        logoutLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        logoutLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        logoutLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int choice = JOptionPane.showConfirmDialog(AdminDashboard.this, 
                    "Are you sure you want to logout?", "Logout", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.OK_OPTION) {
                    new LoginFrame().setVisible(true);
                    dispose();
                }
            }
        });
        
        logoutPanel.add(logoutLabel, BorderLayout.CENTER);
        sidebar.add(logoutPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        
        return sidebar;
    }
    
    private JPanel createNavButton(String text, String cardName) {
        JPanel btnPanel = new JPanel(new BorderLayout());
        btnPanel.setBackground(UIUtils.COLOR_DARK_NAVY);
        btnPanel.setMaximumSize(new Dimension(240, 50));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel label = new JLabel(text);
        label.setForeground(UIUtils.COLOR_WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        label.setOpaque(true);
        label.setBackground(UIUtils.COLOR_DARK_NAVY);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (!cardName.equals("Settings")) {
            navButtons.put(cardName, label);
        }
        
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!cardName.equals("Settings")) {
                    switchTab(cardName);
                } else {
                    JOptionPane.showMessageDialog(AdminDashboard.this, 
                        "Theme: Modern SaaS (FlatLaf)\nApp: College Course Registration Management System\nGroup: 11\nVersion: 1.0", 
                        "System Information", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!label.getBackground().equals(UIUtils.COLOR_PRIMARY_BLUE)) {
                    label.setBackground(UIUtils.COLOR_DARKER_NAVY);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!label.getBackground().equals(UIUtils.COLOR_PRIMARY_BLUE)) {
                    label.setBackground(UIUtils.COLOR_DARK_NAVY);
                }
            }
        });
        
        btnPanel.add(label, BorderLayout.CENTER);
        return btnPanel;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIUtils.COLOR_WHITE);
        header.setPreferredSize(new Dimension(960, 65));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIUtils.COLOR_BORDER));
        
        headerTitle = new JLabel("Dashboard");
        headerTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerTitle.setForeground(UIUtils.COLOR_PRIMARY_TEXT);
        headerTitle.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        header.add(headerTitle, BorderLayout.WEST);
        
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        profilePanel.setBackground(UIUtils.COLOR_WHITE);
        
        JLabel avatar = new JLabel(" A ");
        avatar.setOpaque(true);
        avatar.setBackground(UIUtils.COLOR_PRIMARY_BLUE);
        avatar.setForeground(UIUtils.COLOR_WHITE);
        avatar.setFont(new Font("SansSerif", Font.BOLD, 16));
        avatar.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_PRIMARY_BLUE, 5, true));
        profilePanel.add(avatar);
        
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setBackground(UIUtils.COLOR_WHITE);
        
        JLabel name = new JLabel("Administrator");
        name.setFont(new Font("SansSerif", Font.BOLD, 14));
        namePanel.add(name);
        
        JLabel details = new JLabel("System Admin • Full Control");
        details.setFont(new Font("SansSerif", Font.PLAIN, 12));
        details.setForeground(UIUtils.COLOR_SECONDARY_TEXT);
        namePanel.add(details);
        
        profilePanel.add(namePanel);
        header.add(profilePanel, BorderLayout.EAST);
        return header;
    }
}
