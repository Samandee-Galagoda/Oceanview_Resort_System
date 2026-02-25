package utiltest;

import com.oceanview.resort.util.PasswordUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD tests for PasswordUtil.
 * Covers failure scenarios: null input, empty string, and verifies hashing behavior.
 */
public class PasswordUtilTest {

    /**
     * TDD failure test 1: hash with null input throws exception.
     */
    @Test
    public void testHash_WithNullInput_ThrowsException() {
        assertThrows(Exception.class, () -> {
            PasswordUtil.hash(null);
        }, "hash with null input should throw exception (NullPointerException or IllegalStateException)");
    }

    /**
     * TDD failure test 2: hash with empty string returns hash (not a failure, but edge case).
     * Note: Empty string is technically valid input, but we test it as an edge case.
     */
    @Test
    public void testHash_WithEmptyString_ReturnsHash() {
        String result = PasswordUtil.hash("");

        assertNotNull(result, "hash with empty string should return non-null hash");
        assertFalse(result.isEmpty(), "hash with empty string should return non-empty hash");
        // SHA-256 produces 64 character hex string
        assertEquals(64, result.length(), "SHA-256 hash should be 64 characters long");
    }

    /**
     * TDD pass test: hash with valid password returns consistent hash.
     */
    @Test
    public void testHash_WithValidPassword_ReturnsHash() {
        String password = "password123";

        String hash1 = PasswordUtil.hash(password);
        String hash2 = PasswordUtil.hash(password);

        assertNotNull(hash1, "hash should return non-null value");
        assertEquals(64, hash1.length(), "SHA-256 hash should be 64 characters long");
        assertEquals(hash1, hash2, "hash should return consistent result for same input");
    }

    /**
     * TDD pass test: hash with different passwords returns different hashes.
     */
    @Test
    public void testHash_WithDifferentPasswords_ReturnsDifferentHashes() {
        String hash1 = PasswordUtil.hash("password1");
        String hash2 = PasswordUtil.hash("password2");

        assertNotEquals(hash1, hash2, "hash should return different values for different inputs");
    }

    /**
     * TDD pass test: hash with special characters returns hash.
     */
    @Test
    public void testHash_WithSpecialCharacters_ReturnsHash() {
        String password = "p@ssw0rd!#$%";

        String hash = PasswordUtil.hash(password);

        assertNotNull(hash, "hash with special characters should return non-null hash");
        assertEquals(64, hash.length(), "SHA-256 hash should be 64 characters long");
    }

    /**
     * TDD pass test: hash with very long password returns hash.
     */
    @Test
    public void testHash_WithVeryLongPassword_ReturnsHash() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("a");
        }
        String longPassword = sb.toString();

        String hash = PasswordUtil.hash(longPassword);

        assertNotNull(hash, "hash with very long password should return non-null hash");
        assertEquals(64, hash.length(), "SHA-256 hash should be 64 characters long");
    }
}
