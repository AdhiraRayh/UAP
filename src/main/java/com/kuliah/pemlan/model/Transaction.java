package com.kuliah.pemlan.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        String[] data = csvLine.split(",", 7);
        if (data.length >= 7) {
            this.id = data[0];
            this.date = LocalDate.parse(data[1]);
            this.description = data[2];
            this.amount = Double.parseDouble(data[3]);
            this.category = data[4];
            this.emotion = data[5];
            this.notes = data[6];
        }
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