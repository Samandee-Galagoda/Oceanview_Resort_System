package repositorytest;

import com.oceanview.resort.model.Guest;
import com.oceanview.resort.repository.JdbcGuestRepository;
import com.oceanview.resort.util.DbConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * TDD-style tests for JdbcGuestRepository.
 * These tests now use correct expectations (GREEN phase).
 */
public class JdbcGuestRepositoryTest {

    private JdbcGuestRepository guestRepository;
    private DbConnectionManager dbConnectionManager;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setup() throws Exception {
        guestRepository = new JdbcGuestRepository();

        dbConnectionManager = mock(DbConnectionManager.class);
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(dbConnectionManager.getConnection()).thenReturn(connection);
        // For INSERT with generated keys
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        // For SELECT queries
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        injectDbConnectionManager();
    }

    private void injectDbConnectionManager() throws Exception {
        Field field = JdbcGuestRepository.class.getDeclaredField("dbManager");
        field.setAccessible(true);
        field.set(guestRepository, dbConnectionManager);
    }

    /**
     * TDD pass test 1: create should persist guest and return generated ID.
     */
    @Test
    public void testCreate_ShouldPersistGuestAndReturnId() throws Exception {
        Guest guest = new Guest();
        guest.setFullName("John Doe");
        guest.setAddress("123 Beach Rd");
        guest.setContactNumber("0771234567");
        guest.setCreatedAt(LocalDateTime.of(2025, 5, 1, 10, 0));

        // Simulate generated key = 7
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(7L);

        long id = guestRepository.create(guest);

        assertEquals(7L, id,
                "create should return the generated guest ID");
    }

    /**
     * TDD pass test 2: findById should return a Guest when a row exists.
     */
    @Test
    public void testFindById_WhenGuestExists_ReturnsGuest() throws Exception {
        long guestId = 5L;

        // Simulate result set with one guest row
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(guestId);
        when(resultSet.getString("full_name")).thenReturn("Jane Doe");
        when(resultSet.getString("address")).thenReturn("789 Sea Breeze");
        when(resultSet.getString("contact_number")).thenReturn("0700000000");
        when(resultSet.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(LocalDateTime.of(2025, 4, 1, 8, 0)));

        Guest guest = guestRepository.findById(guestId);

        assertNotNull(guest, "findById should return a Guest when a row exists");
        assertEquals(guestId, guest.getId(), "Guest ID should match the requested ID");
        assertEquals("Jane Doe", guest.getFullName(), "Guest full name should match database value");
        assertEquals("789 Sea Breeze", guest.getAddress(), "Guest address should match database value");
        assertEquals("0700000000", guest.getContactNumber(), "Guest contact number should match database value");
    }
}

