package com.example.lucky7postservice.utils.config;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class SetTime {
    public static String timestampToString(Timestamp timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
        Date date = new Date(timestamp.getTime());

        return format.format(date);
    }
}
