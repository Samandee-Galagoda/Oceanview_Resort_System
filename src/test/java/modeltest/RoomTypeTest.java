package modeltest;

import com.oceanview.resort.model.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD tests for RoomType model.
 * Covers failure scenarios: null values, edge cases, and invalid values.
 */
public class RoomTypeTest {

    private RoomType roomType;

    @BeforeEach
    public void setup() {
        roomType = new RoomType();
    }

    /**
     * TDD failure test 1: getId returns default value 0 when not set.
     */
    @Test
    public void testGetId_WhenNotSet_ReturnsZero() {
        long id = roomType.getId();

        assertEquals(0L, id, "getId when not set should return 0");
    }

    /**
     * TDD failure test 2: setId with negative value sets negative value.
     */
    @Test
    public void testSetId_WithNegativeValue_SetsNegativeValue() {
        roomType.setId(-1L);

        assertEquals(-1L, roomType.getId(), "setId with negative value should set negative value");
    }

    /**
     * TDD failure test 3: getTypeName returns null when not set.
     */
    @Test
    public void testGetTypeName_WhenNotSet_ReturnsNull() {
        String typeName = roomType.getTypeName();

        assertNull(typeName, "getTypeName when not set should return null");
    }

    /**
     * TDD failure test 4: setTypeName with null value sets null.
     */
    @Test
    public void testSetTypeName_WithNull_SetsNull() {
        roomType.setTypeName(null);

        assertNull(roomType.getTypeName(), "setTypeName with null should set null");
    }

    /**
     * TDD failure test 5: setTypeName with empty string sets empty string.
     */
    @Test
    public void testSetTypeName_WithEmptyString_SetsEmptyString() {
        roomType.setTypeName("");

        assertEquals("", roomType.getTypeName(), "setTypeName with empty string should set empty string");
    }

    /**
     * TDD failure test 6: setTypeName with blank string sets blank string.
     */
    @Test
    public void testSetTypeName_WithBlankString_SetsBlankString() {
        roomType.setTypeName("   ");

        assertEquals("   ", roomType.getTypeName(), "setTypeName with blank string should set blank string");
    }

    /**
     * TDD failure test 7: getNightlyRate returns null when not set.
     */
    @Test
    public void testGetNightlyRate_WhenNotSet_ReturnsNull() {
        BigDecimal nightlyRate = roomType.getNightlyRate();

        assertNull(nightlyRate, "getNightlyRate when not set should return null");
    }

    /**
     * TDD failure test 8: setNightlyRate with null value sets null.
     */
    @Test
    public void testSetNightlyRate_WithNull_SetsNull() {
        roomType.setNightlyRate(null);

        assertNull(roomType.getNightlyRate(), "setNightlyRate with null should set null");
    }

    /**
     * TDD failure test 9: setNightlyRate with negative value sets negative value.
     */
    @Test
    public void testSetNightlyRate_WithNegativeValue_SetsNegativeValue() {
        BigDecimal negativeRate = new BigDecimal("-50.00");
        roomType.setNightlyRate(negativeRate);

        assertEquals(negativeRate, roomType.getNightlyRate(), "setNightlyRate with negative value should set negative value");
    }

    /**
     * TDD failure test 10: setNightlyRate with zero sets zero.
     */
    @Test
    public void testSetNightlyRate_WithZero_SetsZero() {
        roomType.setNightlyRate(BigDecimal.ZERO);

        assertEquals(BigDecimal.ZERO, roomType.getNightlyRate(), "setNightlyRate with zero should set zero");
    }

    /**
     * TDD failure test 11: getMaxOccupancy returns default value 0 when not set.
     */
    @Test
    public void testGetMaxOccupancy_WhenNotSet_ReturnsZero() {
        int maxOccupancy = roomType.getMaxOccupancy();

        assertEquals(0, maxOccupancy, "getMaxOccupancy when not set should return 0");
    }

    /**
     * TDD failure test 12: setMaxOccupancy with negative value sets negative value.
     */
    @Test
    public void testSetMaxOccupancy_WithNegativeValue_SetsNegativeValue() {
        roomType.setMaxOccupancy(-2);

        assertEquals(-2, roomType.getMaxOccupancy(), "setMaxOccupancy with negative value should set negative value");
    }

    /**
     * TDD failure test 13: setMaxOccupancy with zero sets zero.
     */
    @Test
    public void testSetMaxOccupancy_WithZero_SetsZero() {
        roomType.setMaxOccupancy(0);

        assertEquals(0, roomType.getMaxOccupancy(), "setMaxOccupancy with zero should set zero");
    }

    /**
     * TDD failure test 14: getDescription returns null when not set.
     */
    @Test
    public void testGetDescription_WhenNotSet_ReturnsNull() {
        String description = roomType.getDescription();

        assertNull(description, "getDescription when not set should return null");
    }

    /**
     * TDD failure test 15: setDescription with null value sets null.
     */
    @Test
    public void testSetDescription_WithNull_SetsNull() {
        roomType.setDescription(null);

        assertNull(roomType.getDescription(), "setDescription with null should set null");
    }

    /**
     * TDD failure test 16: setDescription with empty string sets empty string.
     */
    @Test
    public void testSetDescription_WithEmptyString_SetsEmptyString() {
        roomType.setDescription("");

        assertEquals("", roomType.getDescription(), "setDescription with empty string should set empty string");
    }

    /**
     * TDD failure test 17: setDescription with blank string sets blank string.
     */
    @Test
    public void testSetDescription_WithBlankString_SetsBlankString() {
        roomType.setDescription("   ");

        assertEquals("   ", roomType.getDescription(), "setDescription with blank string should set blank string");
    }

    /**
     * TDD pass test: getId and setId work correctly.
     */
    @Test
    public void testId_SetAndGet() {
        roomType.setId(5L);

        assertEquals(5L, roomType.getId(), "getId should return value set by setId");
    }

    /**
     * TDD pass test: getTypeName and setTypeName work correctly.
     */
    @Test
    public void testTypeName_SetAndGet() {
        roomType.setTypeName("Deluxe");

        assertEquals("Deluxe", roomType.getTypeName(), "getTypeName should return value set by setTypeName");
    }

    /**
     * TDD pass test: getNightlyRate and setNightlyRate work correctly.
     */
    @Test
    public void testNightlyRate_SetAndGet() {
        BigDecimal rate = new BigDecimal("150.00");
        roomType.setNightlyRate(rate);

        assertEquals(rate, roomType.getNightlyRate(), "getNightlyRate should return value set by setNightlyRate");
    }

    /**
     * TDD pass test: getMaxOccupancy and setMaxOccupancy work correctly.
     */
    @Test
    public void testMaxOccupancy_SetAndGet() {
        roomType.setMaxOccupancy(4);

        assertEquals(4, roomType.getMaxOccupancy(), "getMaxOccupancy should return value set by setMaxOccupancy");
    }

    /**
     * TDD pass test: getDescription and setDescription work correctly.
     */
    @Test
    public void testDescription_SetAndGet() {
        roomType.setDescription("Spacious room with ocean view");

        assertEquals("Spacious room with ocean view", roomType.getDescription(), "getDescription should return value set by setDescription");
    }
}
