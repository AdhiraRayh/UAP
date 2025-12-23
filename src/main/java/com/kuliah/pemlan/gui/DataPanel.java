package com.kuliah.pemlan.gui;

import com.kuliah.pemlan.model.*;
import com.kuliah.pemlan.service.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;



public class DataPanel extends JPanel {
    private MainFrame parent;
    private TransactionList transactionList;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private JComboBox<String> filterCombo;



    public DataPanel(MainFrame parent, TransactionList transactionList) {
        this.parent = parent;
        this.transactionList = transactionList;

        initializeUI();
        refreshTable();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));


        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 248, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Data Transaksi");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));

        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Search and Filter Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBackground(new Color(245, 245, 245));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        searchField = new JTextField(20);
        searchField.setToolTipText("Cari berdasarkan deskripsi...");
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void insertUpdate(DocumentEvent e) { filterTable(); }
        });

        filterCombo = new JComboBox<>(new String[]{
                "Semua Kategori", "Makanan", "Transportasi", "Hiburan",
                "Belanja", "Kesehatan", "Pendidikan", "Lainnya"
        });
        filterCombo.addActionListener(e -> filterTable());

        controlPanel.add(new JLabel("Cari:"));
        controlPanel.add(searchField);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(new JLabel("Filter:"));
        controlPanel.add(filterCombo);

        // Table
        String[] columns = {"ID", "Tanggal", "Deskripsi", "Jumlah", "Kategori", "Emosi", "Catatan"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.BLACK);

        DefaultTableCellRenderer headcen = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        headcen.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);





        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        table.getColumnModel().getColumn(6).setPreferredWidth(200);

        table.getColumnModel().getColumn(1).setCellRenderer(center);
        table.getColumnModel().getColumn(2).setCellRenderer(center);
        table.getColumnModel().getColumn(4).setCellRenderer(center);
        table.getColumnModel().getColumn(5).setCellRenderer(center);






        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (value instanceof Double) {
                    label.setText(String.format("Rp%,.0f", (Double) value));
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                }
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));


        JButton refreshBtn = createButton(" Refresh", new Color(100, 149, 237));
        JButton editBtn = createButton(" Edit", new Color(255, 165, 0));
        JButton deleteBtn = createButton(" Hapus", new Color(220, 20, 60));
        JButton sortAmountBtn = createButton(" Sort by Amount", new Color(34, 139, 34));
        JButton sortDateBtn = createButton(" Sort by Date", new Color(138, 43, 226));

        refreshBtn.setForeground(new Color(100, 149, 237));
        editBtn.setForeground(new Color(255, 165, 0));
        deleteBtn.setForeground(new Color(220, 20, 60));
        sortAmountBtn.setForeground(new Color(34, 139, 34));
        sortDateBtn.setForeground(new Color(138, 43, 226));

        refreshBtn.addActionListener(e -> refreshTable());
        editBtn.addActionListener(e -> editSelected());
        deleteBtn.addActionListener(e -> deleteSelected());
        sortAmountBtn.addActionListener(e -> sortByAmount());
        sortDateBtn.addActionListener(e -> sortByDate());

        buttonPanel.add(refreshBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(sortAmountBtn);
        buttonPanel.add(sortDateBtn);

        // Layout
        add(headerPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    public void refreshTable() {
        tableModel.setRowCount(0);

        List<Transaction> transactions = transactionList.getAllTransactions();
        for (Transaction t : transactions) {
            tableModel.addRow(new Object[]{
                    t.getId(),
                    t.getDate().toString(),
                    t.getDescription(),
                    t.getAmount(),
                    t.getCategory(),
                    t.getEmotion(),
                    t.getNotes()
            });
        }
    }

    private void filterTable() {
        String searchText = searchField.getText().toLowerCase();
        String selectedCategory = (String) filterCombo.getSelectedItem();

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        RowFilter<DefaultTableModel, Integer> filter = new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                String id = entry.getStringValue(0).toLowerCase();
                String desc = entry.getStringValue(2).toLowerCase();
                String category = entry.getStringValue(4).toLowerCase();
                String emotion = entry.getStringValue(5).toLowerCase();
                String notes = entry.getStringValue(6).toLowerCase();

                boolean matchesSearch = searchText.isEmpty() ||
                        id.contains(searchText) ||
                        desc.contains(searchText) ||
                        category.contains(searchText) ||
                        emotion.contains(searchText) ||
                        notes.contains(searchText);

                boolean matchesCategory = selectedCategory.equals("Semua Kategori") ||
                        category.equals(selectedCategory.toLowerCase());

                return matchesSearch && matchesCategory;
            }
        };

        sorter.setRowFilter(filter);
    }

    private void sortByAmount() {
        List<Transaction> sorted = transactionList.sortByAmount(false); // Descending
        updateTable(sorted);
    }

    private void sortByDate() {
        List<Transaction> sorted = transactionList.sortByDate(false); // Descending
        updateTable(sorted);
    }

    private void updateTable(List<Transaction> transactions) {
        tableModel.setRowCount(0);
        for (Transaction t : transactions) {
            tableModel.addRow(new Object[]{
                    t.getId(),
                    t.getDate().toString(),
                    t.getDescription(),
                    t.getAmount(),
                    t.getCategory(),
                    t.getEmotion(),
                    t.getNotes()
            });
        }
    }

    private void editSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih transaksi yang ingin diedit!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String transactionId = (String) tableModel.getValueAt(selectedRow, 0);
        Transaction transaction = transactionList.getTransactionById(transactionId);

        if (transaction != null) {
            // Switch to input panel for editing
            parent.showPage("INPUT");
            // Note: Need to implement edit mode in InputPanel
        }
    }

    private void deleteSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih transaksi yang ingin dihapus!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String transactionId = (String) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus transaksi ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = transactionList.deleteTransaction(transactionId);

            if (success) {
                try {
                    FileManager fileManager = new FileManager();
                    fileManager.saveTransactions(transactionList.getAllTransactions());

                    refreshTable();
                    parent.refreshAllData();

                    JOptionPane.showMessageDialog(this,
                            "Transaksi berhasil dihapus!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this,
                            "Gagal menyimpan perubahan: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}