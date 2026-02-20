package dtotest;

import com.oceanview.resort.dto.RoomTypeResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD GREEN tests for RoomTypeResponseDTO.
 */
public class RoomTypeResponseDtoTest {

    private RoomTypeResponseDTO dto;

    @BeforeEach
    public void setup() {
        dto = new RoomTypeResponseDTO();
    }

    @Test
    public void testIdAndTypeName_SetAndGet() {
        dto.setId(7L);
        dto.setTypeName("Suite");

        assertEquals(7L, dto.getId(),
                "id should match setter");
        assertEquals("Suite", dto.getTypeName(),
                "typeName should match setter");
    }

    @Test
    public void testNightlyRateMaxOccupancyAndDescription_SetAndGet() {
        dto.setNightlyRate(new BigDecimal("500.00"));
        dto.setMaxOccupancy(4);
        dto.setDescription("Luxury suite");

        assertEquals(new BigDecimal("500.00"), dto.getNightlyRate(),
                "nightlyRate should match setter");
        assertEquals(4, dto.getMaxOccupancy(),
                "maxOccupancy should match setter");
        assertEquals("Luxury suite", dto.getDescription(),
                "description should match setter");
    }
}

