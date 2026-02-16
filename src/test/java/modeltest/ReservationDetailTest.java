package modeltest;

import com.oceanview.resort.model.ReservationDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD tests for ReservationDetail model.
 * Covers failure scenarios: null values, edge cases, and negative values.
 */
public class ReservationDetailTest {

    private ReservationDetail detail;

    @BeforeEach
    public void setup() {
        detail = new ReservationDetail();
    }

    /**
     * TDD failure test 1: getId returns default value 0 when not set.
     */
    @Test
    public void testGetId_WhenNotSet_ReturnsZero() {
        long id = detail.getId();

        assertEquals(0L, id, "getId when not set should return 0");
    }

    /**
     * TDD failure test 2: setId with negative value sets negative value.
     */
    @Test
    public void testSetId_WithNegativeValue_SetsNegativeValue() {
        detail.setId(-1L);

        assertEquals(-1L, detail.getId(), "setId with negative value should set negative value");
    }

    /**
     * TDD failure test 3: getReservationId returns default value 0 when not set.
     */
    @Test
    public void testGetReservationId_WhenNotSet_ReturnsZero() {
        long reservationId = detail.getReservationId();

        assertEquals(0L, reservationId, "getReservationId when not set should return 0");
    }

    /**
     * TDD failure test 4: setReservationId with negative value sets negative value.
     */
    @Test
    public void testSetReservationId_WithNegativeValue_SetsNegativeValue() {
        detail.setReservationId(-5L);

        assertEquals(-5L, detail.getReservationId(), "setReservationId with negative value should set negative value");
    }

    /**
     * TDD failure test 5: getAdults returns default value 0 when not set.
     */
    @Test
    public void testGetAdults_WhenNotSet_ReturnsZero() {
        int adults = detail.getAdults();

        assertEquals(0, adults, "getAdults when not set should return 0");
    }

    /**
     * TDD failure test 6: setAdults with negative value sets negative value.
     */
    @Test
    public void testSetAdults_WithNegativeValue_SetsNegativeValue() {
        detail.setAdults(-2);

        assertEquals(-2, detail.getAdults(), "setAdults with negative value should set negative value");
    }

    /**
     * TDD failure test 7: setAdults with zero sets zero.
     */
    @Test
    public void testSetAdults_WithZero_SetsZero() {
        detail.setAdults(0);

        assertEquals(0, detail.getAdults(), "setAdults with zero should set zero");
    }

    /**
     * TDD failure test 8: getChildren returns default value 0 when not set.
     */
    @Test
    public void testGetChildren_WhenNotSet_ReturnsZero() {
        int children = detail.getChildren();

        assertEquals(0, children, "getChildren when not set should return 0");
    }

    /**
     * TDD failure test 9: setChildren with negative value sets negative value.
     */
    @Test
    public void testSetChildren_WithNegativeValue_SetsNegativeValue() {
        detail.setChildren(-1);

        assertEquals(-1, detail.getChildren(), "setChildren with negative value should set negative value");
    }

    /**
     * TDD failure test 10: getSpecialRequests returns null when not set.
     */
    @Test
    public void testGetSpecialRequests_WhenNotSet_ReturnsNull() {
        String specialRequests = detail.getSpecialRequests();

        assertNull(specialRequests, "getSpecialRequests when not set should return null");
    }

    /**
     * TDD failure test 11: setSpecialRequests with null value sets null.
     */
    @Test
    public void testSetSpecialRequests_WithNull_SetsNull() {
        detail.setSpecialRequests(null);

        assertNull(detail.getSpecialRequests(), "setSpecialRequests with null should set null");
    }

    /**
     * TDD failure test 12: setSpecialRequests with empty string sets empty string.
     */
    @Test
    public void testSetSpecialRequests_WithEmptyString_SetsEmptyString() {
        detail.setSpecialRequests("");

        assertEquals("", detail.getSpecialRequests(), "setSpecialRequests with empty string should set empty string");
    }

    /**
     * TDD failure test 13: setSpecialRequests with blank string sets blank string.
     */
    @Test
    public void testSetSpecialRequests_WithBlankString_SetsBlankString() {
        detail.setSpecialRequests("   ");

        assertEquals("   ", detail.getSpecialRequests(), "setSpecialRequests with blank string should set blank string");
    }

    /**
     * TDD failure test 14: getNotes returns null when not set.
     */
    @Test
    public void testGetNotes_WhenNotSet_ReturnsNull() {
        String notes = detail.getNotes();

        assertNull(notes, "getNotes when not set should return null");
    }

    /**
     * TDD failure test 15: setNotes with null value sets null.
     */
    @Test
    public void testSetNotes_WithNull_SetsNull() {
        detail.setNotes(null);

        assertNull(detail.getNotes(), "setNotes with null should set null");
    }

    /**
     * TDD failure test 16: setNotes with empty string sets empty string.
     */
    @Test
    public void testSetNotes_WithEmptyString_SetsEmptyString() {
        detail.setNotes("");

        assertEquals("", detail.getNotes(), "setNotes with empty string should set empty string");
    }

    /**
     * TDD pass test: getId and setId work correctly.
     */
    @Test
    public void testId_SetAndGet() {
        detail.setId(10L);

        assertEquals(10L, detail.getId(), "getId should return value set by setId");
    }

    /**
     * TDD pass test: getReservationId and setReservationId work correctly.
     */
    @Test
    public void testReservationId_SetAndGet() {
        detail.setReservationId(20L);

        assertEquals(20L, detail.getReservationId(), "getReservationId should return value set by setReservationId");
    }

    /**
     * TDD pass test: getAdults and setAdults work correctly.
     */
    @Test
    public void testAdults_SetAndGet() {
        detail.setAdults(2);

        assertEquals(2, detail.getAdults(), "getAdults should return value set by setAdults");
    }

    /**
     * TDD pass test: getChildren and setChildren work correctly.
     */
    @Test
    public void testChildren_SetAndGet() {
        detail.setChildren(1);

        assertEquals(1, detail.getChildren(), "getChildren should return value set by setChildren");
    }

    /**
     * TDD pass test: getSpecialRequests and setSpecialRequests work correctly.
     */
    @Test
    public void testSpecialRequests_SetAndGet() {
        detail.setSpecialRequests("Late checkout");

        assertEquals("Late checkout", detail.getSpecialRequests(), "getSpecialRequests should return value set by setSpecialRequests");
    }

    /**
     * TDD pass test: getNotes and setNotes work correctly.
     */
    @Test
    public void testNotes_SetAndGet() {
        detail.setNotes("Guest prefers ground floor");

        assertEquals("Guest prefers ground floor", detail.getNotes(), "getNotes should return value set by setNotes");
    }
}
