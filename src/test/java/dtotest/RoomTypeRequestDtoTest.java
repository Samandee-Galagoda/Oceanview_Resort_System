package dtotest;

import com.oceanview.resort.dto.RoomTypeRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD GREEN tests for RoomTypeRequestDTO.
 */
public class RoomTypeRequestDtoTest {

    private RoomTypeRequestDTO dto;

    @BeforeEach
    public void setup() {
        dto = new RoomTypeRequestDTO();
    }

    @Test
    public void testTypeNameAndNightlyRate_SetAndGet() {
        dto.setTypeName("Deluxe");
        dto.setNightlyRate(new BigDecimal("250.00"));

        assertEquals("Deluxe", dto.getTypeName(),
                "typeName should match setter");
        assertEquals(new BigDecimal("250.00"), dto.getNightlyRate(),
                "nightlyRate should match setter");
    }

    @Test
    public void testMaxOccupancyAndDescription_SetAndGet() {
        dto.setMaxOccupancy(3);
        dto.setDescription("Sea view");

        assertEquals(3, dto.getMaxOccupancy(),
                "maxOccupancy should match setter");
        assertEquals("Sea view", dto.getDescription(),
                "description should match setter");
    }
}

