package com.oceanview.resort.mapper;

import com.oceanview.resort.model.ReservationDetail;

/**
 * Mapper for ReservationDetail – currently simple pass-through.
 * Extend with DTOs when you expose reservation details via API.
 */
public class ReservationDetailMapper {

    public ReservationDetail createDefaultForReservation(long reservationId) {
        ReservationDetail detail = new ReservationDetail();
        detail.setReservationId(reservationId);
        detail.setAdults(1);
        detail.setChildren(0);
        return detail;
    }
}

