package com.group11.courseregistration.gui;

import com.group11.courseregistration.model.Student;
import com.group11.courseregistration.service.StudentService;
import com.group11.courseregistration.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminStudentPanel extends JPanel {
    private StudentService studentService = new StudentService();
    private DefaultTableModel tableModel;
    private JTable table;

    public AdminStudentPanel() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(UIUtils.COLOR_BACKGROUND);

        JLabel title = UIUtils.createTitleLabel("Students");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(title);
        
        JLabel subtitle = UIUtils.createSubtitleLabel("View registered students.");
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        JTextField searchField = UIUtils.createStyledTextField();
        searchField.setPreferredSize(new Dimension(300, 40));
        searchField.setToolTipText("Search by ID, name or department...");
        searchPanel.add(searchField);
        
        JButton searchBtn = UIUtils.createPrimaryButton("Search");
        searchBtn.addActionListener(e -> loadData(searchField.getText()));
        searchPanel.add(searchBtn);
        
        headerPanel.add(searchPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Student ID", "Name", "Email", "Department", "Semester"};
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

        add(tableContainer, BorderLayout.CENTER);

        loadData("");
    }

    private void loadData(String query) {
        tableModel.setRowCount(0);
        List<Student> students;
        if (query == null || query.trim().isEmpty()) {
            students = studentService.getAllStudents();
        } else {
            students = studentService.searchStudents(query);
        }

        for (Student s : students) {
            tableModel.addRow(new Object[]{
                    s.getStudentId(),
                    s.getStudentName(),
                    s.getEmail(),
                    s.getDepartment(),
                    s.getSemester()
            });
        }
    }
}
