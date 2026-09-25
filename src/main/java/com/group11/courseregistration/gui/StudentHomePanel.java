package com.group11.courseregistration.gui;

import com.group11.courseregistration.model.Course;
import com.group11.courseregistration.model.Student;
import com.group11.courseregistration.service.CourseService;
import com.group11.courseregistration.service.RegistrationService;
import com.group11.courseregistration.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class StudentHomePanel extends JPanel {
    private Student student;
    private RegistrationService registrationService = new RegistrationService();
    private CourseService courseService = new CourseService();

    private JLabel totalCoursesVal;
    private JLabel availCoursesVal;
    private JLabel myCoursesVal;
    private JLabel creditsVal;
    private JPanel recentWrapper;

    public StudentHomePanel(Student student) {
        this.student = student;
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UIUtils.COLOR_BACKGROUND);

        // Welcome Header
        JLabel welcomeLabel = UIUtils.createTitleLabel("Good morning, " + student.getStudentName() + " 👋");
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(welcomeLabel);

        JLabel subtitle = UIUtils.createSubtitleLabel("Here's your real-time academic overview.");
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createRigidArea(new Dimension(0, 5)));
        content.add(subtitle);
        content.add(Box.createRigidArea(new Dimension(0, 30)));

        // Statistics Cards (Grid)
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        totalCoursesVal = new JLabel("0");
        availCoursesVal = new JLabel("0");
        myCoursesVal = new JLabel("0");
        creditsVal = new JLabel("0");

        statsPanel.add(createStatCard("📚", "TOTAL COURSES", totalCoursesVal, "Courses available"));
        statsPanel.add(createStatCard("✓", "AVAILABLE COURSES", availCoursesVal, "Open for registration"));
        statsPanel.add(createStatCard("📖", "MY COURSES", myCoursesVal, "Currently registered"));
        statsPanel.add(createStatCard("🎓", "TOTAL CREDITS", creditsVal, "Registered credits"));

        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(statsPanel);

        content.add(Box.createRigidArea(new Dimension(0, 40)));

        // Recent Courses
        JLabel recentTitle = new JLabel("Recent Courses");
        recentTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        recentTitle.setForeground(UIUtils.COLOR_PRIMARY_TEXT);
        recentTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(recentTitle);

        JLabel recentSubtitle = UIUtils.createSubtitleLabel("Your recently registered courses.");
        recentSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(recentSubtitle);
        content.add(Box.createRigidArea(new Dimension(0, 15)));

        recentWrapper = new JPanel(new BorderLayout());
        recentWrapper.setBackground(UIUtils.COLOR_BACKGROUND);
        recentWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(recentWrapper);

        add(content, BorderLayout.NORTH);

        refreshData();
    }

    public void refreshData() {
        int totalCourses = courseService.getAllCourses().size();
        int availableCourses = getAvailableCoursesCount();
        int myCourses = registrationService.getStudentRegistrations(student.getStudentId()).size();
        int totalCredits = registrationService.getStudentTotalCredits(student.getStudentId());

        totalCoursesVal.setText(String.valueOf(totalCourses));
        availCoursesVal.setText(String.valueOf(availableCourses));
        myCoursesVal.setText(String.valueOf(myCourses));
        creditsVal.setText(String.valueOf(totalCredits));

        recentWrapper.removeAll();
        recentWrapper.add(createRecentCoursesPanel(), BorderLayout.CENTER);
        recentWrapper.revalidate();
        recentWrapper.repaint();
    }
    
    private int getAvailableCoursesCount() {
        int count = 0;
        for (Course c : courseService.getAllCourses()) {
            if (!c.isFull()) count++;
        }
        return count;
    }

    private JPanel createStatCard(String icon, String title, JLabel valueLabel, String subtitle) {
        JPanel card = UIUtils.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(iconLabel);
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        titleLabel.setForeground(UIUtils.COLOR_SECONDARY_TEXT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);
        
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        valueLabel.setForeground(UIUtils.COLOR_PRIMARY_TEXT);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(valueLabel);
        
        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subLabel.setForeground(UIUtils.COLOR_SECONDARY_TEXT);
        subLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(subLabel);
        
        return card;
    }

    private JPanel createRecentCoursesPanel() {
        JPanel panel = UIUtils.createCard();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        var regs = registrationService.getStudentRegistrations(student.getStudentId());
        
        if (regs.isEmpty()) {
            JLabel empty = new JLabel("No courses registered yet.");
            empty.setForeground(UIUtils.COLOR_SECONDARY_TEXT);
            panel.add(empty);
            return panel;
        }

        for (int i = 0; i < Math.min(regs.size(), 3); i++) {
            var reg = regs.get(regs.size() - 1 - i); // get latest
            Course course = courseService.getCourse(reg.getCourseId());
            if (course != null) {
                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(UIUtils.COLOR_WHITE);
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
                
                JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
                left.setBackground(UIUtils.COLOR_WHITE);
                
                JLabel idLabel = new JLabel(course.getCourseId());
                idLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                left.add(idLabel);
                
                JLabel nameLabel = new JLabel(course.getCourseName());
                left.add(nameLabel);
                
                JLabel profLabel = new JLabel(course.getFacultyName());
                profLabel.setForeground(UIUtils.COLOR_SECONDARY_TEXT);
                left.add(profLabel);
                
                JLabel credLabel = new JLabel(course.getCredits() + " Credits");
                credLabel.setForeground(UIUtils.COLOR_SECONDARY_TEXT);
                left.add(credLabel);
                
                row.add(left, BorderLayout.WEST);
                
                JLabel badge = new JLabel(" REGISTERED ");
                badge.setOpaque(true);
                badge.setBackground(UIUtils.COLOR_LIGHT_GREEN);
                badge.setForeground(UIUtils.COLOR_SUCCESS);
                badge.setFont(new Font("SansSerif", Font.BOLD, 11));
                
                JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                right.setBackground(UIUtils.COLOR_WHITE);
                right.add(badge);
                row.add(right, BorderLayout.EAST);
                
                panel.add(row);
                if (i < Math.min(regs.size(), 3) - 1) {
                    panel.add(Box.createRigidArea(new Dimension(0, 15)));
                    JSeparator sep = new JSeparator();
                    sep.setForeground(UIUtils.COLOR_BORDER);
                    sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                    panel.add(sep);
                    panel.add(Box.createRigidArea(new Dimension(0, 15)));
                }
            }
        }
        return panel;
    }
}
