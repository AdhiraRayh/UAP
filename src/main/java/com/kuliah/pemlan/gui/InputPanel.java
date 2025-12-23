package com.kuliah.pemlan.gui;

import com.kuliah.pemlan.model.*;
import com.kuliah.pemlan.service.*;
import com.kuliah.pemlan.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalDate;

public class InputPanel extends JPanel {
    private MainFrame parent;
    private TransactionList transactionList;
    private FileManager fileManager;

    // Form components
    private JTextField idField;
    private JTextField dateField;
    private JTextField descriptionField;
    private JTextField amountField;
    private JComboBox<String> categoryCombo;
    private JComboBox<String> emotionCombo;
    private JTextArea notesArea;

    // Mode: ADD or EDIT
    private boolean editMode = false;
    private String editTransactionId;

    public InputPanel(MainFrame parent, TransactionList transactionList, FileManager fileManager) {
        this.parent = parent;
        this.transactionList = transactionList;
        this.fileManager = fileManager;

        initializeUI();
        resetForm();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 248, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Input Transaksi Baru");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));

        JLabel subtitleLabel = new JLabel("Catat pengeluaran dan kondisi emosi Anda");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.DARK_GRAY);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        // Form Panel
        JPanel formPanel = createFormPanel();

        // Button Panel
        JPanel buttonPanel = createButtonPanel();

