package servicetest;

import com.oceanview.resort.dao.GuestDao;
import com.oceanview.resort.dao.ReservationDao;
import com.oceanview.resort.dao.RoomRateDao;
import com.oceanview.resort.dto.ReservationResponseDTO;
import com.oceanview.resort.mapper.ReservationMapper;
import com.oceanview.resort.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationDao reservationDao;
    private GuestDao guestDao;
    private RoomRateDao roomRateDao;
    private ReservationMapper reservationMapper;

    @BeforeEach
    public void setup() {
        reservationDao = mock(ReservationDao.class);
        guestDao = mock(GuestDao.class);
        roomRateDao = mock(RoomRateDao.class);
        reservationMapper = mock(ReservationMapper.class);
        reservationService = new ReservationService(reservationDao, guestDao, roomRateDao, reservationMapper);
    }

    /**
     * TDD pass test 1: getReservation for non-existent number returns null.
     */
    @Test
    public void testGetReservation_WhenNotFound_ReturnsNull() {
        when(reservationDao.findByReservationNumber("NONEXISTENT123")).thenReturn(null);

        ReservationResponseDTO result = reservationService.getReservation("NONEXISTENT123");

        assertNull(result, "getReservation for non-existent number should return null");
    }

    /**
     * TDD pass test 2: getReservation with blank reservation number throws IllegalArgumentException.
     */
    @Test
    public void testGetReservation_WhenReservationNumberBlank_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> reservationService.getReservation("   "),
                "getReservation with blank reservation number should throw IllegalArgumentException");
    }
}
