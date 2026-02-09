package modeltest;

import com.oceanview.resort.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationTest {

    private Reservation reservation;

    @BeforeEach
    public void setup() {
        reservation = new Reservation();
    }

    /**
     * TDD pass test 1: reservationNumber and guestName getters should return values set by setters.
     */
    @Test
    public void testReservationNumberAndGuestName_SetAndGet() {
        reservation.setReservationNumber("RES001");
        reservation.setGuestName("John Doe");

        String reservationNumber = reservation.getReservationNumber();
        String guestName = reservation.getGuestName();

        assertEquals("RES001", reservationNumber,
                "reservationNumber should match the value set by setReservationNumber");
        assertEquals("John Doe", guestName,
                "guestName should match the value set by setGuestName");
    }

    /**
     * TDD pass test 2: dates and status getters should return values set by setters.
     */
    @Test
    public void testDatesAndStatus_SetAndGet() {
        LocalDate checkIn = LocalDate.of(2025, 5, 10);
        LocalDate checkOut = LocalDate.of(2025, 5, 12);
        LocalDateTime createdAt = LocalDateTime.of(2025, 5, 1, 9, 30);

        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);
        reservation.setCreatedAt(createdAt);
        reservation.setStatus("BOOKED");

        LocalDate actualCheckIn = reservation.getCheckInDate();
        LocalDate actualCheckOut = reservation.getCheckOutDate();
        LocalDateTime actualCreatedAt = reservation.getCreatedAt();
        String actualStatus = reservation.getStatus();

        assertEquals(checkIn, actualCheckIn,
                "checkInDate should match the value set by setCheckInDate");
        assertEquals(checkOut, actualCheckOut,
                "checkOutDate should match the value set by setCheckOutDate");
        assertEquals(createdAt, actualCreatedAt,
                "createdAt should match the value set by setCreatedAt");
        assertEquals("BOOKED", actualStatus,
                "status should match the value set by setStatus");
    }
}