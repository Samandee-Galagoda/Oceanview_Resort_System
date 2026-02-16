package daotest;

import com.oceanview.resort.model.Reservation;
import com.oceanview.resort.repository.JdbcReservationRepository;
import com.oceanview.resort.util.DbConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * TDD tests for ReservationDao (JdbcReservationRepository).
 * Covers failure scenarios: not found, empty results, no rows updated.
 */
public class ReservationDaoTest {

    private JdbcReservationRepository repository;
    private DbConnectionManager dbManager;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setup() throws Exception {
        repository = new JdbcReservationRepository();
        dbManager = mock(DbConnectionManager.class);
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(dbManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        injectDbManager();
    }

    private void injectDbManager() throws Exception {
        Field field = JdbcReservationRepository.class.getDeclaredField("dbManager");
        field.setAccessible(true);
        field.set(repository, dbManager);
    }

    /**
     * TDD failure test 1: findByReservationNumber when no row returns null.
     */
    @Test
    public void testFindByReservationNumber_WhenNotFound_ReturnsNull() throws Exception {
        when(resultSet.next()).thenReturn(false);

        Reservation result = repository.findByReservationNumber("NONEXISTENT123");

        assertNull(result, "findByReservationNumber for non-existent number should return null");
    }

    /**
     * TDD failure test 2: existsByReservationNumber when no row returns false.
     */
    @Test
    public void testExistsByReservationNumber_WhenNotExists_ReturnsFalse() throws Exception {
        when(resultSet.next()).thenReturn(false);

        boolean exists = repository.existsByReservationNumber("DOES_NOT_EXIST");

        assertFalse(exists, "existsByReservationNumber for non-existent number should return false");
    }

    /**
     * TDD failure test 3: cancelByReservationNumber when no row updated returns false.
     */
    @Test
    public void testCancelByReservationNumber_WhenNoRowUpdated_ReturnsFalse() throws Exception {
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean cancelled = repository.cancelByReservationNumber("MISSING_RES");

        assertFalse(cancelled, "cancelByReservationNumber when no row updated should return false");
    }

    /**
     * TDD failure test 4: findAll when no rows returns empty list.
     */
    @Test
    public void testFindAll_WhenNoRows_ReturnsEmptyList() throws Exception {
        when(resultSet.next()).thenReturn(false);

        java.util.List<Reservation> list = repository.findAll();

        assertNotNull(list, "findAll should never return null");
        assertTrue(list.isEmpty(), "findAll when no rows should return empty list");
    }

    /**
     * TDD failure test 5: findByGuestName when no matches returns empty list.
     */
    @Test
    public void testFindByGuestName_WhenNoMatches_ReturnsEmptyList() throws Exception {
        when(resultSet.next()).thenReturn(false);

        java.util.List<Reservation> list = repository.findByGuestName("NoSuchGuest");

        assertNotNull(list, "findByGuestName should never return null");
        assertTrue(list.isEmpty(), "findByGuestName when no matches should return empty list");
    }

    /**
     * TDD failure test 6: countActiveOn when no rows returns 0.
     */
    @Test
    public void testCountActiveOn_WhenNoRows_ReturnsZero() throws Exception {
        when(resultSet.next()).thenReturn(false);

        int count = repository.countActiveOn(LocalDate.of(2025, 6, 1));

        assertEquals(0, count, "countActiveOn when no rows should return 0");
    }

    /**
     * TDD failure test 7: countRoomTypeReservationsCreatedBetween when no rows returns empty map.
     */
    @Test
    public void testCountRoomTypeReservationsCreatedBetween_WhenNoRows_ReturnsEmptyMap() throws Exception {
        when(resultSet.next()).thenReturn(false);

        java.util.Map<String, Integer> map = repository.countRoomTypeReservationsCreatedBetween(
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 8));

        assertNotNull(map, "countRoomTypeReservationsCreatedBetween should never return null");
        assertTrue(map.isEmpty(), "when no rows should return empty map");
    }

    /**
     * TDD failure test 8: countCheckoutsBetween when no rows returns 0.
     */
    @Test
    public void testCountCheckoutsBetween_WhenNoRows_ReturnsZero() throws Exception {
        when(resultSet.next()).thenReturn(false);

        int count = repository.countCheckoutsBetween(
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));

        assertEquals(0, count, "countCheckoutsBetween when no rows should return 0");
    }

    /**
     * TDD failure test 9: findMostPopularRoomType when no rows returns N/A.
     */
    @Test
    public void testFindMostPopularRoomType_WhenNoRows_ReturnsNA() throws Exception {
        when(resultSet.next()).thenReturn(false);

        String result = repository.findMostPopularRoomType();

        assertEquals("N/A", result, "findMostPopularRoomType when no rows should return N/A");
    }

    /**
     * TDD failure test 10: sumEstimatedRevenue when no rows returns 0.0.
     */
    @Test
    public void testSumEstimatedRevenue_WhenNoRows_ReturnsZero() throws Exception {
        when(resultSet.next()).thenReturn(false);

        double sum = repository.sumEstimatedRevenue();

        assertEquals(0.0, sum, 0.001, "sumEstimatedRevenue when no rows should return 0.0");
    }

    /**
     * TDD failure test 11: existsOverlappingReservation when no overlap returns false.
     */
    @Test
    public void testExistsOverlappingReservation_WhenNoOverlap_ReturnsFalse() throws Exception {
        when(resultSet.next()).thenReturn(false);

        boolean exists = repository.existsOverlappingReservation(
                1L, LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 3));

        assertFalse(exists, "existsOverlappingReservation when no overlap should return false");
    }

    /**
     * TDD pass test: existsByReservationNumber when row exists returns true.
     */
    @Test
    public void testExistsByReservationNumber_WhenRecordExists_ReturnsTrue() throws Exception {
        when(resultSet.next()).thenReturn(true);

        boolean exists = repository.existsByReservationNumber("RES123");

        assertTrue(exists, "existsByReservationNumber when reservation exists should return true");
    }
}
