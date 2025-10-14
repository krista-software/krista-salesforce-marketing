package app.krista.extensions.crm.marketing_management.salesforce_marketing.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for DatePlus utility class.
 * Tests all public static methods with 100% coverage including edge cases.
 * No Mockito - uses real concrete implementations only.
 */
class DatePlusTest {

    // Test constants for known dates
    private static final long EPOCH_MILLIS_2023_01_01 = 1672531200000L; // 2023-01-01 00:00:00 UTC
    private static final long EPOCH_MILLIS_2023_12_31 = 1704067200000L; // 2023-12-31 00:00:00 UTC
    private static final long EPOCH_MILLIS_1970_01_01 = 0L; // Unix epoch
    private static final long EPOCH_MILLIS_2000_01_01 = 946684800000L; // Y2K
    private static final long EPOCH_MILLIS_LEAP_YEAR = 1582934400000L; // 2020-02-29 (leap year)

    @Test
    @DisplayName("Should format epoch milliseconds to YYYY-MM-DD format correctly")
    void shouldFormatEpochMillisToYYYYMMDD() {
        // Given & When & Then
        assertEquals("2023-01-01", DatePlus.getYYYYMMDD(EPOCH_MILLIS_2023_01_01));
        assertEquals("2023-12-31", DatePlus.getYYYYMMDD(EPOCH_MILLIS_2023_12_31));
        assertEquals("1970-01-01", DatePlus.getYYYYMMDD(EPOCH_MILLIS_1970_01_01));
        assertEquals("2000-01-01", DatePlus.getYYYYMMDD(EPOCH_MILLIS_2000_01_01));
    }

    @Test
    @DisplayName("Should handle leap year dates correctly")
    void shouldHandleLeapYearDatesCorrectly() {
        // Given
        long leapYearFeb29 = EPOCH_MILLIS_LEAP_YEAR;

        // When
        String result = DatePlus.getYYYYMMDD(leapYearFeb29);

        // Then
        assertEquals("2020-02-29", result);
    }

    @Test
    @DisplayName("Should handle negative epoch values (dates before 1970)")
    void shouldHandleNegativeEpochValues() {
        // Given
        long beforeEpoch = -86400000L; // 1969-12-31

        // When
        String result = DatePlus.getYYYYMMDD(beforeEpoch);

        // Then
        assertEquals("1969-12-31", result);
    }

