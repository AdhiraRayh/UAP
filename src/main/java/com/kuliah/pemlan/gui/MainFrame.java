package com.kuliah.pemlan.gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import com.kuliah.pemlan.model.*;
import com.kuliah.pemlan.service.*;

/**
 * Frame utama aplikasi Life Insight Manager+.
 * Kelas ini berfungsi sebagai wadah utama yang mengatur navigasi antara
 * berbagai panel (Dashboard, Data, Input, Report) menggunakan CardLayout.
 *
 * @author [AZIZI]
 * @version 1.0
 */
public class MainFrame extends JFrame {
    /** Layout untuk mengelola perpindahan antar panel */
    private CardLayout cardLayout;

    /** Panel utama yang berisi semua panel konten */
    private JPanel mainPanel;

    /** Daftar transaksi yang digunakan oleh seluruh aplikasi */
    private TransactionList transactionList;

    /** Manager file untuk operasi baca/tulis data transaksi */
    private FileManager fileManager;

    // Panels
    /** Panel dashboard untuk menampilkan ringkasan statistik */
    private DashboardPanel dashboardPanel;

    /** Panel data untuk menampilkan dan mengelola tabel transaksi */
    private DataPanel dataPanel;

    /** Panel input untuk menambah atau mengedit transaksi */
    private InputPanel inputPanel;

    /** Panel laporan untuk menampilkan grafik analisis */
    private ReportPanel reportPanel;

    /**
     * Menampilkan form edit untuk transaksi dengan ID tertentu.
     * Method ini akan beralih ke panel INPUT dan mengatur mode edit.
     *
     * @param transactionId ID transaksi yang akan diedit
     */
    public void showEditForm(String transactionId) {
        cardLayout.show(mainPanel, "INPUT");
        inputPanel.setEditMode(transactionId);
    }

    /**
     * Konstruktor untuk membuat MainFrame.
     * Menginisialisasi frame, memuat data, dan menyiapkan UI.
     */
    public MainFrame() {
        setTitle("Life Insight Manager+ - Personal Behavior & Emotional Spending Analyzer");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize services
        transactionList = new TransactionList();
        fileManager = new FileManager();

        // Load existing data
        loadData();

        // Set up UI
        initializeUI();

        setVisible(true);
    }

    /**
     * Memuat data transaksi dari file menggunakan FileManager.
     * Jika gagal memuat, aplikasi akan membuat data baru.
     */
    private void loadData() {
        try {
            java.util.List<Transaction> transactions = fileManager.loadTransactions();
            for (Transaction t : transactions) {
                transactionList.addTransaction(t);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Tidak dapat memuat data sebelumnya. Aplikasi akan membuat data baru.",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Menginisialisasi semua komponen UI pada frame utama.
     * Membuat panel navigasi, panel konten, dan status bar.
     */
    private void initializeUI() {
        // Main layout with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create navigation bar
        JPanel navPanel = createNavigationPanel();

        // Create content panels
        dashboardPanel = new DashboardPanel(this, transactionList);
        dataPanel = new DataPanel(this, transactionList);
        inputPanel = new InputPanel(this, transactionList, fileManager);
        reportPanel = new ReportPanel(this, transactionList);

        // Add panels to CardLayout
        mainPanel.add(dashboardPanel, "DASHBOARD");
        mainPanel.add(dataPanel, "DATA");
        mainPanel.add(inputPanel, "INPUT");
        mainPanel.add(reportPanel, "REPORT");

        // Set layout
        setLayout(new BorderLayout());
        add(navPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Status bar
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    /**
     * Membuat panel navigasi dengan tombol-tombol untuk berpindah antar panel.
     *
     * @return Panel navigasi dengan 4 tombol (Dashboard, Data, Input, Report)
     */
    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel(new GridLayout(1, 4));
        navPanel.setBackground(new Color(70, 130, 180));
        navPanel.setPreferredSize(new Dimension(1000, 50));

        String[] buttons = {" Dashboard", " Data", " Input", " Report"};
        String[] cards = {"DASHBOARD", "DATA", "INPUT", "REPORT"};

        for (int i = 0; i < buttons.length; i++) {
            JButton button = new JButton(buttons[i]);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setForeground(Color.BLACK);
            button.setBackground(new Color(100, 149, 237));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            final String cardName = cards[i];
            button.addActionListener(e -> {
                cardLayout.show(mainPanel, cardName);
                refreshCurrentPanel(cardName);
            });

            navPanel.add(button);
        }

        return navPanel;
    }

    /**
     * Membuat status bar yang menampilkan informasi aplikasi dan jumlah transaksi.
     *
     * @return Panel status bar dengan informasi versi dan jumlah transaksi
     */
    private JPanel createStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(240, 240, 240));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());

        JLabel statusLabel = new JLabel(" Life Insight Manager+ v1.0");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel dataLabel = new JLabel("Transaksi: " + transactionList.getCount() + "  ");
        dataLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(dataLabel, BorderLayout.EAST);

        return statusPanel;
    }

    /**
     * Memperbarui panel yang sedang aktif berdasarkan nama panel.
     *
     * @param panelName Nama panel yang perlu diperbarui (DASHBOARD, DATA, REPORT)
     */
    private void refreshCurrentPanel(String panelName) {
        switch (panelName) {
            case "DASHBOARD":
                dashboardPanel.refreshData();
                break;
            case "DATA":
                dataPanel.refreshTable();
                break;
            case "REPORT":
                reportPanel.refreshCharts();
                break;
        }
    }

    /**
     * Memperbarui semua panel dan data di aplikasi.
     * Method ini dipanggil setelah operasi yang mempengaruhi data transaksi.
     */
    public void refreshAllData() {
        dashboardPanel.refreshData();
        dataPanel.refreshTable();
        reportPanel.refreshCharts();

        // Update status bar
        updateStatusBar();
    }

    /**
     * Memperbarui status bar dengan jumlah transaksi terkini.
     */
    private void updateStatusBar() {
        Component[] components = getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel && ((JPanel) comp).getComponentCount() > 0) {
                Component[] subComps = ((JPanel) comp).getComponents();
                for (Component subComp : subComps) {
                    if (subComp instanceof JLabel &&
                            ((JLabel) subComp).getText().contains("Transaksi:")) {
                        ((JLabel) subComp).setText(
                                "Transaksi: " + transactionList.getCount() + "  ");
                        return;
                    }
                }
            }
        }
    }

    /**
     * Menampilkan halaman tertentu berdasarkan nama halaman.
     *
     * @param page Nama halaman yang akan ditampilkan (dashboard, data, input, report)
     */
    public void showPage(String page) {
        cardLayout.show(mainPanel, page.toUpperCase());
        refreshCurrentPanel(page.toUpperCase());
    }
}