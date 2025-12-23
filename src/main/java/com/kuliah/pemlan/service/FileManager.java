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

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (!line.trim().isEmpty()) {
                    try {
                        Transaction transaction = new Transaction(line);
                        transactions.add(transaction);
                    } catch (Exception e) {
                        System.err.println("Error parsing line: " + line);
                    }
                }
            }
        }

        return transactions;
    }
}