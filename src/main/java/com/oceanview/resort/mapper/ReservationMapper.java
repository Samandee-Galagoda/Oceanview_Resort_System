package com.oceanview.resort.mapper;

import com.oceanview.resort.dto.ReservationRequestDTO;
import com.oceanview.resort.dto.ReservationResponseDTO;
import com.oceanview.resort.model.Reservation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReservationMapper {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    public Reservation toModel(ReservationRequestDTO dto) {
        Reservation reservation = new Reservation();
        reservation.setReservationNumber(dto.getReservationNumber());
        reservation.setGuestName(dto.getGuestName());
        reservation.setAddress(dto.getAddress());
        reservation.setContactNumber(dto.getContactNumber());
        reservation.setRoomType(dto.getRoomType());
        reservation.setCheckInDate(LocalDate.parse(dto.getCheckInDate(), DATE_FORMAT));
        reservation.setCheckOutDate(LocalDate.parse(dto.getCheckOutDate(), DATE_FORMAT));
        return reservation;
    }

    public ReservationResponseDTO toResponse(Reservation reservation) {
        return ReservationResponseDTO.builder()
                .reservationNumber(reservation.getReservationNumber())
                .guestName(reservation.getGuestName())
                .address(reservation.getAddress())
                .contactNumber(reservation.getContactNumber())
                .roomType(reservation.getRoomType())
                .checkInDate(reservation.getCheckInDate().format(DATE_FORMAT))
                .checkOutDate(reservation.getCheckOutDate().format(DATE_FORMAT))
                .createdAt(reservation.getCreatedAt() == null ? null : reservation.getCreatedAt().toString())
                .build();
    }
}
