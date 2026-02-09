package dtotest;

import com.oceanview.resort.dto.ReservationRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationRequestDtoTest {

    private ReservationRequestDTO dto;

    @BeforeEach
    public void setup() {
        dto = new ReservationRequestDTO();
    }

    /**
     * TDD pass test 1: reservationNumber getter should return the value set by the setter.
     */
    @Test
    public void testReservationNumber_SetAndGet() {
        dto.setReservationNumber("RES123");

        String reservationNumber = dto.getReservationNumber();

        assertEquals("RES123", reservationNumber,
                "reservationNumber should match the value set by setReservationNumber");
    }

    /**
     * TDD pass test 2: check-in and check-out date getters should return the values set by setters.
     */
    @Test
    public void testCheckInAndCheckOutDates_SetAndGet() {
        dto.setCheckInDate("2025-05-01");
        dto.setCheckOutDate("2025-05-05");

        String checkInDate = dto.getCheckInDate();
        String checkOutDate = dto.getCheckOutDate();

        assertEquals("2025-05-01", checkInDate,
                "checkInDate should match the value set by setCheckInDate");
        assertEquals("2025-05-05", checkOutDate,
                "checkOutDate should match the value set by setCheckOutDate");
    }
}