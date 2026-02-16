package modeltest;

import com.oceanview.resort.model.Billing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD tests for Billing model.
 * Covers failure scenarios: null values, edge cases, and business logic failures.
 */
public class BillingTest {

    private Billing billing;

    @BeforeEach
    public void setup() {
        billing = new Billing();
    }

    /**
     * TDD failure test 1: getId returns default value 0 when not set.
     */
    @Test
    public void testGetId_WhenNotSet_ReturnsZero() {
        long id = billing.getId();

        assertEquals(0L, id, "getId when not set should return 0");
    }

    /**
     * TDD failure test 2: getInvoiceNumber returns null when not set.
     */
    @Test
    public void testGetInvoiceNumber_WhenNotSet_ReturnsNull() {
        String invoiceNumber = billing.getInvoiceNumber();

        assertNull(invoiceNumber, "getInvoiceNumber when not set should return null");
    }

    /**
     * TDD failure test 3: setInvoiceNumber with null value sets null.
     */
    @Test
    public void testSetInvoiceNumber_WithNull_SetsNull() {
        billing.setInvoiceNumber(null);

        assertNull(billing.getInvoiceNumber(), "setInvoiceNumber with null should set null");
    }

    /**
     * TDD failure test 4: setInvoiceNumber with empty string sets empty string.
     */
    @Test
    public void testSetInvoiceNumber_WithEmptyString_SetsEmptyString() {
        billing.setInvoiceNumber("");

        assertEquals("", billing.getInvoiceNumber(), "setInvoiceNumber with empty string should set empty string");
    }

    /**
     * TDD failure test 5: getReservationId returns default value 0 when not set.
     */
    @Test
    public void testGetReservationId_WhenNotSet_ReturnsZero() {
        long reservationId = billing.getReservationId();

        assertEquals(0L, reservationId, "getReservationId when not set should return 0");
    }

    /**
     * TDD failure test 6: setReservationId with negative value sets negative value.
     */
    @Test
    public void testSetReservationId_WithNegativeValue_SetsNegativeValue() {
        billing.setReservationId(-1L);

        assertEquals(-1L, billing.getReservationId(), "setReservationId with negative value should set negative value");
    }

    /**
     * TDD failure test 7: getReservationNumber returns null when not set.
     */
    @Test
    public void testGetReservationNumber_WhenNotSet_ReturnsNull() {
        String reservationNumber = billing.getReservationNumber();

        assertNull(reservationNumber, "getReservationNumber when not set should return null");
    }

    /**
     * TDD failure test 8: setReservationNumber with null value sets null.
     */
    @Test
    public void testSetReservationNumber_WithNull_SetsNull() {
        billing.setReservationNumber(null);

        assertNull(billing.getReservationNumber(), "setReservationNumber with null should set null");
    }

    /**
     * TDD failure test 9: getIssuedAt returns null when not set.
     */
    @Test
    public void testGetIssuedAt_WhenNotSet_ReturnsNull() {
        LocalDateTime issuedAt = billing.getIssuedAt();

        assertNull(issuedAt, "getIssuedAt when not set should return null");
    }

    /**
     * TDD failure test 10: setIssuedAt with null value sets null.
     */
    @Test
    public void testSetIssuedAt_WithNull_SetsNull() {
        billing.setIssuedAt(null);

        assertNull(billing.getIssuedAt(), "setIssuedAt with null should set null");
    }

    /**
     * TDD failure test 11: getNights returns default value 0 when not set.
     */
    @Test
    public void testGetNights_WhenNotSet_ReturnsZero() {
        long nights = billing.getNights();

        assertEquals(0L, nights, "getNights when not set should return 0");
    }

    /**
     * TDD failure test 12: setNights with negative value sets negative value.
     */
    @Test
    public void testSetNights_WithNegativeValue_SetsNegativeValue() {
        billing.setNights(-5L);

        assertEquals(-5L, billing.getNights(), "setNights with negative value should set negative value");
    }

    /**
     * TDD failure test 13: getNightlyRate returns null when not set.
     */
    @Test
    public void testGetNightlyRate_WhenNotSet_ReturnsNull() {
        BigDecimal nightlyRate = billing.getNightlyRate();

        assertNull(nightlyRate, "getNightlyRate when not set should return null");
    }

    /**
     * TDD failure test 14: setNightlyRate with null value sets null.
     */
    @Test
    public void testSetNightlyRate_WithNull_SetsNull() {
        billing.setNightlyRate(null);

        assertNull(billing.getNightlyRate(), "setNightlyRate with null should set null");
    }

