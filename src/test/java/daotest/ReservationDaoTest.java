package daotest;

import com.oceanview.resort.dao.ReservationDao;
import com.oceanview.resort.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReservationDaoTest {

    private ReservationDao reservationDao;

    @BeforeEach
    public void setup() {
        reservationDao = mock(ReservationDao.class);
    }

    /**
     * TDD pass test 1: findByReservationNumber for non-existent number returns null.
     */
    @Test
    public void testFindByReservationNumber_WhenNotFound_ReturnsNull() {
        when(reservationDao.findByReservationNumber("NONEXISTENT123")).thenReturn(null);

        Reservation result = reservationDao.findByReservationNumber("NONEXISTENT123");

        assertNull(result, "findByReservationNumber for non-existent number should return null");
    }

    /**
     * TDD pass test 2: existsByReservationNumber for non-existent number returns false.
     */
    @Test
    public void testExistsByReservationNumber_WhenNotExists_ReturnsFalse() {
        when(reservationDao.existsByReservationNumber("DOES_NOT_EXIST")).thenReturn(false);

        boolean exists = reservationDao.existsByReservationNumber("DOES_NOT_EXIST");

        assertFalse(exists, "existsByReservationNumber for non-existent number should return false");
    }
}
