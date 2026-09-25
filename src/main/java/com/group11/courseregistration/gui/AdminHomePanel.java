package com.group11.courseregistration.gui;

import com.group11.courseregistration.model.Course;
import com.group11.courseregistration.service.CourseService;
import com.group11.courseregistration.service.FacultyService;
import com.group11.courseregistration.service.RegistrationService;
import com.group11.courseregistration.service.StudentService;
import com.group11.courseregistration.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class AdminHomePanel extends JPanel {
    private StudentService studentService = new StudentService();
    private CourseService courseService = new CourseService();
    private FacultyService facultyService = new FacultyService();
    private RegistrationService regService = new RegistrationService();
    private AdminDashboard parent;

    private JLabel studentsVal;
    private JLabel coursesVal;
    private JLabel facultyVal;
    private JLabel regsVal;
    private JLabel seatsVal;

    public AdminHomePanel(AdminDashboard parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UIUtils.COLOR_BACKGROUND);

        JLabel welcomeLabel = UIUtils.createTitleLabel("Good morning, Admin 👋");
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(welcomeLabel);

        JLabel subtitle = UIUtils.createSubtitleLabel("Here's your comprehensive system overview.");
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createRigidArea(new Dimension(0, 5)));
        content.add(subtitle);
        content.add(Box.createRigidArea(new Dimension(0, 30)));

        // Statistics Cards (5 cards in grid)
        JPanel statsPanel = new JPanel(new GridLayout(1, 5, 15, 0));
        statsPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        studentsVal = new JLabel("0");
        coursesVal = new JLabel("0");
        facultyVal = new JLabel("0");
        regsVal = new JLabel("0");
        seatsVal = new JLabel("0");

        statsPanel.add(createStatCard("👥", "TOTAL STUDENTS", studentsVal));
        statsPanel.add(createStatCard("📚", "TOTAL COURSES", coursesVal));
        statsPanel.add(createStatCard("👨‍🏫", "TOTAL FACULTY", facultyVal));
        statsPanel.add(createStatCard("📋", "REGISTRATIONS", regsVal));
        statsPanel.add(createStatCard("🪑", "OPEN SEATS", seatsVal));

        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(statsPanel);

        content.add(Box.createRigidArea(new Dimension(0, 40)));

        // Quick Actions
        JLabel qaTitle = new JLabel("Quick Actions");
        qaTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        qaTitle.setForeground(UIUtils.COLOR_PRIMARY_TEXT);
        qaTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(qaTitle);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actionPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton btnManage = UIUtils.createPrimaryButton("Manage Courses");
        btnManage.addActionListener(e -> parent.switchTab("Manage Courses"));
        actionPanel.add(btnManage);
        
        JButton btnStudents = UIUtils.createPrimaryButton("View Students");
        btnStudents.addActionListener(e -> parent.switchTab("Students"));
        actionPanel.add(btnStudents);

        JButton btnFaculty = UIUtils.createPrimaryButton("Faculty Members");
        btnFaculty.addActionListener(e -> parent.switchTab("Faculty"));
        actionPanel.add(btnFaculty);
        
        JButton btnRegs = UIUtils.createPrimaryButton("View Registrations");
        btnRegs.addActionListener(e -> parent.switchTab("Registrations"));
        actionPanel.add(btnRegs);

        content.add(actionPanel);
        
        add(content, BorderLayout.NORTH);

        refreshData();
    }

    public void refreshData() {
        int totalStudents = studentService.getAllStudents().size();
        int totalCourses = courseService.getAllCourses().size();
        int totalFaculty = facultyService.getAllFaculties().size();
        int totalRegs = regService.getAllRegistrations().size();
        
        int availableSeats = 0;
        for (Course c : courseService.getAllCourses()) {
            availableSeats += c.getAvailableSeats();
        }

        studentsVal.setText(String.valueOf(totalStudents));
        coursesVal.setText(String.valueOf(totalCourses));
        facultyVal.setText(String.valueOf(totalFaculty));
        regsVal.setText(String.valueOf(totalRegs));
        seatsVal.setText(String.valueOf(availableSeats));
    }
    
    private JPanel createStatCard(String icon, String title, JLabel valueLabel) {
        JPanel card = UIUtils.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(iconLabel);
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        titleLabel.setForeground(UIUtils.COLOR_SECONDARY_TEXT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);
        
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        valueLabel.setForeground(UIUtils.COLOR_PRIMARY_TEXT);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(valueLabel);
        
        return card;
    }
}
