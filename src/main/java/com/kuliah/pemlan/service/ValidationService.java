package com.kuliah.pemlan.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Layanan untuk validasi data transaksi.
 * Kelas ini menyediakan metode untuk memvalidasi input transaksi
 * seperti tanggal, deskripsi, jumlah, kategori, dan emosi.
 *
 * @author [AZIZI]
 * @version 1.0
 */
public class ValidationService {

    /**
     * Memvalidasi data transaksi yang diinput oleh pengguna.
     *
     * @param dateStr String tanggal dalam format "YYYY-MM-DD"
     * @param description Deskripsi transaksi
     * @param amountStr String jumlah transaksi
     * @param category Kategori transaksi
     * @param emotion Kondisi emosi transaksi
     * @return Objek ValidationResult yang berisi status validasi dan pesan error
     */
    public static ValidationResult validateTransaction(
            String dateStr, String description, String amountStr,
            String category, String emotion) {

        ValidationResult result = new ValidationResult();

        // Validasi Tanggal
        if (dateStr == null || dateStr.trim().isEmpty()) {
            result.addError("Tanggal tidak boleh kosong");
        } else {
            try {
                LocalDate date = LocalDate.parse(dateStr);
                if (date.isAfter(LocalDate.now())) {
                    result.addError("Tanggal tidak boleh di masa depan");
                }
            } catch (DateTimeParseException e) {
                result.addError("Format tanggal tidak valid (gunakan format YYYY-MM-DD)");
            }
        }

        // Validasi Deskripsi
        if (description == null || description.trim().isEmpty()) {
            result.addError("Deskripsi tidak boleh kosong");
        } else if (description.length() > 100) {
            result.addError("Deskripsi maksimal 100 karakter");
        }

        // Validasi Jumlah
        if (amountStr == null || amountStr.trim().isEmpty()) {
            result.addError("Jumlah tidak boleh kosong");
        } else {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    result.addError("Jumlah harus lebih besar dari 0");
                }
            } catch (NumberFormatException e) {
                result.addError("Jumlah harus berupa angka");
            }
        }

        // Validasi Kategori
        if (category == null || category.trim().isEmpty() || category.equals("Pilih kategori...")) {
            result.addError("Kategori harus dipilih");
        }

        // Validasi Emosi
        if (emotion == null || emotion.trim().isEmpty() || emotion.startsWith("Pilih")) {
            result.addError("Kondisi emosi harus dipilih");
        }

        return result;
    }

    /**
     * Kelas untuk menyimpan hasil validasi.
     * Kelas ini berisi status validasi dan daftar pesan error jika ada.
     */
    public static class ValidationResult {
        /** Status validasi (true jika valid, false jika tidak valid) */
        private boolean valid;

        /** Daftar pesan error jika validasi gagal */
        private final List<String> errors;

        /**
         * Konstruktor untuk membuat ValidationResult.
         * Status awal adalah valid (true) dan daftar error kosong.
         */
        public ValidationResult() {
            this.valid = true;
            this.errors = new ArrayList<>();
        }

        /**
         * Menambahkan pesan error ke daftar error.
         * Menandakan bahwa validasi gagal.
         *
         * @param error Pesan error yang akan ditambahkan
         */
        public void addError(String error) {
            this.errors.add(error);
            this.valid = false;
        }

        /**
         * Memeriksa apakah data valid.
         *
         * @return true jika valid, false jika tidak valid
         */
        public boolean isValid() {
            return valid;
        }

        /**
         * Mendapatkan daftar pesan error.
         *
         * @return List yang berisi pesan-pesan error
         */
        public List<String> getErrors() {
            return errors;
        }

        /**
         * Mendapatkan semua pesan error dalam format string.
         * Pesan-pesan dipisahkan oleh newline.
         *
         * @return String yang berisi semua pesan error
         */
        public String getErrorMessage() {
            return String.join("\n", errors);
        }
    }
}
