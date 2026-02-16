package dtotest;

import com.oceanview.resort.dto.ReservationResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * TDD GREEN tests for ReservationResponseDTO (builder-based DTO).
 */
public class ReservationResponseDtoTest {

    @Test
    public void testBuilder_SetsFields() {
        ReservationResponseDTO dto = ReservationResponseDTO.builder()
                .reservationNumber("RES001")
                .guestName("John Doe")
                .roomType("Standard")
                .checkInDate("2025-03-01")
                .checkOutDate("2025-03-03")
                .createdAt("2025-02-01T10:00:00")
                .build();

        assertEquals("RES001", dto.getReservationNumber());
        assertEquals("John Doe", dto.getGuestName());
        assertEquals("Standard", dto.getRoomType());
        assertEquals("2025-03-01", dto.getCheckInDate());
        assertEquals("2025-03-03", dto.getCheckOutDate());
        assertEquals("2025-02-01T10:00:00", dto.getCreatedAt());
    }

    @Test
    public void testCreatedAt_SetAndGet() {
        ReservationResponseDTO dto = ReservationResponseDTO.builder()
                .reservationNumber("RES002")
                .createdAt("2025-02-01T10:00:00")
                .build();

        assertNotNull(dto.getCreatedAt(), "createdAt should not be null when set via builder");
        assertEquals("2025-02-01T10:00:00", dto.getCreatedAt());
    }
}

