package com.group11.courseregistration.gui;

import com.group11.courseregistration.model.Course;
import com.group11.courseregistration.model.Registration;
import com.group11.courseregistration.model.Student;
import com.group11.courseregistration.service.CourseService;
import com.group11.courseregistration.service.RegistrationService;
import com.group11.courseregistration.service.StudentService;
import com.group11.courseregistration.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class AdminRegistrationPanel extends JPanel {
    private RegistrationService registrationService = new RegistrationService();
    private StudentService studentService = new StudentService();
    private CourseService courseService = new CourseService();
    
    private DefaultTableModel tableModel;
    private JTable table;

    public AdminRegistrationPanel() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(UIUtils.COLOR_BACKGROUND);

        JLabel title = UIUtils.createTitleLabel("All Registrations");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(title);
        
        JLabel subtitle = UIUtils.createSubtitleLabel("Monitor course registration activity.");
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Search Bar (optional, can just reload to refresh)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        JButton reloadBtn = UIUtils.createPrimaryButton("Refresh");
        reloadBtn.addActionListener(e -> loadData());
        actionPanel.add(reloadBtn);
        
        headerPanel.add(actionPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Registration ID", "Student ID", "Student Name", "Course ID", "Course Name", "Registration Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        UIUtils.styleTable(table);

        // Status Badge Renderer
        table.getColumnModel().getColumn(6).setCellRenderer(new StatusCellRenderer());

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

    public void loadData() {
        tableModel.setRowCount(0);
        List<Registration> regs = registrationService.getAllRegistrations();

        for (Registration reg : regs) {
            Student s = studentService.getStudent(reg.getStudentId());
            Course c = courseService.getCourse(reg.getCourseId());
            
            String sName = (s != null) ? s.getStudentName() : "Unknown";
            String cName = (c != null) ? c.getCourseName() : "Unknown";
            
            tableModel.addRow(new Object[]{
                    reg.getRegistrationId(),
                    reg.getStudentId(),
                    sName,
                    reg.getCourseId(),
                    cName,
                    reg.getRegistrationDate().toString(),
                    reg.getStatus()
            });
        }
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
            if ("REGISTERED".equals(status)) {
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
