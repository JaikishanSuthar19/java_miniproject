package com.group11.courseregistration.gui;

import com.group11.courseregistration.model.Faculty;
import com.group11.courseregistration.service.FacultyService;
import com.group11.courseregistration.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminFacultyPanel extends JPanel {
    private FacultyService facultyService = new FacultyService();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;

    public AdminFacultyPanel() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(UIUtils.COLOR_BACKGROUND);

        JLabel title = UIUtils.createTitleLabel("Faculty Members");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(title);

        JLabel subtitle = UIUtils.createSubtitleLabel("View college instructors and department allocations.");
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Search Bar and Add Button
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        searchField = UIUtils.createStyledTextField();
        searchField.setPreferredSize(new Dimension(260, 40));
        searchField.setToolTipText("Search by ID, name or department...");
        actionPanel.add(searchField);

        JButton searchBtn = UIUtils.createPrimaryButton("Search");
        searchBtn.addActionListener(e -> loadData(searchField.getText()));
        actionPanel.add(searchBtn);

        JButton addBtn = UIUtils.createPrimaryButton("+ Add Faculty");
        addBtn.addActionListener(e -> showAddFacultyDialog());
        actionPanel.add(addBtn);

        headerPanel.add(actionPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Faculty ID", "Name", "Department", "Email Address"};
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

    public void loadData(String query) {
        tableModel.setRowCount(0);
        List<Faculty> faculties;
        if (query == null || query.trim().isEmpty()) {
            faculties = facultyService.getAllFaculties();
        } else {
            faculties = facultyService.searchFaculties(query);
        }

        for (Faculty f : faculties) {
            tableModel.addRow(new Object[]{
                    f.getFacultyId(),
                    f.getFacultyName(),
                    f.getDepartment(),
                    f.getEmail()
            });
        }
    }

    private void showAddFacultyDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Faculty Member", true);
        dialog.setSize(400, 440);
        dialog.setLocationRelativeTo(this);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.setBackground(UIUtils.COLOR_WHITE);

        JTextField idF = addFormField(p, "Faculty ID (e.g. F007)");
        JTextField nameF = addFormField(p, "Faculty Name (e.g. Prof. Davis)");
        JTextField deptF = addFormField(p, "Department (e.g. CSE / IT)");
        JTextField emailF = addFormField(p, "Email Address");

        p.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnP.setBackground(UIUtils.COLOR_WHITE);

        JButton cBtn = new JButton("Cancel");
        cBtn.addActionListener(e -> dialog.dispose());
        JButton aBtn = UIUtils.createPrimaryButton("Save Faculty");
        aBtn.addActionListener(e -> {
            String id = idF.getText().trim();
            String name = nameF.getText().trim();
            String dept = deptF.getText().trim();
            String email = emailF.getText().trim();

            if (id.isEmpty() || name.isEmpty() || dept.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Faculty newFac = new Faculty(id, name, dept, email);
            if (facultyService.addFaculty(newFac)) {
                loadData("");
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Faculty member added successfully!");
            } else {
                JOptionPane.showMessageDialog(dialog, "Faculty ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnP.add(cBtn);
        btnP.add(aBtn);
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
        p.add(Box.createRigidArea(new Dimension(0, 12)));
        return f;
    }
}
