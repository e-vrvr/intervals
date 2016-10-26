package com.intervals.model.jaxb;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class ParseHelper {
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm:ss");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");

    public static DateTime parseTime(String input){
       return TIME_FORMATTER.parseDateTime(input);
    }

    public static DateTime parseDateTime(String input){
        return DATE_TIME_FORMATTER.parseDateTime(input);
    }

    public static DateTime parseDate(String input){
        return DATE_FORMATTER.parseDateTime(input);
    }
}
