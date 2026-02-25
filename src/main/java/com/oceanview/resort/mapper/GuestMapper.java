package com.oceanview.resort.mapper;

import com.oceanview.resort.dto.GuestDTO;
import com.oceanview.resort.model.Guest;

/**
 * Mapper for Guest <-> GuestDTO.
 */
public class GuestMapper {

    public GuestDTO toDTO(Guest guest) {
        if (guest == null) {
            return null;
        }
        GuestDTO dto = new GuestDTO();
        dto.setId(guest.getId());
        dto.setFullName(guest.getFullName());
        dto.setAddress(guest.getAddress());
        dto.setContactNumber(guest.getContactNumber());
        return dto;
    }

    public Guest toModel(GuestDTO dto) {
        if (dto == null) {
            return null;
        }
        Guest guest = new Guest();
        guest.setId(dto.getId());
        guest.setFullName(dto.getFullName());
        guest.setAddress(dto.getAddress());
        guest.setContactNumber(dto.getContactNumber());
        return guest;
    }
}

