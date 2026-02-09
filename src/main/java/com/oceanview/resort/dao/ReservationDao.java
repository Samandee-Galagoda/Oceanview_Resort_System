package com.oceanview.resort.dao;

import com.oceanview.resort.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDao {
    boolean existsByReservationNumber(String reservationNumber);

    void create(Reservation reservation);

    Reservation findByReservationNumber(String reservationNumber);

    List<Reservation> findAll();

    int countActiveOn(LocalDate date);

    int countCheckoutsBetween(LocalDate startDate, LocalDate endDate);

    String findMostPopularRoomType();

    double sumEstimatedRevenue();
}
