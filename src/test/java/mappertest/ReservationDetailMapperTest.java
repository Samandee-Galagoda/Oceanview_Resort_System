package mappertest;

import com.oceanview.resort.mapper.ReservationDetailMapper;
import com.oceanview.resort.model.ReservationDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationDetailMapperTest {

    private ReservationDetailMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new ReservationDetailMapper();
    }

    @Test
    public void testCreateDefaultForReservation_SetsDefaultsCorrectly() {
        long reservationId = 42L;

        ReservationDetail detail = mapper.createDefaultForReservation(reservationId);

        assertEquals(reservationId, detail.getReservationId(),
                "Reservation id should be set from the argument");
        assertEquals(1, detail.getAdults(),
                "Adults should default to 1");
        assertEquals(0, detail.getChildren(),
                "Children should default to 0");
    }
}