package com.oceanview.resort.configtest;

import com.oceanview.resort.config.DatabaseInitializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * TDD GREEN tests for DatabaseInitializer.
 * These rely on the real DbConnectionManager configuration.
 */
public class DatabaseInitializerTest {

    @Test
    public void testInitialize_DoesNotThrow() {
        DatabaseInitializer initializer = new DatabaseInitializer();

        assertDoesNotThrow(initializer::initialize,
                "initialize() should complete without throwing in a properly configured environment");
    }
}

