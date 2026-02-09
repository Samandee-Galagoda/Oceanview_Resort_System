package com.oceanview.resort.service;

import com.oceanview.resort.dao.ReservationDao;
import com.oceanview.resort.dao.GuestDao;
import com.oceanview.resort.dao.RoomRateDao;
import com.oceanview.resort.dto.ReservationRequestDTO;
import com.oceanview.resort.dto.ReservationResponseDTO;
import com.oceanview.resort.mapper.ReservationMapper;
import com.oceanview.resort.model.Guest;
import com.oceanview.resort.model.Reservation;
import com.oceanview.resort.util.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReservationService {
    private static final Set<String> ROOM_TYPES = new HashSet<>(Arrays.asList("Standard", "Deluxe", "Suite", "Family"));

    private final ReservationDao reservationDao;
    private final GuestDao guestDao;
    private final RoomRateDao roomRateDao;
    private final ReservationMapper reservationMapper;

    public ReservationService(ReservationDao reservationDao, GuestDao guestDao, RoomRateDao roomRateDao, ReservationMapper reservationMapper) {
        this.reservationDao = reservationDao;
        this.guestDao = guestDao;
        this.roomRateDao = roomRateDao;
        this.reservationMapper = reservationMapper;
    }

    public ReservationResponseDTO addReservation(ReservationRequestDTO request) {
        List<String> errors = new ArrayList<>();
        ValidationUtil.requireNonBlank(request.getReservationNumber(), "Reservation number", errors);
        ValidationUtil.requireNonBlank(request.getGuestName(), "Guest name", errors);
        ValidationUtil.requireNonBlank(request.getAddress(), "Address", errors);
        ValidationUtil.requireNonBlank(request.getContactNumber(), "Contact number", errors);
        ValidationUtil.requireNonBlank(request.getRoomType(), "Room type", errors);

        if (request.getRoomType() != null && !ROOM_TYPES.contains(request.getRoomType())) {
            errors.add("Room type must be one of " + ROOM_TYPES);
        }

        if (request.getRoomType() != null && roomRateDao.findRateByRoomType(request.getRoomType()) == null) {
            errors.add("Selected room type is not configured in the system");
        }

        if (!ValidationUtil.isValidContact(request.getContactNumber())) {
            errors.add("Contact number format is invalid");
        }

        LocalDate checkIn = ValidationUtil.parseDate(request.getCheckInDate(), "Check-in date", errors);
        LocalDate checkOut = ValidationUtil.parseDate(request.getCheckOutDate(), "Check-out date", errors);

        if (checkIn != null && checkOut != null && !checkOut.isAfter(checkIn)) {
            errors.add("Check-out date must be after check-in date");
        }

        if (request.getReservationNumber() != null
                && !request.getReservationNumber().trim().isEmpty()
                && reservationDao.existsByReservationNumber(request.getReservationNumber().trim())) {
            errors.add("Reservation number already exists");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }

        Reservation reservation = reservationMapper.toModel(request);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setStatus("BOOKED");

        Guest guest = new Guest();
        guest.setFullName(reservation.getGuestName());
        guest.setAddress(reservation.getAddress());
        guest.setContactNumber(reservation.getContactNumber());
        guest.setCreatedAt(LocalDateTime.now());
        long guestId = guestDao.create(guest);
        reservation.setGuestId(guestId);

        reservation.setRoomTypeId(resolveRoomTypeId(reservation.getRoomType()));
        reservationDao.create(reservation);
        return reservationMapper.toResponse(reservation);
    }

    public ReservationResponseDTO getReservation(String reservationNumber) {
        if (reservationNumber == null || reservationNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Reservation number is required");
        }
        Reservation reservation = reservationDao.findByReservationNumber(reservationNumber.trim());
        if (reservation == null) {
            return null;
        }
        return reservationMapper.toResponse(reservation);
    }

    public List<ReservationResponseDTO> listReservations() {
        List<Reservation> reservations = reservationDao.findAll();
        List<ReservationResponseDTO> items = new ArrayList<>();
        for (Reservation r : reservations) {
            items.add(reservationMapper.toResponse(r));
        }
        return items;
    }

    private long resolveRoomTypeId(String roomTypeName) {
        Long id = roomRateDao.findRoomTypeIdByRoomType(roomTypeName);
        if (id == null) {
            throw new IllegalStateException("Room type not found: " + roomTypeName);
        }
        return id;
    }
}
