package com.kuliah.pemlan.service;

import com.kuliah.pemlan.model.Transaction;
import com.kuliah.pemlan.model.TransactionList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

public class AnalysisService {
    private TransactionList transactionList;

    public AnalysisService(TransactionList transactionList) {
        this.transactionList = transactionList;
    }

    public Map<String, Double> getAmountByCategory() {
        return transactionList.getAllTransactions().stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public Map<String, Double> getAmountByEmotion() {
        return transactionList.getAllTransactions().stream()
                .collect(Collectors.groupingBy(
                        Transaction::getEmotion,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public List<Transaction> getImpulsiveTransactions(double threshold) {
        return transactionList.getAllTransactions().stream()
                .filter(t -> t.getAmount() > threshold &&
                        !t.getEmotion().equalsIgnoreCase("Netral"))
                .collect(Collectors.toList());
    }

    public String generateInsight() {
        StringBuilder insight = new StringBuilder();
        insight.append("ANALISIS PENGELUARAN EMOSIONAL\n\n");

        double total = transactionList.getTotalAmount();
        int count = transactionList.getCount();

        insight.append(String.format("Total Transaksi: %d\n", count));
        insight.append(String.format("Total Pengeluaran: Rp%,.0f\n", total));
        insight.append(String.format("Rata-rata per Transaksi: Rp%,.0f\n\n",
                transactionList.getAverageAmount()));

        // Analisis berdasarkan emosi
        Map<String, Double> emotionAnalysis = getAmountByEmotion();
        if (!emotionAnalysis.isEmpty()) {
            insight.append("PENGELUARAN BERDASARKAN EMOSI:\n");
            emotionAnalysis.forEach((emotion, amount) -> {
                double percentage = (amount / total) * 100;

                insight.append(String.format(" %s: Rp%,.0f (%.1f%%)\n",
                         emotion, amount, percentage));
            });
            insight.append("\n");
        }

        // Identifikasi pola
        double impulsiveTotal = getImpulsiveTransactions(100000).stream()
                .mapToDouble(Transaction::getAmount)
                .sum();

        if (impulsiveTotal > 0) {
            double impulsivePercentage = (impulsiveTotal / total) * 100;
            insight.append(String.format("Pengeluaran Impulsif (> Rp100.000): Rp%,.0f (%.1f%%)\n",
                    impulsiveTotal, impulsivePercentage));
        }

        return insight.toString();
    }


}