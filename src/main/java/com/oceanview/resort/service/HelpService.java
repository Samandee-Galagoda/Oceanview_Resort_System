package com.oceanview.resort.service;

import java.util.Arrays;
import java.util.List;

public class HelpService {
    public List<String> getGuidelines() {
        return Arrays.asList(
                "Login with your staff username and password before managing reservations.",
                "Use 'Add Reservation' to record guest details and verify dates before saving.",
                "Search reservations by reservation number to review booking details.",
                "Generate bills from the reservation number after confirming the stay dates.",
                "Use the reports panel to monitor occupancy, upcoming checkouts, and revenue.",
                "Default admin login is admin / admin123 (change in the database after first login)."
        );
    }
}
