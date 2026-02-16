package daotest;

import com.oceanview.resort.model.Billing;
import com.oceanview.resort.repository.JdbcBillingRepository;
import com.oceanview.resort.util.DbConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * TDD tests for BillingDao (JdbcBillingRepository).
 * Covers failure scenarios: not found.
 */
public class BillingDaoTest {

    private JdbcBillingRepository repository;
    private DbConnectionManager dbManager;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setup() throws Exception {
        repository = new JdbcBillingRepository();
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
        Field field = JdbcBillingRepository.class.getDeclaredField("dbManager");
        field.setAccessible(true);
        field.set(repository, dbManager);
    }

    /**
     * TDD failure test 1: findByInvoiceNumber when no row returns null.
     */
    @Test
    public void testFindByInvoiceNumber_WhenNotFound_ReturnsNull() throws Exception {
        when(resultSet.next()).thenReturn(false);

        Billing result = repository.findByInvoiceNumber("INV-NONEXISTENT");

        assertNull(result, "findByInvoiceNumber when no row should return null");
    }

    /**
     * TDD failure test 2: findByReservationId when no row returns null.
     */
    @Test
    public void testFindByReservationId_WhenNotFound_ReturnsNull() throws Exception {
        when(resultSet.next()).thenReturn(false);

        Billing result = repository.findByReservationId(999L);

        assertNull(result, "findByReservationId when no row should return null");
    }
}
