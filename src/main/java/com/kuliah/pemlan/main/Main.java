package com.kuliah.pemlan.main;

import com.kuliah.pemlan.gui.MainFrame;
import javax.swing.*;
import java.awt.*;

/**
 * Kelas utama untuk aplikasi Life Insight Manager+.
 * Kelas ini berfungsi sebagai entry point aplikasi yang mengatur
 * look and feel GUI dan memulai aplikasi.
 *
 * @author [AZIZI]
 * @version 1.0
 */
public class Main {

    /**
     * Metode utama yang dijalankan saat aplikasi dimulai.
     * Mengatur look and feel GUI, mengkonfigurasi UI settings,
     * dan membuat serta menampilkan MainFrame.
     *
     * @param args Argumen command line (tidak digunakan)
     */
    public static void main(String[] args) {
        // Set look and feel
        try {
            // Menggunakan look and feel sistem operasi
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Custom UI settings
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("Button.background", new Color(100, 149, 237));
            UIManager.put("Button.foreground", Color.BLACK); /** Mengubah Warna Pada panel peringatan dari putih ke hitam **/
            UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 12));

        } catch (Exception e) {
            // Menampilkan error jika gagal mengatur look and feel
            System.err.println("Error setting look and feel: " + e.getMessage());
        }

        // Run application on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}