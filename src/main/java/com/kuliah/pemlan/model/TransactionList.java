package com.kuliah.pemlan.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionList {
    private List<Transaction> transactions;

    public TransactionList() {
        this.transactions = new ArrayList<>();
    }

    // CRUD Operations
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public Transaction getTransactionById(String id) {
        return transactions.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean updateTransaction(String id, Transaction updatedTransaction) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId().equals(id)) {
                transactions.set(i, updatedTransaction);
                return true;
            }
        }
        return false;
    }

    public boolean deleteTransaction(String id) {
        return transactions.removeIf(t -> t.getId().equals(id));
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    // Sorting
    public List<Transaction> sortByDate(boolean ascending) {
        return transactions.stream()
                .sorted(ascending ?
                        Comparator.comparing(Transaction::getDate) :
                        Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors.toList());
    }

    public List<Transaction> sortByAmount(boolean ascending) {
        return transactions.stream()
                .sorted(ascending ?
                        Comparator.comparing(Transaction::getAmount) :
                        Comparator.comparing(Transaction::getAmount).reversed())
                .collect(Collectors.toList());
    }

    // Filtering
    public List<Transaction> filterByCategory(String category) {
        return transactions.stream()
                .filter(t -> t.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterByEmotion(String emotion) {
        return transactions.stream()
                .filter(t -> t.getEmotion().equalsIgnoreCase(emotion))
                .collect(Collectors.toList());
    }

    // Analysis
    public double getTotalAmount() {
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getAverageAmount() {
        if (transactions.isEmpty()) return 0;
        return getTotalAmount() / transactions.size();
    }

    public int getCount() {
        return transactions.size();
    }
}