        // Layout
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // ID Field
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("ID:"), gbc);

        gbc.gridx = 1;
        idField = new JTextField();
        idField.setEditable(false);
        idField.setBackground(new Color(245, 245, 245));
        panel.add(idField, gbc);
        row++;

        // Date Field
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Tanggal (YYYY-MM-DD):*"), gbc);

        gbc.gridx = 1;
        dateField = new JTextField(LocalDate.now().toString());
        dateField.setToolTipText("Format: 2024-01-15");
        panel.add(dateField, gbc);
        row++;

        // Description
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Deskripsi:*"), gbc);

        gbc.gridx = 1;
        descriptionField = new JTextField(20);
        descriptionField.setToolTipText("Contoh: Kopi Starbucks, Bensin, dll");
        panel.add(descriptionField, gbc);
        row++;

        // Amount
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Jumlah (Rp):*"), gbc);

        gbc.gridx = 1;
        amountField = new JTextField();
        amountField.setToolTipText("Masukkan angka saja");
        panel.add(amountField, gbc);
        row++;

        // Category
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Kategori:*"), gbc);

        gbc.gridx = 1;
        String[] categories = {
                "Pilih kategori...", "Makanan & Minuman", "Transportasi",
                "Hiburan", "Belanja", "Kesehatan", "Pendidikan",
                "Tagihan", "Investasi", "Lainnya"
        };
        categoryCombo = new JComboBox<>(categories);
        panel.add(categoryCombo, gbc);
        row++;

        // Emotion
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Kondisi Emosi:*"), gbc);

        gbc.gridx = 1;
        String[] emotions = {
                "Pilih emosi...", "Senang", "Sedih", "Stres", "Marah", "Netral"
        };
        emotionCombo = new JComboBox<>(emotions);
        panel.add(emotionCombo, gbc);
        row++;

        // Notes
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Catatan:"), gbc);

        gbc.gridx = 1;
        notesArea = new JTextArea(4, 20);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane notesScroll = new JScrollPane(notesArea);
        panel.add(notesScroll, gbc);
        row++;

        // Required note
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        JLabel noteLabel = new JLabel("* Menandakan field wajib diisi");
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        noteLabel.setForeground(Color.RED);
        panel.add(noteLabel, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));

        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JButton saveButton = createStyledButton("Simpan", new Color(34, 139, 34));
        JButton clearButton = createStyledButton("Reset", new Color(100, 149, 237));
        JButton cancelButton = createStyledButton("Batal", new Color(220, 20, 60));

        saveButton.setForeground(new Color(14,139,34 )); ;
        clearButton.setForeground(new Color(100, 149, 237));
        cancelButton.setForeground(new Color(220, 20, 60));



        saveButton.addActionListener(e -> saveTransaction());
        clearButton.addActionListener(e -> resetForm());
        cancelButton.addActionListener(e -> parent.showPage("DASHBOARD"));

        panel.add(saveButton);
        panel.add(clearButton);
        panel.add(cancelButton);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

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

    private void saveTransaction() {
        // Validate input
        ValidationService.ValidationResult validation =
                ValidationService.validateTransaction(
                        dateField.getText(),
                        descriptionField.getText(),
                        amountField.getText(),
                        (String) categoryCombo.getSelectedItem(),
                        (String) emotionCombo.getSelectedItem()
                );

        if (!validation.isValid()) {
            JOptionPane.showMessageDialog(this,
                    validation.getErrorMessage(),
                    "Validasi Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Parse input
            LocalDate date = LocalDate.parse(dateField.getText());
            String description = descriptionField.getText().trim();
            double amount = Double.parseDouble(amountField.getText().trim());
            String category = (String) categoryCombo.getSelectedItem();
            String emotion = (String) emotionCombo.getSelectedItem();
            String notes = notesArea.getText().trim();

            if (editMode) {
                // Update existing transaction
                Transaction existing = transactionList.getTransactionById(editTransactionId);
                if (existing != null) {
                    existing.setDate(date);
                    existing.setDescription(description);
                    existing.setAmount(amount);
                    existing.setCategory(category);
                    existing.setEmotion(emotion);
                    existing.setNotes(notes);

                    transactionList.updateTransaction(editTransactionId, existing);

                    JOptionPane.showMessageDialog(this,
                            "Transaksi berhasil diperbarui!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Create new transaction
                String id = IDGenerator.generateTransactionID();
                Transaction transaction = new Transaction(
                        id, date, description, amount, category, emotion, notes
                );

                transactionList.addTransaction(transaction);

                JOptionPane.showMessageDialog(this,
                        String.format("Transaksi berhasil disimpan!\nID: %s", id),
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            // Save to file
            fileManager.saveTransactions(transactionList.getAllTransactions());

            // Reset form and refresh parent
            resetForm();
            parent.refreshAllData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Terjadi kesalahan: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setEditMode(String transactionId) {
        this.editMode = true;
        this.editTransactionId = transactionId;

        Transaction transaction = transactionList.getTransactionById(transactionId);
        if (transaction != null) {
            // Update form fields
            idField.setText(transaction.getId());
            dateField.setText(transaction.getDate().toString());
            descriptionField.setText(transaction.getDescription());
            amountField.setText(String.valueOf(transaction.getAmount()));

            // Set category
            for (int i = 0; i < categoryCombo.getItemCount(); i++) {
                if (categoryCombo.getItemAt(i).equals(transaction.getCategory())) {
                    categoryCombo.setSelectedIndex(i);
                    break;
                }
            }

            // Set emotion
            for (int i = 0; i < emotionCombo.getItemCount(); i++) {
                if (emotionCombo.getItemAt(i).equals(transaction.getEmotion())) {
                    emotionCombo.setSelectedIndex(i);
                    break;
                }
            }

            notesArea.setText(transaction.getNotes());

            // Update title
            updateTitle("✏️ Edit Transaksi");
        }
    }

    private void updateTitle(String newTitle) {
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                Component[] subComps = panel.getComponents();
                for (Component subComp : subComps) {
                    if (subComp instanceof JLabel &&
                            ((JLabel) subComp).getFont().getSize() == 24) {
                        ((JLabel) subComp).setText(newTitle);
                        return;
                    }
                }
            }
        }
    }

    public void resetForm() {
        this.editMode = false;
        this.editTransactionId = null;

        idField.setText("(Auto-generate)");
        dateField.setText(LocalDate.now().toString());
        descriptionField.setText("");
        amountField.setText("");
        categoryCombo.setSelectedIndex(0);
        emotionCombo.setSelectedIndex(0);
        notesArea.setText("");

        updateTitle("Input Transaksi Baru");
    }
}