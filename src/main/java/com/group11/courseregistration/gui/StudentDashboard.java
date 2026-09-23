package com.group11.courseregistration.gui;

import com.group11.courseregistration.model.Student;
import com.group11.courseregistration.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StudentDashboard extends JFrame {
    private Student student;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JLabel headerTitle;
    
    // Panels
    private StudentHomePanel homePanel;
    private CoursePanel coursePanel;
    private RegistrationPanel registrationPanel;
    private MyCoursesPanel myCoursesPanel;

    public StudentDashboard(Student student) {
        this.student = student;
        setTitle("College Course Registration - Student Portal");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());

        // Sidebar
        add(createSidebar(), BorderLayout.WEST);

        // Right side (Header + Content)
        JPanel rightSide = new JPanel(new BorderLayout());
        rightSide.add(createHeader(), BorderLayout.NORTH);
        
        // Main Content (CardLayout)
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        homePanel = new StudentHomePanel(student);
        coursePanel = new CoursePanel(student);
        registrationPanel = new RegistrationPanel(student);
        myCoursesPanel = new MyCoursesPanel(student);
        
        cardPanel.add(homePanel, "Dashboard");
        cardPanel.add(coursePanel, "Available Courses");
        cardPanel.add(registrationPanel, "Register Course");
        cardPanel.add(myCoursesPanel, "My Courses");
        
        rightSide.add(cardPanel, BorderLayout.CENTER);
        
        add(rightSide, BorderLayout.CENTER);
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
        
        JLabel logoText = new JLabel("<html><b style='color:white; font-size:14px;'>College Course</b><br><span style='color:#EFF6FF; font-size:12px;'>Registration Group 11</span></html>");
        logoPanel.add(logoText);
        
        sidebar.add(logoPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Navigation Links
        sidebar.add(createNavButton("🏠 Dashboard", "Dashboard"));
        sidebar.add(createNavButton("📚 Available Courses", "Available Courses"));
        sidebar.add(createNavButton("➕ Register Course", "Register Course"));
        sidebar.add(createNavButton("📖 My Courses", "My Courses"));
        
        sidebar.add(Box.createVerticalGlue());
        
        // Bottom Links
        sidebar.add(createNavButton("⚙ Settings", "Settings"));
        
        // Logout Button
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
                int choice = JOptionPane.showConfirmDialog(StudentDashboard.this, 
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
        
        // Highlight Dashboard initially
        if (cardName.equals("Dashboard")) {
            label.setBackground(UIUtils.COLOR_PRIMARY_BLUE);
        }
        
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!cardName.equals("Settings")) {
                    cardLayout.show(cardPanel, cardName);
                    headerTitle.setText(cardName);
                    
                    // Note: In a real app we'd manage selection state better across all buttons,
                    // but for simplicity we rely on the card layout switching successfully.
                } else {
                    JOptionPane.showMessageDialog(StudentDashboard.this, 
                        "Theme: Light\nApp: College Course Registration Management System\nGroup: 11\nVersion: 1.0", 
                        "Settings", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (label.getBackground() != UIUtils.COLOR_PRIMARY_BLUE) {
                    label.setBackground(UIUtils.COLOR_DARKER_NAVY);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (label.getBackground() != UIUtils.COLOR_PRIMARY_BLUE) {
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
        
        JLabel bell = new JLabel("🔔");
        bell.setFont(new Font("SansSerif", Font.PLAIN, 18));
        profilePanel.add(bell);
        
        JLabel avatar = new JLabel(" " + student.getStudentName().substring(0, 1).toUpperCase() + " ");
        avatar.setOpaque(true);
        avatar.setBackground(UIUtils.COLOR_PRIMARY_BLUE);
        avatar.setForeground(UIUtils.COLOR_WHITE);
        avatar.setFont(new Font("SansSerif", Font.BOLD, 16));
        avatar.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_PRIMARY_BLUE, 5, true));
        profilePanel.add(avatar);
        
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setBackground(UIUtils.COLOR_WHITE);
        
        JLabel name = new JLabel(student.getStudentName());
        name.setFont(new Font("SansSerif", Font.BOLD, 14));
        namePanel.add(name);
        
        JLabel details = new JLabel(student.getDepartment() + " • Semester " + student.getSemester());
        details.setFont(new Font("SansSerif", Font.PLAIN, 12));
        details.setForeground(UIUtils.COLOR_SECONDARY_TEXT);
        namePanel.add(details);
        
        profilePanel.add(namePanel);
        
        header.add(profilePanel, BorderLayout.EAST);
        return header;
    }
}
