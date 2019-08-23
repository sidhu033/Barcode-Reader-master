package com.notbytes.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateTimeUtil {

    public static final String JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String SIMPLE_DATE_FORMAT = "dd/MM/yyyy";
    public static final String DB_SIMPLE_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_TIME = "00:00";

    private DateTimeUtil() {

    }

    public static String getTimeInHeaderFormat(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(JSON_DATE_FORMAT);
        String formattedDate = simpleDateFormat.format(calendar.getTime());
        return formattedDate;
    }

    public static String getSimpleDate(Date date) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault());
            return simpleDateFormat.format(date.getTime());
        }
        return null;
    }

    public static int getDaysFromString(String date) {

        String[] newDate = date.split("-");

        String year = "";

        for (String value : newDate) {
            year += value;
        }

        return Integer.valueOf(year.trim());
    }
}
