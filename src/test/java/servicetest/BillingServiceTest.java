package servicetest;

import com.oceanview.resort.dao.ReservationDao;
import com.oceanview.resort.dao.RoomTypeDao;
import com.oceanview.resort.dto.BillResponseDTO;
import com.oceanview.resort.model.Reservation;
import com.oceanview.resort.model.RoomType;
import com.oceanview.resort.service.BillingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BillingServiceTest {

    private BillingService billingService;
    private ReservationDao reservationDao;
    private RoomTypeDao roomTypeDao;

    @BeforeEach
    public void setup() {
        reservationDao = mock(ReservationDao.class);
        roomTypeDao = mock(RoomTypeDao.class);
        billingService = new BillingService(reservationDao, roomTypeDao);
    }

    /**
     * TDD pass test 1: generateBill should calculate total as nights * rate (using the appropriate strategy).
     * For a Standard room with 3 nights at 100.00/night, total should be 300.00.
     */
    @Test
    public void testGenerateBill_ForStandardRoom_ComputesTotal() {
        // Arrange
        String reservationNumber = "RES100";
        Reservation reservation = new Reservation();
        reservation.setReservationNumber(reservationNumber);
        reservation.setRoomType("Standard");
        reservation.setCheckInDate(LocalDate.of(2025, 6, 1));
        reservation.setCheckOutDate(LocalDate.of(2025, 6, 4)); // 3 nights

        when(reservationDao.findByReservationNumber(reservationNumber)).thenReturn(reservation);
        RoomType roomType = new RoomType();
        roomType.setTypeName("Standard");
        roomType.setNightlyRate(new BigDecimal("100.00"));
        when(roomTypeDao.findByName("Standard")).thenReturn(roomType);

        // Act
        BillResponseDTO bill = billingService.generateBill(reservationNumber);

        // Assert - now expect correct total (3 * 100.00 = 300.00)
        assertEquals(new BigDecimal("300.00"), bill.getTotalAmount(),
                "Total amount should be nights * rate for Standard room");
    }

    /**
     * TDD pass test 2: generateBill with blank reservation number should throw IllegalArgumentException.
     */
    @Test
    public void testGenerateBill_WithBlankReservationNumber_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> billingService.generateBill("   "),
                "generateBill with blank reservation number should throw IllegalArgumentException");
    }
    @Test
    public void testGenerateBill_ExistingReservation_ReturnsBill() {
        Reservation r = new Reservation();
        r.setReservationNumber("RES500");
        r.setRoomType("Standard");
        r.setCheckInDate(LocalDate.of(2025, 9, 1));
        r.setCheckOutDate(LocalDate.of(2025, 9, 3));

        when(reservationDao.findByReservationNumber("RES500")).thenReturn(r);
        RoomType type = new RoomType();
        type.setTypeName("Standard");
        type.setNightlyRate(new BigDecimal("100.00"));
        when(roomTypeDao.findByName("Standard")).thenReturn(type);

        BillResponseDTO dto = billingService.generateBill("RES500");

        assertNotNull(dto, "generateBill should return non-null for valid reservation");
        assertEquals("RES500", dto.getReservationNumber());
        assertEquals(2L, dto.getNights());
        assertEquals(new BigDecimal("100.00"), dto.getNightlyRate());
        assertEquals(new BigDecimal("200.00"), dto.getTotalAmount());
    }

    @Test
    public void testGenerateBill_NonExistingReservation_ReturnsNull() {
        when(reservationDao.findByReservationNumber("MISSING")).thenReturn(null);

        BillResponseDTO dto = billingService.generateBill("MISSING");

        assertNull(dto, "generateBill for non-existing reservation should return null");
    }
}

