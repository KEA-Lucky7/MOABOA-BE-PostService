package com.example.lucky7postservice.utils.config;


import org.springframework.cglib.core.Local;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SetTime {
    public static String timestampToString(Timestamp timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd");
        Date date = new Date(timestamp.getTime());

        return format.format(date);
    }

    public static LocalDate stringToLocalDate(String date) {
        date = date.replace('.', '-');
        return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    }

    public static LocalDate getMonthStart(String month) {
        String startDate = month + ".01";
        startDate = startDate.replace('.', '-');
        return LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
    }

    public static LocalDate getMonthEnd(String month) {
        LocalDate startDate = getMonthStart(month);
        return startDate.plusMonths(1);
    }

    public static boolean specificDateValidation(LocalDate specificDate) {
        LocalDate today = LocalDate.now();
        return !specificDate.isAfter(today);
    }
}
