package com.kuliah.pemlan.gui;

import com.kuliah.pemlan.model.*;
import com.kuliah.pemlan.service.*;
import com.kuliah.pemlan.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Map;

public class ReportPanel extends JPanel {
    private MainFrame parent;
    private TransactionList transactionList;
    private JTextArea insightArea;
    private JLabel summaryLabel;

    public ReportPanel(MainFrame parent, TransactionList transactionList) {
        this.parent = parent;
        this.transactionList = transactionList;

        initializeUI();
        refreshCharts();
    }

    static class RoundedButton extends JButton {

        private int radius = 15; // tingkat kebulatan

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
        }

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

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 248, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Laporan & Analisis");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));

        summaryLabel = new JLabel("Memuat data...");
        summaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        summaryLabel.setForeground(Color.DARK_GRAY);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(summaryLabel, BorderLayout.EAST);

        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));

        tabbedPane.addTab("Ringkasan", createSummaryTab());
        tabbedPane.addTab("Per Kategori", createCategoryTab());
        tabbedPane.addTab("Analisis Emosi", createEmotionTab());
        tabbedPane.addTab("Insight", createInsightTab());

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton refreshBtn = createSmallButton(" Refresh");
        refreshBtn.addActionListener(e -> refreshCharts());

        buttonPanel.add(refreshBtn);

        // Layout
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createSmallButton(String text) {
        JButton button = new RoundedButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        return button;
    }

    private JPanel createSummaryTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        // Stats
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        double total = transactionList.getTotalAmount();
        double average = transactionList.getAverageAmount();
        int count = transactionList.getCount();

        statsPanel.add(createStatBox("Total Pengeluaran",
                String.format("Rp%,.0f", total), Color.RED));
        statsPanel.add(createStatBox("Rata-rata per Transaksi",
                String.format("Rp%,.0f", average), Color.ORANGE));
        statsPanel.add(createStatBox("Jumlah Transaksi",
                String.format("%,d", count), Color.BLUE));

        // Add empty panel for alignment
        statsPanel.add(new JPanel());

        panel.add(statsPanel, BorderLayout.NORTH);

        return panel;
    }

    private JPanel createStatBox(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(color);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCategoryTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Get category data
        AnalysisService analysisService = new AnalysisService(transactionList);
        Map<String, Double> categoryData = analysisService.getAmountByCategory();

        if (!categoryData.isEmpty()) {
            // Create simple pie chart panel
            JPanel chartPanel = ChartHelper.createSimplePieChart(
                    "Pengeluaran per Kategori", categoryData);
            panel.add(chartPanel, BorderLayout.CENTER);
        } else {
            panel.add(new JLabel("Tidak ada data untuk ditampilkan",
                    SwingConstants.CENTER), BorderLayout.CENTER);
        }

        return panel;
    }

    private JPanel createEmotionTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Get emotion analysis
        AnalysisService analysisService = new AnalysisService(transactionList);
        Map<String, Double> emotionData = analysisService.getAmountByEmotion();

        if (!emotionData.isEmpty()) {
            // Create text analysis
            StringBuilder analysis = new StringBuilder();
            analysis.append("ANALISIS PENGELUARAN BERDASARKAN EMOSI\n\n");

            double total = transactionList.getTotalAmount();
            for (Map.Entry<String, Double> entry : emotionData.entrySet()) {
                String emotion = entry.getKey();
                double amount = entry.getValue();
                double percentage = (amount / total) * 100;


                analysis.append(String.format("%s: Rp%,.0f (%.1f%%)\n",
                        emotion, amount, percentage));
            }

            // Add insights
            analysis.append("\nINSIGHT:\n");
            if (emotionData.containsKey("Stres") && emotionData.get("Stres") > total * 0.3) {
                analysis.append("- Anda banyak spending saat stres (>30%)\n");
                analysis.append("- Coba teknik relaksasi sebelum belanja\n");
            }

            JTextArea textArea = new JTextArea(analysis.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            textArea.setBackground(new Color(248, 248, 255));


            JScrollPane scrollPane = new JScrollPane(textArea);
            panel.add(scrollPane, BorderLayout.CENTER);
        } else {
            panel.add(new JLabel("Tidak ada data emosi untuk dianalisis",
                    SwingConstants.CENTER), BorderLayout.CENTER);
        }

        return panel;
    }


    private JPanel createInsightTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Generate insights
        AnalysisService analysisService = new AnalysisService(transactionList);
        String insight = analysisService.generateInsight();

        insightArea = new JTextArea(insight);
        insightArea.setEditable(false);
        insightArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        insightArea.setLineWrap(true);
        insightArea.setWrapStyleWord(true);
        insightArea.setBackground(new Color(248, 248, 255));

        JScrollPane scrollPane = new JScrollPane(insightArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Analisis Lengkap"));

        // Recommendations
        JTextArea recommendations = new JTextArea(
                "💡 REKOMENDASI:\n" +
                        "1. Buat anggaran bulanan untuk kategori pengeluaran tertinggi\n" +
                        "2. Tunggu 24 jam sebelum belanja besar saat emosi tidak stabil\n" +
                        "3. Review pengeluaran mingguan untuk identifikasi pola\n" +
                        "4. Alokasikan 20% pendapatan untuk tabungan dan investasi\n" +
                        "5. Catat setiap pengeluaran segera setelah terjadi"
        );
        recommendations.setEditable(false);
        recommendations.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        recommendations.setBackground(new Color(255, 250, 240));

        JScrollPane recScroll = new JScrollPane(recommendations);
        recScroll.setBorder(BorderFactory.createTitledBorder("Saran Perbaikan"));
        recScroll.setPreferredSize(new Dimension(300, 150));

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(recScroll, BorderLayout.SOUTH);

        return panel;
    }

    public void refreshCharts() {
        // Update summary
        int count = transactionList.getCount();
        double total = transactionList.getTotalAmount();
        summaryLabel.setText(String.format("Total: %d transaksi | Rp%,.0f", count, total));

        // Update insights
        if (insightArea != null) {
            AnalysisService analysisService = new AnalysisService(transactionList);
            String insight = analysisService.generateInsight();
            insightArea.setText(insight);
        }

        // Refresh panels
        revalidate();
        repaint();
    }
}