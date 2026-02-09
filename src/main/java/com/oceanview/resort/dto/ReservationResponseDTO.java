package com.oceanview.resort.dto;

public class ReservationResponseDTO {
    private String reservationNumber;
    private String guestName;
    private String address;
    private String contactNumber;
    private String roomType;
    private String checkInDate;
    private String checkOutDate;
    private String createdAt;

    private ReservationResponseDTO() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public static class Builder {
        private final ReservationResponseDTO dto = new ReservationResponseDTO();

        public Builder reservationNumber(String reservationNumber) {
            dto.reservationNumber = reservationNumber;
            return this;
        }

        public Builder guestName(String guestName) {
            dto.guestName = guestName;
            return this;
        }

        public Builder address(String address) {
            dto.address = address;
            return this;
        }

        public Builder contactNumber(String contactNumber) {
            dto.contactNumber = contactNumber;
            return this;
        }

        public Builder roomType(String roomType) {
            dto.roomType = roomType;
            return this;
        }

        public Builder checkInDate(String checkInDate) {
            dto.checkInDate = checkInDate;
            return this;
        }

        public Builder checkOutDate(String checkOutDate) {
            dto.checkOutDate = checkOutDate;
            return this;
        }

        public Builder createdAt(String createdAt) {
            dto.createdAt = createdAt;
            return this;
        }

        public ReservationResponseDTO build() {
            return dto;
        }
    }
}
