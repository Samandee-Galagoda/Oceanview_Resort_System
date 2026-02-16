package com.oceanview.resort.service;

import com.oceanview.resort.dao.GuestDao;
import com.oceanview.resort.dao.ReservationDao;
import com.oceanview.resort.dao.ReservationDetailDao;
import com.oceanview.resort.dao.RoomTypeDao;
import com.oceanview.resort.dto.ReservationRequestDTO;
import com.oceanview.resort.dto.ReservationResponseDTO;
import com.oceanview.resort.mapper.ReservationMapper;
import com.oceanview.resort.model.Guest;
import com.oceanview.resort.model.Reservation;
import com.oceanview.resort.model.ReservationDetail;
import com.oceanview.resort.model.RoomType;
import com.oceanview.resort.mapper.ReservationDetailMapper;
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
    private final RoomTypeDao roomTypeDao;
    private final ReservationMapper reservationMapper;
    private final ReservationDetailDao reservationDetailDao;
    private final ReservationDetailMapper reservationDetailMapper;

    public ReservationService(ReservationDao reservationDao,
                              GuestDao guestDao,
                              RoomTypeDao roomTypeDao,
                              ReservationMapper reservationMapper) {
        this(reservationDao, guestDao, roomTypeDao, reservationMapper, null, null);
    }

    public ReservationService(ReservationDao reservationDao,
                              GuestDao guestDao,
                              RoomTypeDao roomTypeDao,
                              ReservationMapper reservationMapper,
                              ReservationDetailDao reservationDetailDao,
                              ReservationDetailMapper reservationDetailMapper) {
        this.reservationDao = reservationDao;
        this.guestDao = guestDao;
        this.roomTypeDao = roomTypeDao;
        this.reservationMapper = reservationMapper;
        this.reservationDetailDao = reservationDetailDao;
        this.reservationDetailMapper = reservationDetailMapper;
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

        RoomType type = null;
        if (request.getRoomType() != null) {
            type = roomTypeDao.findByName(request.getRoomType());
            if (type == null) {
                errors.add("Selected room type is not configured in the system");
            }
        }

        if (!ValidationUtil.isValidContact(request.getContactNumber())) {
            errors.add("Contact number format is invalid");
        }

        LocalDate checkIn = ValidationUtil.parseDate(request.getCheckInDate(), "Check-in date", errors);
        LocalDate checkOut = ValidationUtil.parseDate(request.getCheckOutDate(), "Check-out date", errors);

        if (checkIn != null && checkOut != null && !checkOut.isAfter(checkIn)) {
            errors.add("Check-out date must be after check-in date");
        }

        // Check availability for the chosen room type and dates
        if (type != null && checkIn != null && checkOut != null &&
                reservationDao.existsOverlappingReservation(type.getId(), checkIn, checkOut)) {
            errors.add("Selected room type is not available for the chosen dates");
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

        // Optionally create a default ReservationDetail record
        if (reservationDetailDao != null && reservationDetailMapper != null) {
            ReservationDetail detail = reservationDetailMapper.createDefaultForReservation(reservation.getId());
            reservationDetailDao.create(detail);
        }
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

    public void cancelReservation(String reservationNumber) {
        if (reservationNumber == null || reservationNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Reservation number is required");
        }
        boolean updated = reservationDao.cancelByReservationNumber(reservationNumber.trim());
        if (!updated) {
            throw new IllegalStateException("Reservation not found: " + reservationNumber);
        }
    }

    public List<ReservationResponseDTO> listReservations() {
        List<Reservation> reservations = reservationDao.findAll();
        List<ReservationResponseDTO> items = new ArrayList<>();
        for (Reservation r : reservations) {
            items.add(reservationMapper.toResponse(r));
        }
        return items;
    }

    public List<ReservationResponseDTO> searchReservationsByGuestName(String guestName) {
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name is required");
        }
        List<Reservation> reservations = reservationDao.findByGuestName(guestName.trim());
        List<ReservationResponseDTO> items = new ArrayList<>();
        for (Reservation r : reservations) {
            items.add(reservationMapper.toResponse(r));
        }
        return items;
    }

    private long resolveRoomTypeId(String roomTypeName) {
        RoomType type = roomTypeDao.findByName(roomTypeName);
        if (type == null) {
            throw new IllegalStateException("Room type not found: " + roomTypeName);
        }
        return type.getId();
    }
}
