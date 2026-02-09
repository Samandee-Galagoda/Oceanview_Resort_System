package repositorytest;

import com.oceanview.resort.repository.JdbcReservationRepository;
import com.oceanview.resort.util.DbConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * TDD-style tests for JdbcReservationRepository.
 * These tests now use the correct expectations (GREEN phase).
 */
public class JdbcReservationRepositoryTest {

    private JdbcReservationRepository repository;
    private DbConnectionManager dbConnectionManager;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setup() throws Exception {
        repository = new JdbcReservationRepository();

        // Create mocks for the JDBC infrastructure
        dbConnectionManager = mock(DbConnectionManager.class);
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        // Wire up basic JDBC interactions
        when(dbConnectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Inject the mock DbConnectionManager into the repository via reflection
        injectDbConnectionManager();
    }

    private void injectDbConnectionManager() throws Exception {
        Field field = JdbcReservationRepository.class.getDeclaredField("dbManager");
        field.setAccessible(true);
        field.set(repository, dbConnectionManager);
    }

    /**
     * TDD pass test 1: existsByReservationNumber should return true when a row is found.
     */
    @Test
    public void testExistsByReservationNumber_WhenRecordExists() throws Exception {
        // Arrange
        String reservationNumber = "RES123";
        // Simulate that the query finds at least one row
        when(resultSet.next()).thenReturn(true);

        // Act
        boolean exists = repository.existsByReservationNumber(reservationNumber);

        // Assert - now expecting true
        assertEquals(true, exists,
                "existsByReservationNumber should return true when reservation exists");
    }

    /**
     * TDD pass test 2: countActiveOn should return the number of active reservations.
     */
    @Test
    public void testCountActiveOn_WhenActiveReservationsExist() throws Exception {
        // Arrange
        LocalDate date = LocalDate.of(2025, 5, 1);
        // Simulate that the COUNT(*) query returns 5
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(5);

        // Act
        int count = repository.countActiveOn(date);

        // Assert - now expecting the correct count (5)
        assertEquals(5, count,
                "countActiveOn should return the number of active reservations on the given date");
    }
}
