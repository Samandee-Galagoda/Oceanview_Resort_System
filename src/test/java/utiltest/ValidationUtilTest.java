package utiltest;

import com.oceanview.resort.util.ValidationUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD tests for ValidationUtil.
 * Covers failure scenarios: null values, empty strings, invalid formats.
 */
public class ValidationUtilTest {

    /**
     * TDD failure test 1: requireNonBlank with null value adds error.
     */
    @Test
    public void testRequireNonBlank_WithNullValue_AddsError() {
        List<String> errors = new ArrayList<>();

        ValidationUtil.requireNonBlank(null, "Guest Name", errors);

        assertEquals(1, errors.size(), "requireNonBlank with null should add one error");
        assertTrue(errors.contains("Guest Name is required"), "Error message should match");
    }

    /**
     * TDD failure test 2: requireNonBlank with empty string adds error.
     */
    @Test
    public void testRequireNonBlank_WithEmptyString_AddsError() {
        List<String> errors = new ArrayList<>();

        ValidationUtil.requireNonBlank("", "Address", errors);

        assertEquals(1, errors.size(), "requireNonBlank with empty string should add one error");
        assertTrue(errors.contains("Address is required"), "Error message should match");
    }

    /**
     * TDD failure test 3: requireNonBlank with blank string (spaces only) adds error.
     */
    @Test
    public void testRequireNonBlank_WithBlankString_AddsError() {
        List<String> errors = new ArrayList<>();

        ValidationUtil.requireNonBlank("   ", "Contact Number", errors);

        assertEquals(1, errors.size(), "requireNonBlank with blank string should add one error");
        assertTrue(errors.contains("Contact Number is required"), "Error message should match");
    }

    /**
     * TDD failure test 4: parseDate with null value adds error and returns null.
     */
    @Test
    public void testParseDate_WithNullValue_AddsErrorAndReturnsNull() {
        List<String> errors = new ArrayList<>();

        LocalDate result = ValidationUtil.parseDate(null, "Check In Date", errors);

        assertNull(result, "parseDate with null should return null");
        assertEquals(1, errors.size(), "parseDate with null should add one error");
        assertTrue(errors.contains("Check In Date is required"), "Error message should match");
    }

    /**
     * TDD failure test 5: parseDate with empty string adds error and returns null.
     */
    @Test
    public void testParseDate_WithEmptyString_AddsErrorAndReturnsNull() {
        List<String> errors = new ArrayList<>();

        LocalDate result = ValidationUtil.parseDate("", "Check Out Date", errors);

        assertNull(result, "parseDate with empty string should return null");
        assertEquals(1, errors.size(), "parseDate with empty string should add one error");
        assertTrue(errors.contains("Check Out Date is required"), "Error message should match");
    }

    /**
     * TDD failure test 6: parseDate with blank string adds error and returns null.
     */
    @Test
    public void testParseDate_WithBlankString_AddsErrorAndReturnsNull() {
        List<String> errors = new ArrayList<>();

        LocalDate result = ValidationUtil.parseDate("   ", "Check In Date", errors);

        assertNull(result, "parseDate with blank string should return null");
        assertEquals(1, errors.size(), "parseDate with blank string should add one error");
    }

    /**
     * TDD failure test 7: parseDate with invalid format adds error and returns null.
     */
    @Test
    public void testParseDate_WithInvalidFormat_AddsErrorAndReturnsNull() {
        List<String> errors = new ArrayList<>();

        LocalDate result = ValidationUtil.parseDate("01/15/2025", "Check In Date", errors);

        assertNull(result, "parseDate with invalid format should return null");
        assertEquals(1, errors.size(), "parseDate with invalid format should add one error");
        assertTrue(errors.contains("Check In Date must be in yyyy-MM-dd format"), "Error message should match");
    }

