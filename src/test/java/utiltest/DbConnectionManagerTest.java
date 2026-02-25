package utiltest;

import com.oceanview.resort.util.DbConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * TDD-style tests for DbConnectionManager.
 * These tests now use correct expectations (GREEN phase).
 */
public class DbConnectionManagerTest {

    private DbConnectionManager dbManager;

    @BeforeEach
    public void setup() {
        dbManager = DbConnectionManager.getInstance();
    }

    /**
     * TDD pass test 1: getInstance() should return the same singleton instance each time.
     */
    @Test
    public void testGetInstance_ReturnsSingleton() {
        DbConnectionManager first = DbConnectionManager.getInstance();
        DbConnectionManager second = DbConnectionManager.getInstance();

        assertSame(first, second,
                "getInstance should return the same singleton instance each time");
    }

    /**
     * TDD pass test 2: getConnection() should return a valid, non-null Connection.
     */
    @Test
    public void testGetConnection_ReturnsValidConnection() throws SQLException {
        Connection connection = dbManager.getConnection();

        assertNotNull(connection,
                "getConnection should return a valid, non-null Connection");
    }
}