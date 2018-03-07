package com.company.popularmovies.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class StringUtil {
    private static final Locale LOCALE = Locale.getDefault();
    private static final SimpleDateFormat SDF_VIEW_DATE = new SimpleDateFormat("MMM dd, yyyy", LOCALE);
    private static final SimpleDateFormat SDF_PARSE_DATE = new SimpleDateFormat("yyyy-MM-dd", LOCALE);
    private static final String DECIMAL_FORMAT = "%.1f/10";

    private StringUtil() {}

    public static Date parseDate(String str) throws ParseException {
        return SDF_PARSE_DATE.parse(str);
    }

    public static String formatDate(Date date) {
        return SDF_VIEW_DATE.format(date);
    }

    public static String formatRating(double rating) {
        return String.format(LOCALE, DECIMAL_FORMAT, rating);
    }

    public static String formatPath(String format, String order, String appKey) {
        return String.format(LOCALE, format, order, appKey);
    }

    public static String formatMessage(String format, String message) {
        return String.format(LOCALE, format, message);
    }
}