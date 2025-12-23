package com.kuliah.pemlan.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ValidationService {

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

    public static class ValidationResult {
        private boolean valid;
        private final List<String> errors;

        public ValidationResult() {
            this.valid = true;
            this.errors = new ArrayList<>();
        }

        public void addError(String error) {
            this.errors.add(error);
            this.valid = false;
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrors() {
            return errors;
        }

        public String getErrorMessage() {
            return String.join("\n", errors);
        }
    }
}