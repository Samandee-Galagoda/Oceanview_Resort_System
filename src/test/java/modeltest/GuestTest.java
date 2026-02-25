package modeltest;

import com.oceanview.resort.model.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GuestTest {

    private Guest guest;

    @BeforeEach
    public void setup() {
        guest = new Guest();
    }

    /**
     * TDD pass test 1: id and fullName getters should return values set by setters.
     */
    @Test
    public void testIdAndFullName_SetAndGet() {
        guest.setId(10L);
        guest.setFullName("Alice Smith");

        long id = guest.getId();
        String fullName = guest.getFullName();

        assertEquals(10L, id,
                "id should match the value set by setId");
        assertEquals("Alice Smith", fullName,
                "fullName should match the value set by setFullName");
    }

    /**
     * TDD pass test 2: address, contactNumber, and createdAt getters should return values set by setters.
     */
    @Test
    public void testAddressContactAndCreatedAt_SetAndGet() {
        guest.setAddress("456 Ocean View");
        guest.setContactNumber("0712345678");
        LocalDateTime createdAt = LocalDateTime.of(2025, 5, 1, 9, 30);
        guest.setCreatedAt(createdAt);

        String address = guest.getAddress();
        String contactNumber = guest.getContactNumber();
        LocalDateTime actualCreatedAt = guest.getCreatedAt();

        assertEquals("456 Ocean View", address,
                "address should match the value set by setAddress");
        assertEquals("0712345678", contactNumber,
                "contactNumber should match the value set by setContactNumber");
        assertEquals(createdAt, actualCreatedAt,
                "createdAt should match the value set by setCreatedAt");
    }
}