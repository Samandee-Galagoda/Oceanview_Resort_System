package daotest;

import com.oceanview.resort.model.ReservationDetail;
import com.oceanview.resort.repository.JdbcReservationDetailRepository;
import com.oceanview.resort.util.DbConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * TDD tests for ReservationDetailDao (JdbcReservationDetailRepository).
 * Covers failure scenarios: not found, no rows updated.
 */
public class ReservationDetailDaoTest {

    private JdbcReservationDetailRepository repository;
    private DbConnectionManager dbManager;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setup() throws Exception {
        repository = new JdbcReservationDetailRepository();
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
        Field field = JdbcReservationDetailRepository.class.getDeclaredField("dbManager");
        field.setAccessible(true);
        field.set(repository, dbManager);
    }

    /**
     * TDD failure test 1: findByReservationId when no row returns null.
     */
    @Test
    public void testFindByReservationId_WhenNotFound_ReturnsNull() throws Exception {
        when(resultSet.next()).thenReturn(false);

        ReservationDetail result = repository.findByReservationId(999L);

        assertNull(result, "findByReservationId when no row should return null");
    }

    /**
     * TDD failure test 2: deleteByReservationId when no row deleted returns false.
     */
    @Test
    public void testDeleteByReservationId_WhenNoRowDeleted_ReturnsFalse() throws Exception {
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean deleted = repository.deleteByReservationId(999L);

        assertFalse(deleted, "deleteByReservationId when no row updated should return false");
    }

    /**
     * TDD pass test: findByReservationId when detail exists returns detail.
     */
    @Test
    public void testFindByReservationId_WhenExists_ReturnsDetail() throws Exception {
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getLong("reservation_id")).thenReturn(10L);
        when(resultSet.getInt("adults")).thenReturn(2);
        when(resultSet.getInt("children")).thenReturn(1);
        when(resultSet.getString("special_requests")).thenReturn("Late checkout");
        when(resultSet.getString("notes")).thenReturn("Notes");

        ReservationDetail result = repository.findByReservationId(10L);

        assertNotNull(result, "findByReservationId when row exists should return ReservationDetail");
        assertEquals(10L, result.getReservationId());
        assertEquals(2, result.getAdults());
        assertEquals(1, result.getChildren());
    }
}
