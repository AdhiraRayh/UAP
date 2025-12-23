package com.kuliah.pemlan.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private String id;
    private LocalDate date;
    private String description;
    private double amount;
    private String category;
    private String emotion;
    private String notes;

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
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getEmotion() { return emotion; }
    public void setEmotion(String emotion) { this.emotion = emotion; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String toCSV() {
        return String.format("%s,%s,%s,%.2f,%s,%s,%s",
                id, date, description, amount, category, emotion, notes);
    }

    @Override
    public String toString() {
        return String.format("%s - %s - Rp%,.0f - %s",
                date, description, amount, emotion);
    }
}