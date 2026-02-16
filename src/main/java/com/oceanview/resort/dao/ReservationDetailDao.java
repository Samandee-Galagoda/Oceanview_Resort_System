package com.oceanview.resort.dao;

import com.oceanview.resort.model.ReservationDetail;

public interface ReservationDetailDao {

    void create(ReservationDetail detail);

    ReservationDetail findByReservationId(long reservationId);

    boolean deleteByReservationId(long reservationId);
}

