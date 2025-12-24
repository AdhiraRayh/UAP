package com.kuliah.pemlan.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kelas yang merepresentasikan kumpulan transaksi (daftar transaksi).
 * Kelas ini menyediakan operasi CRUD, pengurutan, filter, dan analisis data transaksi.
 *
 * @author [AZIZI]
 * @version 1.0
 */
public class TransactionList {
    /** Daftar transaksi yang disimpan dalam koleksi List */
    private List<Transaction> transactions;

    /**
     * Konstruktor untuk membuat objek TransactionList.
     * Menginisialisasi daftar transaksi kosong.
     */
    public TransactionList() {
        this.transactions = new ArrayList<>();
    }

    // CRUD Operations

    /**
     * Menambahkan transaksi baru ke dalam daftar.
     *
     * @param transaction Objek Transaction yang akan ditambahkan
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Mendapatkan transaksi berdasarkan ID.
     *
     * @param id ID transaksi yang dicari
     * @return Objek Transaction jika ditemukan, null jika tidak ditemukan
     */
    public Transaction getTransactionById(String id) {
        return transactions.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Memperbarui transaksi yang sudah ada berdasarkan ID.
     *
     * @param id ID transaksi yang akan diperbarui
     * @param updatedTransaction Objek Transaction baru dengan data yang diperbarui
     * @return true jika berhasil diperbarui, false jika transaksi tidak ditemukan
     */
    public boolean updateTransaction(String id, Transaction updatedTransaction) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId().equals(id)) {
                transactions.set(i, updatedTransaction);
                return true;
            }
        }
        return false;
    }

    /**
     * Menghapus transaksi berdasarkan ID.
     *
     * @param id ID transaksi yang akan dihapus
     * @return true jika berhasil dihapus, false jika transaksi tidak ditemukan
     */
    public boolean deleteTransaction(String id) {
        return transactions.removeIf(t -> t.getId().equals(id));
    }

    /**
     * Mendapatkan salinan dari semua transaksi.
     * Mengembalikan salinan untuk mencegah modifikasi langsung pada daftar asli.
     *
     * @return List yang berisi semua transaksi
     */
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    // Sorting

    /**
     * Mengurutkan transaksi berdasarkan tanggal.
     *
     * @param ascending true untuk urutan menaik (terlama ke terbaru),
     *                  false untuk urutan menurun (terbaru ke terlama)
     * @return List transaksi yang sudah diurutkan
     */
    public List<Transaction> sortByDate(boolean ascending) {
        return transactions.stream()
                .sorted(ascending ?
                        Comparator.comparing(Transaction::getDate) :
                        Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Mengurutkan transaksi berdasarkan jumlah (amount).
     *
     * @param ascending true untuk urutan menaik (terkecil ke terbesar),
     *                  false untuk urutan menurun (terbesar ke terkecil)
     * @return List transaksi yang sudah diurutkan
     */
    public List<Transaction> sortByAmount(boolean ascending) {
        return transactions.stream()
                .sorted(ascending ?
                        Comparator.comparing(Transaction::getAmount) :
                        Comparator.comparing(Transaction::getAmount).reversed())
                .collect(Collectors.toList());
    }

    // Filtering

    /**
     * Memfilter transaksi berdasarkan kategori.
     *
     * @param category Kategori yang digunakan sebagai filter
     * @return List transaksi yang sesuai dengan kategori
     */
    public List<Transaction> filterByCategory(String category) {
        return transactions.stream()
                .filter(t -> t.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Memfilter transaksi berdasarkan kondisi emosi.
     *
     * @param emotion Kondisi emosi yang digunakan sebagai filter
     * @return List transaksi yang sesuai dengan kondisi emosi
     */
    public List<Transaction> filterByEmotion(String emotion) {
        return transactions.stream()
                .filter(t -> t.getEmotion().equalsIgnoreCase(emotion))
                .collect(Collectors.toList());
    }

    // Analysis

    /**
     * Menghitung total jumlah (amount) dari semua transaksi.
     *
     * @return Total jumlah dari semua transaksi
     */
    public double getTotalAmount() {
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Menghitung rata-rata jumlah (amount) transaksi.
     *
     * @return Rata-rata jumlah transaksi, atau 0 jika tidak ada transaksi
     */
    public double getAverageAmount() {
        if (transactions.isEmpty()) return 0;
        return getTotalAmount() / transactions.size();
    }

    /**
     * Mendapatkan jumlah total transaksi.
     *
     * @return Jumlah transaksi dalam daftar
     */
    public int getCount() {
        return transactions.size();
    }
}