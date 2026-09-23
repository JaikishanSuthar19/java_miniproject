package com.group11.courseregistration.gui;

import com.group11.courseregistration.model.Course;
import com.group11.courseregistration.model.Student;
import com.group11.courseregistration.service.CourseService;
import com.group11.courseregistration.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class CoursePanel extends JPanel {
    private Student student;
    private CourseService courseService = new CourseService();
    private DefaultTableModel tableModel;
    private JTable table;

    public CoursePanel(Student student) {
        this.student = student;
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(UIUtils.COLOR_BACKGROUND);

        JLabel title = UIUtils.createTitleLabel("Available Courses");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(title);
        
        JLabel subtitle = UIUtils.createSubtitleLabel("Browse courses available for your semester.");
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        JTextField searchField = UIUtils.createStyledTextField();
        searchField.setPreferredSize(new Dimension(300, 40));
        searchField.setToolTipText("Search by course ID, name, or faculty...");
        searchPanel.add(searchField);
        
        JButton searchBtn = UIUtils.createPrimaryButton("Search");
        searchBtn.addActionListener(e -> loadData(searchField.getText()));
        searchPanel.add(searchBtn);
        
        headerPanel.add(searchPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Course ID", "Course Name", "Faculty", "Credits", "Capacity", "Registered", "Available", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No direct editing
            }
        };

        table = new JTable(tableModel);
        UIUtils.styleTable(table);

        // Custom renderer for Status column
        table.getColumnModel().getColumn(7).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(UIUtils.COLOR_WHITE);
        scrollPane.getViewport().setBackground(UIUtils.COLOR_WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDER));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(UIUtils.COLOR_BACKGROUND);
        tableContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);

        loadData("");
    }

    private void loadData(String query) {
        tableModel.setRowCount(0);
        List<Course> courses;
        if (query == null || query.trim().isEmpty()) {
            courses = courseService.getSortedCourses();
        } else {
            courses = courseService.searchCourses(query);
        }

        for (Course c : courses) {
            String status = c.isFull() ? "FULL" : "AVAILABLE";
            tableModel.addRow(new Object[]{
                    c.getCourseId(),
                    c.getCourseName(),
                    c.getFacultyName(),
                    c.getCredits(),
                    c.getCapacity(),
                    c.getRegisteredStudents(),
                    c.getAvailableSeats(),
                    status
            });
        }
    }

    // Custom cell renderer for status badges
    class StatusCellRenderer extends JLabel implements TableCellRenderer {
        public StatusCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setFont(new Font("SansSerif", Font.BOLD, 11));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String status = (String) value;
            setText(status);
            if ("AVAILABLE".equals(status)) {
                setBackground(UIUtils.COLOR_LIGHT_GREEN);
                setForeground(UIUtils.COLOR_SUCCESS);
            } else {
                setBackground(UIUtils.COLOR_LIGHT_RED);
                setForeground(UIUtils.COLOR_DANGER);
            }
            return this;
        }
    }
}
