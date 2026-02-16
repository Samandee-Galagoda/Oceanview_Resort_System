package mappertest;

import com.oceanview.resort.dto.BillResponseDTO;
import com.oceanview.resort.mapper.BillingMapper;
import com.oceanview.resort.model.Billing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BillingMapperTest {

    private BillingMapper billingMapper;

    @BeforeEach
    public void setup() {
        billingMapper = new BillingMapper();
    }

    @Test
    public void testToResponseDTO_MapsAmountsCorrectly() {
        Billing billing = new Billing();
        billing.setReservationNumber("RES100");
        billing.setNights(3L);
        billing.setNightlyRate(new BigDecimal("200.00"));
        billing.setTotalAmount(new BigDecimal("600.00"));

        BillResponseDTO dto = billingMapper.toResponseDTO(billing);

        assertEquals("RES100", dto.getReservationNumber(),
                "Reservation number should match the Billing model");
        assertEquals(3L, dto.getNights(),
                "Nights should match the Billing model");
        assertEquals(new BigDecimal("200.00"), dto.getNightlyRate(),
                "Nightly rate should match the Billing model");
        assertEquals(new BigDecimal("600.00"), dto.getTotalAmount(),
                "Total amount should match the Billing model");
    }

    @Test
    public void testToResponseDTO_SetsRoomTypeToNull() {
        Billing billing = new Billing();
        billing.setReservationNumber("RES200");
        billing.setNights(1L);
        billing.setNightlyRate(new BigDecimal("150.00"));
        billing.setTotalAmount(new BigDecimal("150.00"));

        BillResponseDTO dto = billingMapper.toResponseDTO(billing);

        // Mapper deliberately leaves roomType null, handled elsewhere
        assertNull(dto.getRoomType(),
                "Room type should be null because BillingMapper does not set it");
    }
}