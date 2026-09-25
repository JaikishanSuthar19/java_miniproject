package com.group11.courseregistration.utils;

import com.formdev.flatlaf.FlatLightLaf;
import com.group11.courseregistration.data.DataStore;
import com.group11.courseregistration.gui.*;
import com.group11.courseregistration.model.Student;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class CaptureScreenshots {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("defaultFont", new Font("SansSerif", Font.PLAIN, 14));
        } catch (Exception ignored) {}

        DataStore.initializeData();
        File dir = new File("screenshots");
        if (!dir.exists()) dir.mkdirs();

        try {
            // 1. Login Frame
            LoginFrame loginFrame = new LoginFrame();
            saveFrameContent(loginFrame, "screenshots/01_login_frame.png", 1100, 700);
            loginFrame.dispose();

            Student demoStudent = DataStore.students.get("student");

            // 2. Student Dashboard
            StudentDashboard studentDashboard = new StudentDashboard(demoStudent);
            studentDashboard.switchTab("Dashboard");
            saveFrameContent(studentDashboard, "screenshots/02_student_dashboard.png", 1200, 750);

            studentDashboard.switchTab("Available Courses");
            saveFrameContent(studentDashboard, "screenshots/03_student_available_courses.png", 1200, 750);

            studentDashboard.switchTab("Register Course");
            saveFrameContent(studentDashboard, "screenshots/04_student_register_course.png", 1200, 750);

            studentDashboard.switchTab("My Courses");
            saveFrameContent(studentDashboard, "screenshots/05_student_my_courses.png", 1200, 750);
            studentDashboard.dispose();

            // 3. Admin Dashboard
            AdminDashboard adminDashboard = new AdminDashboard();
            adminDashboard.switchTab("Dashboard");
            saveFrameContent(adminDashboard, "screenshots/06_admin_dashboard.png", 1200, 750);

            adminDashboard.switchTab("Manage Courses");
            saveFrameContent(adminDashboard, "screenshots/07_admin_manage_courses.png", 1200, 750);

            adminDashboard.switchTab("Students");
            saveFrameContent(adminDashboard, "screenshots/08_admin_students.png", 1200, 750);

            adminDashboard.switchTab("Faculty");
            saveFrameContent(adminDashboard, "screenshots/09_admin_faculty.png", 1200, 750);

            adminDashboard.switchTab("Registrations");
            saveFrameContent(adminDashboard, "screenshots/10_admin_registrations.png", 1200, 750);
            adminDashboard.dispose();

            System.out.println("ALL_SCREENSHOTS_RENDERED_SUCCESSFULLY");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void saveFrameContent(JFrame frame, String path, int width, int height) {
        try {
            frame.setSize(width, height);
            Container content = frame.getContentPane();
            content.setSize(width, height);
            layoutHierarchy(content);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, width, height);

            content.printAll(g2);
            g2.dispose();

            ImageIO.write(image, "png", new File(path));
            System.out.println("Saved: " + path);
        } catch (Exception e) {
            System.err.println("Failed to save " + path + ": " + e.getMessage());
        }
    }

    private static void layoutHierarchy(Component comp) {
        comp.doLayout();
        comp.validate();
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                layoutHierarchy(child);
            }
        }
    }
}
