package servicetest;

import com.oceanview.resort.dao.GuestDao;
import com.oceanview.resort.dao.ReservationDao;
import com.oceanview.resort.dao.RoomTypeDao;
import com.oceanview.resort.dto.ReservationRequestDTO;
import com.oceanview.resort.dto.ReservationResponseDTO;
import com.oceanview.resort.mapper.ReservationMapper;
import com.oceanview.resort.model.Reservation;
import com.oceanview.resort.model.RoomType;
import com.oceanview.resort.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * TDD tests for ReservationService covering all methods.
 */
public class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationDao reservationDao;
    private RoomTypeDao roomTypeDao;

    @BeforeEach
    public void setup() {
        reservationDao = mock(ReservationDao.class);
        GuestDao guestDao = mock(GuestDao.class);
        roomTypeDao = mock(RoomTypeDao.class);
        ReservationMapper reservationMapper = new ReservationMapper();
        reservationService = new ReservationService(reservationDao, guestDao, roomTypeDao, reservationMapper);
    }

    @Test
    public void testGetReservation_WhenNotFound_ReturnsNull() {
        when(reservationDao.findByReservationNumber("NONEXISTENT123")).thenReturn(null);

        ReservationResponseDTO result = reservationService.getReservation("NONEXISTENT123");

        assertNull(result, "getReservation for non-existent number should return null");
    }

    @Test
    public void testGetReservation_WhenReservationNumberBlank_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> reservationService.getReservation("   "),
                "getReservation with blank reservation number should throw IllegalArgumentException");
    }

    @Test
    public void testGetReservation_Existing_ReturnsDto() {
        Reservation reservation = new Reservation();
        reservation.setReservationNumber("RES100");
        reservation.setGuestName("Jane");
        reservation.setAddress("Ocean Rd");
        reservation.setContactNumber("0712345678");
        reservation.setRoomType("Standard");
        reservation.setCheckInDate(LocalDate.of(2025, 5, 1));
        reservation.setCheckOutDate(LocalDate.of(2025, 5, 3));

        when(reservationDao.findByReservationNumber("RES100")).thenReturn(reservation);

        ReservationResponseDTO dto = reservationService.getReservation("RES100");

        assertNotNull(dto, "Existing reservation should return non-null DTO");
        assertEquals("RES100", dto.getReservationNumber());
        assertEquals("Jane", dto.getGuestName());
    }

    @Test
    public void testCancelReservation_Existing_Succeeds() {
        when(reservationDao.cancelByReservationNumber("RES200")).thenReturn(true);

        assertDoesNotThrow(() -> reservationService.cancelReservation("RES200"),
                "cancelReservation for existing reservation should not throw");
    }

    @Test
    public void testListReservations_ReturnsMappedList() {
        Reservation r = new Reservation();
        r.setReservationNumber("RES300");
        r.setGuestName("Guest");
        r.setAddress("Somewhere");
        r.setContactNumber("0700000000");
        r.setRoomType("Standard");
        r.setCheckInDate(LocalDate.of(2025, 7, 1));
        r.setCheckOutDate(LocalDate.of(2025, 7, 2));

        when(reservationDao.findAll()).thenReturn(Collections.singletonList(r));

        List<ReservationResponseDTO> results = reservationService.listReservations();

        assertEquals(1, results.size(), "listReservations should return one item");
        assertEquals("RES300", results.get(0).getReservationNumber());
    }

    @Test
    public void testSearchReservationsByGuestName_ReturnsMatchingReservations() {
        Reservation r = new Reservation();
        r.setReservationNumber("RES400");
        r.setGuestName("Alice");
        r.setAddress("Lane");
        r.setContactNumber("0711111111");
        r.setRoomType("Deluxe");
        r.setCheckInDate(LocalDate.of(2025, 8, 1));
        r.setCheckOutDate(LocalDate.of(2025, 8, 3));

        when(reservationDao.findByGuestName("Alice")).thenReturn(Collections.singletonList(r));

        List<ReservationResponseDTO> results = reservationService.searchReservationsByGuestName("Alice");

        assertEquals(1, results.size(), "searchReservationsByGuestName should return one item");
        assertEquals("RES400", results.get(0).getReservationNumber());
        assertEquals("Alice", results.get(0).getGuestName());
    }

    @Test
    public void testSearchReservationsByGuestName_BlankName_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> reservationService.searchReservationsByGuestName("   "),
                "searchReservationsByGuestName with blank name should throw");
    }
}