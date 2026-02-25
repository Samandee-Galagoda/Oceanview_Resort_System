package dtotest;

import com.oceanview.resort.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD GREEN tests for UserDTO.
 */
public class UserDtoTest {

    private UserDTO dto;

    @BeforeEach
    public void setup() {
        dto = new UserDTO();
    }

    @Test
    public void testIdAndUsername_SetAndGet() {
        dto.setId(5L);
        dto.setUsername("admin");

        assertEquals(5L, dto.getId(),
                "id should match setter");
        assertEquals("admin", dto.getUsername(),
                "username should match setter");
    }

    @Test
    public void testFullNameAndRole_SetAndGet() {
        dto.setFullName("System Admin");
        dto.setRole("ADMIN");

        assertEquals("System Admin", dto.getFullName(),
                "fullName should match setter");
        assertEquals("ADMIN", dto.getRole(),
                "role should match setter");
    }
}

