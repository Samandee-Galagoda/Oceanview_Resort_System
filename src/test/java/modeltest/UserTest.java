package modeltest;

import com.oceanview.resort.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD tests for User model.
 * Covers failure scenarios: null values, edge cases, and empty strings.
 */
public class UserTest {

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
    }

    /**
     * TDD failure test 1: getId returns default value 0 when not set.
     */
    @Test
    public void testGetId_WhenNotSet_ReturnsZero() {
        long id = user.getId();

        assertEquals(0L, id, "getId when not set should return 0");
    }

    /**
     * TDD failure test 2: setId with negative value sets negative value.
     */
    @Test
    public void testSetId_WithNegativeValue_SetsNegativeValue() {
        user.setId(-1L);

        assertEquals(-1L, user.getId(), "setId with negative value should set negative value");
    }

    /**
     * TDD failure test 3: getUsername returns null when not set.
     */
    @Test
    public void testGetUsername_WhenNotSet_ReturnsNull() {
        String username = user.getUsername();

        assertNull(username, "getUsername when not set should return null");
    }

    /**
     * TDD failure test 4: setUsername with null value sets null.
     */
    @Test
    public void testSetUsername_WithNull_SetsNull() {
        user.setUsername(null);

        assertNull(user.getUsername(), "setUsername with null should set null");
    }

    /**
     * TDD failure test 5: setUsername with empty string sets empty string.
     */
    @Test
    public void testSetUsername_WithEmptyString_SetsEmptyString() {
        user.setUsername("");

        assertEquals("", user.getUsername(), "setUsername with empty string should set empty string");
    }

    /**
     * TDD failure test 6: setUsername with blank string sets blank string.
     */
    @Test
    public void testSetUsername_WithBlankString_SetsBlankString() {
        user.setUsername("   ");

        assertEquals("   ", user.getUsername(), "setUsername with blank string should set blank string");
    }

    /**
     * TDD failure test 7: getFullName returns null when not set.
     */
    @Test
    public void testGetFullName_WhenNotSet_ReturnsNull() {
        String fullName = user.getFullName();

        assertNull(fullName, "getFullName when not set should return null");
    }

    /**
     * TDD failure test 8: setFullName with null value sets null.
     */
    @Test
    public void testSetFullName_WithNull_SetsNull() {
        user.setFullName(null);

        assertNull(user.getFullName(), "setFullName with null should set null");
    }

    /**
     * TDD failure test 9: setFullName with empty string sets empty string.
     */
    @Test
    public void testSetFullName_WithEmptyString_SetsEmptyString() {
        user.setFullName("");

        assertEquals("", user.getFullName(), "setFullName with empty string should set empty string");
    }

    /**
     * TDD failure test 10: getPassword returns null when not set.
     */
    @Test
    public void testGetPassword_WhenNotSet_ReturnsNull() {
        String password = user.getPassword();

        assertNull(password, "getPassword when not set should return null");
    }

    /**
     * TDD failure test 11: setPassword with null value sets null.
     */
    @Test
    public void testSetPassword_WithNull_SetsNull() {
        user.setPassword(null);

        assertNull(user.getPassword(), "setPassword with null should set null");
    }

    /**
     * TDD failure test 12: setPassword with empty string sets empty string.
     */
    @Test
    public void testSetPassword_WithEmptyString_SetsEmptyString() {
        user.setPassword("");

        assertEquals("", user.getPassword(), "setPassword with empty string should set empty string");
    }

    /**
     * TDD failure test 13: getPasswordHash returns null when not set.
     */
    @Test
    public void testGetPasswordHash_WhenNotSet_ReturnsNull() {
        String passwordHash = user.getPasswordHash();

        assertNull(passwordHash, "getPasswordHash when not set should return null");
    }

    /**
     * TDD failure test 14: setPasswordHash with null value sets null.
     */
    @Test
    public void testSetPasswordHash_WithNull_SetsNull() {
        user.setPasswordHash(null);

        assertNull(user.getPasswordHash(), "setPasswordHash with null should set null");
    }

    /**
     * TDD failure test 15: setPasswordHash with empty string sets empty string.
     */
    @Test
    public void testSetPasswordHash_WithEmptyString_SetsEmptyString() {
        user.setPasswordHash("");

        assertEquals("", user.getPasswordHash(), "setPasswordHash with empty string should set empty string");
    }

    /**
     * TDD failure test 16: getRole returns null when not set.
     */
    @Test
    public void testGetRole_WhenNotSet_ReturnsNull() {
        String role = user.getRole();

        assertNull(role, "getRole when not set should return null");
    }

    /**
     * TDD failure test 17: setRole with null value sets null.
     */
    @Test
    public void testSetRole_WithNull_SetsNull() {
        user.setRole(null);

        assertNull(user.getRole(), "setRole with null should set null");
    }

    /**
     * TDD failure test 18: setRole with empty string sets empty string.
     */
    @Test
    public void testSetRole_WithEmptyString_SetsEmptyString() {
        user.setRole("");

        assertEquals("", user.getRole(), "setRole with empty string should set empty string");
    }

    /**
     * TDD failure test 19: setRole with blank string sets blank string.
     */
    @Test
    public void testSetRole_WithBlankString_SetsBlankString() {
        user.setRole("   ");

        assertEquals("   ", user.getRole(), "setRole with blank string should set blank string");
    }

    /**
     * TDD pass test: getId and setId work correctly.
     */
    @Test
    public void testId_SetAndGet() {
        user.setId(1L);

        assertEquals(1L, user.getId(), "getId should return value set by setId");
    }

    /**
     * TDD pass test: getUsername and setUsername work correctly.
     */
    @Test
    public void testUsername_SetAndGet() {
        user.setUsername("admin");

        assertEquals("admin", user.getUsername(), "getUsername should return value set by setUsername");
    }

    /**
     * TDD pass test: getFullName and setFullName work correctly.
     */
    @Test
    public void testFullName_SetAndGet() {
        user.setFullName("John Doe");

        assertEquals("John Doe", user.getFullName(), "getFullName should return value set by setFullName");
    }

    /**
     * TDD pass test: getPassword and setPassword work correctly.
     */
    @Test
    public void testPassword_SetAndGet() {
        user.setPassword("password123");

        assertEquals("password123", user.getPassword(), "getPassword should return value set by setPassword");
    }

    /**
     * TDD pass test: getPasswordHash and setPasswordHash work correctly.
     */
    @Test
    public void testPasswordHash_SetAndGet() {
        user.setPasswordHash("abc123def456");

        assertEquals("abc123def456", user.getPasswordHash(), "getPasswordHash should return value set by setPasswordHash");
    }

    /**
     * TDD pass test: getRole and setRole work correctly.
     */
    @Test
    public void testRole_SetAndGet() {
        user.setRole("ADMIN");

        assertEquals("ADMIN", user.getRole(), "getRole should return value set by setRole");
    }
}
