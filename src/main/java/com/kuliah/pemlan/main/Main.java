package com.kuliah.pemlan.main;

import com.kuliah.pemlan.gui.MainFrame;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Custom UI settings
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("Button.background", new Color(100, 149, 237));
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 12));

        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }

        // Run application
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}