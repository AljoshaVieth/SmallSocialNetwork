package de.aljoshavieth.smallsocialandroidapp;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class Utils {
    public static String unixToFormattedTime(Long unixTimeStamp){
        Instant instant = Instant.ofEpochMilli(unixTimeStamp*1000);
        ZoneId zoneId = ZoneId.of("Europe/Berlin");
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
        Locale locale = Locale.GERMANY;
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(locale);
        return zdt.format(formatter);
    }
}
