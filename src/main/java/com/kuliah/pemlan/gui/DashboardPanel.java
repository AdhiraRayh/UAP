package com.kuliah.pemlan.gui;

import com.kuliah.pemlan.model.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

import java.util.List;

/**
 * Panel dashboard yang menampilkan ringkasan statistik transaksi dan aksi cepat.
 * Panel ini menampilkan total pengeluaran, rata-rata transaksi, jumlah transaksi,
 * pengeluaran emosional, serta daftar transaksi terakhir.
 *
 * @author [AZIZI]
 * @version 1.0
 */
public class DashboardPanel extends JPanel {
    /** Referensi ke frame utama untuk navigasi antar halaman */
    private MainFrame parent;

    /** Daftar transaksi yang akan ditampilkan dan dianalisis */
    private TransactionList transactionList;

    // UI Components
    /** Panel untuk menampilkan total pengeluaran */
    private JPanel totalPanel;

    /** Panel untuk menampilkan rata-rata transaksi */
    private JPanel averagePanel;

    /** Panel untuk menampilkan jumlah transaksi */
    private JPanel countPanel;

    /** Panel untuk menampilkan pengeluaran emosional */
    private JPanel emotionalPanel;

    // Label components inside panels
    /** Label untuk nilai total pengeluaran */
    private JLabel totalValueLabel;

    /** Label untuk nilai rata-rata transaksi */
    private JLabel averageValueLabel;

    /** Label untuk nilai jumlah transaksi */
    private JLabel countValueLabel;

    /** Label untuk nilai pengeluaran emosional */
    private JLabel emotionalValueLabel;

    /** Tabel untuk menampilkan transaksi terakhir */
    private JTable recentTransactionsTable;

    /**
     * Konstruktor untuk membuat DashboardPanel.
     *
     * @param parent Frame utama yang menampung panel ini
     * @param transactionList Daftar transaksi yang akan ditampilkan
     */
    public DashboardPanel(MainFrame parent, TransactionList transactionList) {
        this.parent = parent;
        this.transactionList = transactionList;

        initializeUI();
        refreshData();
    }

    /**
     * Kelas tombol dengan tampilan membulat.
     * Tombol ini memiliki sudut yang melengkung untuk tampilan yang lebih menarik.
     */
    static class RoundedButton extends JButton {

        /** Radius untuk sudut tombol yang membulat */
        private int radius = 15; // tingkat kebulatan

        /**
         * Membuat tombol dengan teks tertentu dan tampilan membulat.
         *
         * @param text Teks yang akan ditampilkan pada tombol
         */
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
        }

