package com.group11.courseregistration.gui;

import com.group11.courseregistration.model.Course;
import com.group11.courseregistration.model.Student;
import com.group11.courseregistration.service.CourseService;
import com.group11.courseregistration.service.RegistrationService;
import com.group11.courseregistration.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RegistrationPanel extends JPanel {
    private Student student;
    private CourseService courseService = new CourseService();
    private RegistrationService registrationService = new RegistrationService();
    private JComboBox<String> courseComboBox;
    private JPanel infoCard;

    public RegistrationPanel(Student student) {
        this.student = student;
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        JLabel title = UIUtils.createTitleLabel("Register for a Course");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(title);
        
        JLabel subtitle = UIUtils.createSubtitleLabel("Choose a course to add to your semester.");
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        // Course Selector
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectorPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        selectorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JLabel selectLabel = new JLabel("Select Course: ");
        selectLabel.setFont(UIUtils.FONT_NORMAL);
        selectorPanel.add(selectLabel);

        courseComboBox = new JComboBox<>();
        courseComboBox.setPreferredSize(new Dimension(300, 40));
        loadCourseDropdown();
        selectorPanel.add(courseComboBox);

        centerPanel.add(selectorPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Course Info Card
        infoCard = UIUtils.createCard();
        infoCard.setLayout(new BoxLayout(infoCard, BoxLayout.Y_AXIS));
        infoCard.setMaximumSize(new Dimension(500, 400));
        infoCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerPanel.add(infoCard);
        
        courseComboBox.addActionListener(e -> updateCourseInfo());

        add(centerPanel, BorderLayout.CENTER);
        
        // Initialize with first item if available
        if (courseComboBox.getItemCount() > 0) {
            courseComboBox.setSelectedIndex(0);
        }
    }

    public void refreshPanel() {
        loadCourseDropdown();
        if (courseComboBox.getItemCount() > 0) {
            updateCourseInfo();
        }
    }

    public void loadCourseDropdown() {
        courseComboBox.removeAllItems();
        List<Course> courses = courseService.getSortedCourses();
        for (Course c : courses) {
            courseComboBox.addItem(c.getCourseId() + " - " + c.getCourseName());
        }
    }

    public void updateCourseInfo() {
        infoCard.removeAll();
        String selected = (String) courseComboBox.getSelectedItem();
        if (selected == null) return;

        String courseId = selected.split(" - ")[0];
        Course c = courseService.getCourse(courseId);

        if (c != null) {
            JLabel nameLabel = new JLabel(c.getCourseName());
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
            nameLabel.setForeground(UIUtils.COLOR_PRIMARY_TEXT);
            infoCard.add(nameLabel);
            
            JLabel idLabel = new JLabel(c.getCourseId());
            idLabel.setFont(UIUtils.FONT_NORMAL);
            idLabel.setForeground(UIUtils.COLOR_SECONDARY_TEXT);
            infoCard.add(idLabel);
            infoCard.add(Box.createRigidArea(new Dimension(0, 20)));

            infoCard.add(createInfoRow("Faculty", c.getFacultyName()));
            infoCard.add(createInfoRow("Credits", String.valueOf(c.getCredits())));
            infoCard.add(createInfoRow("Course Capacity", String.valueOf(c.getCapacity())));
            infoCard.add(createInfoRow("Registered", String.valueOf(c.getRegisteredStudents())));
            infoCard.add(createInfoRow("Available Seats", String.valueOf(c.getAvailableSeats())));
            
            String status = c.isFull() ? "FULL" : "AVAILABLE";
            infoCard.add(createInfoRow("Status", status));
            
            infoCard.add(Box.createRigidArea(new Dimension(0, 30)));
            
            JButton regBtn = UIUtils.createPrimaryButton("Confirm Registration");
            if (c.isFull()) regBtn.setEnabled(false);
            
            regBtn.addActionListener(e -> attemptRegistration(c.getCourseId()));
            
            infoCard.add(regBtn);
        }

        infoCard.revalidate();
        infoCard.repaint();
    }
    
    private JPanel createInfoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(UIUtils.COLOR_WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel l = new JLabel(label);
        l.setForeground(UIUtils.COLOR_SECONDARY_TEXT);
        l.setFont(UIUtils.FONT_NORMAL);
        row.add(l, BorderLayout.WEST);
        
        JLabel v = new JLabel(value);
        v.setForeground(UIUtils.COLOR_PRIMARY_TEXT);
        v.setFont(new Font("SansSerif", Font.BOLD, 14));
        row.add(v, BorderLayout.EAST);
        
        return row;
    }
    
    private void attemptRegistration(String courseId) {
        RegistrationService.RegistrationResult result = registrationService.registerStudent(student.getStudentId(), courseId);
        
        if (result == RegistrationService.RegistrationResult.SUCCESS) {
            JOptionPane.showMessageDialog(this, "You are now registered for " + courseId, "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
            updateCourseInfo(); // Refresh stats
        } else if (result == RegistrationService.RegistrationResult.DUPLICATE) {
            JOptionPane.showMessageDialog(this, "You are already registered for this course.", "Registration Failed", JOptionPane.WARNING_MESSAGE);
        } else if (result == RegistrationService.RegistrationResult.FULL) {
            JOptionPane.showMessageDialog(this, "This course is full.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
        } else if (result == RegistrationService.RegistrationResult.CREDIT_LIMIT_EXCEEDED) {
            JOptionPane.showMessageDialog(this,
                "Registration denied: Adding this course exceeds the semester maximum of " +
                RegistrationService.MAX_CREDITS + " credits.",
                "Credit Limit Exceeded", JOptionPane.WARNING_MESSAGE);
        }
    }
}
