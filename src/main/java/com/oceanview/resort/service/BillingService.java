package com.oceanview.resort.service;

import com.oceanview.resort.dao.BillingDao;
import com.oceanview.resort.dao.ReservationDao;
import com.oceanview.resort.dao.RoomTypeDao;
import com.oceanview.resort.dto.BillResponseDTO;
import com.oceanview.resort.model.Billing;
import com.oceanview.resort.model.Reservation;
import com.oceanview.resort.model.RoomType;
import com.oceanview.resort.pricing.BillingStrategy;
import com.oceanview.resort.pricing.DeluxeBillingStrategy;
import com.oceanview.resort.pricing.FamilyBillingStrategy;
import com.oceanview.resort.pricing.StandardBillingStrategy;
import com.oceanview.resort.pricing.SuiteBillingStrategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class BillingService {
    private final ReservationDao reservationDao;
    private final RoomTypeDao roomTypeDao;
    private final BillingDao billingDao;
    private final Map<String, BillingStrategy> strategyMap = new HashMap<>();

    public BillingService(ReservationDao reservationDao, RoomTypeDao roomTypeDao) {
        this(reservationDao, roomTypeDao, null);
    }

    public BillingService(ReservationDao reservationDao, RoomTypeDao roomTypeDao, BillingDao billingDao) {
        this.reservationDao = reservationDao;
        this.roomTypeDao = roomTypeDao;
        this.billingDao = billingDao;
        registerStrategy(new StandardBillingStrategy());
        registerStrategy(new DeluxeBillingStrategy());
        registerStrategy(new SuiteBillingStrategy());
        registerStrategy(new FamilyBillingStrategy());
    }

    private void registerStrategy(BillingStrategy strategy) {
        strategyMap.put(strategy.getRoomType(), strategy);
    }

    public BillResponseDTO generateBill(String reservationNumber) {
        if (reservationNumber == null || reservationNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Reservation number is required");
        }
        Reservation reservation = reservationDao.findByReservationNumber(reservationNumber.trim());
        if (reservation == null) {
            return null;
        }
        RoomType roomType = roomTypeDao.findByName(reservation.getRoomType());
        BigDecimal baseRate = roomType == null ? null : roomType.getNightlyRate();
        if (baseRate == null) {
            throw new IllegalStateException("Room rate not found for " + reservation.getRoomType());
        }

        long nights = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
        BillingStrategy strategy = strategyMap.getOrDefault(reservation.getRoomType(), new StandardBillingStrategy());
        BigDecimal total = strategy.calculateTotal(nights, baseRate);

        // Persist billing record if DAO is available
        if (billingDao != null) {
            Billing billing = new Billing();
            billing.setReservationId(reservation.getId());
            billing.setReservationNumber(reservation.getReservationNumber());
            billing.setIssuedAt(LocalDateTime.now());
            billing.setNights(nights);
            billing.setNightlyRate(baseRate);
            billing.setTotalAmount(total);
            billing.setPaymentStatus("UNPAID");
            billing.setPaymentMethod(null);
            // Simple invoice number scheme based on reservation number
            billing.setInvoiceNumber("INV-" + reservation.getReservationNumber());
            billingDao.create(billing);
        }

        BillResponseDTO dto = new BillResponseDTO();
        dto.setReservationNumber(reservation.getReservationNumber());
        dto.setRoomType(reservation.getRoomType());
        dto.setNights(nights);
        dto.setNightlyRate(baseRate);
        dto.setTotalAmount(total);
        return dto;
    }
}
