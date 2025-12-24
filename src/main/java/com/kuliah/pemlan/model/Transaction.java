package com.kuliah.pemlan.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Kelas yang merepresentasikan sebuah transaksi keuangan.
 * Setiap transaksi memiliki ID, tanggal, deskripsi, jumlah, kategori,
 * kondisi emosi, dan catatan tambahan.
 *
 * @author [AZIZI}
 * @version 1.0
 */
public class Transaction {
    /** ID unik untuk transaksi */
    private String id;

    /** Tanggal terjadinya transaksi */
    private LocalDate date;

    /** Deskripsi atau nama transaksi */
    private String description;

    /** Jumlah uang yang dikeluarkan dalam transaksi */
    private double amount;

    /** Kategori transaksi (makanan, transportasi, dll) */
    private String category;

    /** Kondisi emosi saat melakukan transaksi */
    private String emotion;

    /** Catatan tambahan untuk transaksi */
    private String notes;

    /**
     * Konstruktor untuk membuat objek Transaction dengan parameter lengkap.
     *
     * @param id ID unik transaksi
     * @param date Tanggal transaksi
     * @param description Deskripsi transaksi
     * @param amount Jumlah transaksi
     * @param category Kategori transaksi
     * @param emotion Kondisi emosi
     * @param notes Catatan tambahan
     */
    public Transaction(String id, LocalDate date, String description,
                       double amount, String category, String emotion, String notes) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.emotion = emotion;
        this.notes = notes;
    }

    /**
     * Konstruktor untuk membuat objek Transaction dari baris CSV.
     * Baris CSV akan di-parse menjadi komponen-komponen transaksi.
     *
     * @param csvLine Baris data dalam format CSV
     */
    public Transaction(String csvLine) {
        // Gunakan regex yang lebih tepat untuk parsing CSV
        // Ini menangani field yang mengandung koma dalam quotes
        List<String> data = parseCSVLine(csvLine);

        if (data.size() >= 7) {
            this.id = data.get(0);
            this.date = LocalDate.parse(data.get(1));
            this.description = data.get(2);
            this.amount = Double.parseDouble(data.get(3));
            this.category = data.get(4);
            this.emotion = data.get(5);
            this.notes = data.get(6);
        }
    }

    /**
     * Metode helper untuk mem-parse baris CSV.
     * Metode ini menangani kasus khusus seperti field yang mengandung koma dalam quotes.
     *
     * @param csvLine Baris CSV yang akan di-parse
     * @return List yang berisi field-field yang telah di-parse
     */
    private List<String> parseCSVLine(String csvLine) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < csvLine.length(); i++) {
            char c = csvLine.charAt(i);

            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        return result;
    }

    // Getters and Setters

    /**
     * Mendapatkan ID transaksi.
     *
     * @return ID transaksi
     */
    public String getId() { return id; }

    /**
     * Mengatur ID transaksi.
     *
     * @param id ID transaksi baru
     */
    public void setId(String id) { this.id = id; }

    /**
     * Mendapatkan tanggal transaksi.
     *
     * @return Tanggal transaksi
     */
    public LocalDate getDate() { return date; }

    /**
     * Mengatur tanggal transaksi.
     *
     * @param date Tanggal transaksi baru
     */
    public void setDate(LocalDate date) { this.date = date; }

    /**
     * Mendapatkan deskripsi transaksi.
     *
     * @return Deskripsi transaksi
     */
    public String getDescription() { return description; }

    /**
     * Mengatur deskripsi transaksi.
     *
     * @param description Deskripsi transaksi baru
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Mendapatkan jumlah transaksi.
     *
     * @return Jumlah transaksi
     */
    public double getAmount() { return amount; }

    /**
     * Mengatur jumlah transaksi.
     *
     * @param amount Jumlah transaksi baru
     */
    public void setAmount(double amount) { this.amount = amount; }

    /**
     * Mendapatkan kategori transaksi.
     *
     * @return Kategori transaksi
     */
    public String getCategory() { return category; }

    /**
     * Mengatur kategori transaksi.
     *
     * @param category Kategori transaksi baru
     */
    public void setCategory(String category) { this.category = category; }

    /**
     * Mendapatkan kondisi emosi transaksi.
     *
     * @return Kondisi emosi transaksi
     */
    public String getEmotion() { return emotion; }

    /**
     * Mengatur kondisi emosi transaksi.
     *
     * @param emotion Kondisi emosi transaksi baru
     */
    public void setEmotion(String emotion) { this.emotion = emotion; }

    /**
     * Mendapatkan catatan transaksi.
     *
     * @return Catatan transaksi
     */
    public String getNotes() { return notes; }

    /**
     * Mengatur catatan transaksi.
     *
     * @param notes Catatan transaksi baru
     */
    public void setNotes(String notes) { this.notes = notes; }

    /**
     * Mengonversi transaksi ke format CSV.
     *
     * @return String dalam format CSV yang mewakili transaksi
     */
    public String toCSV() {
        return String.format("%s,%s,%s,%.2f,%s,%s,%s",
                id, date, description, amount, category, emotion, notes);
    }

    /**
     * Mengonversi transaksi ke format string yang mudah dibaca.
     *
     * @return String representasi transaksi
     */
    @Override
    public String toString() {
        return String.format("%s - %s - Rp%,.0f - %s",
                date, description, amount, emotion);
    }
}