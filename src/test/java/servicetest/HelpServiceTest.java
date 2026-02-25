package servicetest;

import com.oceanview.resort.service.HelpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD RED tests for HelpService.
 */
public class HelpServiceTest {

    private HelpService helpService;

    @BeforeEach
    public void setup() {
        helpService = new HelpService();
    }

    @Test
    public void testGetGuidelines_ReturnsNonEmptyList() {
        List<String> lines = helpService.getGuidelines();

        assertNotNull(lines, "getGuidelines should return non-null list");
        assertFalse(lines.isEmpty(), "getGuidelines should return non-empty list");
        assertTrue(lines.get(0).contains("Login") || lines.get(0).length() > 0,
                "First guideline should contain helpful content");
    }
}