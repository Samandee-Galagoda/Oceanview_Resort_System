package utiltest;

import com.oceanview.resort.dao.BillingDao;
import com.oceanview.resort.dao.GuestDao;
import com.oceanview.resort.dao.ReservationDao;
import com.oceanview.resort.dao.ReservationDetailDao;
import com.oceanview.resort.dao.RoomTypeDao;
import com.oceanview.resort.dao.UserDao;
import com.oceanview.resort.util.DaoFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * TDD-style tests for DaoFactory.
 * These tests now use correct expectations (GREEN phase).
 */
public class DaoFactoryTest {

    private DaoFactory daoFactory;

    @BeforeEach
    public void setup() {
        daoFactory = DaoFactory.getInstance();
    }

    /**
     * TDD pass test 1: getInstance() should return the same singleton instance each time.
     */
    @Test
    public void testGetInstance_ReturnsSingleton() {
        DaoFactory first = DaoFactory.getInstance();
        DaoFactory second = DaoFactory.getInstance();

        assertSame(first, second,
                "getInstance should return the same singleton instance each time");
    }

    /**
     * TDD pass test 2: getReservationDao() should return a non-null ReservationDao.
     */
    @Test
    public void testGetReservationDao_ReturnsNonNull() {
        ReservationDao dao = daoFactory.getReservationDao();

        assertNotNull(dao,
                "getReservationDao should return a non-null ReservationDao");
    }

    /**
     * TDD pass test 3: getUserDao() should return a non-null UserDao.
     */
    @Test
    public void testGetUserDao_ReturnsNonNull() {
        UserDao dao = daoFactory.getUserDao();

        assertNotNull(dao,
                "getUserDao should return a non-null UserDao");
    }

    /**
     * TDD pass test 4: getGuestDao() should return a non-null GuestDao.
     */
    @Test
    public void testGetGuestDao_ReturnsNonNull() {
        GuestDao dao = daoFactory.getGuestDao();

        assertNotNull(dao,
                "getGuestDao should return a non-null GuestDao");
    }

    /**
     * TDD pass test 5: getRoomTypeDao() should return a non-null RoomTypeDao.
     */
    @Test
    public void testGetRoomTypeDao_ReturnsNonNull() {
        RoomTypeDao dao = daoFactory.getRoomTypeDao();

        assertNotNull(dao,
                "getRoomTypeDao should return a non-null RoomTypeDao");
    }

    /**
     * TDD pass test 6: getReservationDetailDao() should return a non-null ReservationDetailDao.
     */
    @Test
    public void testGetReservationDetailDao_ReturnsNonNull() {
        ReservationDetailDao dao = daoFactory.getReservationDetailDao();

        assertNotNull(dao,
                "getReservationDetailDao should return a non-null ReservationDetailDao");
    }

    /**
     * TDD pass test 7: getBillingDao() should return a non-null BillingDao.
     */
    @Test
    public void testGetBillingDao_ReturnsNonNull() {
        BillingDao dao = daoFactory.getBillingDao();

        assertNotNull(dao,
                "getBillingDao should return a non-null BillingDao");
    }
}