    @Test
    @DisplayName("Should handle maximum long value without overflow")
    void shouldHandleMaximumLongValue() {
        // Given
        long maxValue = Long.MAX_VALUE;

        // When & Then
        assertDoesNotThrow(() -> DatePlus.getYYYYMMDD(maxValue));
        
        String result = DatePlus.getYYYYMMDD(maxValue);
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    @DisplayName("Should handle minimum long value without overflow")
    void shouldHandleMinimumLongValue() {
        // Given
        long minValue = Long.MIN_VALUE;

        // When & Then
        assertDoesNotThrow(() -> DatePlus.getYYYYMMDD(minValue));
        
        String result = DatePlus.getYYYYMMDD(minValue);
        assertNotNull(result);
        assertTrue(result.matches("-?\\d{4,}-\\d{2}-\\d{2}"));
    }

    @Test
    @DisplayName("Should parse valid YYYY-MM-DD string to Date object correctly")
    void shouldParseValidYYYYMMDDStringToDate() {
        // Given
        String validDate = "2023-01-01";

        // When
        Date result = DatePlus.fromYYYYMMDD(validDate);

        // Then
        assertNotNull(result);
        
        // Convert back to LocalDate to verify
        LocalDate localDate = result.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        assertEquals(LocalDate.of(2023, 1, 1), localDate);
    }

    @Test
    @DisplayName("Should parse leap year date correctly")
    void shouldParseLeapYearDateCorrectly() {
        // Given
        String leapYearDate = "2020-02-29";

        // When
        Date result = DatePlus.fromYYYYMMDD(leapYearDate);

        // Then
        assertNotNull(result);
        
        LocalDate localDate = result.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        assertEquals(LocalDate.of(2020, 2, 29), localDate);
    }

    @Test
    @DisplayName("Should handle edge case dates correctly")
    void shouldHandleEdgeCaseDatesCorrectly() {
        // Test various edge cases
        assertNotNull(DatePlus.fromYYYYMMDD("1900-01-01")); // Century boundary
        assertNotNull(DatePlus.fromYYYYMMDD("2000-01-01")); // Y2K
        assertNotNull(DatePlus.fromYYYYMMDD("9999-12-31")); // Far future
        assertNotNull(DatePlus.fromYYYYMMDD("0001-01-01")); // Far past
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should return null for null or empty date strings")
    void shouldReturnNullForNullOrEmptyDateStrings(String invalidDate) {
        // When
        Date result = DatePlus.fromYYYYMMDD(invalidDate);

        // Then
        assertNull(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid-date",
        "2023-13-01",    // Invalid month
        "2023-02-30",    // Invalid day for February
        "2023-04-31",    // Invalid day for April
        "2021-02-29",    // Invalid leap year date
        "23-01-01",      // Wrong year format
        "2023-1-1",      // Wrong month/day format
        "2023/01/01",    // Wrong separator
        "01-01-2023",    // Wrong order
        "2023-01",       // Incomplete date
        "2023-01-01-01", // Too many parts
        "   ",           // Whitespace only
        "abc-def-ghi",   // Non-numeric
        "2023-00-01",    // Invalid month (0)
        "2023-01-00",    // Invalid day (0)
        "-2023-01-01",   // Negative year with dash
        "2023--01-01",   // Double dash
        "2023-01--01"    // Double dash
    })
    @DisplayName("Should return null for invalid date format strings")
    void shouldReturnNullForInvalidDateFormatStrings(String invalidDate) {
        // When
        Date result = DatePlus.fromYYYYMMDD(invalidDate);

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("Should handle very large year values")
    void shouldHandleVeryLargeYearValues() {
        // Given
        String largeYearDate = "999999-01-01";

        // When
        Date result = DatePlus.fromYYYYMMDD(largeYearDate);

        // Then
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should be consistent - round trip conversion should work")
    void shouldBeConsistentRoundTripConversion() {
        // Given
        long originalTimestamp = EPOCH_MILLIS_2023_01_01;

        // When
        String dateString = DatePlus.getYYYYMMDD(originalTimestamp);
        Date parsedDate = DatePlus.fromYYYYMMDD(dateString);

        // Then
        assertNotNull(parsedDate);
        
        // Convert parsed date back to string and compare
        long parsedTimestamp = parsedDate.getTime();
        String roundTripDateString = DatePlus.getYYYYMMDD(parsedTimestamp);
        assertEquals(dateString, roundTripDateString);
    }

    @Test
    @DisplayName("Should handle timezone consistency")
    void shouldHandleTimezoneConsistency() {
        // Given
        String dateString = "2023-06-15";

        // When
        Date result = DatePlus.fromYYYYMMDD(dateString);

        // Then
        assertNotNull(result);
        
        // Verify the date is at start of day in system timezone
        LocalDate localDate = result.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        assertEquals(LocalDate.of(2023, 6, 15), localDate);
    }

    @Test
    @DisplayName("Should handle multiple consecutive calls without side effects")
    void shouldHandleMultipleConsecutiveCallsWithoutSideEffects() {
        // Given
        long timestamp = EPOCH_MILLIS_2023_01_01;
        String dateString = "2023-01-01";

        // When - Multiple calls
        String result1 = DatePlus.getYYYYMMDD(timestamp);
        String result2 = DatePlus.getYYYYMMDD(timestamp);
        Date date1 = DatePlus.fromYYYYMMDD(dateString);
        Date date2 = DatePlus.fromYYYYMMDD(dateString);

        // Then - Results should be identical
        assertEquals(result1, result2);
        assertEquals(date1, date2);
    }

    @Test
    @DisplayName("Should handle boundary values for months and days")
    void shouldHandleBoundaryValuesForMonthsAndDays() {
        // Test first and last day of year
        assertNotNull(DatePlus.fromYYYYMMDD("2023-01-01"));
        assertNotNull(DatePlus.fromYYYYMMDD("2023-12-31"));
        
        // Test month boundaries
        assertNotNull(DatePlus.fromYYYYMMDD("2023-01-31")); // January
        assertNotNull(DatePlus.fromYYYYMMDD("2023-02-28")); // February non-leap
        assertNotNull(DatePlus.fromYYYYMMDD("2023-03-31")); // March
        assertNotNull(DatePlus.fromYYYYMMDD("2023-04-30")); // April
        assertNotNull(DatePlus.fromYYYYMMDD("2023-05-31")); // May
        assertNotNull(DatePlus.fromYYYYMMDD("2023-06-30")); // June
        assertNotNull(DatePlus.fromYYYYMMDD("2023-07-31")); // July
        assertNotNull(DatePlus.fromYYYYMMDD("2023-08-31")); // August
        assertNotNull(DatePlus.fromYYYYMMDD("2023-09-30")); // September
        assertNotNull(DatePlus.fromYYYYMMDD("2023-10-31")); // October
        assertNotNull(DatePlus.fromYYYYMMDD("2023-11-30")); // November
        assertNotNull(DatePlus.fromYYYYMMDD("2023-12-31")); // December
    }

    @Test
    @DisplayName("Should validate DatePlus class is final and has private constructor")
    void shouldValidateDatePlusClassIsFinalAndHasPrivateConstructor() {
        // Verify class is final
        assertTrue(java.lang.reflect.Modifier.isFinal(DatePlus.class.getModifiers()));
        
        // Verify constructor is private
        try {
            java.lang.reflect.Constructor<DatePlus> constructor = DatePlus.class.getDeclaredConstructor();
            assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("DatePlus should have a private no-args constructor");
        }
    }
}
