package mappertest;

import com.oceanview.resort.dto.UserDTO;
import com.oceanview.resort.mapper.UserMapper;
import com.oceanview.resort.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    public void setup() {
        userMapper = new UserMapper();
    }

    @Test
    public void testToDTO_MapsFieldsCorrectly() {
        User user = new User();
        user.setId(5L);
        user.setUsername("admin");
        user.setFullName("System Admin");
        user.setRole("ADMIN");

        UserDTO dto = userMapper.toDTO(user);

        assertEquals(5L, dto.getId(),
                "ID should match the User model");
        assertEquals("admin", dto.getUsername(),
                "Username should match the User model");
        assertEquals("System Admin", dto.getFullName(),
                "Full name should match the User model");
        assertEquals("ADMIN", dto.getRole(),
                "Role should match the User model");
    }

    @Test
    public void testToModel_NullDto_ReturnsNull() {
        UserDTO dto = null;

        User user = userMapper.toModel(dto);

        assertNull(user,
                "User should be null when DTO is null");
    }
}