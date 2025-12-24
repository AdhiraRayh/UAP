package com.kuliah.pemlan.service;

import com.kuliah.pemlan.model.Transaction;
import com.kuliah.pemlan.model.TransactionList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Layanan untuk melakukan analisis data transaksi.
 * Kelas ini menyediakan berbagai metode untuk menganalisis data transaksi
 * seperti pengelompokan berdasarkan kategori dan emosi, serta generasi insight.
 *
 * @author [AZIZI]
 * @version 1.0
 */
public class AnalysisService {
    /** Daftar transaksi yang akan dianalisis */
    private TransactionList transactionList;

    /**
     * Konstruktor untuk membuat AnalysisService.
     *
     * @param transactionList Objek TransactionList yang berisi data transaksi
     */
    public AnalysisService(TransactionList transactionList) {
        this.transactionList = transactionList;
    }

    /**
     * Mendapatkan total pengeluaran yang dikelompokkan berdasarkan kategori.
     *
     * @return Map dengan key sebagai nama kategori dan value sebagai total pengeluaran
     */
    public Map<String, Double> getAmountByCategory() {
        return transactionList.getAllTransactions().stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    /**
     * Mendapatkan total pengeluaran yang dikelompokkan berdasarkan kondisi emosi.
     *
     * @return Map dengan key sebagai nama emosi dan value sebagai total pengeluaran
     */
    public Map<String, Double> getAmountByEmotion() {
        return transactionList.getAllTransactions().stream()
                .collect(Collectors.groupingBy(
                        Transaction::getEmotion,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    /**
     * Mendapatkan daftar transaksi impulsif berdasarkan threshold tertentu.
     * Transaksi dianggap impulsif jika jumlahnya melebihi threshold dan
     * tidak memiliki emosi "Netral".
     *
     * @param threshold Batas jumlah uang untuk dianggap sebagai transaksi impulsif
     * @return List transaksi yang dianggap impulsif
     */
    public List<Transaction> getImpulsiveTransactions(double threshold) {
        return transactionList.getAllTransactions().stream()
                .filter(t -> t.getAmount() > threshold &&
                        !t.getEmotion().equalsIgnoreCase("Netral"))
                .collect(Collectors.toList());
    }

    /**
     * Menghasilkan insight analisis dari data transaksi.
     * Insight mencakup statistik dasar, analisis emosi, dan identifikasi pola.
     *
     * @return String berisi insight analisis yang diformat
     */
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