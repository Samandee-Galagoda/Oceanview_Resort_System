package daotest;

import com.oceanview.resort.model.RoomType;
import com.oceanview.resort.repository.JdbcRoomTypeRepository;
import com.oceanview.resort.util.DbConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * TDD tests for RoomTypeDao (JdbcRoomTypeRepository).
 * Covers failure scenarios: not found, empty results, no rows updated.
 */
public class RoomTypeDaoTest {

    private JdbcRoomTypeRepository repository;
    private DbConnectionManager dbManager;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setup() throws Exception {
        repository = new JdbcRoomTypeRepository();
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
        Field field = JdbcRoomTypeRepository.class.getDeclaredField("dbManager");
        field.setAccessible(true);
        field.set(repository, dbManager);
    }

    /**
     * TDD failure test 1: findById when no row returns null.
     */
    @Test
    public void testFindById_WhenNotFound_ReturnsNull() throws Exception {
        when(resultSet.next()).thenReturn(false);

        RoomType result = repository.findById(999L);

        assertNull(result, "findById when no row should return null");
    }

    /**
     * TDD failure test 2: findByName when no row returns null.
     */
    @Test
    public void testFindByName_WhenNotFound_ReturnsNull() throws Exception {
        when(resultSet.next()).thenReturn(false);

        RoomType result = repository.findByName("NoSuchType");

        assertNull(result, "findByName when no row should return null");
    }

    /**
     * TDD failure test 3: findAll when no rows returns empty list.
     */
    @Test
    public void testFindAll_WhenNoRows_ReturnsEmptyList() throws Exception {
        when(resultSet.next()).thenReturn(false);

        java.util.List<RoomType> list = repository.findAll();

        assertNotNull(list, "findAll should never return null");
        assertTrue(list.isEmpty(), "findAll when no rows should return empty list");
    }

    /**
     * TDD failure test 4: deleteById when no row deleted returns false.
     */
    @Test
    public void testDeleteById_WhenNoRowDeleted_ReturnsFalse() throws Exception {
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean deleted = repository.deleteById(999L);

        assertFalse(deleted, "deleteById when no row updated should return false");
    }

    /**
     * TDD pass test: findById when room type exists returns room type.
     */
    @Test
    public void testFindById_WhenExists_ReturnsRoomType() throws Exception {
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("type_name")).thenReturn("Standard");
        when(resultSet.getBigDecimal("nightly_rate")).thenReturn(new BigDecimal("100.00"));
        when(resultSet.getInt("max_occupancy")).thenReturn(2);
        when(resultSet.getString("description")).thenReturn("Standard room");

        RoomType result = repository.findById(1L);

        assertNotNull(result, "findById when row exists should return RoomType");
        assertEquals(1L, result.getId());
        assertEquals("Standard", result.getTypeName());
    }
}
