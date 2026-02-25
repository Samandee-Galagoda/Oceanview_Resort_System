package dtotest;

import com.oceanview.resort.dto.LoginRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD GREEN tests for LoginRequestDTO.
 */
public class LoginRequestDtoTest {

    private LoginRequestDTO dto;

    @BeforeEach
    public void setup() {
        dto = new LoginRequestDTO();
    }

    @Test
    public void testUsername_SetAndGet() {
        dto.setUsername("admin");

        String username = dto.getUsername();

        assertEquals("admin", username,
                "username should match the value set by setUsername");
    }

    @Test
    public void testPassword_SetAndGet() {
        dto.setPassword("secret");

        String password = dto.getPassword();

        assertEquals("secret", password,
                "password should match the value set by setPassword");
    }
}

