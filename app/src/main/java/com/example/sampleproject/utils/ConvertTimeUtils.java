package com.example.sampleproject.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConvertTimeUtils {
    static public String convertTimestampToDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.getDefault());
        Date time = new Date(timestamp);
        return sdf.format(time);
    }
}
