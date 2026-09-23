package com.group11.courseregistration.gui;

import com.group11.courseregistration.model.Course;
import com.group11.courseregistration.model.Registration;
import com.group11.courseregistration.model.Student;
import com.group11.courseregistration.service.CourseService;
import com.group11.courseregistration.service.RegistrationService;
import com.group11.courseregistration.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MyCoursesPanel extends JPanel {
    private Student student;
    private RegistrationService registrationService = new RegistrationService();
    private CourseService courseService = new CourseService();
    private DefaultTableModel tableModel;
    private JTable table;
    
    private JLabel regCoursesLabel;
    private JLabel totalCreditsLabel;

    public MyCoursesPanel(Student student) {
        this.student = student;
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(UIUtils.COLOR_BACKGROUND);

        JLabel title = UIUtils.createTitleLabel("My Registered Courses");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(title);
        
        JLabel subtitle = UIUtils.createSubtitleLabel("View and manage your enrolled courses.");
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        // Summary Cards
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        summaryPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        
        regCoursesLabel = new JLabel("0");
        totalCreditsLabel = new JLabel("0");
        
        summaryPanel.add(createSummaryCard("Registered Courses", regCoursesLabel));
        summaryPanel.add(createSummaryCard("Total Credits", totalCreditsLabel));
        
        headerPanel.add(summaryPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Course ID", "Course Name", "Faculty", "Credits", "Registration Date", "Status", "Action"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only action button is editable
            }
        };

        table = new JTable(tableModel);
        UIUtils.styleTable(table);

        table.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer());
        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(UIUtils.COLOR_WHITE);
        scrollPane.getViewport().setBackground(UIUtils.COLOR_WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDER));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(UIUtils.COLOR_BACKGROUND);
        tableContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);

        loadData();
    }
    
    private JPanel createSummaryCard(String title, JLabel valueLabel) {
        JPanel card = UIUtils.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(150, 70));
        
        JLabel tLabel = new JLabel(title);
        tLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tLabel.setForeground(UIUtils.COLOR_SECONDARY_TEXT);
        tLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        valueLabel.setForeground(UIUtils.COLOR_PRIMARY_TEXT);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(tLabel);
        card.add(valueLabel);
        return card;
    }

    public void loadData() { // made public to be callable from dashboard when tab switches
        tableModel.setRowCount(0);
        List<Registration> regs = registrationService.getStudentRegistrations(student.getStudentId());
        
        int credits = 0;

        for (Registration reg : regs) {
            Course c = courseService.getCourse(reg.getCourseId());
            if (c != null) {
                credits += c.getCredits();
                tableModel.addRow(new Object[]{
                        c.getCourseId(),
                        c.getCourseName(),
                        c.getFacultyName(),
                        c.getCredits(),
                        reg.getRegistrationDate().toString(),
                        reg.getStatus(),
                        "Cancel"
                });
            }
        }
        
        regCoursesLabel.setText(String.valueOf(regs.size()));
        totalCreditsLabel.setText(String.valueOf(credits));
    }

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
            setBackground(UIUtils.COLOR_LIGHT_GREEN);
            setForeground(UIUtils.COLOR_SUCCESS);
            return this;
        }
    }
    
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(UIUtils.COLOR_DANGER);
            setForeground(UIUtils.COLOR_WHITE);
            setFocusPainted(false);
            setFont(UIUtils.FONT_SMALL);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Cancel" : value.toString());
            return this;
        }
    }
    
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(UIUtils.COLOR_DANGER);
            button.setForeground(UIUtils.COLOR_WHITE);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "Cancel" : value.toString();
            button.setText(label);
            isPushed = true;
            currentRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                String courseId = (String) table.getValueAt(currentRow, 0);
                String courseName = (String) table.getValueAt(currentRow, 1);
                
                int confirm = JOptionPane.showConfirmDialog(MyCoursesPanel.this,
                    "Are you sure you want to cancel\n" + courseName + "?",
                    "Cancel Course Registration?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (registrationService.cancelRegistrationByStudentAndCourse(student.getStudentId(), courseId)) {
                        JOptionPane.showMessageDialog(MyCoursesPanel.this, "Registration cancelled successfully.");
                        // Data reload will happen after editing stops because we must not modify table model while editing it
                        SwingUtilities.invokeLater(() -> loadData());
                    }
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
