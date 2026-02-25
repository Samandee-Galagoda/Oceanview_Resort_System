package dtotest;

import com.oceanview.resort.dto.GuestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD GREEN tests for GuestDTO.
 */
public class GuestDtoTest {

    private GuestDTO dto;

    @BeforeEach
    public void setup() {
        dto = new GuestDTO();
    }

    @Test
    public void testIdAndFullName_SetAndGet() {
        dto.setId(10L);
        dto.setFullName("Alice Smith");

        long id = dto.getId();
        String fullName = dto.getFullName();

        assertEquals(10L, id,
                "id should match the value set by setId");
        assertEquals("Alice Smith", fullName,
                "fullName should match the value set by setFullName");
    }

    @Test
    public void testAddressAndContact_SetAndGet() {
        dto.setAddress("456 Ocean View");
        dto.setContactNumber("0712345678");

        String address = dto.getAddress();
        String contact = dto.getContactNumber();

        assertEquals("456 Ocean View", address,
                "address should match the value set by setAddress");
        assertEquals("0712345678", contact,
                "contactNumber should match the value set by setContactNumber");
    }
}

