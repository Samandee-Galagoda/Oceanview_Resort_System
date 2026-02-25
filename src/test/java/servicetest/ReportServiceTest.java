package servicetest;

import com.oceanview.resort.dao.ReservationDao;
import com.oceanview.resort.dto.ReportResponseDTO;
import com.oceanview.resort.model.Reservation;
import com.oceanview.resort.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * TDD pass tests for ReportService.
 */
public class ReportServiceTest {

    private ReportService reportService;
    private ReservationDao reservationDao;

    @BeforeEach
    public void setup() {
        reservationDao = mock(ReservationDao.class);
        reportService = new ReportService(reservationDao);
    }

    @Test
    public void testGetSummary_PopulatesReportFromDao() {
        when(reservationDao.findAll()).thenReturn(
                Collections.singletonList(new Reservation())
        );
        when(reservationDao.countActiveOn(any())).thenReturn(1);
        when(reservationDao.countCheckoutsBetween(any(), any())).thenReturn(2);
        when(reservationDao.sumEstimatedRevenue()).thenReturn(500.0);
        when(reservationDao.findMostPopularRoomType()).thenReturn("Standard");

        ReportResponseDTO report = reportService.getSummary();

        assertEquals(1, report.getTotalReservations(),
                "Total reservations should match DAO findAll size");
        assertEquals(1, report.getActiveReservations());
        assertEquals(2, report.getCheckoutsNextSevenDays());
        assertEquals(BigDecimal.valueOf(500.0), report.getEstimatedRevenue());
        assertEquals("Standard", report.getMostPopularRoomType());
    }
}