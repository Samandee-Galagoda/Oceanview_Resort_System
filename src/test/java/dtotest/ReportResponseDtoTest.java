package dtotest;

import com.oceanview.resort.dto.ReportResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD GREEN tests for ReportResponseDTO.
 */
public class ReportResponseDtoTest {

    private ReportResponseDTO dto;

    @BeforeEach
    public void setup() {
        dto = new ReportResponseDTO();
    }

    @Test
    public void testCounts_SetAndGet() {
        dto.setTotalReservations(10);
        dto.setActiveReservations(3);
        dto.setCheckoutsNextSevenDays(2);

        assertEquals(10, dto.getTotalReservations(),
                "totalReservations should match setter");
        assertEquals(3, dto.getActiveReservations(),
                "activeReservations should match setter");
        assertEquals(2, dto.getCheckoutsNextSevenDays(),
                "checkoutsNextSevenDays should match setter");
    }

    @Test
    public void testRevenueAndMostPopularRoomType_SetAndGet() {
        dto.setEstimatedRevenue(BigDecimal.valueOf(1000));
        dto.setMostPopularRoomType("Standard");

        assertEquals(BigDecimal.valueOf(1000), dto.getEstimatedRevenue(),
                "estimatedRevenue should match setter");
        assertEquals("Standard", dto.getMostPopularRoomType(),
                "mostPopularRoomType should match setter");
    }
}

