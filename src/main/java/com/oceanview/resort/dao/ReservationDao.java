package com.oceanview.resort.dao;

import com.oceanview.resort.model.Reservation;

import java.time.LocalDate;
import java.util.Map;
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

    /**
     * Count reservations grouped by room type where created_at is within the given range.
     * Intended for weekly reporting graphs.
     *
     * @param startInclusive inclusive start date
     * @param endExclusive   exclusive end date
     */
    Map<String, Integer> countRoomTypeReservationsCreatedBetween(LocalDate startInclusive, LocalDate endExclusive);

    /**
     * Find reservations whose guest name matches (case-insensitive, contains).
     */
    List<Reservation> findByGuestName(String guestName);

    /**
     * Cancel (set status = 'CANCELLED') by reservation number.
     *
     * @return true if a row was updated.
     */
    boolean cancelByReservationNumber(String reservationNumber);

    /**
     * Check if there is any non-cancelled reservation for the same room type
     * that overlaps with the given date range.
     */
    boolean existsOverlappingReservation(long roomTypeId, LocalDate checkIn, LocalDate checkOut);
}
