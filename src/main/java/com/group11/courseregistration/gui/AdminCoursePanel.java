package com.group11.courseregistration.gui;

import com.group11.courseregistration.model.Course;
import com.group11.courseregistration.service.CourseService;
import com.group11.courseregistration.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class AdminCoursePanel extends JPanel {
    private CourseService courseService = new CourseService();
    private DefaultTableModel tableModel;
    private JTable table;

    public AdminCoursePanel() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(UIUtils.COLOR_BACKGROUND);

        JLabel title = UIUtils.createTitleLabel("Manage Courses");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(title);
        
        JLabel subtitle = UIUtils.createSubtitleLabel("Create, update and manage college courses.");
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Add Course Button
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        
        JButton addBtn = UIUtils.createPrimaryButton("+ Add Course");
        addBtn.addActionListener(e -> showAddCourseDialog());
        actionPanel.add(addBtn);
        
        headerPanel.add(actionPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Course ID", "Course", "Faculty", "Credits", "Capacity", "Registered", "Available"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        UIUtils.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(UIUtils.COLOR_WHITE);
        scrollPane.getViewport().setBackground(UIUtils.COLOR_WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDER));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(UIUtils.COLOR_BACKGROUND);
        tableContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        // Add Edit / Delete buttons below table for selected row
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JButton editBtn = UIUtils.createPrimaryButton("Edit Selected");
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String courseId = (String) table.getValueAt(row, 0);
                showEditCourseDialog(courseId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a course to edit.");
            }
        });
        
        JButton delBtn = UIUtils.createDangerButton("Delete Selected");
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String courseId = (String) table.getValueAt(row, 0);
                Course course = courseService.getCourse(courseId);
                if (course != null && course.getRegisteredStudents() > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Cannot delete course '" + course.getCourseName() + "'!\n" +
                        course.getRegisteredStudents() + " student(s) are actively enrolled.\n" +
                        "Registrations must be cancelled before deleting.",
                        "Course Deletion Denied", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete course '" + (course != null ? course.getCourseName() : courseId) + "'?\nAll course information will be permanently removed.",
                    "Confirm Deletion",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.OK_OPTION) {
                    courseService.deleteCourse(courseId);
                    loadData();
                    JOptionPane.showMessageDialog(this, "Course deleted successfully.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a course to delete.");
            }
        });
        
        bottomPanel.add(editBtn);
        bottomPanel.add(delBtn);
        tableContainer.add(bottomPanel, BorderLayout.SOUTH);

        add(tableContainer, BorderLayout.CENTER);

        loadData();
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<Course> courses = courseService.getSortedCourses();

        for (Course c : courses) {
            tableModel.addRow(new Object[]{
                    c.getCourseId(),
                    c.getCourseName(),
                    c.getFacultyName(),
                    c.getCredits(),
                    c.getCapacity(),
                    c.getRegisteredStudents(),
                    c.getAvailableSeats()
            });
        }
    }
    
    private void showAddCourseDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Course", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.setBackground(UIUtils.COLOR_WHITE);
        
        JTextField idF = addFormField(p, "Course ID");
        JTextField nameF = addFormField(p, "Course Name");
        JTextField facF = addFormField(p, "Faculty");
        JTextField credF = addFormField(p, "Credits");
        JTextField capF = addFormField(p, "Capacity");
        
        p.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnP.setBackground(UIUtils.COLOR_WHITE);
        
        JButton cBtn = new JButton("Cancel");
        cBtn.addActionListener(e -> dialog.dispose());
        JButton aBtn = UIUtils.createPrimaryButton("Add Course");
        aBtn.addActionListener(e -> {
            try {
                String id = idF.getText();
                String name = nameF.getText();
                String fac = facF.getText();
                int cred = Integer.parseInt(credF.getText());
                int cap = Integer.parseInt(capF.getText());
                
                if (id.isEmpty() || name.isEmpty() || fac.isEmpty() || cred <= 0 || cap <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Please enter valid data.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Course newCourse = new Course(id, name, fac, cred, cap);
                if (courseService.addCourse(newCourse)) {
                    loadData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Course ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Credits and Capacity must be numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnP.add(cBtn);
        btnP.add(aBtn);
        p.add(btnP);
        
        dialog.add(p);
        dialog.setVisible(true);
    }
    
    private void showEditCourseDialog(String courseId) {
        Course c = courseService.getCourse(courseId);
        if (c == null) return;
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Course", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.setBackground(UIUtils.COLOR_WHITE);
        
        JTextField nameF = addFormField(p, "Course Name");
        nameF.setText(c.getCourseName());
        JTextField facF = addFormField(p, "Faculty");
        facF.setText(c.getFacultyName());
        JTextField credF = addFormField(p, "Credits");
        credF.setText(String.valueOf(c.getCredits()));
        JTextField capF = addFormField(p, "Capacity");
        capF.setText(String.valueOf(c.getCapacity()));
        
        p.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnP.setBackground(UIUtils.COLOR_WHITE);
        
        JButton cBtn = new JButton("Cancel");
        cBtn.addActionListener(e -> dialog.dispose());
        JButton sBtn = UIUtils.createPrimaryButton("Save Changes");
        sBtn.addActionListener(e -> {
            try {
                String name = nameF.getText();
                String fac = facF.getText();
                int cred = Integer.parseInt(credF.getText());
                int cap = Integer.parseInt(capF.getText());
                
                if (name.isEmpty() || fac.isEmpty() || cred <= 0 || cap <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Please enter valid data.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Course updated = new Course(c.getCourseId(), name, fac, cred, cap);
                if (courseService.updateCourse(updated)) {
                    loadData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Capacity cannot be less than registered students.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Credits and Capacity must be numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnP.add(cBtn);
        btnP.add(sBtn);
        p.add(btnP);
        
        dialog.add(p);
        dialog.setVisible(true);
    }
    
    private JTextField addFormField(JPanel p, String labelText) {
        JLabel l = new JLabel(labelText);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l);
        p.add(Box.createRigidArea(new Dimension(0, 5)));
        JTextField f = UIUtils.createStyledTextField();
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        p.add(f);
        p.add(Box.createRigidArea(new Dimension(0, 15)));
        return f;
    }
}
