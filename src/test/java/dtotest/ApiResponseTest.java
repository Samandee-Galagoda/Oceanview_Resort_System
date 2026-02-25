package dtotest;

import com.oceanview.resort.dto.ApiResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TDD GREEN tests for ApiResponse.
 */
public class ApiResponseTest {

    @Test
    public void testSuccessAndMessage_Constructed() {
        ApiResponse<String> response = new ApiResponse<>(true, "OK", "DATA");

        assertTrue(response.isSuccess(), "success should be true");
        assertEquals("OK", response.getMessage(), "message should match constructor value");
    }

    @Test
    public void testData_Constructed() {
        ApiResponse<String> response = new ApiResponse<>(true, "OK", "DATA");

        assertNotNull(response.getData(), "data should not be null");
        assertEquals("DATA", response.getData(), "data should match constructor value");
    }
}

