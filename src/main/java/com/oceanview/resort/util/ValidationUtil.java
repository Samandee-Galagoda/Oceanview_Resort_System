package com.oceanview.resort.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ValidationUtil {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private ValidationUtil() {
    }

    public static void requireNonBlank(String value, String fieldName, List<String> errors) {
        if (value == null || value.trim().isEmpty()) {
            errors.add(fieldName + " is required");
        }
    }

    public static LocalDate parseDate(String dateValue, String fieldName, List<String> errors) {
        if (dateValue == null || dateValue.trim().isEmpty()) {
            errors.add(fieldName + " is required");
            return null;
        }
        try {
            return LocalDate.parse(dateValue, DATE_FORMAT);
        } catch (DateTimeParseException ex) {
            errors.add(fieldName + " must be in yyyy-MM-dd format");
            return null;
        }
    }

    public static boolean isValidContact(String contactNumber) {
        return contactNumber != null && contactNumber.matches("[0-9+\\- ]{7,20}");
    }
}
