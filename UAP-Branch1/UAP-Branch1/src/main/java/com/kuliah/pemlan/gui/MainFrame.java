package com.kuliah.pemlan.gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import com.kuliah.pemlan.model.*;
import com.kuliah.pemlan.service.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private TransactionList transactionList;
    private FileManager fileManager;

    // Panels
    private DashboardPanel dashboardPanel;
    private DataPanel dataPanel;
    private InputPanel inputPanel;
    private ReportPanel reportPanel;

    // DI FILE: MainFrame.java
    public void showEditForm(String transactionId) {
        cardLayout.show(mainPanel, "INPUT");
        inputPanel.setEditMode(transactionId);
    }

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

    public void refreshAllData() {
        dashboardPanel.refreshData();
        dataPanel.refreshTable();
        reportPanel.refreshCharts();

        // Update status bar
        updateStatusBar();
    }

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

    public void showPage(String page) {
        cardLayout.show(mainPanel, page.toUpperCase());
        refreshCurrentPanel(page.toUpperCase());
    }
}