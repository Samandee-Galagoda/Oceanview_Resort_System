package servicetest;

import com.oceanview.resort.dao.RoomTypeDao;
import com.oceanview.resort.model.RoomType;
import com.oceanview.resort.service.RoomTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * TDD pass tests for RoomTypeService.
 */
public class RoomTypeServiceTest {

    private RoomTypeService roomTypeService;
    private RoomTypeDao roomTypeDao;

    @BeforeEach
    public void setup() {
        roomTypeDao = mock(RoomTypeDao.class);
        roomTypeService = new RoomTypeService(roomTypeDao);
    }

    @Test
    public void testList_ReturnsAllRoomTypes() {
        when(roomTypeDao.findAll()).thenReturn(Collections.emptyList());

        List<RoomType> types = roomTypeService.list();

        assertEquals(0, types.size(), "list should return all room types from DAO");
    }

    @Test
    public void testCreate_ValidData_Succeeds() {
        when(roomTypeDao.findByName("Standard")).thenReturn(null);

        assertDoesNotThrow(() ->
                roomTypeService.create("Standard", "100.00", "2", "desc"),
                "create with valid data should not throw");
    }

    @Test
    public void testUpdate_ExistingRoomType_Succeeds() {
        RoomType existing = new RoomType();
        existing.setId(5L);
        existing.setTypeName("Standard");
        existing.setNightlyRate(new BigDecimal("100.00"));
        existing.setMaxOccupancy(2);

        when(roomTypeDao.findById(5L)).thenReturn(existing);
        when(roomTypeDao.findByName("Standard Updated")).thenReturn(null);

        assertDoesNotThrow(() ->
                roomTypeService.update("5", "Standard Updated", "120.00", "3", "new"),
                "update for existing room type should not throw");
    }

    @Test
    public void testDelete_ExistingRoomType_Succeeds() {
        when(roomTypeDao.deleteById(7L)).thenReturn(true);

        assertDoesNotThrow(() -> roomTypeService.delete("7"),
                "delete for existing room type should not throw");
    }
}