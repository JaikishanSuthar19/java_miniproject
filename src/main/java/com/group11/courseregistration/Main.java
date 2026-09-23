package com.group11.courseregistration;

import com.formdev.flatlaf.FlatLightLaf;
import com.group11.courseregistration.data.DataStore;
import com.group11.courseregistration.gui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Configure FlatLaf
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
                // Set default font
                UIManager.put("defaultFont", new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 14));
            } catch (Exception ex) {
                System.err.println("Failed to initialize LaF");
            }

            // Initialize data
            DataStore.initializeData();

            // Open login
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
