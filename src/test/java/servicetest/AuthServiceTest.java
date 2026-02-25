package servicetest;

import com.oceanview.resort.dao.UserDao;
import com.oceanview.resort.model.User;
import com.oceanview.resort.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * TDD pass tests for AuthService.
 */
public class AuthServiceTest {

    private AuthService authService;
    private UserDao userDao;

    @BeforeEach
    public void setup() {
        userDao = mock(UserDao.class);
        authService = new AuthService(userDao);
    }

    @Test
    public void testLogin_WithValidCredentials_ReturnsUser() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin123");

        when(userDao.findByUsername("admin")).thenReturn(user);

        User result = authService.login("admin", "admin123");

        assertNotNull(result, "Login with valid credentials should return user");
        assertEquals("admin", result.getUsername());
    }

    @Test
    public void testLogin_WithBlankUsername_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> authService.login("   ", "pass"),
                "Login with blank username should throw IllegalArgumentException");
    }

    @Test
    public void testLogin_WithWrongPassword_ReturnsNull() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin123");
        when(userDao.findByUsername("admin")).thenReturn(user);

        User result = authService.login("admin", "wrongpass");

        assertNull(result, "Login with wrong password should return null");
    }
}