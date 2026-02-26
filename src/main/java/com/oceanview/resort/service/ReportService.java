package com.oceanview.resort.service;

import com.oceanview.resort.dao.ReservationDao;
import com.oceanview.resort.dto.ReportResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class ReportService {
    private final ReservationDao reservationDao;

    public ReportService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReportResponseDTO getSummary() {
        LocalDate today = LocalDate.now();
        ReportResponseDTO report = new ReportResponseDTO();
        report.setTotalReservations(reservationDao.findAll().size());
        report.setActiveReservations(reservationDao.countActiveOn(today));
        report.setCheckoutsNextSevenDays(reservationDao.countCheckoutsBetween(today, today.plusDays(7)));
        report.setEstimatedRevenue(BigDecimal.valueOf(reservationDao.sumEstimatedRevenue()));
        report.setMostPopularRoomType(reservationDao.findMostPopularRoomType());
        return report;
    }

    /**
     * Weekly (last 7 days) counts of reservations grouped by room type.
     * Uses reservation created_at date for the time window.
     */
    public Map<String, Integer> getWeeklyRoomTypeCounts() {
        LocalDate endExclusive = LocalDate.now().plusDays(1); // include today
        LocalDate startInclusive = endExclusive.minusDays(7);
        return reservationDao.countRoomTypeReservationsCreatedBetween(startInclusive, endExclusive);
    }
}
