package com.kuliah.pemlan.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Kelas utilitas untuk menghasilkan ID transaksi yang unik.
 * ID dihasilkan dengan format: TRX + timestamp + sequence number
 * Format timestamp: YYMMDDHHMM (2-digit tahun, bulan, hari, jam, menit)
 * Format sequence: 3-digit angka dengan increment
 *
 * @author [AZIZI]
 * @version 1.0
 */
public class IDGenerator {
    /** Counter untuk sequence number, menggunakan AtomicInteger untuk thread safety */
    private static final AtomicInteger counter = new AtomicInteger(1);

    /** Formatter untuk timestamp dengan format YYMMDDHHMM */
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyMMddHHmm");

    /**
     * Menghasilkan ID transaksi yang unik.
     * Format ID: TRX + YYMMDDHHMM + 3-digit sequence number
     * Contoh: TRX241224143001 (artinya: 24 Desember 2024, 14:30, transaksi ke-1)
     *
     * @return String ID transaksi yang unik
     */
    public static String generateTransactionID() {
        // Ambil timestamp saat ini
        String timestamp = LocalDateTime.now().format(formatter);

        // Ambil sequence number dengan increment
        int sequence = counter.getAndIncrement();

        // Reset counter jika sudah mencapai 999
        if (sequence > 999) {
            counter.set(1);
            sequence = 1;
        }

        // Format: TRX + timestamp + sequence 3-digit
        return String.format("TRX%s%03d", timestamp, sequence);
    }
}