        /**
         * Menggambar komponen tombol dengan latar belakang membulat.
         *
         * @param g Objek Graphics untuk menggambar
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // background
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            super.paintComponent(g);
            g2.dispose();
        }

        /**
         * Menggambar border tombol dengan bentuk membulat.
         *
         * @param g Objek Graphics untuk menggambar border
         */
        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground().darker());
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.dispose();
        }
    }


    /**
     * Menginisialisasi semua komponen UI pada panel dashboard.
     * Metode ini mengatur layout, header, panel statistik, aksi cepat, dan tabel transaksi.
     */
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 248, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Life Insight Manager+ Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));

        JLabel subtitleLabel = new JLabel("Analisis Perilaku & Pengeluaran Emosional");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        // Stats Panel
        JPanel statsPanel = createStatsPanel();

        // Quick Actions
        JPanel actionsPanel = createQuickActionsPanel();

        // Recent Transactions
        JPanel recentPanel = createRecentTransactionsPanel();

        // Layout
        add(headerPanel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);
        add(actionsPanel, BorderLayout.WEST);
        add(recentPanel, BorderLayout.SOUTH);
    }

    /**
     * Membuat panel yang berisi kartu statistik (total, rata-rata, jumlah, emosional).
     *
     * @return Panel yang berisi 4 kartu statistik dalam layout grid 2x2
     */
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        totalPanel = createStatCard("Total Pengeluaran", "Rp 0", Color.RED);
        averagePanel = createStatCard("Rata-rata", "Rp 0", Color.ORANGE);
        countPanel = createStatCard("Jumlah Transaksi", "0", Color.BLUE);
        emotionalPanel = createStatCard("Pengeluaran Emosional", "Rp 0", Color.MAGENTA);

        // Extract value labels from panels
        totalValueLabel = (JLabel) ((JPanel) totalPanel.getComponent(1)).getComponent(0);
        averageValueLabel = (JLabel) ((JPanel) averagePanel.getComponent(1)).getComponent(0);
        countValueLabel = (JLabel) ((JPanel) countPanel.getComponent(1)).getComponent(0);
        emotionalValueLabel = (JLabel) ((JPanel) emotionalPanel.getComponent(1)).getComponent(0);

        panel.add(totalPanel);
        panel.add(averagePanel);
        panel.add(countPanel);
        panel.add(emotionalPanel);

        return panel;
    }

    /**
     * Membuat kartu statistik individual dengan judul, nilai, dan warna tertentu.
     *
     * @param title Judul kartu statistik
     * @param value Nilai awal yang ditampilkan pada kartu
     * @param color Warna untuk nilai dan border kartu
     * @return Panel kartu statistik dengan layout BorderLayout
     */
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(color, 2), BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.BLACK);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);

        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        valuePanel.setBackground(Color.WHITE);
        valuePanel.add(valueLabel);
        card.add(valuePanel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Membuat panel aksi cepat untuk navigasi ke halaman lain.
     * Panel ini berisi tombol-tombol untuk menambah transaksi, melihat data,
     * melihat laporan, dan melakukan analisis.
     *
     * @return Panel dengan tombol-tombol aksi cepat dalam layout vertikal
     */
    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Aksi Cepat"));
        panel.setPreferredSize(new Dimension(200, 300));

        JButton addBtn = createActionButton(" Tambah Transaksi", "INPUT");
        JButton viewBtn = createActionButton(" Lihat Data", "DATA");
        JButton reportBtn = createActionButton(" Lihat Laporan", "REPORT");
        JButton analyzeBtn = createActionButton(" Analisis", "REPORT");

        panel.add(Box.createVerticalStrut(10));
        panel.add(addBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(viewBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(reportBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(analyzeBtn);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Membuat tombol aksi dengan teks dan target halaman tertentu.
     *
     * @param text Teks yang ditampilkan pada tombol
     * @param page Identifikasi halaman tujuan navigasi
     * @return Tombol dengan tampilan membulat dan action listener
     */
    private JButton createActionButton(String text, String page) {
        JButton button = new RoundedButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> parent.showPage(page));

        return button;
    }


    /**
     * Membuat panel yang berisi tabel transaksi terakhir.
     * Sebelumnya panel ini tidak menampilkan apapun, sekarang menampilkan
     * tabel dengan kolom Tanggal, Deskripsi, Jumlah, dan Emosi.
     *
     * @return Panel dengan tabel transaksi terakhir
     */
    private JPanel createRecentTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Transaksi Terakhir"));

        String[] columns = {"Tanggal", "Deskripsi", "Jumlah", "Emosi"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recentTransactionsTable = new JTable(model);  // SIMPAN REFERENSI
        recentTransactionsTable.setRowHeight(25);
        recentTransactionsTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        recentTransactionsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));

        JScrollPane scrollPane = new JScrollPane(recentTransactionsTable);
        scrollPane.setPreferredSize(new Dimension(800, 150));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Memperbarui data yang ditampilkan pada dashboard.
     * Metode ini menghitung ulang statistik dan memperbarui label serta tabel.
     * Pengeluaran emosional dihitung dari transaksi dengan emosi non-netral
     * dan jumlah di atas Rp 50.000.
     */
    public void refreshData() {
        // Update statistics
        double total = transactionList.getTotalAmount();
        double average = transactionList.getAverageAmount();
        int count = transactionList.getCount();

        // Calculate emotional spending (non-neutral emotions with amount > 50k)
        double emotionalTotal = transactionList.getAllTransactions().stream()
                .filter(t -> !t.getEmotion().equalsIgnoreCase("Netral") &&
                        t.getAmount() > 50000)
                .mapToDouble(t -> t.getAmount())
                .sum();

        // Update labels
        if (totalValueLabel != null) {
            totalValueLabel.setText(String.format("Rp%,.0f", total));
        }
        if (averageValueLabel != null) {
            averageValueLabel.setText(String.format("Rp%,.0f", average));
        }
        if (countValueLabel != null) {
            countValueLabel.setText(String.format("%,d", count));
        }
        if (emotionalValueLabel != null) {
            emotionalValueLabel.setText(String.format("Rp%,.0f", emotionalTotal));
        }

        // Update recent transactions table
        updateRecentTransactions();
    }

    /**
     * Memperbarui tabel transaksi terakhir dengan 5 transaksi terbaru.
     * Jika terdapat kurang dari 5 transaksi, semua transaksi akan ditampilkan.
     */
    private void updateRecentTransactions() {
        if (recentTransactionsTable != null) {
            DefaultTableModel model = (DefaultTableModel) recentTransactionsTable.getModel();

            // Clear existing data
            model.setRowCount(0);

            // Add last 5 transactions
            List<Transaction> allTransactions = transactionList.getAllTransactions();
            int start = Math.max(0, allTransactions.size() - 5);

            for (int i = start; i < allTransactions.size(); i++) {
                Transaction t = allTransactions.get(i);
                model.addRow(new Object[]{
                        t.getDate().toString(),
                        t.getDescription(),
                        String.format("Rp%,.0f", t.getAmount()),
                        t.getEmotion()
                });
            }
        }
    }
}