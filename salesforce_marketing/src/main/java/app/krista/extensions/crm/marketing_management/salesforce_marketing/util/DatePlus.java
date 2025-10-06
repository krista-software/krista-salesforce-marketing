package app.krista.extensions.crm.marketing_management.salesforce_marketing.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public final class DatePlus {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * This function will return time stamp in required date format
     *
     * @param date timestamp value
     * @return formatted date string
     */
    public static String getYYYYMMDD(long date) {
        return Instant.ofEpochMilli(date)
                .atZone(ZoneId.systemDefault())
                .format(FORMATTER);
    }

    /**
     * This function will return {@link Date} object for given date string
     *
     * @param date Date String value
     * @return {@link Date} object
     */
    public static Date fromYYYYMMDD(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date, FORMATTER);
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private DatePlus() {}

}
