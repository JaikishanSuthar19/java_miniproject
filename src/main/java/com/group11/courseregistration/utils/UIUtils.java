package com.group11.courseregistration.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class UIUtils {

    // Color Palette
    public static final Color COLOR_PRIMARY_BLUE = Color.decode("#2563EB");
    public static final Color COLOR_DARK_NAVY = Color.decode("#0F172A");
    public static final Color COLOR_DARKER_NAVY = Color.decode("#020617");
    public static final Color COLOR_BACKGROUND = Color.decode("#F8FAFC");
    public static final Color COLOR_WHITE = Color.decode("#FFFFFF");
    public static final Color COLOR_PRIMARY_TEXT = Color.decode("#0F172A");
    public static final Color COLOR_SECONDARY_TEXT = Color.decode("#64748B");
    public static final Color COLOR_BORDER = Color.decode("#E2E8F0");
    public static final Color COLOR_SUCCESS = Color.decode("#16A34A");
    public static final Color COLOR_DANGER = Color.decode("#DC2626");
    public static final Color COLOR_WARNING = Color.decode("#F59E0B");
    public static final Color COLOR_LIGHT_BLUE = Color.decode("#EFF6FF");
    public static final Color COLOR_LIGHT_GREEN = Color.decode("#F0FDF4");
    public static final Color COLOR_LIGHT_RED = Color.decode("#FEF2F2");

    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 26);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 16);
    public static final Font FONT_NORMAL = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("SansSerif", Font.PLAIN, 12);

    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(COLOR_PRIMARY_BLUE);
        button.setForeground(COLOR_WHITE);
        button.setFont(FONT_NORMAL);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(COLOR_DANGER);
        button.setForeground(COLOR_WHITE);
        button.setFont(FONT_NORMAL);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(FONT_NORMAL);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        return textField;
    }
    
    public static JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(FONT_NORMAL);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        return passwordField;
    }

    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(COLOR_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        return card;
    }

    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_TITLE);
        label.setForeground(COLOR_PRIMARY_TEXT);
        return label;
    }

    public static JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SUBTITLE);
        label.setForeground(COLOR_SECONDARY_TEXT);
        return label;
    }
    
    public static void styleTable(JTable table) {
        table.setRowHeight(44);
        table.setBackground(COLOR_WHITE);
        table.setForeground(COLOR_PRIMARY_TEXT);
        table.setFont(FONT_NORMAL);
        table.setSelectionBackground(COLOR_LIGHT_BLUE);
        table.setSelectionForeground(COLOR_PRIMARY_TEXT);
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(COLOR_BORDER);
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.decode("#F1F5F9"));
        header.setForeground(COLOR_PRIMARY_TEXT);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(100, 44));
        
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(new EmptyBorder(0, 10, 0, 10));
        table.setDefaultRenderer(Object.class, renderer);
    }
}
