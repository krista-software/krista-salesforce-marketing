/*
 * Salesforce Marketing Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.krista.extensions.crm.marketing_management.salesforce_marketing.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.Date;

public final class DatePlus {

    // Formatter for parsing with variable width year (4-10 digits) and strict validation
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 4, 10, SignStyle.NORMAL)
            .appendLiteral('-')
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendLiteral('-')
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);

    // Formatter for formatting dates
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 1, 10, SignStyle.NORMAL)
            .appendLiteral('-')
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendLiteral('-')
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .toFormatter();

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
        // Handle null or empty strings
        if (date == null || date.isEmpty()) {
            return null;
        }

        // Reject negative years (dates starting with -)
        if (date.startsWith("-")) {
            return null;
        }

        try {
            LocalDate localDate = LocalDate.parse(date, PARSER);
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private DatePlus() {}

}
