package mappertest;

import com.oceanview.resort.dto.GuestDTO;
import com.oceanview.resort.mapper.GuestMapper;
import com.oceanview.resort.model.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GuestMapperTest {

    private GuestMapper guestMapper;

    @BeforeEach
    public void setup() {
        guestMapper = new GuestMapper();
    }

    @Test
    public void testToDTO_MapsFieldsCorrectly() {
        Guest guest = new Guest();
        guest.setId(10L);
        guest.setFullName("Alice Smith");
        guest.setAddress("456 Ocean View");
        guest.setContactNumber("0712345678");

        GuestDTO dto = guestMapper.toDTO(guest);

        assertEquals(10L, dto.getId(),
                "ID should match the Guest model");
        assertEquals("Alice Smith", dto.getFullName(),
                "Full name should match the Guest model");
        assertEquals("456 Ocean View", dto.getAddress(),
                "Address should match the Guest model");
        assertEquals("0712345678", dto.getContactNumber(),
                "Contact number should match the Guest model");
    }

    @Test
    public void testToModel_NullDto_ReturnsNull() {
        GuestDTO dto = null;

        Guest guest = guestMapper.toModel(dto);

        assertNull(guest,
                "Guest should be null when DTO is null");
    }
}