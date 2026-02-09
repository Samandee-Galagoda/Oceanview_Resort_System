package mappertest;

import com.oceanview.resort.dto.ReservationRequestDTO;
import com.oceanview.resort.dto.ReservationResponseDTO;
import com.oceanview.resort.mapper.ReservationMapper;
import com.oceanview.resort.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationMapperTest {

    private ReservationMapper reservationMapper;

    @BeforeEach
    public void setup() {
        reservationMapper = new ReservationMapper();
    }

    @Test
    public void testToModel_MapsFieldsCorrectly() {
        ReservationRequestDTO dto = new ReservationRequestDTO();
        dto.setReservationNumber("RES001");
        dto.setGuestName("John Doe");
        dto.setAddress("123 Beach Rd");
        dto.setContactNumber("0771234567");
        dto.setRoomType("Standard");
        dto.setCheckInDate("2025-03-01");
        dto.setCheckOutDate("2025-03-03");

        Reservation reservation = reservationMapper.toModel(dto);

        assertEquals("RES001", reservation.getReservationNumber(),
                "Reservation number should match the value from the DTO");
    }

    @Test
    public void testToResponse_MapsFieldsCorrectly() {
        Reservation reservation = new Reservation();
        reservation.setReservationNumber("RES002");
        reservation.setGuestName("Alice Smith");
        reservation.setAddress("456 Ocean View");
        reservation.setContactNumber("0712345678");
        reservation.setRoomType("Deluxe");
        reservation.setCheckInDate(LocalDate.of(2025, 4, 10));
        reservation.setCheckOutDate(LocalDate.of(2025, 4, 12));
        reservation.setCreatedAt(LocalDateTime.of(2025, 4, 1, 10, 30));

        ReservationResponseDTO response = reservationMapper.toResponse(reservation);

        assertEquals("2025-04-10", response.getCheckInDate(),
                "Check-in date should be formatted as ISO_LOCAL_DATE from the Reservation model");
    }
}

