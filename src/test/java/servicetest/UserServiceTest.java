package servicetest;

import com.oceanview.resort.dao.UserDao;
import com.oceanview.resort.model.User;
import com.oceanview.resort.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * TDD pass tests for UserService.
 */
public class UserServiceTest {

    private UserService userService;
    private UserDao userDao;

    @BeforeEach
    public void setup() {
        userDao = mock(UserDao.class);
        userService = new UserService(userDao);
    }

    @Test
    public void testListUsers_ReturnsAllUsers() {
        when(userDao.findAll()).thenReturn(
                Arrays.asList(new User(), new User())
        );

        List<User> users = userService.listUsers();

        assertEquals(2, users.size(), "listUsers should return all users from DAO");
    }

    @Test
    public void testFindByUsername_ExistingUser_ReturnsUser() {
        User u = new User();
        u.setUsername("admin");
        when(userDao.findByUsername("admin")).thenReturn(u);

        User result = userService.findByUsername("admin");

        assertNotNull(result, "Existing user should be returned");
        assertEquals("admin", result.getUsername());
    }

    @Test
    public void testFindByUsername_BlankUsername_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.findByUsername("   "),
                "findByUsername with blank username should throw");
    }

    @Test
    public void testCreateUser_ValidData_Succeeds() {
        when(userDao.findByUsername("reception1")).thenReturn(null);
        when(userDao.findAll()).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() ->
                userService.createUser("Reception One", "reception1", "pass123", "STAFF"),
                "createUser with valid data should not throw");
    }

    @Test
    public void testUpdateUser_ExistingUser_Succeeds() {
        User existing = new User();
        existing.setId(5L);
        existing.setUsername("reception1");
        existing.setFullName("Reception One");
        existing.setRole("RECEPTIONIST");

        when(userDao.findById(5L)).thenReturn(existing);
        when(userDao.findAll()).thenReturn(Collections.singletonList(existing));
        when(userDao.findByUsername("reception1")).thenReturn(existing);

        assertDoesNotThrow(() ->
                        userService.updateUser(5L, "Reception Updated",
                                "reception1", "newpass", "STAFF"),
                "updateUser for existing user should not throw");
    }

    @Test
    public void testDeleteUser_Receptionist_Succeeds() {
        User existing = new User();
        existing.setId(10L);
        existing.setRole("RECEPTIONIST");

        when(userDao.findById(10L)).thenReturn(existing);
        when(userDao.deleteById(10L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(10L),
                "deleteUser for receptionist should not throw");
    }
}