package com.oceanview.resort.mapper;

import com.oceanview.resort.dto.BillResponseDTO;
import com.oceanview.resort.model.Billing;

/**
 * Mapper for Billing domain model and BillResponseDTO.
 */
public class BillingMapper {

    public BillResponseDTO toResponseDTO(Billing billing) {
        BillResponseDTO dto = new BillResponseDTO();
        dto.setReservationNumber(billing.getReservationNumber());
        dto.setRoomType(null); // Room type is handled by BillingService from Reservation
        dto.setNights(billing.getNights());
        dto.setNightlyRate(billing.getNightlyRate());
        dto.setTotalAmount(billing.getTotalAmount());
        return dto;
    }
}

