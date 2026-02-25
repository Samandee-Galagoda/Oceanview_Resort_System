package daotest;

import com.oceanview.resort.model.User;
import com.oceanview.resort.repository.JdbcUserRepository;
import com.oceanview.resort.util.DbConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * TDD tests for UserDao (JdbcUserRepository).
 * Covers failure scenarios: not found, empty results, no rows updated.
 */
public class UserDaoTest {

    private JdbcUserRepository repository;
    private DbConnectionManager dbManager;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setup() throws Exception {
        repository = new JdbcUserRepository();
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
        Field field = JdbcUserRepository.class.getDeclaredField("dbManager");
        field.setAccessible(true);
        field.set(repository, dbManager);
    }

    /**
     * TDD failure test 1: findByUsername when no row returns null.
     */
    @Test
    public void testFindByUsername_WhenNotFound_ReturnsNull() throws Exception {
        when(resultSet.next()).thenReturn(false);

        User result = repository.findByUsername("nonexistent");

        assertNull(result, "findByUsername when no row should return null");
    }

    /**
     * TDD failure test 2: findById when no row returns null.
     */
    @Test
    public void testFindById_WhenNotFound_ReturnsNull() throws Exception {
        when(resultSet.next()).thenReturn(false);

        User result = repository.findById(999L);

        assertNull(result, "findById when no row should return null");
    }

    /**
     * TDD failure test 3: existsAny when no rows returns false.
     */
    @Test
    public void testExistsAny_WhenNoUsers_ReturnsFalse() throws Exception {
        when(resultSet.next()).thenReturn(false);

        boolean exists = repository.existsAny();

        assertFalse(exists, "existsAny when no rows should return false");
    }

    /**
     * TDD failure test 4: findAll when no rows returns empty list.
     */
    @Test
    public void testFindAll_WhenNoRows_ReturnsEmptyList() throws Exception {
        when(resultSet.next()).thenReturn(false);

        java.util.List<User> list = repository.findAll();

        assertNotNull(list, "findAll should never return null");
        assertTrue(list.isEmpty(), "findAll when no rows should return empty list");
    }

    /**
     * TDD failure test 5: deleteById when no row deleted returns false.
     */
    @Test
    public void testDeleteById_WhenNoRowDeleted_ReturnsFalse() throws Exception {
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean deleted = repository.deleteById(999L);

        assertFalse(deleted, "deleteById when no row updated should return false");
    }

    /**
     * TDD pass test: findByUsername when user exists returns user.
     */
    @Test
    public void testFindByUsername_WhenUserExists_ReturnsUser() throws Exception {
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("username")).thenReturn("admin");
        when(resultSet.getString("password")).thenReturn("hash");
        when(resultSet.getString("role")).thenReturn("ADMIN");
        ResultSetMetaData meta = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(4);
        when(meta.getColumnLabel(1)).thenReturn("id");
        when(meta.getColumnLabel(2)).thenReturn("username");
        when(meta.getColumnLabel(3)).thenReturn("password");
        when(meta.getColumnLabel(4)).thenReturn("role");

        User user = repository.findByUsername("admin");

        assertNotNull(user, "findByUsername when row exists should return user");
        assertEquals("admin", user.getUsername());
        assertEquals("ADMIN", user.getRole());
    }
}
