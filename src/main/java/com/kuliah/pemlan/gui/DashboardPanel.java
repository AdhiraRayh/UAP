package com.kuliah.pemlan.gui;

import com.kuliah.pemlan.model.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

import java.util.List;

public class DashboardPanel extends JPanel {
    private MainFrame parent;
    private TransactionList transactionList;

    // UI Components
    private JPanel totalPanel;
    private JPanel averagePanel;
    private JPanel countPanel;
    private JPanel emotionalPanel;

    // Label components inside panels
    private JLabel totalValueLabel;
    private JLabel averageValueLabel;
    private JLabel countValueLabel;
    private JLabel emotionalValueLabel;

    public DashboardPanel(MainFrame parent, TransactionList transactionList) {
        this.parent = parent;
        this.transactionList = transactionList;

        initializeUI();
        refreshData();
    }

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

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        totalPanel = createStatCard("💰 Total Pengeluaran", "Rp 0", Color.RED);
        averagePanel = createStatCard("📊 Rata-rata", "Rp 0", Color.ORANGE);
        countPanel = createStatCard("📝 Jumlah Transaksi", "0", Color.BLUE);
        emotionalPanel = createStatCard("😢 Pengeluaran Emosional", "Rp 0", Color.MAGENTA);

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

    private JButton createActionButton(String text, String page) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.GRAY);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> parent.showPage(page));

        return button;
    }

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

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 150));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

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

    private void updateRecentTransactions() {
        // Find the recent transactions table
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel &&
                    ((JPanel) comp).getBorder() != null &&
                    ((JPanel) comp).getBorder().toString().contains("Transaksi Terakhir")) {

                Component[] subComps = ((JPanel) comp).getComponents();
                for (Component subComp : subComps) {
                    if (subComp instanceof JScrollPane) {
                        JScrollPane scrollPane = (JScrollPane) subComp;
                        JViewport viewport = scrollPane.getViewport();
                        if (viewport.getView() instanceof JTable) {
                            JTable table = (JTable) viewport.getView();
                            DefaultTableModel model = (DefaultTableModel) table.getModel();

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
                break;
            }
        }
    }
}