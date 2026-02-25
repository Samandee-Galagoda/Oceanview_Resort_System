package daotest;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * TDD-style tests for JdbcGuestRepository (GuestDao implementation).
 * These tests now use correct expectations (GREEN phase).
 */
public class GuestDaoTest {

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
     * TDD pass test 1: create should return the generated guest ID.
     */
    @Test
    public void testCreate_ShouldReturnGeneratedId() throws Exception {
        Guest guest = new Guest();
        guest.setFullName("John Doe");
        guest.setAddress("123 Beach Rd");
        guest.setContactNumber("0771234567");

        // Simulate generated key = 42
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(42L);

        long id = guestRepository.create(guest);

        assertEquals(42L, id,
                "create should return the generated guest ID");
    }

    /**
     * TDD pass test 2: findById should return a Guest when a row exists.
     */
    @Test
    public void testFindById_WhenGuestExists() throws Exception {
        long guestId = 10L;

        // Simulate result set with one guest row
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(guestId);
        when(resultSet.getString("full_name")).thenReturn("Alice Smith");
        when(resultSet.getString("address")).thenReturn("456 Ocean View");
        when(resultSet.getString("contact_number")).thenReturn("0712345678");

        Guest guest = guestRepository.findById(guestId);

        assertNotNull(guest, "findById should return a Guest when a row exists");
        assertEquals(guestId, guest.getId(), "Guest ID should match the requested ID");
        assertEquals("Alice Smith", guest.getFullName(), "Guest full name should match database value");
        assertEquals("456 Ocean View", guest.getAddress(), "Guest address should match database value");
        assertEquals("0712345678", guest.getContactNumber(), "Guest contact number should match database value");
    }

    /**
     * TDD failure test 1: findById when no row exists returns null.
     */
    @Test
    public void testFindById_WhenGuestNotFound_ReturnsNull() throws Exception {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Guest guest = guestRepository.findById(999L);

        assertNull(guest, "findById when no row should return null");
    }

    /**
     * TDD failure test 2: create when no generated key returns 0.
     */
    @Test
    public void testCreate_WhenNoGeneratedKey_ReturnsZero() throws Exception {
        Guest guest = new Guest();
        guest.setFullName("John Doe");
        guest.setAddress("123 Beach Rd");
        guest.setContactNumber("0771234567");

        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        long id = guestRepository.create(guest);

        assertEquals(0L, id, "create when no generated key should return 0");
    }
}

