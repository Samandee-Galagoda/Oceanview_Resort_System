package dtotest;

import com.oceanview.resort.dto.BillResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD GREEN tests for BillResponseDTO.
 */
public class BillResponseDtoTest {

    private BillResponseDTO dto;

    @BeforeEach
    public void setup() {
        dto = new BillResponseDTO();
    }

    /**
     * reservationNumber and roomType getters should return values set by setters.
     */
    @Test
    public void testReservationNumberAndRoomType_SetAndGet() {
        dto.setReservationNumber("RES200");
        dto.setRoomType("Deluxe");

        String reservationNumber = dto.getReservationNumber();
        String roomType = dto.getRoomType();

        assertEquals("RES200", reservationNumber,
                "reservationNumber should match the value set by setReservationNumber");
        assertEquals("Deluxe", roomType,
                "roomType should match the value set by setRoomType");
    }

    /**
     * nights, nightlyRate, and totalAmount getters should return values set by setters.
     */
    @Test
    public void testNightsNightlyRateAndTotalAmount_SetAndGet() {
        dto.setNights(3L);
        dto.setNightlyRate(new BigDecimal("150.00"));
        dto.setTotalAmount(new BigDecimal("450.00"));

        long nights = dto.getNights();
        BigDecimal nightlyRate = dto.getNightlyRate();
        BigDecimal totalAmount = dto.getTotalAmount();

        assertEquals(3L, nights,
                "nights should match the value set by setNights");
        assertEquals(new BigDecimal("150.00"), nightlyRate,
                "nightlyRate should match the value set by setNightlyRate");
        assertEquals(new BigDecimal("450.00"), totalAmount,
                "totalAmount should match the value set by setTotalAmount");
    }
}