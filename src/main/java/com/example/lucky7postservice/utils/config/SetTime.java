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
        return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    }
}
