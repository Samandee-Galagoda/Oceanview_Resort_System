package mappertest;

import com.oceanview.resort.dto.RoomTypeRequestDTO;
import com.oceanview.resort.dto.RoomTypeResponseDTO;
import com.oceanview.resort.mapper.RoomTypeMapper;
import com.oceanview.resort.model.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RoomTypeMapperTest {

    private RoomTypeMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new RoomTypeMapper();
    }

    @Test
    public void testToModel_MapsFieldsCorrectly() {
        RoomTypeRequestDTO dto = new RoomTypeRequestDTO();
        dto.setTypeName("Deluxe");
        dto.setNightlyRate(new BigDecimal("250.00"));
        dto.setMaxOccupancy(3);
        dto.setDescription("Deluxe sea view");

        RoomType type = mapper.toModel(dto);

        assertEquals("Deluxe", type.getTypeName(),
                "Type name should match the DTO");
        assertEquals(new BigDecimal("250.00"), type.getNightlyRate(),
                "Nightly rate should match the DTO");
        assertEquals(3, type.getMaxOccupancy(),
                "Max occupancy should match the DTO");
        assertEquals("Deluxe sea view", type.getDescription(),
                "Description should match the DTO");
    }

    @Test
    public void testUpdateModel_OverridesFieldsFromDto() {
        RoomType type = new RoomType();
        type.setTypeName("Standard");
        type.setNightlyRate(new BigDecimal("100.00"));
        type.setMaxOccupancy(2);
        type.setDescription("Old description");

        RoomTypeRequestDTO dto = new RoomTypeRequestDTO();
        dto.setTypeName("Standard Updated");
        dto.setNightlyRate(new BigDecimal("120.00"));
        dto.setMaxOccupancy(3);
        dto.setDescription("New description");

        mapper.updateModel(type, dto);

        assertEquals("Standard Updated", type.getTypeName(),
                "Type name should be updated from DTO");
        assertEquals(new BigDecimal("120.00"), type.getNightlyRate(),
                "Nightly rate should be updated from DTO");
        assertEquals(3, type.getMaxOccupancy(),
                "Max occupancy should be updated from DTO");
        assertEquals("New description", type.getDescription(),
                "Description should be updated from DTO");
    }

    @Test
    public void testToResponseDTO_MapsFieldsCorrectly() {
        RoomType type = new RoomType();
        type.setId(7L);
        type.setTypeName("Suite");
        type.setNightlyRate(new BigDecimal("500.00"));
        type.setMaxOccupancy(4);
        type.setDescription("Luxury suite");

        RoomTypeResponseDTO dto = mapper.toResponseDTO(type);

        assertNotNull(dto, "DTO should not be null");
        assertEquals(7L, dto.getId(),
                "ID should match the RoomType model");
        assertEquals("Suite", dto.getTypeName(),
                "Type name should match the RoomType model");
        assertEquals(new BigDecimal("500.00"), dto.getNightlyRate(),
                "Nightly rate should match the RoomType model");
        assertEquals(4, dto.getMaxOccupancy(),
                "Max occupancy should match the RoomType model");
        assertEquals("Luxury suite", dto.getDescription(),
                "Description should match the RoomType model");
    }
}