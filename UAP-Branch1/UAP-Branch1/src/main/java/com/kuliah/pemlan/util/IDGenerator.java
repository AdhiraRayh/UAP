package com.kuliah.pemlan.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {
    private static final AtomicInteger counter = new AtomicInteger(1);
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyMMddHHmm");

    public static String generateTransactionID() {
        String timestamp = LocalDateTime.now().format(formatter);
        int sequence = counter.getAndIncrement();

        if (sequence > 999) {
            counter.set(1);
            sequence = 1;
        }

        return String.format("TRX%s%03d", timestamp, sequence);
    }
}