    /**
     * TDD failure test 8: parseDate with invalid date string adds error and returns null.
     */
    @Test
    public void testParseDate_WithInvalidDateString_AddsErrorAndReturnsNull() {
        List<String> errors = new ArrayList<>();

        LocalDate result = ValidationUtil.parseDate("2025-13-45", "Check In Date", errors);

        assertNull(result, "parseDate with invalid date string should return null");
        assertEquals(1, errors.size(), "parseDate with invalid date string should add one error");
        assertTrue(errors.contains("Check In Date must be in yyyy-MM-dd format"), "Error message should match");
    }

    /**
     * TDD failure test 9: isValidContact with null returns false.
     */
    @Test
    public void testIsValidContact_WithNull_ReturnsFalse() {
        boolean result = ValidationUtil.isValidContact(null);

        assertFalse(result, "isValidContact with null should return false");
    }

    /**
     * TDD failure test 10: isValidContact with empty string returns false.
     */
    @Test
    public void testIsValidContact_WithEmptyString_ReturnsFalse() {
        boolean result = ValidationUtil.isValidContact("");

        assertFalse(result, "isValidContact with empty string should return false");
    }

    /**
     * TDD failure test 11: isValidContact with too short number returns false.
     */
    @Test
    public void testIsValidContact_WithTooShortNumber_ReturnsFalse() {
        boolean result = ValidationUtil.isValidContact("12345");

        assertFalse(result, "isValidContact with too short number (less than 7 chars) should return false");
    }

    /**
     * TDD failure test 12: isValidContact with too long number returns false.
     */
    @Test
    public void testIsValidContact_WithTooLongNumber_ReturnsFalse() {
        boolean result = ValidationUtil.isValidContact("123456789012345678901");

        assertFalse(result, "isValidContact with too long number (more than 20 chars) should return false");
    }

    /**
     * TDD failure test 13: isValidContact with invalid characters returns false.
     */
    @Test
    public void testIsValidContact_WithInvalidCharacters_ReturnsFalse() {
        boolean result = ValidationUtil.isValidContact("abc1234567");

        assertFalse(result, "isValidContact with invalid characters (letters) should return false");
    }

    /**
     * TDD failure test 14: isValidContact with special invalid characters returns false.
     */
    @Test
    public void testIsValidContact_WithSpecialInvalidCharacters_ReturnsFalse() {
        boolean result = ValidationUtil.isValidContact("123@456#789");

        assertFalse(result, "isValidContact with special invalid characters (@, #) should return false");
    }

    /**
     * TDD pass test: requireNonBlank with valid value does not add error.
     */
    @Test
    public void testRequireNonBlank_WithValidValue_DoesNotAddError() {
        List<String> errors = new ArrayList<>();

        ValidationUtil.requireNonBlank("John Doe", "Guest Name", errors);

        assertTrue(errors.isEmpty(), "requireNonBlank with valid value should not add error");
    }

    /**
     * TDD pass test: parseDate with valid format returns LocalDate.
     */
    @Test
    public void testParseDate_WithValidFormat_ReturnsLocalDate() {
        List<String> errors = new ArrayList<>();

        LocalDate result = ValidationUtil.parseDate("2025-06-15", "Check In Date", errors);

        assertNotNull(result, "parseDate with valid format should return LocalDate");
        assertEquals(LocalDate.of(2025, 6, 15), result, "Parsed date should match");
        assertTrue(errors.isEmpty(), "parseDate with valid format should not add error");
    }

    /**
     * TDD pass test: isValidContact with valid number returns true.
     */
    @Test
    public void testIsValidContact_WithValidNumber_ReturnsTrue() {
        boolean result = ValidationUtil.isValidContact("0771234567");

        assertTrue(result, "isValidContact with valid number should return true");
    }

    /**
     * TDD pass test: isValidContact with valid international format returns true.
     */
    @Test
    public void testIsValidContact_WithValidInternationalFormat_ReturnsTrue() {
        boolean result = ValidationUtil.isValidContact("+94-77-123-4567");

        assertTrue(result, "isValidContact with valid international format should return true");
    }
}