    /**
     * TDD failure test 15: setNightlyRate with negative value sets negative value.
     */
    @Test
    public void testSetNightlyRate_WithNegativeValue_SetsNegativeValue() {
        BigDecimal negativeRate = new BigDecimal("-100.00");
        billing.setNightlyRate(negativeRate);

        assertEquals(negativeRate, billing.getNightlyRate(), "setNightlyRate with negative value should set negative value");
    }

    /**
     * TDD failure test 16: getTotalAmount returns null when not set.
     */
    @Test
    public void testGetTotalAmount_WhenNotSet_ReturnsNull() {
        BigDecimal totalAmount = billing.getTotalAmount();

        assertNull(totalAmount, "getTotalAmount when not set should return null");
    }

    /**
     * TDD failure test 17: setTotalAmount with null value sets null.
     */
    @Test
    public void testSetTotalAmount_WithNull_SetsNull() {
        billing.setTotalAmount(null);

        assertNull(billing.getTotalAmount(), "setTotalAmount with null should set null");
    }

    /**
     * TDD failure test 18: getPaymentStatus returns null when not set.
     */
    @Test
    public void testGetPaymentStatus_WhenNotSet_ReturnsNull() {
        String paymentStatus = billing.getPaymentStatus();

        assertNull(paymentStatus, "getPaymentStatus when not set should return null");
    }

    /**
     * TDD failure test 19: setPaymentStatus with null value sets null.
     */
    @Test
    public void testSetPaymentStatus_WithNull_SetsNull() {
        billing.setPaymentStatus(null);

        assertNull(billing.getPaymentStatus(), "setPaymentStatus with null should set null");
    }

    /**
     * TDD failure test 20: getPaymentMethod returns null when not set.
     */
    @Test
    public void testGetPaymentMethod_WhenNotSet_ReturnsNull() {
        String paymentMethod = billing.getPaymentMethod();

        assertNull(paymentMethod, "getPaymentMethod when not set should return null");
    }

    /**
     * TDD failure test 21: setPaymentMethod with null value sets null.
     */
    @Test
    public void testSetPaymentMethod_WithNull_SetsNull() {
        billing.setPaymentMethod(null);

        assertNull(billing.getPaymentMethod(), "setPaymentMethod with null should set null");
    }

    /**
     * TDD failure test 22: calculateTotal with null nightlyRate returns null.
     */
    @Test
    public void testCalculateTotal_WithNullNightlyRate_ReturnsNull() {
        billing.setNights(3L);
        billing.setNightlyRate(null);

        BigDecimal result = billing.calculateTotal();

        assertNull(result, "calculateTotal with null nightlyRate should return null");
    }

    /**
     * TDD failure test 23: calculateTotal with zero nights returns zero.
     */
    @Test
    public void testCalculateTotal_WithZeroNights_ReturnsZero() {
        billing.setNights(0L);
        billing.setNightlyRate(new BigDecimal("100.00"));

        BigDecimal result = billing.calculateTotal();

        assertEquals(BigDecimal.ZERO, result, "calculateTotal with zero nights should return zero");
    }

    /**
     * TDD failure test 24: calculateTotal with negative nights returns negative result.
     */
    @Test
    public void testCalculateTotal_WithNegativeNights_ReturnsNegativeResult() {
        billing.setNights(-2L);
        billing.setNightlyRate(new BigDecimal("100.00"));

        BigDecimal result = billing.calculateTotal();

        assertEquals(new BigDecimal("-200.00"), result, "calculateTotal with negative nights should return negative result");
    }

    /**
     * TDD failure test 25: calculateTotal with zero nightlyRate returns zero.
     */
    @Test
    public void testCalculateTotal_WithZeroNightlyRate_ReturnsZero() {
        billing.setNights(3L);
        billing.setNightlyRate(BigDecimal.ZERO);

        BigDecimal result = billing.calculateTotal();

        assertEquals(BigDecimal.ZERO, result, "calculateTotal with zero nightlyRate should return zero");
    }

    /**
     * TDD pass test: getId and setId work correctly.
     */
    @Test
    public void testId_SetAndGet() {
        billing.setId(100L);

        assertEquals(100L, billing.getId(), "getId should return value set by setId");
    }

    /**
     * TDD pass test: calculateTotal with valid values returns correct total.
     */
    @Test
    public void testCalculateTotal_WithValidValues_ReturnsCorrectTotal() {
        billing.setNights(3L);
        billing.setNightlyRate(new BigDecimal("150.00"));

        BigDecimal result = billing.calculateTotal();

        assertEquals(new BigDecimal("450.00"), result, "calculateTotal should return nights * nightlyRate");
    }
}
