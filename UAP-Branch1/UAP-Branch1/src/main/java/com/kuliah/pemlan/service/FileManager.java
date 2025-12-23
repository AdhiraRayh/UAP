package com.kuliah.pemlan.service;

import com.kuliah.pemlan.model.Transaction;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String FILE_NAME = "transactions.csv";

    public void saveTransactions(List<Transaction> transactions) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("id,date,description,amount,category,emotion,notes");
            writer.newLine();

            for (Transaction transaction : transactions) {
                writer.write(transaction.toCSV());
                writer.newLine();
            }
        }
    }

    public List<Transaction> loadTransactions() throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return transactions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (!line.trim().isEmpty()) {
                    try {
                        // Coba repair line jika corrupt
                        String repairedLine = repairCSVLine(line);
                        Transaction transaction = new Transaction(repairedLine);
                        transactions.add(transaction);
                    } catch (Exception e) {
                        System.err.println("Error parsing line " + lineNumber + ": " + line);
                        System.err.println("Error: " + e.getMessage());
                        // Skip line yang benar-benar rusak
                    }
                }
            }
        }

        return transactions;
    }

    /**
     * Method untuk memperbaiki line CSV yang corrupt
     * Contoh: "TRX2512240208001,2025-12-24,mboh,100000,00,Makanan & Minuman,Senang,bali mie"
     * Harusnya: "TRX2512240208001,2025-12-24,mboh,100000,Makanan & Minuman,Senang,bali mie"
     */
    private String repairCSVLine(String line) {
        String[] parts = line.split(",", -1); // -1 untuk keep empty strings

        // Jika ada 8 kolom dan kolom ke-4 (index 4) adalah angka (seperti "00")
        if (parts.length == 8 && parts[4].matches("\\d+(\\.\\d+)?")) {
            // Reconstruct tanpa kolom yang salah (index 4)
            StringBuilder repaired = new StringBuilder();
            for (int i = 0; i < parts.length; i++) {
                if (i != 4) { // Skip kolom yang salah
                    if (repaired.length() > 0) repaired.append(",");
                    repaired.append(parts[i]);
                }
            }
            System.out.println("Repaired CSV line: " + line + " -> " + repaired.toString());
            return repaired.toString();
        }

        // Jika ada 7 kolom tapi kolom ke-4 (category) adalah angka
        if (parts.length == 7 && parts[4].matches("\\d+(\\.\\d+)?")) {
            // Geser semua kolom setelah category
            parts[4] = parts[5]; // emotion -> category
            parts[5] = parts[6]; // notes -> emotion
            parts[6] = ""; // notes kosong

            StringBuilder repaired = new StringBuilder();
            for (int i = 0; i < 7; i++) {
                if (i > 0) repaired.append(",");
                repaired.append(parts[i]);
            }
            System.out.println("Repaired CSV line: " + line + " -> " + repaired.toString());
            return repaired.toString();
        }

        return line; // Return asli jika tidak perlu repair
    }
}